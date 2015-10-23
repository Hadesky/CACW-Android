package com.hadesky.cacw.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hadesky.cacw.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        findViewById(R.id.layout_about).setOnClickListener(this);
    }

    @Override
    public void setupView() {

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.layout_about:
                startActivity(new Intent(context, AboutActivity.class));
                break;
            default:
                break;
        }
    }
}
