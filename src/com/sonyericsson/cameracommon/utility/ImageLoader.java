/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.net.Uri
 *  java.io.FileNotFoundException
 *  java.io.InputStream
 *  java.io.InvalidObjectException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InvalidObjectException;

public class ImageLoader {
    private static final int FIRST_REDUCE_RATIO_FULL_IMG = 2;
    private static final int FULL_SIZE_MAX_LENGTH = 1025;
    private static final String TAG = ImageLoader.class.getSimpleName();
    private final Context mContext;
    private final byte[] mImageData;
    private final BitmapFactory.Options mOption;
    private final int mOrientation;
    private final Uri mUri;

    public ImageLoader(Context context, Uri uri, int n) {
        this.mContext = context;
        this.mUri = uri;
        this.mImageData = null;
        this.mOrientation = n;
        this.mOption = new BitmapFactory.Options();
    }

    public ImageLoader(Context context, byte[] arrby, int n) {
        this.mContext = context;
        this.mUri = null;
        this.mImageData = arrby;
        this.mOrientation = n;
        this.mOption = new BitmapFactory.Options();
    }

    private void calcBounds(InputStream inputStream, BitmapFactory.Options options) throws InvalidObjectException, FileNotFoundException {
        options.inSampleSize = 2;
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = super.decodeStream(inputStream, options);
        if (!(bitmap == null || bitmap.isRecycled())) {
            bitmap.recycle();
        }
        if (options.outWidth == -1 || options.outHeight == -1) {
            CameraLogger.e(TAG, "Bitmap read error");
            throw new InvalidObjectException("Failed to calculate bounds of bitmap");
        }
    }

    private int calcRatio(BitmapFactory.Options options, int n, int n2) {
        int n3 = n * options.outHeight;
        int n4 = n * options.outWidth;
        int n5 = (-1 + (n4 + n2)) / n2;
        int n6 = Math.max((int)((-1 + (n3 + n2)) / n2), (int)n5);
        if (n6 == 0) {
            return 1;
        }
        if (n6 > 1 && (n4 / n6 > n2 || n3 / n6 > n2)) {
            --n6;
        }
        return n6;
    }

    private Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) throws FileNotFoundException {
        return BitmapFactory.decodeStream((InputStream)inputStream, (Rect)new Rect(0, 0, 0, 0), (BitmapFactory.Options)options);
    }

    private Bitmap loadFullSize(InputStream inputStream, BitmapFactory.Options options) throws FileNotFoundException, InvalidObjectException {
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = super.decodeStream(inputStream, options);
        if (bitmap == null) {
            CameraLogger.e(TAG, "loadFullSize: Decode read error");
            throw new InvalidObjectException("Failed to decode full size image");
        }
        CameraLogger.showOrientation(TAG, "loadFullSize: mOrientation", this.mOrientation);
        if (this.mOrientation != 0) {
            Bitmap bitmap2 = bitmap;
            Matrix matrix = new Matrix();
            float f = (float)bitmap2.getWidth() / 2.0f;
            float f2 = (float)bitmap2.getHeight() / 2.0f;
            matrix.setRotate((float)this.mOrientation, f, f2);
            Bitmap bitmap3 = Bitmap.createBitmap((Bitmap)bitmap2, (int)0, (int)0, (int)bitmap2.getWidth(), (int)bitmap2.getHeight(), (Matrix)matrix, (boolean)false);
            bitmap2.recycle();
            bitmap = bitmap3.copy(Bitmap.Config.RGB_565, false);
            bitmap3.recycle();
        }
        return bitmap;
    }

    /*
     * Exception decompiling
     */
    public Bitmap load() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:369)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:447)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2783)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:764)
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
}

