/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentProviderOperation
 *  android.content.ContentProviderOperation$Builder
 *  android.content.ContentProviderResult
 *  android.content.ContentResolver
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.OperationApplicationException
 *  android.net.Uri
 *  android.os.Build
 *  android.os.RemoteException
 *  java.lang.Boolean
 *  java.lang.IllegalStateException
 *  java.lang.InterruptedException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 */
package com.sonyericsson.cameracommon.commonsetting;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingConstants;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.securesetting.SecureSettingWriter;
import com.sonyericsson.cameracommon.commonsetting.values.AutoUpload;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.commonsetting.values.ShutterSound;
import com.sonyericsson.cameracommon.commonsetting.values.TermOfUse;
import com.sonyericsson.cameracommon.commonsetting.values.TouchBlock;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.StaticConfigurationUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommonSettings {
    private static final String TAG = CommonSettings.class.getSimpleName();
    private static final boolean loggable;
    private String mCachedFirmwareVersion = "";
    private final Context mContext;
    private ExecutorService mExecutor;
    private Future<?> mFuture;
    private final ContentResolver mResolver;
    private final HashMap<CommonSettingKey, Boolean> mSelectabilities = new HashMap();
    private final HashMap<CommonSettingKey, CommonSettingValue> mSettings = new HashMap();

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = CameraLogger.isEngBuild || CameraLogger.isUserdebugBuild;
        loggable = bl;
    }

    public CommonSettings(ContentResolver contentResolver, Context context) {
        this.mResolver = contentResolver;
        this.mContext = context;
    }

    private ContentProviderOperation createContentProviderUpdateOperation(String string, String string2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", string);
        contentValues.put("value", string2);
        return ContentProviderOperation.newUpdate((Uri)CommonSettingConstants.COMMONSETTING_CONTENT_URI).withValue("name", (Object)string).withValue("value", (Object)string2).build();
    }

    private boolean isFirmwareVersionUpdated() {
        String string = Build.FINGERPRINT;
        if (this.mCachedFirmwareVersion.equals((Object)string)) {
            return false;
        }
        return true;
    }

    private boolean isUnSelectableSetting(CommonSettingKey commonSettingKey) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$CommonSettingKey[commonSettingKey.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
            case 3: 
        }
        return true;
    }

    private void joinStoreTask() {
        try {
            if (this.mFuture != null) {
                this.mFuture.get();
                this.mFuture = null;
            }
            return;
        }
        catch (InterruptedException var3_1) {
            CameraLogger.e(TAG, "joinStoreTask", (Throwable)var3_1);
            return;
        }
        catch (ExecutionException var1_2) {
            CameraLogger.e(TAG, "joinStoreTask", (Throwable)var1_2);
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void loadUnSelectableSettings() {
        block5 : for (CommonSettingKey var4_4 : CommonSettingKey.values()) {
            switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$CommonSettingKey[var4_4.ordinal()]) {
                case 1: {
                    this.putIntoMap(var4_4, AutoUpload.OFF);
                    ** break;
                }
                case 2: {
                    this.putIntoMap(var4_4, TermOfUse.NO_VALUE);
                }
lbl8: // 3 sources:
                default: {
                    continue block5;
                }
                case 3: 
            }
            this.putIntoMap(var4_4, TouchBlock.NO_VALUE);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void putIntoMap(CommonSettingKey commonSettingKey, CommonSettingValue commonSettingValue) {
        void var5_3 = this;
        synchronized (var5_3) {
            if (commonSettingKey != null) {
                if (commonSettingValue == null && loggable) {
                    throw new IllegalStateException(TAG + " > Value of " + commonSettingKey + " is set to NULL.");
                }
                this.mSettings.put((Object)commonSettingKey, (Object)commonSettingValue);
            }
            return;
        }
    }

    private void saveFirmwareVersion() {
        this.mCachedFirmwareVersion = Build.FINGERPRINT;
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)this.createContentProviderUpdateOperation("android.os.Build.FINGERPRINT", this.mCachedFirmwareVersion));
        this.joinStoreTask();
        if (this.mExecutor == null) {
            this.mExecutor = Executors.newSingleThreadExecutor();
        }
        this.mFuture = this.mExecutor.submit((Runnable)new StoreTask(this.mResolver, (List<ContentProviderOperation>)arrayList));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public CommonSettingValue get(CommonSettingKey commonSettingKey) {
        void var4_2 = this;
        synchronized (var4_2) {
            if (commonSettingKey == CommonSettingKey.SHUTTER_SOUND && StaticConfigurationUtil.isForceSound()) {
                return ShutterSound.ON;
            }
            CommonSettingValue commonSettingValue = (CommonSettingValue)this.mSettings.get((Object)commonSettingKey);
            if (commonSettingValue != null) return commonSettingValue;
            throw new IllegalStateException("Value of " + commonSettingKey + " is NULL, CommonSettingProvider may not be loaded yet." + " Current cached settings: " + this.mSettings.entrySet().toString());
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isSelectable(CommonSettingKey commonSettingKey) {
        void var6_2 = this;
        synchronized (var6_2) {
            Boolean bl = (Boolean)this.mSelectabilities.get((Object)commonSettingKey);
            Boolean bl2 = Boolean.FALSE;
            if (bl == bl2) return false;
            return true;
        }
    }

    /*
     * Exception decompiling
     */
    public void load() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredTry.transformStructuredChildren(StructuredTry.java:79)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:485)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.transformStructuredChildren(Block.java:378)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:485)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void set(CommonSettingValue commonSettingValue) {
        void var5_2 = this;
        synchronized (var5_2) {
            CommonSettingKey commonSettingKey = commonSettingValue.getCommonSettingKey();
            if (commonSettingValue == null && loggable) {
                throw new IllegalStateException(TAG + " > Value of " + commonSettingKey + " is set to NULL.");
            }
            this.mSettings.put((Object)commonSettingKey, (Object)commonSettingValue);
            if (commonSettingKey == CommonSettingKey.FAST_CAPTURE) {
                if (commonSettingValue == FastCapture.OFF) {
                    SecureSettingWriter.save(this.mContext, "fcc.status.off", ((FastCapture)commonSettingValue).getProviderValue());
                } else {
                    SecureSettingWriter.save(this.mContext, "fcc.status.on", ((FastCapture)commonSettingValue).getProviderValue());
                }
            }
            this.store();
            return;
        }
    }

    public void setSelectability(CommonSettingKey commonSettingKey, boolean bl) {
        void var5_3 = this;
        synchronized (var5_3) {
            this.mSelectabilities.put((Object)commonSettingKey, (Object)bl);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void store() {
        CommonSettings commonSettings = this;
        synchronized (commonSettings) {
            ArrayList arrayList = new ArrayList();
            for (Map.Entry entry : this.mSettings.entrySet()) {
                CommonSettingKey commonSettingKey = (CommonSettingKey)entry.getKey();
                CommonSettingValue commonSettingValue = (CommonSettingValue)entry.getValue();
                if (this.isUnSelectableSetting(commonSettingKey)) continue;
                arrayList.add((Object)this.createContentProviderUpdateOperation(commonSettingKey.getKey(), commonSettingValue.toString()));
            }
            this.joinStoreTask();
            if (this.mExecutor == null) {
                this.mExecutor = Executors.newSingleThreadExecutor();
            }
            this.mFuture = this.mExecutor.submit((Runnable)new StoreTask(this.mResolver, (List<ContentProviderOperation>)arrayList));
            return;
        }
    }

    public void suspend() {
        this.joinStoreTask();
        if (this.mExecutor != null) {
            this.mExecutor.shutdown();
            this.mExecutor = null;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class StoreTask
    implements Runnable {
        private final ContentResolver mContentResolver;
        private final List<ContentProviderOperation> mOperations;

        public StoreTask(ContentResolver contentResolver, List<ContentProviderOperation> list) {
            this.mContentResolver = contentResolver;
            this.mOperations = list;
        }

        public void run() {
            try {
                this.mContentResolver.applyBatch("com.sonymobile.cameracommon.provider", (ArrayList)this.mOperations);
                return;
            }
            catch (OperationApplicationException var2_1) {
                return;
            }
            catch (RemoteException var1_2) {
                return;
            }
        }
    }

}

