package com.hadesky.cacw.ui.fragment;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.activity.FinishedTaskActivity;
import com.hadesky.cacw.ui.activity.MessageListActivity;
import com.hadesky.cacw.ui.activity.MyInfoActivity;
import com.hadesky.cacw.ui.activity.MyTeamActivity;
import com.hadesky.cacw.ui.activity.SettingActivity;
import com.hadesky.cacw.util.ImageUtils;

/**
 * MeFragment
 * Created by Bright Van on 2015/9/7/007.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private SimpleDraweeView mAvatarImageView;
    private TextView userName;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews(View view) {
        mAvatarImageView = (SimpleDraweeView) view.findViewById(R.id.iv_avatar);
        userName = (TextView) view.findViewById(R.id.tv_me_username);
        //暂时使用这种方式设置listener
        view.findViewById(R.id.layout_setting).setOnClickListener(this);
        view.findViewById(R.id.layout_complete).setOnClickListener(this);
        view.findViewById(R.id.layout_memo).setOnClickListener(this);
        view.findViewById(R.id.layout_myinfo).setOnClickListener(this);
        view.findViewById(R.id.layout_remind).setOnClickListener(this);
        view.findViewById(R.id.layout_my_team).setOnClickListener(this);
    }

    @Override
    protected void setupViews(Bundle bundle) {

    }

    private void loadUserInfo() {
        userName.setText(getNickName());
        mAvatarImageView.setImageURI(getAvatarUrl());
    }

    private String getAvatarUrl() {
        UserBean bean = MyApp.getCurrentUser();
        if (bean != null && bean.getUserAvatar() != null) {
            return bean.getUserAvatar().getUrl();
        }
        return null;
    }

    /**
     * 在Session中获取昵称
     * @return 用户名
     */
    private String getNickName() {
        String nickName = MyApp.getCurrentUser().getNickName();
        if (nickName == null) {
            return "蚂蚁";
        }
        return nickName;
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.layout_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.layout_myinfo:
                //这里不需要传入任何信息
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_complete:
                intent = new Intent(getContext(), FinishedTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_remind:
                intent = new Intent(getContext(), MessageListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_memo:
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
                builder.setTicker("HelloWorld").setContentTitle("标题").setContentText("正文").setSubText("子标题")
                        .setNumber(3).setAutoCancel(true).setSmallIcon(R.mipmap.icon).setDefaults(NotificationCompat.DEFAULT_ALL);
                ImageUtils.getBitmapFromFresco(getAvatarUrl(), getContext(), new ImageUtils.Callback() {
                    @Override
                    public void receiveBitmap(Bitmap bitmap) {
                        builder.setLargeIcon(bitmap);
                        NotificationManager manager = MyApp.getNotificationManager();
                        if (manager != null) {
                            manager.notify(1, builder.build());
                        }
                    }
                });
                break;
            case R.id.layout_my_team:
                Intent intent1 = new Intent(getContext(), MyTeamActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }
}
