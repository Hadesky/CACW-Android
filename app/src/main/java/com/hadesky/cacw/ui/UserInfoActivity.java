package com.hadesky.cacw.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.fragment.MyTaskFragment;
import com.hadesky.cacw.ui.fragment.UserInfoFragment;

public class UserInfoActivity extends BaseActivity
{
    private TextView usernameView;
    @Override
    public int getLayoutId()
    {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        usernameView = (TextView) findViewById(R.id.tv_username);
        //记得改
        usernameView.setText("蚂蚁测试员");


    }

    @Override
    public void setupView()
    {
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
