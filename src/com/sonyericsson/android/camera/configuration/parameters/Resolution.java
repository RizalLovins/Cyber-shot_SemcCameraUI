/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CaptureFrameShape;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.android.camera.util.capability.ResolutionOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Resolution
extends Enum<Resolution>
implements ParameterValue {
    private static final /* synthetic */ Resolution[] $VALUES;
    public static final /* enum */ Resolution EIGHT_MP;
    public static final /* enum */ Resolution EIGHT_MP_WIDE;
    public static final /* enum */ Resolution FIFTEEN_MP_WIDE;
    public static final /* enum */ Resolution FIVE_MP;
    public static final /* enum */ Resolution FIVE_MP_WIDE;
    public static final /* enum */ Resolution HDR_NINE_MP;
    public static final /* enum */ Resolution HDR_SEVEN_MP;
    public static final /* enum */ Resolution HDR_SIX_MP;
    public static final /* enum */ Resolution HDR_TWELVE_MP;
    public static final /* enum */ Resolution HDR_TWENTY_MP;
    public static final /* enum */ Resolution HDR_TWO_MP;
    public static final /* enum */ Resolution HDR_TWO_MP_WIDE;
    public static final /* enum */ Resolution NINE_MP;
    public static final /* enum */ Resolution ONE_MP;
    public static final /* enum */ Resolution ONE_MP_NARROW;
    public static final /* enum */ Resolution SIX_MP;
    public static final /* enum */ Resolution TEN_MP;
    public static final /* enum */ Resolution THIRTEEN_MP;
    public static final /* enum */ Resolution THREE_MP;
    public static final /* enum */ Resolution THREE_MP_WIDE;
    public static final /* enum */ Resolution THREE_POINT_SEVEN_MP_WIDE;
    public static final /* enum */ Resolution TWELVE_MP;
    public static final /* enum */ Resolution TWENTY_MP;
    public static final /* enum */ Resolution TWO_MP;
    public static final /* enum */ Resolution TWO_MP_WIDE;
    public static final /* enum */ Resolution UXGA;
    public static final /* enum */ Resolution VGA;
    private static final int sParameterTextId = 2131361817;
    private final int mIconId;
    private final Rect mPictureRect;
    private final int mTextId;

    static {
        TWENTY_MP = new Resolution(-1, 2131362263, new Rect(0, 0, 5248, 3936));
        HDR_TWENTY_MP = new Resolution(-1, -1, new Rect(0, 0, 4998, 3748));
        FIFTEEN_MP_WIDE = new Resolution(-1, 2131362262, new Rect(0, 0, 5248, 2952));
        THIRTEEN_MP = new Resolution(-1, 2131362261, new Rect(0, 0, 4128, 3096));
        HDR_TWELVE_MP = new Resolution(-1, 2131362260, new Rect(0, 0, 3920, 2940));
        TWELVE_MP = new Resolution(-1, -1, new Rect(0, 0, 4000, 3000));
        TEN_MP = new Resolution(-1, -1, new Rect(0, 0, 4128, 2322));
        NINE_MP = new Resolution(-1, -1, new Rect(0, 0, 4000, 2250));
        HDR_NINE_MP = new Resolution(-1, 2131362258, new Rect(0, 0, 3920, 2204));
        EIGHT_MP = new Resolution(-1, 2131362257, new Rect(0, 0, 3264, 2448));
        EIGHT_MP_WIDE = new Resolution(-1, 2131362256, new Rect(0, 0, 3840, 2160));
        HDR_SEVEN_MP = new Resolution(-1, 2131362255, new Rect(0, 0, 3104, 2328));
        SIX_MP = new Resolution(-1, 2131362254, new Rect(0, 0, 3264, 1836));
        HDR_SIX_MP = new Resolution(-1, -1, new Rect(0, 0, 3104, 1746));
        FIVE_MP = new Resolution(-1, 2131362253, new Rect(0, 0, 2592, 1944));
        FIVE_MP_WIDE = new Resolution(-1, 2131362252, new Rect(0, 0, 3104, 1746));
        THREE_MP = new Resolution(-1, 2131362249, new Rect(0, 0, 2048, 1536));
        THREE_MP_WIDE = new Resolution(-1, 2131362250, new Rect(0, 0, 2560, 1440));
        THREE_POINT_SEVEN_MP_WIDE = new Resolution(-1, 2131362251, new Rect(0, 0, 2592, 1458));
        TWO_MP = new Resolution(-1, 2131362248, new Rect(0, 0, 1632, 1224));
        HDR_TWO_MP = new Resolution(-1, 2131362244, new Rect(0, 0, 1520, 1140));
        TWO_MP_WIDE = new Resolution(-1, 2131362247, new Rect(0, 0, 1920, 1080));
        HDR_TWO_MP_WIDE = new Resolution(-1, 2131362245, new Rect(0, 0, 1824, 1026));
        ONE_MP = new Resolution(-1, -1, new Rect(0, 0, 1280, 960));
        ONE_MP_NARROW = new Resolution(-1, -1, new Rect(0, 0, 1280, 720));
        VGA = new Resolution(-1, -1, new Rect(0, 0, 640, 480));
        UXGA = new Resolution(-1, 2131362246, new Rect(0, 0, 1600, 1200));
        Resolution[] arrresolution = new Resolution[]{TWENTY_MP, HDR_TWENTY_MP, FIFTEEN_MP_WIDE, THIRTEEN_MP, HDR_TWELVE_MP, TWELVE_MP, TEN_MP, NINE_MP, HDR_NINE_MP, EIGHT_MP, EIGHT_MP_WIDE, HDR_SEVEN_MP, SIX_MP, HDR_SIX_MP, FIVE_MP, FIVE_MP_WIDE, THREE_MP, THREE_MP_WIDE, THREE_POINT_SEVEN_MP_WIDE, TWO_MP, HDR_TWO_MP, TWO_MP_WIDE, HDR_TWO_MP_WIDE, ONE_MP, ONE_MP_NARROW, VGA, UXGA};
        $VALUES = arrresolution;
    }

    private Resolution(int n2, int n3, Rect rect) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mPictureRect = rect;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Resolution getDefaultValue(CapturingMode capturingMode) {
        ResolutionOptions resolutionOptions = HardwareCapability.getCapability((int)capturingMode.getCameraId()).RESOLUTION_CAPABILITY.get();
        Resolution resolution = HardwareCapability.getInstance().isStillHdrVer3(capturingMode.getCameraId()) ? Resolution.valueOf(resolutionOptions.getHdr3DefaultResolution()) : Resolution.valueOf(resolutionOptions.getDefaultResolution());
        Resolution[] arrresolution = Resolution.getOptions(capturingMode);
        int n = arrresolution.length;
        for (int i = 0; i < n; ++i) {
            if (!arrresolution[i].equals((Object)resolution)) continue;
            if (HardwareCapability.getInstance().isStillHdrVer3(capturingMode.getCameraId())) {
                return Resolution.valueOf(resolutionOptions.getHdr3DefaultResolution());
            }
            return Resolution.valueOf(resolutionOptions.getDefaultResolution());
        }
        return VGA;
    }

    private static Resolution[] getExpectedOptions(String[] arrstring) {
        ArrayList arrayList = new ArrayList();
        if (arrstring != null) {
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)Resolution.valueOf((Class)Resolution.class, (String)arrstring[i]));
            }
        } else {
            return Resolution.values();
        }
        return (Resolution[])arrayList.toArray((Object[])new Resolution[0]);
    }

    public static Resolution[] getHdrDependentOptions(Resolution[] arrresolution, boolean bl, int n) {
        for (int i = 0; i < arrresolution.length; ++i) {
            arrresolution[i] = Resolution.getHdrResolution(arrresolution[i], bl, n);
        }
        return arrresolution;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static Resolution getHdrResolution(Resolution var0_1, boolean var1, int var2_2) {
        var3_3 = var0_1;
        if (HardwareCapability.getInstance().isStillHdrVer3(var2_2)) {
            return var0_1;
        }
        ** if (!var1) goto lbl20
lbl5: // 1 sources:
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$Resolution[var0_1.ordinal()]) {
            case 1: {
                var3_3 = Resolution.HDR_TWENTY_MP;
                ** break;
            }
            case 2: {
                var3_3 = Resolution.HDR_TWELVE_MP;
                ** break;
            }
            case 3: {
                var3_3 = Resolution.HDR_SEVEN_MP;
            }
lbl14: // 4 sources:
            default: {
                ** GOTO lbl19
            }
            case 4: 
        }
        if (var2_2 == 1) {
            var3_3 = Resolution.HDR_TWO_MP_WIDE;
        } else {
            ** GOTO lbl34
        }
lbl19: // 2 sources:
        ** GOTO lbl34
lbl20: // 1 sources:
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$Resolution[var0_1.ordinal()]) {
            default: {
                break;
            }
            case 5: {
                var3_3 = Resolution.TWENTY_MP;
                break;
            }
            case 6: {
                var3_3 = Resolution.THIRTEEN_MP;
                break;
            }
            case 7: {
                var3_3 = Resolution.EIGHT_MP;
                break;
            }
            case 8: {
                var3_3 = Resolution.TWO_MP_WIDE;
            }
        }
