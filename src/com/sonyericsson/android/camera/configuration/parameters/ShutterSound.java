/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import java.util.ArrayList;

public final class ShutterSound
extends Enum<ShutterSound>
implements ParameterValue {
    private static final /* synthetic */ ShutterSound[] $VALUES;
    public static final /* enum */ ShutterSound OFF;
    public static final /* enum */ ShutterSound SOUND1;
    private static final int sParameterTextId = 2131362102;
    private final Boolean mBooleanValue;
    private final String mDirectoryName;
    private final int mIconId;
    private final int mTextId;

    static {
        SOUND1 = new ShutterSound(-1, 2131361806, true, "sound1/");
        OFF = new ShutterSound(-1, 2131361807, false, "sound0/");
        ShutterSound[] arrshutterSound = new ShutterSound[]{SOUND1, OFF};
        $VALUES = arrshutterSound;
    }

    private ShutterSound(int n2, int n3, Boolean bl, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mBooleanValue = bl;
        this.mDirectoryName = string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ShutterSound[] getOptions(boolean bl) {
        ArrayList arrayList = new ArrayList();
        for (ShutterSound shutterSound : ShutterSound.values()) {
            if (shutterSound.mBooleanValue.booleanValue()) {
                arrayList.add((Object)shutterSound);
                continue;
            }
            if (bl) continue;
            arrayList.add((Object)shutterSound);
        }
        return (ShutterSound[])arrayList.toArray((Object[])new ShutterSound[0]);
    }

    public static ShutterSound valueOf(String string) {
        return (ShutterSound)Enum.valueOf((Class)ShutterSound.class, (String)string);
    }

    public static ShutterSound[] values() {
        return (ShutterSound[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((ShutterSound)this);
    }

    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    public String getDirectoryName() {
        return this.mDirectoryName;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.SHUTTER_SOUND;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362102;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return arrparameterValue[0];
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    @Override
    public String getValue() {
        return this.toString();
    }
}

