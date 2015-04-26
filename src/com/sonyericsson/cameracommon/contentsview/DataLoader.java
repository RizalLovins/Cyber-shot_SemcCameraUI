/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.graphics.Bitmap
 *  android.graphics.Matrix
 *  android.net.Uri
 *  android.os.Environment
 *  android.provider.MediaStore
 *  android.provider.MediaStore$Images
 *  android.provider.MediaStore$Images$Media
 *  android.provider.MediaStore$Video
 *  android.provider.MediaStore$Video$Media
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Locale
 *  java.util.concurrent.Callable
 */
package com.sonyericsson.cameracommon.contentsview;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.sonyericsson.cameracommon.contentsview.MpoUtils;
import com.sonyericsson.cameracommon.contentsview.PhotoStackQueryHelper;
import com.sonyericsson.cameracommon.contentsview.QueryParameterAdapter;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class DataLoader
implements Callable<Integer> {
    private static final int COLUMN_INDEX_BUCKETID = 7;
    private static final int COLUMN_INDEX_DATA = 1;
    private static final int COLUMN_INDEX_HEIGHT = 5;
    private static final int COLUMN_INDEX_ID = 0;
    private static final int COLUMN_INDEX_MIME = 2;
    private static final int COLUMN_INDEX_ORIENTATION = 6;
    private static final int COLUMN_INDEX_WIDTH = 4;
    public static final String EXTENDED_FILES_COLUMN_ID = "files_id";
    public static final Uri EXTENDED_FILES_CONTENT_URI;
    public static final float PANORAMA_ASPECT_THRESHOLD = 1.8777778f;
    private static final String TAG;
    private final String[] CONTENT_EXTENSIONS = new String[]{".JPG", ".3gp", ".mp4"};
    private Context mContext;
    private final DataLoadCallback mDataLoadCallback;
    private final boolean mIsRegisterCache;
    private int mMediaId;
    private CrQueryParameter mParam = null;
    private int mRequestId;
    private final ContentResolver mResolver;

    static {
        TAG = DataLoader.class.getSimpleName();
        EXTENDED_FILES_CONTENT_URI = Uri.parse((String)"content://media/external/extended_file");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public DataLoader(int n, Uri uri, Context context, DataLoadCallback dataLoadCallback, boolean bl) {
        this.mRequestId = n;
        try {
            this.mMediaId = Integer.valueOf((String)uri.getLastPathSegment());
        }
        catch (Exception var6_6) {
            CameraLogger.w(TAG, "mediaId is not corrected.");
        }
        this.mContext = context;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = dataLoadCallback;
        this.mIsRegisterCache = bl;
    }

    public DataLoader(Context context, List<String> list, int n, DataLoadCallback dataLoadCallback, boolean bl) {
        this.mRequestId = -1;
        this.mParam = this.setupQueryParam(list, n);
        this.mContext = context;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = dataLoadCallback;
        this.mIsRegisterCache = bl;
    }

    private Bitmap createAntiAliasBitmap(Bitmap bitmap, int n) {
        Bitmap bitmap2 = null;
        if (bitmap != null) {
            boolean bl = bitmap.isRecycled();
            bitmap2 = null;
            if (!bl) {
                bitmap2 = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)n, (int)n, (Matrix)null, (boolean)true);
            }
        }
        return bitmap2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Content.ContentInfo createContentInfo(Cursor cursor) {
        int n;
        Uri uri;
        int n2 = super.getMediaId(cursor);
        String string = cursor.getString(2);
        String string2 = cursor.getString(1);
        int n3 = cursor.getInt(4);
        int n4 = cursor.getInt(5);
        int n5 = cursor.getInt(7);
        if (string.equals((Object)"image/jpeg")) {
            uri = Uri.withAppendedPath((Uri)MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String)String.valueOf((int)n2));
            n = 1;
        } else if (string.equals((Object)"video/mp4")) {
            uri = Uri.withAppendedPath((Uri)MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (String)String.valueOf((int)n2));
            n = 2;
        } else if (string.equals((Object)"video/3gpp")) {
            uri = Uri.withAppendedPath((Uri)MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (String)String.valueOf((int)n2));
            n = 2;
        } else {
            if (!string.equals((Object)"image/mpo")) {
                return null;
            }
            uri = Uri.withAppendedPath((Uri)QueryParameterAdapter.MPO_3DPICTURES_CONTENT_URI, (String)String.valueOf((int)n2));
            n = 3;
        }
        String string3 = n == 1 ? super.getMpoFilePath(n2) : null;
        int n6 = n == 1 ? cursor.getInt(6) : 0;
        Content.ContentInfo contentInfo = new Content.ContentInfo();
        contentInfo.mId = n2;
        contentInfo.mOriginalUri = uri;
        contentInfo.mOriginalPath = string2;
        contentInfo.mMpoPath = string3;
        contentInfo.mType = n;
        contentInfo.mWidth = n3;
        contentInfo.mHeight = n4;
        contentInfo.mOrientation = n6;
        contentInfo.mMimeType = string;
        contentInfo.mPlayable = super.isPlayableContent(contentInfo);
        contentInfo.mGroupedImage = super.getGroupedImageCount(n5);
        contentInfo.mSomcType = super.getSomcType(string2);
        contentInfo.mBucketId = n5;
        contentInfo.mContentType = super.getContentType(contentInfo);
        contentInfo.mThumbnail = super.decodeThumbnail(contentInfo);
        return contentInfo;
    }

    private Bitmap decodeThumbnail(Content.ContentInfo contentInfo) {
        Bitmap bitmap = null;
        if (contentInfo != null) {
            if (contentInfo.mOriginalPath == null) {
                contentInfo.mOriginalPath = super.getMediaPath(contentInfo.mId, contentInfo.mType);
            }
            if ((bitmap = ThumbnailFactory.createMicroThumbnail(contentInfo)) != null) {
                bitmap = super.createAntiAliasBitmap(bitmap, bitmap.getWidth());
            }
        }
        return bitmap;
    }

    private /* varargs */ String generatePathSelection(String ... arrstring) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        stringBuffer.append("_data");
        stringBuffer.append(" like '");
        for (String string : arrstring) {
            if (!string.startsWith("/")) {
                stringBuffer.append('/');
            }
            stringBuffer.append(string);
        }
        stringBuffer.append("'");
        stringBuffer.append(" AND ");
        stringBuffer.append("_data NOT LIKE '%/.%'");
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    private List<String> generateQueryPathForEachStorage(List<String> list, List<String> list2) {
        Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            super.generateQueryPathForOneStorage(list, (String)iterator.next());
        }
        return list;
    }

    private List<String> generateQueryPathForOneStorage(List<String> list, String string) {
        for (String string2 : this.CONTENT_EXTENSIONS) {
            String[] arrstring = new String[]{string, Environment.DIRECTORY_DCIM, "%" + string2};
            list.add((Object)super.generatePathSelection(arrstring));
        }
        return list;
    }

    private Content.ContentsType getContentType(Content.ContentInfo contentInfo) {
        if (contentInfo.mType == 1) {
            switch (contentInfo.mSomcType) {
                default: {
                    return Content.ContentsType.PHOTO;
                }
                case 2: 
                case 129: {
                    return Content.ContentsType.BURST;
                }
                case 4: 
                case 130: {
                    return Content.ContentsType.TIME_SHIFT;
                }
                case 42: 
            }
            return Content.ContentsType.SOUND_PHOTO;
        }
        if (contentInfo.mType == 2) {
            switch (contentInfo.mSomcType) {
                default: {
                    if (contentInfo.mWidth < 3840 && contentInfo.mHeight < 3840) break;
                    return Content.ContentsType.VIDEO_4K;
                }
                case 12: {
                    return Content.ContentsType.TIME_SHIFT_VIDEO;
                }
                case 11: {
                    return Content.ContentsType.TIME_SHIFT_VIDEO_120F;
                }
            }
            return Content.ContentsType.VIDEO;
        }
        return Content.ContentsType.NONE;
    }

    private Cursor getCoverImageInfo(int n) {
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id", "somctype"};
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{"_id", n};
        crQueryParameter.where = String.format((Locale)locale, (String)"%s like '%s'", (Object[])arrobject);
        Cursor cursor = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        super.getSomcType(cursor.getString(1));
        return cursor;
    }

    private int getGroupedImageCount(int n) {
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"bucket_id"};
        crQueryParameter.sortOrder = String.format((Locale)Locale.US, (String)"%s DESC, %s DESC", (Object[])new Object[]{"datetaken", "title"});
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{"bucket_id", n};
        crQueryParameter.where = String.format((Locale)locale, (String)"%s like '%s'", (Object[])arrobject);
        Cursor cursor = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursor == null) {
            return 1;
        }
        int n2 = cursor.getCount();
        cursor.close();
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Cursor getLatestImageInfo() {
        Cursor cursor = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, this.mParam);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) return cursor;
        cursor.close();
        return null;
    }

    private int getMediaId(Cursor cursor) {
        return cursor.getInt(0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private String getMediaPath(int n, int n2) {
        Uri uri;
        Cursor cursor;
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        switch (n2) {
            default: {
                return null;
            }
            case 1: 
            case 3: {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                crQueryParameter.projection = new String[]{"_data"};
                Locale locale = Locale.US;
                Object[] arrobject = new Object[]{"_id", n};
                crQueryParameter.where = String.format((Locale)locale, (String)"%s=%s", (Object[])arrobject);
                crQueryParameter.offset = 0;
                crQueryParameter.limit = 1;
                break;
            }
            case 2: {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                crQueryParameter.projection = new String[]{"_data"};
                Locale locale = Locale.US;
                Object[] arrobject = new Object[]{"_id", n};
                crQueryParameter.where = String.format((Locale)locale, (String)"%s=%s", (Object[])arrobject);
                crQueryParameter.offset = 0;
                crQueryParameter.limit = 1;
            }
        }
        if ((cursor = PhotoStackQueryHelper.crQuery(this.mResolver, uri, crQueryParameter)) == null) return null;
        try {
            if (!cursor.moveToPosition(0)) return null;
            String string = cursor.getString(0);
            return string;
        }
        catch (RuntimeException var9_11) {
            CameraLogger.e(TAG, "The specified column isn't found.");
            return null;
        }
        finally {
            cursor.close();
        }
    }

    private String getMpoFilePath(int n) {
        Uri uri = EXTENDED_FILES_CONTENT_URI;
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"filelinkpath"};
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{"files_id", n};
        crQueryParameter.where = String.format((Locale)locale, (String)"%s=%s", (Object[])arrobject);
        crQueryParameter.offset = 0;
        crQueryParameter.limit = 1;
        Cursor cursor = PhotoStackQueryHelper.crQuery(this.mResolver, uri, crQueryParameter);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToPosition(0)) {
                String string = cursor.getString(0);
                return string;
            }
            return null;
        }
        catch (RuntimeException var8_8) {
            CameraLogger.e(TAG, "The specified column isn't found.");
            return null;
        }
        finally {
            cursor.close();
        }
    }

    private int getSomcType(String string) {
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_data", "somctype"};
        crQueryParameter.sortOrder = String.format((Locale)Locale.US, (String)"%s DESC, %s DESC", (Object[])new Object[]{"datetaken", "title"});
        crQueryParameter.where = String.format((Locale)Locale.US, (String)"%s like '%s'", (Object[])new Object[]{"_data", string});
        Cursor cursor = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursor == null) {
            return 0;
        }
        boolean bl = cursor.moveToFirst();
        int n = 0;
        if (bl) {
            n = cursor.getInt(cursor.getColumnIndex("somctype"));
        }
        cursor.close();
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isPlayableContent(Content.ContentInfo contentInfo) {
        if (contentInfo.mType == 2) {
            return true;
        }
        if (contentInfo.mMpoPath == null) {
            return false;
        }
        switch (MpoUtils.getType(contentInfo.mMpoPath)) {
            case 2: {
                return true;
            }
            default: {
                return false;
            }
            case 1: 
        }
        if (contentInfo.mWidth == 0) return false;
        if (contentInfo.mHeight == 0) {
            return false;
        }
        if ((float)contentInfo.mWidth / (float)contentInfo.mHeight > 1.8777778f) return true;
        return false;
    }

    private CrQueryParameter setupQueryParam(List<String> list, int n) {
        ArrayList arrayList = new ArrayList();
        super.generateQueryPathForEachStorage((List<String>)arrayList, list);
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id"};
        crQueryParameter.offset = n;
        crQueryParameter.limit = 1;
        crQueryParameter.sortOrder = String.format((Locale)Locale.US, (String)"%s DESC, %s DESC", (Object[])new Object[]{"datetaken", "title"});
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(somctype!=129)");
        stringBuffer.append(" AND ");
        stringBuffer.append("(somctype!=130)");
        stringBuffer.append(" AND (");
        for (int i = 0; i < arrayList.size(); ++i) {
            if (i != 0) {
                stringBuffer.append(" OR ");
            }
            stringBuffer.append((String)arrayList.get(i));
        }
        stringBuffer.append(")");
        crQueryParameter.where = stringBuffer.toString();
        return crQueryParameter;
    }

    /*
     * Exception decompiling
     */
    public Integer call() throws Exception {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    static interface DataLoadCallback {
        public void onDataLoaded(boolean var1, Content.ContentInfo var2, int var3, boolean var4);
    }

}

