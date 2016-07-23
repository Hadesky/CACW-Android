package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.RecyclerView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.ChatView;

import java.util.List;

public class ChatActivity extends BaseActivity implements ChatView
{


    RecyclerView mRecyclerView;


    @Override
    public int getLayoutId()
    {
        return R.layout.activity_chat;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_chat);
    }

    @Override
    public void setupView()
    {
        UserBean bean = (UserBean) getIntent().getSerializableExtra(IntentTag.TAG_USER_BEAN);
        if (bean==null)
            finish();



    }

    @Override
    public void showChatList(List<MessageBean> list)
    {

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
