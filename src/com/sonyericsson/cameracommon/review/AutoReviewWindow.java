/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.ActivityOptions
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnDismissListener
 *  android.content.Intent
 *  android.graphics.Rect
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Message
 *  android.util.AttributeSet
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnKeyListener
 *  android.view.View$OnTouchListener
 *  android.widget.ImageView
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.ref.WeakReference
 *  java.util.List
 */
package com.sonyericsson.cameracommon.review;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.AutoReview;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.cameracommon.launcher.AlbumLauncher;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtilListener;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.OnSelectedReviewMenuButtonListener;
import com.sonyericsson.cameracommon.review.ReviewMenuButton;
import com.sonyericsson.cameracommon.review.ReviewScreen;
import com.sonyericsson.cameracommon.review.ReviewWindowListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.CameraTimer;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class AutoReviewWindow
extends ReviewScreen {
    private static String ACTION_EDIT_HIGH_FRAME_RATE;
    private static final String TAG;
    private ContentResolverUtilListener mCrListener;
    private final DialogInterface.OnDismissListener mDismissListener;
    private long mDuration;
    private View.OnKeyListener mInterceptKeyListener;
    private boolean mIsOpened;
    protected KeyEventTranslator mKeyEventTranslator;
    private ReviewWindowListener mListener;
    private MessagePopup mMessagePopup;
    private final OnSelectedReviewMenuButtonListener mOnSelectListener;
    private CameraTimer mTimer;

    static {
        TAG = AutoReviewWindow.class.getSimpleName();
        ACTION_EDIT_HIGH_FRAME_RATE = "com.sonymobile.moviecreator.intent.action.TIMESHIFT_VIDEO_EDITOR";
    }

    public AutoReviewWindow(Context context) {
        super(context);
        this.mDismissListener = new DismissListener((AutoReviewWindow)this, null);
        this.mOnSelectListener = new OnSelectedListener((AutoReviewWindow)this, null);
        this.mIsOpened = false;
    }

    public AutoReviewWindow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDismissListener = new DismissListener((AutoReviewWindow)this, null);
        this.mOnSelectListener = new OnSelectedListener((AutoReviewWindow)this, null);
        this.mIsOpened = false;
    }

    public AutoReviewWindow(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mDismissListener = new DismissListener((AutoReviewWindow)this, null);
        this.mOnSelectListener = new OnSelectedListener((AutoReviewWindow)this, null);
        this.mIsOpened = false;
    }

    public static boolean isEditorAvailable(Context context, Uri uri, String string) {
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setDataAndType(uri, string);
        intent.setFlags(1);
        return CommonUtility.isActivityAvailable(context, intent);
    }

    public static void launchAlbum(Activity activity, Uri uri, String string) {
        AlbumLauncher.launchAlbum(activity, uri, string, -1, false);
    }

    public static boolean launchEditor(Activity activity, Uri uri, String string) {
        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation((Context)activity, (int)R.anim.edit_activity_fade_in, (int)R.anim.edit_activity_fade_out);
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setDataAndType(uri, string);
        intent.setFlags(1);
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
            activity.startActivity(intent, activityOptions.toBundle());
            return true;
        }
        return false;
    }

    public static boolean launchEditorHighFrameRate(Activity activity, Uri uri, String string) {
        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation((Context)activity, (int)R.anim.edit_activity_fade_in, (int)R.anim.edit_activity_fade_out);
        Intent intent = new Intent(ACTION_EDIT_HIGH_FRAME_RATE);
        intent.setDataAndType(uri, string);
        intent.setFlags(1);
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
            activity.startActivity(intent, activityOptions.toBundle());
            return true;
        }
        return false;
    }

    public static void launchPlayer(Activity activity, Uri uri, String string) {
        AlbumLauncher.launchPlayer(activity, uri, string);
    }

    private boolean transferKeyEvent(int n, KeyEvent keyEvent) {
        View.OnKeyListener onKeyListener = this.mInterceptKeyListener;
        boolean bl = false;
        if (onKeyListener != null) {
            bl = this.mInterceptKeyListener.onKey((View)this, n, keyEvent);
        }
        return bl;
    }

    void backToViewFinder() {
        this.hide();
    }

    ContentResolverUtilListener getContentResolverUtilListener() {
        return this.mCrListener;
    }

    MessagePopup getMessagePopup() {
        return this.mMessagePopup;
    }

    public void hide() {
        this.cancelDialog();
        if (this.mMessagePopup != null) {
            this.mMessagePopup.release();
        }
        this.stopTimer();
        this.clearFocus();
        this.hideScreen();
        if (this.mListener != null) {
            this.mIsOpened = false;
            this.mListener.onReviewWindowClose();
        }
        this.mUri = null;
    }

    public boolean isOpened() {
        return this.mIsOpened;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setBackgroundColor(-16777216);
        this.mPictureImage.setOnTouchListener((View.OnTouchListener)new ReviewScreenListener(null));
        for (ReviewMenuButton reviewMenuButton : this.mButtonList) {
            reviewMenuButton.setReviewScreen((ReviewScreen)this);
            reviewMenuButton.setOnSelectedListener(this.mOnSelectListener);
        }
    }

    protected void onDetachedFromWindow() {
        for (ReviewMenuButton reviewMenuButton : this.mButtonList) {
            reviewMenuButton.setReviewScreen(null);
            reviewMenuButton.setOnSelectedListener(null);
        }
        this.mListener = null;
        this.mCrListener = null;
        super.onDetachedFromWindow();
        this.stopTimer();
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (super.transferKeyEvent(n, keyEvent)) {
            return true;
        }
        KeyEventTranslator.TranslatedKeyCode translatedKeyCode = this.mKeyEventTranslator.translateKeyCode(n);
        switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: {
                if (keyEvent.getRepeatCount() != 0) return true;
                this.backToViewFinder();
                return true;
            }
            case 5: {
                return false;
            }
            case 6: 
            case 7: 
        }
        this.stopTimer();
        return true;
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (super.transferKeyEvent(n, keyEvent)) {
            return true;
        }
        switch (n) {
            default: {
                return false;
            }
            case 4: 
            case 82: 
        }
        this.backToViewFinder();
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean open(Activity activity, Uri uri, String string, Rect rect, int n, int n2, boolean bl, ReviewWindowListener reviewWindowListener, ContentResolverUtilListener contentResolverUtilListener) {
        long l = "video/mp4".equals((Object)string) || "video/3gpp".equals((Object)string) ? -1 : this.mDuration;
        if (l == 0) {
            return false;
        }
        if (this.mAutoReviewRight != null) {
            this.mAutoReviewRight.setVisibility(0);
        }
        this.mListener = reviewWindowListener;
        this.mCrListener = contentResolverUtilListener;
        Uri uri2 = uri;
        String string2 = uri.toString();
        if (string2.startsWith(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString())) {
            uri2 = Uri.parse((String)string2.replaceFirst(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString(), MediaSavingConstants.STANDARD_PHOTO_STORAGE_URI.toString()));
        }
        if (!this.setupScreen(activity, uri2, null, "", string, rect, n, n2, bl)) {
            return false;
        }
        this.show();
        this.startTimer(l);
        if (this.mListener != null) {
            this.mIsOpened = true;
            this.mListener.onReviewWindowOpen();
        }
        return true;
    }

    public boolean open(Activity activity, byte[] arrby, String string, String string2, Rect rect, int n, int n2, boolean bl, ReviewWindowListener reviewWindowListener, ContentResolverUtilListener contentResolverUtilListener) {
        long l = AutoReview.UNLIMITED.getDuration();
        if (this.mAutoReviewRight != null) {
            this.mAutoReviewRight.setVisibility(8);
        }
        this.mListener = reviewWindowListener;
        this.mCrListener = contentResolverUtilListener;
        if (this.setupScreen(activity, null, arrby, string, string2, rect, n, n2, bl)) {
            this.show();
            this.startTimer(l);
            if (this.mListener != null) {
                this.mIsOpened = true;
                this.mListener.onReviewWindowOpen();
            }
            return true;
        }
        return false;
    }

    public void setDuration(long l) {
        this.mDuration = l;
    }

    public void setInterceptKeyListener(View.OnKeyListener onKeyListener) {
        this.mInterceptKeyListener = onKeyListener;
    }

    public void setup(MessagePopup messagePopup, CommonSettings commonSettings) {
        this.setup(messagePopup, new KeyEventTranslator(commonSettings));
    }

    public void setup(MessagePopup messagePopup, KeyEventTranslator keyEventTranslator) {
        this.mMessagePopup = messagePopup;
        this.mKeyEventTranslator = keyEventTranslator;
    }

    public void show() {
        this.showScreen();
        this.requestFocus();
    }

    public void startTimer(long l) {
        this.stopTimer();
        if (l > 0) {
            this.mTimer = new CameraTimer(l, l, (Handler)new ReviewTimerHandler((AutoReviewWindow)this), TAG, 0);
            this.mTimer.start();
        }
    }

    public void stopTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class DismissListener
    implements DialogInterface.OnDismissListener {
        final /* synthetic */ AutoReviewWindow this$0;

        private DismissListener(AutoReviewWindow autoReviewWindow) {
            this.this$0 = autoReviewWindow;
        }

        /* synthetic */ DismissListener(AutoReviewWindow autoReviewWindow,  var2_2) {
            super(autoReviewWindow);
        }

        public void onDismiss(DialogInterface dialogInterface) {
            this.this$0.mMessagePopup.release();
            if (this.this$0.getVisibility() == 0) {
                this.this$0.show();
            }
        }
    }

    private class OnSelectedListener
    implements OnSelectedReviewMenuButtonListener {
        final /* synthetic */ AutoReviewWindow this$0;

        private OnSelectedListener(AutoReviewWindow autoReviewWindow) {
            this.this$0 = autoReviewWindow;
        }

        /* synthetic */ OnSelectedListener(AutoReviewWindow autoReviewWindow,  var2_2) {
            super(autoReviewWindow);
        }

        @Override
        public void onSelected(ReviewMenuButton reviewMenuButton) {
            this.this$0.stopTimer();
            this.this$0.cancelDialog();
        }

        @Override
        public void onSelected(ReviewMenuButton reviewMenuButton, RotatableDialog rotatableDialog) {
            this.this$0.stopTimer();
            rotatableDialog.setOnDismissListener(this.this$0.mDismissListener);
            this.this$0.setCurrentDialog(rotatableDialog);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class ReviewScreenListener
    implements View.OnTouchListener {
        private ReviewScreenListener() {
        }

        /* synthetic */ ReviewScreenListener( var1) {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class ReviewTimerHandler
    extends Handler {
        private final WeakReference<AutoReviewWindow> mWindowRef;

        ReviewTimerHandler(AutoReviewWindow autoReviewWindow) {
            this.mWindowRef = new WeakReference((Object)autoReviewWindow);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 1: {
                    AutoReviewWindow autoReviewWindow = (AutoReviewWindow)this.mWindowRef.get();
                    if (autoReviewWindow == null) return;
                    autoReviewWindow.backToViewFinder();
                    return;
                }
            }
        }
    }

}

