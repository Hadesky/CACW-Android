package com.hadesky.cacw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.ProjectBean;

/**
 * 这是项目详情里面的最新动态adapter
 * Created by MicroStudent on 2016/3/23.
 */
// TODO: 2016/7/9 0009 该功能的实现暂时无计划
public class LatestNewsAdapter extends BaseRvAdapter<LatestNewsAdapter.ViewHolder>
{

    public LatestNewsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_lastest_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }


    public static  class ViewHolder extends BaseViewHolder<ProjectBean>{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(ProjectBean projectBean) {

        }
    }
}
