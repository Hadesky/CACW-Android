package com.hadesky.cacw.presenter;

import android.util.Log;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.network.BaseResult;
import com.hadesky.cacw.ui.view.TaskView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
        mRepertory.getTaskList(state).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult<List<TaskBean>>>() {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mView.hideProgress();
                    }

                    @Override
                    public void onNext(BaseResult<List<TaskBean>> listBaseResult)
                    {
                        mView.hideProgress();
                        Log.e("tag",listBaseResult.toString());
                        if(listBaseResult.getState_code()==0)
                            mView.showDatas(listBaseResult.getData());
                        else
                            mView.showMsg(listBaseResult.getError_msg());
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
