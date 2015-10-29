package com.hadesky.cacw.config;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;


/**
 * 单例模式，只会有一个实例，所以可以保留或共享一些数据变量
 * Created by Bright Van on 2015/8/22/022.
 */

public class MyApp extends Application {

    private SessionManagement session;
    private String URL;
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄露检查

        session  = new SessionManagement(this);
        URL = "http://www.baidu.com";
        mContext = this;
        LeakCanary.install(this);
    }


    public static Context getAppContext()
    {
        return mContext;
    }

    public SessionManagement getSession() {
        return session;
    }

    public String getURL() {
        return URL;
    }
}
