package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.CacwServer;

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

    public static UserRepertory getInstance()
    {
        if(sUserRepertory==null)
            sUserRepertory = new UserRepertory();
        return sUserRepertory;
    }


    public UserRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }

    public Observable<UserBean> updateUserInfo(String username)
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

}
