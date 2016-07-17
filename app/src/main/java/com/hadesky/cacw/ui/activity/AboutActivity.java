package com.hadesky.cacw.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hadesky.cacw.R;

public class AboutActivity extends BaseActivity {
    private TextView versionView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        versionView = (TextView) findViewById(R.id.tv_app_version);
    }

    @Override
    public void setupView() {
        versionView.setText(getVersion());
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     　　* 获取版本号
     　　* @return 当前应用的版本号
     　　*/
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
            } catch (Exception e) {
            e.printStackTrace();
            return "error";
            }
        }
}
