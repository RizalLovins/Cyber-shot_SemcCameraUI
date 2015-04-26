/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.content.DialogInterface$OnKeyListener
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.view.Display
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.Button
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.ScrollView
 *  java.lang.CharSequence
 *  java.lang.Float
 *  java.lang.NullPointerException
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.rotatableview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;

/*
 * Failed to analyse overrides
 */
public class RotatableDialog
implements View.OnAttachStateChangeListener,
View.OnTouchListener {
    public static final String TAG = "RotatableDialog";
    private final int mAnimationsForLand;
    private final int mAnimationsForPort;
    private AlertDialog mDialog;
    private int mDialogWidthForLand;
    private int mDialogWidthForPort;
    private int mDisplayHeight;
    private int mDisplayWidth;
    private int mOrientation = 2;
    private View mScrollableView;
    private Rect mTempRect = new Rect();
    private int mTranslationYForLand;
    private int mTranslationYForPort;
    private Window mWindow;

    protected RotatableDialog(AlertDialog alertDialog) {
        this.mDialog = alertDialog;
        this.mWindow = alertDialog.getWindow();
        this.mAnimationsForLand = R.style.WindowAnimationDeviceDefaultDialogLandscape;
        this.mAnimationsForPort = R.style.WindowAnimationDeviceDefaultDialogPortrait;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void attachScrollableView() {
        View view;
        ScrollView scrollView;
        View view2;
        if (this.mScrollableView == null || (view = this.mWindow.findViewById(16908299)) == null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(this.mWindow.getContext());
        frameLayout.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        frameLayout.addView(this.mScrollableView, view.getLayoutParams().width, view.getLayoutParams().height);
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof LinearLayout) {
            scrollView = (ScrollView)viewParent.getParent();
            view2 = (View)viewParent;
        } else {
            scrollView = (ScrollView)viewParent;
            view2 = view;
        }
        scrollView.removeView(view2);
        scrollView.addView((View)frameLayout, -1, -1);
    }

    private int calculateOutValue(int n, int n2, int n3) {
        if (n < n2) {
            return n - n2;
        }
        if (n3 < n) {
            return n - n3;
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void initialize() {
        ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        viewGroup.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        viewGroup.setOnTouchListener((View.OnTouchListener)this);
        this.setWindowAnimations(this.mOrientation);
        Rect rect = new Rect();
        this.mWindow.getWindowManager().getDefaultDisplay().getRectSize(rect);
        if (rect.width() > rect.height()) {
            this.mDisplayWidth = rect.width();
            this.mDisplayHeight = rect.height();
        } else {
            this.mDisplayWidth = rect.height();
            this.mDisplayHeight = rect.width();
        }
        this.mTranslationYForLand = 0;
        this.mTranslationYForPort = (- this.mDisplayWidth - this.mDisplayHeight) / 2;
        try {
            Resources resources = this.mDialog.getContext().getResources();
            String string = resources.getString(17104899);
            String string2 = resources.getString(17104900);
            String string3 = string.replace((CharSequence)"%", (CharSequence)"");
            String string4 = string2.replace((CharSequence)"%", (CharSequence)"");
            float f = Float.parseFloat((String)string3) / 100.0f;
            float f2 = Float.parseFloat((String)string4) / 100.0f;
            this.mDialogWidthForLand = (int)(f * (float)this.mDisplayWidth);
            this.mDialogWidthForPort = (int)(f2 * (float)this.mDisplayHeight);
        }
        catch (Resources.NotFoundException var5_10) {
            var5_10.printStackTrace();
        }
        catch (NullPointerException var4_11) {
            var4_11.printStackTrace();
        }
        catch (NumberFormatException var3_12) {
            var3_12.printStackTrace();
        }
        this.attachScrollableView();
    }

    private void release() {
        ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        viewGroup.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        viewGroup.setOnTouchListener(null);
        this.mDialog = null;
        this.mWindow = null;
        this.mScrollableView = null;
    }

    private void setWindowAnimations(int n) {
        if (n == 1) {
            this.mWindow.setWindowAnimations(this.mAnimationsForPort);
            return;
        }
        this.mWindow.setWindowAnimations(this.mAnimationsForLand);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayout(int n) {
        if (!this.isShowing()) {
            return;
        }
        super.setWindowAnimations(n);
        ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams)viewGroup.getLayoutParams();
        View view = viewGroup.getChildAt(0);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams)view.getLayoutParams();
        layoutParams2.height = -2;
        layoutParams2.gravity = 17;
        layoutParams.width = this.mDisplayWidth;
        if (n == 1) {
            view.setRotation(270.0f);
            view.setTranslationY((float)this.mTranslationYForPort);
            layoutParams2.width = this.mDialogWidthForPort;
            layoutParams.height = this.mDisplayWidth;
        } else {
            view.setRotation(0.0f);
            view.setTranslationY((float)this.mTranslationYForLand);
            layoutParams2.width = this.mDialogWidthForLand;
            layoutParams.height = this.mDisplayHeight;
        }
        this.mWindow.getWindowManager().updateViewLayout((View)viewGroup, (ViewGroup.LayoutParams)layoutParams);
        view.setLayoutParams((ViewGroup.LayoutParams)layoutParams2);
    }

    public void cancel() {
        if (this.mDialog != null) {
            this.mDialog.cancel();
        }
    }

    public void dismiss() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
        }
    }

    public void hide() {
        if (this.mDialog != null) {
            this.mDialog.hide();
        }
    }

    public boolean isShowing() {
        if (this.mDialog != null) {
            return this.mDialog.isShowing();
        }
        return false;
    }

    public boolean isShown(DialogInterface dialogInterface) {
        if (this.mDialog == dialogInterface) {
            return true;
        }
        return false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        ViewGroup viewGroup = (ViewGroup)view;
        viewGroup.getChildAt(0).getGlobalVisibleRect(this.mTempRect);
        int n = super.calculateOutValue((int)motionEvent.getRawX(), this.mTempRect.left, this.mTempRect.right);
        int n2 = super.calculateOutValue((int)motionEvent.getRawY(), this.mTempRect.top, this.mTempRect.bottom);
        if (n != 0 || n2 != 0) {
            if (n > 0) {
                n+=viewGroup.getWidth();
            }
            if (n2 > 0) {
                n2+=viewGroup.getHeight();
            }
            MotionEvent motionEvent2 = MotionEvent.obtain((MotionEvent)motionEvent);
            motionEvent2.setLocation((float)n, (float)n2);
            if (this.isShowing()) {
                this.mDialog.onTouchEvent(motionEvent2);
            }
            motionEvent2.recycle();
        }
        return false;
    }

    public void onViewAttachedToWindow(View view) {
        ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        viewGroup.findViewById(16908290).setBackground(viewGroup.getBackground());
        viewGroup.setBackground(null);
        super.updateLayout(this.mOrientation);
    }

    public void onViewDetachedFromWindow(View view) {
        super.release();
    }

    public void setCancelable(boolean bl) {
        if (this.mDialog != null) {
            this.mDialog.setCancelable(bl);
        }
    }

    public void setCanceledOnTouchOutside(boolean bl) {
        if (this.mDialog != null) {
            this.mDialog.setCanceledOnTouchOutside(bl);
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        if (this.mDialog != null) {
            this.mDialog.setOnCancelListener(onCancelListener);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        if (this.mDialog != null) {
            this.mDialog.setOnDismissListener(onDismissListener);
        }
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        if (this.mDialog != null) {
            this.mDialog.setOnKeyListener(onKeyListener);
        }
    }

    public void setOrientation(int n) {
        this.mOrientation = n;
        super.updateLayout(n);
    }

    public void setPositiveButtonEnabled(boolean bl) {
        Button button;
        if (this.mDialog != null && (button = this.mDialog.getButton(-1)) != null) {
            button.setEnabled(bl);
        }
    }

    public void setViewAsScrollable(View view) {
        this.mScrollableView = view;
        if (this.mScrollableView != null) {
            this.mDialog.setMessage((CharSequence)"");
        }
    }

    public void show() {
        if (this.mDialog != null) {
            this.mDialog.show();
            this.initialize();
        }
    }

    /*
     * Failed to analyse overrides
     */
    public static class Builder
    extends AlertDialog.Builder {
        private MessagePopup.Cancelable mIsCancelable;
        private MessagePopup.Cancelable mIsCancelableOnTouchOutside;
        protected View mScrollableView = null;
        private int mSensorOrientation;

        public Builder(Context context) {
            super(context);
        }

        /*
         * Enabled aggressive block sorting
         */
        public RotatableDialog createRotatableDialog() {
            boolean bl = true;
            RotatableDialog rotatableDialog = new RotatableDialog(super.create());
            rotatableDialog.setViewAsScrollable(this.mScrollableView);
            if (this.mIsCancelable != MessagePopup.Cancelable.UseDefault) {
                boolean bl2 = this.mIsCancelable == MessagePopup.Cancelable.True ? bl : false;
                rotatableDialog.setCancelable(bl2);
            }
            if (this.mIsCancelableOnTouchOutside != MessagePopup.Cancelable.UseDefault) {
                if (this.mIsCancelableOnTouchOutside != MessagePopup.Cancelable.True) {
                    bl = false;
                }
                rotatableDialog.setCanceledOnTouchOutside(bl);
            }
            rotatableDialog.setOrientation(this.mSensorOrientation);
            return rotatableDialog;
        }

        public Builder setAlertIcon() {
            super.setIcon(17301543);
            return this;
        }

        public Builder setCancelable(MessagePopup.Cancelable cancelable, MessagePopup.Cancelable cancelable2) {
            this.mIsCancelable = cancelable;
            this.mIsCancelableOnTouchOutside = cancelable2;
            return this;
        }

        public Builder setOrientation(int n) {
            this.mSensorOrientation = n;
            return this;
        }

        public Builder setViewAsScrollable(View view) {
            this.mScrollableView = view;
            return this;
        }
    }

}

