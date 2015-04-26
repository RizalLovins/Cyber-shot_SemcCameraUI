/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$Size
 *  java.lang.Boolean
 *  java.lang.Float
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.ResolutionOptions;
import com.sonyericsson.android.camera.util.capability.UxOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CapabilityList {
    private static final String KEY_EXPOSURE_COMPENSATION_STEP = "exposure-compensation-step";
    private static final String KEY_FLASH_MODE = "flash-mode";
    private static final String KEY_FOCUS_MODE = "focus-mode";
    private static final String KEY_MAX_EXPOSURE_COMPENSATION = "max-exposure-compensation";
    private static final String KEY_MAX_NUM_DETECTED_FACES = "max-num-detected-faces";
    private static final String KEY_MAX_NUM_FOCUS_AREAS = "max-num-focus-areas";
    private static final String KEY_MAX_ZOOM = "max-zoom";
    private static final String KEY_MIN_EXPOSURE_COMPENSATION = "min-exposure-compensation";
    private static final String KEY_PICTURE_SIZE = "picture-size";
    private static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_PHOTO = "preferred-preview-size-for-photo";
    private static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO = "preferred-preview-size-for-video";
    private static final String KEY_PREVIEW_FPS_RANGE = "preview-fps-range";
    private static final String KEY_PREVIEW_SIZE = "preview-size";
    private static final String KEY_SCENE_MODE = "scene-mode";
    private static final String KEY_SMOOTH_ZOOM_SUPPORTED = "smooth-zoom-supported";
    private static final String KEY_VIDEO_SIZE = "video-size";
    private static final String KEY_VIDEO_SNAPSHOT = "video-snapshot-supported";
    private static final String KEY_WHITE_BALANCE = "whitebalance";
    public final CapabilityItem<List<String>> AE;
    public final CapabilityItem<List<String>> BURST_SHOT;
    public final CapabilityItem<Integer> EV_MAX;
    public final CapabilityItem<Integer> EV_MIN;
    public final CapabilityItem<Float> EV_STEP;
    public final CapabilityItem<List<String>> EXIF_MAKER_NOTES_TYPES;
    public final CapabilityItem<String> EXTENSION_VERSION;
    public final CapabilityItem<List<String>> FLASH;
    public final CapabilityItem<List<String>> FOCUS_AREA;
    public final CapabilityItem<List<String>> FOCUS_MODE;
    public final CapabilityItem<List<int[]>> FPS_RANGE;
    public final CapabilityItem<List<String>> HDR;
    public final CapabilityItem<List<String>> IMAGE_STABILIZER;
    public final CapabilityItem<List<String>> ISO;
    public final CapabilityItem<Integer> MAX_BURST_SHOT_FRAME_RATE;
    public final CapabilityItem<Rect> MAX_BURST_SHOT_SIZE;
    public final CapabilityItem<Rect> MAX_INTELLIGENT_ACTIVE_SIZE;
    public final CapabilityItem<Integer> MAX_MULTI_FOCUS_AREAS;
    public final CapabilityItem<Integer> MAX_NUM_FACE;
    public final CapabilityItem<Integer> MAX_NUM_FOCUS_AREA;
    public final CapabilityItem<Rect> MAX_PREVIEW_SIZE_STILL;
    public final CapabilityItem<Integer> MAX_SOFT_SKIN_LEVEL;
    public final CapabilityItem<Integer> MAX_SR_ZOOM;
    public final CapabilityItem<Rect> MAX_STEADY_SHOT_SIZE;
    public final CapabilityItem<Integer> MAX_VIDEO_FRAME;
    public final CapabilityItem<Rect> MAX_VIDEO_HDR_SIZE;
    public final CapabilityItem<Rect> MAX_VIDEO_STABILIZER_SIZE_FOR_LEGACY;
    public final CapabilityItem<Integer> MAX_ZOOM;
    public final CapabilityItem<List<String>> METERING;
    public final CapabilityItem<String> MULTI_FOCUS_RECTS;
    public final CapabilityItem<Boolean> OBJECT_TRACKING;
    public final CapabilityItem<List<Rect>> PICTURE_SIZE;
    public final CapabilityItem<List<Rect>> PREVIEW_SIZE;
    public final CapabilityItem<Rect> PREVIEW_SIZE_FOR_PHOTO;
    public final CapabilityItem<Rect> PREVIEW_SIZE_FOR_VIDEO;
    public final CapabilityItem<ResolutionOptions> RESOLUTION_CAPABILITY;
    public final CapabilityItem<List<String>> SCENE;
    public final CapabilityItem<Boolean> SCENE_RECOGNITION;
    public final CapabilityItem<List<String>> SCENE_RECOGNITION_TYPES;
    public final CapabilityItem<Boolean> SMILE_DETECTION;
    public final CapabilityItem<Boolean> SMOOTH_ZOOM;
    public final CapabilityItem<Boolean> SR_ZOOM;
    public final CapabilityItem<UxOptions> UX_CAPABILITY;
    public final CapabilityItem<List<String>> VIDEO_HDR;
    public final CapabilityItem<Boolean> VIDEO_MEATDAT_VALUES;
    public final CapabilityItem<List<String>> VIDEO_NR_VALUES;
    public final CapabilityItem<List<Rect>> VIDEO_SIZE;
    public final CapabilityItem<Boolean> VIDEO_SNAPSHOT;
    public final CapabilityItem<List<String>> VIDEO_STABILIZER;
    public final CapabilityItem<List<String>> VIDEO_STABILIZER_TYPE;
    public final CapabilityItem<List<String>> WHITE_BALANCE;
    private final List<CapabilityItem<?>> mValues;

    /*
     * Exception decompiling
     */
    public CapabilityList(Context var1, SharedPreferences var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotLoadClassException: java/util/List.class - java.io.IOException: No such file
        // org.benf.cfr.reader.state.DCCommonState.loadClassFileAtPath(DCCommonState.java:202)
        // org.benf.cfr.reader.state.DCCommonState.access$500(DCCommonState.java:27)
        // org.benf.cfr.reader.state.DCCommonState$1.invoke(DCCommonState.java:154)
        // org.benf.cfr.reader.state.DCCommonState$1.invoke(DCCommonState.java:151)
        // org.benf.cfr.reader.util.LazyMap.get(LazyMap.java:42)
        // org.benf.cfr.reader.util.LazyExceptionRetainingMap.get(LazyExceptionRetainingMap.java:19)
        // org.benf.cfr.reader.state.DCCommonState.getClassFile(DCCommonState.java:307)
        // org.benf.cfr.reader.state.DCCommonState.getClassFile(DCCommonState.java:322)
        // org.benf.cfr.reader.entities.constantpool.ConstantPoolEntryMethodRef.getMethodPrototype(ConstantPoolEntryMethodRef.java:83)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.buildInvoke(Op02WithProcessedDataAndRefs.java:333)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.createStatement(Op02WithProcessedDataAndRefs.java:1106)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs$11.call(Op02WithProcessedDataAndRefs.java:1888)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs$11.call(Op02WithProcessedDataAndRefs.java:1885)
        // org.benf.cfr.reader.util.graph.AbstractGraphVisitorFI.process(AbstractGraphVisitorFI.java:63)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.convertToOp03List(Op02WithProcessedDataAndRefs.java:1897)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:367)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:153)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public CapabilityList(Context var1, Camera.Parameters var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotLoadClassException: java/util/List.class - java.io.IOException: No such file
        // org.benf.cfr.reader.state.DCCommonState.loadClassFileAtPath(DCCommonState.java:202)
        // org.benf.cfr.reader.state.DCCommonState.access$500(DCCommonState.java:27)
        // org.benf.cfr.reader.state.DCCommonState$1.invoke(DCCommonState.java:154)
        // org.benf.cfr.reader.state.DCCommonState$1.invoke(DCCommonState.java:151)
        // org.benf.cfr.reader.util.LazyMap.get(LazyMap.java:42)
        // org.benf.cfr.reader.util.LazyExceptionRetainingMap.get(LazyExceptionRetainingMap.java:19)
        // org.benf.cfr.reader.state.DCCommonState.getClassFile(DCCommonState.java:307)
        // org.benf.cfr.reader.state.DCCommonState.getClassFile(DCCommonState.java:322)
        // org.benf.cfr.reader.entities.constantpool.ConstantPoolEntryMethodRef.getMethodPrototype(ConstantPoolEntryMethodRef.java:83)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.buildInvoke(Op02WithProcessedDataAndRefs.java:333)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.createStatement(Op02WithProcessedDataAndRefs.java:1106)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs$11.call(Op02WithProcessedDataAndRefs.java:1888)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs$11.call(Op02WithProcessedDataAndRefs.java:1885)
        // org.benf.cfr.reader.util.graph.AbstractGraphVisitorFI.process(AbstractGraphVisitorFI.java:63)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.convertToOp03List(Op02WithProcessedDataAndRefs.java:1897)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:367)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:153)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    public static Rect convertCameraSize(Camera.Size size) {
        if (size == null) {
            return null;
        }
        return new Rect(0, 0, size.width, size.height);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static List<Rect> convertCameraSizeList(List<Camera.Size> list) {
        if (list == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Camera.Size size = (Camera.Size)iterator.next();
            if (size == null) continue;
            arrayList.add((Object)CapabilityList.convertCameraSize(size));
        }
        return arrayList;
    }

    private List<CapabilityItem<?>> createList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.AE);
        arrayList.add(this.BURST_SHOT);
        arrayList.add(this.EV_MAX);
        arrayList.add(this.EV_MIN);
        arrayList.add(this.EV_STEP);
        arrayList.add(this.EXIF_MAKER_NOTES_TYPES);
        arrayList.add(this.FLASH);
        arrayList.add(this.FOCUS_AREA);
        arrayList.add(this.FOCUS_MODE);
        arrayList.add(this.FPS_RANGE);
        arrayList.add(this.IMAGE_STABILIZER);
        arrayList.add(this.HDR);
        arrayList.add(this.ISO);
        arrayList.add(this.MAX_BURST_SHOT_FRAME_RATE);
        arrayList.add(this.MAX_BURST_SHOT_SIZE);
        arrayList.add(this.MAX_MULTI_FOCUS_AREAS);
        arrayList.add(this.MAX_NUM_FACE);
        arrayList.add(this.MAX_NUM_FOCUS_AREA);
        arrayList.add(this.MAX_PREVIEW_SIZE_STILL);
        arrayList.add(this.MAX_SR_ZOOM);
        arrayList.add(this.MAX_ZOOM);
        arrayList.add(this.METERING);
        arrayList.add(this.MULTI_FOCUS_RECTS);
        arrayList.add(this.OBJECT_TRACKING);
        arrayList.add(this.PICTURE_SIZE);
        arrayList.add(this.PREVIEW_SIZE);
        arrayList.add(this.PREVIEW_SIZE_FOR_PHOTO);
        arrayList.add(this.PREVIEW_SIZE_FOR_VIDEO);
        arrayList.add(this.SCENE);
        arrayList.add(this.SCENE_RECOGNITION);
        arrayList.add(this.SCENE_RECOGNITION_TYPES);
        arrayList.add(this.SMILE_DETECTION);
        arrayList.add(this.SMOOTH_ZOOM);
        arrayList.add(this.SR_ZOOM);
        arrayList.add(this.EXTENSION_VERSION);
        arrayList.add(this.VIDEO_HDR);
        arrayList.add(this.VIDEO_SIZE);
        arrayList.add(this.VIDEO_SNAPSHOT);
        arrayList.add(this.VIDEO_STABILIZER);
        arrayList.add(this.VIDEO_STABILIZER_TYPE);
        arrayList.add(this.WHITE_BALANCE);
        arrayList.add(this.MAX_VIDEO_FRAME);
        arrayList.add(this.MAX_VIDEO_HDR_SIZE);
        arrayList.add(this.MAX_VIDEO_STABILIZER_SIZE_FOR_LEGACY);
        arrayList.add(this.MAX_STEADY_SHOT_SIZE);
        arrayList.add(this.MAX_INTELLIGENT_ACTIVE_SIZE);
        arrayList.add(this.VIDEO_NR_VALUES);
        arrayList.add(this.MAX_SOFT_SKIN_LEVEL);
        arrayList.add(this.VIDEO_MEATDAT_VALUES);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ResolutionOptions getResolutionOptions(Context context, boolean bl) {
        int n = this.getMaxPictureSize();
        int n2 = 0;
        switch (n) {
            default: {
                return new ResolutionOptions(context, n, n2, bl);
            }
            case 3264: 
        }
        Iterator iterator = this.VIDEO_SIZE.get().iterator();
        while (iterator.hasNext()) {
            int n3 = ((Rect)iterator.next()).height();
            if (n3 == 1080) {
                n2 = n3;
                return new ResolutionOptions(context, n, n2, bl);
            }
            if (n3 != 720) continue;
            n2 = n3;
        }
        return new ResolutionOptions(context, n, n2, bl);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getMaxPictureSize() {
        List<Rect> list = this.PICTURE_SIZE.get();
        int n = 0;
        for (Rect rect : list) {
            if (n >= rect.width()) continue;
            n = rect.width();
        }
        return n;
    }

    public List<CapabilityItem<?>> values() {
        return this.mValues;
    }
}

