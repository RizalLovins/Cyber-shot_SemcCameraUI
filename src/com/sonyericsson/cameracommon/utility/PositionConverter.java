/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class PositionConverter {
    private static final String TAG = "PositionConverter";
    private static final Rect sDeviceRect = new Rect(-1000, -1000, 1000, 1000);
    private static PositionConverter sInstance = new PositionConverter();
    private int mDisplayOrientation;
    private Matrix mMatrixFromDeviceToPreview;
    private Matrix mMatrixFromDeviceToSurface;
    private Matrix mMatrixFromPreviewToSurface;
    private Matrix mMatrixFromSurfaceToDevice;
    private Matrix mMatrixFromSurfaceToPreview;
    private boolean mMirror;
    private boolean mPrepared;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private int mSurfaceHeight;
    private int mSurfaceWidth;

    private PositionConverter() {
    }

    private Rect convert(Rect rect, Matrix matrix) {
        if (matrix == null) {
            CameraLogger.w("PositionConverter", "Matrix to convert rect is null. Surface has not been created.");
            return new Rect();
        }
        RectF rectF = new RectF(rect);
        matrix.mapRect(rectF);
        return new Rect(Math.round((float)rectF.left), Math.round((float)rectF.top), Math.round((float)rectF.right), Math.round((float)rectF.bottom));
    }

    public static PositionConverter getInstance() {
        return sInstance;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Matrix getMatrix() {
        Matrix matrix = new Matrix();
        if (this.mMirror) {
            matrix.setScale(-1.0f, 1.0f);
        } else {
            matrix.setScale(1.0f, 1.0f);
        }
        matrix.postRotate((float)this.mDisplayOrientation);
        return matrix;
    }

    public Rect convertDeviceToFace(Rect rect) {
        return super.convert(rect, this.mMatrixFromPreviewToSurface);
    }

    public Rect convertFaceFromDeviceToPreview(Rect rect) {
        return super.convert(rect, this.mMatrixFromDeviceToPreview);
    }

    public Rect convertFaceToDevice(Rect rect) {
        return super.convert(rect, this.mMatrixFromSurfaceToPreview);
    }

    public Rect convertToDevice(Rect rect) {
        Rect rect2 = super.convert(rect, this.mMatrixFromSurfaceToDevice);
        if (!sDeviceRect.contains(rect2)) {
            rect2.intersect(sDeviceRect);
        }
        return rect2;
    }

    public Rect convertToView(Rect rect) {
        return super.convert(rect, this.mMatrixFromDeviceToSurface);
    }

    public Rect getDeviceRect() {
        return new Rect(sDeviceRect);
    }

    public Rect getPreviewSize() {
        return new Rect(0, 0, this.mPreviewWidth, this.mPreviewHeight);
    }

    public void init(boolean bl, int n, Rect rect, Rect rect2) {
        if (this.mMirror != bl || this.mDisplayOrientation != n || this.mSurfaceWidth != rect.width() || this.mSurfaceHeight != rect.height()) {
            this.mPrepared = false;
        }
        this.mMirror = bl;
        this.mDisplayOrientation = n;
        this.mPreviewWidth = rect2.width();
        this.mPreviewHeight = rect2.height();
        this.setSurfaceSize(rect.width(), rect.height());
        this.mPrepared = true;
    }

    public void setOrientation(int n) {
        if (this.mPrepared) {
            return;
        }
        this.mMatrixFromDeviceToSurface.postRotate((float)n);
        this.mMatrixFromDeviceToPreview.postRotate((float)n);
        this.mMatrixFromSurfaceToDevice.postRotate((float)n);
        this.mMatrixFromSurfaceToPreview.postRotate((float)n);
        this.mMatrixFromPreviewToSurface.postRotate((float)n);
    }

    public void setPreviewSize(int n, int n2) {
        if (this.mPrepared && this.mPreviewWidth == n && this.mPreviewHeight == n2) {
            return;
        }
        this.mMatrixFromSurfaceToPreview = super.getMatrix();
        this.mMatrixFromPreviewToSurface = super.getMatrix();
        this.mPreviewWidth = n;
        this.mPreviewHeight = n2;
        this.mMatrixFromSurfaceToPreview.postScale((float)this.mPreviewWidth / (float)this.mSurfaceWidth, (float)this.mPreviewHeight / (float)this.mSurfaceHeight);
        this.mMatrixFromPreviewToSurface.postScale((float)this.mSurfaceWidth / (float)this.mPreviewWidth, (float)this.mSurfaceHeight / (float)this.mPreviewHeight);
    }

    public void setSurfaceSize(int n, int n2) {
        if (this.mPrepared && this.mSurfaceWidth == n && this.mSurfaceHeight == n2) {
            return;
        }
        this.mMatrixFromDeviceToSurface = super.getMatrix();
        this.mMatrixFromDeviceToPreview = super.getMatrix();
        this.mMatrixFromSurfaceToDevice = super.getMatrix();
        this.mMatrixFromSurfaceToPreview = super.getMatrix();
        this.mMatrixFromPreviewToSurface = super.getMatrix();
        this.mSurfaceWidth = n;
        this.mSurfaceHeight = n2;
        this.mMatrixFromDeviceToSurface.postScale((float)this.mSurfaceWidth / (float)sDeviceRect.width(), (float)this.mSurfaceHeight / (float)sDeviceRect.height());
        this.mMatrixFromDeviceToSurface.postTranslate((float)this.mSurfaceWidth / 2.0f, (float)this.mSurfaceHeight / 2.0f);
        this.mMatrixFromDeviceToPreview.postScale((float)this.mPreviewWidth / (float)sDeviceRect.width(), (float)this.mPreviewHeight / (float)sDeviceRect.height());
        this.mMatrixFromDeviceToPreview.postTranslate((float)this.mPreviewWidth / 2.0f, (float)this.mPreviewHeight / 2.0f);
        this.mMatrixFromSurfaceToDevice.postScale((float)sDeviceRect.width() / (float)this.mSurfaceWidth, (float)sDeviceRect.height() / (float)this.mSurfaceHeight);
        this.mMatrixFromSurfaceToDevice.postTranslate((float)PositionConverter.sDeviceRect.left, (float)PositionConverter.sDeviceRect.top);
        this.mMatrixFromSurfaceToPreview.postScale((float)this.mPreviewWidth / (float)this.mSurfaceWidth, (float)this.mPreviewHeight / (float)this.mSurfaceHeight);
        this.mMatrixFromPreviewToSurface.postScale((float)this.mSurfaceWidth / (float)this.mPreviewWidth, (float)this.mSurfaceHeight / (float)this.mPreviewHeight);
    }
}

