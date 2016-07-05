package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.presenter.MyTaskPresenter;
import com.hadesky.cacw.ui.activity.TaskDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务列表Adapter
 * Created by ziyue on 2015/7/24 0024.
 */
public class MyTaskRecyclerAdapter extends RecyclerView.Adapter<MyTaskRecyclerAdapter.MyTaskViewHolder>
{

    private Context mContext;
    private List<TaskBean> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    private MyTaskPresenter mPresenter;

    public MyTaskRecyclerAdapter(Context context, MyTaskPresenter presenter)
    {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        mPresenter = presenter;
    }

    public void setDatas(List<TaskBean> list)
    {
        mDatas = list;
    }

    @Override
    public MyTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View view = mInflater.inflate(R.layout.list_item_teamtask, viewGroup, false);

        return new MyTaskViewHolder(view, new MyTaskViewHolder.OnItemClickListener()
        {
            @Override
            public void OnItemClick(View view, int position)
            {
                Intent i = new Intent(mContext, TaskDetailActivity.class);
                i.putExtra("id",mDatas.get(position).getObjectId());
                mContext.startActivity(i);
            }
        }, new MyTaskViewHolder.OnItemLongClickListener()
        {
            @Override
            public boolean OnItemLongClick(View view, final int position)
            {

                new AlertDialog.Builder(mContext).setItems(new String[]{"完成", "删除"}, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if (which == 0)
                            mPresenter.CompleteTask(position);
                        else
                            mPresenter.DeleteTask(position);
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public void onBindViewHolder(MyTaskViewHolder viewHolder, int pos) {
            viewHolder.title.setText(mDatas.get(pos).getTitle());
    }


    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        public TextView title;
        private OnItemClickListener mListener;
        private OnItemLongClickListener mLongClickListener;

        public MyTaskViewHolder(View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mListener = listener;
            mLongClickListener = longClickListener;
            title = (TextView) itemView.findViewById(R.id.title);
        }

        @Override
        public void onClick(View v)
        {
            mListener.OnItemClick(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v)
        {
            return mLongClickListener.OnItemLongClick(v, getLayoutPosition());
        }


        public interface OnItemClickListener
        {
            void OnItemClick(View view, int position);
        }

        public interface OnItemLongClickListener
        {
            boolean OnItemLongClick(View view, int position);
        }
    }
}


