package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.MyProjectModel;
import com.hadesky.cacw.ui.view.MyProjectView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**我的项目Presenter实现
 * Created by 45517 on 2015/10/31.
 */
public class MyProjectPresenterImpl implements MyProjectPresenter {

    private List<ProjectBean> mData;
    private MyProjectView mView;
    private MyProjectModel myProjectModel;
    private UserBean mUser;
    private Subscription mSubscription;
    private TeamBean mTeam;
    public MyProjectPresenterImpl(MyProjectView myProjectView,TeamBean b) {

        this.mView = myProjectView;
        mUser = MyApp.getCurrentUser();
        mTeam = b;
    }

    @Override
    public void loadProject()
    {
        if (mTeam==null)
            loadAddProjects();
        else
            loadTeamProjects();

    }


    private void loadTeamProjects()
    {
        BmobQuery<ProjectBean> q = new BmobQuery<>();
        q.addWhereEqualTo("mTeam", new BmobPointer(mTeam));
        mView.showProgress();
        mSubscription = q.findObjects(new FindListener<ProjectBean>() {
            @Override
            public void done(List<ProjectBean> list, BmobException e) {
                mView.hideProgress();
                if (e!=null)
                {
                    mView.showMsg(e.getMessage());
                }else
                {
                    mView.showProject(list);
                }
            }
        });
    }


    private void loadAddProjects()
    {
        mView.showProgress();
        BmobQuery<TeamMember> tm = new BmobQuery<>();
        tm.addWhereEqualTo("mUser", new BmobPointer(mUser));

        mSubscription =  tm.findObjectsObservable(TeamMember.class)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<TeamMember>, Observable<List<ProjectBean>>>() {
                    @Override
                    public rx.Observable<List<ProjectBean>> call(List<TeamMember> teamMembers) {
                        List<String> teamid = new ArrayList<>();
                        for(TeamMember tm:teamMembers)
                        {
                            teamid.add(tm.getTeam().getObjectId());
                        }
                        BmobQuery<TeamBean> teamQuery = new BmobQuery<>();
                        BmobQuery<ProjectBean> query = new BmobQuery<>();
                        teamQuery.addWhereContainedIn("ObjectId",teamid);
                        query.addWhereMatchesQuery("mTeam", "TeamBean", teamQuery);
                        return query.findObjectsObservable(ProjectBean.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ProjectBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.hideProgress();
                        mView.showMsg("当前没有项目");
                    }

                    @Override
                    public void onNext(List<ProjectBean> projectBeen) {
                        mView.showProject(projectBeen);
                    }
                });
    }

    @Override
    public void onDestroy() {
        mSubscription.unsubscribe();
    }
}
