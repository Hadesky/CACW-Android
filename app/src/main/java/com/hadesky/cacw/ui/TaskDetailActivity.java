package com.hadesky.cacw.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.TaskMembersAdapter;
import com.hadesky.cacw.bean.MemberBean;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends BaseActivity implements View.OnClickListener
{

    private Toolbar mToolbar;
    private TextView mTitle;
    private RecyclerView mRcv_members;
    private View mEdit;
    private ScrollView mScrollView;
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
        mRcv_members = (RecyclerView) findViewById(R.id.rcv_members);
        mEdit =  findViewById(R.id.btn_edit);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    @Override
    public void setupView()
    {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        List<MemberBean> list = new ArrayList<>();
        for (int i=0;i<10;i++)
            list.add(new MemberBean("谢伟鹏",R.drawable.default_user_image));

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);
        mRcv_members.setAdapter(new TaskMembersAdapter(this, list));

        mEdit.setOnClickListener(this);

        //mScrollView.scrollTo(0,0);
        mScrollView.setVerticalFadingEdgeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    //点击编辑
    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(this,EditTaskActivity.class);
        startActivity(i);
    }
}
