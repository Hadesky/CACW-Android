package com.hadesky.cacw.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.FragmentAdapter;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.ui.fragment.MeFragment;
import com.hadesky.cacw.ui.fragment.MyTaskFragment;
import com.hadesky.cacw.ui.fragment.ProjectFragment;
import com.hadesky.cacw.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;//主界面
    private TabLayout mTabLayout;//主界面Fragment的容器
    private Toast exitToast;    //退出软件时的Toast
    private AppBarLayout mAppBarLayout;



    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //Check if firstRun,if true, start Welcome Activity
        checkIfFirstRun();
        //Check if had login,if no, start Login Activity
        checkIfLogin();

        //ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        //icon和字体已经在xml里面定义，目的是为了让homeButton不能点击，只找到这个解决方法，即自定义Toolbar
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle("");

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        //初始化退出的Toast，并不需要Show出来
        exitToast = Toast.makeText(this, "再按返回键退出", Toast.LENGTH_SHORT);


        mAppBarLayout  = (AppBarLayout) findViewById(R.id.appbar);

    }

    private void checkIfLogin() {
        if (!isFirstRun()) {
            final MyApp app = (MyApp) getApplication();
            app.getSession().checkLogin();
        }
    }

    @Override
    public void setupView() {
        setupTabView();
    }

    /**
     * 建立tab界面
     */
    private void setupTabView() {
        String[] titles = getResources().getStringArray(R.array.tab_titles);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[2]));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.setTabTextColors(0xEEF5F5F5, Color.WHITE);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyTaskFragment());
        fragments.add(new ProjectFragment());
        fragments.add(new MeFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position==2)
                mAppBarLayout.setExpanded(true,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                if (item.getIcon() != null) {
                    showToast("未实现");
                }
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                final MyApp app = (MyApp) getApplication();
                app.getSession().logoutUser();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (exitToast.getView().getParent() == null) {
            exitToast.show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 检查是否第一次运行程序
     */
    private void checkIfFirstRun() {
        if (isFirstRun()) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    /**
     *检查是否第一次运行程序，返回值表示是否第一次运行
     * @return  true表示第一次运行
     */
    private boolean isFirstRun() {
        SharedPreferences preferences = getSharedPreferences("runCount", MODE_PRIVATE);
        int count = preferences.getInt("runCount", 0);
        return count == 0;
    }

    /**
     * onDestroy的时候增加一次使用记录
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出自动增加一次使用次数
        SharedPreferences preferences = getSharedPreferences("runCount", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final int count = preferences.getInt("runCount", 0);
        editor.putInt("runCount", count + 1);
        editor.apply();
//        关闭数据库
        DatabaseManager.getInstance(getApplicationContext()).closeDB();
    }

}
