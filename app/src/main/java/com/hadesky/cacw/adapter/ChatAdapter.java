package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dzysg on 2016/7/23 0023.
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder<MessageBean>>
{

    private Context mContext;
    private List<MessageBean> mDatas = new ArrayList<>();
    private static final int Me = 1;
    private static final int Other = 2;
    private HashMap<MessageBean, Integer> mSendState = new HashMap<>(); //1 为发送中，2为成功，3为失败
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LoadMoreLinsener mLoadMoreLinsener;

    public ChatAdapter(Context context, List<MessageBean> datas)
    {
        mContext = context;
        if (datas != null)
            mDatas = datas;
    }

    @Override
    public BaseViewHolder<MessageBean> onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View v;
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
                setTextView(R.id.tv_username, bean.getSender().getNickName());
                SimpleDraweeView draweView = findView(R.id.iv_avatar);
                draweView.setImageURI(bean.getSender().getAvatarUrl());
                Integer state = mSendState.get(bean);
                if (state == null || state == 2)
                {
                    setVisibility(R.id.pb, View.GONE);

                } else if (state == 1)
                {
                    setVisibility(R.id.pb, View.VISIBLE);

                } else if (state == 3)
                {
                    setVisibility(R.id.pb, View.GONE);
                    setVisibility(R.id.v_error, View.VISIBLE);
                }
            }
        };
        return holder;
    }


    public void addNewChat(MessageBean bean)
    {
        mDatas.add(bean);
        mSendState.put(bean, 1);
        notifyDataSetChanged();
        scrolToBottom();

    }

    public void onSucceed(MessageBean bean)
    {
        mSendState.put(bean, 2);
        notifyDataSetChanged();
    }

    public void onFail(MessageBean bean)
    {
        mSendState.put(bean, 3);
        notifyDataSetChanged();
    }

    private void scrolToBottom()
    {
        mRecyclerView.scrollToPosition(mDatas.size() - 1);
    }

    public void addDatasToTop(List<MessageBean> list)
    {

        mDatas.addAll(0,list);
        notifyDataSetChanged();
        mRecyclerView.scrollToPosition(list.size());
        //scrolToBottom();
    }

    public void setDatas(List<MessageBean> list)
    {
        mDatas = list;
        notifyDataSetChanged();
        scrolToBottom();
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


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                //如果滚动停下来后最后一条已经不见了
                if (newState == RecyclerView.SCROLL_STATE_IDLE&&mLayoutManager.findLastCompletelyVisibleItemPosition()!=mDatas.size()-1)
                {
                    int i = mLayoutManager.findFirstCompletelyVisibleItemPosition();//如果滚动到顶了
                    if (i == 0 && mLoadMoreLinsener != null)
                        mLoadMoreLinsener.loadmore();
                }
            }
        });
    }

    public void setLoadMoreLinsener(LoadMoreLinsener loadMoreLinsener)
    {
        mLoadMoreLinsener = loadMoreLinsener;
    }

    public interface LoadMoreLinsener
    {
        void loadmore();
    }
}
