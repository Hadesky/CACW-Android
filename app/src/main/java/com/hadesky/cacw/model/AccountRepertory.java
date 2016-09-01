package com.hadesky.cacw.model;

import com.google.gson.JsonObject;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.network.BaseResult;
import com.hadesky.cacw.network.CacwServer;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public class AccountRepertory
{

    CacwServer mCacwServer;

    public AccountRepertory( )
    {
        mCacwServer = MyApp.getApiServer();
    }

    public Observable<String> login(final String username, String psw)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        jsonObject.addProperty("psw",psw);
        jsonObject.addProperty("deviceId",MyApp.getDeviceId());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

        return  mCacwServer.login(body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s)
                    {
                        MyApp.getSessionManager().setCurrentUser(username);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<BaseResult<String>> logout()
    {
        return mCacwServer.logout().subscribeOn(Schedulers.io());
    }

    public Observable<String> register(String username, String psw)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        jsonObject.addProperty("psw",psw);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

        return mCacwServer.register(body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
