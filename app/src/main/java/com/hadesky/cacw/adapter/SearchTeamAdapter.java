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
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.ui.widget.ColorfulAnimView.ColorfulAnimView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 45517 on 2016/7/22.
 */
public class SearchTeamAdapter extends BaseAdapter<TeamBean> {
    private static final int TYPE_TEAM = 0;
    private static final int TYPE_NEXT_RESULT = 1;

    private boolean isFinal = false;

    private List<TeamBean> mMyTeams;

    private BaseViewHolder.OnItemClickListener mOnItemClickListener;//fragment实现

    private WeakReference<View> mNextResultViewWeakReference;//保存下一次查询的按钮那个View的软引用

    public SearchTeamAdapter(List<TeamBean> list, @LayoutRes int layoutid, BaseViewHolder.OnItemClickListener listener) {
        super(list, layoutid);
        mOnItemClickListener = listener;
    }

    @Override
    public BaseViewHolder<TeamBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEXT_RESULT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_next_result_progress, parent, false);
            mContext = parent.getContext();
            return createNextTenViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<TeamBean> holder, int position) {
        if (position >= mDatas.size()) {
            holder.setData(null);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void setData(List<TeamBean> teamBeans, boolean isFinal) {
        if (teamBeans != null) {
            this.isFinal = isFinal;
            setDatas(teamBeans);
        }
    }

    public void addData(List<TeamBean> teams, boolean isFinal) {
        if (mDatas != null) {
            int start = mDatas.size();
            mDatas.addAll(teams);
            this.isFinal = isFinal;
            if (isFinal) {
                notifyItemRemoved(mDatas.size());
            }
            notifyItemRangeInserted(start, teams.size());
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return isFinal ? mDatas.size() : mDatas.size() + 1;
    }

    private BaseViewHolder<TeamBean> createNextTenViewHolder(View view) {

        mNextResultViewWeakReference = new WeakReference<>(view);

        return new BaseViewHolder<TeamBean>(view) {
            @Override
            public void setData(TeamBean aVoid) {
                setOnItemClickListener(mOnItemClickListener);
            }
        };
    }

    @Override
    public BaseViewHolder<TeamBean> createHolder(View v, Context context) {
        return new BaseViewHolder<TeamBean>(v) {
            @Override
            public void setData(TeamBean team) {
                setTextView(R.id.tv_team_name, team.getTeamName());
                if (team.getSummary() == null) {
                    setVisibility(R.id.tv_summary, View.GONE);
                } else {
                    setVisibility(R.id.tv_summary, View.VISIBLE);
                    setTextView(R.id.tv_summary, team.getSummary());
                }
                SimpleDraweeView view = findView(R.id.iv_avatar);
                if (team.getTeamAvatarUrl() != null) {
                    view.setImageURI(team.getTeamAvatarUrl());
                } else {
                    view.setImageURI((String) null);
                }
                if (mMyTeams != null && mMyTeams.contains(team)) {
                    setVisibility(R.id.tv_team_member, View.VISIBLE);
                    setVisibility(R.id.bt_join, View.GONE);
                } else {
                    setVisibility(R.id.tv_team_member, View.GONE);
                    setVisibility(R.id.bt_join, View.VISIBLE);
                }
                setButtonOnClickListener(R.id.bt_join, mOnItemClickListener);
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
        return position == mDatas.size() ? TYPE_NEXT_RESULT : TYPE_TEAM;
    }

    public void setMyTeams(List<TeamBean> myTeams) {
        if (myTeams != null) {
            mMyTeams = new ArrayList<>();
            for (TeamBean team : myTeams) {
                mMyTeams.add(team);
            }
        }
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
}
