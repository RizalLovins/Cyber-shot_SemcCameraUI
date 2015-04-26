/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.YuvImage
 *  android.media.CameraProfile
 *  android.net.Uri
 *  java.io.ByteArrayOutputStream
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 */
package com.sonyericsson.android.camera.mediasaving;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.CameraProfile;
import android.net.Uri;
import com.sonyericsson.android.camera.mediasaving.ExifFactory;
import com.sonyericsson.android.camera.mediasaving.ExifOption;
import com.sonyericsson.android.camera.mediasaving.IntegrationMakerException;
import com.sonyericsson.android.camera.util.ThreadSafeOutputStream;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class MediaSavingUtil {
    private static final byte[] APP0_MARKER;
    private static final int EXIF_THUMBNAIL_HEIGHT = 120;
    private static final int EXIF_THUMBNAIL_WIDTH = 160;
    public static final int HEADER_MARGIN = 1024;
    private static final int M_DHT = 196;
    private static final int M_DQT = 219;
    private static final int M_MARKER = 255;
    private static final int M_SOI = 216;
    private static final int M_SOS = 218;
    private static final int SIZE_OF_ONE_BYTE = 8;
    private static final byte[] SOI_MARKER;
    static final String TAG;

    static {
        TAG = MediaSavingUtil.class.getSimpleName();
        SOI_MARKER = new byte[]{-1, -40};
        APP0_MARKER = new byte[]{-1, -32};
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int addExifHeader(OutputStream outputStream, SavingRequest savingRequest, byte[] arrby) {
        int n2;
        ExifOption exifOption = ExifOption.create(savingRequest, arrby);
        byte[] arrby2 = new byte[ExifFactory.getLength() + (int)exifOption.mThumbnailDataLength];
        int n = ExifFactory.generate(arrby2, exifOption);
        if (n <= 0) return n;
        try {
            outputStream.write(SOI_MARKER);
            outputStream.write(arrby2, 0, n);
            outputStream.flush();
            n2 = SOI_MARKER.length;
        }
        catch (IOException iOException) {
            CameraLogger.e(TAG, "Add exif header failed.", (Throwable)iOException);
            return -1;
        }
        n+=n2;
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void appendOnlyImageFromJfifData(OutputStream outputStream, byte[] arrby, int n) {
        int n2 = MediaSavingUtil.skipApp0Marker(arrby);
        if (n2 <= 0) return;
        int n3 = n - n2;
        try {
            outputStream.write(arrby, n2, n3);
            outputStream.flush();
            return;
        }
        catch (IOException var5_5) {
            CameraLogger.e(TAG, "Append image data failed.", (Throwable)var5_5);
            return;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    public static MediaSavingResult convertYuvToJpegOutputStream(YuvImage yuvImage, OutputStream outputStream, SavingRequest savingRequest) {
        MediaSavingResult mediaSavingResult = MediaSavingResult.FAIL;
        int n = CameraProfile.getJpegEncodingQualityParameter((int)2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby = new byte[]{};
        boolean bl = yuvImage.compressToJpeg(new Rect(0, 0, savingRequest.common.width, savingRequest.common.height), n, (OutputStream)byteArrayOutputStream);
        int n2 = 0;
        if (bl) {
            byte[] arrby2 = byteArrayOutputStream.toByteArray();
            arrby = new byte[1024 + arrby2.length];
            try {
                int n3;
                n2 = n3 = MediaSavingUtil.integrateJfif(arrby2, arrby);
            }
            catch (IntegrationMakerException var20_11) {
                arrby = byteArrayOutputStream.toByteArray();
                n2 = arrby.length;
            }
        }
        if (MediaSavingUtil.addExifHeader(outputStream, savingRequest, MediaSavingUtil.getExifThumbnail(arrby, savingRequest)) > 0) {
            MediaSavingUtil.appendOnlyImageFromJfifData(outputStream, arrby, n2);
            mediaSavingResult = MediaSavingResult.SUCCESS;
        } else {
            CameraLogger.e(TAG, "addExifHeader failed.");
        }
        if (byteArrayOutputStream == null) return mediaSavingResult;
        try {
            byteArrayOutputStream.close();
            return mediaSavingResult;
        }
        catch (IOException var17_14) {
            CameraLogger.e(TAG, "Closing output stream failed.", (Throwable)var17_14);
            return mediaSavingResult;
        }
        catch (Exception exception) {
            CameraLogger.e(TAG, "saveImage failed.", (Throwable)exception);
            if (byteArrayOutputStream == null) return mediaSavingResult;
            try {
                byteArrayOutputStream.close();
                return mediaSavingResult;
            }
            catch (IOException var14_13) {
                CameraLogger.e(TAG, "Closing output stream failed.", (Throwable)var14_13);
                return mediaSavingResult;
            }
        }
        catch (Throwable throwable) {
            if (byteArrayOutputStream == null) throw throwable;
            try {
                byteArrayOutputStream.close();
            }
            catch (IOException var10_16) {
                CameraLogger.e(TAG, "Closing output stream failed.", (Throwable)var10_16);
                throw throwable;
            }
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static byte[] getExifThumbnail(byte[] arrby, SavingRequest savingRequest) {
        int n;
        int n2;
        int n3;
        float f;
        int n4;
        int n5 = Math.max((int)(savingRequest.common.width / 160), (int)(savingRequest.common.height / 120));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = n5;
        Bitmap bitmap = BitmapFactory.decodeByteArray((byte[])arrby, (int)0, (int)arrby.length, (BitmapFactory.Options)options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap == null) {
            return new byte[0];
        }
        int n6 = bitmap.getHeight();
        float f2 = (float)n6 / 120.0f;
        if (f2 > (f = (float)(n = bitmap.getWidth()) / 160.0f)) {
            n3 = 120;
            n2 = (int)((float)n / f2);
        } else {
            n2 = 160;
            n3 = (int)((float)n6 / f);
        }
        Bitmap bitmap2 = Bitmap.createBitmap((int)160, (int)120, (Bitmap.Config)Bitmap.Config.RGB_565);
        bitmap2.eraseColor(-16777216);
        Canvas canvas = new Canvas(bitmap2);
        int n7 = (160 - n2) / 2;
        int n8 = (120 - n3) / 2;
        Bitmap bitmap3 = Bitmap.createScaledBitmap((Bitmap)bitmap, (int)n2, (int)n3, (boolean)false);
        canvas.drawBitmap(bitmap3, (float)n7, (float)n8, new Paint());
        if (!bitmap3.isRecycled()) {
            bitmap3.recycle();
        }
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)byteArrayOutputStream);
        if (!bitmap2.isRecycled()) {
            bitmap2.recycle();
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        byte[] arrby2 = byteArrayOutputStream.toByteArray();
        byte[] arrby3 = new byte[1024 + arrby2.length];
        try {
            int n9;
            n4 = n9 = MediaSavingUtil.integrateJfif(arrby2, arrby3);
        }
        catch (IntegrationMakerException var20_23) {
            arrby3 = arrby2;
            n4 = arrby3.length;
        }
        int n10 = MediaSavingUtil.skipApp0Marker(arrby3);
        byte[] arrby4 = new byte[n4 - n10 + SOI_MARKER.length];
        arrby4[0] = SOI_MARKER[0];
        arrby4[1] = SOI_MARKER[1];
        System.arraycopy((Object)arrby3, (int)n10, (Object)arrby4, (int)SOI_MARKER.length, (int)(arrby4.length - SOI_MARKER.length));
        try {
            byteArrayOutputStream.close();
            return arrby4;
        }
        catch (IOException var24_24) {
            CameraLogger.e(TAG, "Closing output stream failed.", (Throwable)var24_24);
            return arrby4;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int integrateJfif(byte[] arrby, byte[] arrby2) throws IntegrationMakerException {
        int n = arrby.length;
        byte[] arrby3 = new byte[2];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n2 + 1 < n) {
            arrby3[0] = arrby[n2];
            arrby3[1] = arrby[n2 + 1];
            n2+=2;
            if (-1 != arrby3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == arrby3[1]) break;
            if (-40 == arrby3[1]) continue;
            byte[] arrby4 = new byte[]{arrby[n2], arrby[n2 + 1]};
            int n7 = new BigInteger(arrby4).intValue();
            if (-60 == arrby3[1]) {
                int n8 = n4 == 0 ? n7 : n7 - 2;
                n4+=n8;
                n6+=n7 + 2;
            } else if (-37 == arrby3[1]) {
                int n9 = n3 == 0 ? n7 : n7 - 2;
                n3+=n9;
                n5+=n7 + 2;
            }
            n2+=n7;
        }
        byte[] arrby5 = new byte[n3 + 2];
        byte[] arrby6 = new byte[n4 + 2];
        arrby5[0] = -1;
        arrby5[1] = -37;
        arrby6[0] = -1;
        arrby6[1] = -60;
        String string = String.valueOf((int)n3);
        String string2 = String.valueOf((int)n4);
        byte[] arrby7 = new BigInteger(string).toByteArray();
        arrby5[2] = arrby7[0];
        arrby5[3] = arrby7[1];
        byte[] arrby8 = new BigInteger(string2).toByteArray();
        arrby6[2] = arrby8[0];
        arrby6[3] = arrby8[1];
        int n10 = 4;
        int n11 = 4;
        int n12 = 2 + (n4 + (2 + (n3 + (n - n5 - n6))));
        int n13 = 0;
        int n14 = 0;
        while (n13 + 1 < n) {
            arrby3[0] = arrby[n13];
            arrby3[1] = arrby[n13 + 1];
            n13+=2;
            if (-1 != arrby3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == arrby3[1]) {
                int n15 = n3 + 2;
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby2, (int)n14, (int)n15);
                int n16 = n14 + (n3 + 2);
                System.arraycopy((Object)arrby6, (int)0, (Object)arrby2, (int)n16, (int)(n4 + 2));
                int n17 = n16 + (n4 + 2);
                System.arraycopy((Object)arrby, (int)(n13 - 2), (Object)arrby2, (int)n17, (int)(n - (n13 - 2)));
                return n12;
            }
            if (-40 == arrby3[1]) {
                arrby2[n13 - 2] = arrby3[0];
                arrby2[n13 - 1] = arrby3[1];
                n14+=2;
                continue;
            }
            byte[] arrby9 = new byte[]{arrby[n13], arrby[n13 + 1]};
            int n18 = new BigInteger(arrby9).intValue();
            if (-60 == arrby3[1]) {
                int n19 = n13 + 2;
                int n20 = n18 - 2;
                System.arraycopy((Object)arrby, (int)n19, (Object)arrby6, (int)n11, (int)n20);
                n11+=n18 - 2;
            } else if (-37 == arrby3[1]) {
                int n21 = n13 + 2;
                int n22 = n18 - 2;
                System.arraycopy((Object)arrby, (int)n21, (Object)arrby5, (int)n10, (int)n22);
                n10+=n18 - 2;
            } else {
                int n23 = n13 - 2;
                int n24 = n18 + 2;
                System.arraycopy((Object)arrby, (int)n23, (Object)arrby2, (int)n14, (int)n24);
                n14+=n18 + 2;
            }
            n13+=n18;
        }
        return n12;
    }

    public static boolean isCoverPicture(int n) {
        if (n == 2) {
            return true;
        }
        return false;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    public static MediaSavingResult saveImage(byte[] arrby, Uri uri, Context context) {
        if (uri == null) {
            return MediaSavingResult.FAIL;
        }
        MediaSavingResult mediaSavingResult = MediaSavingResult.FAIL;
        OutputStream outputStream = null;
        outputStream = context.getContentResolver().openOutputStream(uri);
        outputStream.write(arrby);
        outputStream.close();
        mediaSavingResult = MediaSavingResult.SUCCESS;
        if (outputStream == null) return mediaSavingResult;
        try {
            outputStream.flush();
            outputStream.close();
            return mediaSavingResult;
        }
        catch (IOException var16_5) {
            CameraLogger.e(TAG, "Failed to close output stream.", (Throwable)var16_5);
            return mediaSavingResult;
        }
        catch (IOException iOException) {
            CameraLogger.e(TAG, "Failed to write image data.", (Throwable)iOException);
            if (outputStream == null) return mediaSavingResult;
            {
                catch (Throwable throwable) {
                    if (outputStream == null) throw throwable;
                    try {
                        outputStream.flush();
                        outputStream.close();
                    }
                    catch (IOException var6_11) {
                        CameraLogger.e(TAG, "Failed to close output stream.", (Throwable)var6_11);
                        throw throwable;
                    }
                    throw throwable;
                }
            }
            try {
                outputStream.flush();
                outputStream.close();
                return mediaSavingResult;
            }
            catch (IOException var14_7) {
                CameraLogger.e(TAG, "Failed to close output stream.", (Throwable)var14_7);
                return mediaSavingResult;
            }
            catch (SecurityException securityException) {
                CameraLogger.e(TAG, "Failed to write image data.", (Throwable)securityException);
                if (outputStream == null) return mediaSavingResult;
                try {
                    outputStream.flush();
                    outputStream.close();
                    return mediaSavingResult;
                }
                catch (IOException var10_9) {
                    CameraLogger.e(TAG, "Failed to close output stream.", (Throwable)var10_9);
                    return mediaSavingResult;
                }
            }
        }
    }

    /*
     * Exception decompiling
     */
    public static MediaSavingResult saveImage(byte[] var0_1, String var1, int var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static MediaSavingResult saveYuvImage(YuvImage yuvImage, String string, SavingRequest savingRequest) {
        ThreadSafeOutputStream threadSafeOutputStream;
        if (string == null) {
            return MediaSavingResult.FAIL;
        }
        MediaSavingResult mediaSavingResult = MediaSavingResult.SUCCESS;
        try {
            ThreadSafeOutputStream threadSafeOutputStream2;
            threadSafeOutputStream = threadSafeOutputStream2 = new ThreadSafeOutputStream((OutputStream)new FileOutputStream(string));
        }
        catch (FileNotFoundException var12_7) {
            CameraLogger.e(TAG, "saveImage failed.", (Throwable)var12_7);
            mediaSavingResult = MediaSavingResult.FAIL;
            threadSafeOutputStream = null;
        }
        catch (Exception var10_8) {
            CameraLogger.e(TAG, "saveImage failed.", (Throwable)var10_8);
            mediaSavingResult = MediaSavingResult.FAIL;
            threadSafeOutputStream = null;
        }
        if (mediaSavingResult == MediaSavingResult.SUCCESS) {
            mediaSavingResult = MediaSavingUtil.convertYuvToJpegOutputStream(yuvImage, (OutputStream)threadSafeOutputStream, savingRequest);
        }
        if (mediaSavingResult == MediaSavingResult.SUCCESS) {
            try {
                threadSafeOutputStream.flush();
            }
            catch (IOException var8_9) {
                CameraLogger.e(TAG, "Flushing output stream failed.", (Throwable)var8_9);
                mediaSavingResult = MediaSavingResult.FAIL;
            }
        }
        if (threadSafeOutputStream == null) return mediaSavingResult;
        try {
            threadSafeOutputStream.close();
            return mediaSavingResult;
        }
        catch (IOException var6_6) {
            CameraLogger.e(TAG, "Closing output stream failed.", (Throwable)var6_6);
            return MediaSavingResult.FAIL;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int skipApp0Marker(byte[] arrby) {
        if (arrby.length >= 2 && arrby[0] == SOI_MARKER[0] && arrby[1] == SOI_MARKER[1]) {
            int n;
            for (int i = 0 + 2; i < -3 + arrby.length && arrby[i] == APP0_MARKER[0]; i+=n) {
                n = (arrby[i + 2] << 8) + (255 & arrby[i + 3]);
                if (arrby[i + 1] != APP0_MARKER[1]) continue;
                return n + (i + APP0_MARKER.length);
            }
        }
        return -1;
    }
}

