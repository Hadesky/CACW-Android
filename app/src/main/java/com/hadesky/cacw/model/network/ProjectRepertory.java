package com.hadesky.cacw.model.network;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.RxHelper;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目仓库
 * Created by dzysg on 2016/9/3 0003.
 */
public class ProjectRepertory
{

    private static ProjectRepertory sProjectRepertory;
    private CacwServer mCacwServer;
    public static ProjectRepertory getInstance()
    {
        if(sProjectRepertory==null)
            sProjectRepertory = new ProjectRepertory();
        return sProjectRepertory;
    }

    public ProjectRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }


    /** 获取项目列表
     * @param file true 代表获取归档项目，false获取未归档项目，null表示所有
     * @param isPrivate 代表获取私人项目，false获取团队项目，null表示所有
     * @return Observable
     */
    public Observable<List<ProjectBean>> getProjectList(Boolean file,Boolean isPrivate)
    {
        String type ,state;

        if(file==null)
            state = null;
        else if(file)
            state = "file";
        else
            state= "unfile";

        if(isPrivate==null)
            type  = null;
        else if(isPrivate)
            type  = "private";
        else
            type  = "team";
        return mCacwServer.getProjectList(type,state)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<ProjectBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
