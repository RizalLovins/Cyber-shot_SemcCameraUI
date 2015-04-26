/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$Size
 *  java.lang.IllegalArgumentException
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.ResolutionOptions;
import com.sonyericsson.android.camera.util.capability.ResolutionDependence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SupportedValueList {
    static final String TAG = "SupportedValueList";
    public static final HashMap<Rect, Rect> sPhotoSurfaceSizeMap = new HashMap();
    public static final HashMap<Rect, Rect> sVideoSurfaceSizeMap = new HashMap();
    public float mEvStep;
    public int mFacing;
    public List<String> mFlash;
    public List<String> mFocusArea;
    public List<String> mFocusMode;
    public boolean mIsFaceDetectionSupported;
    public boolean mIsHdrSupported;
    public boolean mIsImageStabilizerSupported;
    public boolean mIsSceneRecognitionSupported;
    public boolean mIsSmileDetectionSupported;
    public boolean mIsSmoothZoomSupported;
    public boolean mIsVideoStabilizerSupported;
    public List<String> mIsoValues;
    public int mMaxEv;
    public int mMaxNumFocusAreas;
    public List<String> mMetering;
    public int mMinEv;
    public List<Camera.Size> mPictureSize;
    public Camera.Size mPreferredPreviewSizeForVideo;
    public List<int[]> mPreviewFpsRange;
    public List<Camera.Size> mPreviewSize;
    public ResolutionOptions mResolutionOptions;
    public List<String> mScene;
    public List<Camera.Size> mVideoSize;
    public List<String> mWhiteBalance;

    public SupportedValueList(CameraActivity cameraActivity, Camera.Parameters parameters, int n) {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters of camera Id[" + n + "] is null.");
        }
        this.mFacing = n;
        this.mFlash = super.getSupportedFlash(parameters);
        this.mWhiteBalance = super.getSupportedWhiteBalance(parameters);
        this.mScene = super.getSupportedScene(parameters);
        this.mFocusMode = super.getSupportedFocusMode(parameters);
        this.mMaxEv = parameters.getMaxExposureCompensation();
        this.mMinEv = parameters.getMinExposureCompensation();
        this.mEvStep = parameters.getExposureCompensationStep();
        this.mPreviewSize = super.getSupportedPreviewSize(parameters);
        this.mPictureSize = super.getSupportedPictureSize(parameters);
        this.mVideoSize = super.getSupportedVideoSize(parameters);
        this.mPreferredPreviewSizeForVideo = super.getPreferredPreviewSizeForVideo(parameters);
        this.mResolutionOptions = super.getResolutionOptions((Context)cameraActivity, this.mPictureSize, this.mPreviewSize);
        this.mPreviewFpsRange = parameters.getSupportedPreviewFpsRange();
        this.mIsSmoothZoomSupported = parameters.isSmoothZoomSupported();
        this.mMaxNumFocusAreas = parameters.getMaxNumFocusAreas();
        this.mMetering = super.getSupportedMetering(parameters);
        this.mIsImageStabilizerSupported = super.getSupportedImageStabilizer(parameters);
        this.mIsVideoStabilizerSupported = super.getSupportedVideoStabilizer(parameters);
        this.mFocusArea = super.getSupportedFocusArea(parameters);
        super.getMaxMultiFocusNum(parameters);
        this.mIsSceneRecognitionSupported = super.getSupportedSceneRecognition(parameters);
        this.mIsFaceDetectionSupported = super.getSupportedFaceDetection(parameters);
        this.mIsSmileDetectionSupported = super.getSupportedSmileDetection(parameters);
        this.mIsoValues = super.getSupportedIsoValues(parameters);
        this.mIsHdrSupported = super.getSupportedHdr(parameters);
    }

    private int getMaxMultiFocusNum(Camera.Parameters parameters) {
        try {
            int n = parameters.getInt("sony-max-multi-focus-num");
            return n;
        }
        catch (NumberFormatException var2_3) {
            return 0;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int getMaxPictureWidth(Context context, List<Camera.Size> list) {
        boolean bl = ResolutionDependence.isDependOnAspect(context);
        int n = 0;
        int n2 = 0;
        if (list == null) return n2;
        for (Camera.Size size : list) {
            if (bl) {
                int n3 = size.width * size.height;
                if (n >= n3) continue;
                n = n3;
                n2 = size.width;
                continue;
            }
            if (n2 >= size.width) continue;
            n2 = size.width;
        }
        return n2;
    }

    private Camera.Size getPreferredPreviewSizeForVideo(Camera.Parameters parameters) {
        return parameters.getPreferredPreviewSizeForVideo();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getPreviewHeight(Context context, int n, List<Camera.Size> list) {
        int n2 = 0;
        if (n != 3264) return n2;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            int n3 = ((Camera.Size)iterator.next()).height;
            if (n3 == 1080) {
                return n3;
            }
            if (n3 != 720) continue;
            n2 = n3;
        }
        return n2;
    }

    private ResolutionOptions getResolutionOptions(Context context, List<Camera.Size> list, List<Camera.Size> list2) {
        int n = SupportedValueList.getMaxPictureWidth(context, list);
        return new ResolutionOptions(context, n, super.getPreviewHeight(context, n, list2));
    }

    private boolean getSupportedFaceDetection(Camera.Parameters parameters) {
        if (parameters.getMaxNumDetectedFaces() > 0) {
            return true;
        }
        return false;
    }

    private List<String> getSupportedFlash(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters.getSupportedFlashModes(), "Flash");
    }

    private List<String> getSupportedFocusArea(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters, "sony-focus-area-values", ",");
    }

    private List<String> getSupportedFocusMode(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters.getSupportedFocusModes(), "FocusMode");
    }

    private boolean getSupportedHdr(Camera.Parameters parameters) {
        return super.isSupported(parameters, "sony-is-values", "on-still-hdr");
    }

    private boolean getSupportedImageStabilizer(Camera.Parameters parameters) {
        return super.isSupported(parameters, "sony-is-values", "on");
    }

    private List<String> getSupportedIsoValues(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters, "sony-iso-values", ",");
    }

    private List<String> getSupportedMetering(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters, "sony-metering-mode-values", ",");
    }

    private List<Camera.Size> getSupportedPictureSize(Camera.Parameters parameters) {
        List list = parameters.getSupportedPictureSizes();
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    private List<Camera.Size> getSupportedPreviewSize(Camera.Parameters parameters) {
        List list = parameters.getSupportedPreviewSizes();
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    private List<String> getSupportedScene(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters.getSupportedSceneModes(), "Scene");
    }

    private boolean getSupportedSceneRecognition(Camera.Parameters parameters) {
        return super.isSupported(parameters, "sony-scene-detect-supported", "true");
    }

    private boolean getSupportedSmileDetection(Camera.Parameters parameters) {
        return super.isSupported(parameters, "sony-smile-detect-values", "on");
    }

    private List<String> getSupportedValues(Camera.Parameters parameters, String string, String string2) {
        ArrayList arrayList = new ArrayList();
        String string3 = parameters.get(string);
        if (string3 != null) {
            String[] arrstring = string3.split(string2);
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)arrstring[i]);
            }
        }
        return arrayList;
    }

    private List<String> getSupportedValues(List<String> list, String string) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                arrayList.add((Object)((String)iterator.next()));
            }
        }
        return arrayList;
    }

    private List<Camera.Size> getSupportedVideoSize(Camera.Parameters parameters) {
        List list = parameters.getSupportedVideoSizes();
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    private boolean getSupportedVideoStabilizer(Camera.Parameters parameters) {
        return super.isSupported(parameters, "sony-vs-values", "on");
    }

    private List<String> getSupportedWhiteBalance(Camera.Parameters parameters) {
        return super.getSupportedValues(parameters.getSupportedWhiteBalance(), "WhiteBalance");
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isSupported(Camera.Parameters parameters, String string, String string2) {
        String string3 = parameters.get(string);
        if (string3 == null || string3.indexOf(string2) == -1) {
            return false;
        }
        return true;
    }

    private void logSize(List<Camera.Size> list, String string) {
    }
}

