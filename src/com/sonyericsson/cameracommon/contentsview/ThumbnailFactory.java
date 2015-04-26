/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Matrix
 *  android.media.MediaMetadataRetriever
 *  android.net.Uri
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.sonyericsson.cameracommon.contentsview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class ThumbnailFactory {
    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 19200;
    private static final String TAG = "ThumbnailFactory";
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    private static final int UNCONSTRAINED = -1;

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int n, int n2) {
        double d = options.outWidth;
        double d2 = options.outHeight;
        int n3 = n2 == -1 ? 1 : (int)Math.ceil((double)Math.sqrt((double)(d * d2 / (double)n2)));
        int n4 = n == -1 ? 128 : (int)Math.min((double)Math.floor((double)(d / (double)n)), (double)Math.floor((double)(d2 / (double)n)));
        if (n4 < n3) {
            return n3;
        }
        if (n2 == -1 && n == -1) {
            return 1;
        }
        if (n == -1) return n3;
        return n4;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int n, int n2) {
        int n3;
        int n4 = ThumbnailFactory.computeInitialSampleSize(options, n, n2);
        if (n4 <= 8) {
            for (n3 = 1; n3 < n4; --n3) {
            }
        } else {
            n3 = 8 * ((n4 + 7) / 8);
        }
        return n3;
    }

    /*
     * Exception decompiling
     */
    public static Bitmap createMicroThumbnail(Content.ContentInfo var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [4[CASE]], but top level block is 1[TRYBLOCK]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:392)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:444)
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

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    public static Bitmap createVideoThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, uri);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(-1);
        try {
            mediaMetadataRetriever.release();
            return bitmap;
        }
        catch (RuntimeException var15_4) {
            CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
            return bitmap;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            CameraLogger.e("ThumbnailFactory", "Assume this is a corrupt video file.");
            {
                catch (Throwable throwable) {
                    try {
                        mediaMetadataRetriever.release();
                    }
                    catch (RuntimeException var4_10) {
                        CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
                        throw throwable;
                    }
                    throw throwable;
                }
            }
            try {
                mediaMetadataRetriever.release();
                return null;
            }
            catch (RuntimeException var12_6) {
                CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
                return null;
            }
            catch (RuntimeException runtimeException) {
                CameraLogger.e("ThumbnailFactory", "Assume this is a corrupt video file.");
                try {
                    mediaMetadataRetriever.release();
                    return null;
                }
                catch (RuntimeException var8_8) {
                    CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
                    return null;
                }
            }
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    public static Bitmap createVideoThumbnail(String string) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(string);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(-1);
        try {
            mediaMetadataRetriever.release();
            return bitmap;
        }
        catch (RuntimeException var14_3) {
            CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
            return bitmap;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            CameraLogger.e("ThumbnailFactory", "Assume this is a corrupt video file.");
            {
                catch (Throwable throwable) {
                    try {
                        mediaMetadataRetriever.release();
                    }
                    catch (RuntimeException var3_9) {
                        CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
                        throw throwable;
                    }
                    throw throwable;
                }
            }
            try {
                mediaMetadataRetriever.release();
                return null;
            }
            catch (RuntimeException var11_5) {
                CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
                return null;
            }
            catch (RuntimeException runtimeException) {
                CameraLogger.e("ThumbnailFactory", "Assume this is a corrupt video file.");
                try {
                    mediaMetadataRetriever.release();
                    return null;
                }
                catch (RuntimeException var7_7) {
                    CameraLogger.e("ThumbnailFactory", "Ignore failures while cleaning up.");
                    return null;
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Bitmap rotateThumbnail(Bitmap bitmap, int n) {
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
            CameraLogger.e("ThumbnailFactory", "IllegalArgumentException : width = " + n2 + ", height = " + n3);
            return bitmap2;
        }
        catch (Exception exception) {
            CameraLogger.e("ThumbnailFactory", "Exception : width = " + n2 + ", height = " + n3);
            return bitmap2;
        }
    }
}

