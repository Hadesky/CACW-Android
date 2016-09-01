package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.ui.view.MyTeamView;

import java.util.List;

import rx.Subscription;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public class MyTeamPresenterImpl implements MyTeamPresenter
{
    MyTeamView mView;
    TeamRepertory mTeamRepertory;
    Subscription mSubscription;

    public MyTeamPresenterImpl(MyTeamView view)
    {
        mView = view;
        mTeamRepertory = TeamRepertory.getInstance();
    }

    @Override
    public void LoadAllTeams()
    {
        mView.showProgress();
        mSubscription  =  mTeamRepertory.getTeamList().subscribe(new RxSubscriber<List<TeamBean>>() {
            @Override
            public void _onError(Throwable e)
            {
                mView.hideProgress();
                mView.showMsg(e.getMessage());
            }

            @Override
            public void _onNext(List<TeamBean> list)
            {
                mView.hideProgress();
                mView.showTeamList(list);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
