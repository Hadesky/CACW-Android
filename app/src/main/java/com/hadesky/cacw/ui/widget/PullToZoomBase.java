package com.hadesky.cacw.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hadesky.cacw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * from: https://github.com/Frank-Zhu/PullZoomView
 *
 * Created by 45517 on 2015/11/4.
 */
public abstract class PullToZoomBase<T extends View> extends LinearLayout implements IPullToZoom<T> {
    private static final float FRICTION = 2.0f;
    protected T mRootView;
    protected View mHeaderView;//头部View
    protected View mZoomView;//缩放拉伸View

    private boolean isZoomEnabled = true;
    private boolean isParallax = true;
    private boolean isZooming = false;
    private boolean isHideHeader = false;

    private int mTouchSlop;
    private boolean mIsBeingDragged = false;
    private float mLastMotionY1, mLastMotionY2 = 0;
    private float mLastMotionX1, mLastMotionX2 = 0;
    private float mInitialMotionY1, mInitialMotionY2 = 0;
    private float mInitialMotionX1, mInitialMotionX2 = 0;
    private List<OnPullZoomListener> mOnPullZoomListeners = new ArrayList<>();

    private int newScrollValue = 0;

    public PullToZoomBase(Context context) {
        this(context, null);
    }

    public PullToZoomBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setGravity(Gravity.CENTER);

        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        // Refreshable View
        // By passing the attrs, we can add ListView/GridView params via XML
        mRootView = createRootView(context, attrs);

        if (attrs != null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
            //初始化状态View
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PullToZoomView);

            int zoomViewResId = a.getResourceId(R.styleable.PullToZoomView_zoomView, 0);
            if (zoomViewResId > 0) {
                mZoomView = mLayoutInflater.inflate(zoomViewResId, null, false);
            }

            int headerViewResId = a.getResourceId(R.styleable.PullToZoomView_headerView, 0);
            if (headerViewResId > 0) {
                mHeaderView = mLayoutInflater.inflate(headerViewResId, null, false);
            }

            isParallax = a.getBoolean(R.styleable.PullToZoomView_isHeaderParallax, true);

            // Let the derivative classes have a go at handling attributes, then
            // recycle them...
            //interface方法
            handleStyledAttributes(a);
            a.recycle();
        }
        addView(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void addOnPullZoomListeners(OnPullZoomListener onPullZoomListeners) {
        this.mOnPullZoomListeners.add(onPullZoomListeners);
    }

    @Override
    public T getPullRootView() {
        return mRootView;
    }

    @Override
    public View getZoomView() {
        return mZoomView;
    }

    @Override
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public boolean isPullToZoomEnabled() {
        return isZoomEnabled;
    }

    @Override
    public boolean isZooming() {
        return isZooming;
    }

    @Override
    public boolean isParallax() {
        return isParallax;
    }

    @Override
    public boolean isHideHeader() {
        return isHideHeader;
    }

    public void setZoomEnabled(boolean isZoomEnabled) {
        this.isZoomEnabled = isZoomEnabled;
    }

    public void setParallax(boolean isParallax) {
        this.isParallax = isParallax;
    }

    public void setHideHeader(boolean isHideHeader) {//header显示才能Zoom
        this.isHideHeader = isHideHeader;
    }



    private void NotifyZoomEndLisenter()
    {
        for(OnPullZoomListener listener: mOnPullZoomListeners)
        {
            listener.onPullZoomEnd();
        }
    }

    private void NotifyOnPullZooming(int newScrollValue)
    {
        for(OnPullZoomListener listener: mOnPullZoomListeners)
        {
            listener.onPullZooming(newScrollValue);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //未开启功能，不消费
        if (!isPullToZoomEnabled() || isHideHeader()) {
            return false;
        }

        final int action = event.getAction();
        //手指抬起
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }
        //
        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                if (isReadyForPullStart()) {
                    final float y = event.getY(), x = event.getX();
                    final float diff, oppositeDiff, absDiff;

                    // We need to use the correct values, based on scroll
                    // direction
                    diff = y - mLastMotionY1;
                    oppositeDiff = x - mLastMotionX1;
                    absDiff = Math.abs(diff);

                    if (absDiff > mTouchSlop && absDiff > Math.abs(oppositeDiff)) {
                        if (diff >= 1f) {
                            mLastMotionY1 = y;
                            mLastMotionX1 = x;
                            mIsBeingDragged = true;
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY1 = mInitialMotionY1 = event.getY(0);
                    mLastMotionX1 = mInitialMotionX1 = event.getX(0);
                    mIsBeingDragged = false;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY2 = mInitialMotionY2 = event.getY(1);
                    mLastMotionX2 = mInitialMotionX2 = event.getX(1);
                }
                break;
            }
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!isPullToZoomEnabled() || isHideHeader()) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }
        boolean multTouch = event.getPointerCount() >= 2;
        switch (event.getAction()&MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionY1 = event.getY(0);
                    mLastMotionX1 = event.getX(0);
                    if (multTouch) {
                        mLastMotionX2 = event.getX(1);
                        mLastMotionY2 = event.getY(1);
                    }
                    pullEvent(multTouch);
                    isZooming = true;
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
                if (isReadyForPullStart()) {
                    mLastMotionY2 = mInitialMotionY2 = event.getY(1);
                    mLastMotionX2 = mInitialMotionX2 = event.getX(1);
                    return true;
                }
            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionY1 = mInitialMotionY1 = event.getY(0);
                    mLastMotionX1 = mInitialMotionX1 = event.getX(0);
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    // If we're already refreshing, just scroll back to the top
                    if (isZooming()) {
                        smoothScrollToTop();
                        NotifyZoomEndLisenter();
                        isZooming = false;
                        return true;
                    }
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                mInitialMotionY2 = 0;
                mLastMotionY2 = 0;
        }
        return false;
    }

    public int getNewScrollValue() {
            newScrollValue = Math.round(Math.min(mInitialMotionY1 - mLastMotionY1, 0) / FRICTION) +
                    Math.round(Math.min(mInitialMotionY2 - mLastMotionY2, 0) / FRICTION);

        return newScrollValue;
    }


    private void pullEvent(boolean multTouch) {
        newScrollValue = getNewScrollValue();

        pullHeaderToZoom(newScrollValue);
        NotifyOnPullZooming(newScrollValue);
    }

    protected abstract void pullHeaderToZoom(int newScrollValue);

    public abstract void setHeaderView(View headerView);

    public abstract void setZoomView(View zoomView);

    protected abstract T createRootView(Context context, AttributeSet attrs);

    protected abstract void smoothScrollToTop();

    protected abstract boolean isReadyForPullStart();

    public interface OnPullZoomListener {
        public void onPullZooming(int newScrollValue);

        public void onPullZoomEnd();
    }
}
