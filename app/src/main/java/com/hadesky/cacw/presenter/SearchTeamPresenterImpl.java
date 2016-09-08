package com.hadesky.cacw.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchTeamAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/** 团队搜索
 * Created by dzysg on 2016/9/8 0008.
 */
public class SearchTeamPresenterImpl implements SearchPresenter, BaseViewHolder.OnItemClickListener
{

    private SearchPersonOrTeamView mView;
    private TeamRepertory mTeamRepertory;
    private int page = 1;
    private int pageSize = 5;
    private SearchTeamAdapter mAdapter;
    private List<TeamBean> mTeams;
    private Context mContext;
    private boolean mIsFinal;
    private String mSearchText;
    private Subscription mSubscription;
    private PublishSubject<String> mPublishSubject;
    private boolean mIsShow = false;
    private boolean mHasAdapter = false;


    public SearchTeamPresenterImpl(SearchPersonOrTeamView view,Context context)
    {
        mContext = context;
        mView = view;
        mTeamRepertory = TeamRepertory.getInstance();
        mPublishSubject = PublishSubject.create();
        mAdapter = new SearchTeamAdapter(null, R.layout.item_team_in_search,this);
        mPublishSubject.debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s)
                    {
                        doSearch(s);
                    }
                });
    }

    @Override
    public void search(String key)
    {
        mPublishSubject.onNext(key);
    }

    private void doSearch(String key)
    {
        page = 1;
        mIsFinal = false;
        mSearchText = key;
        mView.showProgress();
        if(mSubscription!=null)
            mSubscription.unsubscribe();
        mSubscription = mTeamRepertory.searchTeam(key,pageSize,0)
                .subscribe(new RxSubscriber<List<TeamBean>>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(List<TeamBean> list)
                    {
                        mView.hideProgress();
                        if (list.size() == 0)
                        {
                            mView.hide();
                        } else
                        {
                            mView.show();
                            mIsFinal = list.size() < pageSize;
                            if (!mHasAdapter)
                            {
                                mView.setAdapter(mAdapter);
                                mHasAdapter = true;
                            }
                            mAdapter.setData(list, mIsFinal);
                            mTeams = list;
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
        mSubscription = mTeamRepertory.searchTeam(mSearchText, 10, offset).subscribe(new RxSubscriber<List<TeamBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
                mView.showMsg(msg);
            }
            @Override
            public void _onNext(List<TeamBean> teams)
            {
                mIsFinal = teams.size() < pageSize;
                mView.hideProgress();
                mAdapter.addData(teams, mIsFinal);
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
    public void OnItemClick(View view, final int position)
    {
        if (mAdapter.getNextResultPosition() == position)
        {
            showNextResults();
            return;
        }
        if(mTeams.size()>position)
        {
            View dialog = View.inflate(mContext,R.layout.dialog_nick_name, null);
            final EditText editText = (EditText) dialog.findViewById(R.id.edit_text);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getString(R.string.invite_dialog_title))
                    .setView(dialog)
                    .setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             applyTeam(position,editText.getText().toString());
                        }
                    });
            builder.create().show();
        }
    }

    private void applyTeam(final int pos , String content)
    {
        mSubscription = mTeamRepertory.applyTeam(mTeams.get(pos).getId(),content)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {

                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mAdapter.disableJoinBtn(pos);
                    }
                });
    }
}
