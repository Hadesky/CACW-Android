package com.hadesky.cacw.widget;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;


import com.hadesky.cacw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 45517 on 2015/11/11.
 */
public class ColorfulAnimView extends View{
    private AnimatorSet animatorSet;
    //当前layout的长宽
    private int mWidth, mHeight;
    //动画的速度因子，范围在0f到1f之间
    private float speedFactor;
    //代表四个圆
    private RoundBean scaleRound,round1,round2, round3;
    //小圆和大圆的直径
    private int normalRoundSize,bigRoundSize;
    //储存四个颜色
    private List<Integer> colorList;
    //每个小圆中心的间隔
    private int mPointMargin;
    //

    public ColorfulAnimView(Context context) {
        this(context, null);
    }

    public ColorfulAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.ColorfulAnim);

        speedFactor = a.getFloat(R.styleable.ColorfulAnim_speed_factor, 1f);

        colorList = new ArrayList<>();
        colorList.add(getResources().getColor(android.R.color.holo_red_light));
        colorList.add(getResources().getColor(android.R.color.holo_orange_light));
        colorList.add(getResources().getColor(android.R.color.holo_green_light));
        colorList.add(getResources().getColor(android.R.color.holo_blue_light));//ScaleRound 's Color

        scaleRound = new RoundBean(colorList.get(3));
        round1 = new RoundBean(colorList.get(0));
        round2 = new RoundBean(colorList.get(1));
        round3 = new RoundBean(colorList.get(2));

        post(new Runnable() {
            @Override
            public void run() {
                int f1 = Math.min(mHeight, mWidth) / 2;
                mPointMargin = mWidth / 5;
                bigRoundSize = Math.min(mPointMargin, f1);
                normalRoundSize = bigRoundSize / 2;
                scaleRound.setAttrs(colorList.get(3), mPointMargin * 4, mHeight / 2, bigRoundSize, 1f, 1f);
                round1.setAttrs(colorList.get(0), mPointMargin * 2, mHeight / 2, normalRoundSize, 0f, 1f);
                round2.setAttrs(colorList.get(1), mPointMargin * 3, mHeight / 2, normalRoundSize, 0f, 1f);
                round3.setAttrs(colorList.get(2), mPointMargin * 4, mHeight / 2, normalRoundSize, 0f, 1f);

                createAnimator();
            }
        });


        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }


    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(heightMeasureSpec);//指定的模式，为EXACTLY时明确了长宽，为AT_MOST时，即wrapContent，
        // 需要和指定的大小进行比较，如果是UNSPECIFIED，则像多大就多大
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = px2dip(getContext(), 1000);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        mHeight = result;
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);//指定的模式，为EXACTLY时明确了长宽，为AT_MOST时，即wrapContent，
        // 需要和指定的大小进行比较，如果是UNSPECIFIED，则像多大就多大
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = px2dip(getContext(), 1500);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        mWidth = result;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRound1(canvas);
        drawRound2(canvas);
        drawRound3(canvas);
        drawScaleRound(canvas);
    }

    private void drawRound3(Canvas canvas) {
        canvas.drawCircle(round3.getX(), round3.getY(), round3.getSize() / 2, round3.getPaint());
    }

    private void drawRound2(Canvas canvas) {
        canvas.drawCircle(round2.getX(), round2.getY(), round2.getSize() / 2, round2.getPaint());
    }

    private void drawRound1(Canvas canvas) {
        canvas.drawCircle(round1.getX(), round1.getY(), round1.getSize() / 2, round1.getPaint());
    }

    private void drawScaleRound(Canvas canvas) {
        canvas.drawCircle(scaleRound.getX(), scaleRound.getY(), scaleRound.getSize() / 2, scaleRound.getPaint());
    }

    public void startAnim() {
        animatorSet.start();
    }

    public void setSpeedFactor(float factor) {
        this.speedFactor = factor;
    }


    private void createAnimator() {
        final int upDownDuration = (int) (2000 * speedFactor);//上下浮动动画时长
        final int preAnimDuration = (int) (600 * speedFactor);//预动画时长，即一开始的圆形缩放和位移动画
        final int alphaInDuration = (int) (200 * speedFactor);//圆形淡出动画时长

        //圆缩放动画
        ValueAnimator scaleAnimator = ObjectAnimator.ofObject(this, "ScaleRoundSize", new IntEvaluator(), bigRoundSize, normalRoundSize);
        scaleAnimator.setDuration(preAnimDuration);
        scaleAnimator.setInterpolator(new OvershootInterpolator());
        //路径动画
        ValueAnimator moveAnimator = ObjectAnimator.ofObject(this, "ScaleRoundX", new IntEvaluator(), round3.getX(), round1.getX() - mPointMargin);
        moveAnimator.setDuration(preAnimDuration);
        moveAnimator.setInterpolator(new LinearInterpolator());

        //淡出动画
        ObjectAnimator animatorAlphaIn1 = ObjectAnimator.ofObject(this, "Round1Alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn1.setDuration(alphaInDuration);

        ObjectAnimator animatorUpDown1 = ObjectAnimator.ofObject(this, "Round1Y", new IntEvaluator(),
                mHeight / 2, mHeight / 2 + normalRoundSize);
        animatorUpDown1.setDuration(upDownDuration);
        animatorUpDown1.setInterpolator(new CycleInterpolator(1));
        animatorUpDown1.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator animatorAlphaIn2 = ObjectAnimator.ofObject(this, "Round2Alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn2.setDuration(alphaInDuration);

        ObjectAnimator animatorUpDown2 = animatorUpDown1.clone();
        animatorUpDown2.setPropertyName("Round2Y");

        ObjectAnimator animatorAlphaIn3 = ObjectAnimator.ofObject(this, "Round3Alpha", new FloatEvaluator(), 0f, 1f);
        animatorAlphaIn3.setDuration(alphaInDuration);
        ObjectAnimator animatorUpDown3 = animatorUpDown1.clone();
        animatorUpDown3.setPropertyName("Round3Y");

        ObjectAnimator animatorUpDown4 = animatorUpDown1.clone();
        animatorUpDown4.setPropertyName("Round4Y");

        animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimator).before(moveAnimator);
        animatorSet.play(moveAnimator).with(animatorUpDown3);//播放完预动画，时间为2*preAnimDuration，而animatorUpDown3播放的具体时间是preAnimDuration

        animatorSet.play(animatorUpDown2).after(preAnimDuration + alphaInDuration);//preAnimDuration+alphaInDuration
        animatorSet.play(animatorUpDown1).after(preAnimDuration + (2 * alphaInDuration));
        animatorSet.play(animatorUpDown4).after(preAnimDuration + (3 * alphaInDuration));

        animatorSet.play(animatorAlphaIn3).after(preAnimDuration); //preAnimDuration
        animatorSet.play(animatorAlphaIn2).after(preAnimDuration + alphaInDuration);
        animatorSet.play(animatorAlphaIn1).after(preAnimDuration + (2 * alphaInDuration));

        animatorSet.setStartDelay(200);//以上动画延时200毫秒执行，解决一开始的缩放动画卡顿问题
    }
    //TODO private非法
    public void setScaleRoundSize(int size) {
        scaleRound.setSize(size);
        invalidate();
    }

    public void setScaleRoundX(int x) {
        scaleRound.setX(x);
        invalidate();
    }

    public void setRound1Alpha(float alpha) {
        round1.setAlpha(alpha);
        invalidate();
    }

    public void setRound2Alpha(float alpha) {
        round2.setAlpha(alpha);
        invalidate();
    }
    public void setRound3Alpha(float alpha) {
        round3.setAlpha(alpha);
        invalidate();
    }

    public void setRound1Y(int Y) {
        round1.setY(Y);
        invalidate();
    }

    public void setRound2Y(int Y) {
        round2.setY(Y);
        invalidate();
    }

    public void setRound3Y(int Y) {
        round3.setY(Y);
        invalidate();
    }

    public void setRound4Y(int Y) {
        scaleRound.setY(Y);
        invalidate();
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private class RoundBean {
        private int color;
        private Paint paint;
        private int x;
        private int y;
        private float alpha;
        private float scaleFactor;//0f~1f
        private int size;

        public RoundBean(int color) {
            paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint paint) {
            this.paint = paint;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public float getAlpha() {
            return alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            if (paint != null) {
                paint.setAlpha((int) (alpha * 255));
            }
        }

        public float getScaleFactor() {
            return scaleFactor;
        }

        public void setScaleFactor(float scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
            if (paint == null) {
                paint = new Paint();
            }
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setAttrs(int color, int x, int y, int size, float alpha, float scaleFactor) {
            this.size = size;
            this.x = x;
            this.y = y;
            this.alpha = alpha;
            this.scaleFactor = scaleFactor;
            if (paint == null) {
                paint = new Paint();
                paint.setColor(color);
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);
            }
            paint.setAlpha((int) (255 * alpha));
        }
    }
}
