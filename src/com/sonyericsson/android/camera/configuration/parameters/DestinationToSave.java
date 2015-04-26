/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.net.Uri
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Locale
 */
package com.sonyericsson.android.camera.configuration.parameters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.storage.StorageManager;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.cameracommon.mediasaving.StorageUtil;
import java.util.ArrayList;
import java.util.Locale;

public final class DestinationToSave
extends Enum<DestinationToSave>
implements ParameterValue {
    private static final /* synthetic */ DestinationToSave[] $VALUES;
    public static final /* enum */ DestinationToSave EMMC = new DestinationToSave(-1, 2131361864, StorageManager.StorageType.INTERNAL, null);
    public static final /* enum */ DestinationToSave INTERNAL_MASS_STORAGE;
    public static final /* enum */ DestinationToSave MEMORY_CARD;
    public static final /* enum */ DestinationToSave SDCARD;
    private static final String TAG = "DestinationToSave";
    private static int sParameterTextId;
    private static DestinationToSave sPrimaryStorage;
    private final DestinationToSave mCompatibleValue;
    private int mIconId;
    private String mMountPoint;
    private int mTextId;
    private StorageManager.StorageType mType;

    static {
        SDCARD = new DestinationToSave(-1, 2131361865, StorageManager.StorageType.EXTERNAL_CARD, null);
        INTERNAL_MASS_STORAGE = new DestinationToSave(-1, 2131361864, StorageManager.StorageType.INTERNAL, EMMC);
        MEMORY_CARD = new DestinationToSave(-1, 2131361865, StorageManager.StorageType.EXTERNAL_CARD, SDCARD);
        DestinationToSave[] arrdestinationToSave = new DestinationToSave[]{EMMC, SDCARD, INTERNAL_MASS_STORAGE, MEMORY_CARD};
        $VALUES = arrdestinationToSave;
        sParameterTextId = 2131361863;
        sPrimaryStorage = EMMC;
    }

    private DestinationToSave(int n2, int n3, StorageManager.StorageType storageType, DestinationToSave destinationToSave) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mType = storageType;
        this.mCompatibleValue = destinationToSave;
    }

    public static DestinationToSave[] getOptions() {
        ArrayList arrayList = new ArrayList();
        for (DestinationToSave destinationToSave : DestinationToSave.values()) {
            if (destinationToSave.getPath() == null) continue;
            arrayList.add((Object)destinationToSave);
        }
        return (DestinationToSave[])arrayList.toArray((Object[])new DestinationToSave[0]);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static DestinationToSave[] getOptions(Uri uri, String string, Context context) {
        if (string != null) {
            StorageManager.StorageType storageType = StorageUtil.getStorageTypeFromPath(string, context);
            int n = .$SwitchMap$android$os$storage$StorageManager$StorageType[storageType.ordinal()];
            DestinationToSave destinationToSave = null;
            switch (n) {
                case 1: {
                    destinationToSave = EMMC;
                    break;
                }
                case 2: {
                    destinationToSave = SDCARD;
                    break;
                }
            }
            if (destinationToSave == null) return DestinationToSave.getOptions();
            return new DestinationToSave[]{destinationToSave};
        } else {
            if (uri == null) return DestinationToSave.getOptions();
            {
                DestinationToSave[] arrdestinationToSave = new DestinationToSave[]{EMMC};
                return arrdestinationToSave;
            }
        }
    }

    public static DestinationToSave getPrimaryStorage() {
        return sPrimaryStorage;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static DestinationToSave getValueFromPath(String string) {
        if (string == null) {
            return null;
        }
        for (DestinationToSave destinationToSave : DestinationToSave.values()) {
            String string2 = destinationToSave.getPath();
            if (string2 == null) continue;
            if (string.equals((Object)string2)) return destinationToSave;
        }
        return null;
    }

    public static void setMountPoint(Context context, String[] arrstring) {
        block0 : for (String string : arrstring) {
            StorageManager.StorageType storageType = StorageUtil.getStorageTypeFromPath(string, context);
            DestinationToSave[] arrdestinationToSave = DestinationToSave.values();
            int n = arrdestinationToSave.length;
            int n2 = 0;
            do {
                if (n2 >= n) continue block0;
                DestinationToSave destinationToSave = arrdestinationToSave[n2];
                if (destinationToSave.mCompatibleValue == null) {
                    if (storageType == destinationToSave.getType()) {
                        destinationToSave.mMountPoint = string;
                        continue block0;
                    }
                    if (storageType == StorageManager.StorageType.UNKNOWN) {
                        destinationToSave.mMountPoint = "/dev/null";
                    }
                }
                ++n2;
            } while (true);
        }
        DestinationToSave.updatePrimaryStorage(context);
    }

    private static void updatePrimaryStorage(Context context) {
        String string = context.getResources().getString(2131361794).toLowerCase(Locale.US);
        String string2 = SDCARD.getType().name().toLowerCase(Locale.US);
        if ((EMMC.getPath() == null || string.equals((Object)string2)) && SDCARD.getPath() != null) {
            sPrimaryStorage = SDCARD;
        }
    }

    public static DestinationToSave valueOf(String string) {
        return (DestinationToSave)Enum.valueOf((Class)DestinationToSave.class, (String)string);
    }

    public static DestinationToSave[] values() {
        return (DestinationToSave[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((DestinationToSave)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.DESTINATION_TO_SAVE;
    }

    @Override
    public int getParameterKeyTextId() {
        return sParameterTextId;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    public String getPath() {
        return this.mMountPoint;
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        if (arrparameterValue != null) {
            DestinationToSave destinationToSave = (DestinationToSave)parameterValue;
            if (destinationToSave.mCompatibleValue == null) {
                return destinationToSave;
            }
            int n = arrparameterValue.length;
            for (int i = 0; i < n; ++i) {
                ParameterValue parameterValue2 = arrparameterValue[i];
                if (parameterValue2 != destinationToSave.mCompatibleValue) continue;
                return parameterValue2;
            }
            if (arrparameterValue.length > 0) {
                return arrparameterValue[0];
            }
        }
        return DestinationToSave.getPrimaryStorage();
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    public StorageManager.StorageType getType() {
        return this.mType;
    }

    @Override
    public String getValue() {
        return this.getPath();
    }

}

