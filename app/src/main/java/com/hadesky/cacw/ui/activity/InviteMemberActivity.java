package com.hadesky.cacw.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.InvitePersonFragment;

public class InviteMemberActivity extends BaseActivity
{


    private FragmentManager mFragmentManager;
    private InvitePersonFragment mInvitePersonFragment;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_invite_member;
    }

    @Override
    public void initView()
    {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void setupView()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        loadSearchPersonFragment();
    }

    private void loadSearchPersonFragment()
    {
        TeamBean currentTeam = (TeamBean) getIntent().getSerializableExtra(IntentTag.TAG_TEAM_BEAN);
        if (currentTeam == null)
        {
            Log.e("tag", "InviteMemberActivity get null teambean");
            return;
        }

        mInvitePersonFragment = new InvitePersonFragment();
        Bundle b = new Bundle();
        b.putSerializable(IntentTag.TAG_TEAM_BEAN, currentTeam);
        mInvitePersonFragment.setArguments(b);
        mFragmentManager.beginTransaction().add(R.id.container, mInvitePersonFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
