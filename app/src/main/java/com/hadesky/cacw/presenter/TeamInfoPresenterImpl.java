package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.ui.view.TeamInfoView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public class TeamInfoPresenterImpl implements TeamInfoPresenter
{

    private TeamInfoView mView;
    private TeamRepertory mTeamRepertory;
    private SubscriptionList mSubscriptionList = new SubscriptionList();


    int tid;

    public TeamInfoPresenterImpl(TeamInfoView view,int id)
    {
        mView = view;
        tid = id;
        mTeamRepertory = TeamRepertory.getInstance();
    }

    @Override
    public void getTeamInfo()
    {

        Subscription subscription  =  mTeamRepertory.getTeamInfo(tid)
                .subscribe(new RxSubscriber<TeamBean>() {
                    @Override
                    public void _onError(String e)
                    {
                        mView.hideProgress();
                        mView.showMsg(e);
                    }

                    @Override
                    public void _onNext(TeamBean teamBean)
                    {
                        mView.hideProgress();
                        mView.showInfo(teamBean);
                    }
                });
        mSubscriptionList.add(subscription);
    }


    @Override
    public void getTeamMembers()
    {
        Subscription subscription  =  mTeamRepertory.getTeamMember(tid,4,0,false)
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
                        mView.showMembers(members);
                    }
                });
        mSubscriptionList.add(subscription);
    }

    @Override
    public void modifySummary(String s)
    {
        Map<String, String> info = new HashMap<>();
        info.put("summary",s);
        Subscription subscription  =  mTeamRepertory.modifyTeamInfo(tid,info)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String e)
                    {
                        mView.hideProgress();
                        mView.showMsg(e);
                    }

                    @Override
                    public void _onNext(String teamBean)
                    {
                        mView.hideProgress();
                    }
                });
        mSubscriptionList.add(subscription);
    }

    @Override
    public void modifyNotice(String s)
    {
        Map<String, String> info = new HashMap<>();
        info.put("notice",s);
        mView.showProgress();
        Subscription subscription  =  mTeamRepertory.modifyTeamInfo(tid,info)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String e)
                    {
                        mView.hideProgress();
                        mView.showMsg(e);
                    }
                    @Override
                    public void _onNext(String teamBean)
                    {
                        mView.hideProgress();
                    }
                });
        mSubscriptionList.add(subscription);
    }

    @Override
    public void saveTeamIcon(File file)
    {
        mView.showProgress();
        Subscription s = mTeamRepertory.modifyTeamIcon(tid,file)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);

                    }

                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                    }
                });
        mSubscriptionList.add(s);
    }

    @Override
    public void onDestroy()
    {
        mSubscriptionList.unsubscribe();
    }


    @Override
    public void delCurrentTeam()
    {

    }

    @Override
    public void exitTeam()
    {

    }
}
