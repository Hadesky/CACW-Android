package com.hadesky.cacw.config;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.util.ActivityLifeCallBack;

import cn.jpush.android.api.JPushInterface;


/**
 * 单例模式，只会有一个实例，所以可以保留或共享一些数据变量
 * Created by Bright Van on 2015/8/22/022.
 */

public class MyApp extends Application
{

    private static final String TAG = MyApp.class.getSimpleName();
    private static String URL;//服务器地址
    private static Context mContext;//App实例




    @Override
    public void onCreate()
    {
        super.onCreate();

        URL = "http://115.28.15.194:8000";
        mContext = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Fresco.initialize(this);
        this.registerActivityLifecycleCallbacks(new ActivityLifeCallBack());
    }


    /**
     * 获得通知manager
     * @return NotificationManager
     */
    public static NotificationManager getNotificationManager() {
        if (mContext != null) {
            return (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return null;
    }

    public static boolean isCurrentUser(UserBean sb)
    {
        return getCurrentUser().equals(sb);
    }

    public static UserBean getCurrentUser()
    {
        return new UserBean();
    }

    public static Context getAppContext()
    {
        return mContext;
    }


    public static String getURL()
    {
        return URL;
    }

}
