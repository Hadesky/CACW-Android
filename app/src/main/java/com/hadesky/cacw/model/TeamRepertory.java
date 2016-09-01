package com.hadesky.cacw.model;

import com.google.gson.JsonObject;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.CacwServer;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dzysg on 2016/9/1 0001.
 */
public class TeamRepertory
{

    CacwServer mCacwServer;

    private static TeamRepertory sTeamRepertory;

    public static TeamRepertory getInstance()
    {
        if (sTeamRepertory == null)
            sTeamRepertory = new TeamRepertory();
        return sTeamRepertory;
    }

    public TeamRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }


    public Observable<String> createTeam(String name, File avatar)
    {

        MultipartBody.Part body = null;
        if (avatar != null)
        {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
            body = MultipartBody.Part.createFormData("img", avatar.getName(), requestFile);
        }

        return mCacwServer.createTeam(name, body, "dex").subscribeOn(Schedulers.io()).compose(RxHelper.<String>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<TeamBean>> getTeamList()
    {
        return mCacwServer.getTeamList(true).subscribeOn(Schedulers.io()).compose(RxHelper.<List<TeamBean>>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<TeamBean> getTeamInfo(int tid)
    {
        return mCacwServer.getTeamInfo(tid).subscribeOn(Schedulers.io()).compose(RxHelper.<TeamBean>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<UserBean>> getTeamMember(int tid, int limit, int offset, boolean all)
    {

        return mCacwServer.getTeamMember(tid, limit, offset, all)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<UserBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<String> modifyTeamInfo(int tid, Map<String,String> map)
    {

        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String,String> entry:map.entrySet())
        {
            jsonObject.addProperty(entry.getKey(),entry.getValue());
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

       return  mCacwServer.modifyTeamInfo(tid,body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
