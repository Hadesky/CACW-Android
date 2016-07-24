package com.hadesky.cacw.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.InvitePersonAdapter;
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.activity.InviteMemberActivity;
import com.hadesky.cacw.ui.fragment.InvitePersonFragment;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;
import com.hadesky.cacw.util.StringUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 *
 * Created by 45517 on 2016/7/22.
 */
public class InvitePersonPresenterImpl extends SearchPersonPresenterImpl implements InvitePersonPresenter {

    private static final String TAG = "InvitePersonPresenter";

    private InviteMemberActivity.OnInviteListener mOnInviteListener;

    private TeamBean mCurrentTeam;

    private InvitePersonFragment mInvitePersonFragment;

    public InvitePersonPresenterImpl(SearchPersonOrTeamView<UserBean> view, Context context,
                                     InvitePersonFragment invitePersonFragment) {
        super(view, context);
        mInvitePersonFragment = invitePersonFragment;
    }

    @Override
    protected SearchPersonAdapter createAdapter() {
        return new InvitePersonAdapter(null, R.layout.item_person_in_invite, this);
    }

    @Override
    public void OnItemClick(View view, int position) {
        if (view instanceof Button) {
            mOnInviteListener.onInvite(position);
        } else {
            super.OnItemClick(view, position);
        }
    }

    @Override
    public void setOnInviteListener(InviteMemberActivity.OnInviteListener listener) {
        mOnInviteListener = listener;
    }

    @Override
    public void setTeamMember(List<UserBean> teamMember) {
        ((InvitePersonAdapter) mAdapter).setTeamMember(teamMember);
    }

    @Override
    public void handleInviteMessage(String s, final int position) {
        if (mInvitePersonFragment != null && mCurrentTeam != null) {
            UserBean invitedUser = mAdapter.getDatas().get(position);
            MessageBean messageBean = new MessageBean();
            messageBean.setSender(MyApp.getCurrentUser());
            messageBean.setReceiver(invitedUser);
            messageBean.setMsg(handleMessage(s));
            messageBean.setType(MessageBean.TYPE_TEAM_TO_USER);
            messageBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        mInvitePersonFragment.showMsg("成功发送邀请");
                        mInvitePersonFragment.disableInviteButton(position);
                    } else {
                        mInvitePersonFragment.showMsg("发送失败，请检查网络");
                    }
                }
            });
        }
    }

    private String handleMessage(String msg) {
        return StringUtils.composeInviteOrJoinString(mCurrentTeam.getObjectId(), mCurrentTeam.getTeamName(), msg);
    }

    @Override
    public void setCurrentTeam(TeamBean currentTeam) {
        mCurrentTeam = currentTeam;
    }
}
