/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.net.Uri
 *  android.util.AttributeSet
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.review;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import com.sonyericsson.cameracommon.review.AutoReviewWindow;
import com.sonyericsson.cameracommon.review.ReviewMenuButton;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;

/*
 * Failed to analyse overrides
 */
public class PlayButton
extends ReviewMenuButton {
    public PlayButton(Context context) {
        super(context);
    }

    public PlayButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PlayButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public RotatableDialog select() {
        AutoReviewWindow.launchPlayer((Activity)this.getContext(), this.mReviewScreen.mUri, this.mReviewScreen.mMime);
        return null;
    }
}

