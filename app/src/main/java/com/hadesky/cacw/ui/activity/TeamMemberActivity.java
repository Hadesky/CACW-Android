package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.TeamMemberFragment;
import com.hadesky.cacw.ui.fragment.UserInfoFragment;

public class TeamMemberActivity extends BaseActivity {

    private TeamMemberFragment mFragment;

    private TeamBean mTeam;

    @Override
    public int getLayoutId() {
        return R.layout.activity_team_member;
    }

    @Override
    public void initView() {
    }

    @Override
    public void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fm = getSupportFragmentManager();

        mFragment = (TeamMemberFragment) fm.findFragmentById(R.id.container);

        mTeam = (TeamBean) getIntent().getSerializableExtra(IntentTag.TAG_TEAM_BEAN);

        if (mFragment == null) {
            mFragment = TeamMemberFragment.newInstance(mTeam);
            fm.beginTransaction()
                    .add(R.id.container, mFragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_member:
                if (mFragment != null) {
                    mFragment.navigateToInviteMemberActivity();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_member, menu);
        return true;
    }
}
