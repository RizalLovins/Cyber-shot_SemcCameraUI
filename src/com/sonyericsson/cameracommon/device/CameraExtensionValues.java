/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Deprecated
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.sonyericsson.cameracommon.device;

import java.util.HashMap;
import java.util.Map;

public class CameraExtensionValues {
    public static final int CAMERA_FACING_BACK = 0;
    public static final int CAMERA_FACING_FRONT = 1;
    public static final String EX_AE_MODE_AUTO = "auto";
    public static final String EX_AE_MODE_ISO_PRIO = "iso-prio";
    public static final String EX_AE_MODE_MANUAL = "manual";
    public static final String EX_AE_MODE_SHUTTER_PRIO = "shutter-prio";
    public static final String EX_CENTER_WEIGHTED = "center-weighted";
    public static final String EX_DC_MODE_FOR_PICTURE = "sony-dc-mode-for-picture";
    public static final String EX_DC_MODE_FOR_PREVIEW = "sony-dc-mode-for-preview";
    public static final String EX_DC_MODE_FOR_VIDEO = "sony-dc-mode-for-video";
    public static final String EX_DC_MODE_SUPPORTED = "sony-dc-mode-values";
    public static final String EX_EXIF_MAKER_NOTE_TYPE_SUPER_RESOLUTION = "super-resolution";
    public static final String EX_FACE = "face";
    public static final String EX_FALSE = "false";
    public static final String EX_FOCUS_AREA_CENTER = "center";
    public static final String EX_FOCUS_AREA_MULTI = "multi";
    public static final String EX_FOCUS_AREA_USER = "user";
    public static final String EX_FRAME_AVERAGE = "frame-average";
    public static final String EX_HDR_AUTO = "auto";
    public static final String EX_HDR_ON = "on-still-hdr";
    public static final String EX_LENS_DC_MODE_AUTO = "auto";
    public static final String EX_LENS_DC_MODE_BALANCE = "balance";
    public static final String EX_LENS_DC_MODE_LANDSCAPE = "landscape";
    public static final String EX_MULTI = "multi";
    public static final String EX_OFF = "off";
    public static final String EX_ON = "on";
    public static final String EX_ON_INTELLIGENT_ACTIVE = "on-intelligent-active";
    public static final String EX_ON_STEADY_SHOT = "on-steady-shot";
    public static final String EX_SCENE_DETECTION_PREVIEW_STILL = "preview-still";
    public static final String EX_SCENE_DETECTION_PREVIEW_VIDEO = "preview-video";
    public static final String EX_SCENE_DETECTION_RECORDING = "recording";
    public static final String EX_SCENE_DETECTION_SNAPSHOT = "snapshot";
    public static final String EX_SCENE_MODE_ANTI_MOTION = "anti-motion-blur";
    public static final String EX_SCENE_MODE_BABY = "baby";
    public static final String EX_SCENE_MODE_BACKLIGHT = "backlight";
    public static final String EX_SCENE_MODE_BACKLIGHT_PORTRAIT = "backlight-portrait";
    public static final String EX_SCENE_MODE_DARK = "dark";
    public static final String EX_SCENE_MODE_DISH = "dish";
    public static final String EX_SCENE_MODE_DOCUMENT = "document";
    public static final String EX_SCENE_MODE_HAND_NIGHT = "handheld-twilight";
    public static final String EX_SCENE_MODE_HIGH_SENSITIVITY = "high-sensitivity";
    public static final String EX_SCENE_MODE_PET = "pet";
    public static final String EX_SCENE_MODE_SOFT_SKIN = "soft-skin";
    public static final String EX_SCENE_MODE_SPOT_LIGHT = "spot-light";
    public static final String EX_SCENE_MODE_SWEEP_STITCH = "sweep-stitch";
    public static final String EX_SPOT = "spot";
    public static final String EX_TRUE = "true";
    public static final String EX_VIDEO_STABILIZER_TYPE_NORMAL = "normal";
    public static final String EX_VIDEO_STABILIZER_TYPE_STEADY = "steady-shot";
    public static final String KEY_EX_AE_MODE = "sony-ae-mode";
    public static final String KEY_EX_BURST_SHOT = "sony-burst-shot";
    public static final String KEY_EX_BURST_SHOT_FRAME_RATE = "sony-burst-shot-frame-rate";
    public static final String KEY_EX_EXIF_MAKER_NOTE_TYPES = "sony-exif-maker-note-types";
    public static final String KEY_EX_EXTENSION_VERSION = "sony-extension-version";
    @Deprecated
    public static final String KEY_EX_FACE_DETECTION_SUPPORTED = "sony-face-detect-supported";
    public static final String KEY_EX_FOCUS_AREA = "sony-focus-area";
    public static final String KEY_EX_IMAGE_STABILIZER = "sony-is";
    public static final String KEY_EX_ISO = "sony-iso";
    public static final String KEY_EX_MAX_BURST_SHOT_FRAME_RATE = "sony-max-burst-shot-frame-rate";
    public static final String KEY_EX_MAX_BURST_SHOT_SIZE = "sony-max-burst-shot-size";
    @Deprecated
    public static final String KEY_EX_MAX_FOCUS_AREA_HEIGHT = "sony-max-focus-area-height";
    @Deprecated
    public static final String KEY_EX_MAX_FOCUS_AREA_WIDTH = "sony-max-focus-area-width";
    public static final String KEY_EX_MAX_INTELLIGENT_ACTIVE_SIZE = "sony-max-vs-intelligent-active-size";
    public static final String KEY_EX_MAX_MULTI_FOCUS_NUM = "sony-max-multi-focus-num";
    public static final String KEY_EX_MAX_PREVIEW_SIZE_STILL = "sony-preferred-preview-size-for-still";
    public static final String KEY_EX_MAX_SHUTTER_SPEED = "sony-max-shutter-speed";
    public static final String KEY_EX_MAX_SOFT_SKIN_LEVEL = "sony-max-soft-skin-level";
    public static final String KEY_EX_MAX_SR_ZOOM = "sony-max-sr-zoom";
    public static final String KEY_EX_MAX_STEADY_SHOT_SIZE = "sony-max-vs-steady-shot-size";
    public static final String KEY_EX_MAX_VIDEO_HDR_SIZE = "sony-max-video-hdr-size";
    public static final String KEY_EX_MAX_VIDEO_STABILIZER_SIZE = "sony-max-vs-size";
    @Deprecated
    public static final String KEY_EX_MAX_ZOOM = "sony-max-zoom";
    public static final String KEY_EX_METERING_MODE = "sony-metering-mode";
    public static final String KEY_EX_MIN_SHUTTER_SPEED = "sony-min-shutter-speed";
    public static final String KEY_EX_MULTI_FOCUS_RECTS = "sony-multi-focus-rects";
    public static final String KEY_EX_OBJECT_TRACKING_SUPPORTED = "sony-object-tracking-supported";
    @Deprecated
    public static final String KEY_EX_POSTVIEW_FORMAT = "sony-postview-format";
    public static final String KEY_EX_REC_SOUND = "key-sony-ext-recordingsound";
    public static final String KEY_EX_SCENE_DETECTION_SUPPORTED = "sony-scene-detect-supported";
    public static final String KEY_EX_SCENE_DETECTION_TYPES = "sony-scene-detect-apply-types";
    public static final String KEY_EX_SHUTTER_SOUND = "key-sony-ext-shuttersound";
    public static final String KEY_EX_SHUTTER_SPEED = "sony-shutter-speed";
    public static final String KEY_EX_SHUTTER_SPEED_STEP = "sony-shutter-speed-step";
    public static final String KEY_EX_SMILE_DETECTION = "sony-smile-detect";
    public static final String KEY_EX_SOFT_SKIN_LEVEL_PICTURE = "sony-soft-skin-level-for-picture";
    public static final String KEY_EX_SR_ZOOM_SUPPORTED = "sony-sr-zoom-supported";
    public static final String KEY_EX_SUPPORTED_AE_MODES = "sony-ae-mode-values";
    public static final String KEY_EX_SUPPORTED_BURST_SHOT = "sony-burst-shot-values";
    public static final String KEY_EX_SUPPORTED_FOCUS_AREAS = "sony-focus-area-values";
    public static final String KEY_EX_SUPPORTED_IMAGE_STABILIZERS = "sony-is-values";
    public static final String KEY_EX_SUPPORTED_ISO = "sony-iso-values";
    public static final String KEY_EX_SUPPORTED_MAX_VIDEO_FRAME = "sony-max-video-frame-rate-for-1920x1080";
    public static final String KEY_EX_SUPPORTED_METERING_MODES = "sony-metering-mode-values";
    @Deprecated
    public static final String KEY_EX_SUPPORTED_POSTVIEW_FORMATS = "sony-postview-format-values";
    public static final String KEY_EX_SUPPORTED_SMILE_DETECTIONS = "sony-smile-detect-values";
    public static final String KEY_EX_SUPPORTED_VIDEO_HDR = "sony-video-hdr-values";
    public static final String KEY_EX_SUPPORTED_VIDEO_HDRS = "sony-video-hdr-values";
    @Deprecated
    public static final String KEY_EX_SUPPORTED_VIDEO_MODES = "sony-video-mode-values";
    public static final String KEY_EX_SUPPORTED_VIDEO_STABILIZERS = "sony-vs-values";
    public static final String KEY_EX_USER_FOCUS_AREA_X = "sony-user-focus-area-x";
    public static final String KEY_EX_USER_FOCUS_AREA_Y = "sony-user-focus-area-y";
    public static final String KEY_EX_VIDEO_HDR = "sony-video-hdr";
    public static final String KEY_EX_VIDEO_METADATA_SUPPORTED = "sony-video-metadata-supported";
    @Deprecated
    public static final String KEY_EX_VIDEO_MODE = "sony-video-mode";
    public static final String KEY_EX_VIDEO_NR = "sony-video-nr";
    public static final String KEY_EX_VIDEO_NR_VALUES = "sony-video-nr-values";
    public static final String KEY_EX_VIDEO_STABILIZER = "sony-vs";
    public static final String KEY_EX_VIDEO_STABILIZER_TYPE = "sony-vs-type";
    public static final String POSTVIEW_FORMAT_RGB565 = "rgb565";
    public static final String POSTVIEW_FORMAT_YUV420SP = "yuv420sp";
    private static Map<String, Integer> sSceneMap = null;

