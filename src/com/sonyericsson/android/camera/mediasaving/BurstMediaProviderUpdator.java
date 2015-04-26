/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.sqlite.SQLiteFullException
 *  android.net.Uri
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.mediasaving;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteFullException;
import android.net.Uri;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtil;
import com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator;
import java.util.ArrayList;
import java.util.List;

public class BurstMediaProviderUpdator
extends MediaProviderUpdator {
    private static final int MAX_COUNT_BULK_INSERT = 100;
    private static final String TAG = BurstMediaProviderUpdator.class.getSimpleName();
    private List<ContentValues> mInsertPictureRequestList;

    public BurstMediaProviderUpdator(Context context, boolean bl) {
        super(context, bl);
    }

    public void addInsertPictureRequest(PhotoSavingRequest photoSavingRequest) {
        ContentValues contentValues = photoSavingRequest.createContentValues("");
        this.mInsertPictureRequestList.add((Object)contentValues);
        if (this.mInsertPictureRequestList.size() >= 100) {
            this.commitBulkInsert();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public StoreDataResult commitBulkInsert() {
        MediaSavingResult mediaSavingResult = MediaSavingResult.FAIL;
        if (this.mInsertPictureRequestList != null) {
            try {
                ContentResolverUtil.crBulkInsert(this.mContext, MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI, (ContentValues[])this.mInsertPictureRequestList.toArray((Object[])new ContentValues[0]));
                mediaSavingResult = MediaSavingResult.SUCCESS;
            }
            catch (SQLiteFullException var2_2) {
                mediaSavingResult = MediaSavingResult.FAIL_MEMORY_FULL;
            }
            this.mInsertPictureRequestList.clear();
        }
        return new StoreDataResult(mediaSavingResult, Uri.EMPTY, null);
    }

    public void prepareBulkInsert() {
        if (this.mInsertPictureRequestList == null) {
            this.mInsertPictureRequestList = new ArrayList();
            return;
        }
        this.mInsertPictureRequestList.clear();
    }
}

