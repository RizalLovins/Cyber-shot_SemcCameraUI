/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$Size
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.Camera;
import com.sonyericsson.android.camera.CameraSize;
import com.sonyericsson.android.camera.configuration.ResolutionOptions;
import com.sonyericsson.android.camera.configuration.SupportedValueList;
import com.sonyericsson.android.camera.configuration.parameters.CaptureFrameShape;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.UnsupportedSensorResolutionException;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.utility.ClassDefinitionChecker;
import java.util.List;

public class PlatformDependencyResolver {
    private static final String TAG = PlatformDependencyResolver.class.getSimpleName();

    public static int getBurstFrameRate(Camera.Parameters parameters, Context context) {
        int n;
        int n2 = context.getResources().getInteger(2131230722);
        String string = parameters.get("sony-max-burst-shot-frame-rate");
        if (string != null && (n = Integer.parseInt((String)string)) < n2) {
            return n;
        }
        return n2;
    }

    public static Resolution getBurstPictureSizeAccordingTo(Camera.Parameters parameters) {
        List list = parameters.getSupportedPictureSizes();
        int n = 0;
        if (list != null) {
            for (Camera.Size size : list) {
                if (n >= size.width) continue;
                n = size.width;
            }
        }
        switch (n) {
            default: {
                throw new IllegalStateException("Burst supported ?");
            }
            case 4128: {
                return Resolution.HDR_NINE_MP;
            }
            case 3264: 
        }
        return Resolution.HDR_SIX_MP;
    }

    public static String getDefaultFlash(Camera.Parameters parameters) {
        List list = parameters.getSupportedFlashModes();
        if (list != null && list.contains((Object)"auto")) {
            return "auto";
        }
        return parameters.getFlashMode();
    }

    public static String getDefaultFocusModeForFastCapturePhoto(Camera.Parameters parameters) {
        List list = parameters.getSupportedFocusModes();
        if (list != null) {
            if (list.contains((Object)"continuous-picture")) {
                return "continuous-picture";
            }
            if (list.contains((Object)"auto")) {
                return "auto";
            }
        }
        return parameters.getFocusMode();
    }

    public static String getDefaultFocusModeForFastCaptureVideo(Camera.Parameters parameters) {
        List list = parameters.getSupportedFocusModes();
        if (list != null && list.contains((Object)"continuous-video")) {
            return "continuous-video";
        }
        return parameters.getFocusMode();
    }

    public static String getDefaultMeteringForFastCapture(Camera.Parameters parameters) {
        String string = parameters.get("sony-metering-mode-values");
        if (string == null) {
            return null;
        }
        if (string.indexOf("face") == -1) {
            return "center-weighted";
        }
        return "face";
    }

    public static String getDefaultPhotolight(Camera.Parameters parameters) {
        List list = parameters.getSupportedFlashModes();
        if (list != null && list.contains((Object)"off")) {
            return "off";
        }
        return parameters.getFlashMode();
    }

