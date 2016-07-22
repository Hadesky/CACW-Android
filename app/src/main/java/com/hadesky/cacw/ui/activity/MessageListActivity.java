package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.RecyclerView;

import com.hadesky.cacw.R;

public class MessageListActivity extends BaseActivity
{


    RecyclerView mRecyclerView;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_message_list;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_message);
    }

    @Override
    public void setupView()
    {




    }
}
