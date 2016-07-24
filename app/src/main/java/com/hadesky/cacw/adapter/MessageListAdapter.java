package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.ChatActivity;

import java.util.List;

/**
 *  消息列表
 * Created by dzysg on 2016/7/23 0023.
 */
public class MessageListAdapter extends BaseAdapter<MessageBean>
{


    public MessageListAdapter(List<MessageBean> list, @LayoutRes int layoutid)
    {
        super(list, layoutid);
    }

    @Override
    public BaseViewHolder<MessageBean> createHolder(View v, Context context)
    {
        BaseViewHolder<MessageBean> holder = new BaseViewHolder<MessageBean>(v)
        {
            @Override
            public void setData(MessageBean messageBean)
            {
                UserBean u = messageBean.getReceiver();
                if (MyApp.isCurrentUser(u))
                {
                    u = messageBean.getSender();
                }
                setTextView(R.id.tv_nick_name, u.getNickName());
                setTextView(R.id.tv_msg, messageBean.getMsg());
                SimpleDraweeView iv = findView(R.id.iv_avatar);
                iv.setImageURI(u.getAvatarUrl());

                //如果消息没读而且消息来自他人
                if (!messageBean.getHasRead() && !MyApp.isCurrentUser(messageBean.getSender()))
                    setVisibility(R.id.iv_has_read, View.VISIBLE);
                else
                    setVisibility(R.id.iv_has_read, View.GONE);

            }
        };

        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener()
        {
            @Override
            public void OnItemClick(View view, int position)
            {
                Intent i = new Intent(mContext, ChatActivity.class);
                if (MyApp.isCurrentUser(mDatas.get(position).getReceiver()))
                    i.putExtra(IntentTag.TAG_USER_BEAN, mDatas.get(position).getSender());
                else
                    i.putExtra(IntentTag.TAG_USER_BEAN, mDatas.get(position).getReceiver());
                mContext.startActivity(i);
            }
        });
        return holder;
    }


}
