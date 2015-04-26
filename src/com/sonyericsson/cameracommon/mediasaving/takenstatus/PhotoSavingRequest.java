/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.location.Location
 *  java.io.File
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import android.content.ContentValues;
import android.location.Location;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.io.File;

public class PhotoSavingRequest
extends SavingRequest {
    private static final String TAG = PhotoSavingRequest.class.getSimpleName();
    public final TakenStatusPhoto photo;

    public PhotoSavingRequest(PhotoSavingRequest photoSavingRequest) {
        super(photoSavingRequest);
        this.photo = new TakenStatusPhoto(photoSavingRequest.photo);
    }

    public PhotoSavingRequest(PhotoSavingRequest photoSavingRequest, int n) {
        super(photoSavingRequest, n);
        this.photo = new TakenStatusPhoto(photoSavingRequest.photo);
    }

    public PhotoSavingRequest(TakenStatusCommon takenStatusCommon, TakenStatusPhoto takenStatusPhoto) {
        super(takenStatusCommon);
        this.photo = takenStatusPhoto;
        this.log();
    }

    @Override
    public ContentValues createContentValues(String string) {
        ContentValues contentValues = new ContentValues();
        if (this.common.mSomcType != 0) {
            contentValues.put("somctype", Integer.valueOf((int)this.getSomcType()));
        }
        File file = new File(this.getFilePath());
        contentValues.put("title", CommonUtility.removeFileExtension(file.getName()));
        contentValues.put("_display_name", file.getName());
        if (string.length() > 0) {
            contentValues.put("description", string);
        }
        contentValues.put("datetaken", Long.valueOf((long)this.getDateTaken()));
        contentValues.put("mime_type", this.common.mimeType);
        contentValues.put("orientation", Integer.valueOf((int)this.common.orientation));
        contentValues.put("_size", Long.valueOf((long)file.length()).toString());
        contentValues.put("date_modified", Long.valueOf((long)(file.lastModified() / 1000)));
        if (this.common.location != null) {
            contentValues.put("latitude", Double.valueOf((double)this.common.location.getLatitude()));
            contentValues.put("longitude", Double.valueOf((double)this.common.location.getLongitude()));
        }
        contentValues.put("_data", this.getFilePath());
        contentValues.put("width", Integer.valueOf((int)this.common.width));
        contentValues.put("height", Integer.valueOf((int)this.common.height));
        return contentValues;
    }

    public byte[] getImageData() {
        return this.photo.mImage;
    }

    @Override
    public void log() {
        super.log();
        this.photo.log();
    }

    public void setImageData(byte[] arrby) {
        this.photo.mImage = arrby;
    }
}

