package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.TaskMembersAdapter;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.TaskDetailPresenter;
import com.hadesky.cacw.presenter.TaskDetailPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.TaskDetailView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.DateUtil;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskDetailActivity extends BaseActivity implements View.OnClickListener, TaskDetailView
{

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
    private View mBtnDelTask;
    private NestedScrollView mScrollView;
    private TaskDetailPresenter mPresenter;
    private TaskMembersAdapter mAdapter;
    private TaskBean mTask;
    private AnimProgressDialog mProgressDialog;
    private ArrayList<UserBean> mMembers;

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
        mScrollView = (NestedScrollView) findViewById(R.id.scrollView);
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
    public void setupView()
    {
        mProgressDialog = new AnimProgressDialog(this, false, null, "获取中");


        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);
        mAdapter = new TaskMembersAdapter(this, new ArrayList<UserBean>());
        mRcv_members.setAdapter(mAdapter);

        mBtnDelTask.setOnClickListener(this);

        //mScrollView.scrollTo(0,0);
        mScrollView.setVerticalFadingEdgeEnabled(false);

        TaskBean tm = (TaskBean) getIntent().getSerializableExtra(IntentTag.TAG_TASK_BEAN);
        if (tm == null)
        {
            finish();
            return;
        }


        mTask = tm;
        mPresenter = new TaskDetailPresenterImpl(this, mTask);
        showInfo(mTask);
        mPresenter.LoadTaskMember();

    }


    //点击删除
    @Override
    public void onClick(View v)
    {

       if (v.getId() == R.id.btn_del)
        {
            new AlertDialog.Builder(this).setMessage(R.string.confirm_to_del_task).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    mPresenter.onDeleteTask();
                }
            }).setNegativeButton(R.string.cancel, null).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == MainActivity.result_task_change)
        {
            setResult(resultCode);
            if (data != null)
            {
                TaskBean taskBean = (TaskBean) data.getSerializableExtra(IntentTag.TAG_TASK_BEAN);
                if (taskBean != null)
                {
                    mTask = taskBean;
                    showInfo(mTask);
                    mPresenter.LoadTaskMember();
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showInfo(TaskBean task)
    {
        mTask = task;
        mTitle.setText(task.getTitle());
        mProject.setText(task.getProject().getName());
        mDetail.setText(task.getContent());
        mLocation.setText(task.getLocation());

        Calendar start = DateUtil.StringToCalendar(task.getStartDate());
        Calendar end = DateUtil.StringToCalendar(task.getEndDate());


        mTvStartDate.setText(String.format(Locale.US, "%d-%02d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH)));
        mTvStartTime.setText(String.format(Locale.US, "%02d:%02d", start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE)));
        mTvEndDate.setText(String.format(Locale.US, "%d-%02d-%02d", end.get(Calendar.YEAR), end.get(Calendar.MONTH) + 1, end.get(Calendar.DAY_OF_MONTH)));
        mTvEndTime.setText(String.format(Locale.US, "%02d:%02d", end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE)));

        //判断显示删除按钮
        if (mTask.getAdminId() != MyApp.getCurrentUser().getId())
        {
            mBtnDelTask.setVisibility(View.INVISIBLE);
        }
    }

    private void onEditTaskClick()
    {
        if (mTask.getAdminId() != MyApp.getCurrentUser().getId())
        {
            showToast(getString(R.string.you_are_admin_can_not_exit));
            return;
        }
        if(mTask.isFinish())
        {
            showToast(getString(R.string.can_not_edit_finished_task));
            return;
        }


        Intent i = new Intent(this, EditTaskActivity.class);
        i.putExtra(IntentTag.TAG_TASK_BEAN, mTask);
        i.putExtra(IntentTag.TAG_Task_MEMBER,mMembers);
        startActivityForResult(i, MainActivity.RequestCode_TaskChange);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        if (item.getItemId() == R.id.action_edit)
        {
            onEditTaskClick();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public void ShowMember(List<UserBean> users)
    {
        mMembers = (ArrayList<UserBean>) users;
        mAdapter.setDatas(users);
    }


    @Override
    public void closeActivity()
    {
        setResult(MainActivity.result_task_change);
        finish();
    }

    @Override
    public void showProgress()
    {
        mProgressDialog.show();
    }

    @Override
    public void hideProgress()
    {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }
}
