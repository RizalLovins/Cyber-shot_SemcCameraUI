/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;

/*
 * Failed to analyse overrides
 */
public class Indicators
extends RelativeLayout {
    public static final String TAG = "Indicators";
    private boolean mAnimating = false;
    private Indicator mCameraSelfTimer;
    private ImageView mPhotoSmileCapture;
    private Indicator mVideoSelfTimer;
    private ImageView mVideoSmileCapture;

    public Indicators(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setSmileCaptureVisible(ImageView imageView, boolean bl) {
        if (bl) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(8);
        }
        this.invalidate();
    }

    public void hideSelfTimerIndicator() {
        this.setPhotoSelfTimerCaptureVisible(false);
        this.setVideoSelfTimerCaptureVisible(false);
    }

    public void onCameraSelfTimerEnd() {
        this.setPhotoSelfTimerCaptureVisible(true);
        this.setVideoSelfTimerCaptureVisible(true);
        this.updateLayout();
    }

    public void onCameraSelftimerSettingsChanged(CapturingMode capturingMode, boolean bl, int n) {
        this.onCameraSelfTimerEnd();
        if (capturingMode == CapturingMode.NORMAL || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.SCENE_RECOGNITION) {
            if (bl) {
                this.mCameraSelfTimer.set(true);
                this.mCameraSelfTimer.setImageResource(n);
                this.setPhotoSelfTimerCaptureVisible(true);
                return;
            }
            this.mCameraSelfTimer.set(false);
            this.setPhotoSelfTimerCaptureVisible(false);
            return;
        }
        this.mCameraSelfTimer.set(false);
        this.setPhotoSelfTimerCaptureVisible(false);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mCameraSelfTimer = new Indicator((ImageView)this.findViewById(2131689503));
        this.mVideoSelfTimer = new Indicator((ImageView)this.findViewById(2131689504));
        this.mPhotoSmileCapture = (ImageView)this.findViewById(2131689505);
        this.mVideoSmileCapture = (ImageView)this.findViewById(2131689506);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onPhotoSmileCaptureSettingsChanged(boolean bl, int n) {
        if (bl) {
            this.mPhotoSmileCapture.setImageResource(n);
            this.setPhotoSmileCaptureVisible(true);
        } else {
            this.setPhotoSmileCaptureVisible(false);
        }
        this.mPhotoSmileCapture.invalidate();
    }

    public void onVideoSelfTimerEnd() {
        this.setPhotoSelfTimerCaptureVisible(true);
        this.setVideoSelfTimerCaptureVisible(true);
        this.updateLayout();
    }

    public void onVideoSelftimerSettingsChanged(CapturingMode capturingMode, boolean bl, int n) {
        this.onVideoSelfTimerEnd();
        if (capturingMode == CapturingMode.VIDEO || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.SCENE_RECOGNITION) {
            if (bl) {
                this.mVideoSelfTimer.set(true);
                this.mVideoSelfTimer.setImageResource(n);
                this.setVideoSelfTimerCaptureVisible(true);
                return;
            }
            this.mVideoSelfTimer.set(false);
            this.setVideoSelfTimerCaptureVisible(false);
            return;
        }
        this.mVideoSelfTimer.set(false);
        this.setVideoSelfTimerCaptureVisible(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onVideoSmileCaptureSettingsChanged(boolean bl, int n) {
        if (bl) {
            this.mVideoSmileCapture.setImageResource(n);
            this.setVideoSmileCaptureVisible(true);
        } else {
            this.setVideoSmileCaptureVisible(false);
        }
        this.mVideoSmileCapture.invalidate();
    }

    protected void setAnimationStatus(boolean bl) {
        if (this.mAnimating == bl) {
            return;
        }
        this.mAnimating = bl;
    }

    public void setPhotoSelfTimerCaptureVisible(boolean bl) {
        if (bl) {
            this.mCameraSelfTimer.show();
            return;
        }
        this.mCameraSelfTimer.hide();
    }

    public void setPhotoSmileCaptureVisible(boolean bl) {
        super.setSmileCaptureVisible(this.mPhotoSmileCapture, bl);
    }

    public void setSensorOrientation(int n) {
        float f = RotationUtil.getAngle(n);
        this.mCameraSelfTimer.setSensorOrientation(n);
        this.mVideoSelfTimer.setSensorOrientation(n);
        this.mPhotoSmileCapture.setRotation(f);
        this.mVideoSmileCapture.setRotation(f);
    }

    public void setVideoSelfTimerCaptureVisible(boolean bl) {
        if (bl) {
            this.mVideoSelfTimer.show();
            return;
        }
        this.mVideoSelfTimer.hide();
    }

    public void setVideoSmileCaptureVisible(boolean bl) {
        super.setSmileCaptureVisible(this.mVideoSmileCapture, bl);
    }

    public void showSelfTimerIndicator() {
        this.setPhotoSelfTimerCaptureVisible(true);
        this.setVideoSelfTimerCaptureVisible(true);
    }

    protected void updateLayout() {
        this.requestLayout();
        this.invalidate();
    }
}

