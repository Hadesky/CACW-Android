package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTeamAdapter;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.presenter.MyTeamPresenterImpl;
import com.hadesky.cacw.presenter.MyteamPresenter;
import com.hadesky.cacw.ui.view.MyTeamView;
import com.hadesky.cacw.ui.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MyTeamActivity extends BaseActivity implements MyTeamView
{


    private RecyclerView mRecyclerView;
    private MyTeamAdapter mMyTeamAdapter;
    private MyteamPresenter mPresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_my_team;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
    }

    @Override
    public void setupView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMyTeamAdapter = new MyTeamAdapter(new ArrayList<TeamBean>(),R.layout.list_item_team);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMyTeamAdapter);

        mPresenter = new MyTeamPresenterImpl(this);
        List<TeamBean> list = new ArrayList<>();
        mMyTeamAdapter.setDatas(list);
    }

    @Override
    public void showTeamList(List<TeamBean> list)
    {
        mMyTeamAdapter.setDatas(list);
    }

    @Override
    public void showProgressBar(boolean visibility)
    {

    }

    @Override
    public void showMessage(String msg)
    {

    }
}
