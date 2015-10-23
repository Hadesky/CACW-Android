package com.hadesky.cacw.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.fragment.UserInfoFragment;

public class MyInfoActivity extends BaseActivity {
    private TextView usernameView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    public void initView() {
        usernameView = (TextView) findViewById(R.id.tv_username);
        //记得改
        usernameView.setText("我的资料");
    }

    @Override
    public void setupView() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new UserInfoFragment();
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
