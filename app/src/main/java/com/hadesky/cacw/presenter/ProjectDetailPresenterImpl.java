package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.network.ProjectRepertory;
import com.hadesky.cacw.ui.view.ProjectDetailView;

import rx.Subscription;

/**
 *  项目详情
 * Created by dzysg on 2016/9/6 0006.
 */
public class ProjectDetailPresenterImpl implements ProjectDetailPresenter
{


    private ProjectDetailView mView;
    private ProjectRepertory mProjectRepertory;
    private Subscription mSubscription;

    public ProjectDetailPresenterImpl(ProjectDetailView view)
    {
        mView = view;
        mProjectRepertory = ProjectRepertory.getInstance();
    }

    @Override
    public void loadProject(int pid)
    {
        mSubscription = mProjectRepertory.getProject(pid)
                .subscribe(new RxSubscriber<ProjectBean>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.showMsg(msg);
                    }

                    @Override
                    public void _onNext(ProjectBean bean)
                    {
                        mView.showProject(bean);
                    }
                });
    }

    @Override
    public void onCcancel()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
