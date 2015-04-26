/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.focusview;

import android.graphics.Rect;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FaceInformationList {
    private static final String TAG = FaceInformationList.class.getSimpleName();
    private List<NamedFace> mNamedFaceList = new ArrayList();
    private boolean mUseSmileGuage = false;
    private String mUserTouchUuid = null;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void dumpFaceInformationList(FaceInformationList faceInformationList) {
        if (faceInformationList == null) {
            CameraLogger.v(TAG, "dumpFaceInformationList() argument is null");
            return;
        }
        CameraLogger.v(TAG, "dumpFaceInformationList use smile guage = " + faceInformationList.isUseSmileGuage());
        CameraLogger.v(TAG, "################");
        for (NamedFace namedFace : faceInformationList.getNamedFaceList()) {
            CameraLogger.v(TAG, " name = " + namedFace.mName + " UUID = " + namedFace.mUuid + " position = " + (Object)namedFace.mFacePosition + " smileScore = " + namedFace.mSmileScore);
        }
        CameraLogger.v(TAG, "################");
    }

    public void addNamedFace(NamedFace namedFace) {
        this.mNamedFaceList.add((Object)namedFace);
    }

    public void addNamedFaceList(List<NamedFace> list) {
        this.mNamedFaceList.addAll(list);
    }

    public void clearNamedFaceList() {
        this.mNamedFaceList.clear();
    }

    public NamedFace getNamedFace(int n) {
        if (this.mNamedFaceList.size() <= n) {
            CameraLogger.e(TAG, "getNamedFace index overflow index = " + n);
            return null;
        }
        return (NamedFace)this.mNamedFaceList.get(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public NamedFace getNamedFaceByUuid(String string) {
        NamedFace namedFace;
        Iterator iterator = this.mNamedFaceList.iterator();
        do {
            boolean bl = iterator.hasNext();
            NamedFace namedFace2 = null;
            if (!bl) return namedFace2;
            namedFace = (NamedFace)iterator.next();
        } while (!namedFace.mUuid.equals((Object)string));
        return namedFace;
    }

    public List<NamedFace> getNamedFaceList() {
        return this.mNamedFaceList;
    }

    public String getUserSelectedUuid() {
        return this.mUserTouchUuid;
    }

    public boolean isUseSmileGuage() {
        return this.mUseSmileGuage;
    }

    public void setNamedFaceList(List<NamedFace> list) {
        this.mNamedFaceList = list;
    }

    public void setUseSmileGuage(boolean bl) {
        this.mUseSmileGuage = bl;
    }

    public void setUserTouchUuid(String string) {
        this.mUserTouchUuid = string;
    }
}

