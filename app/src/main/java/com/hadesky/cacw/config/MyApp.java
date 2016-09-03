package com.hadesky.cacw.config;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.SessionManagement;
import com.hadesky.cacw.model.network.CacwServer;
import com.hadesky.cacw.model.network.CookieManager;
import com.hadesky.cacw.util.ActivityLifeCallBack;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 单例模式，只会有一个实例，所以可以保留或共享一些数据变量
 * Created by Bright Van on 2015/8/22/022.
 */

public class MyApp extends Application
{

    private static final String TAG = MyApp.class.getSimpleName();
    private static String URL;//服务器地址
    private static Context mContext;//App实例
    private static OkHttpClient sOkHttpClient;
    private static SessionManagement sSessionManagement;
    private static CacwServer sApiServer;
    private static String sDeviceId;
    private static int sUserId = -1;
    private static UserBean mUser;

    @Override
    public void onCreate()
    {
        super.onCreate();
        URL = "http://192.168.199.234:8081";
        mContext = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Fresco.initialize(this);
        this.registerActivityLifecycleCallbacks(new ActivityLifeCallBack());
        sOkHttpClient = new OkHttpClient.Builder().writeTimeout(10, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS).cookieJar(new CookieManager()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).client(sOkHttpClient).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        sApiServer = retrofit.create(CacwServer.class);
    }


    public static String getDeviceId()
    {
        if (sDeviceId == null)
            sDeviceId = JPushInterface.getRegistrationID(getAppContext());
        return sDeviceId;
    }

    public static CacwServer getApiServer()
    {
        return sApiServer;
    }

    /**
     * 获得通知manager
     *
     * @return NotificationManager
     */
    public static NotificationManager getNotificationManager()
    {
        if (mContext != null)
        {
            return (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return null;
    }

    public static synchronized SessionManagement getSessionManager()
    {
        if (sSessionManagement == null)
            sSessionManagement = new SessionManagement(getAppContext());
        return sSessionManagement;
    }

    //messageList用到
    public static boolean isCurrentUser(UserBean sb)
    {
        return true;
    }


    public static void setCurrentUser(UserBean u)
    {
        mUser = u;
    }

    public static UserBean getCurrentUser()
    {
        if(mUser==null)
            mUser = getSessionManager().getUser();
        return mUser;
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
