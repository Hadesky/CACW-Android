package com.hadesky.cacw.widget;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;

/**
 * Created by 45517 on 2015/10/23.
 */
public class AnimProgressDialog extends Dialog {
    private ValueAnimator animator1, animator2, animatorUpDown1,animatorUpDown2,animatorUpDown3, animatorUpDown4;
    private ObjectAnimator animatorAlphaIn1,animatorAlphaIn2, animatorAlphaIn3;
    private AnimatorSet animatorSet;
    private ImageView ball1,ball2,ball3, ball4;
    private Matrix matrix;

    private TextView titleView;
    private String title;


    public AnimProgressDialog(Context context) {
        this(context, true, null);
    }

    public AnimProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context, cancelable, cancelListener, "载入中...");
    }

    public AnimProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener, String title) {
        super(context, cancelable, cancelListener);
        matrix = new Matrix();
        this.title = title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_anim_process);

        titleView = (TextView) findViewById(R.id.tv_title);
        if (!title.isEmpty()) {
            titleView.setText(title);
        }

        ball1 = (ImageView) findViewById(R.id.iv_ball1);
        ball2 = (ImageView) findViewById(R.id.iv_ball2);
        ball3 = (ImageView) findViewById(R.id.iv_ball3);
        ball4 = (ImageView) findViewById(R.id.iv_ball4);

        ball4.post(new Runnable() {
            @Override
            public void run() {
                createAnimator();
                setupListener();
                animatorSet.start();
            }
        });
    }

    private void setupListener() {
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                matrix.set(ball4.getImageMatrix());
                matrix.setScale((Float) animation.getAnimatedValue(), (Float) animation.getAnimatedValue(), ball4.getPivotX(), ball4.getPivotY());
                ball4.setImageMatrix(matrix);
            }
        });
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball4.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        animatorUpDown1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball1.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        animatorUpDown2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball2.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        animatorUpDown3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball3.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        animatorUpDown4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball4.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    private void createAnimator() {

        animator1 = ValueAnimator.ofFloat(0.8f, 0.3f);
        animator1.setDuration(600);
        animator1.setInterpolator(new BounceInterpolator());

        animator2 = ValueAnimator.ofFloat(0f, -getTranX());
        animator2.setDuration(600);
        animator2.setInterpolator(new AccelerateInterpolator());

        //淡出动画
        animatorAlphaIn1 = ObjectAnimator.ofObject(ball1, "alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn1.setDuration(200);

        animatorUpDown1 = ValueAnimator.ofFloat(0f, 50f);
        animatorUpDown1.setDuration(2000);
        animatorUpDown1.setInterpolator(new CycleInterpolator(1));
        animatorUpDown1.setRepeatCount(ValueAnimator.INFINITE);

        animatorAlphaIn2 = ObjectAnimator.ofObject(ball2, "alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn2.setDuration(200);

        animatorUpDown2 = animatorUpDown1.clone();

        animatorAlphaIn3 = ObjectAnimator.ofObject(ball3, "alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn3.setDuration(200);
        animatorUpDown3 = animatorUpDown1.clone();

        animatorUpDown4 = animatorUpDown1.clone();

        animatorSet = new AnimatorSet();
        animatorSet.play(animator1).before(animator2);//600
        animatorSet.play(animator2).with(animatorUpDown3);//600
        animatorSet.play(animatorUpDown2).after(800);
        animatorSet.play(animatorUpDown1).after(1000);
        animatorSet.play(animatorUpDown4).after(1200);
        animatorSet.play(animatorAlphaIn3).after(600);
        animatorSet.play(animatorAlphaIn2).after(800);
        animatorSet.play(animatorAlphaIn1).after(1000);

        animatorSet.setStartDelay(200);
    }

    private float getTranX() {
        final float width = ball1.getWidth();
        final float perX = width + getContext().getResources().getDimension(R.dimen.spacing_small);
        return 3f * perX;
    }


    public void setMessage(String title) {
        this.title = title;
        if (!title.isEmpty() && titleView != null) {
            titleView.setText(title);
        }
    }
}

