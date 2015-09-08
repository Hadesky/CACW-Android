package com.hadesky.cacw.ui;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.FragmentAdapter;
import com.hadesky.cacw.ui.fragment.LinkManFragment;
import com.hadesky.cacw.ui.fragment.MeFragment;
import com.hadesky.cacw.ui.fragment.MessageFragment;
import com.hadesky.cacw.ui.fragment.MyTaskFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private long exitTime = 0;// 双击退出的时间标记
    private ViewPager mViewPager;//主界面
    private TabLayout mTabLayout;//主界面Fragment的容器
    private Toast exitToast;    //退出软件时的Toast

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);//TODO 更换ICON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_main);
        mTabLayout = (TabLayout) findViewById(R.id.tabs_activity_main);

        //初始化退出的Toast，并不需要Show出来
        exitToast = Toast.makeText(this, "再按返回键退出", Toast.LENGTH_SHORT);
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
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[3]));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.setTabTextColors(0xEEF5F5F5, Color.WHITE);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyTaskFragment());
        fragments.add(new MessageFragment());
        fragments.add(new LinkManFragment());
        fragments.add(new MeFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exitToast.getView().getParent() == null) {
            exitToast.show();
        } else {
            super.onBackPressed();
        }
    }
}
