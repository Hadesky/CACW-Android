package com.hadesky.cacw.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.task.GetUserInfoTask;
import com.hadesky.cacw.ui.widget.PullToZoomBase;
import com.hadesky.cacw.ui.widget.PullToZoomScrollViewEx;
import com.hadesky.cacw.util.ImageLoader;

/**
 *
 * Created by 45517 on 2015/10/23.
 */
public class UserInfoFragment extends BaseFragment {
    private static final String TAG = "UserInfoFragment";
    private PullToZoomScrollViewEx pullToZoomScrollView;
    private TextView userNameView,descView,idView,phoneView,emailView, addressView;
    private ImageView sexView,avatarView,zoomView;

    private long userId;

    private PullToZoomBase.OnPullZoomListener mOnPullZoomListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initViews(View view) {
        pullToZoomScrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.zoom_scrollView);
        userNameView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_username);
        descView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_desc);
        idView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_id);
        phoneView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_phone);
        emailView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_email);
        addressView = (TextView) pullToZoomScrollView.findViewById(R.id.tv_address);
        sexView = (ImageView) pullToZoomScrollView.findViewById(R.id.iv_sex);
        avatarView = (ImageView) pullToZoomScrollView.findViewById(R.id.iv_avatar);
        zoomView = (ImageView) pullToZoomScrollView.findViewById(R.id.iv_zoom);

        userId = getArguments().getLong(IntentTag.TAG_USER_ID, 0);

        Window window = getActivity().getWindow();

        //test
        ImageLoader loader = ImageLoader.build(getContext());
        loader.bindBitmap("http://www.dujin.org/sys/bing/1366.php", zoomView,1024,768);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    @Override
    protected void setupViews(Bundle bundle) {
        // TODO: 2016/7/10 0010 逻辑不应该写在这
        GetUserInfoTask task = new GetUserInfoTask(getActivity(), new GetUserInfoTask.Callback() {
            @Override
            public void onSuccess(UserBean bean) {
                updateData(bean);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
        task.execute(userId);
        if (mOnPullZoomListener != null) {
            pullToZoomScrollView.setOnPullZoomListener(mOnPullZoomListener);
        }
    }


    private void updateData(UserBean bean) {
        if (bean != null) {
            userNameView.setText(bean.getNickName());
            idView.setText(bean.getObjectId());
        }
    }

    public void setOnPullZoomListener(PullToZoomBase.OnPullZoomListener listener) {
        mOnPullZoomListener = listener;
    }
}
