package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TaskMemberBean;
import com.hadesky.cacw.ui.SelectMemberActivity;
import com.hadesky.cacw.ui.UserInfoActivity;

import java.util.List;

/**
 * 这是任务成员列表的adapter
 * Created by dzysg on 2015/10/16 0016.
 */
public class TaskMembersAdapter extends RecyclerView.Adapter<TaskMembersAdapter.TaskMemberVH>
{
    private Context              mContext;
    private List<TaskMemberBean> mDatas;

    public TaskMembersAdapter(Context context, List<TaskMemberBean> datas)
    {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public TaskMemberVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;
        TaskMemberVH holder;
        if (viewType == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_task_members, parent, false);

            holder = new TaskMemberVH(view, new OnItemClickListener()
            {
                @Override
                public void OnItemClick(View view, int position)
                {
                    mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                }
            });
        } else
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_add_member, parent, false);
            holder = new TaskMemberVH(view, new OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position)
                {
                    mContext.startActivity(new Intent(mContext, SelectMemberActivity.class));
                }
            });

        }

        return holder;
    }

    @Override
    public void onBindViewHolder(TaskMemberVH holder, int position)
    {

    }


    @Override
    public int getItemViewType(int position)
    {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public static class TaskMemberVH extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private OnItemClickListener mOnItemClickListener;
        private TextView            mName;


        public TaskMemberVH(View itemView, OnItemClickListener listener)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mOnItemClickListener = listener;
        }

        @Override
        public void onClick(View v)
        {
            if (mOnItemClickListener != null)
                mOnItemClickListener.OnItemClick(v, getLayoutPosition());
        }
    }

    public interface OnItemClickListener
    {
        void OnItemClick(View view, int position);
    }
}
