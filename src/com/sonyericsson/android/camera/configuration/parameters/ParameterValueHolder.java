/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.ClassNotFoundException
 *  java.lang.Enum
 *  java.lang.IllegalAccessException
 *  java.lang.IllegalArgumentException
 *  java.lang.InstantiationException
 *  java.lang.LinkageError
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.reflect.Constructor
 *  java.lang.reflect.InvocationTargetException
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;

public class ParameterValueHolder<T extends ParameterValue> {
    public static final String DELIMITER = "-";
    public static final String NO_VALUE = "NO_VALUE";
    public static final String REGULAR_EXPRESSION = "@";
    private static final String TAG = "ParameterValueHolder";
    private boolean mChanged;
    private T mDefaultValue;
    private T[] mOptions;
    private ParameterValueHolder<T> mState;

    public ParameterValueHolder(T t) {
        this.mDefaultValue = t;
        this.mState = new NormalState((ParameterValueHolder)this, t, null, null);
        this.onApplied();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private T deserialize(String string) {
        String[] arrstring;
        ParameterValue parameterValue;
        if (string.equals((Object)"NO_VALUE") || (arrstring = string.split("@")).length < 2) {
            return null;
        }
        try {
            parameterValue = (ParameterValue)Enum.valueOf((Class)Class.forName((String)arrstring[0]), (String)arrstring[1]);
        }
        catch (IllegalArgumentException var6_4) {
            return null;
        }
        catch (LinkageError var5_5) {
            return null;
        }
        catch (ClassNotFoundException var4_6) {
            return null;
        }
        catch (ClassCastException var3_7) {
            return null;
        }
        return (T)parameterValue;
    }

    private ParameterValueHolder<T> getParameterState(String string) {
        try {
            Class class_ = Class.forName((String)string);
            Class[] arrclass = new Class[]{this.getClass()};
            ParameterState parameterState = (ParameterState)class_.getConstructor(arrclass).newInstance(new Object[]{this});
            return parameterState;
        }
        catch (LinkageError var10_5) {
            return null;
        }
        catch (NoSuchMethodException var9_6) {
            return null;
        }
        catch (InvocationTargetException var8_7) {
            return null;
        }
        catch (IllegalAccessException var7_8) {
            return null;
        }
        catch (InstantiationException var6_9) {
            return null;
        }
        catch (SecurityException var5_10) {
            return null;
        }
        catch (IllegalArgumentException var4_11) {
            return null;
        }
        catch (ClassNotFoundException var3_12) {
            return null;
        }
        catch (ClassCastException var2_13) {
            return null;
        }
    }

    private String serialize(T t) {
        if (t == null) {
            return "NO_VALUE";
        }
        return t.getClass().getName() + "@" + t.toString();
    }

    public void applyCurrentValue() {
        this.mState.applyCurrentValue();
    }

    public void applyRecommendedValue(T t) {
        this.mState.applyRecommendedValue(t);
    }

    public String createValueString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.mState.getClass().getName());
        stringBuffer.append("-");
        stringBuffer.append(this.serialize(this.mState.getCurrentValue()));
        stringBuffer.append("-");
        stringBuffer.append(this.serialize(this.mState.getOriginalValue()));
        stringBuffer.append("-");
        stringBuffer.append(this.serialize(this.mState.getRecommendedValue()));
        return stringBuffer.toString();
    }

    public boolean forceChange(T t) {
        return this.mState.forceChange(t);
    }

    public T get() {
        return this.mState.getCurrentValue();
    }

    public T getDefaultValue() {
        return this.mDefaultValue;
    }

    public T[] getOptions() {
        return (ParameterValue[])this.mOptions.clone();
    }

    public T getOriginalValue() {
        return this.mState.getOriginalValue();
    }

    public T getRecommendedValue() {
        return this.mState.getRecommendedValue();
    }

    public boolean hasChanged() {
        return this.mChanged;
    }

    public void onApplied() {
        this.mChanged = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void parseValueString(String string) {
        String[] arrstring;
        T t;
        ParameterValueHolder<T> parameterValueHolder;
        if (string == null || this.mChanged || (arrstring = string.split("-")).length < 4 || (parameterValueHolder = super.getParameterState(arrstring[0])) == null || (t = super.deserialize(arrstring[1])) == null) {
            return;
        }
        T t2 = super.deserialize(arrstring[2]);
        T t3 = super.deserialize(arrstring[3]);
        this.mState = parameterValueHolder;
        this.mState.setCurrentValue(t);
        this.mState.setOriginalValue(t2);
        this.mState.setRecommendedValue(t3);
    }

    public boolean reset() {
        return this.mState.reset();
    }

    public void set(T t) {
        this.mState.setCurrentValue(t);
    }

    public T setDefaultValue() {
        this.mState.setCurrentValue(this.mDefaultValue);
        return this.mDefaultValue;
    }

    public void setOptions(T[] arrT) {
        this.mOptions = (ParameterValue[])arrT.clone();
    }

    public T updateDefaultValue(T t) {
        this.mDefaultValue = t;
        if (!this.mChanged) {
            this.setDefaultValue();
            this.onApplied();
        }
        return this.mDefaultValue;
    }

    private class ForceChangedState
    extends ParameterValueHolder<T> {
        final /* synthetic */ ParameterValueHolder this$0;

        public ForceChangedState(ParameterValueHolder parameterValueHolder) {
            this.this$0 = parameterValueHolder;
            super(parameterValueHolder);
        }

        /*
         * WARNING - Possible parameter corruption
         */
        public ForceChangedState(T var1_1, T t, T t2) {
            this.this$0 = var1_1;
            super((ParameterValueHolder)var1_1, t, t2, t3);
        }

        @Override
        public void applyCurrentValue() {
            if (this.mRecommendedValue != null) {
                this.setCurrentValue(this.mRecommendedValue);
            }
        }

        @Override
        public void applyRecommendedValue(T t) {
            this.setCurrentValue(t);
        }

        @Override
        public boolean forceChange(T t) {
            this.setCurrentValue(t);
            return false;
        }

        @Override
        public boolean reset() {
            this.this$0.mState = (ParameterState)new NormalState(this.this$0, this.mCurrentValue, null, null);
            this.this$0.mChanged = true;
            return true;
        }
    }

    private class NormalState
    extends ParameterValueHolder<T> {
        final /* synthetic */ ParameterValueHolder this$0;

        public NormalState(ParameterValueHolder parameterValueHolder) {
            this.this$0 = parameterValueHolder;
            super(parameterValueHolder);
        }

        /*
         * WARNING - Possible parameter corruption
         */
        public NormalState(T var1_1, T t, T t2) {
            this.this$0 = var1_1;
            super((ParameterValueHolder)var1_1, t, t2, t3);
        }

        @Override
        public void applyCurrentValue() {
        }

        @Override
        public void applyRecommendedValue(T t) {
            this.this$0.mState = (ParameterState)new ForceChangedState(this.this$0, t, this.this$0.get(), t);
            this.this$0.mChanged = true;
        }

        @Override
        public boolean forceChange(T t) {
            this.this$0.mState = (ParameterState)new ForceChangedState(this.this$0, t, this.this$0.get(), null);
            this.this$0.mChanged = true;
            return true;
        }

        @Override
        public boolean reset() {
            return false;
        }
    }

    private abstract class ParameterState {
        protected T mCurrentValue;
        protected T mOriginalValue;
        protected T mRecommendedValue;
        final /* synthetic */ ParameterValueHolder this$0;

        public ParameterState(ParameterValueHolder parameterValueHolder) {
            this.this$0 = parameterValueHolder;
        }

        /*
         * WARNING - Possible parameter corruption
         */
        public ParameterState(T var1_1, T t, T t2) {
            this.this$0 = var1_1;
            this.setCurrentValue(t);
            this.setOriginalValue(t2);
            this.setRecommendedValue(t3);
        }

        public abstract void applyCurrentValue();

        public abstract void applyRecommendedValue(T var1);

        public abstract boolean forceChange(T var1);

        public final T getCurrentValue() {
            return this.mCurrentValue;
        }

        public final T getOriginalValue() {
            return this.mOriginalValue;
        }

        public final T getRecommendedValue() {
            return this.mRecommendedValue;
        }

        public abstract boolean reset();

        public final void setCurrentValue(T t) {
            if (this.mCurrentValue != t) {
                this.this$0.mChanged = true;
            }
            this.mCurrentValue = t;
        }

        public final void setOriginalValue(T t) {
            this.mOriginalValue = t;
        }

        public final void setRecommendedValue(T t) {
            this.mRecommendedValue = t;
        }
    }

}

