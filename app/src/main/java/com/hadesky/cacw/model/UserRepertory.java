package com.hadesky.cacw.model;

import com.google.gson.JsonObject;
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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/** 用户信息仓库
 * Created by dzysg on 2016/9/4 0004.
 */
public class UserRepertory
{

    CacwServer mCacwServer;

    private static UserRepertory sUserRepertory;
    private static class  holder{
        private static UserRepertory instance=new UserRepertory();
    }

    public static UserRepertory getInstance()
    {
        if(sUserRepertory==null)
            sUserRepertory = holder.instance;
        return sUserRepertory;
    }


    public UserRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }


    public Observable<UserBean> getUserInfo(String username)
    {
        return  mCacwServer.getUserInfo(username)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<UserBean>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //只能用于更新自己的信息,获取最新数据并保存
    public Observable<UserBean> loadAndUpdateMyInfo(String username)
    {
        return  mCacwServer.getUserInfo(username)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<UserBean>handleResult())
                .doOnNext(new Action1<UserBean>() {
                    @Override
                    public void call(UserBean bean)
                    {
                        MyApp.getSessionManager().saveUser(bean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> modifyUserInfo(Map<String,String> info)
    {
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String,String> entry:info.entrySet())
        {
            jsonObject.addProperty(entry.getKey(),entry.getValue());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        return mCacwServer.modifyUserInfo(body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());



    }


    public Observable<String> modifyUserIcon(File file)
    {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part  body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);
        return mCacwServer.modifyUserIcon(body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<UserBean>> searchUser(String text,int limit,int offset)
    {
        return mCacwServer.searchUser(text,limit,offset)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<UserBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
