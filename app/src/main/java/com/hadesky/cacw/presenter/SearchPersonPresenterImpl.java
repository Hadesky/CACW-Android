package com.hadesky.cacw.presenter;

import android.content.Intent;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 *
 * Created by 45517 on 2016/7/22.
 */
public class SearchPersonPresenterImpl implements SearchPersonOrTeamPresenter {

    private static final int QUERY_COUNT_EACH_QUERY = 5;//每次查询的数量

    private Subscription mSubscription;

    private SearchPersonOrTeamView<UserBean> mView;

    private BmobQuery<UserBean> mQuery;

    private SearchPersonAdapter mAdapter;

    private int mReceivedCount;//自从上次调用search之后查询到的数据数和

    public SearchPersonPresenterImpl(SearchPersonOrTeamView<UserBean> view) {
        mView = view;
        mAdapter = new SearchPersonAdapter(null, R.layout.item_person_in_search, new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (position == mAdapter.getNextResultPosition()) {
                    showNextResults();
                } else {
                    Intent intent = new Intent(mView.getContext(), UserInfoActivity.class);
                    intent.putExtra(IntentTag.TAG_USER_BEAN, mAdapter.getDatas().get(position));
                    mView.getContext().startActivity(intent);
                }
            }
        });
        mView.setAdapter(mAdapter);
    }

    @Override
    public void search(String key) {
        createMainQuery(key);

        mView.showProgress();
        mSubscription = mQuery.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> list, BmobException e) {
                mView.hideProgress();
                mReceivedCount = list.size();
                if (mReceivedCount == 0) {
                    mView.hide();
                } else {
                    mView.show();
                    mAdapter.setData(list, mReceivedCount < QUERY_COUNT_EACH_QUERY);
                }
            }
        });
    }

    private void createMainQuery(String key) {
        List<BmobQuery<UserBean>> queries = new ArrayList<>();

        BmobQuery<UserBean> phoneQuery = new BmobQuery<>();
        phoneQuery.addWhereEqualTo("mobilePhoneNumber", key);
        queries.add(phoneQuery);

        BmobQuery<UserBean> emailQuery = new BmobQuery<>();
        emailQuery.addWhereEqualTo("email", key);
        queries.add(emailQuery);

        BmobQuery<UserBean> nickNameQuery = new BmobQuery<>();
        nickNameQuery.addWhereContains("mNickName", key);
        queries.add(nickNameQuery);

        mQuery = new BmobQuery<>();
        mQuery.or(queries);
        mQuery.setLimit(QUERY_COUNT_EACH_QUERY);
    }

    @Override
    public void showNextResults() {
        if (mQuery != null) {
            mQuery.setSkip(mReceivedCount);

            mAdapter.showProgress();
            mSubscription = mQuery.findObjects(new FindListener<UserBean>() {
                @Override
                public void done(List<UserBean> list, BmobException e) {
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
}
