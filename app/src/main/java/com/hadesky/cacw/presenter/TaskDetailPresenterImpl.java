package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.ui.view.TaskDetailView;

import java.util.List;

import rx.Subscription;

/** 任务详情presenter
 * Created by dzysg on 2016/9/2 0002.
 */
public class TaskDetailPresenterImpl implements TaskDetailPresenter
{

    private  TaskDetailView mView;
    private TaskBean mTask;
    private TaskRepertory mTaskRepertory;
    private Subscription mSubscription;

    public TaskDetailPresenterImpl(TaskDetailView view, TaskBean task)
    {
        mView = view;
        mTask = task;
        mTaskRepertory = TaskRepertory.getInstance();
    }

    @Override
    public void LoadTaskMember()
    {

       mSubscription =  mTaskRepertory.getTaskMember(mTask.getId())
                .subscribe(new RxSubscriber<List<UserBean>>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(List<UserBean> userBeen)
                    {
                        mView.ShowMember(userBeen);
                    }
                });
    }

    @Override
    public void onDestroy()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public void loadTaskInfo()
    {

    }

    @Override
    public void onDeleteTask()
    {

    }
}
