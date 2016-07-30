package com.hadesky.cacw.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.EditTaskPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.EditTaskView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.DateUtil;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.datatype.BmobDate;


public class EditTaskActivity extends BaseActivity implements EditTaskView, EditableMembersAdapter.OnMemberEditListener
{
    private Toolbar mToolbar;
    private View mStartDate;
    private View mStartTime;
    private View mEndDate;
    private View mEndTime;

    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvEndDate;
    private TextView mTvEndTime;

    private TextView mTvProject;


    private EditText mEdtTitle;
    private EditText mEdtLocation;
    private EditText mEdtDetail;
    private View mProject;

    private View mOk;
    private RecyclerView mRcv_members;
    private TaskBean mTask;
    private EditableMembersAdapter mAdapter;

    private EditTaskPresenterImpl mPresenter;

    private boolean newTask;//表示当前是新建任务还是编辑现有任务
    private List<ProjectBean> mProjectList;
    private AnimProgressDialog mProgressDialog;

    private Calendar mCalendarStart;
    private Calendar mCalendarEnd;
    private List<TaskMember> mMembers;


    @Override
    public int getLayoutId()
    {
        return R.layout.activity_edit_task;
    }

    @Override
    public void initView()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mStartTime = findViewById(R.id.start_time);
        mStartDate = findViewById(R.id.start_date);
        mProject = findViewById(R.id.project);
        mOk = findViewById(R.id.ok);

        mRcv_members = (RecyclerView) findViewById(R.id.rcv_members);
        mEdtTitle = (EditText) findViewById(R.id.edtTitle);
        mEdtLocation = (EditText) findViewById(R.id.edtLoaction);
        mEdtDetail = (EditText) findViewById(R.id.edtDetail);
        mTvProject = (TextView) findViewById(R.id.tvProject);

