package com.hadesky.cacw.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.UserRepertory;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.List;

import rx.Subscription;

/**
 * f 搜索用户
 * Created by dzysg on 2016/9/7 0007.
 */
public class SearchPersonPresenterImpl implements SearchPresenter, BaseViewHolder.OnItemClickListener
{

    private SearchPersonOrTeamView mView;
    private UserRepertory mUserRepertory;
    private int page = 1;
    private int pageSize = 10;
    private SearchPersonAdapter mAdapter;
    private List<UserBean> mUsers;
    private Context mContext;
    private boolean mIsFinal;
    private String mSearchText;
    private Subscription mSubscription;
    private UserBean Me;

    public SearchPersonPresenterImpl(SearchPersonOrTeamView view, Context context)
    {
        mView = view;
        mContext = context;
        mUserRepertory = UserRepertory.getInstance();
        mAdapter = new SearchPersonAdapter(null, R.layout.item_person_in_search, this);
        Me = MyApp.getCurrentUser();
    }

    @Override
    public void search(String key)
    {
        page = 1;
        mIsFinal = false;
        mSearchText = key;
        mView.showProgress();
        mSubscription = mUserRepertory.searchUser(key, pageSize, 0).subscribe(new RxSubscriber<List<UserBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
                mView.showMsg(msg);
            }

            @Override
            public void _onNext(List<UserBean> userBeen)
            {
                mView.hideProgress();
                mView.show();
                if (userBeen.size() == 0)
                    mView.hide();
                else
                {
                    mView.setAdapter(mAdapter);
                    if(userBeen.remove(Me))
                        mIsFinal = userBeen.size() < pageSize-1;
                    else
                        mIsFinal = userBeen.size() < pageSize;

                    mAdapter.setData(userBeen, mIsFinal);
                    mUsers = userBeen;
                }
            }
        });
    }

    @Override
    public void showNextResults()
    {
        if (mIsFinal)
            return;

        mView.showProgress();
        page++;
        int offset = pageSize * (page - 1);
        mSubscription = mUserRepertory.searchUser(mSearchText, 10, offset).subscribe(new RxSubscriber<List<UserBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
                mView.showMsg(msg);
            }

            @Override
            public void _onNext(List<UserBean> userBeen)
            {
                if(userBeen.remove(Me))
                    mIsFinal = userBeen.size() < pageSize-1;
                else
                    mIsFinal = userBeen.size() < pageSize;

                mView.hideProgress();

                mAdapter.addData(userBeen, mIsFinal);
            }
        });
    }



    @Override
    public void onDestroy()
    {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public void OnItemClick(View view, int position)
    {
        if(mAdapter.getNextResultPosition()==position)
        {
            showNextResults();
            return;
        }
        if (mUsers != null && mUsers.size() > position)
        {
            Intent intent = new Intent(mContext, UserInfoActivity.class);
            intent.putExtra(IntentTag.TAG_USER_BEAN, (Parcelable) mUsers.get(position));
            mContext.startActivity(intent);
        }
    }
}
