package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTaskRecyclerAdapter;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.presenter.MyTaskPresenter;
import com.hadesky.cacw.presenter.MyTaskPresenterImpl;
import com.hadesky.cacw.ui.view.TaskView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.ui.widget.RecyclerViewItemDecoration;
import com.hadesky.cacw.ui.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的任务Fragment
 * Created by Bright Van on 2015/9/7/007.
 */
public class MyTaskFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,TaskView
{

    private MyTaskRecyclerAdapter mAdapter;
    private AnimProgressDialog mDialog ;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyTaskPresenter mPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_task;
    }

    @Override
    protected void initViews(View view)
    {
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.rv_task);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.layout_swipe_refresh);
    }

    @Override
    protected void setupViews(Bundle bundle)
    {
        mDialog = new AnimProgressDialog(getActivity(), false, null, "正在发送请求");
        mPresenter = new MyTaskPresenterImpl(this);


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_primary));
        mSwipeRefreshLayout.setProgressViewOffset(true, -100, 50);
        mSwipeRefreshLayout.setScrollUpChild(mRecyclerView);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyTaskRecyclerAdapter(new ArrayList<TaskBean>(),mPresenter,R.layout.list_item_teamtask);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPresenter.LoadTasks();
    }

    @Override
    public void onRefresh()
    {
        mPresenter.LoadTasks();
    }

    @Override
    public void showDatas(List<TaskBean> tasks)
    {
        mAdapter.setDatas(tasks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress()
    {

    }
    @Override
    public void hideProgress()
    {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMsg(String msg)
    {
        showToast(msg);
    }

}
