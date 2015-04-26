/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.StatFs
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  java.io.File
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.InterruptedException
 *  java.lang.Math
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.StorageUtil;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CameraStorageManager
implements BaseActivity.StorageEventListener {
    private static final String SHARED_PREFERENCE_KEY = "is-sdcard-available-on-previous-onpause";
    private static final String SHARED_PREFERENCE_NAME = "storage_preferences";
    private static final String TAG = CameraStorageManager.class.getSimpleName();
    public static final int TIMEOUT_GET_STATFS = 3000;
    private Activity mActivity;
    private String mCurrentStorage = Environment.getExternalStorageDirectory().getPath();
    private final Map<String, DetailStorageState> mLastStorageStates;
    private final SharedPreferences mSharedPrefs;
    private StorageController mStorageController;

    public CameraStorageManager(Activity activity, StorageController storageController) {
        this.mActivity = activity;
        this.mStorageController = storageController;
        this.mSharedPrefs = activity.getSharedPreferences("storage_preferences", 0);
        this.mLastStorageStates = new HashMap();
        String[] arrstring = StorageUtil.getMountedPaths((Context)this.mActivity);
        int n = arrstring.length;
        for (int i = 0; i < n; ++i) {
            super.updateStorageState(arrstring[i], 0, "");
        }
    }

    private boolean checkFsWritable(String string) {
        File file = new File(string);
        if (!(file.isDirectory() || file.mkdirs())) {
            return false;
        }
        return file.canWrite();
    }

    private DetailStorageState getLastStorageState() {
        return (DetailStorageState)this.mLastStorageStates.get((Object)this.mCurrentStorage);
    }

    private DetailStorageState getNextStateFromRemain(long l) {
        if (l > 153600) {
            return DetailStorageState.MEMORY_READY;
        }
        if (l > 61440) {
            return DetailStorageState.MEMORY_READY_LOW;
        }
        return DetailStorageState.MEMORY_ERR_FULL;
    }

    private DetailStorageState getNextStateFromVolume(String string) {
        String string2 = StorageUtil.getVolumeState(string, (Context)this.mActivity);
        if ("bad_removal".equals((Object)string2)) {
            return DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        if ("mounted_ro".equals((Object)string2)) {
            return DetailStorageState.MEMORY_ERR_READ_ONLY;
        }
        if ("removed".equals((Object)string2)) {
            return DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        if ("shared".equals((Object)string2)) {
            return DetailStorageState.MEMORY_ERR_SHARED;
        }
        if ("unmountable".equals((Object)string2)) {
            return DetailStorageState.MEMORY_ERR_FORMAT;
        }
        if ("unmounted".equals((Object)string2)) {
            return DetailStorageState.MEMORY_ERR_SHARED;
        }
        if ("checking".equals((Object)string2)) {
            return DetailStorageState.MEMORY_CHECKING;
        }
        if ("mounted".equals((Object)string2)) {
            return DetailStorageState.MEMORY_READY;
        }
        return DetailStorageState.MEMORY_ERR_ACCESS;
    }

    private DetailStorageState getNextStateFromWritable(String string) {
        if (super.checkFsWritable(string)) {
            if (DcfPathBuilder.getInstance().isAlreadyLastFileExist(DcfPathBuilder.getInstance().getDcimDirectory(string))) {
                return DetailStorageState.MEMORY_ERR_FULL;
            }
            if (DcfPathBuilder.getInstance().checkAndCreateDirectory(DcfPathBuilder.getInstance().getDcimDirectory(string), true)) {
                return DetailStorageState.MEMORY_READY;
            }
            return DetailStorageState.MEMORY_NO_DCIM;
        }
        return DetailStorageState.MEMORY_ERR_READ_ONLY;
    }

    private boolean isExternalStorageChangedToReadable() {
        boolean bl = this.mSharedPrefs.getBoolean("is-sdcard-available-on-previous-onpause", false);
        boolean bl2 = false;
        if (!bl) {
            boolean bl3 = this.isReadable(StorageUtil.getPathFromType(StorageManager.StorageType.EXTERNAL_CARD, (Context)this.mActivity));
            bl2 = false;
            if (bl3) {
                this.saveExternalStorageStateInPrefs();
                bl2 = true;
            }
        }
        return bl2;
    }

    private boolean isReadable(DetailStorageState detailStorageState) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$CameraStorageManager$DetailStorageState[detailStorageState.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
        }
        return true;
    }

    private boolean isReadable(String string) {
        return super.isReadable((DetailStorageState)this.mLastStorageStates.get((Object)string));
    }

    private boolean isReady(DetailStorageState detailStorageState) {
        if (detailStorageState == null) {
            return false;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$CameraStorageManager$DetailStorageState[detailStorageState.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
        }
        return true;
    }

    private void logStorageState() {
    }

    private void setLastStorageState(String string, DetailStorageState detailStorageState) {
        this.mLastStorageStates.put((Object)string, (Object)detailStorageState);
        this.mStorageController.updateStorageState(string, detailStorageState);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateAllStorageState(String string, long l, String string2) {
        for (String string3 : StorageUtil.getMountedPaths((Context)this.mActivity)) {
            if (string3.equals((Object)string)) {
                super.updateStorageState(string3, l, string2);
                continue;
            }
            super.updateStorageState(string3, 0, "");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private DetailStorageState updateStorageState(String string, long l, String string2) {
        long l2 = 0;
        DetailStorageState detailStorageState = super.getNextStateFromVolume(string);
        if (detailStorageState == DetailStorageState.MEMORY_READY) {
            detailStorageState = super.getNextStateFromWritable(string);
        }
        if (detailStorageState == DetailStorageState.MEMORY_READY) {
            l2 = this.updateAvailableStorageSize(string, l);
            detailStorageState = super.getNextStateFromRemain(l2);
        }
        if (string2.equals((Object)"android.intent.action.MEDIA_EJECT")) {
            detailStorageState = detailStorageState.equals((Object)DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD) ? DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD : DetailStorageState.MEMORY_ERR_SHARED;
        }
        if (this.mCurrentStorage.equals((Object)string)) {
            this.mStorageController.setAvailableStorageSize(l2);
        }
        super.setLastStorageState(string, detailStorageState);
        return detailStorageState;
    }

    public void addStorageListener(StorageController.StorageListener storageListener) {
        this.mStorageController.addStorageListener(storageListener);
    }

    public String getCurrentStorage() {
        return this.mCurrentStorage;
    }

    public List<String> getReadableStorage() {
        ArrayList arrayList = new ArrayList();
        for (String string : this.mLastStorageStates.keySet()) {
            if (!this.isReadable(string)) continue;
            arrayList.add((Object)string);
        }
        return arrayList;
    }

    public StatFs getStatFs(String string) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit((Callable)new StorageUtil.GetStatFsTask(string));
        try {
            StatFs statFs = (StatFs)future.get(3000, TimeUnit.MILLISECONDS);
            return statFs;
        }
        catch (InterruptedException var12_5) {
            CameraLogger.e(TAG, "GetStatFsTask has been interrupted.", (Throwable)var12_5);
            return null;
        }
        catch (ExecutionException var9_6) {
            CameraLogger.e(TAG, "GetStatFsTask failed.", (Throwable)var9_6);
            return null;
        }
        catch (TimeoutException var6_7) {
            CameraLogger.e(TAG, "GetStatFsTask failed.", (Throwable)var6_7);
            return null;
        }
        finally {
            future.cancel(true);
            executorService.shutdown();
        }
    }

    public boolean hasEnoughFreeSpace() {
        DetailStorageState detailStorageState = this.getLastStorageState();
        switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$CameraStorageManager$DetailStorageState[detailStorageState.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
        }
        return true;
    }

    public boolean isCurrentStorageExternal() {
        if (StorageUtil.getStorageTypeFromPath(this.mCurrentStorage, (Context)this.mActivity) == StorageManager.StorageType.EXTERNAL_CARD) {
            return true;
        }
        return false;
    }

    public boolean isReadable() {
        return this.isReadable(this.getLastStorageState());
    }

    public boolean isReady() {
        return this.isReady(this.getLastStorageState());
    }

    public boolean isStorageExternal(String string) {
        if (StorageUtil.getStorageTypeFromPath(string, (Context)this.mActivity) == StorageManager.StorageType.EXTERNAL_CARD) {
            return true;
        }
        return false;
    }

    public boolean isStorageInternal(String string) {
        if (StorageUtil.getStorageTypeFromPath(string, (Context)this.mActivity) == StorageManager.StorageType.INTERNAL) {
            return true;
        }
        return false;
    }

    public boolean isToggledStorageReady() {
        return this.mStorageController.isToggledStorageReady();
    }

    @Override
    public void onMediaScanFinished() {
        this.mStorageController.checkAndNotifyStateChanged(this.mCurrentStorage, true);
    }

    @Override
    public void onStorageCheckRequested(String string, String string2) {
        super.updateAllStorageState(string2, 0, string);
        this.mStorageController.checkAllState(string2, (DetailStorageState)this.mLastStorageStates.get((Object)string2), true, false);
    }

    public void pause() {
        this.mStorageController.pause();
    }

    public void release() {
        this.mStorageController.release();
    }

    public void removeStorageListener(StorageController.StorageListener storageListener) {
        this.mStorageController.removeStorageListener(storageListener);
    }

    public void requestCheckAll() {
        this.mStorageController.checkAllState(this.mCurrentStorage, (DetailStorageState)this.mLastStorageStates.get((Object)this.mCurrentStorage), this.isExternalStorageChangedToReadable(), false);
    }

    public void resume() {
        this.updateAllStorageState("", 0, "");
    }

    public void saveExternalStorageStateInPrefs() {
        String string = StorageUtil.getPathFromType(StorageManager.StorageType.EXTERNAL_CARD, (Context)this.mActivity);
        SharedPreferences.Editor editor = this.mSharedPrefs.edit();
        if (editor != null) {
            editor.putBoolean("is-sdcard-available-on-previous-onpause", this.isReadable(string));
            editor.commit();
        }
    }

    public void setCurrentStorage(StorageManager.StorageType storageType) {
        if (storageType == null) {
            return;
        }
        this.mCurrentStorage = StorageUtil.getPathFromType(storageType, (Context)this.mActivity);
        DcfPathBuilder.getInstance().setDestinationToSave(this.mCurrentStorage);
        this.mStorageController.setCurrentStorage(this.mCurrentStorage);
        super.updateStorageState(this.mCurrentStorage, 0, "");
    }

    /*
     * Enabled aggressive block sorting
     */
    public long updateAvailableStorageSize(String string, long l) {
        long l2;
        long l3;
        StatFs statFs = this.getStatFs(string);
        if (statFs == null) {
            CameraLogger.e(TAG, "Failed to get StatFs: " + string);
            return 0;
        }
        if (!StorageUtil.getVolumeState(string, (Context)this.mActivity).equals((Object)"mounted")) return 0;
        if (Build.VERSION.SDK_INT <= 17) {
            l3 = statFs.getBlockSize();
            l2 = statFs.getAvailableBlocks();
            return Math.max((long)0, (long)((l3 * l2 - l) / 1024));
        }
        l3 = statFs.getBlockSizeLong();
        l2 = statFs.getAvailableBlocksLong();
        return Math.max((long)0, (long)((l3 * l2 - l) / 1024));
    }

    public long updateRemain(long l, boolean bl) {
        super.updateAllStorageState(this.mCurrentStorage, l, "");
        this.mStorageController.checkAllState(this.mCurrentStorage, (DetailStorageState)this.mLastStorageStates.get((Object)this.mCurrentStorage), super.isExternalStorageChangedToReadable(), bl);
        return this.mStorageController.getAvailableStorageSize();
    }

    public static final class DetailStorageState
    extends Enum<DetailStorageState> {
        private static final /* synthetic */ DetailStorageState[] $VALUES;
        public static final /* enum */ DetailStorageState MEMORY_CHECKING;
        public static final /* enum */ DetailStorageState MEMORY_ERR_ACCESS;
        public static final /* enum */ DetailStorageState MEMORY_ERR_FORMAT;
        public static final /* enum */ DetailStorageState MEMORY_ERR_FULL;
        public static final /* enum */ DetailStorageState MEMORY_ERR_NO_MEMORY_CARD;
        public static final /* enum */ DetailStorageState MEMORY_ERR_READ_ONLY;
        public static final /* enum */ DetailStorageState MEMORY_ERR_SHARED;
        public static final /* enum */ DetailStorageState MEMORY_NO_DCIM;
        public static final /* enum */ DetailStorageState MEMORY_READY;
        public static final /* enum */ DetailStorageState MEMORY_READY_LOW;

        static {
            MEMORY_READY = new DetailStorageState();
            MEMORY_ERR_NO_MEMORY_CARD = new DetailStorageState();
            MEMORY_ERR_READ_ONLY = new DetailStorageState();
            MEMORY_ERR_ACCESS = new DetailStorageState();
            MEMORY_ERR_FORMAT = new DetailStorageState();
            MEMORY_ERR_FULL = new DetailStorageState();
            MEMORY_ERR_SHARED = new DetailStorageState();
            MEMORY_READY_LOW = new DetailStorageState();
            MEMORY_CHECKING = new DetailStorageState();
            MEMORY_NO_DCIM = new DetailStorageState();
            DetailStorageState[] arrdetailStorageState = new DetailStorageState[]{MEMORY_READY, MEMORY_ERR_NO_MEMORY_CARD, MEMORY_ERR_READ_ONLY, MEMORY_ERR_ACCESS, MEMORY_ERR_FORMAT, MEMORY_ERR_FULL, MEMORY_ERR_SHARED, MEMORY_READY_LOW, MEMORY_CHECKING, MEMORY_NO_DCIM};
            $VALUES = arrdetailStorageState;
        }

        private DetailStorageState() {
            super(string, n);
        }

        public static DetailStorageState valueOf(String string) {
            return (DetailStorageState)Enum.valueOf((Class)DetailStorageState.class, (String)string);
        }

        public static DetailStorageState[] values() {
            return (DetailStorageState[])$VALUES.clone();
        }
    }

    public static interface ExternalMemoryListener {
        public void onExternalMemoryStatusChanged(DetailStorageState var1);
    }

}

