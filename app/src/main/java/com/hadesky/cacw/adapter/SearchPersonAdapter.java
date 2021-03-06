package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.widget.ColorfulAnimView.ColorfulAnimView;

import java.lang.ref.WeakReference;
import java.util.List;

/** 搜索用户列表 adapter
 * Created by 45517 on 2016/7/22.
 */
public class SearchPersonAdapter extends BaseAdapter<UserBean> {
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_NEXT_RESULT = 1;

    private boolean isFinal = false;

    protected BaseViewHolder.OnItemClickListener mOnItemClickListener;//fragment实现

    private WeakReference<View> mNextResultViewWeakReference;//保存下一次查询的按钮那个View的软引用

    public SearchPersonAdapter(List<UserBean> list, @LayoutRes int layoutid, BaseViewHolder.OnItemClickListener listener) {
        super(list, layoutid);
        mOnItemClickListener = listener;
    }

    @Override
    public BaseViewHolder<UserBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEXT_RESULT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_next_result_progress, parent, false);
            mContext = parent.getContext();
            return createNextTenViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<UserBean> holder, int position) {
        if (position >= mDatas.size()) {
            holder.setData(null);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void setData(List<UserBean> userBeen, boolean isFinal) {
        if (userBeen != null) {
            this.isFinal = isFinal;
            setDatas(userBeen);
        }
    }

    public void addData(List<UserBean> userBeen, boolean isFinal) {
        if (mDatas != null) {
            this.isFinal = isFinal;
            if (isFinal) {
                notifyItemRemoved(mDatas.size());
            }
            int start = mDatas.size();
            mDatas.addAll(userBeen);
            notifyItemRangeInserted(start, userBeen.size());
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

    private BaseViewHolder<UserBean> createNextTenViewHolder(View view) {

        mNextResultViewWeakReference = new WeakReference<>(view);
        return new BaseViewHolder<UserBean>(view) {
            @Override
            public void setData(UserBean aVoid) {
                setOnItemClickListener(mOnItemClickListener);
            }
        };
    }

    @Override
    public BaseViewHolder<UserBean> createHolder(View v, Context context) {
        return new PersonViewHolder(v);
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
        return position == mDatas.size() ? TYPE_NEXT_RESULT : TYPE_PERSON;
    }

    class PersonViewHolder extends BaseViewHolder<UserBean> {
        public PersonViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(UserBean userBean) {
            setTextView(R.id.tv_nick_name, userBean.getNickName());
            if (userBean.getSummary() == null) {
                setVisibility(R.id.tv_summary, View.GONE);
            } else {
                setVisibility(R.id.tv_summary, View.VISIBLE);
                setTextView(R.id.tv_summary, userBean.getSummary());
            }
            SimpleDraweeView view = findView(R.id.iv_avatar);
            if (userBean.getAvatarUrl() != null) {
                view.setImageURI(userBean.getAvatarUrl());
            } else {
                view.setImageURI((String) null);
            }
            setOnItemClickListener(mOnItemClickListener);
        }
    }
}
