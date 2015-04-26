/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ContentResolver
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.media.ThumbnailUtils
 *  android.net.Uri
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.contentsview;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ThumbnailUtil {
    private static final String TAG = ThumbnailUtil.class.getSimpleName();

    /*
     * Enabled aggressive block sorting
     */
    public static RelativeLayout createThumbnailViewFromJpeg(Activity activity, byte[] arrby, int n) {
        if (arrby == null) {
            CameraLogger.e(TAG, "data is null");
            return null;
        }
        RelativeLayout relativeLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.content_early_thumbnail, null);
        ImageView imageView = (ImageView)relativeLayout.findViewById(R.id.early_thumbnail_image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray((byte[])arrby, (int)0, (int)arrby.length, (BitmapFactory.Options)options);
        int n2 = options.outWidth;
        int n3 = options.outHeight;
        options.inSampleSize = n2 > n3 ? Math.round((float)((float)n3 / 96.0f)) : Math.round((float)((float)n2 / 96.0f));
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        Bitmap bitmap = ThumbnailUtils.extractThumbnail((Bitmap)BitmapFactory.decodeByteArray((byte[])arrby, (int)0, (int)arrby.length, (BitmapFactory.Options)options), (int)96, (int)96);
        Bitmap bitmap2 = null;
        if (bitmap != null && (bitmap2 = ThumbnailUtil.rotateThumbnail(bitmap, n)) != null) {
            imageView.setImageBitmap(bitmap2);
            return relativeLayout;
        }
        imageView.setImageDrawable(null);
        return relativeLayout;
    }

    public static RelativeLayout createThumbnailViewFromUri(Activity activity, Uri uri) {
        return ThumbnailUtil.createThumbnailViewFromUri(activity, uri, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static RelativeLayout createThumbnailViewFromUri(Activity activity, Uri uri, int n) {
        int n2;
        InputStream inputStream;
        int n3;
        if (uri == null) {
            CameraLogger.e(TAG, "uri is null");
            return null;
        }
        RelativeLayout relativeLayout = (RelativeLayout)activity.getLayoutInflater().inflate(R.layout.content_early_thumbnail, null);
        ImageView imageView = (ImageView)relativeLayout.findViewById(R.id.early_thumbnail_image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            InputStream inputStream2;
            inputStream = inputStream2 = activity.getContentResolver().openInputStream(uri);
        }
        catch (FileNotFoundException var6_14) {
            CameraLogger.e(TAG, "FileNotFoundException :  = " + (Object)var6_14);
            inputStream = null;
        }
        BitmapFactory.decodeStream((InputStream)inputStream, (Rect)null, (BitmapFactory.Options)options);
        if (inputStream != null) {
            try {
                inputStream.close();
            }
            catch (IOException var20_15) {
                CameraLogger.e(TAG, "IOException :  = " + (Object)var20_15);
            }
        }
        options.inSampleSize = (n2 = options.outWidth) > (n3 = options.outHeight) ? Math.round((float)((float)n3 / 96.0f)) : Math.round((float)((float)n2 / 96.0f));
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        try {
            InputStream inputStream3;
            inputStream = inputStream3 = activity.getContentResolver().openInputStream(uri);
        }
        catch (FileNotFoundException var12_16) {
            CameraLogger.e(TAG, "FileNotFoundException :  = " + (Object)var12_16);
        }
        Bitmap bitmap = BitmapFactory.decodeStream((InputStream)inputStream, (Rect)null, (BitmapFactory.Options)options);
        if (inputStream != null) {
            try {
                inputStream.close();
            }
            catch (IOException var17_17) {
                CameraLogger.e(TAG, "IOException :  = " + (Object)var17_17);
            }
        }
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail((Bitmap)bitmap, (int)96, (int)96);
        Bitmap bitmap3 = null;
        if (bitmap2 != null && (bitmap3 = ThumbnailUtil.rotateThumbnail(bitmap2, n)) != null) {
            imageView.setImageBitmap(bitmap3);
            return relativeLayout;
        }
        imageView.setImageDrawable(null);
        return relativeLayout;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Bitmap rotateThumbnail(Bitmap bitmap, int n) {
        int n2 = bitmap.getWidth();
        int n3 = bitmap.getHeight();
        Bitmap bitmap2 = bitmap;
        if (n == 0) return bitmap2;
        try {
            Matrix matrix = new Matrix();
            matrix.setRotate((float)n, (float)n2 / 2.0f, (float)n3 / 2.0f);
            Bitmap bitmap3 = Bitmap.createBitmap((Bitmap)bitmap2, (int)0, (int)0, (int)n2, (int)n3, (Matrix)matrix, (boolean)false);
            bitmap2.recycle();
            return bitmap3;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            CameraLogger.e(TAG, "IllegalArgumentException : width = " + n2 + ", height = " + n3);
            return bitmap2;
        }
        catch (Exception exception) {
            CameraLogger.e(TAG, "Exception : width = " + n2 + ", height = " + n3);
            return bitmap2;
        }
    }
}

