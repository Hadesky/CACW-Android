package com.hadesky.cacw.ui.fragment;

import android.support.v4.app.Fragment;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.InvitePersonPresenter;
import com.hadesky.cacw.presenter.InvitePersonPresenterImpl;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.ui.activity.InviteMemberActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitePersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitePersonFragment extends SearchPersonFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_person;
    }

    @Override
    protected SearchPersonOrTeamPresenter createPresenter() {
        return new InvitePersonPresenterImpl(this, getContext());
    }

    public interface OnInviteListener {
        void onInvite(UserBean user);
    }

    public InvitePersonPresenter getPresenter() {
        return (InvitePersonPresenter) mPresenter;
    }
}
