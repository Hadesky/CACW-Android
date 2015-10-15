package com.hadesky.cacw.ui;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hadesky.cacw.R;

public class TaskDetailActivity extends BaseActivity
{

    private Toolbar mToolbar;
    private TextView mTitle;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_task_detail;
    }

    @Override
    public void initView()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void setupView()
    {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
