/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.view.View
 *  java.lang.Math
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.layoutcoordinator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogBasic;
import com.sonyericsson.cameracommon.setting.layoutcoordinator.LayoutCoordinateUtil;
import com.sonyericsson.cameracommon.setting.layoutcoordinator.LayoutCoordinator;
import com.sonyericsson.cameracommon.setting.layoutcoordinator.MenuDialogRectCalculator;
import com.sonyericsson.cameracommon.utility.CommonUtility;

public class SecondLayerLayoutCoordinator
implements LayoutCoordinator {
    private final Rect mAnchor;
    private final Rect mBounds;
    private int mDialogHeight;
    public Rect mDialogRect;
    private int mDialogWidth;
    private final MenuDialogRectCalculator mMenuDialogRectCalculator;
    private final int mTopMargin;
    private final SettingDialogBasic mView;

    public SecondLayerLayoutCoordinator(SettingDialogBasic settingDialogBasic, Rect rect, Rect rect2, int n, int n2) {
        this.mView = settingDialogBasic;
        this.mBounds = rect;
        this.mAnchor = rect2;
        this.mMenuDialogRectCalculator = new MenuDialogRectCalculator(settingDialogBasic.getContext(), rect, n, n2);
        this.mTopMargin = settingDialogBasic.getContext().getResources().getDimensionPixelSize(R.dimen.second_layer_dialog_margin_top);
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
    @Override
    public void coordinatePosition(int n) {
        int n2 = this.mMenuDialogRectCalculator.computeWidth(n);
        int n3 = this.mMenuDialogRectCalculator.computeHeight(n);
        int n4 = CommonUtility.isMirroringRequired(this.mView.getContext()) ? this.mAnchor.left : this.mAnchor.right - this.mDialogWidth;
        int n5 = this.mAnchor.bottom + this.mTopMargin;
        Rect rect = new Rect(n4, n5, n4 + this.mDialogWidth, n5 + this.mDialogHeight);
        Rect rect2 = new Rect(0, 0, n2, n3);
        Point point = this.mMenuDialogRectCalculator.computePosition(n);
        if (super.isPortrait(n)) {
            if (rect.bottom + point.x > this.mBounds.right) {
                point.x = this.mBounds.right - rect.bottom;
            }
        } else if (rect.bottom + point.y > this.mBounds.bottom) {
            point.y = this.mBounds.bottom - rect.bottom;
        }
        this.mDialogRect = LayoutCoordinateUtil.coodinatePosition(n, (View)this.mView, rect, rect2, point);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void coordinateSize(int n) {
        int n2 = super.isPortrait(n) ? this.mBounds.width() : this.mBounds.height();
        this.mView.setNumColumns(1);
        int n3 = this.mView.getNumRows(n2);
        this.mDialogWidth = this.mView.computeWidth(1);
        this.mDialogHeight = Math.min((int)this.mView.computeMaxHeight(n3), (int)this.mView.computeHeight(1));
        this.mView.getLayoutParams().width = this.mDialogWidth;
        this.mView.getLayoutParams().height = this.mDialogHeight;
    }

    @Override
    public Rect getDialogRect() {
        return this.mDialogRect;
    }
}

