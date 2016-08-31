package com.hadesky.cacw.ui.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.WindowManager;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页面
 * Created by Bright Van on 2015/8/22/022.
 */
public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 800; // 延迟
    private static final String MyPrefs = "MyPrefs";

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉状态栏
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                validityCheck();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void validityCheck() {
        if (isFirstRun()) {
            //退出自动增加一次使用次数
            SharedPreferences preferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first", false);
            editor.apply();
            navigateTo(WelcomeActivity.class, false);
        } else if (!hasLogin()) {
            navigateTo(LoginActivity.class, true);
        } else {
            navigateTo(MainActivity.class, true);
        }
        finish();
    }

    /**
     * 检查是否已经登录
     * @return true 如果已经登录
     */
    private boolean hasLogin() {
        UserBean bean =null;// BmobUser.getCurrentUser(UserBean.class);
        return bean != null;
    }

    /**
     *检查是否第一次运行程序，返回值表示是否第一次运行
     * @return  true表示第一次运行
     */
    private boolean isFirstRun() {
        SharedPreferences preferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        return preferences.getBoolean("first", true);
    }

    @Override
    public void setupView() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
