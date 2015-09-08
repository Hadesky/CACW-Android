package com.hadesky.cacw.util;

/**
 * @TODO Log工具类，设置开关，防止发布版本时log信息泄露
 * @author Bright Van
 * @date 2015/08/22
 */


import android.util.Log;

import com.hadesky.cacw.BuildConfig;

public class LogUtils {

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }

    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }

    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }

    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }

    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

}
