/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Environment
 *  android.os.StatFs
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  android.os.storage.StorageVolume
 *  java.io.FileNotFoundException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Locale
 *  java.util.concurrent.Callable
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.values.SaveDestination;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Callable;

public class StorageUtil {
    private static final String TAG = StorageUtil.class.getSimpleName();

    public static String[] getMountedPaths(Context context) {
        ArrayList arrayList = new ArrayList();
        for (StorageVolume storageVolume : StorageUtil.getStorageManager(context).getVolumeList()) {
            if (storageVolume.getPath() == null) continue;
            arrayList.add((Object)storageVolume.getPath());
        }
        return (String[])arrayList.toArray((Object[])new String[0]);
    }

    private static String getPathFromConfig(Context context) {
        String string = context.getString(R.string.default_destination_to_save).toLowerCase(Locale.US);
        StorageVolume[] arrstorageVolume = StorageUtil.getStorageManager(context).getVolumeList();
        int n = arrstorageVolume.length;
        for (int i = 0; i < n; ++i) {
            String string2 = arrstorageVolume[i].getPath();
            if (!string.equals((Object)StorageUtil.getStorageTypeFromPath(string2, context).name().toLowerCase(Locale.US))) continue;
            return string2;
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String getPathFromSaveDestination(SaveDestination saveDestination, Context context) {
        return StorageUtil.getPathFromType(StorageUtil.getStorageTypeFromSaveDestination(saveDestination), context);
    }

    public static String getPathFromType(StorageManager.StorageType storageType, Context context) {
        try {
            String string = StorageUtil.getStorageManager(context).getVolumePath(storageType);
            return string;
        }
        catch (FileNotFoundException var2_3) {
            return StorageUtil.getPathFromConfig(context);
        }
    }

    /*
     * Exception decompiling
     */
    public static String getPathFromUri(Context var0_1, Uri var1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 10[CATCHBLOCK]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:392)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:444)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2783)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:764)
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

    private static StorageManager getStorageManager(Context context) {
        return (StorageManager)context.getSystemService("storage");
    }

    public static StorageManager.StorageType getStorageTypeFromPath(String string, Context context) {
        StorageManager.StorageType storageType = StorageManager.StorageType.UNKNOWN;
        try {
            StorageManager.StorageType storageType2 = StorageUtil.getStorageManager(context).getVolumeType(string);
            return storageType2;
        }
        catch (FileNotFoundException var3_4) {
            CameraLogger.w(TAG, "getVolumeType(" + string + ") failed.");
            return storageType;
        }
    }

    public static StorageManager.StorageType getStorageTypeFromSaveDestination(SaveDestination saveDestination) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$SaveDestination[saveDestination.ordinal()]) {
            default: {
                return StorageManager.StorageType.INTERNAL;
            }
            case 1: 
        }
        return StorageManager.StorageType.EXTERNAL_CARD;
    }

    public static StorageManager.StorageType getStorageTypeFromUri(Uri uri, Context context) {
        String string = StorageUtil.getPathFromUri(context, uri);
        StorageUtil.getStorageTypeFromPath(string, context);
        return StorageUtil.getStorageTypeFromPath(string, context);
    }

    public static String getVolumeState(String string, Context context) {
        return StorageUtil.getStorageManager(context).getVolumeState(string);
    }

    public static class GetStatFsTask
    implements Callable<StatFs> {
        private final String mPath;

        public GetStatFsTask(String string) {
            if (string == null) {
                throw new IllegalArgumentException("Target path is null.");
            }
            this.mPath = string;
        }

        public StatFs call() {
            try {
                StatFs statFs = new StatFs(this.mPath);
                return statFs;
            }
            catch (IllegalArgumentException var2_2) {
                CameraLogger.e(TAG, "Create StatFs failed.", (Throwable)var2_2);
                return null;
            }
        }
    }

}