    public static Resolution getDefaultResolution(Camera.Parameters parameters, Context context) {
        List list = parameters.getSupportedPictureSizes();
        if (list == null) {
            return null;
        }
        return Resolution.valueOf(ResolutionOptions.getDefaultResolution(context, SupportedValueList.getMaxPictureWidth(context, list)));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Resolution getDefaultResolutionFrom(Context context, CaptureFrameShape captureFrameShape, Camera.Parameters parameters, int n) {
        int n2;
        int n3 = SupportedValueList.getMaxPictureWidth(context, parameters.getSupportedPictureSizes());
        switch (n3) {
            default: {
                throw new UnsupportedSensorResolutionException(n3);
            }
            case 5248: {
                n2 = 2131623954;
                break;
            }
            case 4128: {
                n2 = 2131623955;
                break;
            }
            case 3264: {
                if (HardwareCapability.getInstance().isStillHdrVer3(n)) {
                    n2 = 2131623958;
                    break;
                }
                n2 = 2131623957;
                break;
            }
            case 2592: {
                n2 = 2131623959;
                break;
            }
            case 1920: {
                n2 = HardwareCapability.getInstance().isStillHdrVer3(n) ? 2131623951 : 2131623950;
            }
        }
        String[] arrstring = context.getResources().getStringArray(n2);
        int n4 = arrstring.length;
        for (int i = 0; i < n4; ++i) {
            Resolution resolution = Resolution.valueOf(arrstring[i]);
            if ((int)(100.0f * (float)resolution.getPictureRect().width() / (float)resolution.getPictureRect().height()) != captureFrameShape.getAspectRatio_x100()) continue;
            return resolution;
        }
        throw new UnsupportedSensorResolutionException(n3);
    }

    public static String getDefaultSceneModeForFastCapture(Camera.Parameters parameters) {
        List list = parameters.getSupportedSceneModes();
        if (list == null) {
            return null;
        }
        if (list.contains((Object)"auto")) {
            return "auto";
        }
        return parameters.getSceneMode();
    }

    public static VideoSize getDefaultVideoSize(Camera.Parameters parameters) {
        List list = parameters.getSupportedVideoSizes();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        if (list != null) {
            for (Camera.Size size : list) {
                if (size.width == 1920 && size.height == 1080) {
                    bl = true;
                }
                if (size.width == 1280 && size.height == 720) {
                    bl3 = true;
                }
                if (size.width == 864 && size.height == 480) {
                    bl2 = true;
                }
                if (size.width != 640 || size.height != 480) continue;
                bl4 = true;
            }
        }
        if (bl) {
            return VideoSize.FULL_HD;
        }
        if (bl3) {
            return VideoSize.HD;
        }
        if (bl2) {
            return VideoSize.FWVGA;
        }
        if (bl4) {
            return VideoSize.VGA;
        }
        return null;
    }

    public static String getDefaultWhiteBalanceForFastCapturePhoto(Camera.Parameters parameters) {
        List list = parameters.getSupportedWhiteBalance();
        if (list == null) {
            return null;
        }
        if (list.contains((Object)"auto")) {
            return "auto";
        }
        return parameters.getWhiteBalance();
    }

    public static int getMaxSuperResolutionZoom(Camera.Parameters parameters) {
        String string = parameters.get("sony-max-sr-zoom");
        int n = 0;
        if (string != null) {
            n = Integer.parseInt((String)string);
        }
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Rect getOptimalPreviewSize(Camera.Parameters parameters, int n, Rect rect) {
        Rect rect2;
        Rect rect3;
        if (n == 2) {
            rect3 = rect;
            rect2 = CapabilityList.convertCameraSize(parameters.getPreferredPreviewSizeForVideo());
            do {
                return CameraSize.getOptimalPreviewRect(rect3, rect2, CapabilityList.convertCameraSizeList(parameters.getSupportedPreviewSizes()));
                break;
            } while (true);
        }
        rect3 = CameraSize.getIdealSurfaceRect(CameraSize.getDisplayRect(), rect);
        rect2 = CapabilityList.convertCameraSize(parameters.getPreferredPreviewSizeForVideo());
        return CameraSize.getOptimalPreviewRect(rect3, rect2, CapabilityList.convertCameraSizeList(parameters.getSupportedPreviewSizes()));
    }

    public static int getSoftSkinMaxLevel(Camera.Parameters parameters) {
        String string = parameters.get("sony-max-soft-skin-level");
        int n = 0;
        if (string != null) {
            n = Integer.parseInt((String)string);
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isAutoSceneRecogntionDuringRecSupported(Camera.Parameters parameters) {
        if (parameters == null) {
            return false;
        }
        String string = parameters.get("sony-scene-detect-apply-types");
        if (string == null) return false;
        boolean bl = string.indexOf("recording") != -1;
        if (!PlatformDependencyResolver.isSceneRecognitionSupported(parameters)) return false;
        if (!bl) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isBurstCaptureSupported(Camera.Parameters parameters) {
        String string = parameters.get("sony-burst-shot-values");
        if (string == null || string.indexOf("on") == -1) {
            return false;
        }
        return true;
    }

    public static boolean isFaceDetectionSupported(Camera.Parameters parameters) {
        if (parameters.getMaxNumDetectedFaces() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isFlashLightSupported(Camera.Parameters parameters) {
        List list = parameters.getSupportedFlashModes();
        if (list != null && list.contains((Object)"on")) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isHdrSupported(Camera.Parameters parameters) {
        String string = parameters.get("sony-is-values");
        if (string == null || string.indexOf("auto") == -1) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isImageStabilizerSupported(Camera.Parameters parameters) {
        String string = parameters.get("sony-is-values");
        if (string == null || string.indexOf("on") == -1) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isIsoSupported(Camera.Parameters parameters, String string) {
        String string2 = parameters.get("sony-ae-mode-values");
        if (string2 == null || string2.indexOf(string) == -1) {
            return false;
        }
        return true;
    }

    public static boolean isObjectTrackingSuppoted(Camera.Parameters parameters) {
        return "true".equals((Object)parameters.get("sony-object-tracking-supported"));
    }

    public static boolean isPhotoLightSupported(Camera.Parameters parameters) {
        List list = parameters.getSupportedFlashModes();
        if (list != null && list.contains((Object)"torch")) {
            return true;
        }
        return false;
    }

    public static boolean isSceneRecognitionSupported(Camera.Parameters parameters) {
        return "true".equals((Object)parameters.get("sony-scene-detect-supported"));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isSoftSkinSupported(Camera.Parameters parameters) {
        String string;
        if (parameters == null || (string = parameters.get("sony-max-soft-skin-level")) == null || Integer.parseInt((String)string) <= 0) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isSuperResolutionZoomSupported(Camera.Parameters parameters) {
        if (parameters == null) {
            return false;
        }
        boolean bl = "true".equals((Object)parameters.get("sony-sr-zoom-supported"));
        String string = parameters.get("sony-exif-maker-note-types");
        boolean bl2 = false;
        if (string != null) {
            int n = string.indexOf("super-resolution");
            bl2 = false;
            if (n != -1) {
                bl2 = true;
            }
        }
        boolean bl3 = ClassDefinitionChecker.isSuperResolutionProcessorSupported();
        if (!bl) return false;
        if (!bl2) return false;
        if (!bl3) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isVideoHdrSupported(Camera.Parameters parameters) {
        String string = parameters.get("sony-video-hdr-values");
        if (string == null || string.indexOf("on") == -1) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isVideoStabilizerSupported(Camera.Parameters parameters) {
        String string = parameters.get("sony-vs-values");
        if (string == null || string.indexOf("on") == -1) {
            return false;
        }
        return true;
    }
}

