package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.ui.view.TeamInfoView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Subscription;

/**
 * 团队详情
 * Created by dzysg on 2016/7/12 0012.
 */
public class TeamInfoPresenterImpl implements TeamInfoPresenter
{
    private TeamBean mTeam;
    private Subscription mSubscriptions;
    private TeamInfoView mView;

    public TeamInfoPresenterImpl(TeamBean team, TeamInfoView view)
    {
        mTeam = team;
        mView = view;
    }

    @Override
    public void refreshTeamInfo()
    {
        if (mTeam != null)
        {
            mView.showProgress();
            BmobQuery<TeamBean> query = new BmobQuery<>();
            mSubscriptions = query.getObject(mTeam.getObjectId(), new QueryListener<TeamBean>()
            {
                @Override
                public void done(TeamBean teamBean, BmobException e)
                {
                    mView.hideProgress();
                    if (e == null)
                    {
                        mTeam = teamBean;
                        mView.showInfo(teamBean);
                    } else
                    {
                        mView.showMsg(e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void getTeamMembers()
    {

        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTeam", new BmobPointer(mTeam));
        query.include("mUser");
        query.setLimit(4);//只显示4个人，更多要点击进入成员页面
        mView.showProgress();

        mSubscriptions = query.findObjects(new FindListener<TeamMember>()
        {
            @Override
            public void done(List<TeamMember> list, BmobException e)
            {
                mView.hideProgress();
                if (e == null)
                {
                    mView.showMembers(list);
                } else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }


    @Override
    public void changeSummary(final String s)
    {
        mView.showProgress();
        TeamBean t = new TeamBean();
        t.setObjectId(mTeam.getObjectId());
        t.setSummary(s);
        t.update(new UpdateListener()
        {
            @Override
            public void done(BmobException e)
            {
                mView.hideProgress();
                if (e != null)
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
    public void saveTeamIcon(File file)
    {

        //先上传图片
        final BmobFile bmobFile = new BmobFile(file);
        mView.showProgress();
        mSubscriptions = bmobFile.upload(new UploadFileListener()
        {
            @Override
            public void done(BmobException e)
            {

                if (e != null)
                {
                    mView.hideProgress();
                    mView.showMsg(e.getMessage());
                } else
                {
                    //再设置给团队
                    setTeamAvatar(bmobFile);
                }
            }
        });

    }

    //再设置给团队
    private void setTeamAvatar(final BmobFile file)
    {
        TeamBean bean = new TeamBean();
        bean.setTeamAvatar(file);
        mSubscriptions = bean.update(mTeam.getObjectId(), new UpdateListener()
        {
            @Override
            public void done(BmobException e)
            {
                mView.hideProgress();
                if (e == null)
                {
                    mView.showMsg("上传成功");
                    BmobFile old = mTeam.getTeamAvatar();
                    if (old != null)
                        old.delete();
                    mTeam.setTeamAvatar(file);
                    mView.showInfo();
                } else
                    mView.showMsg(e.getMessage());
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if (mSubscriptions != null)
            mSubscriptions.unsubscribe();
    }

    @Override
    public void getProjectCount()
    {

        BmobQuery<ProjectBean> query = new BmobQuery<>();
        query.addWhereEqualTo("mTeam", new BmobPointer(mTeam));

        query.count(ProjectBean.class, new CountListener()
        {
            @Override
            public void done(Integer integer, BmobException e)
            {
                if (e != null)
                    integer = 0;
                mView.showProjectCount(integer);
            }
        });


    }
}
