package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.MyTeamModel;
import com.hadesky.cacw.ui.view.MyTeamView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 *
 * Created by dzysg on 2016/3/22 0022.
 */
public class MyTeamPresenterImpl implements MyteamPresenter
{


    MyTeamModel mModel;
    MyTeamView mTeamView;
    Subscription mSubscriptions;
    UserBean mUser;

    public MyTeamPresenterImpl(MyTeamView TeamView)
    {
        mModel = new MyTeamModel();
        mTeamView = TeamView;
        mUser = MyApp.getCurrentUser();
    }

    @Override
    public void LoadAllTeams()
    {
        mTeamView.showProgress();
        BmobQuery<TeamMember> q = new BmobQuery<>();
        q.addWhereEqualTo("mUser", new BmobPointer(mUser));
        mSubscriptions =  q.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                mTeamView.hideProgress();
                if (e==null)
                {
                    mTeamView.showTeamList(list);
                }else
                {
                    mTeamView.showMsg(e.getMessage());
                }
            }
        });

    }

    @Override
    public void onDestory() {
        if (mSubscriptions!=null)
            mSubscriptions.unsubscribe();
    }
}
