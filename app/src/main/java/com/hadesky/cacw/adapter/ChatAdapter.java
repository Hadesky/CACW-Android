package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.config.MyApp;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/7/23 0023.
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder<MessageBean>>
{

    private Context mContext;
    private List<MessageBean> mDatas;
    private static final int Me = 1;
    private static final int Other = 2;


    @Override
    public BaseViewHolder<MessageBean> onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View v = null;

        if (viewType == Me)
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_me, parent, false);
        else
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_other, parent, false);


        BaseViewHolder<MessageBean> holder = new BaseViewHolder<MessageBean>(v)
        {
            @Override
            public void setData(MessageBean bean)
            {
                setTextView(R.id.tv_msg, bean.getMsg());
                SimpleDraweeView draweView = findView(R.id.iv_avatar);
                draweView.setImageURI(bean.getSender().getAvatarUrl());
            }
        };
        return holder;
    }


    public void setDatas(List<MessageBean> list)
    {
        mDatas = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<MessageBean> holder, int position)
    {
        holder.setData(mDatas.get(position));
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isMe(mDatas.get(position)))
        {
            return Me;
        }
        return Other;
    }

    private boolean isMe(MessageBean bean)
    {
        return MyApp.isCurrentUser(bean.getSender());
    }

    @Override
    public int getItemCount()
    {
        if (mDatas != null)
            return mDatas.size();
        return 0;
    }
}
