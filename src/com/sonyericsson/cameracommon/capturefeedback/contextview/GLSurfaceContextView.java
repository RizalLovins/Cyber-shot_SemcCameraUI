/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.opengl.GLES20
 *  android.opengl.GLSurfaceView
 *  android.opengl.GLSurfaceView$Renderer
 *  android.opengl.Matrix
 *  android.util.AttributeSet
 *  android.view.SurfaceHolder
 *  android.view.View
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  javax.microedition.khronos.egl.EGLConfig
 *  javax.microedition.khronos.opengles.GL10
 */
package com.sonyericsson.cameracommon.capturefeedback.contextview;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.View;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonymobile.cameracommon.media.utility.ReferenceClock;
import com.sonymobile.cameracommon.opengl.ExtendedGlSurfaceView;
import com.sonymobile.cameracommon.opengl.ShaderProgramFactory;
import com.sonymobile.cameracommon.opengl.SimpleFrame;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * Failed to analyse overrides
 */
public class GLSurfaceContextView
extends ExtendedGlSurfaceView
implements GLSurfaceView.Renderer,
CaptureFeedback {
    private static final float CENTER_X_POS = 0.0f;
    private static final float CENTER_Y_POS = 0.0f;
    private static final float CENTER_Z_POS = 0.2f;
    private static final float[] EYE_SIGHT_MATRIX;
    private static final float[] PARALLEL_PROJECTION_MATRIX;
    private static final float[] PERSPECTIVE_PROJECTION_MATRIX;
    private static final float[] ROOT_GM;
    private static final String TAG;
    private CaptureFeedbackAnimation mAnimation;
    private final CaptureFeedbackAnimationCanvas mAnimationCanvas;
    private final ReferenceClock mAnimationElapsedTimeCount;
    private SimpleFrame mFlashFeedback = null;
    private final SetInvisibleTask mSetInvisibleTask;
    private int mSimpleFrameShader = 0;

    static {
        TAG = GLSurfaceContextView.class.getSimpleName();
        EYE_SIGHT_MATRIX = new float[16];
        PERSPECTIVE_PROJECTION_MATRIX = new float[16];
        PARALLEL_PROJECTION_MATRIX = new float[16];
        ROOT_GM = new float[16];
        Matrix.setLookAtM((float[])EYE_SIGHT_MATRIX, (int)0, (float)0.0f, (float)0.0f, (float)100.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        Matrix.orthoM((float[])PARALLEL_PROJECTION_MATRIX, (int)0, (float)-1.0f, (float)1.0f, (float)-1.0f, (float)1.0f, (float)0.0f, (float)200.0f);
        Matrix.frustumM((float[])PERSPECTIVE_PROJECTION_MATRIX, (int)0, (float)-1.0f, (float)1.0f, (float)-1.0f, (float)1.0f, (float)50.0f, (float)150.0f);
    }

    public GLSurfaceContextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSetInvisibleTask = new SetInvisibleTask((GLSurfaceContextView)this, null);
        this.mAnimationCanvas = new AnimationCanvas((GLSurfaceContextView)this, null);
        this.mAnimationElapsedTimeCount = new ReferenceClock();
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setZOrderOnTop(true);
        this.setRenderer((GLSurfaceView.Renderer)this);
        this.setRenderMode(0);
        this.getHolder().setFormat(-2);
    }

    private void clearSurface() {
        GLES20.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GLES20.glClear((int)17664);
    }

    private void createAllShaders() {
        if (this.mSimpleFrameShader != 0) {
            ShaderProgramFactory.deleteShaderProgram(this.mSimpleFrameShader);
        }
        this.mSimpleFrameShader = ShaderProgramFactory.createSimpleFrameShaderProgram(this.getContext());
    }

    private boolean disableGlobalFunctions() {
        GLES20.glDisable((int)3042);
        GLES20.glDisable((int)2929);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void doRender() {
        int n = 1;
        this.clearSurface();
        if (this.mFlashFeedback == null) {
            return;
        }
        Matrix.setIdentityM((float[])ROOT_GM, (int)0);
        Matrix.multiplyMM((float[])ROOT_GM, (int)0, (float[])EYE_SIGHT_MATRIX, (int)0, (float[])ROOT_GM, (int)0);
        Matrix.multiplyMM((float[])ROOT_GM, (int)0, (float[])PARALLEL_PROJECTION_MATRIX, (int)0, (float[])ROOT_GM, (int)0);
        this.mFlashFeedback.setGlobalMatrix(ROOT_GM);
        CaptureFeedbackAnimation captureFeedbackAnimation = this.mAnimation;
        if (captureFeedbackAnimation != null) {
            GLES20.glBlendFunc((int)770, (int)n);
            if (captureFeedbackAnimation.draw(this.mAnimationCanvas, this.mAnimationElapsedTimeCount.elapsedTimeMillis())) {
                n = 0;
            }
            GLES20.glBlendFunc((int)770, (int)771);
        } else {
            n = 1;
        }
        if (n == 0) return;
        this.mAnimationElapsedTimeCount.stop();
        this.post((Runnable)this.mSetInvisibleTask);
    }

    private boolean enableGlobalFunctions() {
        GLES20.glEnable((int)3042);
        GLES20.glBlendFunc((int)770, (int)771);
        GLES20.glEnable((int)2929);
        return true;
    }

    private void releaseAllShaders() {
        ShaderProgramFactory.deleteShaderProgram(this.mSimpleFrameShader);
        this.mSimpleFrameShader = 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void render() {
        if (!this.enableGlobalFunctions()) {
            CameraLogger.e(TAG, "render():[Enable functions failed.]");
            return;
        } else {
            this.doRender();
            if (this.disableGlobalFunctions()) return;
            {
                CameraLogger.e(TAG, "render():[Disable functions failed.]");
                return;
            }
        }
    }

    private void setupDynamicConfig(int n, int n2) {
        if (n2 < n) {
            GLES20.glViewport((int)0, (int)(-1 * ((n - n2) / 2)), (int)n, (int)n);
            return;
        }
        GLES20.glViewport((int)0, (int)(-1 * ((n2 - n) / 2)), (int)n2, (int)n2);
    }

    public void onDrawFrame(GL10 gL10) {
        super.render();
    }

    public void onSurfaceChanged(GL10 gL10, int n, int n2) {
        this.setupRelatedToSurfaceSize();
    }

    public void onSurfaceCreated(GL10 gL10, EGLConfig eGLConfig) {
    }

    public void release() {
        this.queueEvent((Runnable)new ReleaseTask(this, null));
    }

    public void setupRelatedToSurfaceSize() {
        this.queueEvent((Runnable)new SetupRelatedToSurfaceSizeTask(this, null));
    }

    public void start(CaptureFeedbackAnimation captureFeedbackAnimation) {
        this.removeCallbacks((Runnable)this.mSetInvisibleTask);
        this.mAnimation = captureFeedbackAnimation;
        this.mAnimationElapsedTimeCount.start();
        this.setVisibility(0);
        this.setRenderMode(1);
        this.requestRender();
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.release();
        super.surfaceDestroyed(surfaceHolder);
    }

    private class AnimationCanvas
    implements CaptureFeedbackAnimationCanvas {
        final /* synthetic */ GLSurfaceContextView this$0;

        private AnimationCanvas(GLSurfaceContextView gLSurfaceContextView) {
            this.this$0 = gLSurfaceContextView;
        }

        /* synthetic */ AnimationCanvas(GLSurfaceContextView gLSurfaceContextView,  var2_2) {
            super(gLSurfaceContextView);
        }

        @Override
        public void drawColor(float f, float f2, float f3, float f4) {
            this.this$0.mFlashFeedback.translate(0.0f, 0.0f, 0.2f);
            this.this$0.mFlashFeedback.setColor(f2, f3, f4, f);
            this.this$0.mFlashFeedback.render();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ReleaseTask
    implements Runnable {
        final /* synthetic */ GLSurfaceContextView this$0;

        private ReleaseTask(GLSurfaceContextView gLSurfaceContextView) {
            this.this$0 = gLSurfaceContextView;
        }

        /* synthetic */ ReleaseTask(GLSurfaceContextView gLSurfaceContextView,  var2_2) {
            super(gLSurfaceContextView);
        }

        public void run() {
            if (this.this$0.mFlashFeedback != null) {
                this.this$0.mFlashFeedback.release();
                this.this$0.mFlashFeedback = null;
            }
            this.this$0.releaseAllShaders();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SetInvisibleTask
    implements Runnable {
        final /* synthetic */ GLSurfaceContextView this$0;

        private SetInvisibleTask(GLSurfaceContextView gLSurfaceContextView) {
            this.this$0 = gLSurfaceContextView;
        }

        /* synthetic */ SetInvisibleTask(GLSurfaceContextView gLSurfaceContextView,  var2_2) {
            super(gLSurfaceContextView);
        }

        public void run() {
            this.this$0.setVisibility(4);
            this.this$0.setRenderMode(0);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SetupRelatedToSurfaceSizeTask
    implements Runnable {
        final /* synthetic */ GLSurfaceContextView this$0;

        private SetupRelatedToSurfaceSizeTask(GLSurfaceContextView gLSurfaceContextView) {
            this.this$0 = gLSurfaceContextView;
        }

        /* synthetic */ SetupRelatedToSurfaceSizeTask(GLSurfaceContextView gLSurfaceContextView,  var2_2) {
            super(gLSurfaceContextView);
        }

        public void run() {
            this.this$0.setupDynamicConfig(this.this$0.getWidth(), this.this$0.getHeight());
            if (this.this$0.mFlashFeedback == null) {
                this.this$0.createAllShaders();
                this.this$0.mFlashFeedback = new SimpleFrame(this.this$0.getContext(), (View)this.this$0);
                this.this$0.mFlashFeedback.setColor(0.0f, 0.0f, 0.0f, 0.0f);
                this.this$0.mFlashFeedback.setShaderProgram(this.this$0.mSimpleFrameShader);
                this.this$0.mFlashFeedback.setVisibility(true);
            }
        }
    }

}

