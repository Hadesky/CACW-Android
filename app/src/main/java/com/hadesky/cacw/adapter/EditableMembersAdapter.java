package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 45517 on 2015/10/17.
 */
public class EditableMembersAdapter extends RecyclerView.Adapter<EditableMembersAdapter.MembersViewHolder>
{
    public static final int MODE_NORMAL = 0;
    public static final int MODE_DELETE = 1;

    public static final int BUTTON_TYPE_AVATAR = 0;
    public static final int BUTTON_TYPE_ADD = 1;
    public static final int BUTTON_TYPE_DELETE = 2;

    private boolean ableToDelete = false;

    private boolean ableToAdd = false;

    private List<UserBean> members;
    private Context mContext;
    private LayoutInflater inflater;
    private int mode = 0;
    private TaskBean mTask;


    private OnMemberEditListener mOnMemberEditListener;

    //返回true才执行删除
    public interface OnMemberEditListener
    {
        boolean onMemberDelete(UserBean user_id);

        void onAddMember();
    }

    public EditableMembersAdapter(List<UserBean> member, Context context, OnMemberEditListener listener, TaskBean task)
    {
        this.mOnMemberEditListener = listener;
        this.mContext = context;
        this.members = member;
        if (member == null)
            members = new ArrayList<>();
        mTask = task;
        inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<UserBean> list)
    {
        members = list;
        notifyDataSetChanged();
    }

    public List<UserBean> getDatas()
    {
        return members;
    }


    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MembersViewHolder viewHolder;
        View view;
        if (viewType == BUTTON_TYPE_AVATAR)
        {
            //头像
            view = inflater.inflate(R.layout.list_item_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener()
            {
                @Override
                public void OnItemClick(View view, int position)
                {
                    if (view.getId() == R.id.iv_avatar)
                    {
                        //点击到头像
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtra(IntentTag.TAG_USER_BEAN,(Parcelable) members.get(position));
                        mContext.startActivity(intent);
                    } else
                    {
                        //点击到头像的删除按钮
                        if (mOnMemberEditListener.onMemberDelete(members.get(position)))
                        {
                            members.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        } else if (viewType == BUTTON_TYPE_ADD)
        {
            //添加按钮
            view = inflater.inflate(R.layout.item_add_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener()
            {
                @Override
                public void OnItemClick(View view, int position)
                {
                    if (mOnMemberEditListener != null)
                        mOnMemberEditListener.onAddMember();
                }
            });
        } else
        {
            //删除按钮
            view = inflater.inflate(R.layout.item_delete_member, parent, false);
            viewHolder = new MembersViewHolder(view, viewType, new MembersViewHolder.OnItemClickListener()
            {
                @Override
                public void OnItemClick(View view, int position)
                {
                    mode = MODE_DELETE;
                    notifyDataSetChanged();
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position)
    {
        if (holder.getViewType() == BUTTON_TYPE_AVATAR)
        {
            //当前要进行设置的是普通按钮
            if (mode == MODE_NORMAL)
            {
                //                普通模式
                holder.setDeleteViewVisible(false);
                holder.avatarView.setClickable(true);

            } else if (mode == MODE_DELETE)
            {
                //                删除模式
                holder.setDeleteViewVisible(true);
                holder.avatarView.setClickable(false);
            }
            holder.setText(members.get(position).getNickName());
            holder.setImageUrl(members.get(position).getAvatarUrl());

        } else if (holder.getViewType() == BUTTON_TYPE_DELETE)
        {
            //当前要进行设置的是删除按钮
            if (mode == MODE_NORMAL)
            {
                //普通状态显示删除按钮
                holder.itemView.setVisibility(View.VISIBLE);
            } else if (mode == MODE_DELETE)
            {
                //删除状态去除删除按钮
                holder.itemView.setVisibility(View.GONE);
            }
        }
        else if (holder.getViewType() == BUTTON_TYPE_ADD)
        {
            //当前要进行设置的是添加按钮
            if (mode == MODE_NORMAL)
            {
                //普通状态显示+按钮
                holder.itemView.setVisibility(View.VISIBLE);
            } else if (mode == MODE_DELETE)
            {
                //删除状态去除+按钮
                holder.itemView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        if (isAbleToAdd())
            if (isAbleToDelete())
            {
                return members.size() + 2;
            } else
                return members.size() + 1;
        else
            return members.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position < members.size())
        {
            return BUTTON_TYPE_AVATAR;
        } else
        {
            if (position == members.size())
                return BUTTON_TYPE_ADD;
            else if (position == members.size() + 1)
            {
                return BUTTON_TYPE_DELETE;
            }
        }
        throw new RuntimeException("Item Count Error!");
    }

    private boolean isAbleToAdd()
    {
        return ableToAdd;
    }

    public boolean isAbleToDelete()
    {
        return ableToDelete;
    }

    public void setAbleToDelete(boolean ableToDelete)
    {
        this.ableToDelete = ableToDelete;
    }

    public void setAbleToAdd(boolean ableToAdd)
    {
        this.ableToAdd = ableToAdd;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public int getMode()
    {
        return mode;
    }

    static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private SimpleDraweeView avatarView;
        private ImageView deleteView;
        private TextView textView;
        private int viewType;
        private OnItemClickListener listener;

        public interface OnItemClickListener
        {
            void OnItemClick(View view, int position);
        }

        public MembersViewHolder(View itemView, int viewType, OnItemClickListener listener)
        {
            super(itemView);
            if (viewType == BUTTON_TYPE_AVATAR)
            {
                avatarView = (SimpleDraweeView) itemView.findViewById(R.id.iv_avatar);
                textView = (TextView) itemView.findViewById(R.id.tv);
                deleteView = (ImageView) itemView.findViewById(R.id.iv_delete);
                avatarView.setOnClickListener(this);
                deleteView.setOnClickListener(this);
            } else
            {
                itemView.setOnClickListener(this);
            }

            this.listener = listener;
            this.viewType = viewType;
        }

        public void setText(String text)
        {
            if (textView != null)
            {
                textView.setText(text);
            }
        }

        public void setImageUrl(String url)
        {
            avatarView.setImageURI(url);
        }

        public void setImageSrc(int src)
        {
            if (avatarView != null)
            {
                avatarView.setImageResource(src);
            }
        }

        public int getViewType()
        {
            return viewType;
        }

        public void setDeleteViewVisible(boolean visible)
        {
            if (deleteView != null)
            {
                if (visible)
                {
                    deleteView.setVisibility(View.VISIBLE);
                } else
                {
                    deleteView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v)
        {
            listener.OnItemClick(v, getLayoutPosition());
        }
    }
}
