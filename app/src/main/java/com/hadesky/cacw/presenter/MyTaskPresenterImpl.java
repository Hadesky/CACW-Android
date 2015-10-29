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

    MyTaskModel.GetDateCallBack mCallBack = new MyTaskModel.GetDateCallBack() {
        @Override
        public void onSucceed(List<TaskBean> list)
        {
            mTaskView.showDatas(list);
        }

        @Override
        public void onFalure(String error)
        {
            mTaskView.onFalure();
        }
    };

    public MyTaskPresenterImpl(TaskView view)
    {
        mTaskView = view;
        mTaskModel = new MyTaskModel(mCallBack);
    }



    @Override
    public void LoadTasks()
    {
        mTaskView.showProgress();
        mTaskModel.LoadTaskByCache();//先拿本地数据显示,再请求网络
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext()))
        {
            mTaskModel.LoadTaskByNetwork();
        }
    }

    @Override
    public void CompleteTask(int taskid)
    {
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext()))
        {
            // TODO: 2015/10/29 0029 网络请求
        }
        else
        mTaskView.onFalure();
    }

    @Override
    public void DeleteTask(int taskid)
    {
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext()))
        {
            mTaskModel.deleteTask(taskid);
        }
       else
            mTaskView.onFalure();
    }
}
