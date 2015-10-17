package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.ui.ProjectDetailActivity;
import com.hadesky.cacw.util.LogUtils;

import java.util.List;

/**
 * Created by 45517 on 2015/9/18.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<ProjectBean> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public ProjectAdapter(Context context, List<ProjectBean> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_project, parent, false);

        return new ProjectViewHolder(view, new ProjectViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, ProjectDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        ProjectBean bean = mData.get(position);
        holder.tv_title.setText(bean.getTitle());
        holder.iv_avatar.setImageResource(bean.getResId());
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    /**
     * ViewHolder内部类
     */
    public static class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener listener;
        private ImageView iv_avatar;
        private TextView tv_title;

        public ProjectViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_project_avatar);
            tv_title = (TextView) itemView.findViewById(R.id.tv_project_title);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(v,getLayoutPosition());
        }

        public interface OnItemClickListener {
            void OnItemClick(View view, int position);
        }
    }

}


