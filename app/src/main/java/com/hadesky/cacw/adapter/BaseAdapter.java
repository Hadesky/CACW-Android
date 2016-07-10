package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;

import java.util.List;

/**
 * 通用adapter，大部分情况下只需重写createHolder方法
 * Created by dzysg on 2015/10/9 0009.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>>
{

    protected List<T> mDatas;
    protected Context mContext;
    int itemLayoutId;

    public BaseAdapter( List<T> list, @LayoutRes int layoutid)
    {
        mDatas = list;
        itemLayoutId = layoutid;
    }

    @Override
    public  BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType)
    {
            if (mContext==null)
                mContext = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
            return createHolder(view,parent.getContext());
    }

    public abstract BaseViewHolder<T> createHolder(View v,Context context);


    public void setDatas(List<T> datas)
    {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<T> getDatas()
    {
        return mDatas;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position)
    {
        T item = mDatas.get(position);
        holder.setData(item);
    }
}
