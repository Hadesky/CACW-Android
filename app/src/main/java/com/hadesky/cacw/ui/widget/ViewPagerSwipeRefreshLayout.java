package com.hadesky.cacw.ui.widget;

import android.content.Context;
import android.support.v4.widget.*;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 下拉刷新layout，为了解决和ViewPager的滑动冲突
 * Created by Leaves on 2016/8/6.
 */
public class ViewPagerSwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout{
    private int mLastXIntercept;
    private int mLastYIntercept;

    public ViewPagerSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ViewPagerSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                intercepted = Math.abs(deltaY) > Math.abs(deltaX);
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted && super.onInterceptTouchEvent(ev);
    }
}
