package com.hadesky.cacw.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.UserRepertory;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 *  搜索用户
 * Created by dzysg on 2016/9/7 0007.
 */
public class SearchPersonPresenterImpl implements SearchPresenter, BaseViewHolder.OnItemClickListener
{
    private SearchPersonOrTeamView mView;
    private UserRepertory mUserRepertory;
    private int page = 1;
    private int pageSize = 5;
    private SearchPersonAdapter mAdapter;
    private List<UserBean> mUsers;
    private Context mContext;
    private boolean mIsFinal;
    private String mSearchText;
    private Subscription mSubscription;
    private PublishSubject<String> mPublishSubject;
    private boolean mIsShow = false;
    private boolean mHasAdapter = false;


    public SearchPersonPresenterImpl(SearchPersonOrTeamView view, Context context)
    {
        mView = view;
        mContext = context;
        mUserRepertory = UserRepertory.getInstance();
        mAdapter = new SearchPersonAdapter(null, R.layout.item_person_in_search, this);
        mPublishSubject = PublishSubject.create();
        //1秒搜索延迟，即以1秒内最后一次的关键词为准
        mPublishSubject.debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                doSearch(s);
            }
        });
    }


    private void doSearch(String key)
    {
        if (key.length() == 0)
        {
            mView.hide();
            mIsShow = false;
            return;
        }
        page = 1;
        mIsFinal = false;
        mSearchText = key;
        mView.showProgress();
        if(mSubscription!=null)
            mSubscription.unsubscribe();
        mSubscription = mUserRepertory.searchUser(key, pageSize, 0,null).subscribe(new RxSubscriber<List<UserBean>>()
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

                if (userBeen.size() == 0)
                {
                    mView.hide();
                } else
                {
                    mView.show();
                    mIsFinal = userBeen.size() < pageSize;
                    if (!mHasAdapter)
                    {
                        mView.setAdapter(mAdapter);
                        mHasAdapter = true;
                    }
                    mAdapter.setData(userBeen, mIsFinal);
                    mUsers = userBeen;
                }
            }
        });
    }


    @Override
    public void search(String key)
    {
        mPublishSubject.onNext(key);
    }

    @Override
    public void LoadNextPage()
    {
        if (mIsFinal)
            return;

        mView.showProgress();
        page++;
        int offset = pageSize * (page - 1);
        mSubscription = mUserRepertory.searchUser(mSearchText, 10, offset,null).subscribe(new RxSubscriber<List<UserBean>>()
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
        if (mAdapter.getNextResultPosition() == position)
        {
            LoadNextPage();
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
