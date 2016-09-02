package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.ui.view.TaskView;

import java.util.List;

import rx.Subscription;

/**主界面 我的任务
 * Created by dzysg on 2016/8/31 0031.
 */
public class MyTaskPresenterImpl implements MyTaskPresenter
{


    private TaskView mView;
    private TaskRepertory mRepertory;
    private Subscription mSubscription;
    public MyTaskPresenterImpl(TaskView view)
    {
        mView = view;
        mRepertory = TaskRepertory.getInstance();
    }

    /** 任务状态
     * @param state 0为未完成 1 为完成 2为所有
     */
    @Override
    public void LoadTasks(int state)
    {
        mView.showProgress();
        mSubscription = mRepertory.getTaskList(state)
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
