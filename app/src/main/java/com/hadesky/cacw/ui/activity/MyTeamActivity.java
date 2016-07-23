package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTeamAdapter;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.presenter.MyTeamPresenter;
import com.hadesky.cacw.presenter.MyTeamPresenterImpl;
import com.hadesky.cacw.ui.view.MyTeamView;
import com.hadesky.cacw.ui.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;

public class MyTeamActivity extends BaseActivity implements MyTeamView, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private MyTeamAdapter mMyTeamAdapter;
    private MyTeamPresenter mPresenter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_team;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
    }

    @Override
    public void setupView() {

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_primary));
        mRefreshLayout.setProgressViewOffset(true, -100, 50);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMyTeamAdapter = new MyTeamAdapter(new ArrayList<TeamMember>(),R.layout.list_item_team);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMyTeamAdapter);

        mPresenter = new MyTeamPresenterImpl(this);

        mPresenter.LoadAllTeams(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);

        List<TeamMember> list = new ArrayList<>();
        mMyTeamAdapter.setDatas(list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void showTeamList(List<TeamMember> list)
    {
        mMyTeamAdapter.setDatas(list);
    }

    @Override
    public void showProgress() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMsg(String msg)
    {
        showToast(msg);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyTeamAdapter.onDestroy();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.LoadAllTeams(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
    }
}
