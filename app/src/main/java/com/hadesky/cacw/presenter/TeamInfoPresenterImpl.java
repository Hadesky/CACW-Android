package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.ui.view.TeamInfoView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Subscription;

/**
 *  团队详情
 * Created by dzysg on 2016/7/12 0012.
 */
public class TeamInfoPresenterImpl implements TeamInfoPresenter {



    private TeamBean mTeam;
    private Subscription mSubscriptions;
    private TeamInfoView mView;

    public TeamInfoPresenterImpl(TeamBean team, TeamInfoView view) {
        mTeam = team;
        mView  = view;
    }

    @Override
    public void getTeamMembers() {

        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTeam", new BmobPointer(mTeam));
        query.setLimit(4);
        mView.showProgress();

        mSubscriptions = query.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                mView.hideProgress();
                if (e==null)
                {
                    mView.showMembers(list);
                }else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }



    @Override
    public void changeeSummary(final String s) {
        mView.showProgress();
        TeamBean t = new TeamBean();
        t.setObjectId(mTeam.getObjectId());
        t.setSummary(s);
        t.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mView.hideProgress();
                if (e!=null)
                    mView.showMsg(e.getMessage());
                else
                {
                    mTeam.setSummary(s);
                    mView.showInfo();
                }
            }
        });
    }

    @Override
    public void saveTeamIcon(File file) {

        final BmobFile bmobFile = new BmobFile(file);
        mView.showProgress();
        mSubscriptions =  bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

                if (e!=null)
                {
                    mView.hideProgress();
                    mView.showMsg(e.getMessage());
                }
                else
                {
                    saveAvatar(bmobFile);
                }
            }
        });

    }

    private void saveAvatar(final BmobFile file)
    {
        TeamBean bean = new TeamBean();
        bean.setTeamAvatar(file);
        mSubscriptions = bean.update(mTeam.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mView.hideProgress();
                if (e ==null)
                {
                    mView.showMsg("上传成功");
                    mTeam.setTeamAvatar(file);
                    mView.showInfo();
                }
                else
                    mView.showMsg(e.getMessage());
            }
        });
    }

    @Override
    public void onDestory() {
        if (mSubscriptions!=null)
             mSubscriptions.unsubscribe();
    }
}
