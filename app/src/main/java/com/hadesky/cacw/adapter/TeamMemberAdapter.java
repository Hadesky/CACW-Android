package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.util.PinyinUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * Created by MicroStudent on 2016/7/16.
 */

public class TeamMemberAdapter extends BaseAdapter<UserBean>
        implements StickyRecyclerHeadersAdapter<TeamMemberAdapter.AlphabetViewHolder> {
    /**
     * 构造方法
     * @param list data
     * @param layoutid item的Layout id
     */
    public TeamMemberAdapter(List<UserBean> list, @LayoutRes int layoutid) {
        super(list, layoutid);
    }

    @Override
    public BaseViewHolder<UserBean> createHolder(View v, Context context) {
        TeamMemberViewHolder viewHolder = new TeamMemberViewHolder(v);
        viewHolder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(IntentTag.TAG_USER_BEAN, mDatas.get(position));
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    /**
     * 字母排序的header id，本质上是首字的拼音首字母
     */
    @Override
    public long getHeaderId(int position) {
        return PinyinUtils.getFirstPinyinLetter(mDatas.get(position).getNickName());
    }

    /**
     * 字母排序相关
     */
    @Override
    public AlphabetViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_alpha_header, parent, false);
        return new AlphabetViewHolder(view);
    }

    /**
     * 字母排序相关
     */
    @Override
    public void onBindHeaderViewHolder(AlphabetViewHolder holder, int position) {
        holder.mTextView.setText(String.valueOf((char) getHeaderId(position)));
    }

    private class TeamMemberViewHolder extends BaseViewHolder<UserBean> {
        public TeamMemberViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(UserBean userBean) {
            setTextView(R.id.tv_nick_name, userBean.getNickName());
            TextView textView = findView(R.id.tv_admin);
            textView.setVisibility(View.VISIBLE);
            SimpleDraweeView view = findView(R.id.iv_avatar);
            if (userBean.getUserAvatar() != null) {
                view.setImageURI(userBean.getUserAvatar().getUrl());
            }
        }
    }

    class AlphabetViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        public AlphabetViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mTextView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}