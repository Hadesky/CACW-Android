package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.ChatPresenter;
import com.hadesky.cacw.presenter.ChatPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.ChatView;

import java.util.List;

public class ChatActivity extends BaseActivity implements ChatView
{


    private RecyclerView mRecyclerView;
    private  EditText mEdt;
    private   View mSend;
    private  ChatPresenter mPresenter;
    private   UserBean mReceiver;
    private   ChatAdapter mAdapter;
    private TextView mTitle;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_chat;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_chat);
        mEdt = (EditText) findViewById(R.id.edt_send);
        mSend = findViewById(R.id.v_send);

        mTitle = (TextView) findViewById(R.id.tv_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void setupView()
    {
        UserBean bean = (UserBean) getIntent().getSerializableExtra(IntentTag.TAG_USER_BEAN);
        if (bean==null)
        {
            finish();
            return;
        }
        mReceiver = bean;

        mTitle.setText(mReceiver.getNickName());

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMsg();
            }
        });


        mEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMsg();
                return true;
            }
        });

        mAdapter = new ChatAdapter(this, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter=  new ChatPresenterImpl(this,mReceiver,mAdapter);
        mPresenter.loadChatMessage();

    }


    private void sendMsg()
    {
        if (mEdt.getText().toString().trim().length()==0)
        {
            showToast("不能发送空内容");
            return;
        }
        mPresenter.send(mEdt.getText().toString());
        mEdt.setText("");
    }



    @Override
    public void showChatList(List<MessageBean> list)
    {
        mAdapter.setDatas(list);
    }


    @Override
    public void showProgress()
    {

    }

    @Override
    public void hideProgress()
    {

    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }
}
