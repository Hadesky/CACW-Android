package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.presenter.MyTaskPresenter;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.BaseActivity;
import com.hadesky.cacw.ui.activity.MainActivity;
import com.hadesky.cacw.ui.activity.TaskDetailActivity;
import com.hadesky.cacw.ui.widget.CircleTextView;
import com.hadesky.cacw.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 任务列表Adapter
 * Created by ziyue on 2015/7/24 0024.
 */
public class MyTaskRecyclerAdapter extends BaseAdapter<TaskBean>
{

    private MyTaskPresenter mPresenter;
    private String mToday;
    private String mTomorow;

    public MyTaskRecyclerAdapter(List<TaskBean> list, MyTaskPresenter presenter, @LayoutRes int resId) {
        super(list, resId);
        mPresenter = presenter;
        SimpleDateFormat format = DateUtil.getSimpleDateFormat();
        Calendar today = Calendar.getInstance();
        mToday = format.format(today.getTime());
        today.add(Calendar.DATE,1);
        mTomorow = format.format(today.getTime());
    }

    @Override
    public BaseViewHolder<TaskBean> createHolder(View v, Context context) {

        final BaseViewHolder<TaskBean> holder = new BaseViewHolder<TaskBean>(v) {
            @Override
            public void setData(TaskBean task) {
                setTextView(R.id.tv_title, task.getTitle());
                //显示日期


                String str = task.getStartDate().substring(0, 10);
                if(mToday.startsWith(str))
                    str = "今天";
                else if(mTomorow.startsWith(str))
                    str = "明天";

                setTextView(R.id.tv_start_date, str);
                CircleTextView v = findView(R.id.icon);
                v.setText(task.getProject().getName());
            }
        };

        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                //打开任务详情
                Intent i = new Intent(mContext, TaskDetailActivity.class);
                i.putExtra(IntentTag.TAG_TASK_BEAN, mDatas.get(position));
                ((BaseActivity) mContext).startActivityForResult(i, MainActivity.RequestCode_TaskChange);
            }
        });

        holder.setOnItemLongClickListener(new BaseViewHolder.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(View view, final int position) {
                if (mDatas.get(position).isFinish())
                    return true;

                new AlertDialog.Builder(mContext).setItems(new String[]{"完成"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            mPresenter.CompleteTask(mDatas.get(position));
                    }
                }).show();
                return true;
            }
        });
        return holder;

    }
}


