package com.hadesky.cacw.config;

import android.app.Application;

/**
 * Created by Bright Van on 2015/8/22/022.
 */

/**
 * 单例模式，只会有一个实例，所以可以保留或共享一些数据变量
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
