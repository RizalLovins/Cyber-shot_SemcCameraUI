/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.Buffer
 *  java.nio.ByteBuffer
 */
package com.sonyericsson.cameracommon.utility;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ByteBufferUtil {
    private static final String TAG = ByteBufferUtil.class.getSimpleName();

    public static byte[] array(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2 = byteBuffer.duplicate();
        byte[] arrby = new byte[byteBuffer2.limit()];
        byteBuffer2.rewind();
        byteBuffer2.get(arrby);
        return arrby;
    }
}

