package com.hadesky.cacw.config;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hadesky.cacw.JPush.JPushManager;
import com.hadesky.cacw.bean.UserBean;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;


/**
 * 单例模式，只会有一个实例，所以可以保留或共享一些数据变量
 * Created by Bright Van on 2015/8/22/022.
 */

public class MyApp extends Application
{

    private static final String TAG = MyApp.class.getSimpleName();
    private static String URL;//服务器地址
    private static Context mContext;//App实例
    private static JPushManager sJPushManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        URL = "http://115.28.15.194:8000";
        mContext = this;
        Bmob.initialize(this, "e3eaf0e8f1712c6cb3dee7ba7cc995de");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Fresco.initialize(this);
        JPushManager.init("e1ddf6b7d3bb9c6ba2545a55","f8dd58cf4736e59d58a28432");
    }

    public static JPushManager getJPushManager()
    {
        if (sJPushManager==null)
            sJPushManager = new JPushManager(new OkHttpClient());
        return sJPushManager;
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
        return BmobUser.getCurrentUser(UserBean.class);
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
