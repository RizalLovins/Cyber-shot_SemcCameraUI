/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.SurfaceTexture
 *  android.view.TextureView
 *  android.view.TextureView$SurfaceTextureListener
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.concurrent.Executors
 *  java.util.concurrent.ScheduledExecutorService
 *  java.util.concurrent.ScheduledFuture
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.TimeUnit
 */
package com.sonyericsson.cameracommon.capturefeedback.contextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas;
import com.sonymobile.cameracommon.media.utility.ReferenceClock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/*
 * Failed to analyse overrides
 */
public class TextureContextView
extends TextureView
implements TextureView.SurfaceTextureListener,
CaptureFeedback {
    private static final long DRAW_INTERVAL_MILLIS = 33;
    private static final String TAG = TextureContextView.class.getSimpleName();
    private CaptureFeedbackAnimation mAnimation;
    private final ReferenceClock mAnimationElapsedTimeCount;
    private final AnimationCanvas mCanvas;
    private final ScheduledExecutorService mExecutor;
    private boolean mIsAnimationRquested;
    private ScheduledFuture<?> mScheduledFuture;
    private final SetInvisibleTask mSetInvisibleTask;

    public TextureContextView(Context context) {
        super(context);
        this.mSetInvisibleTask = new SetInvisibleTask((TextureContextView)this, null);
        this.mCanvas = new AnimationCanvas((TextureContextView)this, null);
        this.setSurfaceTextureListener((TextureView.SurfaceTextureListener)this);
        this.mAnimationElapsedTimeCount = new ReferenceClock();
        this.mExecutor = Executors.newSingleThreadScheduledExecutor((ThreadFactory)new ThreadFactoryImpl(null));
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int n, int n2) {
        if (this.mIsAnimationRquested) {
            this.mIsAnimationRquested = false;
            this.mScheduledFuture = this.mExecutor.scheduleAtFixedRate((Runnable)new DrawFrameTask((TextureContextView)this, null), 0, 33, TimeUnit.MILLISECONDS);
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int n, int n2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void release() {
    }

    public void start(CaptureFeedbackAnimation captureFeedbackAnimation) {
        this.mAnimation = captureFeedbackAnimation;
        this.mAnimationElapsedTimeCount.start();
        this.setVisibility(0);
        if (this.isAvailable()) {
            this.mScheduledFuture = this.mExecutor.scheduleAtFixedRate((Runnable)new DrawFrameTask((TextureContextView)this, null), 0, 33, TimeUnit.MILLISECONDS);
            return;
        }
        this.mIsAnimationRquested = true;
    }

    private class AnimationCanvas
    implements CaptureFeedbackAnimationCanvas {
        private Canvas mCanvas;
        final /* synthetic */ TextureContextView this$0;

        private AnimationCanvas(TextureContextView textureContextView) {
            this.this$0 = textureContextView;
            this.mCanvas = null;
        }

        /* synthetic */ AnimationCanvas(TextureContextView textureContextView,  var2_2) {
            super(textureContextView);
        }

        public void clear() {
            if (this.mCanvas == null) {
                return;
            }
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }

        @Override
        public void drawColor(float f, float f2, float f3, float f4) {
            if (this.mCanvas == null) {
                return;
            }
            this.mCanvas.drawColor(Color.argb((int)((int)(255.0f * f)), (int)((int)(255.0f * f2)), (int)((int)(255.0f * f3)), (int)((int)(255.0f * f4))), PorterDuff.Mode.SRC_OVER);
        }

        public boolean lock() {
            this.mCanvas = this.this$0.lockCanvas();
            if (this.mCanvas == null) {
                return false;
            }
            return true;
        }

        public void unlock() {
            if (this.mCanvas != null) {
                this.this$0.unlockCanvasAndPost(this.mCanvas);
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class DrawFrameTask
    implements Runnable {
        final /* synthetic */ TextureContextView this$0;

        private DrawFrameTask(TextureContextView textureContextView) {
            this.this$0 = textureContextView;
        }

        /* synthetic */ DrawFrameTask(TextureContextView textureContextView,  var2_2) {
            super(textureContextView);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void run() {
            if (!this.this$0.mCanvas.lock()) {
                return;
            }
            this.this$0.mCanvas.clear();
            CaptureFeedbackAnimation captureFeedbackAnimation = this.this$0.mAnimation;
            boolean bl = captureFeedbackAnimation != null ? !captureFeedbackAnimation.draw(this.this$0.mCanvas, this.this$0.mAnimationElapsedTimeCount.elapsedTimeMillis()) : true;
            this.this$0.mCanvas.unlock();
            if (!bl) return;
            this.this$0.mAnimationElapsedTimeCount.stop();
            this.this$0.post((Runnable)this.this$0.mSetInvisibleTask);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SetInvisibleTask
    implements Runnable {
        final /* synthetic */ TextureContextView this$0;

        private SetInvisibleTask(TextureContextView textureContextView) {
            this.this$0 = textureContextView;
        }

        /* synthetic */ SetInvisibleTask(TextureContextView textureContextView,  var2_2) {
            super(textureContextView);
        }

        public void run() {
            this.this$0.setVisibility(4);
            this.this$0.mScheduledFuture.cancel(true);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class ThreadFactoryImpl
    implements ThreadFactory {
        private ThreadFactoryImpl() {
        }

        /* synthetic */ ThreadFactoryImpl( var1) {
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(10);
            return thread;
        }
    }

}

