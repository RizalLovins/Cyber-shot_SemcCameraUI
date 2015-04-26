/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util;

import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.utility.RecordingUtil;

public class MaxVideoSize {
    public static final long GUARANTEED_MIN_DURATION_IN_MILLIS = 3000;
    public static final String TAG = MaxVideoSize.class.getSimpleName();
    private long mMaxDurationMillisecs;
    private long mMaxFileSizeBytes;

    /*
     * Enabled aggressive block sorting
     */
    public static MaxVideoSize create(Configurations configurations, VideoSize videoSize, StorageController storageController) {
        long l;
        long l2 = RecordingUtil.getRecordableSizeKBytes(storageController);
        MaxVideoSize maxVideoSize = new MaxVideoSize();
        long l3 = l2 * 1024;
        long l4 = configurations.getVideoMaxFileSizeInBytes();
        if (l4 > 0 && l4 < l3) {
            maxVideoSize.setMaxFileSizeBytes(l4);
        } else {
            maxVideoSize.setMaxFileSizeBytes(l3);
        }
        long l5 = RecordingUtil.getDurationMillsFromAverage(l2, videoSize.getAverageFileSize());
        long l6 = configurations.getVideoMaxDurationInMillisecs();
        if (l6 > 0) {
            maxVideoSize.setMaxDurationMillisecs(Math.min((long)l6, (long)l5));
        } else {
            maxVideoSize.setMaxDurationMillisecs(l5);
        }
        long l7 = 1024 * videoSize.getAverageFileSize() / 60;
        if (videoSize.isConstraint()) {
            long l8 = configurations.getMmsOptions().mMaxDuration;
            long l9 = configurations.getMmsOptions().mMaxFileSizeBytes;
            if (l9 > 0) {
                if (l9 < maxVideoSize.getMaxFileSize()) {
                    maxVideoSize.setMaxFileSizeBytes(l9);
                } else {
                    maxVideoSize.setMaxFileSizeBytes(maxVideoSize.getMaxFileSize());
                }
            } else {
                long l10 = l7 * (l8 / 1000);
                if (l10 < maxVideoSize.getMaxFileSize()) {
                    maxVideoSize.setMaxFileSizeBytes(l10);
                } else {
                    maxVideoSize.setMaxFileSizeBytes(maxVideoSize.getMaxFileSize());
                }
            }
            l = l8;
        } else {
            l = maxVideoSize.getMaxDuration();
        }
        long l11 = Math.max((long)0, (long)((long)Math.floor((double)((double)maxVideoSize.getMaxFileSize() / (double)l7))));
        long l12 = l6 > 0 ? Math.min((long)l6, (long)(1000 * l11)) : l11 * 1000;
        if (l > 0) {
            maxVideoSize.setMaxDurationMillisecs(Math.min((long)l, (long)l12));
            return maxVideoSize;
        }
        maxVideoSize.setMaxDurationMillisecs(l12);
        return maxVideoSize;
    }

    private void setMaxDurationMillisecs(long l) {
        if (l > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }
        this.mMaxDurationMillisecs = l;
    }

    private void setMaxFileSizeBytes(long l) {
        this.mMaxFileSizeBytes = l;
    }

    public int getMaxDuration() {
        return (int)this.mMaxDurationMillisecs;
    }

    public long getMaxFileSize() {
        return this.mMaxFileSizeBytes;
    }
}

