package com.hadesky.cacw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Bright Van on 2015/8/22/022.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context context;//子类可直接获取上下文引用

    public BaseActivity(){
        context = this;
    }

    /**
     * 子类不需要再重写该方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 禁止横屏，所有继承该Activity的之类都不支持横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        initView();
        setupView();
    }

    /**
     * 设置布局文件
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化界面
     */
    public abstract void initView();

    /**
     * 设置界面
     */
    public abstract  void setupView();


    protected void navigateTo(Class<?> activity,boolean newTask)
    {
        Intent intent = new Intent();
        intent.setClass(this,activity);
        if (newTask)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
    }


    /**
     * 之类直接调用该方法显示toast
     * @param message
     */
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
