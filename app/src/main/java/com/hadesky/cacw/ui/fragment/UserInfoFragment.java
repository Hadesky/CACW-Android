package com.hadesky.cacw.ui.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.widget.PullToZoomBase;
import com.hadesky.cacw.ui.widget.PullToZoomScrollViewEx;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 *
 * Created by 45517 on 2015/10/23.
 */
public class UserInfoFragment extends BaseFragment {
    private static final String TAG = "UserInfoFragment";
    private PullToZoomScrollViewEx pullToZoomScrollView;
    private TextView mNickNameView, mSummaryView, mPhoneView, mEmailView, mAddressView;
    private ImageView mSexView, mZoomView;
    private SimpleDraweeView mAvatarView;
    private String userId;

    private PullToZoomBase.OnPullZoomListener mOnPullZoomListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initViews(View view) {
        pullToZoomScrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.zoom_scrollView);
        mNickNameView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_username);
        mSummaryView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_summary);
        mPhoneView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_phone);
        mEmailView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_email);
        mAddressView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_address);
        mSexView = (ImageView) pullToZoomScrollView.findViewById(R.id.iv_sex);
        mAvatarView = (SimpleDraweeView) pullToZoomScrollView.findViewById(R.id.iv_avatar);
        mZoomView = (SimpleDraweeView) pullToZoomScrollView.findViewById(R.id.iv_zoom);

        userId = getArguments().getString(IntentTag.TAG_USER_ID);

        Window window = getActivity().getWindow();

//        //test
//        ImageLoader loader = ImageLoader.build(getContext());
//        loader.bindBitmap("http://www.dujin.org/sys/bing/1366.php", mZoomView,1024,768);
        Uri uri = Uri.parse("http://www.dujin.org/sys/bing/1366.php");
        mZoomView.setImageURI(uri);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    @Override
    protected void setupViews(Bundle bundle) {
        if (mOnPullZoomListener != null) {
            pullToZoomScrollView.setOnPullZoomListener(mOnPullZoomListener);
        }
    }

    private void loadUserInfo() {
        if (userId != null) {
            if (userId.equals(MyApp.getCurrentUser().getObjectId())) {
                //是当前用户，不联网查询
                updateData(MyApp.getCurrentUser());
                return;
            }
            BmobQuery<UserBean> query = new BmobQuery<>();
            query.getObject(userId, new QueryListener<UserBean>() {
                @Override
                public void done(UserBean userBean, BmobException e) {
                    if (e == null) {
                        updateData(userBean);
                    } else {
                        showToast("更新失败");
                    }
                }
            });
        }
    }


    private void updateData(UserBean bean) {
        if (bean != null) {
            if (bean.getNickName()==null) {
                mNickNameView.setText(R.string.default_user_name);
            } else {
                mNickNameView.setText(bean.getNickName());
            }
            mEmailView.setText(bean.getEmail());
            mPhoneView.setText(bean.getMobilePhoneNumber());
            mSummaryView.setText(bean.getSummary());
            mAddressView.setText(bean.getAddress());
            if (bean.getSex() != null) {
                mSexView.setImageLevel(bean.getSex());
            }
            if (bean.getUserAvatar() != null) {
                mAvatarView.setImageURI(bean.getUserAvatar().getUrl());
            }
        }
    }

    public void setOnPullZoomListener(PullToZoomBase.OnPullZoomListener listener) {
        mOnPullZoomListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }
}
