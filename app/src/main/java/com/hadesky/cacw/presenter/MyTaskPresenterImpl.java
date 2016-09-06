package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.model.network.ProjectRepertory;
import com.hadesky.cacw.ui.view.TaskView;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**主界面 我的任务
 * Created by dzysg on 2016/8/31 0031.
 */
public class MyTaskPresenterImpl implements MyTaskPresenter
{


    private TaskView mView;
    private TaskRepertory mTaskRepertory;
    private ProjectRepertory mProjectRepertory;
    private Subscription mSubscription;


    public MyTaskPresenterImpl(TaskView view)
    {
        mView = view;
        mTaskRepertory = TaskRepertory.getInstance();
        mProjectRepertory = ProjectRepertory.getInstance();
    }



    /** 任务状态
     * @param state 0为未完成 1 为完成 2为所有
     */
    @Override
    public void LoadTasks(int state,int projectid)
    {

        mView.showProgress();
        Observable<List<TaskBean>> observable;

        if(projectid>0)
            observable = mProjectRepertory.getProjectTask(projectid,state);
        else
            observable = mTaskRepertory.getTaskList(state);


        mSubscription = observable
                    .subscribe(new RxSubscriber<List<TaskBean>>() {
                        @Override
                        public void _onError(String e)
                        {
                            mView.hideProgress();
                            mView.showMsg(e);
                        }
                        @Override
                        public void _onNext(List<TaskBean> list)
                        {
                            mView.hideProgress();
                            mView.showDatas(list);
                        }
                    });
    }

    @Override
    public void CompleteTask(TaskBean pos)
    {

    }

    @Override
    public void onDestroy()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
