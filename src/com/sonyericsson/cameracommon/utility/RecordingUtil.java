/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.CamcorderProfile
 *  java.io.File
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.media.CamcorderProfile;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RecordingUtil {
    public static final int ERROR_SIZE_LIMIT = -1;
    public static final String TAG = "RecordingUtil";
    public static final int UPDATE_REMAIN_INTERVAL = 10;
    public static final int VIDEO_PROGRESS_BAR_UPDATE_INTERVAL = 100;
    public static final int VIDEO_REC_TIME_UPDATE_INTERVAL_MILLISEC = 1000;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean deleteFile(String string, boolean bl) {
        if (string == null) {
            return false;
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit((Callable)new DeleteFileTask(string, bl));
        Boolean bl2 = false;
        try {
            bl2 = (Boolean)future.get(3000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException var13_5) {
            CameraLogger.w("RecordingUtil", "Delete file interrupted.", (Throwable)var13_5);
            return bl2;
        }
        catch (ExecutionException var10_6) {
            CameraLogger.w("RecordingUtil", "Delete file failed.", (Throwable)var10_6);
            return bl2;
        }
        catch (TimeoutException var7_7) {
            CameraLogger.w("RecordingUtil", "Delete file time out.", (Throwable)var7_7);
            return bl2;
        }
        finally {
            future.cancel(true);
            executorService.shutdown();
            return bl2;
        }
        do {
            return bl2;
            break;
        } while (true);
    }

    public static long getDurationMillsFromAverage(long l, long l2) {
        return 1000 * Math.max((long)0, (long)((long)Math.floor((double)(60.0 * (double)l / (double)l2))));
    }

    public static long getMaxDurationMillisecond(long l, StorageController storageController) {
        long l2;
        long l3;
        long l4;
        long l5 = 1024 * RecordingUtil.getRecordableSizeKBytes(storageController);
        if (l5 < (l2 = (l4 = RecordingUtil.getDurationMillsFromAverage(storageController.getAvailableStorageSize(), l)) / 1000) * (l3 = 1024 * l / 60)) {
            l4 = 1000 * l5 / l3;
        }
        if (Integer.MAX_VALUE < l4) {
            l4 = Integer.MAX_VALUE;
        }
        return l4;
    }

    public static long getMaxRecordingDuration(CamcorderProfile camcorderProfile, StorageController storageController) {
        return RecordingUtil.getMaxDurationMillisecond(60 * (long)camcorderProfile.videoBitRate / 1024 / 8 + 60 * (long)camcorderProfile.audioBitRate / 1024 / 8, storageController);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static String getOutputFile(String string, Context context) {
        int n;
        String string2 = "/dev/null";
        for (n = 0; n < 30 && (string2 = DcfPathBuilder.getInstance().getVideoPath(context, string)).equals((Object)"/dev/null"); ++n) {
            try {
                Thread.sleep((long)100);
                continue;
            }
            catch (InterruptedException interruptedException) {}
        }
        if (n < 30) return string2;
        return "/dev/null";
    }

    public static long getRecordableSizeKBytes(StorageController storageController) {
        long l = 15360 + (storageController.getAvailableStorageSize() - 61440);
        if (l < 0) {
            l = 0;
        }
        return l;
    }

    private static class DeleteFileTask
    implements Callable<Boolean> {
        private final String mFilePath;
        private final boolean mIsEmpty;

        public DeleteFileTask(String string, boolean bl) {
            this.mFilePath = string;
            this.mIsEmpty = bl;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Boolean call() {
            boolean bl;
            File file = new File(this.mFilePath);
            if (this.mIsEmpty) {
                long l = file.length() LCMP 0;
                bl = false;
                if (l != false) return bl;
                boolean bl2 = file.delete();
                bl = false;
                if (!bl2) return bl;
                bl = true;
                return bl;
            }
            long l = file.length() LCMP 0;
            bl = false;
            if (l <= 0) return bl;
            boolean bl3 = file.delete();
            bl = false;
            if (!bl3) return bl;
            bl = true;
            return bl;
        }
    }

}

