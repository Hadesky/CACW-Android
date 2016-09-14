package com.hadesky.cacw.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SelectMemberAdapter;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.SelectMemberPresenter;
import com.hadesky.cacw.presenter.SelectMemberPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.SelectTaskMemberView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectMemberActivity extends BaseActivity implements SelectTaskMemberView
{

    private RecyclerView mRecyclerView;
    private SelectMemberAdapter mAdapter;
    private List<UserBean> mCurrentUser;
    private ArrayList<UserBean> mNewUser = new ArrayList<>();
    private ArrayList<UserBean> mAllUser;
    private boolean mHasError = true;
    private SelectMemberPresenter mPresenter;
    private  AnimProgressDialog mProgressDialog ;
    private int mTaskId;
    private int mProjectId;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_select_member;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_members);
        mProgressDialog = new AnimProgressDialog(this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                if(mPresenter!=null)
                    mPresenter.onCancel();
            }
        },"加载中");
    }

    @Override
    public void setupView()
    {
        mTaskId = getIntent().getIntExtra(IntentTag.TAG_TASK_ID,-1);
        mProjectId = getIntent().getIntExtra(IntentTag.TAG_PROJECT_ID,-1);
        mCurrentUser = getIntent().getParcelableArrayListExtra(IntentTag.TAG_Task_MEMBER);

        if(mProjectId<0||mTaskId<0||mCurrentUser==null)
        {
            showToast("参数错误");
            return;
        }

        Map<Integer, Boolean> map = new HashMap<>();
        mAdapter = new SelectMemberAdapter(null, R.layout.list_item_select_members, map);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mPresenter = new SelectMemberPresenterImpl(this,mCurrentUser,mProjectId,mTaskId);
        mPresenter.getSelectableMember();
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

        Map<Integer, Boolean> map = mAdapter.getSelectMap();
        for(Map.Entry<Integer,Boolean> entry:map.entrySet())
        {
             if(entry.getValue())
             {
                 mNewUser.add(mAllUser.get(entry.getKey()));
             }
        }
        mPresenter.addTaskMember(mNewUser);
    }

    @Override
    public void showMembers(List<UserBean> userBeens)
    {
        mAllUser = (ArrayList<UserBean>) userBeens;
        mHasError = false;
        mAdapter.setDatas(userBeens);

    }

    @Override
    public void FinishAndClose()
    {

        Intent i = new Intent();
        if(mNewUser.size()>0)
            i.putParcelableArrayListExtra(IntentTag.TAG_Task_MEMBER, mNewUser);
        setResult(Activity.RESULT_OK,i);
        finish();

    }

    @Override
    public void showProgress()
    {
        mProgressDialog.show();
    }

    @Override
    public void hideProgress()
    {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }
}
