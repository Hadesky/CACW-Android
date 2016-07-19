package com.hadesky.cacw.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/** 用来判断子view是否滑到顶
 * Created by dzysg on 2016/7/6 0006.
 */
public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {

    View mScrollUpChild;
    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean canChildScrollUp() {

        //判断子view是否可再向下滑
        if (mScrollUpChild != null) {
            return ViewCompat.canScrollVertically(mScrollUpChild, -1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }
}
