package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.NewTeamView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 *
 * Created by dzysg on 2016/7/10 0010.
 */
public class NewTeamPresenterImpl implements NewTeamPresenter {


    NewTeamView mView;

    public NewTeamPresenterImpl(NewTeamView view) {
        mView = view;
    }

    @Override
    public void createTeam(String tname, File avatar) {

        if (tname.trim().length() == 0) {
            mView.showMsg("团队名称不能为空");
            return;
        }

        TeamBean teamBean = new TeamBean(tname);
        teamBean.setAdminUserId(MyApp.getCurrentUser().getObjectId());
        if (avatar != null)
            teamBean.setTeamAvatar(new BmobFile(avatar));

        final TeamMember tm = new TeamMember();
        tm.setTeam(teamBean);
        tm.setUser(MyApp.getCurrentUser());

        mView.showProgress();

        teamBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    tm.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            mView.hideProgress();
                            if (e == null)
                                mView.showMsg("创建成功");
                            else {
                                mView.showMsg(e.getMessage());
                            }
                        }
                    });
                } else {
                     mView.hideProgress();
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }
}
