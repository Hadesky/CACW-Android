package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.MemberBean;
import com.hadesky.cacw.ui.SelectMemberActivity;
import com.hadesky.cacw.ui.UserInfoActivity;

import java.util.List;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    public static final int MODE_NORMAL = 0;
    public static final int MODE_DELETE = 1;

    private List<MemberBean> members;
    private Context mContext;
    private LayoutInflater inflater;
    private int mode = 0;

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
                    if (view.getId() == R.id.iv_avatar) {
                        mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                    } else {
                        members.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        } else if (viewType == MemberBean.TYPE_ADD) {
            view = inflater.inflate(R.layout.item_add_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    mContext.startActivity(new Intent(mContext, SelectMemberActivity.class));
                }
            });
        } else {
            view = inflater.inflate(R.layout.item_delete_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    mode = MODE_DELETE;
                    notifyItemRangeChanged(0, members.size());
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {
        if (holder.getViewType() == MemberBean.TYPE_NORMAL) {
            if (mode == MODE_NORMAL) {
                holder.setDeleteViewVisible(false);
            }else if (mode == MODE_DELETE) {
                holder.setDeleteViewVisible(true);
            }
            holder.setText(members.get(position).getUsername());
            holder.setImageSrc(members.get(position).getAvatarResid());
        }else if (holder.getViewType() == MemberBean.TYPE_DELETE) {
            if (mode == MODE_NORMAL) {
                holder.itemView.setVisibility(View.VISIBLE);
            }else if (mode == MODE_DELETE) {
                holder.itemView.setVisibility(View.INVISIBLE);
            }
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

    public void setMode(int mode) {
        this.mode = mode;
        notifyItemRangeChanged(0, members.size());
    }

    public int getMode() {
        return mode;
    }

    static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView avatarView;
        private ImageView deleteView;
        private TextView textView;
        private int viewType;
        private OnItemClickListener listener;
        public interface OnItemClickListener {
            void OnItemClick(View view, int position);
        }

        public MembersViewHolder(View itemView, int viewType, OnItemClickListener listener) {
            super(itemView);
            if (viewType == MemberBean.TYPE_NORMAL) {
                avatarView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                textView = (TextView) itemView.findViewById(R.id.tv);
                deleteView = (ImageView) itemView.findViewById(R.id.iv_delete);
                avatarView.setOnClickListener(this);
                deleteView.setOnClickListener(this);
            }else {
                itemView.setOnClickListener(this);
            }

            this.listener = listener;
            this.viewType = viewType;
        }

        public void setText(String text) {
            if (textView != null) {
                textView.setText(text);
            }
        }

        public void setImageSrc(int src) {
            if (avatarView != null) {
                avatarView.setImageResource(src);
            }
        }

        public int getViewType() {
            return viewType;
        }

        public void setDeleteViewVisible(boolean visible) {
            if (deleteView != null) {
                if (visible) {
                    deleteView.setVisibility(View.VISIBLE);
                } else {
                    deleteView.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(v, getLayoutPosition());
        }
    }
}
