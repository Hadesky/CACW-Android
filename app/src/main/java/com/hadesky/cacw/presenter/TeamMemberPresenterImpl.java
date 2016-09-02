package com.hadesky.cacw.presenter;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.TeamMemberAdapter;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.ui.view.TeamMemberView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 *
 * Created by dzysg on 2016/9/2 0002.
 */
public class TeamMemberPresenterImpl implements TeamMemberPresenter
{

    TeamMemberView mView;
    TeamBean mTeam;
    private List<UserBean> mUsers;
    private TeamMemberAdapter mAdapter;
    private TeamRepertory mTeamRepertory;
    SubscriptionList mSubscriptionList = new SubscriptionList();


    public TeamMemberPresenterImpl(TeamMemberView view, TeamBean t)
    {
        mView = view;
        mTeam = t;
        mTeamRepertory = TeamRepertory.getInstance();
    }

    @Override
    public void loadMembers()
    {
        Subscription subscription  =  mTeamRepertory.getTeamMember(mTeam.getId(),null,0,true)
                .subscribe(new RxSubscriber<List<UserBean>>() {
                    @Override
                    public void _onError(String e)
                    {
                        mView.hideProgress();
                        mView.showMsg(e);
                    }

                    @Override
                    public void _onNext(List<UserBean> members)
                    {
                        mView.hideProgress();
                        handleResult(members);
                    }
                });
        mSubscriptionList.add(subscription);
    }

    @Override
    public void onDestroy()
    {
        mSubscriptionList.unsubscribe();
    }

    @Override
    public int getUserCount()
    {
        return 0;
    }

    @Override
    public void deleteMember(UserBean bean)
    {

    }

    private void handleResult(List<UserBean> list) {
        List<UserBean> users = new ArrayList<>();
        UserBean admin = null;
        for (UserBean member : list) {
            users.add(member);
            if (member.getId()==mTeam.getAdminId()) {
                admin = member;
            }
        }
        mUsers = users;
        mAdapter = new TeamMemberAdapter(users, R.layout.item_user, admin);
        mView.setAdapter(mAdapter);
        mView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        mView.setData(users.toArray());
    }

}
