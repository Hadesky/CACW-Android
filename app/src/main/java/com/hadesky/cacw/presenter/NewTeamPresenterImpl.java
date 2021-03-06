package com.hadesky.cacw.presenter;

import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.ui.view.NewTeamView;

import java.io.File;

import rx.Subscription;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public class NewTeamPresenterImpl implements NewTeamPresenter
{

    private TeamRepertory mTeamRepertory;
    private NewTeamView mView;
    private Subscription mSubscription;

    public NewTeamPresenterImpl(NewTeamView view)
    {
        mView = view;
        mTeamRepertory = TeamRepertory.getInstance();
    }

    @Override
    public void createTeam(String team, File avatar)
    {

        mView.showProgress();
        mSubscription = mTeamRepertory.createTeam(team,avatar).subscribe(new RxSubscriber<String>() {
            @Override
            public void _onError(String e)
            {
                mView.hideProgress();
                mView.showMsg(e);
            }

            @Override
            public void _onNext(String s)
            {
                mView.hideProgress();
                mView.showMsg("创建成功");
                mView.Close();
            }
        });
    }

    @Override
    public void cancel()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
