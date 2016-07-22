package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.view.SearchPersonView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * Created by 45517 on 2016/7/22.
 */
public class SearchPersonPresenterImpl implements SearchPersonPresenter {

    private static final int QUERY_COUNT_EACH_QUERY = 5;//每次查询的数量

    private Subscription mSubscription;
    private SearchPersonView mView;

    private BmobQuery<UserBean> mQuery;

    private int mReceivedCount;//自从上次调用search之后查询到的数据数和

    public SearchPersonPresenterImpl(SearchPersonView view) {
        mView = view;
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
                mView.setData(list, mReceivedCount < QUERY_COUNT_EACH_QUERY);
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

            mView.showProgress();
            mSubscription = mQuery.findObjects(new FindListener<UserBean>() {
                @Override
                public void done(List<UserBean> list, BmobException e) {
                    mView.hideProgress();
                    mReceivedCount += list.size();
                    mView.addData(list, list.size() < QUERY_COUNT_EACH_QUERY);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        mSubscription.unsubscribe();
    }
}
