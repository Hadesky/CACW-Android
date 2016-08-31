package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SelectMemberAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SelectMemberActivity extends BaseActivity
{

    private RecyclerView mRecyclerView;
    private SelectMemberAdapter mAdapter;
    private ProjectBean mProjectBean;
    private List<UserBean> mCurrentUser;
    private List<UserBean> mAllUser = new ArrayList<>();
    private boolean mHasError = true;
    private UserBean mUser;
    private  AnimProgressDialog mProgressDialog ;


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


    }

    private void getAllMember()
    {

    }

    private void getAllMember(TeamBean bean)
    {

    }

    private void setupAdapter()
    {
        mHasError = false;
        Map<Integer, Boolean> map = new HashMap<>();
        for(int i = 0; i < mAllUser.size(); i++)
        {
            UserBean bean = mAllUser.get(i);
            map.put(i, mCurrentUser.contains(bean));
        }

        mAdapter = new SelectMemberAdapter(mAllUser, R.layout.list_item_select_members, map);
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
        if (!mHasError)
        {
            Intent intent = new Intent();
            ArrayList<UserBean> list = new ArrayList<>();
            for(int i = 0; i < mAllUser.size(); i++)
            {
                if (mAdapter.getSelectMap().get(i))
                    list.add(mAllUser.get(i));
            }
            list.add(0,mUser);
            intent.putExtra(IntentTag.TAG_USER_BEAN_LIST, list);
            setResult(RESULT_OK, intent);

        } else
            setResult(RESULT_CANCELED);

        finish();
    }
}
