/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Face
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$ExtFace
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  java.lang.Boolean
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.utility;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import com.sonyericsson.cameracommon.focusview.FaceInformationList;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FaceDetectUtil {
    private static final String TAG = FaceDetectUtil.class.getSimpleName();

    private static int computeClosesDistance(Rect rect, Rect rect2) {
        Rect rect3 = PositionConverter.getInstance().convertFaceFromDeviceToPreview(rect);
        int n = rect2.centerX() - rect3.centerX();
        int n2 = rect2.centerY() - rect3.centerY();
        if (n < 0) {
            n*=-1;
        }
        if (n2 < 0) {
            n2*=-1;
        }
        return n + n2;
    }

    public static List<FaceIdentification.FaceIdentificationRequest> createFaceIdentificationRequest(CameraExtension.FaceDetectionResult faceDetectionResult) {
        int n = faceDetectionResult.extFaceList.size();
        ArrayList arrayList = null;
        if (n > 0) {
            arrayList = new ArrayList();
            for (CameraExtension.ExtFace extFace : faceDetectionResult.extFaceList) {
                Rect rect = PositionConverter.getInstance().convertFaceFromDeviceToPreview(extFace.face.rect);
                arrayList.add((Object)new FaceIdentification.FaceIdentificationRequest(Integer.valueOf((int)extFace.face.id).toString(), rect));
            }
        }
        return arrayList;
    }

    private static List<DistanceMapItem> createSortedDistanceList(CameraExtension.FaceDetectionResult faceDetectionResult, Rect rect) {
        if (faceDetectionResult == null) {
            return null;
        }
        if (faceDetectionResult.extFaceList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int n = 0;
        Iterator iterator = faceDetectionResult.extFaceList.iterator();
        while (iterator.hasNext()) {
            arrayList.add((Object)new DistanceMapItem(n, FaceDetectUtil.computeClosesDistance(((CameraExtension.ExtFace)iterator.next()).face.rect, rect)));
            ++n;
        }
        Collections.sort((List)arrayList, (Comparator)new DistanceComparator(null));
        return arrayList;
    }

    public static void dumpDistanceMapList(List<DistanceMapItem> list) {
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static String getFaceIdentifyNameByUuid(List<FaceIdentification.FaceIdentificationResult> list, String string) {
        if (list == null) {
            return null;
        }
        Iterator iterator = list.iterator();
        do {
            if (!iterator.hasNext()) return null;
            FaceIdentification.FaceIdentificationResult faceIdentificationResult = (FaceIdentification.FaceIdentificationResult)iterator.next();
        } while (!faceIdentificationResult.mUUid.equals((Object)string));
        if (faceIdentificationResult.mName.equals((Object)"NO NAME")) return null;
        return faceIdentificationResult.mName;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static FaceInformationList getFaceInformationList(List<FaceIdentification.FaceIdentificationResult> list, CameraExtension.FaceDetectionResult faceDetectionResult, Rect rect, String string) {
        List<DistanceMapItem> list2;
        if (faceDetectionResult == null || (list2 = FaceDetectUtil.createSortedDistanceList(faceDetectionResult, rect)) == null) {
            return null;
        }
        FaceInformationList faceInformationList = new FaceInformationList();
        faceInformationList.setUserTouchUuid(string);
        Iterator iterator = list2.iterator();
        do {
            if (!iterator.hasNext()) {
                FaceDetectUtil.logFaceDetectionResult(faceDetectionResult);
                FaceDetectUtil.logFaceIdentificationResult(list);
                return faceInformationList;
            }
            int n = ((DistanceMapItem)iterator.next()).getArrayIndex();
            CameraExtension.ExtFace extFace = (CameraExtension.ExtFace)faceDetectionResult.extFaceList.get(n);
            String string2 = String.valueOf((int)extFace.face.id);
            faceInformationList.addNamedFace(new NamedFace(FaceDetectUtil.getFaceIdentifyNameByUuid(list, string2), string2, extFace.face.rect, extFace.smileScore));
        } while (true);
    }

    public static Boolean hasValidFaceId(CameraExtension.FaceDetectionResult faceDetectionResult) {
        Boolean bl = Boolean.TRUE;
        Iterator iterator = faceDetectionResult.extFaceList.iterator();
        while (iterator.hasNext()) {
            if (((CameraExtension.ExtFace)iterator.next()).face.id != -1) continue;
            bl = Boolean.FALSE;
        }
        return bl;
    }

    public static boolean isValidFaceDetectionResult(CameraExtension.FaceDetectionResult faceDetectionResult) {
        boolean bl = false;
        if (faceDetectionResult != null) {
            int n = faceDetectionResult.extFaceList.size();
            int n2 = faceDetectionResult.indexOfSelectedFace;
            bl = false;
            if (n > n2) {
                int n3 = faceDetectionResult.indexOfSelectedFace;
                bl = false;
                if (n3 >= 0) {
                    bl = true;
                }
            }
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void logFaceDetectionResult(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult == null) {
            CameraLogger.v(TAG, "onFaceDetection: result is null");
            return;
        } else {
            CameraLogger.v(TAG, "onFaceDetection: Number of faces: " + faceDetectionResult.extFaceList.size());
            CameraLogger.v(TAG, "onFaceDetection: Selected index : " + faceDetectionResult.indexOfSelectedFace);
            if (faceDetectionResult.extFaceList.isEmpty()) return;
            {
                int n = 0;
                for (CameraExtension.ExtFace extFace : faceDetectionResult.extFaceList) {
                    String string = "ExtFACE[" + n + "]";
                    String string2 = string + " face = " + (Object)extFace.face + " ";
                    String string3 = string2 + " face.id = " + extFace.face.id + " ";
                    String string4 = string3 + " face.score = " + extFace.face.score + " ";
                    String string5 = string4 + " face.leftEye = " + (Object)extFace.face.leftEye + " ";
                    String string6 = string5 + " face.mouth = " + (Object)extFace.face.mouth + " ";
                    String string7 = string6 + " face.rect = " + (Object)extFace.face.rect + " ";
                    String string8 = string7 + " face.rightEye = " + (Object)extFace.face.rightEye + " ";
                    String string9 = string8 + " SmileScore = " + extFace.smileScore + " ";
                    CameraLogger.v(TAG, string9);
                    ++n;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void logFaceIdentificationResult(List<FaceIdentification.FaceIdentificationResult> list) {
        if (list == null) {
            CameraLogger.v(TAG, "onFaceDetection: resultList is null");
            return;
        } else {
            CameraLogger.v(TAG, "onFaceIdentification: Size of list: " + list.size());
            int n = 0;
            for (FaceIdentification.FaceIdentificationResult faceIdentificationResult : list) {
                CameraLogger.v(TAG, "onFaceIdentification: FACE[" + n + "] Name  : " + faceIdentificationResult.mName);
                CameraLogger.v(TAG, "onFaceIdentification: FACE[" + n + "] Rect  : " + (Object)faceIdentificationResult.mRect);
                CameraLogger.v(TAG, "onFaceIdentification: FACE[" + n + "] UUid  : " + faceIdentificationResult.mUUid);
                ++n;
            }
        }
    }

    /*
     * Exception decompiling
     */
    public static TaggedRectangle overwriteTaggedRectangle(HashMap<String, TaggedRectangle> var0_1, String var1, FaceInformationList var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    public static CameraExtension.FaceDetectionResult setUuidFaceDetectionResult(CameraExtension.FaceDetectionResult faceDetectionResult) {
        int n = 0;
        Iterator iterator = faceDetectionResult.extFaceList.iterator();
        while (iterator.hasNext()) {
            ((CameraExtension.ExtFace)iterator.next()).face.id = n++;
        }
        return faceDetectionResult;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static TaggedRectangle sortRectangles(HashMap<String, TaggedRectangle> hashMap, FaceInformationList faceInformationList) {
        TaggedRectangle taggedRectangle = null;
        int n = faceInformationList.getNamedFaceList().size() < hashMap.size() ? faceInformationList.getNamedFaceList().size() : hashMap.size();
        for (int i = var4_4 = n - 1; i >= 0; --i) {
            TaggedRectangle taggedRectangle2;
            String string = faceInformationList.getNamedFace((int)i).mUuid;
            if (string.equals((Object)faceInformationList.getUserSelectedUuid()) || (taggedRectangle2 = (TaggedRectangle)hashMap.get((Object)string)) == null || faceInformationList.getNamedFace((int)i).mName != null || taggedRectangle2.getName() != null) continue;
            taggedRectangle2.bringToFront();
            taggedRectangle = taggedRectangle2;
        }
        for (int j = var4_4; j >= 0; --j) {
            TaggedRectangle taggedRectangle3;
            String string = faceInformationList.getNamedFace((int)j).mUuid;
            if (string.equals((Object)faceInformationList.getUserSelectedUuid()) || (taggedRectangle3 = (TaggedRectangle)hashMap.get((Object)string)) == null || faceInformationList.getNamedFace((int)j).mName == null && taggedRectangle3.getName() == null) continue;
            taggedRectangle3.bringToFront();
            taggedRectangle = taggedRectangle3;
        }
        if (faceInformationList.getNamedFaceByUuid(faceInformationList.getUserSelectedUuid()) == null) return taggedRectangle;
        TaggedRectangle taggedRectangle4 = (TaggedRectangle)hashMap.get((Object)faceInformationList.getUserSelectedUuid());
        if (taggedRectangle4 == null) return taggedRectangle;
        taggedRectangle4.bringToFront();
        return taggedRectangle4;
    }

    private static class DistanceComparator
    implements Comparator<DistanceMapItem> {
        private DistanceComparator() {
        }

        /* synthetic */ DistanceComparator( var1) {
        }

        public int compare(DistanceMapItem distanceMapItem, DistanceMapItem distanceMapItem2) {
            return distanceMapItem.getDistance() - distanceMapItem2.getDistance();
        }
    }

    private static class DistanceMapItem {
        private final int mArrayIndex;
        private final int mDistance;

        public DistanceMapItem(int n, int n2) {
            this.mArrayIndex = n;
            this.mDistance = n2;
        }

        public int getArrayIndex() {
            return this.mArrayIndex;
        }

        public int getDistance() {
            return this.mDistance;
        }
    }

}

