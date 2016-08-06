package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.ui.widget.ColorfulAnimView.ColorfulAnimView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 * Created by 45517 on 2016/7/22.
 */
public class SearchTaskAdapter extends BaseAdapter<TaskBean> {
    private static final int TYPE_TASK = 0;
    private static final int TYPE_NEXT_RESULT = 1;
    private static final String TAG = "SearchTaskAdapter";

    private boolean isFinal = false;

    protected BaseViewHolder.OnItemClickListener mOnItemClickListener;//fragment实现

    private WeakReference<View> mNextResultViewWeakReference;//保存下一次查询的按钮那个View的软引用

    public SearchTaskAdapter(List<TaskBean> list, @LayoutRes int layoutid, BaseViewHolder.OnItemClickListener listener) {
        super(list, layoutid);
        mOnItemClickListener = listener;
    }

    @Override
    public BaseViewHolder<TaskBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEXT_RESULT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_next_result_progress, parent, false);
            mContext = parent.getContext();
            return createNextTenViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<TaskBean> holder, int position) {
        if (position >= mDatas.size()) {
            holder.setData(null);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void setData(List<TaskBean> taskBeen, boolean isFinal) {
        if (taskBeen != null) {
            this.isFinal = isFinal;
            setDatas(taskBeen);
        }
    }

    public void addData(List<TaskBean> taskBeen, boolean isFinal) {
        if (mDatas != null) {
            int start = mDatas.size();
            mDatas.addAll(taskBeen);
            this.isFinal = isFinal;
            if (isFinal) {
                notifyItemRemoved(mDatas.size());
            }
            notifyItemRangeInserted(start, taskBeen.size());
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return isFinal ? mDatas.size() : mDatas.size() + 1;
    }

    /**
     * 返回nextResults那个View在adapter里的位置，若没有这个View，返回-1
     * @return nextResults那个View在adapter里的位置，若没有这个View，返回-1
     */
    public int getNextResultPosition() {
        if (isFinal) {
            return -1;
        }
        return mDatas.size();
    }

    private BaseViewHolder<TaskBean> createNextTenViewHolder(View view) {

        mNextResultViewWeakReference = new WeakReference<>(view);

        return new BaseViewHolder<TaskBean>(view) {
            @Override
            public void setData(TaskBean aVoid) {
                setOnItemClickListener(mOnItemClickListener);
            }
        };
    }

    @Override
    public BaseViewHolder<TaskBean> createHolder(View v, Context context) {
        return new BaseViewHolder<TaskBean>(v) {
            @Override
            public void setData(TaskBean taskBean) {
                setTextView(R.id.tv_title, taskBean.getTitle());
                setTextView(R.id.tv_content, taskBean.getContent());
                if (taskBean.getLocation() != null && !taskBean.getLocation().isEmpty()) {
                    setVisibility(R.id.layout_place, View.VISIBLE);
                    setTextView(R.id.tv_location, taskBean.getLocation());
                }
                setTextView(R.id.tv_date_time, taskBean.getEndDate().getDate());
            }
        };
    }

    /**
     * 开始动画
     */
    public void showProgress() {
        if (mNextResultViewWeakReference != null && mNextResultViewWeakReference.get() != null) {
            View nextTenView = mNextResultViewWeakReference.get();
            ColorfulAnimView animView = (ColorfulAnimView) nextTenView.findViewById(R.id.view_anim);
            TextView textView = (TextView) nextTenView.findViewById(R.id.tv);
            if (animView != null && textView != null) {
                textView.setVisibility(View.INVISIBLE);
                animView.startAnim();
            }
        }
    }

    /**
     * 隐藏动画
     */
    public void hideProgress() {
        if (mNextResultViewWeakReference != null && mNextResultViewWeakReference.get() != null) {
            View nextTenView = mNextResultViewWeakReference.get();
            ColorfulAnimView animView = (ColorfulAnimView) nextTenView.findViewById(R.id.view_anim);
            TextView textView = (TextView) nextTenView.findViewById(R.id.tv);
            if (animView != null && textView != null) {
                textView.setVisibility(View.VISIBLE);
                animView.stopAnimAndHide();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == mDatas.size() ? TYPE_NEXT_RESULT : TYPE_TASK;
    }

    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }
}