lbl34: // 8 sources:
        var4_4 = HardwareCapability.getCapability((int)var2_2).PICTURE_SIZE.get().iterator();
        do {
            if (var4_4.hasNext() == false) return var0_1;
            var5_5 = (Rect)var4_4.next();
        } while (var3_3.mPictureRect.width() != var5_5.width() || var3_3.mPictureRect.height() != var5_5.height());
        return var3_3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Resolution[] getOptions(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return Resolution.getSuperiorAutoOptions(capturingMode.getCameraId());
        }
        ArrayList arrayList = new ArrayList();
        if (capturingMode.getType() == 1) {
            CapabilityList capabilityList = HardwareCapability.getCapability(capturingMode.getCameraId());
            List<Rect> list = capabilityList.PICTURE_SIZE.get();
            if (!list.isEmpty()) {
                String[] arrstring = HardwareCapability.getInstance().isStillHdrVer3(capturingMode.getCameraId()) ? capabilityList.RESOLUTION_CAPABILITY.get().getHdr3ResolutionOptions() : capabilityList.RESOLUTION_CAPABILITY.get().getResolutionOptions();
                for (Resolution resolution : Resolution.getExpectedOptions(arrstring)) {
                    for (Rect rect : list) {
                        if (resolution.mPictureRect.width() != rect.width() || resolution.mPictureRect.height() != rect.height()) continue;
                        arrayList.add((Object)resolution);
                    }
                }
                return (Resolution[])arrayList.toArray((Object[])new Resolution[0]);
            }
        }
        return (Resolution[])arrayList.toArray((Object[])new Resolution[0]);
    }

    public static Resolution getResolutionFromFrameShape(CaptureFrameShape captureFrameShape, int n) {
        int n2 = captureFrameShape.getAspectRatio_x100();
        for (Resolution resolution : Resolution.getSuperiorAutoOptions(n)) {
            Rect rect = resolution.getPictureRect();
            if (n2 != (int)(100.0f * ((float)rect.width() / (float)rect.height()))) continue;
            return resolution;
        }
        throw new UnsupportedOperationException("Unsupported shape or resolution");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Resolution[] getSuperiorAutoOptions(int n) {
        ArrayList arrayList = new ArrayList();
        CapabilityList capabilityList = HardwareCapability.getCapability(n);
        List<Rect> list = capabilityList.PICTURE_SIZE.get();
        for (Resolution resolution : Resolution.getExpectedOptions(capabilityList.RESOLUTION_CAPABILITY.get().getSuperiorAutoResolutionOptions())) {
            for (Rect rect : list) {
                if (resolution.mPictureRect.width() != rect.width() || resolution.mPictureRect.height() != rect.height()) continue;
                arrayList.add((Object)resolution);
            }
        }
        return (Resolution[])arrayList.toArray((Object[])new Resolution[0]);
    }

    public static Resolution getValueFromPictureSize(int n, int n2) {
        for (Resolution resolution : Resolution.values()) {
            Rect rect = resolution.getPictureRect();
            if (rect.width() != n || rect.height() != n2) continue;
            return resolution;
        }
        return null;
    }

    public static Resolution valueOf(String string) {
        return (Resolution)Enum.valueOf((Class)Resolution.class, (String)string);
    }

    public static Resolution[] values() {
        return (Resolution[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Resolution)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.RESOLUTION;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361817;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    public Rect getPictureRect() {
        return this.mPictureRect;
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

