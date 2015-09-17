package com.hadesky.cacw.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.util.DecodeBitmap;
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
    private BitmapDrawable mClearIcon;
    private int mClearIconSize;
    private int mIconLeftX;
    private int mIconRightX;
    private boolean isClearIconVisible = true;
    private boolean isFocused = false;
    private boolean isTouch = false;
    private Resources mResources;


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
    }

    private void init() {

        //这里的18是调试出来的，估计换设备调试要跪
        final Bitmap ClearIconBitmap = DecodeBitmap.decodeSampledBitmapFromResource(mResources, mClearIconId, 18, 18);
        mClearIcon = new BitmapDrawable(mResources, ClearIconBitmap);

        mPaint = new Paint();

        if (mClearIcon == null) {
            throw new RuntimeException("没有为删除图标设置资源");
        }

        mClearIconSize = Math.max(mClearIcon.getIntrinsicWidth(), mClearIcon.getIntrinsicHeight());

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
        mIconRightX = getMeasuredWidth() - getCompoundDrawablePadding() - getPaddingRight();
        mIconLeftX = getMeasuredWidth() - mClearIconSize - getCompoundDrawablePadding() - getPaddingRight();
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
            setCompoundDrawablesWithIntrinsicBounds(null, null, mClearIcon, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public void setHintColor(int Color) {
        this.HintColor = Color;
    }


    private void drawUnderLine(Canvas canvas,boolean foucsed) {
        mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
        mPaint.setColor(mUnderlineColor);
        if (foucsed && HintColor != 0) {
            mPaint.setColor(HintColor);
        }
        int x = this.getScrollX();
        int w = this.getMeasuredWidth();
        canvas.drawLine(0, this.getHeight() - 1, w + x,
                this.getHeight() - 1, mPaint);
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
