package com.hadesky.cacw.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 *
 * Created by dzysg on 2016/7/31 0031.
 */
public class ActivityLifeCallBack implements Application.ActivityLifecycleCallbacks
{
    private static int count = 0;
    public static WeakReference<Activity> mTop;

    public ActivityLifeCallBack()
    {
        count = 0;
    }


    public static boolean isForeground()
    {
        return count>0;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {
        count++;
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        mTop = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity)
    {

    }

    @Override
    public void onActivityStopped(Activity activity)
    {
        count--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {

    }
}
