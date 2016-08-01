package com.hadesky.cacw.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.JPush.JPushSender;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.FinishedTaskActivity;
import com.hadesky.cacw.ui.activity.MessageListActivity;
import com.hadesky.cacw.ui.activity.MyInfoActivity;
import com.hadesky.cacw.ui.activity.MyTeamActivity;
import com.hadesky.cacw.ui.activity.SettingActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * MeFragment
 * Created by Bright Van on 2015/9/7/007.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private SimpleDraweeView mAvatarImageView;
    private TextView userName;
    private View mCircle;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews(View view) {

        mCircle = view.findViewById(R.id.view_red_circle);
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

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.e("tag", "MeFragment 收到广播");
                mCircle.setVisibility(View.VISIBLE);
            }
        };
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
                mCircle.setVisibility(View.GONE);
                intent = new Intent(getContext(), MessageListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_memo:
                new Thread(){
                    @Override
                    public void run() {
                        JPushSender sender = new JPushSender.SenderBuilder().addAlias(MyApp.getCurrentUser().
                                getObjectId()).Message("title", "message").build();
                        MyApp.getJPushManager().sendMsg(sender, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });
                    }
                }.start();
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
        IntentFilter filter = new IntentFilter(IntentTag.ACTION_MSG_RECEIVE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }
}
