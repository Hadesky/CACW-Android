package com.hadesky.cacw.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.fragment.MyTaskFragment;

public class TaskListActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_task_list;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setupView() {
        //ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String status = getIntent().getStringExtra(IntentTag.TAG_TASK_STATUS);
        if (toolbar != null) {
            toolbar.setTitle(status);
        }
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new MyTaskFragment();
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
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
}
