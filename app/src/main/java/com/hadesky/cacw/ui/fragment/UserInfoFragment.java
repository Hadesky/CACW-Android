package com.hadesky.cacw.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.ImageActivity;
import com.hadesky.cacw.ui.widget.PullToZoomBase;
import com.hadesky.cacw.ui.widget.PullToZoomScrollViewEx;

/**
 *
 * Created by 45517 on 2015/10/23.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "UserInfoFragment";
    private PullToZoomScrollViewEx pullToZoomScrollView;
    private TextView mNickNameView, mSummaryView, mPhoneView, mEmailView, mAddressView, mShortPhoneView;
    private ImageView mSexView, mZoomView;
    private SimpleDraweeView mAvatarView;
    private UserBean mUserBean;
    private ImageButton mCallButton,mSMSButton,mEmailButton, mNavigateButton, mShortPhoneButton;
    private FrameLayout mPhoneLayout;
    private RelativeLayout mEmailLayout;
    private RelativeLayout mAddressLayout;

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
        mShortPhoneView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_short_phone);
        mEmailView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_email);
        mAddressView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_address);
        mSexView = (ImageView) pullToZoomScrollView.findViewById(R.id.iv_sex);
        mAvatarView = (SimpleDraweeView) pullToZoomScrollView.findViewById(R.id.iv_avatar);
        mZoomView = (SimpleDraweeView) pullToZoomScrollView.findViewById(R.id.iv_zoom);

        mUserBean = (UserBean) getArguments().getSerializable(IntentTag.TAG_USER_BEAN);

        mCallButton = (ImageButton) pullToZoomScrollView.findViewById(R.id.bt_call);
        mSMSButton = (ImageButton) pullToZoomScrollView.findViewById(R.id.bt_sms);
        mEmailButton = (ImageButton) pullToZoomScrollView.findViewById(R.id.bt_email);
        mNavigateButton = (ImageButton) pullToZoomScrollView.findViewById(R.id.bt_navigate);
        mShortPhoneButton = (ImageButton) pullToZoomScrollView.findViewById(R.id.bt_call_short);

        mPhoneLayout = (FrameLayout) pullToZoomScrollView.findViewById(R.id.layout_phone);
        mEmailLayout = (RelativeLayout) pullToZoomScrollView.findViewById(R.id.layout_email);
        mAddressLayout = (RelativeLayout) pullToZoomScrollView.findViewById(R.id.layout_address);

        Uri uri = Uri.parse("http://www.dujin.org/sys/bing/1366.php");
        mZoomView.setImageURI(uri);

        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void setupViews(Bundle bundle) {
        if (mOnPullZoomListener != null) {
            pullToZoomScrollView.setOnPullZoomListener(mOnPullZoomListener);
        }
        mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAvatarView.getTag() != null) {
                    Intent intent = new Intent(getContext(), ImageActivity.class);
                    intent.putExtra("url", (String) mAvatarView.getTag());
                    startActivity(intent);
                }
            }
        });
    }

    private void loadUserInfo() {
//        if (mUserBean != null) {
//            if (mUserBean.getObjectId().equals(MyApp.getCurrentId().getObjectId())) {
//                //是当前用户，不联网查询
//                updateData(MyApp.getCurrentId());
//                hideAllActionButton();
//                return;
//            }
//            updateData(mUserBean);
//            setupActionButtonListener(mUserBean);
//        }
    }

    private void setupActionButtonListener(UserBean userBean) {
        mCallButton.setOnClickListener(this);
        mShortPhoneButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mNavigateButton.setOnClickListener(this);
        mSMSButton.setOnClickListener(this);
    }


    private void updateData(UserBean bean) {
//        if (bean != null) {
//            mNickNameView.setText(bean.getNickName());
//            showOrHide(mEmailView, mEmailLayout, bean.getEmail());
//            showOrHide(mPhoneView, mPhoneLayout, bean.getMobilePhoneNumber());
//            showOrHide(mAddressView, mAddressLayout, bean.getAddress());
//            if (bean.getShortNumber() != null && !bean.getShortNumber().isEmpty()) {
//                mShortPhoneView.setText(StringUtils.roundWithBrackets(bean.getShortNumber()));
//                mShortPhoneButton.setVisibility(View.VISIBLE);
//            } else {
//                mShortPhoneButton.setVisibility(View.GONE);
//                mShortPhoneView.setVisibility(View.GONE);
//            }
//            mSummaryView.setText(bean.getSummary());
//            if (bean.getSex() != null) {
//                mSexView.setImageLevel(bean.getSex());
//            }
//            if (bean.getUserAvatar() != null) {
//                mAvatarView.setImageURI(bean.getUserAvatar().getUrl());
//                mAvatarView.setTag(bean.getUserAvatar().getUrl());//将url作为tag放在iv里面
//            }
//        }
    }

    private void showOrHide(TextView view, ViewGroup layout, String data) {
        if (view != null) {
            if (data != null) {
                view.setText(data);
            } else {
                layout.setVisibility(View.GONE);
            }
        }
    }

    private void hideAllActionButton() {
        mCallButton.setVisibility(View.GONE);
        mSMSButton.setVisibility(View.GONE);
        mEmailButton.setVisibility(View.GONE);
        mNavigateButton.setVisibility(View.GONE);
        mShortPhoneButton.setVisibility(View.GONE);
    }

    public void setOnPullZoomListener(PullToZoomBase.OnPullZoomListener listener) {
        mOnPullZoomListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_call:
                String uri = "tel:" + mPhoneView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "权限不足，请检查是否赋予了正确的权限", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getContext().startActivity(intent);
                }
                break;
            case R.id.bt_call_short:
                uri = "tel:" + mShortPhoneView.getText().toString();
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "权限不足，请检查是否赋予了正确的权限", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getContext().startActivity(intent);
                }
                break;
            case R.id.bt_email:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmailView.getText().toString()});
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.bt_navigate:
                String uriString = String.format("geo:0,0?q=%s", mAddressView.getText());
                Uri mUri = Uri.parse(uriString);
                Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
                if (mIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(mIntent);
                }
                break;
            case R.id.bt_sms:
                uri = "sms:" + mPhoneView.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                getContext().startActivity(intent);
                break;
        }
    }
}
