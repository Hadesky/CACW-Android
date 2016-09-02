package com.hadesky.cacw.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.adapter.viewholder.SimpleHolder;

import java.util.List;

/**
 * 通用adapter，大部分情况下只需重写createHolder方法
 * Created by dzysg on 2015/10/9 0009.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>>
{
    private static final int EmptyViewType = 99;
    protected List<T> mDatas;
    protected Context mContext;
    protected int itemLayoutId;
    protected int mEmptyLayoutId = -1;
    private boolean mShowEmptyView = false;

    public BaseAdapter(List<T> list, @LayoutRes int layoutid)
    {
        mDatas = list;
        itemLayoutId = layoutid;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mContext == null)
            mContext = parent.getContext();

        if (mShowEmptyView&&mEmptyLayoutId!=0)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(mEmptyLayoutId, parent, false);
            return new SimpleHolder<>(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return createHolder(view, parent.getContext());
    }

    public abstract BaseViewHolder<T> createHolder(View v, Context context);


    public void setEmptyLayoutId(@LayoutRes int emptyLayoutId)
    {
        mEmptyLayoutId = emptyLayoutId;
    }


    public void setDatas(List<T> datas)
    {
        if (datas != null)
        {
            mDatas = datas;
            notifyDataSetChanged();
        }
    }

    public List<T> getDatas()
    {
        return mDatas;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mShowEmptyView)
            return EmptyViewType;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount()
    {
        if (mDatas.size() == 0&&mEmptyLayoutId>0)
        {
            mShowEmptyView =true;
            return 1;
        }else
            mShowEmptyView = false;
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position)
    {
        if (!mShowEmptyView)
        {
            T item = mDatas.get(position);
            holder.setData(item);
        }

    }
}
