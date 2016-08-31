package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.network.BaseResult;
import com.hadesky.cacw.network.CacwServer;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public class TaskRepertory
{

    CacwServer mCacwServer;
    public TaskRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }


    /** 获取任务列表
     * @param state 0 为未完成 1为完成 2 为所有
     * @return 任务列表Observable
     */
    public Observable<BaseResult<List<TaskBean>>> getTaskList(int state)
    {
        String query;
        switch (state)
        {
            case 0:query="unfinish";break;
            case 1:query="finished";break;
            default:
                query="all";
        }
        return mCacwServer.getTaskList(query).subscribeOn(Schedulers.io());
    }

}
