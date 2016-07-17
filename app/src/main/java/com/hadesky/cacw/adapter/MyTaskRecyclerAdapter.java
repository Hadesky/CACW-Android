package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.presenter.MyTaskPresenter;
import com.hadesky.cacw.ui.activity.BaseActivity;
import com.hadesky.cacw.ui.activity.MainActivity;
import com.hadesky.cacw.ui.activity.TaskDetailActivity;

import java.util.List;

/**
 * 任务列表Adapter
 * Created by ziyue on 2015/7/24 0024.
 */
public class MyTaskRecyclerAdapter extends BaseAdapter<TaskMember>
{

    private MyTaskPresenter mPresenter;

    public MyTaskRecyclerAdapter(List<TaskMember> list, MyTaskPresenter presenter, @LayoutRes int resId)
    {
        super(list,resId);
        mPresenter = presenter;
    }

    @Override
    public BaseViewHolder<TaskMember> createHolder(View v, Context context) {

        final BaseViewHolder<TaskMember> holder = new BaseViewHolder<TaskMember>(v){
            @Override
            public void setData(TaskMember o) {
                  setTextView(R.id.tv_title,o.getTask().getTitle());

                  String str =  o.getTask().getStartDate().getDate().substring(0,10);
                 setTextView(R.id.tv_start_date,str);
            }
        };

        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener()
        {
            @Override
            public void OnItemClick(View view, int position)
            {
                //打开任务详情
                Intent i = new Intent(mContext, TaskDetailActivity.class);
                i.putExtra("task",mDatas.get(position));
                ((BaseActivity)mContext).startActivityForResult(i, MainActivity.RequestCode_TaskChange);
            }
        });

        holder.setOnItemLongClickListener(new BaseViewHolder.OnItemLongClickListener()
        {
            @Override
            public boolean OnItemLongClick(View view, final int position)
            {

                new AlertDialog.Builder(mContext).setItems(new String[]{"完成"}, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
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


