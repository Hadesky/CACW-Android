package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.model.network.ProjectRepertory;
import com.hadesky.cacw.ui.view.EditTaskView;

import java.util.List;

import rx.Subscription;

/** 编辑任务或新建任务presenter
 * Created by dzysg on 2016/9/2 0002.
 */
public class EditTaskPresenterImpl implements EditTaskPresenter
{
    private EditTaskView mView;
    private TaskBean mTask;
    private TaskRepertory mTaskRepertory;
    private Subscription mSubscription;
    private ProjectRepertory mProjectRepertory;


    public EditTaskPresenterImpl(EditTaskView view,TaskBean t)
    {
        mView = view;
        mTask = t;
        mTaskRepertory = TaskRepertory.getInstance();
        mProjectRepertory = ProjectRepertory.getInstance();
    }

    @Override
    public void loadTaskMember()
    {

    }

    @Override
    public void saveTask()
    {
        if(mTask.getTitle()==null||mTask.getTitle().length()==0)
        {
            mView.showMsg("请填写任务标题");
            return;
        }
        mSubscription = mTaskRepertory.modifyTaskInfo(mTask)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                    }
                    @Override
                    public void _onNext(String taskBean)
                    {
                        mView.hideProgress();
                        mView.showMsg("保存成功");
                        mView.close();
                    }
                });
    }

    @Override
    public void createTask(List<UserBean> member)
    {
        if(mTask.getTitle()==null||mTask.getTitle().length()==0)
        {
            mView.showMsg("请填写任务标题");
            return;
        }

        mView.showProgress();
        mSubscription = mTaskRepertory.createTask(mTask,member)
                .subscribe(new RxSubscriber<TaskBean>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                    }
                    @Override
                    public void _onNext(TaskBean taskBean)
                    {
                        mView.hideProgress();
                        mView.showMsg("创建成功");
                        mView.close();
                    }
                });
    }

    @Override
    public void addMember(List<UserBean> member)
    {

    }

    @Override
    public void deleteMember(List<UserBean> member)
    {

    }

    @Override
    public void loadProjects()
    {
        mView.showProgress();
        mProjectRepertory.getProjectList(false, null).subscribe(new RxSubscriber<List<ProjectBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
            }

            @Override
            public void _onNext(List<ProjectBean> projectBeen)
            {
                mView.hideProgress();
                mView.showProjects(projectBeen);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

}
