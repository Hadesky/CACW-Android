package com.hadesky.cacw.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hadesky.cacw.R;


/** 圆圈加单个字的view
 * Created by dzysg on 2016/7/17 0017.
 */
public class CircleTextView extends View {

    private int mCircleColor;
    private int mTextColor;
    private float mTextSize;
    private String mText = "哈";
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private float offY;
    static int index = 0;


    public int[] mColorList = {
            0xFFF44336, 0xFF3F51B5, 0xFF4CAF50, 0xFFFF9800, 0xFF795548, 0xFFFBC02D, 0xFF009688, 0xFF512DA8, 0xFF9C27B0,
    };

    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);

        mText = typedArray.getString(R.styleable.CircleTextView_Text);
        mTextColor = typedArray.getColor(R.styleable.CircleTextView_TextColor, Color.WHITE);
        mCircleColor = typedArray.getColor(R.styleable.CircleTextView_CircleColor, Color.BLACK);
        mTextSize = typedArray.getDimension(R.styleable.CircleTextView_TextSize, 30);
        boolean isRandomColor = typedArray.getBoolean(R.styleable.CircleTextView_RandomColor, true);

        typedArray.recycle();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        if (isRandomColor)
            mCirclePaint.setColor(getRandomColor());
        else
            mCirclePaint.setColor(mCircleColor);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        offY = (fm.bottom - fm.top) / 2 - fm.bottom;


        if (mText==null||mText.length() == 0)
            mText = "无";
        if (mText.length() > 1)
            mText = mText.substring(0, 1);
    }


    private int getRandomColor() {
        index++;
        return mColorList[index % mColorList.length];
    }

    public void setText(String text) {
        if (text == null||text.length()==0)
            return;

        if (text.length() > 1)
            mText = text.substring(0, 1);
        else
            mText = text;
        invalidate();
    }

    public void setBgColor(int color) {
        mCircleColor = color;
        mCirclePaint.setColor(mCircleColor);
        invalidate();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(color);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getMeasuredHeight() - getPaddingLeft() - getPaddingRight();
        radius /= 2;
        float center = getMeasuredHeight() / 2;
        canvas.drawCircle(center, center, radius, mCirclePaint);
        canvas.drawText(mText, center, center + offY, mTextPaint);
    }
}
