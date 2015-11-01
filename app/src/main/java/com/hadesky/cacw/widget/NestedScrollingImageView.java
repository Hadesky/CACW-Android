package com.hadesky.cacw.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.hadesky.cacw.util.ViewGroupUtils;

import java.util.List;

/**
 * Created by 45517 on 2015/10/31.
 */
public class NestedScrollingImageView extends ImageView implements NestedScrollingChild {
    private NestedScrollingChildHelper mHelper;

    public NestedScrollingImageView(Context context) {
        this(context, null);
    }

    public NestedScrollingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = new NestedScrollingChildHelper(this);
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public static class ImageViewBehavior extends CoordinatorLayout.Behavior<CircleImageView> {
        private Rect mTmpRect;

        public ImageViewBehavior() {
        }

        public ImageViewBehavior(Context context, AttributeSet attrs) {
            super();
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, CircleImageView child, int layoutDirection) {
            List dependencies = parent.getDependencies(child);
            int i = 0;

            for(int count = dependencies.size(); i < count; ++i) {
                View dependency = (View)dependencies.get(i);
                if(dependency instanceof AppBarLayout && this.updateFabVisibility(parent, (AppBarLayout)dependency, child)) {
                    break;
                }
            }
            parent.onLayoutChild(child, layoutDirection);
            return true;
        }

        private boolean updateFabVisibility(CoordinatorLayout parent, AppBarLayout appBarLayout, CircleImageView child) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
            if(lp.getAnchorId() != appBarLayout.getId()) {
                return false;
            } else {
                if(this.mTmpRect == null) {
                    this.mTmpRect = new Rect();
                }

                Rect rect = this.mTmpRect;
                ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);

                return true;
            }
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
            if(dependency instanceof AppBarLayout) {
                this.updateFabVisibility(parent, (AppBarLayout) dependency, child);
            }
            return false;
        }
    }
}
