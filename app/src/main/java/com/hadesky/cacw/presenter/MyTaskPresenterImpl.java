package com.hadesky.cacw.presenter;

import android.util.Log;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.ui.view.TaskView;

import java.util.List;

import rx.Subscriber;

/**主界面 我的任务
 * Created by dzysg on 2016/8/31 0031.
 */
public class MyTaskPresenterImpl implements MyTaskPresenter
{


    private TaskView mView;
    private TaskRepertory mRepertory;

    public MyTaskPresenterImpl(TaskView view)
    {
        mView = view;
        mRepertory = new TaskRepertory();
    }

    /** 任务状态
     * @param state 0为未完成 1 为完成 2为所有
     */
    @Override
    public void LoadTasks(int state)
    {

        mView.showProgress();
        mRepertory.getTaskList(state)
                .subscribe(new Subscriber<List<TaskBean>>() {
                    @Override
                    public void onCompleted()
                    {
                        mView.hideProgress();
                    }
                    @Override
                    public void onError(Throwable e)
                    {
                        mView.hideProgress();
                        Log.e("tag", e.getMessage());
                    }
                    @Override
                    public void onNext(List<TaskBean> list)
                    {
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

    }
}
