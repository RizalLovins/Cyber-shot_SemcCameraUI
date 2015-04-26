/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.YuvImage
 *  com.sonymobile.imageprocessor.jpegencoder.JpegEncoder
 *  com.sonymobile.imageprocessor.jpegencoder.JpegEncoder$Parameters
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Iterator
 */
package com.sonyericsson.cameracommon.mediasaving.yuv2jpeg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import com.sonyericsson.cameracommon.mediasaving.yuv2jpeg.ExifFactory;
import com.sonyericsson.cameracommon.mediasaving.yuv2jpeg.ExifInfo;
import com.sonyericsson.cameracommon.mediasaving.yuv2jpeg.ExifOption;
import com.sonyericsson.cameracommon.mediasaving.yuv2jpeg.IntegrationMakerException;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonymobile.imageprocessor.jpegencoder.JpegEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Yuv2ExifJpegConvertor {
    private static final byte[] APP0_MARKER;
    private static final byte[] APP1_MARKER;
    private static final byte[] EXIF_BYTE_ORDER_BE;
    private static final byte[] EXIF_BYTE_ORDER_LE;
    private static final int EXIF_BYTE_ORDER_OFFSET = 11;
    private static final byte[] EXIF_CODE;
    private static final int EXIF_THUMBNAIL_HEIGHT = 120;
    private static final int EXIF_THUMBNAIL_WIDTH = 160;
    public static final int HEADER_MARGIN = 1024;
    private static final int JPEG_COMPRESS_QUALITY = 93;
    private static final byte MARKER_CODE = -1;
    private static final int MARKER_SIZE = 2;
    private static final int M_DHT = 196;
    private static final int M_DQT = 219;
    private static final int M_MARKER = 255;
    private static final int M_SOI = 216;
    private static final int M_SOS = 218;
    private static final int SEGMENT_LENGTH_AREA_SIZE = 2;
    private static final int SIZE_OF_ONE_BYTE = 8;
    private static final byte[] SOI_MARKER;
    private static final String TAG = "Yuv2ExifJpegConvertor";

    static {
        SOI_MARKER = new byte[]{-1, -40};
        APP0_MARKER = new byte[]{-1, -32};
        APP1_MARKER = new byte[]{-1, -31};
        EXIF_CODE = new byte[]{69, 120, 105, 102, 0, 0};
        EXIF_BYTE_ORDER_BE = new byte[]{77, 77};
        EXIF_BYTE_ORDER_LE = new byte[]{73, 73};
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int addExifHeader(OutputStream outputStream, ExifInfo exifInfo, byte[] arrby) {
        int n2;
        ExifOption exifOption = ExifOption.create(exifInfo, arrby);
        byte[] arrby2 = new byte[ExifFactory.getLength(exifOption) + (int)exifOption.mThumbnailDataLength];
        int n = ExifFactory.generate(arrby2, exifOption);
        if (n <= 0) return n;
        try {
            outputStream.write(SOI_MARKER);
            outputStream.write(arrby2, 0, n);
            outputStream.flush();
            n2 = SOI_MARKER.length;
        }
        catch (IOException iOException) {
            CameraLogger.e("Yuv2ExifJpegConvertor", "Add exif header failed.", (Throwable)iOException);
            return -1;
        }
        n+=n2;
        return n;
    }

    public static byte[] addExifToPlainJpeg(byte[] arrby, ExifInfo exifInfo) {
        if (arrby == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby2 = Yuv2ExifJpegConvertor.getExifThumbnail(arrby, exifInfo);
        if (Yuv2ExifJpegConvertor.addExifHeader((OutputStream)byteArrayOutputStream, exifInfo, arrby2) > 0) {
            try {
                Yuv2ExifJpegConvertor.appendOnlyImageFromJpegData((OutputStream)byteArrayOutputStream, arrby, arrby.length);
            }
            catch (IntegrationMakerException var5_4) {
                CameraLogger.e("Yuv2ExifJpegConvertor", "Failed to append jpeg data.", (Throwable)var5_4);
                return null;
            }
            return byteArrayOutputStream.toByteArray();
        }
        CameraLogger.e("Yuv2ExifJpegConvertor", "convertYuvToExifJpeg():[Add EXIF header failed.]");
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void appendImageInExifFormat(byte[] arrby, int n, OutputStream outputStream) throws IOException, IntegrationMakerException {
        int n2;
        int n3 = n;
        boolean bl = false;
        boolean bl2 = false;
        for (; n3 < -3 + arrby.length; n3+=n2 + 2) {
            if (arrby[n3] != -1) {
                throw new IntegrationMakerException("Invalid marker identifier code: " + Integer.toHexString((int)(255 & arrby[n3])));
            }
            n2 = Yuv2ExifJpegConvertor.getSegmentLength(arrby, n3);
            if (arrby[n3 + 1] == -38) {
                outputStream.write(arrby, n3, arrby.length - n3);
                return;
            }
            if (arrby[n3 + 1] == -37) {
                if (bl) continue;
                Yuv2ExifJpegConvertor.appendInOneSegment(arrby, n3, outputStream);
                bl = true;
                continue;
            }
            if (arrby[n3 + 1] == -60) {
                if (bl2) continue;
                Yuv2ExifJpegConvertor.appendInOneSegment(arrby, n3, outputStream);
                bl2 = true;
                continue;
            }
            outputStream.write(arrby, n3, n2 + 2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void appendInOneSegment(byte[] arrby, int n, OutputStream outputStream) throws IOException, IntegrationMakerException {
        int n2;
        int n3;
        if (arrby == null || n2 + 1 > arrby.length) {
            throw new IntegrationMakerException("Invalid Segment.");
        }
        byte by = arrby[n2 + 1];
        ArrayList arrayList = new ArrayList();
        for (n2 = n; n2 < -3 + arrby.length; n2+=n3 + 2) {
            if (arrby[n2] != -1) {
                throw new IntegrationMakerException("Invalid marker identifier code: " + Integer.toHexString((int)(255 & arrby[n2])));
            }
            if (arrby[n2 + 1] == -38) break;
            n3 = Yuv2ExifJpegConvertor.getSegmentLength(arrby, n2);
            if (arrby[n2 + 1] != by) continue;
            arrayList.add((Object)new SegmentInfo(n2, n3));
        }
        outputStream.write(new byte[]{-1, by});
        int n4 = 2;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            n4+=-2 + ((SegmentInfo)iterator.next()).getSegmentLength();
        }
        outputStream.write(Yuv2ExifJpegConvertor.convertToSegmentLengthBytes(n4));
        Iterator iterator2 = arrayList.iterator();
        do {
            if (!iterator2.hasNext()) {
                arrayList.clear();
                return;
            }
            SegmentInfo segmentInfo = (SegmentInfo)iterator2.next();
            outputStream.write(arrby, 2 + (2 + segmentInfo.getIndex()), -2 + segmentInfo.getSegmentLength());
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void appendOnlyImageFromJpegData(OutputStream outputStream, byte[] arrby, int n) throws IntegrationMakerException {
        int n2 = Yuv2ExifJpegConvertor.skipApp0OrApp1Marker(arrby);
        if (n2 <= 0) return;
        int n3 = Yuv2ExifJpegConvertor.getSegmentNum(arrby, -37);
        int n4 = Yuv2ExifJpegConvertor.getSegmentNum(arrby, -60);
        if (n3 > 1 || n4 > 1) {
            try {
                Yuv2ExifJpegConvertor.appendImageInExifFormat(arrby, n2, outputStream);
                outputStream.flush();
                return;
            }
            catch (IOException var6_6) {
                CameraLogger.e("Yuv2ExifJpegConvertor", "Failed to append image data in exif format.", (Throwable)var6_6);
                return;
            }
        }
        int n5 = n - n2;
        try {
            outputStream.write(arrby, n2, n5);
            outputStream.flush();
            return;
        }
        catch (IOException var9_8) {
            CameraLogger.e("Yuv2ExifJpegConvertor", "Append image data failed.", (Throwable)var9_8);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] convertBitmapToExifJpeg(Bitmap bitmap, ExifInfo exifInfo) {
        byte[] arrby;
        if (bitmap == null || (arrby = Yuv2ExifJpegConvertor.convertBitmapToPlainJpeg(bitmap)) == null) {
            return null;
        }
        return Yuv2ExifJpegConvertor.addExifToPlainJpeg(arrby, exifInfo);
    }

    public static byte[] convertBitmapToPlainJpeg(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] convertToSegmentLengthBytes(int n) {
        byte[] arrby = new byte[2];
        arrby[1] = (byte)(n & 255);
        arrby[0] = (byte)(255 & n >>> 8);
        return arrby;
    }

    public static byte[] convertYuvToExifJpeg(YuvImage yuvImage, ExifInfo exifInfo) {
        byte[] arrby = Yuv2ExifJpegConvertor.convertYuvToPlainJpeg(yuvImage, exifInfo);
        if (arrby == null) {
            return null;
        }
        return Yuv2ExifJpegConvertor.addExifToPlainJpeg(arrby, exifInfo);
    }

    public static byte[] convertYuvToPlainJpeg(YuvImage yuvImage, ExifInfo exifInfo) {
        try {
            Class.forName((String)"com.sonymobile.imageprocessor.jpegencoder.JpegEncoder");
            JpegEncoder jpegEncoder = JpegEncoder.create();
            byte[] arrby = Yuv2ExifJpegConvertor.convertYuvToPlainJpegWithImageProcessor(jpegEncoder, yuvImage, exifInfo.getWidth(), exifInfo.getHeight());
            jpegEncoder.release();
            return arrby;
        }
        catch (ClassNotFoundException var2_4) {
            return Yuv2ExifJpegConvertor.convertYuvToPlainJpegWithoutImageProcessor(yuvImage, exifInfo.getWidth(), exifInfo.getHeight());
        }
    }

    protected static byte[] convertYuvToPlainJpegWithImageProcessor(JpegEncoder jpegEncoder, YuvImage yuvImage, int n, int n2) {
        JpegEncoder.Parameters parameters = new JpegEncoder.Parameters(n, n2, yuvImage.getYuvFormat(), 93);
        return jpegEncoder.process((byte[])yuvImage.getYuvData(), (JpegEncoder.Parameters)parameters).imageBuffer;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static byte[] convertYuvToPlainJpegWithoutImageProcessor(YuvImage yuvImage, int n, int n2) {
        int n3;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (!yuvImage.compressToJpeg(new Rect(0, 0, n, n2), 93, (OutputStream)byteArrayOutputStream)) {
            CameraLogger.e("Yuv2ExifJpegConvertor", "convertYuvToExifJpeg():[Compress failed]");
            return null;
        }
        byte[] arrby = byteArrayOutputStream.toByteArray();
        byte[] arrby2 = new byte[1024 + arrby.length];
        try {
            int n4;
            n3 = n4 = Yuv2ExifJpegConvertor.integrateJfif(arrby, arrby2);
        }
        catch (IntegrationMakerException var6_8) {
            CameraLogger.w("Yuv2ExifJpegConvertor", "convertYuvToExifJpeg():[JFIF integration failed.]");
            var6_8.printStackTrace();
            arrby2 = byteArrayOutputStream.toByteArray();
            n3 = arrby2.length;
            return Arrays.copyOf((byte[])arrby2, (int)n3);
        }
        do {
            return Arrays.copyOf((byte[])arrby2, (int)n3);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ExifInfo.ByteOrder getExifByteOrder(byte[] arrby) {
        if (arrby != null && arrby.length >= 2 && arrby[0] == SOI_MARKER[0] && arrby[1] == SOI_MARKER[1]) {
            int n = 0 + 2;
            while (n + 11 < arrby.length && arrby[n] == -1 && arrby[n + 1] != -38) {
                if (arrby[n + 1] == APP1_MARKER[1] && arrby[n + 4] == EXIF_CODE[0] && arrby[n + 5] == EXIF_CODE[1] && arrby[n + 6] == EXIF_CODE[2] && arrby[n + 7] == EXIF_CODE[3] && arrby[n + 8] == EXIF_CODE[4] && arrby[n + 9] == EXIF_CODE[5]) {
                    if (arrby[n + 10] == EXIF_BYTE_ORDER_BE[0] && arrby[n + 11] == EXIF_BYTE_ORDER_BE[1]) {
                        return ExifInfo.ByteOrder.BIG_ENDIAN;
                    }
                    if (arrby[n + 10] != EXIF_BYTE_ORDER_LE[0] || arrby[n + 11] != EXIF_BYTE_ORDER_LE[1]) break;
                    return ExifInfo.ByteOrder.LITTLE_ENDIAN;
                }
                n+=2 + Yuv2ExifJpegConvertor.getSegmentLength(arrby, n);
            }
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static byte[] getExifThumbnail(byte[] arrby, ExifInfo exifInfo) {
        int n;
        int n2;
        int n3;
        float f;
        int n4;
        int n5 = Math.max((int)(exifInfo.getWidth() / 160), (int)(exifInfo.getHeight() / 120));
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
            n4 = n9 = Yuv2ExifJpegConvertor.integrateJfif(arrby2, arrby3);
        }
        catch (IntegrationMakerException var20_23) {
            arrby3 = arrby2;
            n4 = arrby3.length;
        }
        int n10 = Yuv2ExifJpegConvertor.skipApp0OrApp1Marker(arrby3);
        byte[] arrby4 = new byte[n4 - n10 + SOI_MARKER.length];
        arrby4[0] = SOI_MARKER[0];
        arrby4[1] = SOI_MARKER[1];
        System.arraycopy((Object)arrby3, (int)n10, (Object)arrby4, (int)SOI_MARKER.length, (int)(arrby4.length - SOI_MARKER.length));
        try {
            byteArrayOutputStream.close();
            return arrby4;
        }
        catch (IOException var24_24) {
            CameraLogger.e("Yuv2ExifJpegConvertor", "Closing output stream failed.", (Throwable)var24_24);
            return arrby4;
        }
    }

    private static int getSegmentLength(byte[] arrby, int n) {
        return ((255 & arrby[n + 2]) << 8) + (255 & arrby[n + 3]);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int getSegmentNum(byte[] arrby, byte by) {
        if (arrby == null || arrby.length < 2 || arrby[0] != -1 || arrby[1] != -40) {
            CameraLogger.e("Yuv2ExifJpegConvertor", "Invalid SOI marker.");
            return -1;
        }
        int n = 0;
        for (int i = 0 + 2; i < -3 + arrby.length; i+=2 + Yuv2ExifJpegConvertor.getSegmentLength((byte[])arrby, (int)i)) {
            if (arrby[i] != -1) {
                CameraLogger.e("Yuv2ExifJpegConvertor", "Invalid marker identifier code: " + Integer.toHexString((int)(255 & arrby[i])));
                return -1;
            }
            if (arrby[i + 1] == -38) return n;
            if (arrby[i + 1] != by) continue;
            ++n;
        }
        return n;
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

    /*
     * Enabled aggressive block sorting
     */
    public static int skipApp0OrApp1Marker(byte[] arrby) {
        if (arrby.length >= 2 && arrby[0] == SOI_MARKER[0] && arrby[1] == SOI_MARKER[1]) {
            int n;
            for (int i = 0 + 2; i < -3 + arrby.length && (arrby[i] == APP0_MARKER[0] || arrby[i] == APP1_MARKER[0]); i+=n) {
                n = Yuv2ExifJpegConvertor.getSegmentLength(arrby, i);
                if (arrby[i] == APP0_MARKER[0] && arrby[i + 1] == APP0_MARKER[1]) {
                    return n + (i + APP0_MARKER.length);
                }
                if (arrby[i] != APP1_MARKER[0] || arrby[i + 1] != APP1_MARKER[1]) continue;
                return n + (i + APP1_MARKER.length);
            }
        }
        return -1;
    }

    private static class SegmentInfo {
        private final int mIndex;
        private final int mSegmentLength;

        SegmentInfo(int n, int n2) {
            this.mIndex = n;
            this.mSegmentLength = n2;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public int getSegmentLength() {
            return this.mSegmentLength;
        }
    }

}

