package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.MyTeamView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 *
 *
 * Created by dzysg on 2016/3/22 0022.
 */
public class MyTeamPresenterImpl implements MyTeamPresenter
{



    MyTeamView mTeamView;
    Subscription mSubscriptions;
    UserBean mUser;

    public MyTeamPresenterImpl(MyTeamView TeamView)

    {
        mUser = MyApp.getCurrentUser();
    }

    @Override
    public void LoadAllTeams(BmobQuery.CachePolicy policy) {
        mTeamView.showProgress();
        BmobQuery<TeamMember> q = new BmobQuery<>();
        q.setCachePolicy(policy);
        q.addWhereEqualTo("mUser", new BmobPointer(mUser));
        q.include("mUser,mTeam");

        mSubscriptions = q.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                mTeamView.hideProgress();
                if (e == null) {
                    mTeamView.showTeamList(list);
                } else {
                    mTeamView.showMsg(e.getMessage());
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        if (mSubscriptions!=null)
            mSubscriptions.unsubscribe();
    }
}
