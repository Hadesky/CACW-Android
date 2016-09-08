package com.hadesky.cacw.model;

import com.google.gson.JsonObject;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.BaseResult;
import com.hadesky.cacw.model.network.CacwServer;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 用户帐户模块，包括登录、注册等
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
                .compose(RxHelper.<UserBean>handleResult())
                .doOnNext(new Action1<UserBean>() {
                    @Override
                    public void call(UserBean s)
                    {
                        MyApp.getSessionManager().saveUser(s);
                    }
                })
                .map(new Func1<UserBean, String>() {
                    @Override
                    public String call(UserBean bean)
                    {
                        return "";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
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
