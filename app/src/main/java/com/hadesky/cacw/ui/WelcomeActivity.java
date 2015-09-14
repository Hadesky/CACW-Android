package com.hadesky.cacw.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.util.ZoomOutPageTransformer;
import com.hadesky.cacw.widget.IndicatorView;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BaseActivity {

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private IndicatorView mIndicatorView;
    private List<ImageView> imageViewList;
    private static final int PAGER_NUMBER = 3;
    private List<Integer> colorList;
    private int mPriviousIndex = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        colorList = new ArrayList<>();
        colorList.add(0xFF92D050);
        colorList.add(0xFF7030A0);
        colorList.add(0xFF00B0F0);

        initImageView();

        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_welcome);
        mAdapter = new WelcomePagerAdapter(imageViewList);
        mIndicatorView = (IndicatorView) findViewById(R.id.indicator_activity_welcome);
    }

    @Override
    public void setupView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        final View bg = findViewById(R.id.bg_activity_welcome);
        bg.setBackgroundColor(colorList.get(0));
        mViewPager.setAdapter(mAdapter);
        mIndicatorView.setViewPager(mViewPager);
        mIndicatorView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                //颜色渐变动画
                ObjectAnimator animator1 = ObjectAnimator.ofObject(bg,
                        "backgroundColor",
                        new ArgbEvaluator(),
                        colorList.get(mPriviousIndex),
                        colorList.get(position));
                mPriviousIndex = position;
                animator1.setDuration(500);
                animator1.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void initImageView() {
        imageViewList = new ArrayList<>();
        ImageView imageView;
        for (int i = 0; i < PAGER_NUMBER; i++) {
            imageView = new ImageView(this);
            imageView.setImageDrawable(getImageDrawable(i));
            imageViewList.add(imageView);
        }
    }

    private Drawable getImageDrawable(int pos) {
        if (pos > PAGER_NUMBER || pos < 0) {
            return null;
        }
        switch (pos) {
            case 0:
                return getResources().getDrawable(R.drawable.activity_welcome_welcome1);
            case 1:
                return getResources().getDrawable(R.drawable.activity_welcome_welcome2);
            case 2:
                return getResources().getDrawable(R.drawable.activity_welcome_welcome3);
            default:
                return null;
        }
    }

    public void startLoginActivity(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        this.finish();
    }


    private class WelcomePagerAdapter extends PagerAdapter {

        private List<ImageView> mImageViewList;

        public WelcomePagerAdapter(List<ImageView> imageViewList) {
            super();
            if (imageViewList != null) {
                this.mImageViewList = imageViewList;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public int getCount() {
            return PAGER_NUMBER;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
