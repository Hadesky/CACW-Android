package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SelectMemberAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class SelectMemberActivity extends BaseActivity
{

    private RecyclerView mRecyclerView;
    private SelectMemberAdapter mAdapter;
    private ProjectBean mProjectBean;
    private List<UserBean> mCurrentUser;
    private List<UserBean> mAllUser = new ArrayList<>();
    private boolean mHasError = false;
    private UserBean mUser;

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


        List<TaskMember> current = (List<TaskMember>) getIntent().getSerializableExtra(IntentTag.TAG_Task_MEMBER);
        ProjectBean pb = (ProjectBean) getIntent().getSerializableExtra(IntentTag.TAG_PROJECT_BEAN);
        if (current == null || pb == null)
        {
            finish();
            return;
        }
        mProjectBean = pb;
        mCurrentUser = new ArrayList<>();
        for(TaskMember tm : current)
        {
            mCurrentUser.add(tm.getUser());
        }


        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUser = MyApp.getCurrentUser();

        getAllMember();
    }

    private void getAllMember()
    {
        if (mProjectBean.getTeam() == null)
        {

            BmobQuery<ProjectBean> query = new BmobQuery<>();
            query.include("mTeam");
            query.getObject(mProjectBean.getObjectId(), new QueryListener<ProjectBean>()
            {
                @Override
                public void done(ProjectBean projectBean, BmobException e)
                {
                    if (e == null)
                    {
                        getAllMember(projectBean.getTeam());
                    } else
                    {
                        showToast(e.getMessage());
                        mHasError = true;
                    }
                }
            });
        } else
        {
            getAllMember(mProjectBean.getTeam());
        }
    }

    private void getAllMember(TeamBean bean)
    {
        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTeam", new BmobPointer(bean));
        query.addWhereNotEqualTo("mUser", new BmobPointer(mUser));
        query.include("mUser");
        query.findObjects(new FindListener<TeamMember>()
        {
            @Override
            public void done(List<TeamMember> list, BmobException e)
            {
                if (e == null)
                {
                    for(TeamMember tm : list)
                    {
                        if (!tm.getUser().getObjectId().equals(mUser.getObjectId()))
                            mAllUser.add(tm.getUser());
                    }
                    setupAdapter();
                } else
                {
                    showToast(e.getMessage());
                    mHasError = true;
                }
            }
        });
    }

    private void setupAdapter()
    {
        Map<Integer, Boolean> map = new HashMap<>();
        for(int i = 0; i < mAllUser.size(); i++)
        {
            UserBean bean = mAllUser.get(i);
            map.put(i, mCurrentUser.contains(bean));
        }

        mAdapter = new SelectMemberAdapter(mAllUser, R.layout.item_select_members, map);
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
