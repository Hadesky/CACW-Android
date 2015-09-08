package com.hadesky.cacw.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hadesky.cacw.R;

/**
 * PagerView的指示器，自定义View
 * Created by Derek on 2015/8/14.
 */
public class IndicatorView extends View implements ViewPager.OnPageChangeListener{

    //空指示器图标
    private Drawable mIndicatorEmpty;
    //选择的指示器图标
    private Drawable mIndicatorSelected;
    //整个指示器的大小
    private int mWidth;
    //padding加上间距加上每个指示器大小
    private int mContextWidth;
    //滑动的偏移量
    private float mOffSet;
    //指示器的大小,根据指示器图片大小来决定，取其大者
    private int mIndicatorSize;
    //指示器的个数
    private int mCount;
    //指示器的间距
    private int mMargin;
    //当前所选中的item
    private int mSelectedItem;
    //是否平滑移动
    private boolean mSmooth;
    /**
     * 因为ViewPager 的 pageChangeListener 被占用了，所以需要定义一个
     * 以便其他调用
     */
    private ViewPager.OnPageChangeListener mPageChangeListener;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.IndicatorView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IndicatorView_indicator_empty:
                    mIndicatorEmpty = typedArray.getDrawable(attr);
                    break;
                case R.styleable.IndicatorView_indicator_selected:
                    mIndicatorSelected = typedArray.getDrawable(attr);
                    break;
                case R.styleable.IndicatorView_indicator_margin:
                    float defMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                    mMargin = (int) typedArray.getDimension(attr, defMargin);
                    break;
                case R.styleable.IndicatorView_indicator_smooth:
                    mSmooth = typedArray.getBoolean(attr, false);
                    break;
            }
        }
        typedArray.recycle();
        initIndicator();
    }

    private void initIndicator() {
        //获取指示器大小
        if (mIndicatorEmpty.getIntrinsicWidth()!=mIndicatorSelected.getIntrinsicWidth())
            throw new RuntimeException("图标大小错误，请统一图标大小");
        mIndicatorSize = Math.max(mIndicatorEmpty.getIntrinsicWidth(), mIndicatorEmpty.getIntrinsicHeight());


        mIndicatorEmpty.setBounds(0, 0, mIndicatorSize, mIndicatorSize);
        mIndicatorSelected.setBounds(0, 0, mIndicatorSize, mIndicatorSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        int desert = getPaddingBottom() + getPaddingTop() + mIndicatorSize;
        if (mode == MeasureSpec.AT_MOST) {
            height = Math.min(size, desert);
        } else {
            height = desert;
        }
        return height;
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width;
        int desert = getPaddingLeft() + getPaddingRight() + mCount * mIndicatorSize + (mCount - 1) * mMargin;
        mContextWidth = desert;
        if (mode == MeasureSpec.AT_MOST) {
            width = Math.min(size, desert);
        } else if (mode == MeasureSpec.EXACTLY) {
            width = Math.max(size, desert);
        } else {
            width = desert;
        }
        mWidth = width;
        return width;
    }

    /**
     * 重绘View
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //保存当前位置
        canvas.save();
        //计算当前第一个指示器的位置
        int left = mWidth / 2 - mContextWidth / 2 + getPaddingLeft();
        //移动到第一个指示器的位置
        canvas.translate(left, getPaddingTop());
        //开始绘制每一个空指示器
        for (int i = 0; i < mCount; i++) {
            mIndicatorEmpty.draw(canvas);
            canvas.translate(mMargin + mIndicatorSize, 0);
        }
        //恢复默认位置
        canvas.restore();
        float leftDraw = (mIndicatorSize + mMargin) * (mSelectedItem + mOffSet);
        canvas.translate(left, getPaddingTop());
        canvas.translate(leftDraw, 0);
        mIndicatorSelected.draw(canvas);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new RuntimeException("ViewPager的Adapter为空");
        }
        mCount = pagerAdapter.getCount();
        viewPager.addOnPageChangeListener(this);
        mSelectedItem = viewPager.getCurrentItem();

        invalidate();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mSmooth) {
            mSelectedItem = position;
            mOffSet = positionOffset;
            invalidate();
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mSelectedItem = position;
        invalidate();
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }
}
