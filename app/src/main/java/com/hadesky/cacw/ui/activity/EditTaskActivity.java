package com.hadesky.cacw.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.EditableMembersAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.EditTaskPresenter;
import com.hadesky.cacw.presenter.EditTaskPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.EditTaskView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.ui.widget.DateTimePickerDialog;
import com.hadesky.cacw.util.DateUtil;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EditTaskActivity extends BaseActivity implements EditTaskView, EditableMembersAdapter.OnMemberEditListener
{
    private Toolbar mToolbar;

    private TextView mTvStartDate;
    private TextView mTvStartTime;
    private TextView mTvEndDate;
    private TextView mTvEndTime;

    private TextView mTvProject;


    private EditText mEdtTitle;
    private EditText mEdtLocation;
    private EditText mEdtDetail;
    private View mProject;

    private RecyclerView mRcvMembers;
    private TaskBean mTask;
    private EditableMembersAdapter mAdapter;

    private EditTaskPresenter mPresenter;

    private boolean newTask;//表示当前是新建任务还是编辑现有任务
    private List<ProjectBean> mProjectList;
    private AnimProgressDialog mProgressDialog;

    private Calendar mCalendarStart;
    private Calendar mCalendarEnd;
    private int mStartHourOfDay, mStartMinute, mEndHourOfDay, mEndMinute;
    private List<UserBean> mMembers;
    private DateTimePickerDialog mDateTimePickerDialog;

    private boolean isEdited = false;//是否已经更改

    public static final String TIME_FORMAT = "%02d:%02d";
    public static final String DATE_FORMAT = "%d-%02d-%02d";

    private DateTimePickerDialog.Callback mDateTimeCallback = new DateTimePickerDialog.Callback()
    {
        @Override
        public void onCancelled()
        {
            if (mDateTimePickerDialog != null)
            {
                mDateTimePickerDialog.dismiss();
            }
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, int startOrEnd)
        {

            if (selectedDate.getType() == SelectedDate.Type.RANGE)
            {
                //选择了一个范围，直接用来做开始和结束
                mCalendarStart = selectedDate.getStartDate();
                mCalendarEnd = selectedDate.getEndDate();
                mStartMinute = mEndMinute = minute;
                mStartHourOfDay = mEndHourOfDay = hourOfDay;
            } else if (startOrEnd == DateTimePickerDialog.TYPE_START)
            {
                mCalendarStart = selectedDate.getStartDate();
                if (mCalendarEnd == null || mCalendarEnd.compareTo(mCalendarStart) < 0)
                {
                    //将end和start置同
                    mCalendarEnd = mCalendarStart;
                    mStartMinute = mEndMinute = minute;
                    mStartHourOfDay = mEndHourOfDay = hourOfDay;
                } else
                {
                    mStartHourOfDay = hourOfDay;
                    mStartMinute = minute;
                }
            } else
            {
                //结束
                mCalendarEnd = selectedDate.getStartDate();
                if (mCalendarStart == null || mCalendarStart.compareTo(mCalendarEnd) > 0)
                {
                    //将end和start置同
                    mCalendarStart = mCalendarEnd;
                    mStartMinute = mEndMinute = minute;
                    mStartHourOfDay = mEndHourOfDay = hourOfDay;
                } else
                {
                    mEndHourOfDay = hourOfDay;
                    mEndMinute = minute;
                }
            }
            adjustDateTimeDisplay();
            isEdited = true;
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            isEdited = true;
        }
    };

    private void adjustDateTimeDisplay()
    {
        if (mCalendarStart != null && mCalendarEnd != null)
        {
            mTvStartDate.setText(String.format(Locale.getDefault(), DATE_FORMAT, mCalendarStart.get(Calendar.YEAR), mCalendarStart.get(Calendar.MONTH)+1, mCalendarStart.get(Calendar.DAY_OF_MONTH)));
            mTvEndDate.setText(String.format(Locale.getDefault(), DATE_FORMAT, mCalendarEnd.get(Calendar.YEAR), mCalendarEnd.get(Calendar.MONTH)+1, mCalendarEnd.get(Calendar.DAY_OF_MONTH)));
        }
        mTvStartTime.setText(String.format(Locale.getDefault(), TIME_FORMAT, mStartHourOfDay, mStartMinute));
        mTvEndTime.setText(String.format(Locale.getDefault(), TIME_FORMAT, mEndHourOfDay, mEndMinute));
    }


    @Override
    public int getLayoutId()
    {
        return R.layout.activity_edit_task;
    }

    @Override
    public void initView()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProject = findViewById(R.id.project);


        mRcvMembers = (RecyclerView) findViewById(R.id.rcv_members);
        mEdtTitle = (EditText) findViewById(R.id.et_title);
        mEdtLocation = (EditText) findViewById(R.id.edtLoaction);
        mEdtDetail = (EditText) findViewById(R.id.edtDetail);
        mTvProject = (TextView) findViewById(R.id.tvProject);

        mTvEndDate = (TextView) findViewById(R.id.tv_end_date);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);

        mTvStartDate = (TextView) findViewById(R.id.tv_start_date);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);


        mDateTimePickerDialog = new DateTimePickerDialog();
        mDateTimePickerDialog.setCallback(mDateTimeCallback);
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        options.setDisplayOptions(displayOptions);
        options.setCanPickDateRange(true);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DateTimePickerDialog.SUBLIME_OPTIONS, options);
        mDateTimePickerDialog.setArguments(bundle);
        mDateTimePickerDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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
        mRcvMembers.setLayoutManager(manager);
        mRcvMembers.setVerticalFadingEdgeEnabled(false);
        mAdapter = new EditableMembersAdapter(new ArrayList<UserBean>(), this, this, mTask);
        mAdapter.setAbleToAdd(true);
        mAdapter.setAbleToDelete(true);
        mRcvMembers.setAdapter(mAdapter);

        //判定是新建还是编辑任务
        mTask = (TaskBean) getIntent().getSerializableExtra(IntentTag.TAG_TASK_BEAN);
        mMembers = (List<UserBean>) getIntent().getSerializableExtra(IntentTag.TAG_Task_MEMBER);
        if (mTask != null)
        {
            newTask = false;
            initDateAndTime();//这个方法一定要在newTask被赋值之后
            showTaskDetail(mTask);
            showTaskMember(mMembers);
        } else
        {
            newTask = true;
            mTask = new TaskBean();
            mMembers = new ArrayList<>();
            mMembers.add(MyApp.getCurrentUser());
            initDateAndTime();
        }
        mPresenter = new EditTaskPresenterImpl(this, mTask);
        setListener();
    }

    private void setListener()
    {

        //给所有的EditText添加监听，以判断是否被修改过
        mEdtDetail.addTextChangedListener(mTextWatcher);
        mEdtLocation.addTextChangedListener(mTextWatcher);
        mEdtTitle.addTextChangedListener(mTextWatcher);


        findViewById(R.id.bt_start_date_time).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showStartTimeDialog();
            }
        });
        findViewById(R.id.bt_end_date_time).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showEndTimeDialog();
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
                    showMsg(getString(R.string.can_not_modify_project));
                } else
                {
                    mPresenter.loadProjects();
                    isEdited = true;
                }
            }
        });
    }

    private void showEndTimeDialog()
    {
        mDateTimePickerDialog.show(getSupportFragmentManager(), "end");
    }

    private void showStartTimeDialog()
    {
        mDateTimePickerDialog.show(getSupportFragmentManager(), "start");
    }


    private void saveTask()
    {
        if (mTvStartDate.getText().length() == 0 || mTvStartTime.getText().length() == 0)
        {
            showToast(getString(R.string.please_select_Date));
            return;
        }
        if (mCalendarStart.compareTo(mCalendarEnd) > 0)
        {
            showToast("开始时间晚于结束时间");
            return;
        }

        mTask.setStartDate(DateUtil.getSimpleDateFormat().format(mCalendarStart.getTime()));
        mTask.setEndDate(DateUtil.getSimpleDateFormat().format(mCalendarEnd.getTime()));
        mTask.setTitle(mEdtTitle.getText().toString());
        mTask.setLocation(mEdtLocation.getText().toString());
        mTask.setContent(mEdtDetail.getText().toString());
        if (newTask)
        {
            if (mTask.getProject() == null)
            {
                showMsg("请先选择项目");
                return;
            }
            mPresenter.createTask(mMembers);
        } else
        {
            mPresenter.saveTask();
        }
    }

    @Deprecated
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

    @Deprecated
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
        mTvStartDate.setText(String.format(Locale.US, "%d-%02d-%02d", mCalendarStart.get(Calendar.YEAR), mCalendarStart.get(Calendar.MONTH) + 1, mCalendarStart.get(Calendar.DAY_OF_MONTH)));
        mTvStartTime.setText(String.format(Locale.US, "%02d:%02d", mCalendarStart.get(Calendar.HOUR_OF_DAY), mCalendarStart.get(Calendar.MINUTE)));
        mTvEndDate.setText(String.format(Locale.US, "%d-%02d-%02d", mCalendarEnd.get(Calendar.YEAR), mCalendarEnd.get(Calendar.MONTH) + 1, mCalendarEnd.get(Calendar.DAY_OF_MONTH)));
        mTvEndTime.setText(String.format(Locale.US, "%02d:%02d", mCalendarEnd.get(Calendar.HOUR_OF_DAY), mCalendarEnd.get(Calendar.MINUTE)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        if (item.getItemId() == R.id.action_save)
        {
            saveTask();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (mAdapter.getMode() == EditableMembersAdapter.MODE_DELETE)
        {
            mAdapter.setMode(EditableMembersAdapter.MODE_NORMAL);
        } else if (isEdited)
        {
            new AlertDialog.Builder(this).setMessage("是否保存改动？").setNegativeButton("舍弃", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    finish();
                }
            }).setNeutralButton("取消", null).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    saveTask();
                }
            }).create().show();
        } else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onMemberDelete(UserBean user)
    {

        if (user.getId() == MyApp.getCurrentUser().getId())
        {
            showMsg("不能删除自己");
            return false;
        }
        isEdited = true;
        return true;
    }

    @Override
    public void onAddMember() //当+号被点击时调用
    {
        if (mTask.getProject() == null)
        {
            showToast("请先选择项目");
            return;
        }
        if (mTask.getProject().isPrivate())
        {
            showToast("个人项目不是添加成员");
            return;
        }

        Intent i = new Intent(this, SelectMemberActivity.class);
        i.putExtra(IntentTag.TAG_PROJECT_ID, mTask.getProject().getId());
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //从选择成员界面回来
        if (resultCode == Activity.RESULT_OK)
        {
            ArrayList<UserBean> list = (ArrayList<UserBean>) data.getSerializableExtra(IntentTag.TAG_USER_BEAN_LIST);
            if (list != null)
            {
                //                mMembers = new ArrayList<>();
                //                for(UserBean ub : list)
                //                {
                //                    TaskMember tm = new TaskMember();
                //                    tm.setTask(mTask);
                //                    tm.setUser(ub);
                //                    mMembers.add(tm);
                //                }
                //                mAdapter.setDatas(mMembers);
                isEdited = true;
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showTaskMember(List<UserBean> members)
    {
        mAdapter.setDatas(members);
        mMembers = members;
    }

    @Override
    public void close()
    {
        Intent i = new Intent();
        //如果是任务详情过来的，告诉任务详情页面 新的taskbean
        if (!newTask)
        {
            if (isEdited)
                i.putExtra(IntentTag.TAG_TASK_BEAN, mTask);
            else
            {
                finish();
                return;
            }
        }
        setResult(MainActivity.result_task_change, i);
        finish();
    }

    @Override
    public void showProjects(List<ProjectBean> beanList)
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
            items[i] = beanList.get(i).getName();
        }

        new AlertDialog.Builder(EditTaskActivity.this).setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //如果项目没有变
                if (mTask.getProject() != null && mTask.getProject().equals(mProjectList.get(which)))
                    return;

                mTask.setProject(mProjectList.get(which));
                mTvProject.setText(mProjectList.get(which).getName());
                resetMembers();
            }
        }).show();
    }

    //编辑任务不可改项目，新建任务该项目后所有成员重置
    private void resetMembers()
    {
        mMembers = new ArrayList<>();
        mMembers.add(MyApp.getCurrentUser());
        mAdapter.setDatas(mMembers);
        isEdited = true;
    }

    private void initDateAndTime()
    {
        if (newTask)
        {
            mCalendarStart = Calendar.getInstance();
            mCalendarEnd = Calendar.getInstance();
            mStartMinute = mEndMinute = mCalendarStart.get(Calendar.MINUTE);
            mStartHourOfDay = mEndHourOfDay = mCalendarStart.get(Calendar.HOUR_OF_DAY);
            adjustDateTimeDisplay();
        } else
        {
            mCalendarStart = Calendar.getInstance();
            Date d = DateUtil.StringToDate(mTask.getStartDate());
            mCalendarStart.setTime(d);

            mCalendarEnd = Calendar.getInstance();
            Date ed = DateUtil.StringToDate(mTask.getEndDate());
            mCalendarEnd.setTime(ed);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public void showTaskDetail(TaskBean b)
    {

        mEdtTitle.setText(b.getTitle());
        mTvProject.setText(b.getProject().getName());
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
        if (mPresenter != null)
            mPresenter.onDestroy();
    }
}
