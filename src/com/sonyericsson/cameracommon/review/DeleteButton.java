/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.net.Uri
 *  android.util.AttributeSet
 */
package com.sonyericsson.cameracommon.review;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.AttributeSet;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtil;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtilListener;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.ReviewMenuButton;
import com.sonyericsson.cameracommon.review.ReviewScreen;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;

/*
 * Failed to analyse overrides
 */
public class DeleteButton
extends ReviewMenuButton
implements DialogInterface.OnClickListener {
    public DeleteButton(Context context) {
        super(context);
    }

    public DeleteButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DeleteButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public void onClick(DialogInterface dialogInterface, int n) {
        ContentResolverUtil.executeDeteleTask(this.getContext(), this.mReviewScreen.mUri, this.mReviewScreen.mHasMpo, this.mReviewScreen.getContentResolverUtilListener());
        this.mReviewScreen.backToViewFinder();
    }

    public RotatableDialog select() {
        return this.getMessagePopup().showOkAndCancel(-1, R.string.cam_strings_file_delete_confirm_txt, false, R.string.cam_strings_file_delete_confirm_title_txt, R.string.cam_strings_cancel_txt, (DialogInterface.OnClickListener)this, null, null);
    }
}

