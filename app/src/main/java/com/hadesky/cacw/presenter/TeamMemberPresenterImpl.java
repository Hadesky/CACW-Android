package com.hadesky.cacw.presenter;

import android.util.Log;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.TeamMemberAdapter;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.view.TeamMemberView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * Created by MicroStudent on 2016/7/16.
 */

public class TeamMemberPresenterImpl implements TeamMemberPresenter {

    private static final String TAG = "TeamMemberPresenterImpl";

    private TeamMemberView mView;

    private Subscription mSubscriptions;

    private TeamMemberAdapter mAdapter;

    private List<UserBean> mUsers;

    private TeamBean mTeamBean;

    public TeamMemberPresenterImpl(TeamMemberView view) {
        mView = view;
        mTeamBean = view.getTeamBean();
    }

    @Override
    public void loadData() {
        Log.d(TAG, "loading data");
        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTeam", new BmobPointer(mTeamBean));
        query.include("mUser");
        mView.showProgress();

        mSubscriptions = query.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                mView.hideProgress();
                if (e==null) {
                    handleResult(list);
                }else {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestory() {
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }
        mView = null;
    }

    @Override
    public int getUserCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    private void handleResult(List<TeamMember> list) {
        List<UserBean> users = new ArrayList<>();
        for (TeamMember member : list) {
            users.add(member.getUser());
        }

        mUsers = users;

        mAdapter = new TeamMemberAdapter(users, R.layout.item_user);

        mView.setAdapter(mAdapter);
        mView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
    }
}
