package com.hadesky.cacw.presenter;

import com.hadesky.cacw.Model.MyTaskModel;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.fragment.TaskView;
import com.hadesky.cacw.util.NetworkUtils;
import java.util.List;

/**
 * Created by dzysg on 2015/10/29 0029.
 */
public class MyTaskPresenterImpl implements MyTaskPresenter
{
    TaskView mTaskView;
    MyTaskModel mTaskModel;
    List<TaskBean> mDatas;

    MyTaskModel.GetDateCallBack mCallBack = new MyTaskModel.GetDateCallBack() {
        @Override
        public void onSucceed(List<TaskBean> list)
        {
            mTaskView.showDatas(list);
            mDatas = list;
        }

        @Override
        public void onFalure(String error)
        {
            mTaskView.onFalure(error);
        }
    };

    MyTaskModel.DelTaskCallBack mDelTaskCallBack = new MyTaskModel.DelTaskCallBack() {
        @Override
        public void onSucceed()
        {
            mTaskView.showWaitingDialog(false);
        }

        @Override
        public void onFalure(String error)
        {

        }
    };

    MyTaskModel.CompleteTaskCallBack mCompleteTaskCallBack = new MyTaskModel.CompleteTaskCallBack() {
        @Override
        public void onSucceed()
        {
            mTaskView.showWaitingDialog(false);
        }

        @Override
        public void onFalure(String error)
        {

        }
    };

    public MyTaskPresenterImpl(TaskView view)
    {
        mTaskView = view;
        mTaskModel = new MyTaskModel(mCallBack,mDelTaskCallBack,mCompleteTaskCallBack);
    }



    @Override
    public void LoadTasks()
    {
        mTaskView.showProgress();
        mTaskModel.LoadTaskByCache();//先拿本地络数据显示,再请求网
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext()))
        {
            mTaskModel.LoadTaskByNetwork();
        }
    }


    @Override
    public void CompleteTask(int pos)
    {
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext()))
        {
            mTaskView.showWaitingDialog(true);
            mTaskModel.taskComplete(mDatas.get(pos).getTaskId());
        }
        else
        mTaskView.onFalure("操作失败");
    }

    @Override
    public void DeleteTask(int pos)
    {
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext()))
        {
            mTaskModel.deleteTask(mDatas.get(pos).getTaskId());
        }
       else
            mTaskView.onFalure("删除失败");
    }
}
