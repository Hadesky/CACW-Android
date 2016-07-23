package com.hadesky.cacw.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.InvitePersonAdapter;
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.InviteMemberActivity;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.ui.fragment.InvitePersonFragment;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * Created by 45517 on 2016/7/22.
 */
public class InvitePersonPresenterImpl extends SearchPersonPresenterImpl implements InvitePersonPresenter {

    private InvitePersonFragment.OnInviteListener mOnInviteListener;

    public InvitePersonPresenterImpl(SearchPersonOrTeamView<UserBean> view, Context context) {
        super(view, context);
    }

    @Override
    protected SearchPersonAdapter createAdapter() {
        return new InvitePersonAdapter(null, R.layout.item_person_in_invite, this);
    }


    @Override
    public void OnItemClick(View view, int position) {
        if (view instanceof Button) {
            mOnInviteListener.onInvite(mAdapter.getDatas().get(position));
        } else {
            super.OnItemClick(view, position);
        }
    }

    @Override
    public void setOnInviteListener(InvitePersonFragment.OnInviteListener listener) {
        mOnInviteListener = listener;
    }

    @Override
    public void setTeamMember(List<UserBean> teamMember) {
        ((InvitePersonAdapter) mAdapter).setTeamMember(teamMember);
    }
}
