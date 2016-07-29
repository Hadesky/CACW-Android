package com.hadesky.cacw.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 45517 on 2016/7/27.
 */
public class CACWReceiver extends BroadcastReceiver
{

    private static final String TAG = "CACWReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        if (message != null)
            Log.d(TAG, message);
    }
}
