/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$MediaProviderUpdator
 *  java.lang.String
 *  java.lang.System
 */
package com.sonyericsson.android.camera.burst;

import android.content.Context;
import com.sonyericsson.android.camera.mediasaving.BurstMediaProviderUpdator;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameraextension.CameraExtension;

/*
 * Failed to analyse overrides
 */
public class BurstInfoUpdator
extends BurstMediaProviderUpdator
implements CameraExtension.MediaProviderUpdator {
    private final PhotoSavingRequest mSavingRequest;

    public BurstInfoUpdator(BaseActivity baseActivity, PhotoSavingRequest photoSavingRequest) {
        super((Context)baseActivity, baseActivity.isOneShotPhoto());
        this.mSavingRequest = photoSavingRequest;
    }

    public void insert(String string) {
        this.mSavingRequest.setFilePath(string);
        this.mSavingRequest.setDateTaken(System.currentTimeMillis());
        if (this.mSavingRequest.getSomcType() == 2) {
            this.insertPictureAndSendIntent(this.mSavingRequest, false);
            this.mSavingRequest.setSomcType(129);
            return;
        }
        this.addInsertPictureRequest(this.mSavingRequest);
    }
}

