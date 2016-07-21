package com.hadesky.cacw.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.util.ImageResizer;
import com.hadesky.cacw.util.DensityUtil;

/**
 * 自定义View，带有一个删除键
 * Created by Derek on 2015/8/17.
 */
public class DeletableEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private int mUnderlineColor;
    private int HintColor = 0;
    private Paint mPaint;
    private int mClearIconId;
    private Drawable mClearIcon;
    private int mClearIconSize;
    private int mIconLeftX;
    private int mIconRightX;
    private boolean isClearIconVisible = true;
    private boolean isFocused = false;
    private boolean isTouch = false;
    private Resources mResources;

    private float animProportion = 0;//下划线动画的进度比例，0f~1f
    private ObjectAnimator underLineAnim;


    public DeletableEditText(Context context) {
        this(context, null);
    }

    public DeletableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public DeletableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mResources = getResources();

        TypedArray typedArray = mResources.obtainAttributes(attrs, R.styleable.DeletableEditText);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.DeletableEditText_clear_icon:
                    mClearIconId = typedArray.getResourceId(attr, -1);
                    break;
                case R.styleable.DeletableEditText_underline_color:
                    mUnderlineColor = typedArray.getColor(attr, mResources.getColor(android.R.color.darker_gray));
                default:
                    break;
            }
        }
        typedArray.recycle();

        HintColor = getResources().getColor(R.color.color_primary);

        init();

        underLineAnim = ObjectAnimator.ofFloat(this, "animProportion", 0f, 1f);
        underLineAnim.setDuration(250);
        underLineAnim.setInterpolator(new DecelerateInterpolator());
        underLineAnim.setStartDelay(100);
    }

    private void init() {
        mClearIcon = ResourcesCompat.getDrawable(getResources(), mClearIconId, null);
        if (mClearIcon != null) {
            mClearIcon.setBounds(0, 0, 80, 80);
        }
        mPaint = new Paint();

        if (mClearIcon == null) {
            throw new RuntimeException("没有为删除图标设置资源");
        }

        mClearIconSize = Math.max(mClearIcon.getBounds().width(), mClearIcon.getBounds().width());

        //默认隐藏clear按钮
        setIsClearIconVisible(false);
        //设置焦点变化的监听器
        setOnFocusChangeListener(this);
        //设置内容变化监听器
        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算icon绘制的位置
        mIconRightX = getWidth()  - getPaddingRight();
        mIconLeftX = mIconRightX - mClearIconSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isClearIconVisible) {
                    //按下事件，且图标可视，此时应该判断第一次按下的位置是否处图标所在位置
                    isTouch = event.getX() > mIconLeftX && event.getX() < mIconRightX;
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起，且图标可视，此时应该判断位置是否在图标所在位置，若是，再判断isTouch是否为真，为真则清空文本
                if (event.getX() > mIconLeftX && event.getX() < mIconRightX) {
                    if (isTouch) {
                        this.setText("");
                    }
                }
                break;
        }
        return super.onTouchEvent(event);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnderLine(canvas, isFocused);
    }

    public void setIsClearIconVisible(boolean isClearIconVisible) {
        this.isClearIconVisible = isClearIconVisible;
        if (isClearIconVisible) {
            setCompoundDrawables(null, null, mClearIcon, null);
        } else {
            setCompoundDrawables(null, null, null, null);
        }
    }

    public void setHintColor(int Color) {
        this.HintColor = Color;
    }

    private void drawUnderLine(Canvas canvas,boolean foucsed) {
        mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
        mPaint.setColor(mUnderlineColor);

        int x = this.getScrollX();
        int w = this.getMeasuredWidth();
        int halfWidth = (x + w) / 2;
        canvas.drawLine(0, this.getHeight() - 1, w + x,
                this.getHeight() - 1, mPaint);
        if (foucsed && HintColor != 0) {
            mPaint.setColor(HintColor);
            mPaint.setAlpha((int) (animProportion * 255));
            mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
        }
        canvas.drawLine(halfWidth - halfWidth * animProportion, this.getHeight() - 1, halfWidth + halfWidth * animProportion,
                this.getHeight() - 1, mPaint);
    }


    public float getAnimProportion() {
        return animProportion;
    }

    public void setAnimProportion(float animProportion) {
        this.animProportion = animProportion;
        invalidate();
    }

    public void setUnderlineColor(int Color) {
        this.mUnderlineColor = Color;
        invalidate();
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setIsClearIconVisible(getText().length() > 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        isFocused = hasFocus;
        if (hasFocus) {
            setIsClearIconVisible(getText().length() > 0);
            animProportion = 0;
            underLineAnim.start();
        } else {
            setIsClearIconVisible(false);
        }
        invalidate();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
