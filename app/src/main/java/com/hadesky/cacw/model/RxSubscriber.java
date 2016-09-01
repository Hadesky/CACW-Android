package com.hadesky.cacw.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.CookieManager;
import com.hadesky.cacw.model.network.SessionException;
import com.hadesky.cacw.util.ActivityLifeCallBack;

import rx.Subscriber;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public abstract class RxSubscriber<T> extends Subscriber<T>
{


    public abstract void _onError(Throwable e);

    public abstract void _onNext(T t);

    public void onCompleted()
    {

    }

    @Override
    public final void onError(Throwable e)
    {
        e.printStackTrace();
        if (e instanceof SessionException)
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
                _onError(ex);
            }

        } else
            _onError(e);
    }

    @Override
    public final void onNext(T t)
    {
        _onNext(t);
    }
}
