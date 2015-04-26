/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.location.Location
 *  android.net.Uri
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.ref.WeakReference
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import android.content.ContentValues;
import android.location.Location;
import android.net.Uri;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import java.util.Iterator;
import java.util.List;

public abstract class SavingRequest {
    private static final String TAG = SavingRequest.class.getSimpleName();
    public final TakenStatusCommon common;

    public SavingRequest(SavingRequest savingRequest) {
        this.common = new TakenStatusCommon(savingRequest.common);
    }

    public SavingRequest(SavingRequest savingRequest, int n) {
        this.common = new TakenStatusCommon(savingRequest.common.mDateTaken, n, savingRequest.common.location, savingRequest.common.width, savingRequest.common.height, savingRequest.common.mimeType, savingRequest.common.fileExtension, savingRequest.common.savedFileType, savingRequest.common.mFilePath, savingRequest.common.cropValue, savingRequest.common.addToMediaStore, savingRequest.common.doPostProcessing);
        this.common.mCallbacks = savingRequest.common.mCallbacks;
    }

    public SavingRequest(TakenStatusCommon takenStatusCommon) {
        this.common = takenStatusCommon;
    }

    public void addCallback(StoreDataCallback storeDataCallback) {
        Iterator iterator = this.common.mCallbacks.iterator();
        while (iterator.hasNext()) {
            if ((StoreDataCallback)((WeakReference)iterator.next()).get() != storeDataCallback) continue;
            return;
        }
        this.common.mCallbacks.add((Object)new WeakReference((Object)storeDataCallback));
    }

    public abstract ContentValues createContentValues(String var1);

    public long getDateTaken() {
        return this.common.mDateTaken;
    }

    public Uri getExtraOutput() {
        return this.common.mExtraOutput;
    }

    public String getFilePath() {
        return this.common.mFilePath;
    }

    public int getRequestId() {
        return this.common.mRequestId;
    }

    public int getSomcType() {
        return this.common.mSomcType;
    }

    public void log() {
        this.common.log();
    }

    public void notifyStoreFailed(MediaSavingResult mediaSavingResult) {
        StoreDataResult storeDataResult = new StoreDataResult(MediaSavingResult.FAIL, Uri.EMPTY, (SavingRequest)this);
        Iterator iterator = this.common.mCallbacks.iterator();
        StoreDataCallback storeDataCallback;
        while (iterator.hasNext() && (storeDataCallback = (StoreDataCallback)((WeakReference)iterator.next()).get()) != null) {
            storeDataCallback.onStoreComplete(storeDataResult);
        }
        return;
    }

    public void notifyStoreResult(StoreDataResult storeDataResult) {
        Iterator iterator = this.common.mCallbacks.iterator();
        StoreDataCallback storeDataCallback;
        while (iterator.hasNext() && (storeDataCallback = (StoreDataCallback)((WeakReference)iterator.next()).get()) != null) {
            storeDataCallback.onStoreComplete(storeDataResult);
        }
        return;
    }

    public void setDateTaken(long l) {
        this.common.mDateTaken = l;
    }

    public void setExtraOutput(Uri uri) {
        this.common.mExtraOutput = uri;
    }

    public void setFilePath(String string) {
        this.common.mFilePath = string;
    }

    public void setRequestId(int n) {
        this.common.mRequestId = n;
    }

    public void setSomcType(int n) {
        this.common.mSomcType = n;
    }

    public static interface StoreDataCallback {
        public void onStoreComplete(StoreDataResult var1);
    }

}

