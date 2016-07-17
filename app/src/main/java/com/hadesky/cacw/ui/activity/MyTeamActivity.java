package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTeamAdapter;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.presenter.MyTeamPresenterImpl;
import com.hadesky.cacw.presenter.MyteamPresenter;
import com.hadesky.cacw.ui.view.MyTeamView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.ui.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MyTeamActivity extends BaseActivity implements MyTeamView
{


    private RecyclerView mRecyclerView;
    private MyTeamAdapter mMyTeamAdapter;
    private MyteamPresenter mPresenter;
    private AnimProgressDialog mProgressDialog;


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

        mProgressDialog = new AnimProgressDialog(this, false, null, "获取中...");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyTeamAdapter = new MyTeamAdapter(new ArrayList<TeamMember>(),R.layout.list_item_team);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMyTeamAdapter);

        mPresenter = new MyTeamPresenterImpl(this);
        mPresenter.LoadAllTeams();
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
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
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
}
