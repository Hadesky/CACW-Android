package com.hadesky.cacw.presenter;

import android.util.Log;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.TeamMemberAdapter;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.TeamMemberView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * Created by MicroStudent on 2016/7/16.
 */

public class TeamMemberPresenterImpl implements TeamMemberPresenter
{

    private static final String TAG = "TeamMemberPresenterImpl";

    private TeamMemberView mView;

    private Subscription mSubscriptions;

    private TeamMemberAdapter mAdapter;

    private List<UserBean> mUsers;

    private List<TeamMember> mTeamMembers;

    private TeamBean mTeamBean;

    public TeamMemberPresenterImpl(TeamMemberView view)
    {
        mView = view;
        mTeamBean = view.getTeamBean();
    }

    @Override
    public void loadData()
    {
        Log.d(TAG, "loading data");
        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTeam", new BmobPointer(mTeamBean));
        query.include("mUser");
        mView.showProgress();

        mSubscriptions = query.findObjects(new FindListener<TeamMember>()
        {
            @Override
            public void done(List<TeamMember> list, BmobException e)
            {
                mView.hideProgress();
                if (e == null)
                {
                    handleResult(list);
                } else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if (mSubscriptions != null)
        {
            mSubscriptions.unsubscribe();
        }
        mView = null;
    }

    @Override
    public int getUserCount()
    {
        if (mUsers != null)
        {
            return mUsers.size();
        }
        return 0;
    }

    @Override
    public void deleteMember(final UserBean bean)
    {
        if (bean.equals(MyApp.getCurrentUser()))
        {
            mView.showMsg("不能删除自己");
            return;
        }

        mView.showProgress();
        TeamMember del=null;
        for(TeamMember tm : mTeamMembers)
        {
            if (tm.getUser().equals(bean))
            {
                del = tm;
                break;
            }
        }

        if (del!=null)
        {
            del.delete(new UpdateListener() {
                @Override
                public void done(BmobException e)
                {
                    if (e!=null)
                    {
                        mView.hideProgress();
                        mView.showMsg(e.getMessage());
                    }else
                    {
                        deleteTask(bean);
                    }
                }
            });
        }
    }

    //删除成员后删除相关的任务成员
    private void deleteTask(UserBean bean)
    {
        BmobQuery<TaskMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mUser", new BmobPointer(bean));
        query.include("mTask.mProjectBean.mTeam,mUser");
        query.findObjects(new FindListener<TaskMember>()
        {
            @Override
            public void done(List<TaskMember> list, BmobException e)
            {
                if (e != null)
                {
                    mView.showMsg(e.getMessage());
                    mView.hideProgress();
                }
                else
                {
                    List<BmobObject> members = new ArrayList<>();
                    for(TaskMember tm : list)
                    {
                        if (tm.getTask().getProjectBean().getTeam().getObjectId().equals(mTeamBean.getObjectId()))
                            members.add(tm);
                    }
                    BmobBatch batch = new BmobBatch();
                    batch.deleteBatch(members);
                    batch.doBatch(new QueryListListener<BatchResult>()
                    {
                        @Override
                        public void done(List<BatchResult> list, BmobException e)
                        {
                            mView.hideProgress();
                            if (e == null)
                            {
                                mView.showMsg("删除成功");
                                loadData();
                            } else
                                mView.showMsg(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public List<UserBean> getData()
    {
        return mUsers;
    }

    private void handleResult(List<TeamMember> list)
    {
        mTeamMembers = list;
        List<UserBean> users = new ArrayList<>();
        UserBean admin = null;
        for(TeamMember member : list)
        {
            users.add(member.getUser());
            if (member.getUser().equals(mTeamBean.getAdminUser()))
            {
                admin = member.getUser();
            }
        }

        mUsers = users;

        mAdapter = new TeamMemberAdapter(users, R.layout.item_user, admin);
        mAdapter.setPresenter(this);
        mView.setAdapter(mAdapter);
        mView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        mView.setData(users.toArray());
    }
}
