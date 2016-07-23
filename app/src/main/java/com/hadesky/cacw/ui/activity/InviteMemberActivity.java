package com.hadesky.cacw.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.fragment.InvitePersonFragment;
import com.hadesky.cacw.ui.fragment.SearchFragment;
import com.hadesky.cacw.ui.widget.SearchView.SearchView;

import java.util.ArrayList;
import java.util.List;

public class InviteMemberActivity extends BaseActivity implements InvitePersonFragment.OnInviteListener, SearchFragment.OnFragmentLoadingListener{

    private SearchView mSearchView;

    private FragmentManager mFragmentManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_member;
    }

    @Override
    public void initView() {
        mSearchView = (SearchView) findViewById(R.id.searchview);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void setupView() {
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
                }
                loadSearchPersonFragment(s);
            }
        });
    }

    private void loadSearchPersonFragment(String s) {
        InvitePersonFragment fragment = (InvitePersonFragment) getPersonFragment();
        if (fragment == null) {
            fragment = InvitePersonFragment.newInstance(InvitePersonFragment.class, s);
            mFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        } else {
            fragment.updateSearchKey(s);
        }
    }

    private Fragment getPersonFragment() {
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

    }
}
