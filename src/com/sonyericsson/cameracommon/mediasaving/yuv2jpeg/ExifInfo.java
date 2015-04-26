/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.mediasaving.yuv2jpeg;

import android.location.Location;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;

public class ExifInfo {
    private ByteOrder mByteOrder;
    private final int mHeight;
    private final Location mLocation;
    private final int mOrientation;
    private long mTimestamp;
    private final int mWidth;

    public ExifInfo(long l, int n, Location location, int n2, int n3) {
        this.mTimestamp = 0;
        this.mByteOrder = ByteOrder.BIG_ENDIAN;
        this.mTimestamp = l;
        this.mOrientation = n;
        this.mLocation = location;
        this.mWidth = n2;
        this.mHeight = n3;
    }

    public ExifInfo(long l, int n, Location location, int n2, int n3, ByteOrder byteOrder) {
        this(l, n, location, n2, n3);
        this.mByteOrder = byteOrder;
    }

    public ExifInfo(SavingRequest savingRequest) {
        this.mTimestamp = 0;
        this.mByteOrder = ByteOrder.BIG_ENDIAN;
        this.mTimestamp = savingRequest.getDateTaken();
        this.mOrientation = savingRequest.common.orientation;
        this.mLocation = savingRequest.common.location;
        this.mWidth = savingRequest.common.width;
        this.mHeight = savingRequest.common.height;
    }

    public ByteOrder getByteOrder() {
        return this.mByteOrder;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setTimestamp(long l) {
        this.mTimestamp = l;
    }

    public static final class ByteOrder
    extends Enum<ByteOrder> {
        private static final /* synthetic */ ByteOrder[] $VALUES;
        public static final /* enum */ ByteOrder BIG_ENDIAN = new ByteOrder();
        public static final /* enum */ ByteOrder LITTLE_ENDIAN = new ByteOrder();

        static {
            ByteOrder[] arrbyteOrder = new ByteOrder[]{BIG_ENDIAN, LITTLE_ENDIAN};
            $VALUES = arrbyteOrder;
        }

        private ByteOrder() {
            super(string, n);
        }

        public static ByteOrder valueOf(String string) {
            return (ByteOrder)Enum.valueOf((Class)ByteOrder.class, (String)string);
        }

        public static ByteOrder[] values() {
            return (ByteOrder[])$VALUES.clone();
        }
    }

}

