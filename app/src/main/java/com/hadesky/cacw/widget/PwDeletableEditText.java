package com.hadesky.cacw.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hadesky.cacw.R;

/**
 * Created by 45517 on 2015/9/16.
 */
public class PwDeletableEditText extends LinearLayout {
    private Resources mResources;
    private Paint mPaint;
    private int mEyeIconId;
    private int mUnderlineColor;
    private boolean mIsPwVisitable = false;


    private DeletableEditText deletableEditText;
    private ImageButton eyeButton;

    public PwDeletableEditText(Context context) {
        this(context, null);
    }


    public PwDeletableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = getResources();
        mPaint = new Paint();

        TypedArray typedArray = mResources.obtainAttributes(attrs, R.styleable.PwDeletableEditText);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.DeletableEditText_underline_color:
                    mUnderlineColor = typedArray.getColor(attr, mResources.getColor(android.R.color.darker_gray));
                    break;
                case R.styleable.PwDeletableEditText_eye_icon:
                    mEyeIconId = typedArray.getResourceId(attr, -1);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        deletableEditText = new DeletableEditText(context, attrs);
        deletableEditText.setUnderlineColor(Color.TRANSPARENT);
        deletableEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        deletableEditText.setImeOptions(EditorInfo.IME_ACTION_GO);

        eyeButton = new ImageButton(context);

        setupEyeButton();

        addView(deletableEditText);
        addView(eyeButton);
    }

    private void setupEyeButton() {
        eyeButton.setLayoutParams(new ViewGroup.LayoutParams(40,40));
        eyeButton.setImageResource(mEyeIconId);
        eyeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        eyeButton.setBackgroundColor(Color.TRANSPARENT);

        eyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsPwVisible(!mIsPwVisitable);
                if (mIsPwVisitable) {
                    eyeButton.setSelected(false);
                    mIsPwVisitable = false;
                    setIsPwVisible(false);
                } else {
                    eyeButton.setSelected(true);
                    mIsPwVisitable = true;
                    setIsPwVisible(true);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnderLine(canvas);
    }

    private void drawUnderLine(Canvas canvas) {
        mPaint.setStrokeWidth(3.0f);
        mPaint.setColor(mUnderlineColor);
        int x=this.getScrollX();
        int w=this.getMeasuredWidth();
        canvas.drawLine(0, this.getHeight() - 1, w + x,
                this.getHeight() - 1, mPaint);
    }

    private void setIsPwVisible(boolean visible) {
        if (visible) {
            deletableEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //定位到最后
            CharSequence text = deletableEditText.getText();
            if (text != null) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        } else {
            deletableEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //定位到最后
            CharSequence text = deletableEditText.getText();
            if (text != null) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
    }

    public void setUnderlineColor(int Color) {
        this.mUnderlineColor = Color;
        invalidate();
    }
}
