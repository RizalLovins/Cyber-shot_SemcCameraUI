/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.Intent
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteFullException
 *  android.net.Uri
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package com.sonyericsson.cameracommon.mediasaving.updator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteFullException;
import android.net.Uri;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettings;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.VideoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtil;
import com.sonyericsson.cameracommon.mediasaving.updator.CrUpdateParameter;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class MediaProviderUpdator {
    private static final String TAG = MediaProviderUpdator.class.getSimpleName();
    protected Context mContext = null;
    private final boolean mIsOneShotPhoto;

    public MediaProviderUpdator(Context context, boolean bl) {
        this.mContext = context;
        DcfPathBuilder.getInstance().resetStatus();
        this.mIsOneShotPhoto = bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Uri crInsert(Uri uri, ContentValues contentValues) {
        void var8_3 = this;
        synchronized (var8_3) {
            try {
                Uri uri2 = this.mContext.getContentResolver().insert(uri, contentValues);
                return uri2;
            }
            catch (SQLiteFullException var6_6) {
                throw var6_6;
            }
            catch (Exception var3_8) {
                return null;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private Uri crQuery(String string) {
        void var8_2 = this;
        synchronized (var8_2) {
            Cursor cursor = this.mContext.getContentResolver().query(MediaSavingConstants.VIDEO_STORAGE_URI, new String[]{"_id"}, string, null, null);
            Uri uri = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Uri uri2;
                    int n = cursor.getInt(0);
                    uri = uri2 = Uri.withAppendedPath((Uri)MediaSavingConstants.VIDEO_STORAGE_URI, (String)String.valueOf((int)n));
                }
                uri = null;
            }
            return uri;
            finally {
                cursor.close();
            }
        }
    }

    private String getQueryParam(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(").append("_data").append("=").append("'" + string + "'").append(")");
        return stringBuffer.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Uri insertVideoContentManager(VideoSavingRequest videoSavingRequest, String string) {
        String string2 = videoSavingRequest.getFilePath();
        ContentValues contentValues = videoSavingRequest.createContentValues(string);
        Uri uri = super.crQuery(super.getQueryParam(string2));
        if (uri != null) {
            CrUpdateParameter crUpdateParameter = new CrUpdateParameter();
            crUpdateParameter.values = contentValues;
            if (ContentResolverUtil.crUpdate(this.mContext, uri, crUpdateParameter) > 0) return uri;
            return null;
        }
        if (videoSavingRequest.getSomcType() == 0) {
            return super.crInsert(MediaSavingConstants.VIDEO_STORAGE_URI, contentValues);
        }
        contentValues.put("somctype", Integer.valueOf((int)videoSavingRequest.getSomcType()));
        return super.crInsert(MediaSavingConstants.EXTENDED_VIDEO_STORAGE_URI, contentValues);
    }

    private void logInsertedContent(ContentValues contentValues) {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void sendBroadcastCameraShot(Context context, Uri uri, boolean bl) {
        if (uri == null || context == null) return;
        String string = uri.toString();
        if (string.startsWith(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString())) {
            Uri uri2 = Uri.parse((String)string.replaceFirst(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString(), MediaSavingConstants.STANDARD_PHOTO_STORAGE_URI.toString()));
            context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", uri2));
            if (bl) return;
            {
                AutoUploadSettings.sendBroadcastIntent(context, uri2);
                return;
            }
        }
        if (string.contains((CharSequence)MediaSavingConstants.PHOTO_STORAGE_URI.toString())) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", uri));
            AutoUploadSettings.sendBroadcastIntent(context, uri);
            return;
        }
        if (!string.contains((CharSequence)MediaSavingConstants.VIDEO_STORAGE_URI.toString())) {
            return;
        }
        context.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO", uri));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public StoreDataResult insertPictureAndSendIntent(PhotoSavingRequest photoSavingRequest, boolean bl) {
        String string = photoSavingRequest.getFilePath();
        MediaSavingResult mediaSavingResult = MediaSavingResult.FAIL;
        Uri uri = Uri.EMPTY;
        if (string != null) {
            try {
                uri = this.insertPictureContentManager(photoSavingRequest, "");
                if (uri != null) {
                    mediaSavingResult = MediaSavingResult.SUCCESS;
                }
            }
            catch (SQLiteFullException var7_6) {
                mediaSavingResult = MediaSavingResult.FAIL_MEMORY_FULL;
            }
        }
        if (mediaSavingResult == MediaSavingResult.SUCCESS) {
            photoSavingRequest.notifyStoreResult(new StoreDataResult(mediaSavingResult, uri, photoSavingRequest));
            if (!bl) return new StoreDataResult(mediaSavingResult, uri, photoSavingRequest);
            if (this.mIsOneShotPhoto) return new StoreDataResult(mediaSavingResult, uri, photoSavingRequest);
            MediaProviderUpdator.sendBroadcastCameraShot(this.mContext, uri, false);
            return new StoreDataResult(mediaSavingResult, uri, photoSavingRequest);
        }
        CameraLogger.e(TAG, "Failed to inserting a photo:" + (Object)mediaSavingResult);
        photoSavingRequest.notifyStoreFailed(mediaSavingResult);
        return new StoreDataResult(mediaSavingResult, uri, photoSavingRequest);
    }

    public Uri insertPictureContentManager(PhotoSavingRequest photoSavingRequest, String string) {
        Uri uri = MediaSavingConstants.PHOTO_STORAGE_URI;
        if (photoSavingRequest.common.savedFileType == SavingTaskManager.SavedFileType.BURST) {
            uri = MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI;
        }
        return super.crInsert(uri, photoSavingRequest.createContentValues(string));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public Uri insertVideoAndSendIntent(VideoSavingRequest videoSavingRequest, String string, boolean bl) {
        String string2 = videoSavingRequest.getFilePath();
        MediaSavingResult mediaSavingResult = MediaSavingResult.FAIL;
        Uri uri = Uri.EMPTY;
        if (string2 != null) {
            try {
                uri = super.insertVideoContentManager(videoSavingRequest, string);
                if (uri != null) {
                    mediaSavingResult = MediaSavingResult.SUCCESS;
                }
            }
            catch (SQLiteFullException var8_7) {
                mediaSavingResult = MediaSavingResult.FAIL_MEMORY_FULL;
            }
        }
        if (mediaSavingResult != MediaSavingResult.SUCCESS) {
            CameraLogger.e(TAG, "Failed to inserting a video:" + (Object)mediaSavingResult);
            return uri;
        }
        if (bl) {
            MediaProviderUpdator.sendBroadcastCameraShot(this.mContext, uri, false);
        }
        return uri;
    }
}

