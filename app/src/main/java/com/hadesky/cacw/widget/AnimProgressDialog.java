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
    private ValueAnimator animator1, animator2, animatorUpDown1,animatorUpDown2,animatorUpDown3, animatorUpDown4;
    private ObjectAnimator animatorAlphaIn1,animatorAlphaIn2, animatorAlphaIn3;
    private AnimatorSet animatorSet;
    private ImageView ball1,ball2,ball3, ball4;
    private Matrix matrix;

    private double speed_factor = 0.6;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题栏

        setContentView(R.layout.dialog_anim_progress);

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
                //计算ball4的位置
                setupBall4();

                createAnimator();
                setupListener();
                animatorSet.start();
            }
        });
    }

    private void setupBall4() {
        ball4.setPivotX(ball3.getX() - ball2.getX() + ball3.getWidth() / 2);
        ball4.setPivotY(ball3.getY() / 2f - ball3.getHeight() / 4);
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
        final int upDownDuration = (int) (2000 * speed_factor);//上下浮动动画时长
        final int preAnimDuration = (int) (600 * speed_factor);//预动画时长，即一开始的圆形缩放和位移动画
        final int alphaInDuration = (int) (200 * speed_factor);//圆形淡出动画时长

        //圆缩放动画
        animator1 = ValueAnimator.ofFloat(1f, 0.25f);
        animator1.setDuration(preAnimDuration);
        animator1.setInterpolator(new OvershootInterpolator());
        //路径动画
        animator2 = ValueAnimator.ofFloat(0f, -getTranX());
        animator2.setDuration(preAnimDuration);
        animator2.setInterpolator(new LinearInterpolator());

        //淡出动画
        animatorAlphaIn1 = ObjectAnimator.ofObject(ball1, "alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn1.setDuration(alphaInDuration);

        animatorUpDown1 = ValueAnimator.ofFloat(0f, 20f);
        animatorUpDown1.setDuration(upDownDuration);
        animatorUpDown1.setInterpolator(new CycleInterpolator(1));
        animatorUpDown1.setRepeatCount(ValueAnimator.INFINITE);

        animatorAlphaIn2 = ObjectAnimator.ofObject(ball2, "alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn2.setDuration(alphaInDuration);

        animatorUpDown2 = animatorUpDown1.clone();

        animatorAlphaIn3 = ObjectAnimator.ofObject(ball3, "alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn3.setDuration(alphaInDuration);
        animatorUpDown3 = animatorUpDown1.clone();

        animatorUpDown4 = animatorUpDown1.clone();

        animatorSet = new AnimatorSet();
        animatorSet.play(animator1).before(animator2);
        animatorSet.play(animator2).with(animatorUpDown3);//播放完预动画，时间为2*preAnimDuration，而animatorUpDown3播放的具体时间是preAnimDuration

        animatorSet.play(animatorUpDown2).after(preAnimDuration + alphaInDuration);//preAnimDuration+alphaInDuration
        animatorSet.play(animatorUpDown1).after(preAnimDuration + (2 * alphaInDuration));
        animatorSet.play(animatorUpDown4).after(preAnimDuration + (3 * alphaInDuration));

        animatorSet.play(animatorAlphaIn3).after(preAnimDuration); //preAnimDuration
        animatorSet.play(animatorAlphaIn2).after(preAnimDuration + alphaInDuration);
        animatorSet.play(animatorAlphaIn1).after(preAnimDuration + (2 * alphaInDuration));

        animatorSet.setStartDelay(200);//以上动画延时200毫秒执行，解决一开始的缩放动画卡顿问题
    }

    private float getTranX() {
        final float width = ball1.getWidth();
        final float perX = width + getContext().getResources().getDimension(R.dimen.spacing_more_small);
        return 3f * perX;
    }


    public void setMessage(String title) {
        this.title = title;
        if (!title.isEmpty() && titleView != null) {
            titleView.setText(title);
        }
    }

    @Override
    public void cancel() {
        if (isShowing() && animator1 != null) {
            removeAllUpdateListeners();
        }
        super.cancel();
    }

    private void removeAllUpdateListeners() {
        animator1.removeAllUpdateListeners();
        animator2.removeAllUpdateListeners();
        animatorUpDown1.removeAllUpdateListeners();
        animatorUpDown2.removeAllUpdateListeners();
        animatorUpDown3.removeAllUpdateListeners();
        animatorUpDown4.removeAllUpdateListeners();
    }
}

