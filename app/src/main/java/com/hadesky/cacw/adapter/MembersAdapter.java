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
import com.hadesky.cacw.bean.MemberBean;
import com.hadesky.cacw.ui.SelectMemberActivity;
import com.hadesky.cacw.ui.UserInfoActivity;

import java.util.List;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersViewHolder> {
    private List<MemberBean> members;
    private Context mContext;
    private LayoutInflater inflater;

    public MembersAdapter(List<MemberBean> members, Context context) {
        this.mContext = context;
        this.members = members;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MembersViewHolder viewHolder;
        View view;
        if (viewType == MemberBean.TYPE_NORMAL) {
            view = inflater.inflate(R.layout.list_item_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                }
            });
        } else {
            view = inflater.inflate(R.layout.item_add_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    mContext.startActivity(new Intent(mContext, SelectMemberActivity.class));
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {
        if (holder.getViewType() == MemberBean.TYPE_NORMAL) {
            holder.setText(members.get(position).getUsername());
            holder.setImageSrc(members.get(position).getAvatarResid());
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public int getItemViewType(int position) {
        return members.get(position).getType();
    }
}


class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ImageView imageView;
    private TextView textView;
    private int viewType;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public MembersViewHolder(View itemView, int viewType, OnItemClickListener listener) {
        super(itemView);
        if (viewType == MemberBean.TYPE_NORMAL) {
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }

        itemView.setOnClickListener(this);
        this.listener = listener;

        this.viewType = viewType;
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setImageSrc(int src) {
        imageView.setImageResource(src);
    }

    public int getViewType() {
        return viewType;
    }

    @Override
    public void onClick(View v) {
        listener.OnItemClick(v, getLayoutPosition());
    }
}
