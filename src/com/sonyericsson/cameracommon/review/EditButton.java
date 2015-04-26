/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ActivityNotFoundException
 *  android.content.Context
 *  android.net.Uri
 *  android.util.AttributeSet
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.sonyericsson.cameracommon.review;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import com.sonyericsson.cameracommon.review.AutoReviewWindow;
import com.sonyericsson.cameracommon.review.ReviewMenuButton;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.CameraLogger;

/*
 * Failed to analyse overrides
 */
public class EditButton
extends ReviewMenuButton {
    private static final String TAG = EditButton.class.getSimpleName();

    public EditButton(Context context) {
        super(context);
    }

    public EditButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EditButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public RotatableDialog select() {
        try {
            AutoReviewWindow.launchEditor((Activity)this.getContext(), this.mReviewScreen.mUri, this.mReviewScreen.mMime);
            do {
                return null;
                break;
            } while (true);
        }
        catch (ActivityNotFoundException var1_1) {
            CameraLogger.e(TAG, "launchEditor: failed.", (Throwable)var1_1);
            return null;
        }
    }
}

