package com.hadesky.cacw.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchTeamAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.fragment.SearchTeamFragment;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;
import com.hadesky.cacw.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * Created by 45517 on 2016/7/22.
 */
public class SearchTeamPresenterImpl implements SearchPersonOrTeamPresenter, BaseViewHolder.OnItemClickListener {
    private static final String TAG = "SearchTeamPresenter";
    private SearchPersonOrTeamView<TeamBean> mView;

    private static final int QUERY_COUNT_EACH_QUERY = 5;//每次查询的数量

    private Subscription mSubscription;

    private BmobQuery<TeamBean> mQuery;

    private int mReceivedCount;//自从上次调用search之后查询到的数据数和

    private SearchTeamAdapter mAdapter;

    private Context mContext;

    public SearchTeamPresenterImpl(SearchPersonOrTeamView<TeamBean> view, Context context) {
        mView = view;
        mAdapter = new SearchTeamAdapter(null, R.layout.item_team_in_search, this);
        mView.setAdapter(mAdapter);
        mContext = context;
    }

    @Override
    public void search(String key) {
        if (mReceivedCount == 0) {
            //第一次搜索，获取我的团队
            loadMyTeamsAndSearch(key);
            return;
        }
        mView.showProgress();

        platformSearch(key);
    }

    /**
     * 真正进行搜索的方法
     * @param key 关键字
     */
    private void platformSearch(String key) {
        createMainQuery(key);

        mSubscription = mQuery.findObjects(new FindListener<TeamBean>() {
            @Override
            public void done(List<TeamBean> list, BmobException e) {
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

    private void loadMyTeamsAndSearch(final String key) {
        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mUser", MyApp.getCurrentUser());
        mSubscription = query.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                if (e == null) {
                    mAdapter.setMyTeams(list);
                    platformSearch(key);
                } else {
                    mView.hideProgress();
                    mView.hide();
                }
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

            mAdapter.showProgress();
            mSubscription = mQuery.findObjects(new FindListener<TeamBean>() {
                @Override
                public void done(List<TeamBean> list, BmobException e) {
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

    @Override
    public void OnItemClick(View view, int position) {
        if (position == mAdapter.getNextResultPosition()) {
            showNextResults();
        } else if (view instanceof Button) {
            showApplyToJoinDialog(position);
        }
    }

    private void showApplyToJoinDialog(final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_with_edit_text_30, null);

        final EditText editText = (EditText) view.findViewById(R.id.edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.invite_dialog_title))
                .setView(view)
                .setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleJoinMessage(editText.getText().toString(), position);
                    }
                });
        builder.create().show();
    }

    private void handleJoinMessage(String s, final int position) {
        TeamBean teamToApply = mAdapter.getDatas().get(position);
        MessageBean messageBean = new MessageBean();
        messageBean.setSender(MyApp.getCurrentUser());
        messageBean.setReceiver(teamToApply.getAdminUser());
        messageBean.setMsg(StringUtils.composeInviteOrJoinString(teamToApply.getObjectId(), teamToApply.getTeamName(), s));
        messageBean.setType(MessageBean.TYPE_USER_TO_TEAM);
        messageBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mView.showMsg("成功发送请求");
                    ((SearchTeamFragment) mView).disableJoinButton(position);
                } else {
                    mView.showMsg("发送失败，请检查网络");
                }
            }
        });
    }
}
