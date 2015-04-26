/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialog;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/*
 * Failed to analyse overrides
 */
public class SettingControlDialog
extends SettingDialog {
    private static final String TAG = SettingControlDialog.class.getSimpleName();
    private final int CONTROL_DIALOG_LEFT_PADDING;
    private SettingAdapter mAdapter;
    private Drawable mBackground;
    private LinearLayout mItems;

    public SettingControlDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.CONTROL_DIALOG_LEFT_PADDING = super.getPixel(R.dimen.control_dialog_left_padding);
    }

    private Rect computeBackgroundRectBeforeRotation() {
        if (this.mOrientation == 1) {
            Rect rect = new Rect(0, 0, this.getMeasuredHeight(), this.getMeasuredWidth());
            rect.offset((- rect.width() - this.getMeasuredWidth()) / 2, (- rect.height() - this.getMeasuredHeight()) / 2);
            return rect;
        }
        return new Rect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    private void drawBackground(Canvas canvas) {
        if (this.mBackground == null) {
            return;
        }
        canvas.rotate(RotationUtil.getAngle(this.mOrientation), (float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2));
        this.mBackground.setBounds(super.computeBackgroundRectBeforeRotation());
        this.mBackground.draw(canvas);
    }

    private int getPixel(int n) {
        return this.getResources().getDimensionPixelSize(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateItems() {
        for (int i = 0; i < this.mItems.getChildCount(); ++i) {
            View view = this.mItems.getChildAt(i);
            if (!(view.getTag() instanceof SettingItem)) continue;
            if (((SettingItem)view.getTag()).isSelectable()) {
                view.setVisibility(0);
                continue;
            }
            view.setVisibility(8);
        }
    }

    public int getControlDialogLeftPadding() {
        return this.CONTROL_DIALOG_LEFT_PADDING;
    }

    public boolean getSelectedItemRect(Rect rect) {
        return false;
    }

    protected void onAttachedToWindow() {
        this.mBackground = this.getResources().getDrawable(R.drawable.cam_shortcut_dialog_background_icn);
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        this.mBackground = null;
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        canvas.save();
        super.drawBackground(canvas);
        canvas.restore();
        super.onDraw(canvas);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setWillNotDraw(false);
        this.mItems = (LinearLayout)this.findViewById(R.id.dialog_items);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setAdapter(SettingAdapter settingAdapter) {
        this.mAdapter = settingAdapter;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        for (int i = 0; i < this.mAdapter.getCount(); ++i) {
            if (i < this.mItems.getChildCount()) {
                View view;
                View view2 = this.mItems.getChildAt(i);
                if (view2 == (view = this.mAdapter.getView(i, view2, (ViewGroup)this.mItems))) continue;
                this.mItems.removeViewAt(i);
                this.mItems.removeView(view);
                this.mItems.addView(view, i, (ViewGroup.LayoutParams)layoutParams);
                continue;
            }
            this.mItems.addView(this.mAdapter.getView(i, null, (ViewGroup)this.mItems), (ViewGroup.LayoutParams)layoutParams);
        }
        do {
            if (this.mItems.getChildCount() <= this.mAdapter.getCount()) {
                super.updateItems();
                return;
            }
            this.mItems.removeViewAt(-1 + this.mItems.getChildCount());
        } while (true);
    }

    public void setSensorOrientation(int n) {
        this.requestLayout();
        for (int i = 0; i < this.mItems.getChildCount(); ++i) {
            if (!(this.mItems.getChildAt(i).getTag() instanceof SettingDialogItem)) continue;
            ((SettingDialogItem)this.mItems.getChildAt(i).getTag()).setUiOrientation(n);
        }
        super.setSensorOrientation(n);
    }
}

