package com.hadesky.cacw.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.MainActivity;
import com.hadesky.cacw.ui.activity.MessageListActivity;
import com.hadesky.cacw.ui.activity.TaskDetailActivity;
import com.hadesky.cacw.util.ActivityLifeCallBack;
import com.hadesky.cacw.util.StringUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * 推送接收器
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

        Log.d(TAG, title + "  " + message);

        if (message == null)
            return;

        //ms开头代表私信，tm代表任务信息
        if (message.startsWith(IntentTag.TAG_PUSH_MSG))
        {
            handleMsg(context, title, message);
        } else if (message.startsWith(IntentTag.TAG_PUSH_TASK))
        {
            handleTaskMsg(context, title, message);
        }else
        {
            handleNormalMsg(context,title,message);
        }
    }


    //普通消息点击打开主界面
    private void handleNormalMsg(Context context, String title, String message)
    {
        if (ActivityLifeCallBack.isForeground())//如果在前台,什么都不做
            return;

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent i = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat
                .Builder(context).setContentTitle(title)
                .setContentText(message).setAutoCancel(true)
                .setSmallIcon(R.mipmap.icon)
                .setContentIntent(i)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();
        NotificationManager manager = MyApp.getNotificationManager();
        if (manager != null)
        {
            manager.notify(0, notification);
        }
    }


    private void handleTaskMsg(Context context, String title, String message)
    {

        if (ActivityLifeCallBack.isForeground())//如果在前台,什么都不做
            return;

        String taskid = StringUtils.getObjectIdFromPushMsg(message);
        String content = StringUtils.getContentFromPushMsg(message);

        //弹通知,点击通知打开消息列表
        Intent messageintent = new Intent(context, TaskDetailActivity.class);
        messageintent.putExtra(IntentTag.TAG_TASK_ID, taskid);
        PendingIntent i = PendingIntent.getActivity(context, 0, messageintent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(content).setAutoCancel(true).setSmallIcon(R.mipmap.icon).setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(i).build();
        NotificationManager manager = MyApp.getNotificationManager();
        if (manager != null)
        {
            manager.notify(0, notification);
        }

    }

    private void handleMsg(Context context, String title, String message)
    {
        String content = StringUtils.getContentFromPushMsg(message);
        String oid = StringUtils.getObjectIdFromPushMsg(message);
        if (oid.equals(""))
            return;

        if (ActivityLifeCallBack.isForeground())//如果在前台，则发广播
        {
            Log.e(TAG, "应用在前台，发送广播");
            LocalBroadcastManager broadcastManagermanager = LocalBroadcastManager.getInstance(context.getApplicationContext());

            Intent bintent = new Intent();
            bintent.setAction(IntentTag.ACTION_MSG_RECEIVE);
            bintent.putExtra(IntentTag.TAG_USER_ID, oid);
            broadcastManagermanager.sendBroadcast(bintent);
            return;
        }


        //否则弹通知,点击通知打开消息列表
        Intent messageintent = new Intent(context, MessageListActivity.class);
        PendingIntent i = PendingIntent.getActivity(context, 0, messageintent, PendingIntent.FLAG_NO_CREATE);


        Notification notification = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(content).setAutoCancel(true).setSmallIcon(R.mipmap.icon).setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(i).build();
        NotificationManager manager = MyApp.getNotificationManager();
        if (manager != null)
        {
            manager.notify(0, notification);
        }
    }


}
