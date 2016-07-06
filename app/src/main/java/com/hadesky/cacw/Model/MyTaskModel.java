package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.MyTaskDAO;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
    private UserBean mUser;


    public MyTaskModel(GetDateCallBack callBack,DelTaskCallBack delTaskCallBack,CompleteTaskCallBack completeTaskCallBack)
    {
        mGetTasksCallBack = callBack;
        mDelTaskCallBack = delTaskCallBack;
        mCompleteTaskCallBack = completeTaskCallBack;
        mUser = MyApp.getCurrentUser();
    }

    public void LoadTaskByCache()
    {

    }

    public void LoadTaskByNetwork()
    {

        // TODO: 2016/7/6 0006 没测试
        BmobQuery<TaskMember> tm = new BmobQuery<>();
        BmobQuery<UserBean> userquery = new BmobQuery<>();
        userquery.addWhereEqualTo("mUser",mUser);
        tm.addWhereMatchesQuery("mUser","UserBeam",userquery);

        tm.findObjects(new FindListener<TaskMember>() {
            @Override
            public void done(List<TaskMember> list, BmobException e) {
                if (e!=null)
                {
                    mGetTasksCallBack.onFailure(e.getMessage());
                }else
                {
                    List<TaskBean> tasks = new ArrayList<>(list.size());
                    for (TaskMember tm :list)
                    {
                        tasks.add(tm.getTask());
                    }
                    mGetTasksCallBack.onSucceed(tasks);
                }
            }
        });

    }

    public void deleteTask(String id)
    {

    }
    public void taskComplete(String id)
    {

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
