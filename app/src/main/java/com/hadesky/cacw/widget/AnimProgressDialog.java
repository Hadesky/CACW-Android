package com.hadesky.cacw.widget;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hadesky.cacw.R;

/**
 * Created by 45517 on 2015/10/23.
 */
public class AnimProgressDialog extends Dialog {

    private TextView titleView;
    private String title;

    private ColorfulAnimView animView;

    public AnimProgressDialog(Context context) {
        this(context, true, null);
    }

    public AnimProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context, cancelable, cancelListener, "载入中...");
    }

    public AnimProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener, String title) {
        super(context, cancelable, cancelListener);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题栏

        setContentView(R.layout.dialog_anim_progress);

        titleView = (TextView) findViewById(R.id.tv_title);
        if (!title.isEmpty()) {
            titleView.setText(title);
        }
        animView = (ColorfulAnimView) findViewById(R.id.view_anim);
        titleView.post(new Runnable() {
            @Override
            public void run() {
                animView.startAnim();
            }
        });
    }

}

