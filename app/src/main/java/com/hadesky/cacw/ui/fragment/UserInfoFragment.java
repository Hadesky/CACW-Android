package com.hadesky.cacw.ui.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hadesky.cacw.R;
import com.hadesky.cacw.widget.PullToZoomScrollViewEx;

/**
 * Created by 45517 on 2015/10/23.
 */
public class UserInfoFragment extends BaseFragment {
    private PullToZoomScrollViewEx pullToZoomScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initViews(View view) {
        pullToZoomScrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.zoom_scrollView);
        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(0X33000000);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void setupViews(Bundle bundle) {
    }
}
