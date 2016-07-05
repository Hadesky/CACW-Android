package com.hadesky.cacw.config;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
    private RequestQueue requestQueue;//Volley请求队列



    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄露检查

        session  = new SessionManagement(this);
        URL = "http://115.28.15.194:8000";
        mContext = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Bmob.initialize(this,"9840ab2d3565b71aec5c22be01317c0f");
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

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        requestQueue.add(request);
    }

    public <T> void addToRequestQueue(Request request) {
        request.setTag(TAG);
        requestQueue.add(request);
    }

    public void cancelPendingRequest(String tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}