        mTvEndDate = (TextView) findViewById(R.id.tv_end_date);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mEndDate = findViewById(R.id.end_date);
        mEndTime = findViewById(R.id.end_time);
        mTvDate = (TextView) findViewById(R.id.tv_start_date);
        mTvTime = (TextView) findViewById(R.id.tv_start_time);
    }

    @Override
    public void setupView()
    {

        mProgressDialog = new AnimProgressDialog(this, false, null, "获取中");
        mToolbar.setTitle(R.string.edit_task);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //初始化RecycleView
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);


        //判定是新建来是编辑任务
        mTask = (TaskBean) getIntent().getSerializableExtra("task");
        if (mTask != null)
        {
            newTask = false;
            initDateAndTime();//这个方法一定要在newTask被赋值之后
            showTaskDetail(mTask);
        } else
        {
            newTask = true;
            mTask = new TaskBean();
            initDateAndTime();
        }

        mAdapter = new EditableMembersAdapter(new ArrayList<TaskMember>(), this, this, mTask);
        mAdapter.setAbleToAdd(true);
        mAdapter.setAbleToDelete(true);

        mRcv_members.setAdapter(mAdapter);


        mPresenter = new EditTaskPresenterImpl(this, mTask, newTask);
        mPresenter.loadTaskMember();
        setListener();
    }

    private void setListener()
    {
        //点击确定
        mOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveTask();
            }
        });


        //点击开始时间
        mStartTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openTimeDialog(true);
            }
        });
        //点击开始日期
        mStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDateDialog(true);
            }
        });
        //点击结束时间
        mEndTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openTimeDialog(false);
            }
        });
        //点击结束日期
        mEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDateDialog(false);
            }
        });
        //点击项目
        mProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!newTask)
                {
                    showMsg("不可修改任务项目");
                } else
                    mPresenter.loadProjects();
            }
        });
        //点击保存
        mOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveTask();
            }
        });

    }


    private void saveTask()
    {

        if (mTvDate.getText().length() == 0 || mTvTime.getText().length() == 0)
        {
            showToast(getString(R.string.please_select_Date));
            return;
        }
        if (mCalendarStart.compareTo(mCalendarEnd) > 0)
        {
            showToast("开始时间晚于结束时间");
            return;
        }

        mTask.setStartDate(new BmobDate(mCalendarStart.getTime()));
        mTask.setEndDate(new BmobDate(mCalendarEnd.getTime()));
        mTask.setTitle(mEdtTitle.getText().toString());
        mTask.setLocation(mEdtLocation.getText().toString());
        mTask.setContent(mEdtDetail.getText().toString());

        mPresenter.saveTask(mAdapter.getDatas());
    }

    private void openDateDialog(final boolean start)
    {
        Calendar c;
        if (start)
            c = mCalendarStart;
        else
            c = mCalendarEnd;

        int nyear = c.get(Calendar.YEAR);
        int nmonth = c.get(Calendar.MONTH);
        int nday = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog d = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                if (start)
                    mCalendarStart.set(year, monthOfYear, dayOfMonth);
                else
                    mCalendarEnd.set(year, monthOfYear, dayOfMonth);
                setDateTextView();
            }
        }, nyear, nmonth, nday);
        d.show();
    }


    private void openTimeDialog(final boolean start)
    {

        Calendar c;
        if (start)
            c = mCalendarStart;
        else
            c = mCalendarEnd;

        int nhour = c.get(Calendar.HOUR);
        int nmin = c.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(EditTaskActivity.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                if (start)
                {
                    mCalendarStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendarStart.set(Calendar.MINUTE, minute);
                } else
                {
                    mCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendarEnd.set(Calendar.MINUTE, minute);
                }

                setDateTextView();
            }
        }, nhour, nmin, true);
        dialog.show();
    }


    private void setDateTextView()
    {
        mTvDate.setText(String.format(Locale.US, "%d-%02d-%02d", mCalendarStart.get(Calendar.YEAR), mCalendarStart.get(Calendar.MONTH) + 1, mCalendarStart.get(Calendar.DAY_OF_MONTH)));
        mTvTime.setText(String.format(Locale.US, "%02d:%02d", mCalendarStart.get(Calendar.HOUR_OF_DAY), mCalendarStart.get(Calendar.MINUTE)));
        mTvEndDate.setText(String.format(Locale.US, "%d-%02d-%02d", mCalendarEnd.get(Calendar.YEAR), mCalendarEnd.get(Calendar.MONTH) + 1, mCalendarEnd.get(Calendar.DAY_OF_MONTH)));
        mTvEndTime.setText(String.format(Locale.US, "%02d:%02d", mCalendarEnd.get(Calendar.HOUR_OF_DAY), mCalendarEnd.get(Calendar.MINUTE)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (mAdapter.getMode() == EditableMembersAdapter.MODE_DELETE)
        {
            mAdapter.setMode(EditableMembersAdapter.MODE_NORMAL);
        } else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onMemberDelete(UserBean user)
    {

        if (user.getObjectId().equals(MyApp.getCurrentUser().getObjectId()))
        {
            showMsg("不能删除自己");
            return false;
        }
        return true;
    }

    @Override
    public void onAddMember() //当+号被点击时调用
    {
        if (mTask.getProjectBean() == null)
        {
            showToast("请先选择项目");
            return;
        }
        Intent i = new Intent(this, SelectMemberActivity.class);
        i.putExtra(IntentTag.TAG_Task_MEMBER, (ArrayList<?>) mMembers);
        i.putExtra(IntentTag.TAG_PROJECT_BEAN, mTask.getProjectBean());
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == Activity.RESULT_OK)
        {
            ArrayList<UserBean> list = (ArrayList<UserBean>) data.getSerializableExtra(IntentTag.TAG_USER_BEAN_LIST);
            if (list != null)
            {
                mMembers = new ArrayList<>();
                for(UserBean ub : list)
                {
                    TaskMember tm = new TaskMember();
                    tm.setTask(mTask);
                    tm.setUser(ub);
                    mMembers.add(tm);
                }
                mAdapter.setDatas(mMembers);
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showTaskMember(List<TaskMember> members)
    {
        mAdapter.setDatas(members);
        mMembers = members;
    }

    @Override
    public void closePage()
    {
        Intent i = new Intent();
        i.putExtra("task", mTask);
        setResult(MainActivity.result_task_change, i);
        finish();
    }

    @Override
    public void selectProject(List<ProjectBean> beanList)
    {

        if (beanList == null || beanList.size() == 0)
        {
            showMsg("当前没有项目");
            return;
        }

        mProjectList = beanList;
        final String[] items = new String[beanList.size()];
        for(int i = 0; i < beanList.size(); i++)
        {
            items[i] = beanList.get(i).getProjectName();
        }

        new AlertDialog.Builder(EditTaskActivity.this).setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (mTask.getProjectBean()!=null&& mTask.getProjectBean().getObjectId().equals(mProjectList.get(which).getObjectId()))
                    return;

                mTask.setProjectBean(mProjectList.get(which));
                mTvProject.setText(mProjectList.get(which).getProjectName());
                resetMembers();

            }
        }).show();
    }

    //编辑任务不可改项目，新建任务改项目后所有成员重置
    private void resetMembers()
    {
        mMembers = new ArrayList<>();
        TaskMember tm = new TaskMember();
        tm.setUser(MyApp.getCurrentUser());
        tm.setTask(mTask);
        mMembers.add(tm);
        mAdapter.setDatas(mMembers);
    }

    private void initDateAndTime()
    {
        if (newTask)
        {
            mCalendarStart = Calendar.getInstance();
            mCalendarEnd = Calendar.getInstance();
        } else
        {

            mCalendarStart = Calendar.getInstance();
            Date d = DateUtil.StringToDate(mTask.getStartDate().getDate());
            mCalendarStart.setTime(d);


            mCalendarEnd = Calendar.getInstance();
            Date ed = DateUtil.StringToDate(mTask.getEndDate().getDate());
            mCalendarEnd.setTime(ed);
        }
    }


    @Override
    public void showTaskDetail(TaskBean b)
    {

        mEdtTitle.setText(b.getTitle());
        mTvProject.setText(b.getProjectBean().getProjectName());
        mEdtLocation.setText(b.getLocation());
        mEdtDetail.setText(b.getContent());
        setDateTextView();
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
    public void showMsg(String msg)
    {
        showToast(msg);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
