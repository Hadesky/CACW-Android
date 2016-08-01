package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.ui.view.TeamInfoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
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

    //有5个表需要做删除操作，分别进行查询
    List<? extends BmobObject> mTaskBeanToDel;
    List<? extends BmobObject> mTaskMembersToDel;
    List<? extends BmobObject> mProjectBeanToDel;
    List<? extends BmobObject> mTeamMembersToDel;

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

    @Override
    public void delCurrentTeam() {
        mView.showProgress();

        //查询所有Project
        final BmobQuery<ProjectBean> query = new BmobQuery<>();
        query.addQueryKeys("objectId");
        query.addWhereEqualTo("mTeam", mTeam);
        query.findObjects(new FindListener<ProjectBean>() {
            @Override
            public void done(List<ProjectBean> list, BmobException e) {
                if (e == null) {
                    //找到了所有的ProjectBean，继续找所有符合的Task
                    mProjectBeanToDel = list;
                    queryTaskBean(list);
                } else {
                    mView.hideProgress();
                    mView.showMsg("解散失败，请稍后再试");
                }
            }

            private void queryTaskBean(List<ProjectBean> list) {
                if (list == null || list.isEmpty()) {
                    queryTaskMember(null);
                    return;
                }
                List<BmobQuery<TaskBean>> queries = new ArrayList<>();
                for (ProjectBean bean : list) {
                    BmobQuery<TaskBean> taskBeanBmobQuery = new BmobQuery<>();
                    taskBeanBmobQuery.addQueryKeys("objectId");
                    taskBeanBmobQuery.addWhereEqualTo("mProjectBean", bean.getObjectId());
                    queries.add(taskBeanBmobQuery);
                }
                BmobQuery<TaskBean> mainQuery = new BmobQuery<>();
                mainQuery = mainQuery.or(queries);
                mainQuery.addQueryKeys("objectId");
                mainQuery.findObjects(new FindListener<TaskBean>() {
                    @Override
                    public void done(List<TaskBean> list, BmobException e) {
                        //找到了所有TaskBean，查找TeamMember
                        if (e == null) {
                            mTaskBeanToDel = list;
                            queryTaskMember(list);
                        } else {
                            mView.hideProgress();
                            mView.showMsg("解散失败，请稍后再试");
                        }
                    }
                });
            }

            private void queryTaskMember(List<TaskBean> list) {
                if (list == null || list.isEmpty()) {
                    queryTeamMember();
                    return;
                }
                List<BmobQuery<TaskMember>> queries = new ArrayList<>();
                for (TaskBean bean : list) {
                    BmobQuery<TaskMember> taskBeanBmobQuery = new BmobQuery<>();
                    taskBeanBmobQuery.addQueryKeys("objectId");
                    taskBeanBmobQuery.addWhereEqualTo("mTask", bean.getObjectId());
                    queries.add(taskBeanBmobQuery);
                }
                BmobQuery<TaskMember> mainQuery = new BmobQuery<>();
                mainQuery = mainQuery.or(queries);
                mainQuery.addQueryKeys("objectId");
                mainQuery.findObjects(new FindListener<TaskMember>() {
                    @Override
                    public void done(List<TaskMember> list, BmobException e) {
                        //找到了所有TaskMember，查找TeamMember
                        if (e == null) {
                            mTaskMembersToDel = list;
                            queryTeamMember();
                        } else {
                            mView.hideProgress();
                            mView.showMsg("解散失败，请稍后再试");
                        }
                    }
                });
            }

            private void queryTeamMember() {
                BmobQuery<TeamMember> query1 = new BmobQuery<>();
                query1.addWhereEqualTo("mTeam", mTeam);
                query1.addQueryKeys("objectId");
                query1.findObjects(new FindListener<TeamMember>() {
                    @Override
                    public void done(List<TeamMember> list, BmobException e) {
                        if (e == null) {
                            mTeamMembersToDel = list;
                            doDelete();
                        } else {
                            mView.hideProgress();
                            mView.showMsg("解散失败，请稍后再试");
                        }
                    }
                });
            }

            @SuppressWarnings("unchecked")
            private void doDelete() {
                BmobBatch bmobBatch = new BmobBatch();
                List<BmobObject> teamBeen = new ArrayList<>();
                teamBeen.add(mTeam);
                bmobBatch.deleteBatch(teamBeen);
                if (mProjectBeanToDel != null && !mProjectBeanToDel.isEmpty()) {
                    bmobBatch.deleteBatch((List<BmobObject>) mProjectBeanToDel);
                }
                if (mTaskBeanToDel != null && !mTaskBeanToDel.isEmpty()) {
                    bmobBatch.deleteBatch((List<BmobObject>) mTaskBeanToDel);
                }
                if (mTaskMembersToDel != null && !mTaskMembersToDel.isEmpty()) {
                    bmobBatch.deleteBatch((List<BmobObject>) mTaskMembersToDel);
                }
                if (mTeamMembersToDel != null && !mTeamMembersToDel.isEmpty()) {
                    bmobBatch.deleteBatch((List<BmobObject>) mTeamMembersToDel);
                }
                bmobBatch.doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if (e == null) {
                            mView.hideProgress();
                            mView.showMsg("解散成功");
                            mView.close();
                        } else {
                            mView.hideProgress();
                            mView.showMsg("解散失败，请稍后再试");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void exitTeam() {

    }
}
