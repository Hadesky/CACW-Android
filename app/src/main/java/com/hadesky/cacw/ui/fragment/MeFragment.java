package com.hadesky.cacw.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.config.SessionManagement;

import java.io.ByteArrayInputStream;

/**
 * MeFragment
 * Created by Bright Van on 2015/9/7/007.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView userImageView;
    private TextView userName;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews(View view) {
        userImageView = (ImageView) view.findViewById(R.id.iv_me_avatar);
        userName = (TextView) view.findViewById(R.id.tv_me_username);
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
     *
     * @param app
     * @return
     */
    private Bitmap getUserAvatar(MyApp app) {
        if (app != null) {
            String byteString = app.getSession().getUserDetails().get(SessionManagement.KEY_AVATAR);
            if (byteString != null) {
                byte[] byteArray= Base64.decode(byteString, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
                return BitmapFactory.decodeStream(byteArrayInputStream);
            }
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
        }
    }
}
