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

/**
 * 自定义View，带有一个删除键
 * Created by Derek on 2015/8/17.
 */
public class DeletableEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private int mUnderlineColor;
    private Paint mPaint;
    private int mClearIconId;
    private BitmapDrawable mClearIcon;
    private int mClearIconSize;
    private int mIconLeftX;
    private int mIconRightX;
    private boolean isClearIconVisible = true;
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
        init();
    }

    private void init() {

        //这里的18是调试出来的，估计换设备调试要跪
        final Bitmap ClearIconBitmap = DecodeBitmap.decodeSampledBitmapFromResource(mResources, mClearIconId, 18, 18);
        mClearIcon = new BitmapDrawable(mResources, ClearIconBitmap);

        mPaint = new Paint();
        mPaint.setStrokeWidth(3.0f);
        mPaint.setColor(mUnderlineColor);

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
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isClearIconVisible) {
                boolean isTouch = event.getX() > mIconLeftX && event.getX() < mIconRightX;
                if (isTouch) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int x=this.getScrollX();
            int w=this.getMeasuredWidth();
            canvas.drawLine(0, this.getHeight() - 1, w+x,
                    this.getHeight() - 1, mPaint);
    }

    public void setIsClearIconVisible(boolean isClearIconVisible) {
        this.isClearIconVisible = isClearIconVisible;
        if (isClearIconVisible) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, mClearIcon, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setIsClearIconVisible(getText().length() > 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setIsClearIconVisible(getText().length() > 0);
        } else {
            setIsClearIconVisible(false);
        }
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
