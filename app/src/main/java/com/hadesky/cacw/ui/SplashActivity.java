package com.hadesky.cacw.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.hadesky.cacw.R;
/**
 * 启动页面
 * Created by Bright Van on 2015/8/22/022.
 */
public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 1000; // 延迟2秒

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
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void setupView() {
    }
}
