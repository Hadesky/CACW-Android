package com.hadesky.cacw.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.presenter.TeamMemberPresenter;
import com.hadesky.cacw.presenter.TeamMemberPresenterImpl;
import com.hadesky.cacw.ui.view.TeamMemberView;
import com.microstudent.app.bouncyfastscroller.vertical.VerticalBouncyFastScroller;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamMemberFragment extends Fragment implements TeamMemberView{

    private RecyclerView mRecyclerView;
    private VerticalBouncyFastScroller mScroller;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TEAM_BEAN = "team_bean";

    private TeamBean mTeamBean;

    private TeamMemberPresenter mPresenter;

    public TeamMemberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamBean 用于新建Fragment的TeamBean
     * @return A new instance of fragment TeamMemberFragment.
     */
    public static TeamMemberFragment newInstance(TeamBean teamBean) {
        TeamMemberFragment fragment = new TeamMemberFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_BEAN, teamBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamBean = (TeamBean) getArguments().getSerializable(ARG_TEAM_BEAN);
        }
        mPresenter = new TeamMemberPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_member, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mScroller = (VerticalBouncyFastScroller) view.findViewById(R.id.vbfs);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mPresenter.loadData();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }

    @Override
    public TeamBean getTeamBean() {
        return mTeamBean;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);

//            if (mRecyclerView.getScrollY() < mRecyclerView.getHeight()) {
//                mScroller.setVisibility(View.INVISIBLE);
//            }
        }
    }

    @Override
    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(decoration);
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
    }
}
