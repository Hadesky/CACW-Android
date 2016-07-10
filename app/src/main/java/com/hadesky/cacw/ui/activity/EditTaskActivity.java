package com.hadesky.cacw.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.EditableMembersAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.EditTaskPresenterImpl;
import com.hadesky.cacw.ui.view.EditTaskView;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditTaskActivity extends BaseActivity implements EditTaskView, EditableMembersAdapter.OnMemberDeleteListener {
    private Toolbar mToolbar;
    private View mDate;
    private View mTime;
    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvProject;
    private EditText mEdtTitle;
    private EditText mEdtLocation;
    private EditText mEdtDetail;
    private View mProject;

    private View mOk;
    private RecyclerView mRcv_members;
    private TaskBean mTask;
    private EditableMembersAdapter mAdapter;
    // TODO: 2016/7/6 0006 换成接口
    private EditTaskPresenterImpl mPresenter;

    private boolean newTask;//表示当前是新建任务还是编辑现有任务
    private List<ProjectBean> mProjectList;
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_task;
    }

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTime = findViewById(R.id.time);
        mDate = findViewById(R.id.date);
        mProject = findViewById(R.id.project);
        mOk = findViewById(R.id.ok);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mRcv_members = (RecyclerView) findViewById(R.id.rcv_members);
        mEdtTitle = (EditText) findViewById(R.id.edtTItle);
        mEdtLocation = (EditText) findViewById(R.id.edtLoaction);
        mEdtDetail = (EditText) findViewById(R.id.edtDetail);
        mTvProject = (TextView) findViewById(R.id.tvProject);

    }

    @Override
    public void setupView() {
        mToolbar.setTitle(R.string.edit_task);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //点击确定
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveTask(mAdapter.getDatas());
            }
        });


        //初始化RecycleView
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);

        mAdapter = new EditableMembersAdapter(new ArrayList<TaskMember>(), this, this);
        mAdapter.setAbleToAdd(true);
        mAdapter.setAbleToDelete(true);

        mRcv_members.setAdapter(mAdapter);

        //判定是新建来是编辑任务
        mTask = (TaskBean) getIntent().getSerializableExtra("task");
        if (mTask != null)
        {
            newTask = false;
            showTaskDetail(mTask);
        }
        else
        {
            newTask = true;
            mTask = new TaskBean();
        }

        mPresenter = new EditTaskPresenterImpl(this,mTask,newTask);
        mPresenter.loadTaskMember();

        setupDataOnClick();
        setupTimeOnclick();
        setupProjectClick();
    }


    private void setupDataOnClick() {

        Calendar c = Calendar.getInstance();
        final int nyear = c.get(Calendar.YEAR);
        final int nmonth = c.get(Calendar.MONTH);
        final int nday = c.get(Calendar.DAY_OF_MONTH);


        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(year, monthOfYear, dayOfMonth);
                    }
                }, nyear, nmonth, nday);
                d.show();
            }
        });
    }


    private void setupTimeOnclick() {

        Calendar c = Calendar.getInstance();
        final int nhour = c.get(Calendar.HOUR);
        final int nmin = c.get(Calendar.MINUTE);


        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(EditTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTime(view.getCurrentHour(), minute);
                    }
                }, nhour, nmin, true);
                dialog.show();
            }
        });
    }


    private void setupProjectClick()
    {
        //选择项目
        mProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadProjects();
            }
        });
    }

    public void setTime(int hour, int min) {

        mTvTime.setText(String.format("%02d : %02d", hour, min));
    }

    public void setDate(int year, int month, int day) {
        mTvDate.setText(String.format("%d-%d-%d", year, month + 1, day));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.getMode() == EditableMembersAdapter.MODE_DELETE) {
            mAdapter.setMode(EditableMembersAdapter.MODE_NORMAL);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onMemberDelete(UserBean user_id) {

        return true;
    }

    @Override
    public void showTaskMember(List<TaskMember> members) {
            mAdapter.setDatas(members);
    }

    @Override
    public void closePage() {
        this.finish();
    }

    @Override
    public void selectProject(List<ProjectBean> beanList) {

        if (beanList==null||beanList.size()==0)
        {
            showMsg("当前没有项目");
            return;
        }

        mProjectList = beanList;
        final String[] items = new String[beanList.size()];
        for (int i = 0; i < beanList.size(); i++) {
            items[i] = beanList.get(i).getProjectName();
        }

        new AlertDialog.Builder(EditTaskActivity.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTask.setProjectBean(mProjectList.get(which));
                mTvProject.setText(mProjectList.get(which).getProjectName());
            }
        }).show();
    }

    @Override
    public void showTaskDetail(TaskBean b) {

        mEdtTitle.setText(b.getTitle());
        mTvDate.setText(b.getStartDate().getDate());
        // TODO: 2016/7/6 0006 这里BmobData只有 getDate，没有时间？
        mTvTime.setText(b.getStartDate().getDate());
        mTvProject.setText(b.getProjectBean().getProjectName());
        mEdtLocation.setText(b.getLocation());
        mEdtDetail.setText(b.getLocation());
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgess() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
