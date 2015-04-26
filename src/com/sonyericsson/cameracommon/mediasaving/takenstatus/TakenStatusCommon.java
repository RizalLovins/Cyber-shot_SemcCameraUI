/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  android.net.Uri
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.ref.WeakReference
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import android.location.Location;
import android.net.Uri;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import java.util.ArrayList;
import java.util.List;

public class TakenStatusCommon {
    private static final String TAG = TakenStatusCommon.class.getSimpleName();
    public final boolean addToMediaStore;
    public final String cropValue;
    public final boolean doPostProcessing;
    public final String fileExtension;
    public final int height;
    public final Location location;
    protected List<WeakReference<SavingRequest.StoreDataCallback>> mCallbacks;
    protected long mDateTaken;
    protected Uri mExtraOutput;
    protected String mFilePath;
    protected int mRequestId;
    protected int mSomcType;
    public final String mimeType;
    public final int orientation;
    public final SavingTaskManager.SavedFileType savedFileType;
    public final int width;

    public TakenStatusCommon(long l, int n, Location location, int n2, int n3, String string, String string2, SavingTaskManager.SavedFileType savedFileType, String string3, String string4, boolean bl, boolean bl2) {
        this.mRequestId = -1;
        this.mDateTaken = 0;
        this.mCallbacks = new ArrayList();
        this.mSomcType = 0;
        this.mDateTaken = l;
        this.orientation = n;
        this.location = location;
        this.width = n2;
        this.height = n3;
        this.mimeType = string;
        this.fileExtension = string2;
        this.savedFileType = savedFileType;
        this.mFilePath = string3;
        this.cropValue = string4;
        this.addToMediaStore = bl;
        this.doPostProcessing = bl2;
    }

    public TakenStatusCommon(TakenStatusCommon takenStatusCommon) {
        this.mRequestId = -1;
        this.mDateTaken = 0;
        this.mCallbacks = new ArrayList();
        this.mSomcType = 0;
        this.mRequestId = takenStatusCommon.mRequestId;
        this.mDateTaken = takenStatusCommon.mDateTaken;
        this.orientation = takenStatusCommon.orientation;
        this.location = takenStatusCommon.location;
        this.width = takenStatusCommon.width;
        this.height = takenStatusCommon.height;
        this.mimeType = takenStatusCommon.mimeType;
        this.fileExtension = takenStatusCommon.fileExtension;
        this.savedFileType = takenStatusCommon.savedFileType;
        this.mFilePath = takenStatusCommon.mFilePath;
        this.mCallbacks = takenStatusCommon.mCallbacks;
        this.cropValue = takenStatusCommon.cropValue;
        this.addToMediaStore = takenStatusCommon.addToMediaStore;
        this.doPostProcessing = takenStatusCommon.doPostProcessing;
        this.mExtraOutput = takenStatusCommon.mExtraOutput;
        this.mSomcType = takenStatusCommon.mSomcType;
    }

    public TakenStatusCommon(TakenStatusCommon takenStatusCommon, String string, long l) {
        this.mRequestId = -1;
        this.mDateTaken = 0;
        this.mCallbacks = new ArrayList();
        this.mSomcType = 0;
        this.mRequestId = takenStatusCommon.mRequestId;
        this.mDateTaken = l;
        this.orientation = takenStatusCommon.orientation;
        this.location = takenStatusCommon.location;
        this.width = takenStatusCommon.width;
        this.height = takenStatusCommon.height;
        this.mimeType = takenStatusCommon.mimeType;
        this.fileExtension = takenStatusCommon.fileExtension;
        this.savedFileType = takenStatusCommon.savedFileType;
        this.mFilePath = string;
        this.mCallbacks = takenStatusCommon.mCallbacks;
        this.cropValue = takenStatusCommon.cropValue;
        this.addToMediaStore = takenStatusCommon.addToMediaStore;
        this.doPostProcessing = takenStatusCommon.doPostProcessing;
    }

    public void log() {
    }
}

