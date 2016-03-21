package com.hadesky.cacw.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTeamAdapter;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MyTeamActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private MyTeamAdapter mMyTeamAdapter;
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

        mMyTeamAdapter = new MyTeamAdapter(this);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMyTeamAdapter);
        List<TeamBean> list = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            list.add(new TeamBean("生学会",1));
        }
        mMyTeamAdapter.setDatas(list);
    }
}
