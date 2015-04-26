/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Matrix
 *  android.net.Uri
 *  android.os.Parcelable
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Parcelable;
import com.sonyericsson.cameracommon.utility.ImageLoader;

public class OneShotUtility {
    public static final String KEY_ADD_TO_MEDIA_STORE = "addToMediaStore";
    public static final int REQUEST_ONE_SHOT = 1;
    private static final String TAG = OneShotUtility.class.getSimpleName();

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static int computeInitialSampleSize(double d, double d2, int n, int n2) {
        int n3 = n2 < 0 ? 1 : (int)Math.ceil((double)Math.sqrt((double)(d * d2 / (double)n2)));
        int n4 = n < 0 ? 128 : (int)Math.min((double)Math.floor((double)(d / (double)n)), (double)Math.floor((double)(d2 / (double)n)));
        if (n4 < n3) {
            return n3;
        }
        if (n2 < 0 && n < 0) {
            return 1;
        }
        if (n < 0) return n3;
        return n4;
    }

    public static int computeSampleSize(double d, double d2, int n, int n2) {
        int n3;
        int n4 = OneShotUtility.computeInitialSampleSize(d, d2, n, n2);
        if (n4 <= 8) {
            for (n3 = 1; n3 < n4; --n3) {
            }
        } else {
            n3 = 8 * ((n4 + 7) / 8);
        }
        return n3;
    }

    public static Intent createResultIntent(Activity activity, Uri uri, String string, int n) {
        return OneShotUtility.createResultIntent(uri, string, new ImageLoader((Context)activity, uri, n).load());
    }

    public static Intent createResultIntent(Uri uri, String string, Bitmap bitmap) {
        Intent intent = new Intent("inline-data");
        if (bitmap != null) {
            float f = 1.0f / (float)OneShotUtility.computeSampleSize(bitmap.getWidth(), bitmap.getHeight(), -1, 51200);
            Matrix matrix = new Matrix();
            matrix.setScale(f, f);
            Bitmap bitmap2 = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)matrix, (boolean)true);
            Bitmap bitmap3 = bitmap2.copy(Bitmap.Config.ARGB_8888, false);
            bitmap2.recycle();
            intent.putExtra("data", (Parcelable)bitmap3);
        }
        intent.setDataAndType(uri, string);
        return intent;
    }
}

