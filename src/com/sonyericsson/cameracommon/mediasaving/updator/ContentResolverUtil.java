/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.ContentUris
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteFullException
 *  android.media.MediaScannerConnection
 *  android.media.MediaScannerConnection$OnScanCompletedListener
 *  android.net.Uri
 *  android.os.AsyncTask
 *  java.io.File
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Void
 *  java.util.Locale
 */
package com.sonyericsson.cameracommon.mediasaving.updator;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteFullException;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtilListener;
import com.sonyericsson.cameracommon.mediasaving.updator.CrDeleteParameter;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import com.sonyericsson.cameracommon.mediasaving.updator.CrUpdateParameter;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class ContentResolverUtil {
    private static final String MPO_EXTENSION = ".MPO";
    private static final String TAG = ContentResolverUtil.class.getSimpleName();

    public static int crBulkInsert(Context context, Uri uri, ContentValues[] arrcontentValues) {
        try {
            int n = context.getContentResolver().bulkInsert(uri, arrcontentValues);
            return n;
        }
        catch (SQLiteFullException var4_4) {
            throw var4_4;
        }
        catch (Exception var3_5) {
            return 0;
        }
    }

    public static int crDelete(Context context, Uri uri, CrDeleteParameter crDeleteParameter) {
        try {
            int n = context.getContentResolver().delete(uri, crDeleteParameter.where, crDeleteParameter.selectionArgs);
            return n;
        }
        catch (Exception var3_4) {
            return 0;
        }
    }

    public static InputStream crOpenInputStream(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return inputStream;
        }
        catch (Exception var2_3) {
            return null;
        }
    }

    public static OutputStream crOpenOutputStream(Context context, Uri uri) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            return outputStream;
        }
        catch (Exception var2_3) {
            return null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Cursor crQuery(Context context, Uri uri, CrQueryParameter crQueryParameter) {
        try {
            String string;
            if (crQueryParameter.limit > 0) {
                Locale locale = Locale.US;
                Object[] arrobject = new Object[]{crQueryParameter.sortOrder, crQueryParameter.limit, crQueryParameter.offset};
                string = String.format((Locale)locale, (String)"%s limit %d offset %d", (Object[])arrobject);
                do {
                    return context.getContentResolver().query(uri, crQueryParameter.projection, crQueryParameter.where, crQueryParameter.selectionArgs, string);
                    break;
                } while (true);
            }
            string = crQueryParameter.sortOrder;
            return context.getContentResolver().query(uri, crQueryParameter.projection, crQueryParameter.where, crQueryParameter.selectionArgs, string);
        }
        catch (Exception var3_6) {
            return null;
        }
    }

    public static int crUpdate(Context context, Uri uri, CrUpdateParameter crUpdateParameter) {
        try {
            int n = context.getContentResolver().update(uri, crUpdateParameter.values, crUpdateParameter.where, crUpdateParameter.selectionArgs);
            return n;
        }
        catch (SQLiteFullException var4_4) {
            throw var4_4;
        }
        catch (Exception var3_5) {
            return 0;
        }
    }

    public static boolean deleteImage(Context context, Uri uri) {
        return ContentResolverUtil.deleteImageImpl(context, uri, false);
    }

    private static void deleteImageByFilePath(Context context, String string) {
        if (new File(string).delete()) {
            MediaScannerConnection.scanFile((Context)context, (String[])new String[]{string}, (String[])new String[]{null}, (MediaScannerConnection.OnScanCompletedListener)new MediaScannerConnection.OnScanCompletedListener(){

                public void onScanCompleted(String string, Uri uri) {
                }
            });
        }
    }

    private static boolean deleteImageImpl(Context context, Uri uri, boolean bl) {
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
        Cursor cursor = ContentResolverUtil.crQuery(context, uri, crQueryParameter);
        int n = 0;
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); ++i) {
                cursor.moveToPosition(i);
                String string = cursor.getString(1);
                CrDeleteParameter crDeleteParameter = new CrDeleteParameter();
                crDeleteParameter.where = "_id=" + Long.valueOf((long)ContentUris.parseId((Uri)uri)).toString();
                crDeleteParameter.selectionArgs = null;
                if (ContentResolverUtil.crDelete(context, uri, crDeleteParameter) != 1) {
                    ++n;
                }
                if (!bl) continue;
                ContentResolverUtil.deleteImageByFilePath(context, ContentResolverUtil.removeExtension(string) + ".MPO");
            }
            cursor.close();
        }
        if (n == 0) {
            return true;
        }
        return false;
    }

    public static void executeDeteleTask(Context context, Uri uri, boolean bl, ContentResolverUtilListener contentResolverUtilListener) {
        new AsyncDeleteTask(context, uri, bl, contentResolverUtilListener).execute((Object[])new Void[0]);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isExist(Context context, Uri uri) {
        InputStream inputStream;
        boolean bl = true;
        try {
            InputStream inputStream2;
            inputStream = inputStream2 = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return bl;
        }
        catch (Exception var3_5) {
            return false;
        }
        try {
            inputStream.close();
        }
        catch (Exception var5_6) {
            return bl;
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static String removeExtension(String string) {
        if (string == null) {
            return null;
        }
        int n = string.lastIndexOf(".");
        if (n == -1) return string;
        return string.substring(0, n);
    }

    static class AsyncDeleteTask
    extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private final ContentResolverUtilListener mListener;
        private final Uri mUri;
        private final boolean mWithMpo;

        public AsyncDeleteTask(Context context, Uri uri, boolean bl, ContentResolverUtilListener contentResolverUtilListener) {
            this.mContext = context;
            this.mUri = uri;
            this.mWithMpo = bl;
            this.mListener = contentResolverUtilListener;
        }

        protected /* varargs */ Boolean doInBackground(Void ... arrvoid) {
            return ContentResolverUtil.deleteImageImpl(this.mContext, this.mUri, this.mWithMpo);
        }

        protected void onPostExecute(Boolean bl) {
            if (this.mListener != null) {
                this.mListener.onDeleted(bl, this.mUri);
            }
        }
    }

}

