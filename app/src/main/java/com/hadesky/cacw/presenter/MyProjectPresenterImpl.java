package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.model.network.ProjectRepertory;
import com.hadesky.cacw.ui.view.MyProjectView;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by dzysg on 2016/9/2 0002.
 */
public class MyProjectPresenterImpl implements MyProjectPresenter
{

    private MyProjectView mView;
    private TeamBean mTeamBean;
    private Subscription mSubscription;
    private TeamRepertory mTeamRepertory;
    private List<ProjectBean> mProjects;
    private ProjectRepertory mProjectRepertory;

    public MyProjectPresenterImpl(MyProjectView view, TeamBean t)
    {
        mView = view;
        mTeamBean = t;
        if (t != null)
            mTeamRepertory = TeamRepertory.getInstance();
        else
            mProjectRepertory = ProjectRepertory.getInstance();
    }

    @Override
    public void loadProject()
    {
        mView.showProgress();

        Observable<List<ProjectBean>> observable;
        if (mTeamRepertory != null)
        {
            observable = mTeamRepertory.getTeamProjects(mTeamBean.getId(),null);

        } else
        {
            observable = mProjectRepertory.getProjectList(false, null);
        }
        mSubscription = observable.subscribe(new RxSubscriber<List<ProjectBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
                mView.showMsg(msg);
            }

            @Override
            public void _onNext(List<ProjectBean> projectBeens)
            {
                mView.hideProgress();
                mProjects = projectBeens;
                mView.showProject(projectBeens);
            }
        });
    }

    @Override
    public void createProject(final String name)
    {
        mView.showProgress();
        mTeamRepertory.createTeamProject(mTeamBean.getId(), name).subscribe(new RxSubscriber<String>()
        {
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
                ProjectBean p = new ProjectBean();
                p.setName(name);
                p.setId(Integer.parseInt(s));
                mProjects.add(p);
                mView.showProject(mProjects);
            }
        });
    }

    @Override
    public void onDestroy()
    {

    }
}
