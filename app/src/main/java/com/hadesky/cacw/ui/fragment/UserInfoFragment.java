package com.hadesky.cacw.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.UserInfoPresenter;
import com.hadesky.cacw.presenter.UserInfoPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.ImageActivity;
import com.hadesky.cacw.ui.view.UserInfoView;
import com.hadesky.cacw.ui.widget.PullToZoomBase;
import com.hadesky.cacw.ui.widget.PullToZoomScrollViewEx;
import com.hadesky.cacw.util.StringUtils;

/**
 * 我的个人资料界面
 * Created by 45517 on 2015/10/23.
 */
public class UserInfoFragment extends BaseFragment implements UserInfoView, View.OnClickListener
{
    private static final String TAG = "UserInfoFragment";
    private PullToZoomScrollViewEx pullToZoomScrollView;
    private TextView mNickNameView, mSummaryView, mPhoneView, mEmailView, mAddressView, mShortPhoneView;
    private ImageView mSexView, mZoomView;
    private SimpleDraweeView mAvatarView;
    private UserBean mUserBean;
    private ImageButton mCallButton, mSMSButton, mEmailButton, mNavigateButton, mShortPhoneButton;
    private FrameLayout mPhoneLayout;
    private RelativeLayout mEmailLayout;
    private RelativeLayout mAddressLayout;
    private UserInfoPresenter mPresenter;
    private PullToZoomBase.OnPullZoomListener mOnPullZoomListener;
    private float mZoomOffset;
    private float mRefreshOffset;

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initViews(View view)
    {
        mRefreshOffset = getResources().getDisplayMetrics().density * 80;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void setupViews(Bundle bundle)
    {
        if (mOnPullZoomListener != null)
        {
            //这里的mOnPullZoomListener 在UserInfoActivity被设置
            pullToZoomScrollView.addOnPullZoomListeners(mOnPullZoomListener);
        }
        pullToZoomScrollView.addOnPullZoomListeners(new PullToZoomBase.OnPullZoomListener()
        {
            @Override
            public void onPullZooming(int newScrollValue)
            {
                mZoomOffset = -newScrollValue;
            }

            @Override
            public void onPullZoomEnd()
            {

                //Log.d("tag", "offset " + mZoomOffset + " over " + mRefreshOffset);
                if (mZoomOffset > mRefreshOffset)
                    mPresenter.loadUserInfo();
                mZoomOffset = 0;
            }
        });


        mAvatarView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mAvatarView.getTag() != null)
                {
                    Intent intent = new Intent(getContext(), ImageActivity.class);
                    intent.putExtra("url", (String) mAvatarView.getTag());
                    startActivity(intent);
                }
            }
        });
        //获取个人资料
        mUserBean = getArguments().getParcelable(IntentTag.TAG_USER_BEAN);

        if (mUserBean == null)
        {
            showToast("参数错误:找不到用户");
            return;
        }

        mPresenter = new UserInfoPresenterImpl(this, mUserBean);

        //如果当前用户是自己，先不联网
        if (mUserBean.getId() == MyApp.getCurrentUser().getId())
        {
            showUserInfo(mUserBean);
        }else
            mPresenter.loadUserInfo();
    }


    @Override
    public void showUserInfo(UserBean user)
    {
        if (user != null)
        {
            if (user.getId() == MyApp.getCurrentUser().getId())
            {
                //如果当前用户是自己，则不用加载一些按钮
                updateView(user);
                hideAllActionButton();
                return;
            }
            updateView(user);
            setupActionButtonListener(user);
        }
    }

    private void setupActionButtonListener(UserBean userBean)
    {
        mCallButton.setOnClickListener(this);
        mShortPhoneButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mNavigateButton.setOnClickListener(this);
        mSMSButton.setOnClickListener(this);
    }


    private void updateView(UserBean bean)
    {
        if (bean != null)
        {
            mNickNameView.setText(bean.getNickName());
            showOrHide(mEmailView, mEmailLayout, bean.getEmail());
            showOrHide(mPhoneView, mPhoneLayout, bean.getMobilePhone());
            showOrHide(mAddressView, mAddressLayout, bean.getAddress());
            if (bean.getShortNumber() != null && !bean.getShortNumber().isEmpty())
            {
                mShortPhoneView.setText(StringUtils.roundWithBrackets(bean.getShortNumber()));
                mShortPhoneButton.setVisibility(View.VISIBLE);
            } else
            {
                mShortPhoneButton.setVisibility(View.GONE);
                mShortPhoneView.setVisibility(View.GONE);
            }
            mSummaryView.setText(bean.getSummary());
            mSexView.setImageLevel(bean.getSex());

            if (bean.getAvatarUrl() != null)
            {
                mAvatarView.setImageURI(bean.getAvatarUrl());
                mAvatarView.setTag(bean.getAvatarUrl());//将url作为tag放在iv里面
            }
        }
    }

    private void showOrHide(TextView view, ViewGroup layout, String data)
    {
        if (view != null)
        {
            if (data != null)
            {
                layout.setVisibility(View.VISIBLE);
                view.setText(data);
            } else
            {
                layout.setVisibility(View.GONE);
            }
        }
    }

    private void hideAllActionButton()
    {
        mCallButton.setVisibility(View.GONE);
        mSMSButton.setVisibility(View.GONE);
        mEmailButton.setVisibility(View.GONE);
        mNavigateButton.setVisibility(View.GONE);
        mShortPhoneButton.setVisibility(View.GONE);
    }

    public void setOnPullZoomListener(PullToZoomBase.OnPullZoomListener listener)
    {
        mOnPullZoomListener = listener;
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_call:
                String uri = "tel:" + mPhoneView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                getContext().startActivity(intent);
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
//                {
//                    Toast.makeText(getContext(), "权限不足，请检查是否赋予了正确的权限", Toast.LENGTH_SHORT).show();
//                    return;
//                } else
//                {
//                    getContext().startActivity(intent);
//                }
                break;
            case R.id.bt_call_short:
                uri = "tel:" + mShortPhoneView.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                getContext().startActivity(intent);
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
//                {
//                    Toast.makeText(getContext(), "权限不足，请检查是否赋予了正确的权限", Toast.LENGTH_SHORT).show();
//                    return;
//                } else
//                {
//                    getContext().startActivity(intent);
//                }
                break;
            case R.id.bt_email:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmailView.getText().toString()});
                if (intent.resolveActivity(getContext().getPackageManager()) != null)
                {
                    startActivity(intent);
                }
                break;
            case R.id.bt_navigate:
                String uriString = String.format("geo:0,0?q=%s", mAddressView.getText());
                Uri mUri = Uri.parse(uriString);
                Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
                if (mIntent.resolveActivity(getContext().getPackageManager()) != null)
                {
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


    @Override
    public void showProgress()
    {

    }

    @Override
    public void hideProgress()
    {

    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }
}
