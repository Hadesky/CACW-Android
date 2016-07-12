package com.hadesky.cacw.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.TeamInfoPresenter;
import com.hadesky.cacw.presenter.TeamInfoPresenterImpl;
import com.hadesky.cacw.ui.view.TeamInfoView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class TeamInfoActivity extends BaseActivity implements TeamInfoView {

    static public final String IntentTag = "team";
    private AnimProgressDialog mProgressDialog;

    private TextView mTvTeamName;
    private TextView mTvTeamId;
    private TextView mTvSummary;
    private RecyclerView mRcvMembers;
    private BaseAdapter<TeamMember> mAdapter;
    private TeamInfoPresenter mPresenters;
    private TeamBean mTeam;

    @Override
    public int getLayoutId() {
        return R.layout.activity_team_info;
    }

    @Override
    public void initView() {
        mTvTeamId = (TextView) findViewById(R.id.tv_teamid);
        mTvTeamName = (TextView) findViewById(R.id.tv_team_name);
        mTvSummary = (TextView) findViewById(R.id.tv_team_summary);
        mProgressDialog = new AnimProgressDialog(this, false, null, "获取中...");
        mRcvMembers = (RecyclerView) findViewById(R.id.rcv_team_member);

    }

    @Override
    public void setupView() {

        Intent i = getIntent();

        mTeam = (TeamBean) i.getSerializableExtra(IntentTag);
        if (mTeam==null)
        {
            finish();
            return;
        }

        showInfo();

        mAdapter = new BaseAdapter<TeamMember>(new ArrayList<TeamMember>(), R.layout.list_item_member) {
            @Override
            public BaseViewHolder<TeamMember> createHolder(View v, Context context) {

                BaseViewHolder<TeamMember> holder = new BaseViewHolder<TeamMember>(v) {
                    @Override
                    public void setData(TeamMember o) {
                        setTextView(R.id.tv, o.getUser().getNickName());
                    }
                };

                return holder;
            }
        };


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcvMembers.setLayoutManager(manager);
        mRcvMembers.setVerticalFadingEdgeEnabled(false);
        mRcvMembers.setAdapter(mAdapter);

        mPresenters = new TeamInfoPresenterImpl(mTeam,this);


        View v = findViewById(R.id.team_summary);
        if (v!=null)
        {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSumaryClick();
                }
            });
        }
    }

    private void showInfo()
    {

        mTvTeamId.setText(mTeam.getObjectId());
        mTvTeamName.setText(mTeam.getTeamName());
        mTvSummary.setText(mTeam.getSummary());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenters.getTeamMembers();
    }

    private void onSumaryClick() {
        if (mTeam.getAdminUserId().equals(MyApp.getCurrentUser().getObjectId()))
        {

            View view = getLayoutInflater().inflate(R.layout.dialog_nick_name, null);
            final EditText editText = (EditText) view.findViewById(R.id.edit_text);
            editText.setText(mTvSummary.getText());
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.changeSummary))
                    .setView(view)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenters.changeeSummary(editText.getText().toString());
                            showInfo();
                        }
                    });
            builder.create().show();
        }

    }

    @Override
    public void showMembers(List<TeamMember> list) {
        mAdapter.setDatas(list);
    }

    @Override
    public void showProject(List<ProjectBean> list) {

    }

    @Override
    public void showProgress() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }
}
