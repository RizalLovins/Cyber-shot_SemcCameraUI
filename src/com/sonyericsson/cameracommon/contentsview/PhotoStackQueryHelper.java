/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.ContentUris
 *  android.database.Cursor
 *  android.net.Uri
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Locale
 */
package com.sonyericsson.cameracommon.contentsview;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import com.sonyericsson.cameracommon.mediasaving.updator.CrDeleteParameter;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import java.util.Locale;

public class PhotoStackQueryHelper {
    private static final String TAG = "PhotoStackQueryHelper";

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int crDelete(ContentResolver contentResolver, Uri uri, CrDeleteParameter crDeleteParameter) {
        reference var7_3 = PhotoStackQueryHelper.class;
        synchronized (PhotoStackQueryHelper.class) {
            try {
                int n = contentResolver.delete(uri, crDeleteParameter.where, crDeleteParameter.selectionArgs);
                return n;
            }
            catch (Throwable var5_6) {
                // ** MonitorExit[var7_3] (shouldn't be in output)
                throw var5_6;
            }
            catch (Exception var3_7) {
                return 0;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static Cursor crQuery(ContentResolver contentResolver, Uri uri, CrQueryParameter crQueryParameter) {
        reference var10_3 = PhotoStackQueryHelper.class;
        synchronized (PhotoStackQueryHelper.class) {
            try {
                Cursor cursor;
                String string;
                if (crQueryParameter.limit > 0) {
                    Locale locale = Locale.US;
                    Object[] arrobject = new Object[]{crQueryParameter.sortOrder, crQueryParameter.limit, crQueryParameter.offset};
                    string = String.format((Locale)locale, (String)"%s limit %d offset %d", (Object[])arrobject);
                    cursor = contentResolver.query(uri, crQueryParameter.projection, crQueryParameter.where, crQueryParameter.selectionArgs, string);
                    return cursor;
                } else {
                    string = crQueryParameter.sortOrder;
                }
                cursor = contentResolver.query(uri, crQueryParameter.projection, crQueryParameter.where, crQueryParameter.selectionArgs, string);
                return cursor;
            }
            catch (Exception var3_9) {
                return null;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean deleteImage(ContentResolver contentResolver, Uri uri) {
        if (uri == null) {
            return false;
        }
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data"};
        crQueryParameter.where = null;
        crQueryParameter.selectionArgs = null;
        crQueryParameter.sortOrder = null;
        crQueryParameter.limit = 0;
        crQueryParameter.offset = 0;
        Cursor cursor = PhotoStackQueryHelper.crQuery(contentResolver, uri, crQueryParameter);
        int n = 0;
        if (cursor == null) return true;
        int n2 = 0;
        do {
            if (n2 >= cursor.getCount()) {
                cursor.close();
                if (n != 0) return false;
                return true;
            }
            cursor.moveToPosition(n2);
            cursor.getString(1);
            CrDeleteParameter crDeleteParameter = new CrDeleteParameter();
            crDeleteParameter.where = "_id=" + Long.valueOf((long)ContentUris.parseId((Uri)uri)).toString();
            crDeleteParameter.selectionArgs = null;
            if (PhotoStackQueryHelper.crDelete(contentResolver, uri, crDeleteParameter) != 1) {
                ++n;
            }
            ++n2;
        } while (true);
    }
}

