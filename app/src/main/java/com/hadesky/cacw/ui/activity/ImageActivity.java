package com.hadesky.cacw.ui.activity;

import android.net.Uri;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.ImageViewPagerAdapter;
import com.hadesky.cacw.ui.widget.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览类，还在完善中...
 * TODO:完善传递多图片URL的机制，完善缩放操作
 */
public class ImageActivity extends BaseActivity {

    private ViewPager mViewPager;
    private String mUri;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mUri = getIntent().getStringExtra("url");//待完善
    }

    @Override
    public void setupView() {
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(mUri, this);
        mViewPager.setAdapter(adapter);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
