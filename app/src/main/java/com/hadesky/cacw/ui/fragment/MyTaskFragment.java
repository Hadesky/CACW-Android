package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTaskRecyclerAdapter;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.presenter.MyTaskPresenter;
import com.hadesky.cacw.presenter.MyTaskPresenterImpl;
import com.hadesky.cacw.widget.AnimProgressDialog;
import com.hadesky.cacw.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的任务Fragment
 * Created by Bright Van on 2015/9/7/007.
 */
public class MyTaskFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,TaskView
{

    private MyTaskRecyclerAdapter mAdapter;
    private List<TaskBean> mDatas;
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

        mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mDatas.add(new TaskBean());
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyTaskRecyclerAdapter(getContext(),mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                //当第一个item完全显示的时候，刷新可用
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                    mSwipeRefreshLayout.setEnabled(true);
                else
                    mSwipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

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
    public void onFalure(String msg)
    {
        Toast.makeText(getContext(),"操作失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWaitingDialog(boolean is)
    {
        if (is)
            mDialog.show();
        else
            mDialog.cancel();
    }
}
