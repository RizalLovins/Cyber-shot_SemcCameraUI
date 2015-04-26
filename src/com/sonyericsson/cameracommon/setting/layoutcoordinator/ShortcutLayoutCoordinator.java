/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  java.lang.Math
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.layoutcoordinator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogBasic;
import com.sonyericsson.cameracommon.setting.layoutcoordinator.LayoutCoordinator;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;

public class ShortcutLayoutCoordinator
implements LayoutCoordinator {
    private final Rect mAnchorRect;
    private final Rect mContainerRect;
    private int mDialogHeight;
    public Rect mDialogRect;
    private int mDialogWidth;
    private final boolean mIsTablet;
    private final int mMaxHeightMargin;
    private final SettingDialogBasic mView;

    /*
     * Enabled aggressive block sorting
     */
    public ShortcutLayoutCoordinator(SettingDialogBasic settingDialogBasic, Rect rect, Rect rect2) {
        this.mView = settingDialogBasic;
        this.mContainerRect = rect;
        this.mAnchorRect = rect2;
        this.mIsTablet = LayoutDependencyResolver.isTablet(this.mView.getContext());
        Resources resources = this.mView.getContext().getResources();
        int n = this.mIsTablet ? resources.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_tablet) : resources.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_phone);
        this.mMaxHeightMargin = n;
    }

    private void coordinatePositionPhone(int n) {
        this.mView.setPivotX(0.0f);
        this.mView.setPivotY(0.0f);
        this.mView.setRotation(RotationUtil.getAngle(n));
        if (super.isPortrait(n)) {
            int n2 = this.mContainerRect.left;
            int n3 = (int)((float)(this.mContainerRect.top + this.mDialogWidth) + (float)(this.mContainerRect.height() - this.mDialogWidth) / 2.0f);
            this.mView.setLeft(n2);
            this.mView.setRight(n2 + this.mDialogWidth);
            this.mView.setTop(n3);
            this.mView.setBottom(n3 + this.mDialogHeight);
            this.mDialogRect = new Rect((int)this.mView.getX(), (int)this.mView.getY() - this.mDialogWidth, (int)this.mView.getX() + this.mDialogHeight, (int)this.mView.getY());
            return;
        }
        int n4 = this.mContainerRect.left;
        int n5 = (int)((float)this.mContainerRect.top + (float)(this.mContainerRect.height() - this.mDialogHeight) / 2.0f);
        this.mView.setLeft(n4);
        this.mView.setRight(n4 + this.mDialogWidth);
        this.mView.setTop(n5);
        this.mView.setBottom(n5 + this.mDialogHeight);
        this.mDialogRect = new Rect((int)this.mView.getX(), (int)this.mView.getY(), (int)this.mView.getX() + this.mDialogWidth, (int)this.mView.getY() + this.mDialogHeight);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void coordinatePositionTablet(int n) {
        int n2 = LayoutDependencyResolver.getLeftItemCount(this.mView.getContext());
        int n3 = this.mView.getContext().getResources().getDimensionPixelSize(R.dimen.shortcut_dialog_item_height);
        int n4 = (this.mContainerRect.height() / n2 - n3) / 2;
        this.mView.setPivotX(0.0f);
        this.mView.setPivotY(0.0f);
        if (super.isPortrait(n)) {
            int n5 = this.mContainerRect.left;
            int n6 = (int)((float)(this.mContainerRect.top + this.mAnchorRect.centerY()) + (float)this.mDialogWidth / 2.0f);
            if (n6 < n4 + this.mContainerRect.top + this.mDialogWidth) {
                n6 = n4 + this.mContainerRect.top + this.mDialogWidth;
            } else if (n6 + this.mDialogWidth > this.mContainerRect.bottom - n4 + this.mDialogWidth) {
                n6 = this.mContainerRect.bottom - n4 - this.mDialogWidth + this.mDialogWidth;
            }
            this.mView.setLeft(n5);
            this.mView.setRight(n5 + this.mDialogWidth);
            this.mView.setTop(n6);
            this.mView.setBottom(n6 + this.mDialogHeight);
            this.mDialogRect = new Rect((int)this.mView.getX(), (int)this.mView.getY() - this.mDialogWidth, (int)this.mView.getX() + this.mDialogHeight, (int)this.mView.getY());
        } else {
            int n7 = this.mContainerRect.left;
            int n8 = (int)((float)(this.mContainerRect.top + this.mAnchorRect.centerY()) - (float)this.mDialogHeight / 2.0f);
            if (n8 < n4 + this.mContainerRect.top) {
                n8 = n4 + this.mContainerRect.top;
            } else if (n8 + this.mDialogHeight > this.mContainerRect.bottom - n4) {
                n8 = this.mContainerRect.bottom - n4 - this.mDialogHeight;
            }
            this.mView.setLeft(n7);
            this.mView.setRight(n7 + this.mDialogWidth);
            this.mView.setTop(n8);
            this.mView.setBottom(n8 + this.mDialogHeight);
            this.mDialogRect = new Rect((int)this.mView.getX(), (int)this.mView.getY(), (int)this.mView.getX() + this.mDialogWidth, (int)this.mView.getY() + this.mDialogHeight);
        }
        this.mView.setRotation(RotationUtil.getAngle(n));
    }

    private boolean isPortrait(int n) {
        if (n == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void coordinatePosition(int n) {
        if (this.mIsTablet) {
            super.coordinatePositionTablet(n);
            return;
        }
        super.coordinatePositionPhone(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void coordinateSize(int n) {
        ViewGroup.LayoutParams layoutParams = this.mView.getLayoutParams();
        int n2 = super.isPortrait(n) ? this.mContainerRect.width() : this.mContainerRect.height();
        int n3 = n2 - this.mMaxHeightMargin;
        if (!(this.mView.computeHeight(1) <= n3 || super.isPortrait(n))) {
            this.mView.setNumColumns(2);
            int n4 = this.mView.getNumRows(n3);
            layoutParams.height = Math.min((int)this.mView.computeMaxHeight(n4), (int)this.mView.computeHeight(2));
            layoutParams.width = this.mView.computeWidth(2);
        } else {
            this.mView.setNumColumns(1);
            int n5 = this.mView.getNumRows(n3);
            layoutParams.height = Math.min((int)this.mView.computeMaxHeight(n5), (int)this.mView.computeHeight(1));
            layoutParams.width = this.mView.computeWidth(1);
        }
        this.mDialogWidth = layoutParams.width;
        this.mDialogHeight = layoutParams.height;
    }

    @Override
    public Rect getDialogRect() {
        return this.mDialogRect;
    }
}

