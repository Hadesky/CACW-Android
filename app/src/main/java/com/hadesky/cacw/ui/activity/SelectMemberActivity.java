package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SelectMemberAdapter;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SelectMemberActivity extends BaseActivity
{

    private  RecyclerView mRecyclerView;
    private SelectMemberAdapter mAdapter;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_select_member;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_members);
    }

    @Override
    public void setupView()
    {
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<UserBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new UserBean("谢伟鹏",R.drawable.default_user_image));
        }
        mAdapter = new SelectMemberAdapter(this,list);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void onSelectAll(View v)
    {
        mAdapter.selectAll();
    }
    public void onReverse(View v)
    {
        mAdapter.reverse();
    }
    public void onOK(View v)
    {
        onBackPressed();
    }
}
