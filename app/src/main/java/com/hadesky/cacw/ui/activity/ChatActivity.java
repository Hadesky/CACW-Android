package com.hadesky.cacw.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.ChatPresenter;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.ChatView;

import java.util.List;

public class ChatActivity extends BaseActivity implements ChatView
{
    private RecyclerView mRecyclerView;
    private EditText mEdt;
    private FloatingActionButton mSendButton;
    private ChatPresenter mPresenter;
    private UserBean mReceiver;
    private ChatAdapter mAdapter;
    private TextView mTitle;
    private BroadcastReceiver mBroadcastReceiver;


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
        mSendButton = (FloatingActionButton) findViewById(R.id.v_send);

        mTitle = (TextView) findViewById(R.id.tv_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setupView()
    {
        UserBean bean = (UserBean) getIntent().getParcelableExtra(IntentTag.TAG_USER_BEAN);

        if (bean==null)
        {
           finish();
            return;
        }
        mReceiver = bean;
        mTitle.setText(bean.getNickName());
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMsg();
            }
        });
        mSendButton.hide();

        mEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    mSendButton.show();
                } else {
                    mSendButton.hide();
                }
            }
        });
        mEdt.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
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

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdt.getWindowToken(), 0);
                }
                return false;
            }
        });


        //mPresenter=  new ChatPresenterImpl(this,mReceiver,mAdapter);
        mAdapter.setPresenter(mPresenter);
        mPresenter.loadChatMessage();
        setupReciever();
    }

    private void setupReciever()
    {
        IntentFilter filter = new IntentFilter(IntentTag.ACTION_MSG_RECEIVE);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.e("tag", "收到广播");
                String id = intent.getStringExtra(IntentTag.TAG_USER_ID);
                if (id.equals(mReceiver.getId()))
                {
                    mPresenter.loadNewMsg();
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, filter);
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
    protected void onDestroy()
    {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(this).setMessage("确认清空此对话吗？").
                        setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteChat();
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
}
