package com.hadesky.cacw.config;

import android.app.Application;
import android.content.Context;

import com.hadesky.cacw.bean.UserBean;

import cn.bmob.v3.Bmob;


/**
 * 单例模式，只会有一个实例，所以可以保留或共享一些数据变量
 * Created by Bright Van on 2015/8/22/022.
 */

public class MyApp extends Application {

    private static final String TAG = MyApp.class.getSimpleName();
    private static SessionManagement session;//本地账户session
    private static String URL;//服务器地址
    private static Context mContext;//App实例
    private static UserBean mCurrentUser;



    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄露检查

        session  = new SessionManagement(this);
        URL = "http://115.28.15.194:8000";
        mContext = this;
        Bmob.initialize(this,"e3eaf0e8f1712c6cb3dee7ba7cc995de");
    }

    public static UserBean getCurrentUser()
    {
        return mCurrentUser;
    }

    public static void setCurrentUser(UserBean u)
    {
        mCurrentUser = u;
    }


    public static Context getAppContext()
    {
        return mContext;
    }

    public static SessionManagement getSession() {
        return session;
    }

    public static String getURL() {
        return URL;
    }
}
