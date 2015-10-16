package com.hadesky.cacw.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.TaskMembersAdapter;
import com.hadesky.cacw.bean.TaskMemberBean;
import com.hadesky.cacw.widget.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditTaskActivity extends BaseActivity
{

    private  Toolbar mToolbar;
    private View mDate;
    private View mTime;
    private View mProject;
    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvProject;
    private View mOk;
    private RecyclerView mRcv_members;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_edit_task;
    }

    @Override
    public void initView()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTime = (View) findViewById(R.id.time);
        mDate = (View) findViewById(R.id.date);
        mProject = (View) findViewById(R.id.project);
        mOk = (View) findViewById(R.id.ok);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mRcv_members = (RecyclerView) findViewById(R.id.rcv_members);

    }

    @Override
    public void setupView()
    {
        mToolbar.setTitle(R.string.edit_task);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        mProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(EditTaskActivity.this).setItems(new String[]{"项目1", "项目2"}, null).show();
            }
        });


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);//这个4后期需经过计算得出
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcv_members.setLayoutManager(manager);
        mRcv_members.setVerticalFadingEdgeEnabled(false);



        List<TaskMemberBean> list = new ArrayList<>();
        for (int i = 0;i<5;i++)
                list.add(new TaskMemberBean());
        list.add(new TaskMemberBean(2));
            mRcv_members.setAdapter(new TaskMembersAdapter(this, list));



        setupDataOnClick();
        setupTimeOnclick();
    }



    private void setupDataOnClick()
    {

        Calendar c = Calendar.getInstance();
        final int nyear = c.get(Calendar.YEAR);
        final int nmonth = c.get(Calendar.MONTH);
        final int nday = c.get(Calendar.DAY_OF_MONTH);


        mDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                DatePickerDialog d = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        setDate(year, monthOfYear, dayOfMonth);
                    }
                }, nyear, nmonth, nday);
                d.show();

            }
        });
    }



    private void setupTimeOnclick()
    {

        Calendar c = Calendar.getInstance();
        final int nhour = c.get(Calendar.HOUR);
        final int nmin = c.get(Calendar.MINUTE);


        mTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerDialog dialog = new TimePickerDialog(EditTaskActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        setTime(hourOfDay, minute);

                    }
                }, nhour, nmin, false);
                dialog.show();
            }
        });
    }

    public void setTime(int hour,int min)
    {
        if (hour>12)
            hour = hour -12;
        String t = hour>12?"下午":"上午";

        String s = t+" "+hour+":"+min;
        mTvTime.setText(s);
    }

   public void setDate(int year,int month,int day)
   {
       String s = year+"-"+month+"-"+day;
       Log.i("tag",s);
       mTvDate.setText(s);
   }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
