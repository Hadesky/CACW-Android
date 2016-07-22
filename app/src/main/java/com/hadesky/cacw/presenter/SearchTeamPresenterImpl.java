package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;
import com.hadesky.cacw.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * Created by 45517 on 2016/7/22.
 */
public class SearchTeamPresenterImpl implements SearchPersonOrTeamPresenter {
    private SearchPersonOrTeamView<TeamBean> mView;

    private static final int QUERY_COUNT_EACH_QUERY = 5;//每次查询的数量

    private Subscription mSubscription;

    private BmobQuery<TeamBean> mQuery;

    private int mReceivedCount;//自从上次调用search之后查询到的数据数和

    public SearchTeamPresenterImpl(SearchPersonOrTeamView<TeamBean> view) {
        mView = view;
    }

    @Override
    public void search(String key) {
        createMainQuery(key);

        mView.showProgress();
        mSubscription = mQuery.findObjects(new FindListener<TeamBean>() {
            @Override
            public void done(List<TeamBean> list, BmobException e) {
                mView.hideProgress();
                mReceivedCount = list.size();
                mView.setData(list, mReceivedCount < QUERY_COUNT_EACH_QUERY);
            }
        });
    }

    private void createMainQuery(String key) {
        List<BmobQuery<TeamBean>> queries = new ArrayList<>();
        BmobQuery<TeamBean> idQuery = new BmobQuery<>();
        if (StringUtils.isAllDigest(key)) {
            Long longKey = Long.parseLong(key);
            idQuery.addWhereEqualTo("mTeamId", longKey);
            queries.add(idQuery);
        }

        BmobQuery<TeamBean> nameQuery = new BmobQuery<>();
        nameQuery.addWhereContains("mTeamName", key);
        queries.add(nameQuery);

        mQuery = new BmobQuery<>();
        mQuery.or(queries);
        mQuery.setLimit(QUERY_COUNT_EACH_QUERY);
    }

    @Override
    public void showNextResults() {
        if (mQuery != null) {
            mQuery.setSkip(mReceivedCount);

            mView.showProgress();
            mSubscription = mQuery.findObjects(new FindListener<TeamBean>() {
                @Override
                public void done(List<TeamBean> list, BmobException e) {
                    mView.hideProgress();
                    mReceivedCount += list.size();
                    mView.addData(list, list.size() < QUERY_COUNT_EACH_QUERY);
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
