package com.hadesky.cacw.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hadesky.cacw.R;

public class EditMyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_info);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    }
}
