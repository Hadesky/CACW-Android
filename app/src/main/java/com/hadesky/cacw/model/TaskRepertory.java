package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.CacwServer;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public class TaskRepertory
{

    CacwServer mCacwServer;

    private  static TaskRepertory sTaskRepertory;


    public static TaskRepertory getInstance()
    {
        if(sTaskRepertory==null)
            sTaskRepertory = new TaskRepertory();
        return sTaskRepertory;
    }


    public TaskRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }

    /**
     * 获取任务列表
     * @param state 0 为未完成 1为完成 2 为所有
     * @return 任务列表Observable
     */
    public Observable<List<TaskBean>> getTaskList(int state)
    {
        String query;
        switch (state)
        {
            case 0:
                query = "unfinish";
                break;
            case 1:
                query = "finished";
                break;
            default:
                query = "all";
        }
        return mCacwServer.getTaskList(query).subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<TaskBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<UserBean>> getTaskMember(int tid)
    {
        return mCacwServer.getTaskMembers(tid)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<UserBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
