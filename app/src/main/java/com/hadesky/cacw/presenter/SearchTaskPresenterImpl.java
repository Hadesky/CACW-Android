package com.hadesky.cacw.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.adapter.SearchTaskAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.SearchActivity;
import com.hadesky.cacw.ui.activity.TaskDetailActivity;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * 搜索界面的搜索任务presenter
 * Created by Leaves on 2016/8/3.
 */
public class SearchTaskPresenterImpl implements SearchPresenter, BaseViewHolder.OnItemClickListener {

    private static final int QUERY_COUNT_EACH_QUERY = 5;//每次查询的数量

    private Subscription mSubscription;

    protected SearchActivity mView;

    private BmobQuery<TaskBean> mQuery;

    protected SearchTaskAdapter mAdapter;

    protected Context mContext;

    private int mReceivedCount;//自从上次调用search之后查询到的数据数和

    public SearchTaskPresenterImpl(SearchActivity view, Context context) {
        mContext = context;
        mView = view;
        mAdapter = createAdapter();
        mView.setSearchTaskAdapter(mAdapter);
    }

    protected SearchTaskAdapter createAdapter() {
        return new SearchTaskAdapter(null, R.layout.item_task_search, this);
    }

    @Override
    public void search(String key) {
        createMainQuery(key);

        mView.onFragmentLoadingStart();
        mSubscription = mQuery.findObjects(new FindListener<TaskBean>() {
            @Override
            public void done(List<TaskBean> list, BmobException e) {
                if (e == null) {
                    mView.onFragmentLoadingEnd();
                    mReceivedCount = list.size();
                    if (mReceivedCount == 0) {
                        mAdapter.clear();
                    } else {
                        mAdapter.setData(list, mReceivedCount < QUERY_COUNT_EACH_QUERY);
                    }
                }
            }
        });
    }

    private void createMainQuery(String key) {
        List<BmobQuery<TaskBean>> queries = new ArrayList<>();

        BmobQuery<TaskBean> titleQuery = new BmobQuery<>();
        titleQuery.addWhereContains("mTitle", key);
        queries.add(titleQuery);

        BmobQuery<TaskBean> contentQuery = new BmobQuery<>();
        contentQuery.addWhereContains("mContent", key);
        queries.add(contentQuery);

        BmobQuery<TaskBean> locationQuery = new BmobQuery<>();
        locationQuery.addWhereContains("mLocation", key);
        queries.add(locationQuery);

        mQuery = new BmobQuery<>();
        mQuery.or(queries);
        mQuery.setLimit(QUERY_COUNT_EACH_QUERY);
    }

    @Override
    public void showNextResults() {
        if (mQuery != null) {
            mQuery.setSkip(mReceivedCount);

            mAdapter.showProgress();
            mSubscription = mQuery.findObjects(new FindListener<TaskBean>() {
                @Override
                public void done(List<TaskBean> list, BmobException e) {
                    mAdapter.hideProgress();
                    mReceivedCount += list.size();
                    mAdapter.addData(list, list.size() < QUERY_COUNT_EACH_QUERY);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscription!=null)
            mSubscription.unsubscribe();
    }

    /**
     * 按下搜索到的人的item
     * @param view view 或 button
     * @param position 在layout里的position
     */
    @Override
    public void OnItemClick(View view, int position) {
        if (position == mAdapter.getNextResultPosition()) {
            showNextResults();
        } else {
//            Intent intent = new Intent(mContext, TaskDetailActivity.class);
//            intent.putExtra(IntentTag.TAG_TASK_BEAN, mAdapter.getDatas().get(position));
//            mContext.startActivity(intent);
        }
    }
}
