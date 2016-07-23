package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.InvitePersonPresenter;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.InvitePersonFragment;
import com.hadesky.cacw.ui.fragment.SearchFragment;
import com.hadesky.cacw.ui.widget.SearchView.SearchView;

import java.util.ArrayList;
import java.util.List;

public class InviteMemberActivity extends BaseActivity implements InvitePersonFragment.OnInviteListener, SearchFragment.OnFragmentLoadingListener{

    private SearchView mSearchView;

    private FragmentManager mFragmentManager;

    private InvitePersonFragment mInvitePersonFragment;

    private List<UserBean> mTeamMember;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_member;
    }

    @Override
    public void initView() {
        mSearchView = (SearchView) findViewById(R.id.searchview);
        mFragmentManager = getSupportFragmentManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setupView() {
        mTeamMember = (List<UserBean>) getIntent().getSerializableExtra(IntentTag.TAG_TEAM_MEMBER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mSearchView.setListener(new SearchView.SearchListener() {
            @Override
            public void onTextChange(String text) {

            }

            @Override
            public void onSubmit(String s) {
                if (s.length() == 0) {
                    hideAllFragment();
                } else {
                    loadSearchPersonFragment(s);
                }
            }
        });
        loadSearchPersonFragment(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPresenter();
    }

    private void setupPresenter() {
        InvitePersonPresenter presenter = mInvitePersonFragment.getPresenter();
        if (presenter != null) {
            presenter.setTeamMember(mTeamMember);
            presenter.setOnInviteListener(this);
        }
    }

    private void loadSearchPersonFragment(String s) {
        mInvitePersonFragment = (InvitePersonFragment) getPersonFragment();
        if (mInvitePersonFragment == null) {
            mInvitePersonFragment = InvitePersonFragment.newInstance(InvitePersonFragment.class, s);
            mFragmentManager.beginTransaction()
                    .add(R.id.container, mInvitePersonFragment)
                    .commit();
        } else {
            mInvitePersonFragment.updateSearchKey(s);
        }
    }

    private Fragment getPersonFragment() {
        if (mInvitePersonFragment != null) {
            return mInvitePersonFragment;
        }
        return mFragmentManager.findFragmentById(R.id.container);
    }

    private void hideAllFragment() {
        if (mFragmentManager.getFragments() != null) {
            for (Fragment fragment : mFragmentManager.getFragments()) {
                mFragmentManager.beginTransaction().hide(fragment).commit();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentLoadingStart() {

    }

    @Override
    public void onFragmentLoadingEnd() {

    }

    @Override
    public void onInvite(UserBean user) {
        showToast(user.getNickName());
    }
}
