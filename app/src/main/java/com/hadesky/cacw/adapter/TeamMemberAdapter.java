package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.TeamMemberPresenter;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.MyInfoActivity;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.util.PinyinUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 *
 * Created by MicroStudent on 2016/7/16.
 */

public class TeamMemberAdapter extends BaseAdapter<UserBean>
        implements StickyRecyclerHeadersAdapter<TeamMemberAdapter.AlphabetViewHolder> {

    private UserBean mAdminUser;//团队管理员
    private TeamMemberPresenter mPresenter;
    /**
     * 构造方法
     *
     * @param list     data
     * @param layoutid item的Layout id
     * @param admin
     */
    public TeamMemberAdapter(List<UserBean> list, @LayoutRes int layoutid, UserBean admin) {
        super(list, layoutid);
        mAdminUser = admin;
    }

    @Override
    public BaseViewHolder<UserBean> createHolder(View v, Context context) {
        TeamMemberViewHolder viewHolder = new TeamMemberViewHolder(v);
        viewHolder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (mDatas.get(position).equals(MyApp.getCurrentUser())) {
                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra(IntentTag.TAG_USER_BEAN, mDatas.get(position));
                    mContext.startActivity(intent);
                }
            }
        });
        if (MyApp.isCurrentUser(mAdminUser)) {
            viewHolder.setOnItemLongClickListener(new BaseViewHolder.OnItemLongClickListener() {
                @Override
                public boolean OnItemLongClick(View view, final int position)
                {
                    if (!mDatas.get(position).equals(mAdminUser)) {
                        new AlertDialog.Builder(mContext).setItems(new String[]{"移除成员"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mPresenter!=null)
                                    mPresenter.deleteMember(mDatas.get(position));
                            }
                        }).show();
                        return true;
                    }
                    return false;
                }
            });
        }
        return viewHolder;
    }

    public void setPresenter(TeamMemberPresenter presenter)
    {
        mPresenter = presenter;
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
            SimpleDraweeView view = findView(R.id.iv_avatar);
            if (userBean.getUserAvatar() != null) {
                view.setImageURI(userBean.getUserAvatar().getUrl());
            } else {
                view.setImageURI((String) null);
            }
            if (mAdminUser != null && mAdminUser.equals(userBean)) {
                setVisibility(R.id.tv_admin, View.VISIBLE);
            } else {
                setVisibility(R.id.tv_admin, View.GONE);
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
