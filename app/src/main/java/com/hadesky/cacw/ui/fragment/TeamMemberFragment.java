package com.hadesky.cacw.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.TeamMemberPresenter;
import com.hadesky.cacw.presenter.TeamMemberPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.InviteMemberActivity;
import com.hadesky.cacw.ui.view.TeamMemberView;
import com.microstudent.app.bouncyfastscroller.vertical.VerticalBouncyFastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamMemberFragment extends BaseFragment implements TeamMemberView{

    private RecyclerView mRecyclerView;
    private VerticalBouncyFastScroller mScroller;

    private TeamBean mTeamBean;

    private TeamMemberPresenter mPresenter;

    private List<UserBean> mTeamMember;


    /**
     * @param teamBean 用于新建Fragment的TeamBean
     * @return A new instance of fragment TeamMemberFragment.
     */
    public static TeamMemberFragment newInstance(TeamBean teamBean) {
        TeamMemberFragment fragment = new TeamMemberFragment();
        Bundle args = new Bundle();
        args.putSerializable(IntentTag.TAG_TEAM_BEAN, teamBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamBean = (TeamBean) getArguments().getSerializable(IntentTag.TAG_TEAM_BEAN);
        }
        mPresenter = new TeamMemberPresenterImpl(this,mTeamBean);
    }


    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_team_member;
    }

    @Override
    protected void initViews(View view)
    {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mScroller = (VerticalBouncyFastScroller) view.findViewById(R.id.vbfs);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mPresenter.loadMembers();
    }

    @Override
    protected void setupViews(Bundle bundle)
    {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    @Override
    public void setData(Object[] data) {
        if (data != null) {
            mScroller.setRecyclerView(mRecyclerView);
            mScroller.setData(data);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }

    public void navigateToInviteMemberActivity() {
        ArrayList<UserBean> data =null; //(ArrayList<UserBean>) mPresenter.getData();
        if (data != null) {
            Intent intent = new Intent(getContext(), InviteMemberActivity.class);
            intent.putExtra(IntentTag.TAG_TEAM_MEMBER, data);
            intent.putExtra(IntentTag.TAG_TEAM_BEAN, mTeamBean);
            startActivity(intent);
        }
    }
}
