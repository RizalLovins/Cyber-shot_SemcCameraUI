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
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.io.File;

public class VideoSavingRequest
extends SavingRequest {
    private static final String TAG = VideoSavingRequest.class.getSimpleName();
    public final TakenStatusVideo video;

    public VideoSavingRequest(TakenStatusCommon takenStatusCommon, TakenStatusVideo takenStatusVideo) {
        super(takenStatusCommon);
        this.video = takenStatusVideo;
        this.log();
    }

    public VideoSavingRequest(VideoSavingRequest videoSavingRequest) {
        super(videoSavingRequest);
        this.video = new TakenStatusVideo(videoSavingRequest.video.maxDurationMills, videoSavingRequest.video.maxFileSizeBytes);
    }

    @Override
    public ContentValues createContentValues(String string) {
        ContentValues contentValues = new ContentValues();
        File file = new File(this.getFilePath());
        contentValues.put("title", CommonUtility.removeFileExtension(file.getName()));
        contentValues.put("_display_name", file.getName());
        if (string.length() > 0) {
            contentValues.put("description", string);
        }
        contentValues.put("datetaken", Long.valueOf((long)this.getDateTaken()));
        contentValues.put("mime_type", this.common.mimeType);
        contentValues.put("_size", Long.valueOf((long)file.length()).toString());
        contentValues.put("date_modified", Long.valueOf((long)(file.lastModified() / 1000)));
        if (this.common.location != null) {
            contentValues.put("latitude", Double.valueOf((double)this.common.location.getLatitude()));
            contentValues.put("longitude", Double.valueOf((double)this.common.location.getLongitude()));
        }
        contentValues.put("artist", "<unknown>");
        contentValues.put("album", "<unknown>");
        contentValues.put("duration", Long.valueOf((long)this.getDuration()).toString());
        contentValues.put("resolution", "" + this.common.width + "x" + this.common.height);
        contentValues.put("width", Integer.valueOf((int)this.common.width));
        contentValues.put("height", Integer.valueOf((int)this.common.height));
        contentValues.put("_data", this.getFilePath());
        return contentValues;
    }

    public long getDuration() {
        return this.video.mDuration;
    }

    @Override
    public void log() {
        super.log();
        this.video.log();
    }

    public void setDuration(long l) {
        this.video.mDuration = l;
    }
}

