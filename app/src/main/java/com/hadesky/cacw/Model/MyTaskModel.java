package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.database.MyTaskDAO;

import java.util.List;

/**
 * MyTask这个页面的数据层,负责从本地或网络获取数据
 * Created by dzysg on 2015/10/29 0029.
 */
public class MyTaskModel
{
    //必须在主线程调用这个接口
    private GetDateCallBack mGetTasksCallBack;
    private DelTaskCallBack mDelTaskCallBack;
    private CompleteTaskCallBack mCompleteTaskCallBack;

    private MyTaskDAO mDAO = new MyTaskDAO();

    public MyTaskModel(GetDateCallBack callBack,DelTaskCallBack delTaskCallBack,CompleteTaskCallBack completeTaskCallBack)
    {
        mGetTasksCallBack = callBack;
        mDelTaskCallBack = delTaskCallBack;
        mCompleteTaskCallBack = completeTaskCallBack;
    }

    public void LoadTaskByCache()
    {
        List<TaskBean> list = mDAO.getTask(1);
        mGetTasksCallBack.onSucceed(list);
    }

    public void LoadTaskByNetwork()
    {
        mGetTasksCallBack.onFailure("无网络数据");
    }

    public void deleteTask(long id)
    {
        mDelTaskCallBack.onSucceed();
    }
    public void taskComplete(long id)
    {
        mCompleteTaskCallBack.onSucceed();
    }


    public interface GetDateCallBack{
        public void onSucceed(List<TaskBean> list);
        public void onFailure(String error);
    }
    public interface DelTaskCallBack
    {
        public void onSucceed();
        public void onFailure(String error);
    }
    public interface CompleteTaskCallBack
    {
        public void onSucceed();
        public void onFailure(String error);
    }

}
