package com.hadesky.cacw.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.config.SessionManagement;
import com.hadesky.cacw.ui.MainActivity;

import java.io.ByteArrayInputStream;

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
        MyApp app = (MyApp) getActivity().getApplication();
        userName.setText(getUserName(app));
        userImageView.setImageBitmap(getUserAvatar(app));
    }

    /**
     * 在Session中获取用户名
     * @return 用户名
     */
    private String getUserName(MyApp app) {
        if (app != null) {
            return app.getSession().getUserDetails().get(SessionManagement.KEY_NAME);
        }
        return "";
    }

    /**
     * 在Session里获取头像
     * @param app
     * @return
     */
    private Bitmap getUserAvatar(MyApp app) {
        if (app != null) {
            String byteString = app.getSession().getUserDetails().get(SessionManagement.KEY_AVATAR);
            byte[] byteArray= Base64.decode(byteString, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);

            return BitmapFactory.decodeStream(byteArrayInputStream);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
            ((MainActivity) getActivity()).setAppBarLayoutVisiable(true);
    }

}
