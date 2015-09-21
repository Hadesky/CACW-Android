package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTaskRecyclerAdapter;
import com.hadesky.cacw.bean.TaskBean;

import java.util.ArrayList;
import java.util.List;




/**
 * 我的任务Fragment
 * Created by Bright Van on 2015/9/7/007.
 */
public class MyTaskFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{


    private MyTaskRecyclerAdapter mAdapter;
    private List<TaskBean> mDatas;

    private LinearLayoutManager mLayoutManager;
    private PopupMenu mPopupMenu;
    private TextView mTvSelectRange;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public int getLayoutId() {

        return R.layout.fragment_my_task;
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.lv_task_my_task_fragment);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.srl_my_task_fragment);
        mTvSelectRange = (TextView) mContentView.findViewById(R.id.tv_my_task_range);
    }

    @Override
    protected void setupViews(Bundle bundle) {

        mDatas = new ArrayList<TaskBean>();
        for (int i = 0; i < 15; i++) {
            mDatas.add(new TaskBean());
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyTaskRecyclerAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当第一个item完全显示的时候，刷新可用
                if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                    mSwipeRefreshLayout.setEnabled(true);
                else
                    mSwipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mPopupMenu = new PopupMenu(getContext(),mTvSelectRange);
        getActivity().getMenuInflater().inflate(R.menu.menu_task_popup,mPopupMenu.getMenu());
        mTvSelectRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu.show();
            }
        });
    }
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


}
