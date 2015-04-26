/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.nio.charset.Charset
 *  java.util.Calendar
 *  java.util.Locale
 *  java.util.TimeZone
 */
package com.sonyericsson.android.camera.mediasaving;

import android.location.Location;
import com.sonyericsson.android.camera.mediasaving.ExifOption;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ExifFactory {
    private static byte[] APP1_HEADER = new byte[]{-1, -31, 3, 27, 69, 120, 105, 102, 0, 0};
    private static int APP1_LENGTH = 0;
    private static byte[] EXIF_IFD;
    private static byte[] FIRST_IFD;
    private static byte[] GPS_IFD;
    private static final int MAKER_NAME_LIMITATION = 14;
    private static final String TAG = "ExifFactory";
    private static byte[] TIFF_HEADER;
    private static byte[] ZERO_IFD;
    private static byte[] ZERO_IFD_INT;

    static {
        TIFF_HEADER = new byte[]{77, 77, 0, 42, 0, 0, 0, 8};
        ZERO_IFD = new byte[]{0, 10, 1, 15, 0, 2, 0, 0, 0, 0, 0, 0, 0, -122, 1, 16, 0, 2, 0, 0, 0, 30, 0, 0, 0, -108, 1, 18, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 0, -78, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 0, -70, 1, 40, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 1, 50, 0, 2, 0, 0, 0, 20, 0, 0, 0, -62, 2, 19, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, -121, 105, 0, 4, 0, 0, 0, 1, 0, 0, 0, -42, -120, 37, 0, 4, 0, 0, 0, 1, 0, 0, 1, -102, 0, 0, 2, -86, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 0, 0, 1, 0, 0, 0, 72, 0, 0, 0, 1, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 32, 49, 50, 58, 51, 52, 58, 53, 54, 0};
        EXIF_IFD = new byte[]{0, 9, -112, 0, 0, 7, 0, 0, 0, 4, 48, 50, 50, 48, -112, 3, 0, 2, 0, 0, 0, 20, 0, 0, 1, 84, -112, 4, 0, 2, 0, 0, 0, 20, 0, 0, 1, 104, -111, 1, 0, 7, 0, 0, 0, 4, 1, 2, 3, 0, -96, 0, 0, 7, 0, 0, 0, 4, 48, 49, 48, 48, -96, 1, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, -96, 2, 0, 4, 0, 0, 0, 1, 0, 0, 12, -64, -96, 3, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, -96, 5, 0, 4, 0, 0, 0, 1, 0, 0, 1, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 32, 49, 50, 58, 51, 52, 58, 53, 54, 0, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 32, 49, 50, 58, 51, 52, 58, 53, 54, 0};
        ZERO_IFD_INT = new byte[]{0, 2, 0, 1, 0, 2, 0, 0, 0, 4, 82, 57, 56, 0, 0, 2, 0, 7, 0, 0, 0, 4, 48, 49, 48, 48, 0, 0, 0, 0};
        GPS_IFD = new byte[]{0, 12, 0, 0, 0, 1, 0, 0, 0, 4, 2, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0, 2, 78, 0, 0, 0, 0, 2, 0, 5, 0, 0, 0, 3, 0, 0, 2, 48, 0, 3, 0, 2, 0, 0, 0, 2, 69, 0, 0, 0, 0, 4, 0, 5, 0, 0, 0, 3, 0, 0, 2, 72, 0, 5, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 6, 0, 5, 0, 0, 0, 1, 0, 0, 2, 96, 0, 7, 0, 5, 0, 0, 0, 3, 0, 0, 2, 104, 0, 9, 0, 2, 0, 0, 0, 2, 65, 0, 0, 0, 0, 18, 0, 2, 0, 0, 0, 7, 0, 0, 2, -128, 0, 27, 0, 7, 0, 0, 0, 0, 0, 0, 2, -118, 0, 29, 0, 2, 0, 0, 0, 11, 0, 0, 2, -98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 87, 71, 83, 45, 56, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 0, 0};
        FIRST_IFD = new byte[]{0, 7, 1, 3, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 18, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 3, 4, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 3, 12, 1, 40, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 2, 1, 0, 4, 0, 0, 0, 1, 0, 0, 3, 20, 2, 2, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 0, 0, 1, 0, 0, 0, 72, 0, 0, 0, 1};
        APP1_LENGTH = APP1_HEADER.length + TIFF_HEADER.length + ZERO_IFD.length + EXIF_IFD.length + ZERO_IFD_INT.length + GPS_IFD.length + FIRST_IFD.length;
    }

    private static void checkArguments(byte[] arrby, ExifOption exifOption) {
        if (arrby == null || exifOption == null || exifOption.mModel == null || exifOption.mDateTime == null || exifOption.mThumbnailData == null) {
            throw new IllegalArgumentException("can not null");
        }
        if (!(exifOption.mGPSOption == null || exifOption.mGPSOption.hasAltitude())) {
            exifOption.mGPSOption.setAltitude(0.0);
        }
        if ((long)exifOption.mThumbnailData.length < exifOption.mThumbnailDataLength) {
            throw new IllegalArgumentException("thumbnail data length too big");
        }
        if ((long)arrby.length < (long)APP1_LENGTH + exifOption.mThumbnailDataLength) {
            throw new IllegalArgumentException("buffer too short");
        }
        if (exifOption.mModel.length() == 0 || exifOption.mDateTime.length() < "YYYY:MM:DD hh:mm:ss".length()) {
            throw new IllegalArgumentException("model or datetime too short");
        }
        if (exifOption.mModel.length() >= 30 || exifOption.mDateTime.length() > "YYYY:MM:DD hh:mm:ss".length()) {
            throw new IllegalArgumentException("model or datetime too long");
        }
    }

    private static void fillNullValue(byte[] arrby, int n, int n2) {
        for (int i = 0; i < n2; ++i) {
            arrby[n + i] = 0;
        }
    }

    public static int generate(byte[] arrby, ExifOption exifOption) {
        ExifFactory.checkArguments(arrby, exifOption);
        int n = ExifFactory.writeTemplate(arrby);
        ExifFactory.updateMake(arrby, exifOption.mMake);
        ExifFactory.updateModel(arrby, exifOption.mModel);
        ExifFactory.updateOrientation(arrby, exifOption.mOrientation);
        ExifFactory.updateDateTime(arrby, exifOption.mDateTime);
        ExifFactory.updatePixelXDimension(arrby, exifOption.mPixelXDimension);
        ExifFactory.updatePixelYDimension(arrby, exifOption.mPixelYDimension);
        ExifFactory.updateGpsFields(arrby, exifOption.mGPSOption);
        ExifFactory.updateJpegInterchangeFormatLength(arrby, exifOption.mThumbnailDataLength);
        System.arraycopy((Object)exifOption.mThumbnailData, (int)0, (Object)arrby, (int)n, (int)((int)exifOption.mThumbnailDataLength));
        int n2 = (int)((long)n + exifOption.mThumbnailDataLength);
        ExifFactory.updateExifSize(arrby, n2 - 2);
        return n2;
    }

    public static int getLength() {
        return APP1_LENGTH;
    }

    private static void removeGpsInfoFromHeader(byte[] arrby) {
        ExifFactory.writeShortValue(arrby, 8 + APP1_HEADER.length, 9);
        ExifFactory.fillNullValue(arrby, 118 + APP1_HEADER.length, 11);
        ExifFactory.writeLongValue(arrby, 118 + APP1_HEADER.length, 682);
        ExifFactory.fillNullValue(arrby, 410 + APP1_HEADER.length, 272);
    }

    private static void updateDateTime(byte[] arrby, String string) {
        ExifFactory.writeASCIIValue(arrby, 194 + APP1_HEADER.length, string);
        ExifFactory.writeASCIIValue(arrby, 340 + APP1_HEADER.length, string);
        ExifFactory.writeASCIIValue(arrby, 360 + APP1_HEADER.length, string);
    }

    private static void updateExifSize(byte[] arrby, int n) {
        ExifFactory.writeShortValue(arrby, -8 + APP1_HEADER.length, n);
    }

    private static void updateGpsFields(byte[] arrby, Location location) {
        if (location != null && ExifFactory.writeGpsInfoToHeader(arrby, location)) {
            return;
        }
        ExifFactory.removeGpsInfoFromHeader(arrby);
    }

    private static void updateJpegInterchangeFormatLength(byte[] arrby, long l) {
        ExifFactory.writeLongValue(arrby, 764 + APP1_HEADER.length, l);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void updateMake(byte[] arrby, String string) {
        String string2 = string.length() > 14 ? string.substring(0, 14) : string;
        int n = ExifFactory.writeASCIIValue(arrby, 134 + APP1_HEADER.length, string2);
        ExifFactory.writeShortValue(arrby, 16 + APP1_HEADER.length, n + 1);
    }

    private static void updateModel(byte[] arrby, String string) {
        int n = ExifFactory.writeASCIIValue(arrby, 148 + APP1_HEADER.length, string);
        ExifFactory.writeLongValue(arrby, 26 + APP1_HEADER.length, n + 1);
    }

    private static void updateOrientation(byte[] arrby, int n) {
        ExifFactory.writeShortValue(arrby, 42 + APP1_HEADER.length, n);
        ExifFactory.writeShortValue(arrby, 704 + APP1_HEADER.length, n);
    }

    private static void updatePixelXDimension(byte[] arrby, long l) {
        ExifFactory.writeLongValue(arrby, -12 + (308 + APP1_HEADER.length), l);
    }

    private static void updatePixelYDimension(byte[] arrby, long l) {
        ExifFactory.writeLongValue(arrby, -12 + (320 + APP1_HEADER.length), l);
    }

    private static int writeASCIIValue(byte[] arrby, int n, String string) {
        byte[] arrby2 = string.getBytes(Charset.forName((String)"US-ASCII"));
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n, (int)arrby2.length);
        return arrby2.length;
    }

    private static void writeByteValue(byte[] arrby, int n, int n2) {
        arrby[n + 0] = (byte)n2;
    }

    private static boolean writeGpsInfoToHeader(byte[] arrby, Location location) {
        String string;
        String string2;
        String string3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(location.getTime());
        double d = location.getLatitude();
        if (d < 0.0) {
            ExifFactory.writeASCIIValue(arrby, 432 + APP1_HEADER.length, "S");
            d = - d;
        }
        try {
            string2 = Location.convert((double)d, (int)2);
        }
        catch (IllegalArgumentException var5_13) {
            return false;
        }
        String[] arrstring = string2.split(":");
        try {
            ExifFactory.writeRationalValue(arrby, 560 + APP1_HEADER.length, Long.parseLong((String)arrstring[0]), 1);
            ExifFactory.writeRationalValue(arrby, 568 + APP1_HEADER.length, Long.parseLong((String)arrstring[1]), 1);
            ExifFactory.writeRationalValue(arrby, 576 + APP1_HEADER.length, (long)(1000.0f * Float.parseFloat((String)arrstring[2])), 1000);
        }
        catch (NumberFormatException var8_14) {
            return false;
        }
        double d2 = location.getLongitude();
        if (d2 < 0.0) {
            ExifFactory.writeASCIIValue(arrby, 456 + APP1_HEADER.length, "W");
            d2 = - d2;
        }
        try {
            string = Location.convert((double)d2, (int)2);
        }
        catch (IllegalArgumentException var11_15) {
            return false;
        }
        String[] arrstring2 = string.split(":");
        try {
            ExifFactory.writeRationalValue(arrby, 584 + APP1_HEADER.length, Long.parseLong((String)arrstring2[0]), 1);
            ExifFactory.writeRationalValue(arrby, 592 + APP1_HEADER.length, Long.parseLong((String)arrstring2[1]), 1);
            ExifFactory.writeRationalValue(arrby, 600 + APP1_HEADER.length, (long)(1000.0f * Float.parseFloat((String)arrstring2[2])), 1000);
        }
        catch (NumberFormatException var14_16) {
            return false;
        }
        double d3 = location.getAltitude();
        if (d3 < 0.0) {
            ExifFactory.writeByteValue(arrby, 480 + APP1_HEADER.length, 1);
        }
        ExifFactory.writeRationalValue(arrby, 608 + APP1_HEADER.length, (long)(1000.0 * d3), 1000);
        calendar.setTimeZone(TimeZone.getTimeZone((String)"UTC"));
        try {
            ExifFactory.writeRationalValue(arrby, 616 + APP1_HEADER.length, calendar.get(11), 1);
            ExifFactory.writeRationalValue(arrby, 624 + APP1_HEADER.length, 1 + calendar.get(12), 1);
            ExifFactory.writeRationalValue(arrby, 632 + APP1_HEADER.length, 1000 * (long)calendar.get(13), 1000);
            Locale locale = Locale.US;
            Object[] arrobject = new Object[]{calendar.get(1), 1 + calendar.get(2), calendar.get(5)};
            string3 = String.format((Locale)locale, (String)"%04d:%02d:%02d", (Object[])arrobject);
        }
        catch (IllegalArgumentException var17_17) {
            return false;
        }
        ExifFactory.writeASCIIValue(arrby, 670 + APP1_HEADER.length, string3);
        return true;
    }

    private static void writeLongValue(byte[] arrby, int n, long l) {
        arrby[n + 0] = (byte)(l / 0x1000000);
        arrby[n + 1] = (byte)(l / 65536);
        arrby[n + 2] = (byte)(l / 256);
        arrby[n + 3] = (byte)(l % 256);
    }

    private static void writeRationalValue(byte[] arrby, int n, long l, long l2) {
        ExifFactory.writeLongValue(arrby, n + 0, l);
        ExifFactory.writeLongValue(arrby, n + 4, l2);
    }

    private static void writeShortValue(byte[] arrby, int n, int n2) {
        arrby[n + 0] = (byte)(n2 / 256);
        arrby[n + 1] = (byte)(n2 % 256);
    }

    private static int writeTemplate(byte[] arrby) {
        System.arraycopy((Object)APP1_HEADER, (int)0, (Object)arrby, (int)0, (int)APP1_HEADER.length);
        int n = 0 + APP1_HEADER.length;
        System.arraycopy((Object)TIFF_HEADER, (int)0, (Object)arrby, (int)n, (int)TIFF_HEADER.length);
        int n2 = n + TIFF_HEADER.length;
        System.arraycopy((Object)ZERO_IFD, (int)0, (Object)arrby, (int)n2, (int)ZERO_IFD.length);
        int n3 = n2 + ZERO_IFD.length;
        System.arraycopy((Object)EXIF_IFD, (int)0, (Object)arrby, (int)n3, (int)EXIF_IFD.length);
        int n4 = n3 + EXIF_IFD.length;
        System.arraycopy((Object)ZERO_IFD_INT, (int)0, (Object)arrby, (int)n4, (int)ZERO_IFD_INT.length);
        int n5 = n4 + ZERO_IFD_INT.length;
        System.arraycopy((Object)GPS_IFD, (int)0, (Object)arrby, (int)n5, (int)GPS_IFD.length);
        int n6 = n5 + GPS_IFD.length;
        System.arraycopy((Object)FIRST_IFD, (int)0, (Object)arrby, (int)n6, (int)FIRST_IFD.length);
        return n6 + FIRST_IFD.length;
    }
}

