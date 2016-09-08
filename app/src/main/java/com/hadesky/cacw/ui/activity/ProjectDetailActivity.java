package com.hadesky.cacw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.FragmentAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.presenter.ProjectDetailPresenter;
import com.hadesky.cacw.presenter.ProjectDetailPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.MyTaskFragment;
import com.hadesky.cacw.ui.view.ProjectDetailView;
import com.hadesky.cacw.ui.widget.CircleProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjectDetailActivity extends BaseActivity implements ProjectDetailView, View.OnClickListener
{


    private TabLayout mTabLayout;
    private ProjectBean mProjectBean;
    private ViewPager mViewPager;
    private TextView mTvProjectName;
    private TextView mTvTeamName;
    private TextView mTvTaskCount;
    private CircleProgressView mProgressView;

    private ProjectDetailPresenter mPresenter;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_project_detail;
    }

    @Override
    public void initView()
    {

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTvProjectName = (TextView) findViewById(R.id.tv_project_name);
        mTvTeamName = (TextView) findViewById(R.id.tv_team_name);
        mTvTaskCount = (TextView) findViewById(R.id.tv_task_count);
        mProgressView = (CircleProgressView) findViewById(R.id.progress);


        String[] titles = {"近期任务", "已完成"};
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[1]));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.setTabTextColors(0xEEF5F5F5, Color.WHITE);
        List<Fragment> fragments = new ArrayList<>();

        initData();
        //近期任务
        MyTaskFragment unfinishfragment = new MyTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentTag.TAG_PROJECT_ID, mProjectBean.getId());
        bundle.putInt(IntentTag.TAG_TASK_STATUS, 0);
        unfinishfragment.setArguments(bundle);
        fragments.add(unfinishfragment);


        //完成任务
        Bundle bundle2 = new Bundle();
        bundle2.putInt(IntentTag.TAG_PROJECT_ID, mProjectBean.getId());
        bundle2.putInt(IntentTag.TAG_TASK_STATUS, 1);

        MyTaskFragment finishedfragment = new MyTaskFragment();
        finishedfragment.setArguments(bundle2);
        fragments.add(finishedfragment);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mPresenter = new ProjectDetailPresenterImpl(this);
        mPresenter.loadProject(mProjectBean.getId());
    }

    private void initData()
    {
        mProjectBean = (ProjectBean) getIntent().getSerializableExtra(IntentTag.TAG_PROJECT_BEAN);
        if (mProjectBean == null)
        {
            finish();
            return;
        }
        showProjectInfo(mProjectBean);
    }

    public void showProjectInfo(ProjectBean bean)
    {
        mTvProjectName.setText(bean.getName());
        if (bean.isPrivate())
            mTvTeamName.setText("私人项目");
        else if(bean.getTeam()!=null)
            mTvTeamName.setText(bean.getTeam().getTeamName());
    }

    @Override
    public void setupView()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mProjectBean.getName());
        }
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProject(ProjectBean bean)
    {
        float p;
        if (bean.getTaskCount() == 0)
        {
            p = 0f;
            mTvTaskCount.setText("无任务");
        }
        else
        {
            p = (bean.getTaskCount() - bean.getUnfinishTaskCount()) * 1.0f / bean.getTaskCount();
            mTvTaskCount.setText(String.format(Locale.getDefault(), "共%d个任务,已完成%d个", bean.getTaskCount(), (bean.getTaskCount() - bean.getUnfinishTaskCount())));
        }



        String percent = String.format(Locale.getDefault(), "%.1f", p * 100);
        if (percent.charAt(percent.length() - 1) == '0')
            percent = percent.substring(0, percent.length() - 2);

        mProgressView.setText(percent + "%");
        mProgressView.setPercent(p);
    }

    @Override
    public void showProgress()
    {

    }

    @Override
    public void hideProgress()
    {

    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }
}
