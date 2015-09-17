package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.config.SessionManagement;
import com.hadesky.cacw.ui.MainActivity;

/**
 * MeFragment
 * Created by Bright Van on 2015/9/7/007.
 */

public class MeFragment extends BaseFragment {
    private ImageView userImageView;
    private TextView userName;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews(View view) {
        userImageView = (ImageView) view.findViewById(R.id.iv_Me_UserImg);
        userName = (TextView) view.findViewById(R.id.tv_Me_UserName);
    }

    @Override
    protected void setupViews(Bundle bundle) {
        userName.setText(getUserName());
    }

    /**
     * 在Session中获取用户名
     * @return 用户名
     */
    private String getUserName() {
        final MyApp app = (MyApp) getActivity().getApplication();
        return app.getSession().getUserDetails().get(SessionManagement.KEY_NAME);
    }

    @Override
    public void onResume() {
        super.onResume();
            ((MainActivity) getActivity()).setAppBarLayoutVisiable(true);
    }

}
