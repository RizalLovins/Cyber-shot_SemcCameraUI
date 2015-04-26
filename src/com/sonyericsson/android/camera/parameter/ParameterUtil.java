/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;

public class ParameterUtil {
    private static final String TAG = "ParameterUtil";

    /*
     * Enabled aggressive block sorting
     */
    public static <T extends ParameterValue> T applyCurrentValue(ParameterValueHolder<T> parameterValueHolder, T t) {
        ParameterKey parameterKey = parameterValueHolder.get().getParameterKey();
        if (parameterValueHolder.getRecommendedValue() != null) {
            parameterValueHolder.applyCurrentValue();
        } else {
            parameterValueHolder.set(t);
        }
        parameterKey.setSelectability(ParameterSelectability.SELECTABLE);
        return parameterValueHolder.get();
    }

    public static <T extends ParameterValue> T applyRecommendedValue(ParameterValueHolder<T> parameterValueHolder, T t) {
        ParameterKey parameterKey = t.getParameterKey();
        parameterValueHolder.applyRecommendedValue(t);
        parameterKey.setSelectability(ParameterSelectability.SELECTABLE);
        return t;
    }

    public static <T extends ParameterValue> T forceChange(ParameterValueHolder<T> parameterValueHolder, T t) {
        ParameterKey parameterKey = t.getParameterKey();
        if (!parameterKey.isInvalid()) {
            parameterValueHolder.forceChange(t);
            parameterKey.setSelectability(ParameterSelectability.FORCE_CHANGED);
        }
        return t;
    }

    public static <T extends ParameterValue> T getPrimaryValue(T t, T t2, T[] arrT) {
        int n = arrT.length;
        for (int i = 0; i < n; ++i) {
            if (t != arrT[i]) continue;
            return t;
        }
        return t2;
    }

    public static <T extends ParameterValue> T reset(ParameterValueHolder<T> parameterValueHolder) {
        ParameterKey parameterKey = parameterValueHolder.get().getParameterKey();
        if (!parameterKey.isInvalid()) {
            parameterValueHolder.reset();
            parameterKey.setSelectability(ParameterSelectability.SELECTABLE);
        }
        return parameterValueHolder.get();
    }

    public static <T extends ParameterValue> T reset(ParameterValueHolder<T> parameterValueHolder, T t) {
        ParameterKey parameterKey = t.getParameterKey();
        if (!parameterKey.isInvalid()) {
            parameterValueHolder.reset();
            parameterValueHolder.set(t);
            parameterKey.setSelectability(ParameterSelectability.SELECTABLE);
        }
        return parameterValueHolder.get();
    }

    public static <T extends ParameterValue> ParameterValueHolder<T> setOptions(ParameterValueHolder<T> parameterValueHolder, T[] arrT) {
        parameterValueHolder.setOptions(arrT);
        return parameterValueHolder;
    }

    public static <T extends ParameterValue> T unavailable(ParameterValueHolder<T> parameterValueHolder, T t) {
        ParameterKey parameterKey = t.getParameterKey();
        if (!parameterKey.isInvalid()) {
            parameterValueHolder.set(t);
            parameterKey.setSelectability(ParameterSelectability.UNAVAILABLE);
        }
        return t;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static <T extends ParameterValue> ParameterValueHolder<T> updateDefaultValue(ParameterValueHolder<T> parameterValueHolder) {
        T[] arrT = parameterValueHolder.getOptions();
        ParameterSelectability parameterSelectability = ParameterSelectability.getSelectability(arrT.length);
        if (parameterSelectability == ParameterSelectability.INVALID) return parameterValueHolder;
        T t = parameterValueHolder.getDefaultValue();
        T t2 = arrT[0];
        if (parameterSelectability == ParameterSelectability.FIXED) {
            if (t == t2) return parameterValueHolder;
            {
                parameterValueHolder.updateDefaultValue(t2);
                return parameterValueHolder;
            }
        }
        if (parameterSelectability != ParameterSelectability.SELECTABLE || t == ParameterUtil.getPrimaryValue(t, t2, arrT)) {
            return parameterValueHolder;
        }
        parameterValueHolder.updateDefaultValue(t2);
        return parameterValueHolder;
    }
}

