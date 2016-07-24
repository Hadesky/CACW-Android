package com.hadesky.cacw.adapter.viewholder;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用ViewHolder，大部分情况只需重写setDatas方法，通过setXXxX方法更新控件内容
 * Created by 45517 on 2016/3/21.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    protected SparseArray<View> mViews;


    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mViews = new SparseArray<>();
    }

    public abstract void setData(T t);


    public void setTextView(@IdRes int id, String text) {
        TextView view = findView(id);

        view.setText(text);
    }


    public void setCheckBox(@IdRes int id, boolean b) {
        CheckBox cb = findView(id);
        cb.setChecked(b);
    }

    public void setImageBitmap(@DrawableRes int id, @IdRes int viewid) {
        ImageView view = findView(viewid);
        if (view != null)
            view.setImageResource(id);
    }

    public void setVisibility(@IdRes int id,int visibility) {
        View view = findView(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public <Tv> Tv findView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null)
            view = itemView.findViewById(id);
        return (Tv) view;
    }

    public void setButtonOnClickListener(@IdRes int buttonId, OnItemClickListener listener) {
        Button button = findView(buttonId);
        if (button != null) {
            button.setOnClickListener(this);
        }
        mOnItemClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener m) {
        this.mOnItemLongClickListener = m;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnItemLongClickListener != null && mOnItemLongClickListener.OnItemLongClick(v, getLayoutPosition());
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick(View view, int position);
    }
}
