package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.model.UserRepertory;
import com.hadesky.cacw.ui.view.InvitePersonView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 *
 * Created by dzysg on 2016/9/13 0013.
 */
public class InvitePersonPresenterImpl implements InvitePersonPresenter
{


    private InvitePersonView mView;
    private TeamBean mTeamBean;
    private boolean mIsFinal;
    private String mSearchText;
    private Subscription mSubscription;
    private TeamRepertory mTeamRepertory;
    private UserRepertory mUserRepertory;
    private List<UserBean> mUsers;
    private int page = 1;
    private int pageSize = 5;

    public InvitePersonPresenterImpl(InvitePersonView view, TeamBean t)
    {
        mView = view;
        mTeamBean = t;
        mTeamRepertory = TeamRepertory.getInstance();
        mUserRepertory = UserRepertory.getInstance();
    }


    @Override
    public void inviteUser(String s, final int position)
    {
        mSubscription = mTeamRepertory.invitePerson(mTeamBean.getId(),mUsers.get(position).getId())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {

                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.onInviteSucceed(position);
                    }
                });
    }

    @Override
    public void search(String key)
    {
        doSearch(key);
    }

    private void doSearch(String key)
    {
        if(key.length()==0)
        {
            mView.showUser(new ArrayList<UserBean>(),true);
            return;
        }
        mSubscription = mUserRepertory.searchUser(key,pageSize,0,mTeamBean.getId())
                .subscribe(new RxSubscriber<List<UserBean>>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(List<UserBean> userBeen)
                    {
                        mUsers = userBeen;
                        mView.hideProgress();
                        mIsFinal = userBeen.size() < pageSize;
                        mView.showUser(userBeen,mIsFinal);
                    }
                });
    }

    @Override
    public void LoadNextPage()
    {

    }

    @Override
    public void onDestroy()
    {

    }
}
