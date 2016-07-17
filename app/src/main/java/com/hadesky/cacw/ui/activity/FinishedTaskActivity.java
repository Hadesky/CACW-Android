package com.hadesky.cacw.ui.activity;

import android.os.Bundle;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.fragment.MyTaskFragment;

public class FinishedTaskActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_finished_task;
    }

    @Override
    public void initView() {

        MyTaskFragment fragment = new MyTaskFragment();
        Bundle b = new Bundle();
        b.putBoolean("isFinished",true);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }

    @Override
    public void setupView() {

    }
}
