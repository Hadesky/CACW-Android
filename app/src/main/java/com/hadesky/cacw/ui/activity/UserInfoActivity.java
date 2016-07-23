package com.hadesky.cacw.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
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
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.UserInfoFragment;
import com.hadesky.cacw.ui.widget.PullToZoomBase;

public class UserInfoActivity extends BaseActivity
{
    private static final String TAG = "UserInfoActivity";
    private Toolbar toolbar;
    private UserBean mUserBean;
    private LinearLayout mMenuLayout;
    private int lastY;
    private ObjectAnimator mMenuAnimator;
    private Button mBtnSms;

    private boolean isMenuHided = false;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        mMenuLayout = (LinearLayout) findViewById(R.id.layout_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mUserBean = (UserBean) getIntent().getSerializableExtra(IntentTag.TAG_USER_BEAN);
        mBtnSms = (Button) findViewById(R.id.btn_sms);
        mMenuAnimator = ObjectAnimator.ofFloat(mMenuLayout, "TranslationY", 0, 1000);
        mMenuAnimator.setDuration(500);
    }


    /**
     * 显示下面的图标菜单
     */
    private void showMenuLayout() {
        if (isMenuHided) {
            isMenuHided = false;
            mMenuAnimator.reverse();
        }
    }

    /**
     * 隐藏菜单
     */
    private void hideMenuLayout() {
        if (!isMenuHided) {
            isMenuHided = true;
            mMenuAnimator.start();
        }
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
                    hideMenuLayout();
                } else {
                    showMenuLayout();
                }
            }

            @Override
            public void onPullZoomEnd() {
                showMenuLayout();
            }
        });

        mBtnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
