package com.hadesky.cacw.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.InvitePersonPresenter;
import com.hadesky.cacw.presenter.InvitePersonPresenterImpl;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.ui.activity.InviteMemberActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitePersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitePersonFragment extends SearchPersonFragment implements InviteMemberActivity.OnInviteListener {

    private static final String ARG_TEAM_MEMBER = "team_member";
    public static final String ARG_TEAM_BEAN = "team_bean";

    /**
     * @param searchKey 需要查找的Key.
     * @return A new instance of fragment SearchPersonFragment，可能为null
     */
    @Nullable
    public static <T extends SearchFragment> T newInstance(Class<T> subClass, String searchKey,
                                                           ArrayList<UserBean> teamMember, TeamBean teamBean) {
        T fragment = null;
        try {
            fragment = subClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_KEY, searchKey);
        args.putSerializable(ARG_TEAM_MEMBER, teamMember);
        args.putSerializable(ARG_TEAM_BEAN, teamBean);
        if (fragment != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<UserBean> teamMember = (List<UserBean>) getArguments().getSerializable(ARG_TEAM_MEMBER);
            TeamBean currentTeam = (TeamBean) getArguments().getSerializable(ARG_TEAM_BEAN);
            ((InvitePersonPresenter) mPresenter).setTeamMember(teamMember);
            ((InvitePersonPresenter) mPresenter).setOnInviteListener(this);
            ((InvitePersonPresenter) mPresenter).setCurrentTeam(currentTeam);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_person;
    }

    @Override
    protected SearchPersonOrTeamPresenter createPresenter() {
        return new InvitePersonPresenterImpl(this, getContext(), this);
    }

    @Override
    public void onInvite(int position) {
        showInviteDialog(position);
    }

    private void showInviteDialog(final int position) {
        View view = getLayoutInflater(null).inflate(R.layout.dialog_with_edit_text_30, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.invite_dialog_title))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((InvitePersonPresenter) mPresenter).handleInviteMessage(editText.getText().toString(),position);
                    }
                });
        builder.create().show();
    }


    public void disableInviteButton(int position) {
        if (position >= 0 && position < mRecyclerView.getChildCount()) {
            View view = mRecyclerView.getChildAt(position);
            Button button = (Button) view.findViewById(R.id.bt_invite);
            if (button != null) {
                button.setSelected(true);
                button.setEnabled(false);
            }
        }
    }
}
