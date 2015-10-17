package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.MemberBean;
import com.hadesky.cacw.util.LogUtils;

import java.util.List;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class MenbersAdapter extends RecyclerView.Adapter<MenbersAdapter.MembersViewHolder> {
    private List<MemberBean> members;
    private Context mContext;
    private LayoutInflater inflater;

    public MenbersAdapter(List<MemberBean> members, Context context) {
        this.mContext = context;
        this.members = members;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_member, parent, false);
        return new MembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {
        if (members.get(position).getType() == MemberBean.TYPE_NORMAL) {
            holder.setText(members.get(position).getUsername());
            holder.setImageSrc(members.get(position).getAvatarResid());
        } else if (members.get(position).getType() == MemberBean.TYPE_ADD) {
            holder.setText("");
            holder.setImageSrc(R.drawable.ic_action_add);
        } else {
            holder.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    class MembersViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public MembersViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_item_member);
            textView = (TextView) itemView.findViewById(R.id.tv_item_member);
        }

        public void setText(String text) {
            textView.setText(text);
        }

        public void setImageSrc(int src) {
            imageView.setImageResource(src);
        }
    }
}
