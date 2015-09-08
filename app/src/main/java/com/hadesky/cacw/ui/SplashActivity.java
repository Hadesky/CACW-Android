package com.hadesky.cacw.ui;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.hadesky.cacw.R;
/**
 * Created by Bright Van on 2015/8/22/022.
 */
public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟2秒
    private Handler handler;
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
        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public void setupView() {

    }


}
