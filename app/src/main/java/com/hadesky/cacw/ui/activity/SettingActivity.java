package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.fragment.SettingFragment;

public class SettingActivity extends BaseActivity{



    Toolbar mToolbar;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        assert getSupportActionBar()!=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .add(R.id.container, new SettingFragment())
                .commit();
    }

    @Override
    public void setupView() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