    static {
        sSceneMap = new HashMap();
        sSceneMap.put((Object)"auto", (Object)0);
        sSceneMap.put((Object)"action", (Object)1);
        sSceneMap.put((Object)"portrait", (Object)2);
        sSceneMap.put((Object)"landscape", (Object)3);
        sSceneMap.put((Object)"night", (Object)4);
        sSceneMap.put((Object)"night-portrait", (Object)5);
        sSceneMap.put((Object)"theatre", (Object)6);
        sSceneMap.put((Object)"beach", (Object)7);
        sSceneMap.put((Object)"snow", (Object)8);
        sSceneMap.put((Object)"sunset", (Object)9);
        sSceneMap.put((Object)"steadyphoto", (Object)10);
        sSceneMap.put((Object)"fireworks", (Object)11);
        sSceneMap.put((Object)"sports", (Object)12);
        sSceneMap.put((Object)"party", (Object)13);
        sSceneMap.put((Object)"candlelight", (Object)13);
        sSceneMap.put((Object)"document", (Object)16);
        sSceneMap.put((Object)"backlight", (Object)17);
        sSceneMap.put((Object)"backlight-portrait", (Object)18);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int convertSceneStringToInt(String string) {
        Integer n;
        if (string == null || (n = (Integer)sSceneMap.get((Object)string)) == null) {
            return -1;
        }
        return n;
    }

    public static class SceneRecognition {
        public static final int ACTION = 1;
        public static final int AUTO = 0;
        public static final int BACKLIGHT = 17;
        public static final int BACKLIGHT_PORTRAIT = 18;
        public static final int BEACH = 7;
        public static final int CANDLELIGHT = 14;
        public static final int DOCUMENT = 16;
        public static final int FIREWORKS = 11;
        public static final int INVALID = -1;
        public static final int LANDSCAPE = 3;
        public static final int NIGHT = 4;
        public static final int NIGHT_PORTRAIT = 5;
        public static final int PARTY = 13;
        public static final int PORTRAIT = 2;
        public static final int SNOW = 8;
        public static final int SPORTS = 12;
        public static final int STEADYPHOTO = 10;
        public static final int SUNSET = 9;
        public static final int THEATRE = 6;
    }

}

