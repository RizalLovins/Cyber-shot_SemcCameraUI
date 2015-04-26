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
public class ViewButton
extends ReviewMenuButton {
    private static final String TAG = ViewButton.class.getSimpleName();

    public ViewButton(Context context) {
        super(context);
    }

    public ViewButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ViewButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public RotatableDialog select() {
        try {
            AutoReviewWindow.launchAlbum((Activity)this.getContext(), this.mReviewScreen.mUri, this.mReviewScreen.mMime);
            do {
                return null;
                break;
            } while (true);
        }
        catch (ActivityNotFoundException var1_1) {
            CameraLogger.e(TAG, "launchAlbum: failed.", (Throwable)var1_1);
            return null;
        }
    }
}

