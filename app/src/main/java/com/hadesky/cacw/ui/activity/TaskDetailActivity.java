package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.TaskDetailPresenter;
import com.hadesky.cacw.presenter.TaskDetailPresenterImpl;
import com.hadesky.cacw.ui.view.TaskDetailView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.DateUtil;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskDetailActivity extends BaseActivity implements View.OnClickListener, TaskDetailView {

    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mLocation;
    private TextView mDetail;
    private TextView mProject;

    private TextView mTvStartDate;
    private TextView mTvStartTime;
    private TextView mTvEndDate;
    private TextView mTvEndTime;


    private RecyclerView mRcv_members;
    private View mBtnEditTask;
    private View mBtnDelTask;
    private ScrollView mScrollView;
    private TaskDetailPresenter mPresenter;
    private TaskMembersAdapter mAdapter;
    private TaskBean mTask;
    private AnimProgressDialog mProgressDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_task_detail;
    }

    @Override
    public void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mRcv_members = (RecyclerView) findViewById(R.id.rcv_members);
        mBtnEditTask = findViewById(R.id.btn_edit);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mLocation = (TextView) findViewById(R.id.tv_location);
        mDetail = (TextView) findViewById(R.id.tv_detail);
        mProject = (TextView) findViewById(R.id.tv_projectname);
        mBtnDelTask = findViewById(R.id.btn_del);


        mTvEndDate = (TextView) findViewById(R.id.tv_end_date);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mTvStartDate = (TextView) findViewById(R.id.tv_start_date);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);
    }

    @Override
    public void setupView() {
        mProgressDialog = new AnimProgressDialog(this, false, null, "获取中");


        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);
        mAdapter = new TaskMembersAdapter(this, new ArrayList<TaskMember>());
        mRcv_members.setAdapter(mAdapter);

        mBtnEditTask.setOnClickListener(this);
        mBtnDelTask.setOnClickListener(this);

        //mScrollView.scrollTo(0,0);
        mScrollView.setVerticalFadingEdgeEnabled(false);

        TaskMember tm = (TaskMember) getIntent().getSerializableExtra("task");
        if (tm == null) {
            showToast("数据错误");
            finish();
            return;
        }
        mTask = tm.getTask();
        showInfo(mTask);

        mPresenter = new TaskDetailPresenterImpl(this, mTask);
        mPresenter.LoadTaskMember();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    //点击编辑
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_edit) {
            Intent i = new Intent(this, EditTaskActivity.class);
            i.putExtra("task", mTask);
            startActivityForResult(i, MainActivity.RequestCode_TaskChange);
        } else if (v.getId() == R.id.btn_del) {
            new AlertDialog.Builder(this).setTitle("你确定要删除任务吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.onDeleteTask();
                }
            }).setNegativeButton("取消", null).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == MainActivity.result_task_change)
        {
            setResult(resultCode);
            if (data != null) {
                TaskBean taskBean = (TaskBean) data.getSerializableExtra("task");
                if (taskBean != null) {
                    mTask = taskBean;
                    showInfo(mTask);
                    mPresenter.LoadTaskMember();
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showInfo(TaskBean task) {
        mTitle.setText(task.getTitle());
        mProject.setText(task.getProjectBean().getProjectName());
        mDetail.setText(task.getContent());
        mLocation.setText(task.getLocation());

        Calendar start = DateUtil.StringToCalendar(task.getStartDate().getDate());
        Calendar end = DateUtil.StringToCalendar(task.getEndDate().getDate());


        mTvStartDate.setText(String.format(Locale.US, "%d-%02d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH)));
        mTvStartTime.setText(String.format(Locale.US, "%02d:%02d", start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE)));
        mTvEndDate.setText(String.format(Locale.US, "%d-%02d-%02d", end.get(Calendar.YEAR), end.get(Calendar.MONTH) + 1, end.get(Calendar.DAY_OF_MONTH)));
        mTvEndTime.setText(String.format(Locale.US, "%02d:%02d", end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE)));

        if (!task.getAdaminUserId().equals(MyApp.getCurrentUser().getObjectId())) {
            mBtnEditTask.setVisibility(View.INVISIBLE);
            mBtnDelTask.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void ShowMember(List<TaskMember> users) {
        mAdapter.setDatas(users);
    }


    @Override
    public void closeActivity() {
        setResult(MainActivity.result_task_change);
        finish();
    }

    @Override
    public void showProgress() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }
}
