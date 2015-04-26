/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.ImageView
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.review;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.OnSelectedReviewMenuButtonListener;
import com.sonyericsson.cameracommon.review.ReviewScreen;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;

/*
 * Failed to analyse overrides
 */
public abstract class ReviewMenuButton
extends ImageView {
    private static final String TAG = ReviewMenuButton.class.getSimpleName();
    private final View.OnClickListener mOnClickListener;
    private OnSelectedReviewMenuButtonListener mOnSelectedListener;
    protected ReviewScreen mReviewScreen;

    public ReviewMenuButton(Context context) {
        super(context);
        this.mOnClickListener = new View.OnClickListener(){

            public void onClick(View view) {
                if (ReviewMenuButton.this.getVisibility() == 0) {
                    RotatableDialog rotatableDialog = ReviewMenuButton.this.select();
                    if (rotatableDialog != null) {
                        ReviewMenuButton.this.notifySelected(rotatableDialog);
                    }
                } else {
                    return;
                }
                ReviewMenuButton.this.notifySelected();
            }
        };
    }

    public ReviewMenuButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnClickListener = new ;
    }

    public ReviewMenuButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mOnClickListener = new ;
    }

    private void notifySelected() {
        if (this.mOnSelectedListener != null) {
            this.mOnSelectedListener.onSelected(this);
        }
    }

    private void notifySelected(RotatableDialog rotatableDialog) {
        if (this.mOnSelectedListener != null) {
            this.mOnSelectedListener.onSelected((ReviewMenuButton)this, rotatableDialog);
        }
    }

    protected MessagePopup getMessagePopup() {
        return this.mReviewScreen.getMessagePopup();
    }

    protected void onAttachedToWindow() {
        this.setOnClickListener(this.mOnClickListener);
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        this.setOnClickListener(null);
        super.onDetachedFromWindow();
    }

    protected abstract RotatableDialog select();

    public void setOnSelectedListener(OnSelectedReviewMenuButtonListener onSelectedReviewMenuButtonListener) {
        this.mOnSelectedListener = onSelectedReviewMenuButtonListener;
    }

    public void setReviewScreen(ReviewScreen reviewScreen) {
        this.mReviewScreen = reviewScreen;
    }

}

