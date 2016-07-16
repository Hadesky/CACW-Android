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
import rx.Subscription;

/**
 * 我的项目Presenter实现
 * Created by 45517 on 2015/10/31.
 */
public class MyProjectPresenterImpl implements MyProjectPresenter {

    private List<ProjectBean> mData;
    private MyProjectView mView;
    private MyProjectModel myProjectModel;
    private UserBean mUser;
    private Subscription mSubscription;
    private TeamBean mTeam;

    public MyProjectPresenterImpl(MyProjectView myProjectView, TeamBean b) {

        this.mView = myProjectView;
        mUser = MyApp.getCurrentUser();
        mTeam = b;
    }

    @Override
    public void loadProject() {
        if (mTeam == null)
            loadAllProjects();
        else
            loadTeamProjects();

    }


    private void loadTeamProjects() {
        BmobQuery<ProjectBean> q = new BmobQuery<>();
        q.addWhereEqualTo("mTeam", new BmobPointer(mTeam));
        mView.showProgress();
        mSubscription = q.findObjects(new FindListener<ProjectBean>() {
            @Override
            public void done(List<ProjectBean> list, BmobException e) {
                mView.hideProgress();
                if (e != null) {
                    mView.showMsg(e.getMessage());
                } else {
                    mView.showProject(list);
                }
            }
        });
    }


    private void loadAllProjects() {
        String sql = "select include mTeam,* from ProjectBean where mTeam in (select include mTeam from TeamMember" +
                " where mUser = Pointer(_User,%s))";



        mView.showProgress();
        BmobQuery<TeamMember> tm = new BmobQuery<>();
        tm.addWhereEqualTo("mUser", new BmobPointer(mUser));


        tm.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                if (e != null) {
                    mView.hideProgress();
                } else {
                    List<String> IdList = new ArrayList<>();
                    for (TeamMember tm : list) {
                        IdList.add(tm.getTeam().getObjectId());
                    }
                    BmobQuery<ProjectBean> pb = new BmobQuery<>();
                    pb.include("mTeam");
                    BmobQuery<TeamBean> tb = new BmobQuery<>();
                    tb.addWhereContainedIn("ObjectId", IdList);
                    pb.addWhereMatchesQuery("mTeam", "TeamBean", tb);

                    pb.findObjects(new FindListener<ProjectBean>() {
                        @Override
                        public void done(List<ProjectBean> list, BmobException e) {
                            mView.hideProgress();
                            if (e != null) {

                                mView.showMsg(e.getMessage());
                            } else {
                                mView.showProject(list);
                            }
                        }
                    });

                }
            }
        });


    }

    @Override
    public void onDestroy() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }
}
