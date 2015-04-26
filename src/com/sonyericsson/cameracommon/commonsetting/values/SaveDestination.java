/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  android.os.storage.StorageVolume
 *  java.io.FileNotFoundException
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.cameracommon.commonsetting.values;

import android.content.Context;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class SaveDestination
extends Enum<SaveDestination>
implements CommonSettingValue {
    private static final /* synthetic */ SaveDestination[] $VALUES;
    public static final /* enum */ SaveDestination EMMC = new SaveDestination(-1, R.string.cam_strings_save_destination_ims_txt, null, "emmc", StorageManager.StorageType.INTERNAL);
    public static final /* enum */ SaveDestination SDCARD = new SaveDestination(-1, R.string.cam_strings_save_destination_sd_txt, null, "sdcard", StorageManager.StorageType.EXTERNAL_CARD);
    private static final String TAG;
    private static int sParameterTextId;
    private static SaveDestination sPrimaryStorage;
    private final SaveDestination mCompatibleValue;
    private int mIconId;
    private final String mProviderValue;
    private int mTextId;
    private final StorageManager.StorageType mType;

    static {
        SaveDestination[] arrsaveDestination = new SaveDestination[]{EMMC, SDCARD};
        $VALUES = arrsaveDestination;
        TAG = SaveDestination.class.getSimpleName();
        sParameterTextId = R.string.cam_strings_save_destination_txt;
        sPrimaryStorage = EMMC;
    }

    private SaveDestination(int n2, int n3, SaveDestination saveDestination, String string2, StorageManager.StorageType storageType) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mCompatibleValue = saveDestination;
        this.mProviderValue = string2;
        this.mType = storageType;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    public static List<SaveDestination> getOptions(Context var0) {
        var1_1 = (StorageManager)var0.getSystemService("storage");
        var2_2 = new ArrayList();
        var3_3 = var1_1.getVolumeList();
        var4_4 = var3_3.length;
        var5_5 = 0;
        block2 : do {
            if (var5_5 >= var4_4) return var2_2;
            var6_12 = var3_3[var5_5].getPath();
            try {
                var9_8 = var1_1.getVolumeType(var6_12);
                var10_10 = SaveDestination.values();
                var11_6 = var10_10.length;
                var12_9 = 0;
            }
            catch (FileNotFoundException var7_11) {
                CameraLogger.e(SaveDestination.TAG, "No StorageType found for the path: " + var6_12, (Throwable)var7_11);
                ** GOTO lbl-1000
            }
            do {
                if (var12_9 < var11_6) {
                    var13_7 = var10_10[var12_9];
                    if (var9_8 == var13_7.mType) {
                        var2_2.add((Object)var13_7);
                    }
                } else lbl-1000: // 3 sources:
                {
                    ++var5_5;
                    continue block2;
                }
                ++var12_9;
            } while (true);
            break;
        } while (true);
    }

    public static SaveDestination getPrimaryStorage() {
        return sPrimaryStorage;
    }

    public static SaveDestination valueOf(String string) {
        return (SaveDestination)Enum.valueOf((Class)SaveDestination.class, (String)string);
    }

    public static SaveDestination[] values() {
        return (SaveDestination[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.SAVE_DESTINATION;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getParameterKeyTextId() {
        return sParameterTextId;
    }

    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public String getProviderValue() {
        return this.mProviderValue;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    public SaveDestination isCompatibleValue() {
        return this.mCompatibleValue;
    }
}

