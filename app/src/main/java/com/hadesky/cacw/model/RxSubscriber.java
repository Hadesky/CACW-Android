package com.hadesky.cacw.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.CookieManager;
import com.hadesky.cacw.model.network.SessionException;
import com.hadesky.cacw.util.ActivityLifeCallBack;

import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于presenter里每个接口的订阅器
 * Created by dzysg on 2016/9/1 0001.
 */
public abstract class RxSubscriber<T> extends Subscriber<T>
{


    public abstract void _onError(String msg);

    public abstract void _onNext(T t);

    public void onCompleted()
    {

    }

    @Override
    public final void onError(Throwable e)
    {
        e.printStackTrace();
        Log.e("tag", e.getMessage());

        if(e instanceof SocketTimeoutException)
        {
            _onError("连接超时");
        }
        //session过期
        else if (e instanceof SessionException)
        {
            try
            {
                if(ActivityLifeCallBack.mTop.get()!=null)
                {
                    new AlertDialog.Builder(ActivityLifeCallBack.mTop.get())
                            .setMessage(R.string.session_expire)
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    //清除cookie,重启app
                                    CookieManager.clearCookie();
                                    Intent intent = MyApp.getAppContext().getPackageManager()
                                            .getLaunchIntentForPackage( MyApp.getAppContext().getPackageName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    MyApp.getAppContext().startActivity(intent);
                                }
                            }).create().show();
                }
            }
            catch (Exception ex)
            {
                _onError(ex.getMessage());
            }
        } else
            _onError(e.getMessage());
    }

    @Override
    public final void onNext(T t)
    {
        _onNext(t);
    }
}
