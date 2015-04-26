/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.graphics.Rect
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.layoutcoordinator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;

class MenuDialogRectCalculator {
    private final Rect mBounds;
    private final Context mContext;
    private final int mDividerHeight;
    private final boolean mIsTablet;
    private final int mItemHeight;
    private final int mMaxHeightMargin;
    private int mMenuDialogRowCount;
    private int mNumberOfTabs;
    private final int mPadding;
    private final int mTabHeight;
    private final int mWidth;

    /*
     * Enabled aggressive block sorting
     */
    public MenuDialogRectCalculator(Context context, Rect rect, int n, int n2) {
        this.mContext = context;
        Resources resources = context.getResources();
        this.mPadding = resources.getDimensionPixelSize(R.dimen.menu_dialog_padding);
        this.mDividerHeight = resources.getDimensionPixelSize(R.dimen.divider_height);
        this.mTabHeight = resources.getDimensionPixelSize(R.dimen.setting_group_tab_height);
        this.mWidth = resources.getDimensionPixelSize(R.dimen.setting_dialog_menu_width);
        this.mItemHeight = resources.getDimensionPixelSize(R.dimen.menu_dialog_item_height);
        this.mBounds = rect;
        this.mIsTablet = LayoutDependencyResolver.isTablet(this.mContext);
        int n3 = this.mIsTablet ? resources.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_tablet) : resources.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_phone);
        this.mMaxHeightMargin = n3;
        this.mMenuDialogRowCount = n;
        this.mNumberOfTabs = n2;
    }

    private Point computePositionForPhone(int n) {
        if (super.isPortrait(n)) {
            return new Point(this.mBounds.left, this.mBounds.top + (this.mBounds.height() - this.computeWidth(n)) / 2);
        }
        return new Point(this.mBounds.left, this.mBounds.top + (this.mBounds.height() - this.computeHeight(n)) / 2);
    }

    private Point computePositionForTablet(int n) {
        int n2 = LayoutDependencyResolver.getLeftItemCount(this.mContext);
        int n3 = this.mContext.getResources().getDimensionPixelSize(R.dimen.shortcut_dialog_item_height);
        int n4 = (this.mBounds.height() / n2 - n3) / 2;
        if (super.isPortrait(n)) {
            return new Point(this.mBounds.left, this.mBounds.bottom - n4 - this.computeWidth(n));
        }
        return new Point(this.mBounds.left, this.mBounds.bottom - n4 - this.computeHeight(n));
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getNumRows(int n) {
        int n2 = this.mNumberOfTabs < 2 ? (n - this.mMaxHeightMargin - 2 * this.mPadding + this.mDividerHeight) / (this.mItemHeight + this.mDividerHeight) : (n - this.mMaxHeightMargin - 2 * this.mPadding - this.mTabHeight + this.mDividerHeight) / (this.mItemHeight + this.mDividerHeight);
        if (this.mMenuDialogRowCount <= 0) return n2;
        if (this.mMenuDialogRowCount >= n2) return n2;
        return this.mMenuDialogRowCount;
    }

    private boolean isPortrait(int n) {
        if (n == 1) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int computeHeight(int n) {
        int n2 = super.isPortrait(n) ? this.mBounds.width() : this.mBounds.height();
        int n3 = super.getNumRows(n2);
        if (this.mNumberOfTabs < 2) {
            return n3 * this.mItemHeight + 2 * this.mPadding + n3 * this.mDividerHeight;
        }
        return n3 * this.mItemHeight + 2 * this.mPadding + this.mTabHeight + n3 * this.mDividerHeight;
    }

    public Point computePosition(int n) {
        if (this.mIsTablet) {
            return super.computePositionForTablet(n);
        }
        return super.computePositionForPhone(n);
    }

    public int computeWidth(int n) {
        return this.mWidth;
    }
}

