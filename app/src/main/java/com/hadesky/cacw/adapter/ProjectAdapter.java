package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.ProjectDetailActivity;

import java.util.List;

/**
 *
 * Created by 45517 on 2015/9/18.
 */
public class ProjectAdapter extends BaseAdapter<ProjectBean> {

    public ProjectAdapter(List<ProjectBean> list, @LayoutRes int layoutid) {
        super(list, layoutid);
    }

    @Override
    public BaseViewHolder<ProjectBean> createHolder(View v, Context context) {

        BaseViewHolder<ProjectBean> holder  = new BaseViewHolder<ProjectBean> (v) {
            @Override
            public void setData(ProjectBean o) {
                setTextView(R.id.tv_project_title,o.getProjectName());
            }
        };

        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, ProjectDetailActivity.class);
                intent.putExtra(IntentTag.TAG_PROJECT_BEAN, mDatas.get(position));
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

}


