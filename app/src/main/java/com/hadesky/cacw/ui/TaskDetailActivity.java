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
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.TaskDetailPresenter;
import com.hadesky.cacw.presenter.TaskDetailPresenterImpl;
import com.hadesky.cacw.ui.View.TaskDetailView;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends BaseActivity implements View.OnClickListener,TaskDetailView
{

    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mLocation;
    private TextView mDetail;
    private TextView mProject;

    private RecyclerView mRcv_members;
    private View mEdit;
    private ScrollView mScrollView;
    private TaskDetailPresenter mPresenter;
    private Long mTaskId;
    private TaskMembersAdapter mAdapter;


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
        mLocation = (TextView) findViewById(R.id.tv_location);
        mDetail = (TextView) findViewById(R.id.tv_detail);
        mProject = (TextView) findViewById(R.id.tv_projectname);
    }

    @Override
    public void setupView()
    {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);
        mAdapter = new TaskMembersAdapter(this, new ArrayList<UserBean>());
        mRcv_members.setAdapter(mAdapter);

        mEdit.setOnClickListener(this);

        //mScrollView.scrollTo(0,0);
        mScrollView.setVerticalFadingEdgeEnabled(false);

        mTaskId = getIntent().getLongExtra("id", 0);

        mPresenter = new TaskDetailPresenterImpl(this);
        mPresenter.LoadTask(mTaskId);
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


    @Override
    public void showInfo(TaskBean task)
    {
        mTitle.setText(task.getTitle());
        mProject.setText(task.getProjectName());
        mDetail.setText(task.getContent());
        mLocation.setText(task.getLocation());

        List<UserBean> list = task.getMembers();


        mAdapter.setDatas(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowMember(List<UserBean> user)
    {

    }

    @Override
    public void showProgress()
    {

    }

    @Override
    public void hideProgress()
    {

    }
}
