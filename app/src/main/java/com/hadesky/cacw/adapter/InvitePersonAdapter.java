package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 * Created by 45517 on 2016/7/22.
 */
public class InvitePersonAdapter extends SearchPersonAdapter {

    private List<UserBean> mTeamMember;//已经加入团队的成员

    public InvitePersonAdapter(List<UserBean> list, @LayoutRes int layoutid,
                               BaseViewHolder.OnItemClickListener listener) {
        super(list, layoutid, listener);
    }

    @Override
    public BaseViewHolder<UserBean> createHolder(View v, Context context) {
        return new InvitePersonViewHolder(v);
    }

    class InvitePersonViewHolder extends BaseViewHolder<UserBean> {

        public InvitePersonViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(UserBean userBean) {
            setTextView(R.id.tv_nick_name, userBean.getNickName());
            setTextView(R.id.tv_summary, userBean.getSummary());
            SimpleDraweeView view = findView(R.id.iv_avatar);
            if (userBean.getUserAvatar() != null) {
                view.setImageURI(userBean.getUserAvatar().getUrl());
            } else {
                view.setImageURI((String) null);
            }
            if (mTeamMember != null && mTeamMember.contains(userBean)) {
                setVisibility(R.id.tv_team_member, View.VISIBLE);
                setVisibility(R.id.bt_invite, View.GONE);
            } else {
                setVisibility(R.id.tv_team_member, View.GONE);
                setVisibility(R.id.bt_invite, View.VISIBLE);
            }
            setOnItemClickListener(mOnItemClickListener);
            setButtonOnClickListener(R.id.bt_invite, mOnItemClickListener);
        }
    }

    public void setTeamMember(List<UserBean> teamMember) {
        mTeamMember = teamMember;
    }
}
