package com.hadesky.cacw.ui.widget;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.hadesky.cacw.R;

/**
 *
 * Created by 45517 on 2016/7/25.
 */
public class ColorfulPopupMemu implements MenuBuilder.Callback {

    private View mAnChorView;//锚点View

    private MenuBuilder mMenu;

    private Context mContext;

    private MenuPopupHelper mMenuPopupHelper;

    public ColorfulPopupMemu(Context context, View anchorView) {
        this(context, anchorView, Gravity.NO_GRAVITY);
    }

    public ColorfulPopupMemu(Context context, View anchorView, int gravity) {
        mContext = context;
        mAnChorView = anchorView;
        mMenu = new MenuBuilder(mContext);
        mMenu.setCallback(this);
        mMenuPopupHelper = new MenuPopupHelper(context, mMenu, anchorView, false, R.attr.popupMenuStyle);
    }


    public MenuInflater getMenuInflater() {
        return new MenuInflater(mContext);
    }

    public Menu getMenu() {
        return mMenu;
    }

    @Override
    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
        return false;
    }

    @Override
    public void onMenuModeChange(MenuBuilder menu) {

    }
}
