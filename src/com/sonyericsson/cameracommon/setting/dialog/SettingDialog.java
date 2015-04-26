/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.widget.RelativeLayout
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogStateListener;
import com.sonyericsson.cameracommon.setting.layoutcoordinator.LayoutCoordinator;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;

/*
 * Failed to analyse overrides
 */
public abstract class SettingDialog
extends RelativeLayout
implements SettingDialogInterface {
    private Animation mCloseAnimation;
    private boolean mIsNested = false;
    private LayoutCoordinator mLayoutCoordinator;
    protected int mOrientation = 2;
    private ViewGroup mParentView;
    private SettingDialogStateListener mStateListener;

    public SettingDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void cancelAnimation() {
        if (this.mParentView == null) {
            return;
        }
        if (this.mParentView.getAnimation() == null) return;
        this.mParentView.setAnimation(null);
        if (this.mCloseAnimation == null) return;
        this.mCloseAnimation.setAnimationListener(null);
    }

    public void close() {
        Handler handler;
        this.cancelAnimation();
        if (this.mCloseAnimation != null) {
            this.startAnimation(this.mCloseAnimation);
        }
        if ((handler = this.getHandler()) != null) {
            handler.post((Runnable)new Runnable(){

                public void run() {
                    if (SettingDialog.this.mParentView != null) {
                        SettingDialog.this.mParentView.removeView((View)SettingDialog.this);
                    }
                }
            });
        }
        if (this.mStateListener != null) {
            this.mStateListener.onClosed();
        }
    }

    public LayoutCoordinator getLayoutCoordinator() {
        return this.mLayoutCoordinator;
    }

    public boolean hitTest(int n, int n2) {
        Rect rect = new Rect();
        if (this.getGlobalVisibleRect(rect)) {
            return rect.contains(n, n2);
        }
        return false;
    }

    public boolean isNested() {
        return this.mIsNested;
    }

    protected void notifyItemSelected(SettingItem settingItem) {
        if (!settingItem.isSelectable()) {
            return;
        }
        settingItem.select();
        this.close();
    }

    protected void notifyItemUpdated(SettingItem settingItem) {
        settingItem.select();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.isEnabled()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        if (this.mLayoutCoordinator != null) {
            this.mLayoutCoordinator.coordinatePosition(this.mOrientation);
        }
    }

    public void open(ViewGroup viewGroup) {
        this.open(viewGroup, false);
    }

    public void open(ViewGroup viewGroup, boolean bl) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view shouldn't be null");
        }
        this.mIsNested = bl;
        this.mParentView = viewGroup;
        this.mParentView.addView((View)this);
        if (this.mStateListener != null) {
            this.mStateListener.onOpened();
        }
    }

    public abstract void setAdapter(SettingAdapter var1);

    public void setCloseAnimation(Animation animation) {
        this.mCloseAnimation = animation;
    }

    public void setLayoutCoordinator(LayoutCoordinator layoutCoordinator) {
        this.mLayoutCoordinator = layoutCoordinator;
    }

    public void setSensorOrientation(int n) {
        this.mOrientation = n;
        if (this.mLayoutCoordinator != null) {
            this.mLayoutCoordinator.coordinateSize(n);
        }
    }

    public void setStateListener(SettingDialogStateListener settingDialogStateListener) {
        this.mStateListener = settingDialogStateListener;
    }

    protected void startAnimation() {
        if (this.mCloseAnimation == null) {
            return;
        }
        this.cancelAnimation();
        this.mCloseAnimation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                SettingDialog.this.close();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.mParentView.startAnimation(this.mCloseAnimation);
    }

    protected void updateSelectItem(SettingAdapter settingAdapter, SettingItem settingItem) {
        for (int i = 0; i < settingAdapter.getCount(); ++i) {
            ((SettingItem)settingAdapter.getItem(i)).setSelected(false);
        }
        settingItem.setSelected(true);
    }

}

