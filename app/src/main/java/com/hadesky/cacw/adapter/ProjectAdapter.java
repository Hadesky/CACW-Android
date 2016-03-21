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
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.ProjectDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 45517 on 2015/9/18.
 */
public class ProjectAdapter extends BaseRvAdapter<ProjectAdapter.ProjectViewHolder> {

    private List<ProjectBean> mData = new ArrayList<>();

    public ProjectAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        ProjectBean bean = mData.get(position);
        holder.tv_title.setText(bean.getProjectName());
        holder.iv_avatar.setImageResource(bean.getAvatarResId());

        holder.setOnItemClickListener(new ProjectViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, ProjectDetailActivity.class);
                intent.putExtra(IntentTag.TAG_PROJECT_ID, mData.get(position).getProjectId());

                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<ProjectBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder内部类
     */
    public class ProjectViewHolder extends BaseViewHolder {

        private ImageView iv_avatar;
        private TextView tv_title;

        public ProjectViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_project_avatar);
            tv_title = (TextView) itemView.findViewById(R.id.tv_project_title);
        }
    }
}


