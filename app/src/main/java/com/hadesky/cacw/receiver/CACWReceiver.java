package com.hadesky.cacw.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;

import cn.jpush.android.api.JPushInterface;

/**
 *  推送接收器
 * Created by 45517 on 2016/7/27.
 */
public class CACWReceiver extends BroadcastReceiver
{

    private static final String TAG = "CACWReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        if (message != null) {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title).setContentText(message).setAutoCancel(true)
                    .setSmallIcon(R.mipmap.icon).setDefaults(NotificationCompat.DEFAULT_ALL).build();
            NotificationManager manager = MyApp.getNotificationManager();
            if (manager != null)
            {
                manager.notify(0, notification);
            }
        }
    }
}
