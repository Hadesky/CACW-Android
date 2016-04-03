package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.Toolbar;

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
        //findViewById(R.id.layout_about).setOnClickListener(this);


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

//    @Override
//    public void onClick(View v) {
//        final int id = v.getId();
//        switch (id) {
//            case R.id.layout_about:
//                startActivity(new Intent(context, AboutActivity.class));
//                break;
//            default:
//                break;
//        }
//    }
}
