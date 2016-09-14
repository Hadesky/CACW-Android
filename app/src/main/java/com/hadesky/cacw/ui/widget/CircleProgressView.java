package com.hadesky.cacw.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.hadesky.cacw.R;

/**
 *
 * Created by dzysg on 2016/9/5 0005.
 */
public class CircleProgressView extends View
{

    private int mHeigh = 50;
    private int mWidth = 50;
    private Paint mCiclePain;
    private Paint mTextPain;
    private int mCicleColor;
    private int mTextColor;
    private float mPercent=  0.9f;
    private int mStrokeWidth = 20;
    private String mText;
    private int mTextSize;
    private Paint mUnFinishPain;
    private int mUnfinishColor =0xFFFF9E33;
    float offY;


    public CircleProgressView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context,attrs);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CircleProgress);

        mCicleColor = typedArray.getColor(R.styleable.CircleProgress_circle_color, 0xFF15E002);
        mTextColor = typedArray.getColor(R.styleable.CircleProgress_text_color, 0xFF15E002);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgress_circle_width,20);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgress_text_size,30);
        mPercent = typedArray.getFloat(R.styleable.CircleProgress_percent,0.9f);
        mText = typedArray.getString(R.styleable.CircleProgress_text);
        mUnfinishColor = typedArray.getColor(R.styleable.CircleProgress_unfinish_color,0xFFFF9E33);


        typedArray.recycle();

        if(mText==null)
            mText = "noText";

        if(mPercent>1)
            mPercent = 0.5f;

        mCiclePain = new Paint();
        mCiclePain.setColor(mCicleColor);
        mCiclePain.setStyle(Paint.Style.STROKE);
        mCiclePain.setStrokeWidth(mStrokeWidth);
        mCiclePain.setAntiAlias(true);

        mTextPain = new Paint();
        mTextPain.setColor(mTextColor);
        mTextPain.setTextAlign(Paint.Align.CENTER);
        mTextPain.setTextSize(mTextSize);
        mTextPain.setAntiAlias(true);

        mUnFinishPain = new Paint();
        mUnFinishPain.setColor(mUnfinishColor);
        mUnFinishPain.setStyle(Paint.Style.STROKE);
        mUnFinishPain.setStrokeWidth(mStrokeWidth);
        mUnFinishPain.setAntiAlias(true);


        Paint.FontMetrics fm = mTextPain.getFontMetrics();
        offY = (fm.bottom - fm.top) / 2 - fm.bottom;
    }

    RectF rectF = new RectF();


    public void setPercent(float p)
    {
        if(p>=0&&p<=1&&p!=mPercent)
        {
            mPercent= p;
            invalidate();
        }
    }

    public void setText(String t)
    {
        if(!mText.equals(t))
        {
            mText = t;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeigh = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(mStrokeWidth,mStrokeWidth,mWidth-mStrokeWidth,mHeigh-mStrokeWidth);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawArc(rectF,0,360*mPercent,false,mCiclePain);
        canvas.drawArc(rectF,0,-360*(1-mPercent),false,mUnFinishPain);
        canvas.drawText(mText,mWidth/2,mHeigh/2+offY,mTextPain);
    }
}
