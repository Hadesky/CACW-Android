package com.hadesky.cacw.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by 45517 on 2016/3/21.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private OnItemClickListener onItemClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
        itemView.setOnClickListener(this);
    }

    public abstract void initView(View itemView);

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            this.onItemClickListener.OnItemClick(v,getLayoutPosition());
        }
    }
}
