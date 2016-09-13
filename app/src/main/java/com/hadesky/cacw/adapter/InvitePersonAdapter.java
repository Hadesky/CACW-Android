package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 45517 on 2016/7/22.
 */
public class InvitePersonAdapter extends SearchPersonAdapter
{

    private BaseViewHolder.OnItemClickListener mInviteLisenter;
    private Set<Integer> mDisableSet = new HashSet<>();


    public InvitePersonAdapter(List<UserBean> list, @LayoutRes int layoutid, BaseViewHolder.OnItemClickListener listener, BaseViewHolder.OnItemClickListener inviteLisenter)
    {
        super(list, layoutid, listener);
        mInviteLisenter = inviteLisenter;
    }

    @Override
    public BaseViewHolder<UserBean> createHolder(View v, Context context)
    {
        return new InvitePersonViewHolder(v);
    }


    public void disableInviteButton(int pos)
    {
        mDisableSet.add(pos);
        notifyItemChanged(pos);
    }


    class InvitePersonViewHolder extends BaseViewHolder<UserBean>
    {

        public InvitePersonViewHolder(View itemView)
        {
            super(itemView);
        }
        @Override
        public void setData(UserBean userBean)
        {
            setTextView(R.id.tv_nick_name, userBean.getNickName());
            setTextView(R.id.tv_summary, userBean.getSummary());
            SimpleDraweeView view = findView(R.id.iv_avatar);
            if (userBean.getAvatarUrl() != null)
            {
                view.setImageURI(userBean.getAvatarUrl());
            } else
            {
                view.setImageURI((String) null);
            }
            setOnItemClickListener(mOnItemClickListener);
            if(mDisableSet.contains(getAdapterPosition()))
            {
                setEnable(R.id.bt_invite,false);
                setBtnText(R.id.bt_invite,"已邀请");
                Button b = findView(R.id.bt_invite);
                b.setOnClickListener(null);
            }else
            {
                setEnable(R.id.bt_invite,true);
                setBtnText(R.id.bt_invite,"邀请");
                Button b = findView(R.id.bt_invite);
                b.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mInviteLisenter.OnItemClick(v, getAdapterPosition());
                    }
                });
            }

        }
    }
}
