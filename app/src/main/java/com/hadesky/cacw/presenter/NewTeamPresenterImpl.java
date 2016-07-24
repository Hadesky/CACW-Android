package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.NewTeamView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 *
 * Created by dzysg on 2016/7/10 0010.
 */
public class NewTeamPresenterImpl implements NewTeamPresenter {


    NewTeamView mView;

    public NewTeamPresenterImpl(NewTeamView view) {
        mView = view;
    }


    private void createTeam(TeamBean teamBean)
    {

        teamBean.setAdminUser(MyApp.getCurrentUser());
        final TeamMember tm = new TeamMember();
        tm.setTeam(teamBean);
        tm.setUser(MyApp.getCurrentUser());

        teamBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    tm.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            mView.hideProgress();
                            if (e == null)
                            {
                                mView.showMsg("创建成功");
                                mView.Close();
                            }
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




    @Override
    public void createTeam(final String tname, File avatar) {

        if (tname.trim().length() == 0) {
            mView.showMsg("团队名称不能为空");
            return;
        }

        mView.showProgress();

        if (avatar!=null)
        {
            final BmobFile f = new BmobFile(avatar);
            f.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e==null)
                    {

                        TeamBean tb = new TeamBean();
                        tb.setTeamName(tname);
                        tb.setTeamAvatar(f);
                        createTeam(tb);
                    }else
                    {
                        mView.showMsg(e.getMessage());
                        mView.hideProgress();
                    }
                }
            });
        }else
        {
            TeamBean tb = new TeamBean();
            tb.setTeamName(tname);
            createTeam(tb);
        }
    }
}
