/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.ImageView
 */
package com.sonyericsson.cameracommon.viewfinder.recordingindicator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.CommonUtility;

/*
 * Failed to analyse overrides
 */
public class RecordingProgressBar
extends ImageView {
    private int mProgressBarWidth = 0;
    private Drawable mProgressIcon;
    private int mProgressRatio = 0;

    public RecordingProgressBar(Context context) {
        super(context);
    }

    public RecordingProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RecordingProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public int getProgress() {
        return this.mProgressRatio;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = this.getPaddingTop() + this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
        if (CommonUtility.isMirroringRequired(this.getContext())) {
            int n2 = this.mProgressBarWidth - this.getPaddingRight() - this.mProgressRatio;
            this.mProgressIcon.setBounds(n2, this.getPaddingTop(), this.mProgressBarWidth - this.getPaddingRight(), n);
        } else {
            int n3 = this.getPaddingLeft() + this.mProgressRatio;
            this.mProgressIcon.setBounds(this.getPaddingLeft(), this.getPaddingTop(), n3, n);
        }
        this.mProgressIcon.draw(canvas);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressIcon = this.getResources().getDrawable(R.drawable.cam_video_recording_progress_indicator_icn);
        this.mProgressBarWidth = this.getResources().getDimensionPixelSize(R.dimen.rec_constraint_progress_width);
    }

    public void setProgress(int n, int n2) {
        int n3 = 0;
        if (n2 != 0) {
            n3 = (int)((double)n / (double)n2 * (double)(this.mProgressBarWidth - this.getPaddingLeft() - this.getPaddingRight()));
        }
        this.mProgressRatio = n3;
        this.invalidate();
    }
}

