package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;

public class TeamInfoActivity extends BaseActivity {

    static public final String IntentTag = "team";
    private AnimProgressDialog mProgressDialog;

    TextView mTvTeamName;
    TextView mTvTeamId;
    TextView mTvSummary;


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
    }

    @Override
    public void setupView() {

        Intent i = getIntent();
        if (i!=null) {
            TeamBean teamBean = (TeamBean) i.getSerializableExtra(IntentTag);
            mTvTeamId.setText(teamBean.getObjectId());
            mTvTeamName.setText(teamBean.getTeamName());
            mTvSummary.setText(teamBean.getSummary());
        }

    }


    private void onSumaryClick()
    {



    }
}
