/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.mediasaving;

import com.sonyericsson.cameracommon.R;

public final class MediaSavingResult
extends Enum<MediaSavingResult> {
    private static final /* synthetic */ MediaSavingResult[] $VALUES;
    public static final /* enum */ MediaSavingResult FAIL;
    public static final /* enum */ MediaSavingResult FAIL_MEMORY_FULL;
    public static final /* enum */ MediaSavingResult SUCCESS;
    public final int mResultCode;
    public final boolean mSuccess;
    public final int mTextId;

    static {
        SUCCESS = new MediaSavingResult(true, -1, -1);
        FAIL = new MediaSavingResult(false, R.string.cam_strings_store_fail_txt, 0);
        FAIL_MEMORY_FULL = new MediaSavingResult(false, R.string.cam_strings_memory_full_save_failed_txt, 0);
        MediaSavingResult[] arrmediaSavingResult = new MediaSavingResult[]{SUCCESS, FAIL, FAIL_MEMORY_FULL};
        $VALUES = arrmediaSavingResult;
    }

    private MediaSavingResult(boolean bl, int n2, int n3) {
        super(string, n);
        this.mSuccess = bl;
        this.mTextId = n2;
        this.mResultCode = n3;
    }

    public static MediaSavingResult valueOf(String string) {
        return (MediaSavingResult)Enum.valueOf((Class)MediaSavingResult.class, (String)string);
    }

    public static MediaSavingResult[] values() {
        return (MediaSavingResult[])$VALUES.clone();
    }
}

