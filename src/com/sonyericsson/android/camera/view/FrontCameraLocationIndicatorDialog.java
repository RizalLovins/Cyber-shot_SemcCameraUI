/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnKeyListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.Button
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.cameracommon.utility.RotationUtil;

public class FrontCameraLocationIndicatorDialog {
    private static final String TAG = FrontCameraLocationIndicatorDialog.class.getSimpleName();
    private final ExtendedActivity mActivity;
    private final View.OnKeyListener mBackgroundKeyListener;
    private final View.OnTouchListener mBackgroundTouchListener;
    private final OnCloseListener mCloseListener;
    private final View.OnTouchListener mDialogTouchListener;
    private final View.OnClickListener mOkButtonClickListener;
    private int mOrientation;
    private final FrameLayout mParent;

    public FrontCameraLocationIndicatorDialog(FrameLayout frameLayout, OnCloseListener onCloseListener) {
        this.mDialogTouchListener = new View.OnTouchListener(){

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        };
        this.mOkButtonClickListener = new View.OnClickListener(){

            public void onClick(View view) {
                FrontCameraLocationIndicatorDialog.this.close();
                FrontCameraLocationIndicatorDialog.this.mCloseListener.onClose(FrontCameraLocationIndicatorDialog.this);
            }
        };
        this.mBackgroundTouchListener = new View.OnTouchListener(){

            public boolean onTouch(View view, MotionEvent motionEvent) {
                FrontCameraLocationIndicatorDialog.this.close();
                FrontCameraLocationIndicatorDialog.this.mCloseListener.onClose(FrontCameraLocationIndicatorDialog.this);
                return true;
            }
        };
        this.mBackgroundKeyListener = new View.OnKeyListener(){

            public boolean onKey(View view, int n, KeyEvent keyEvent) {
                if (keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 4) {
                    FrontCameraLocationIndicatorDialog.this.close();
                    FrontCameraLocationIndicatorDialog.this.mCloseListener.onClose(FrontCameraLocationIndicatorDialog.this);
                    return true;
                }
                return false;
            }
        };
        this.mParent = frameLayout;
        this.mCloseListener = onCloseListener;
        super.updateLayoutSize();
        this.mActivity = (ExtendedActivity)this.mParent.getContext();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applySensorOrientation() {
        View view = this.getLayout();
        if (view != null) {
            float f = RotationUtil.getAngle(this.mOrientation);
            ImageView imageView = (ImageView)view.findViewById(2131689547);
            int n = f == 0.0f ? 2130837519 : 2130837520;
            imageView.setImageResource(n);
            View view2 = view.findViewById(2131689546);
            view2.setRotation(f);
            ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
            int n2 = f == 0.0f ? 2131296437 : 2131296436;
            layoutParams.width = this.getDimen(n2);
            view2.requestLayout();
            view.requestLayout();
        }
    }

    private void attatchView() {
        View view = ((Activity)this.mParent.getContext()).getLayoutInflater().inflate(2130903059, null);
        view.setOnTouchListener(this.mBackgroundTouchListener);
        view.setOnKeyListener(this.mBackgroundKeyListener);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        this.mParent.addView(view);
        ((Button)view.findViewById(2131689549)).setOnClickListener(this.mOkButtonClickListener);
        view.findViewById(2131689546).setOnTouchListener(this.mDialogTouchListener);
    }

    private void detatchView() {
        View view = this.getLayout();
        if (view != null) {
            this.mParent.removeView(view);
        }
    }

    private int getDimen(int n) {
        return this.mParent.getContext().getResources().getDimensionPixelSize(n);
    }

    private View getLayout() {
        return this.mParent.findViewById(2131689545);
    }

    private boolean isShown() {
        if (this.getLayout() != null) {
            return true;
        }
        return false;
    }

    private void updateLayoutSize() {
        View view = this.getLayout();
        if (view != null) {
            int n;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            layoutParams.width = n = Math.max((int)this.mParent.getMeasuredWidth(), (int)this.mParent.getMeasuredHeight());
            layoutParams.height = n;
            layoutParams.gravity = 17;
            view.requestLayout();
        }
    }

    public void close() {
        this.detatchView();
        if (this.mActivity != null) {
            this.mActivity.enableAutoOffTimer();
        }
    }

    public void setSensorOrientation(int n) {
        if (this.mOrientation != n) {
            this.mOrientation = n;
            super.applySensorOrientation();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void show() {
        if (this.isShown()) {
            return;
        }
        this.attatchView();
        this.applySensorOrientation();
        if (this.mActivity == null) return;
        this.mActivity.disableAutoOffTimer();
    }

    public static interface OnCloseListener {
        public void onClose(FrontCameraLocationIndicatorDialog var1);
    }

}

