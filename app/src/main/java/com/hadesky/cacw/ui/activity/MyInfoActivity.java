package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.UserInfoFragment;

public class MyInfoActivity extends BaseActivity {
    private Toolbar toolbar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    @Override
    public void setupView() {
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
            bundle.putString(IntentTag.TAG_USER_ID, MyApp.getCurrentUser().getObjectId());
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, EditMyInfoActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return false;
    }
}
