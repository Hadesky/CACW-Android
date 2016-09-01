package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.CacwServer;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public class TeamRepertory
{

    CacwServer mCacwServer;

    private static TeamRepertory sTeamRepertory;
    public static TeamRepertory  getInstance()
    {
        if(sTeamRepertory==null)
            sTeamRepertory = new TeamRepertory();
        return sTeamRepertory;
    }

    public TeamRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }


    public Observable<String> createTeam(String name, File avatar)
    {

        MultipartBody.Part body=null;
        if(avatar!=null)
        {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
             body = MultipartBody.Part.createFormData("img", avatar.getName(), requestFile);
        }

        return mCacwServer.createTeam(name,body,"dex")
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<TeamBean>> getTeamList()
    {
        return   mCacwServer.getTeamList(true)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<TeamBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
