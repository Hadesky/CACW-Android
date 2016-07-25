package com.hadesky.cacw.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.UserInfoFragment;
import com.hadesky.cacw.ui.widget.PullToZoomBase;

public class UserInfoActivity extends BaseActivity
{
    private static final String TAG = "UserInfoActivity";
    private Toolbar toolbar;
    private UserBean mUserBean;
    private FloatingActionButton mMessageButton;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mUserBean = (UserBean) getIntent().getSerializableExtra(IntentTag.TAG_USER_BEAN);
        if (mUserBean.equals(MyApp.getCurrentUser())) {
            navigateTo(MyInfoActivity.class, false);
            finish();
        }
        mMessageButton = (FloatingActionButton) findViewById(R.id.bt_message);
    }


    @Override
    public void setupView()
    {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new UserInfoFragment();
            Bundle bundle = new Bundle();

            bundle.putSerializable(IntentTag.TAG_USER_BEAN, mUserBean);

            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        ((UserInfoFragment)fragment).setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                if (newScrollValue < -400) {
                    mMessageButton.hide();
                } else {
                    mMessageButton.show();
                }
            }

            @Override
            public void onPullZoomEnd() {
                mMessageButton.show();
            }
        });

        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mUserBean == null || mUserBean.equals(MyApp.getCurrentUser())) {
                    return;
                }
                Intent i = new Intent(UserInfoActivity.this,ChatActivity.class);
                i.putExtra(IntentTag.TAG_USER_BEAN, mUserBean);
                startActivity(i);
            }
        });


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
}
