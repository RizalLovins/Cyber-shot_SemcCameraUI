/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.Interpolator
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.capturefeedback.animation;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas;

public class CaptureFeedbackAnimationFactory {
    private static final String TAG = CaptureFeedbackAnimationFactory.class.getSimpleName();

    public static CaptureFeedbackAnimation createDefaultAnimation() {
        return new DefaultFeedbackAnimation(null);
    }

    private static class DefaultFeedbackAnimation
    implements CaptureFeedbackAnimation {
        private static final float BLUE = 0.0f;
        private static final long DURATION_MILLIS = 200;
        private static final float END_ALPHA = 0.0f;
        private static final float GREEN = 0.0f;
        private static final float RED = 0.0f;
        private static final float START_ALPHA = 1.0f;
        private final Interpolator mInterpolator;

        private DefaultFeedbackAnimation() {
            this.mInterpolator = new AccelerateInterpolator();
        }

        /* synthetic */ DefaultFeedbackAnimation( var1) {
        }

        @Override
        public boolean draw(CaptureFeedbackAnimationCanvas captureFeedbackAnimationCanvas, long l) {
            float f = (float)l / 200.0f;
            float f2 = 1.0f + -1.0f * Math.min((float)1.0f, (float)this.mInterpolator.getInterpolation(f));
            if ((double)f > 1.0) {
                return false;
            }
            captureFeedbackAnimationCanvas.drawColor(f2, 0.0f, 0.0f, 0.0f);
            return true;
        }
    }

}

