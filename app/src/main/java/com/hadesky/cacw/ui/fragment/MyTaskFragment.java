package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTaskRecyclerAdapter;
import com.hadesky.cacw.bean.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**我的任务Fragment
 * Created by Bright Van on 2015/9/7/007.
 */
public class MyTaskFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private MyTaskRecyclerAdapter mAdapter;
    private List<TaskBean> mDatas;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_task;
    }

    @Override
    protected void initViews(View view) {

        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.lv_task_my_task_fragment);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.srl_my_task_fragment);
    }

    @Override
    protected void setupViews(Bundle bundle) {

        mDatas = new ArrayList<TaskBean>();
        for (int i = 0; i < 15; i++) {
            mDatas.add(new TaskBean());
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyTaskRecyclerAdapter(getContext(),mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(mLayoutManager.findFirstVisibleItemPosition() == 0);
            }
        });
    }


}
