package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;

import java.util.List;

/**
 * Created by 45517 on 2015/9/18.
 */
public class LinkManAdapter extends RecyclerView.Adapter<LinkManAdapter.LinkManViewHolder> {

    private List<TeamBean> mData;
    private LayoutInflater mInflater;


    public LinkManAdapter(Context context,List<TeamBean> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public LinkManViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_team, parent, false);
        return new LinkManViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LinkManViewHolder holder, int position) {
        TeamBean bean = mData.get(position);
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
    class LinkManViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_avatar;
        private TextView tv_title;

        public LinkManViewHolder(View itemView) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_team_avatar);
            tv_title = (TextView) itemView.findViewById(R.id.tv_team_title);
        }
    }
}

