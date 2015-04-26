/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.DialogInterface
 *  android.os.Environment
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.content.DialogInterface;
import android.os.Environment;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StorageController {
    static final String TAG = StorageController.class.getSimpleName();
    protected long mAvailableSize;
    protected String mCurrentStorage = Environment.getExternalStorageDirectory().getPath();
    private boolean mIsOneShot;
    protected final Map<String, StorageState> mLatestCheckedStorageState = new HashMap();
    private final List<StorageListener> mListeners = new ArrayList();
    protected MessagePopup mMessagePopup = null;
    protected final Map<String, Integer> mStoragePriority = new HashMap();
    protected final Map<String, StorageState> mStorageStatus = new HashMap();
    protected ViewFinderInterface mViewFinder;

    public StorageController(ViewFinderInterface viewFinderInterface) {
        this.mViewFinder = viewFinderInterface;
    }

    private void notifyAvailableSize(long l) {
        Iterator iterator = this.mListeners.iterator();
        while (iterator.hasNext()) {
            ((StorageListener)iterator.next()).onAvailableSizeUpdated(l);
        }
    }

    private void notifyStateChanged(String string) {
        Iterator iterator = this.mListeners.iterator();
        while (iterator.hasNext()) {
            ((StorageListener)iterator.next()).onStorageStateChanged(string);
        }
    }

    private void notifyStorageChanged() {
        Iterator iterator = this.mListeners.iterator();
        while (iterator.hasNext()) {
            ((StorageListener)iterator.next()).onDestinationToSaveChanged();
        }
    }

    public void addStorageListener(StorageListener storageListener) {
        if (!this.mListeners.contains((Object)storageListener)) {
            this.mListeners.add((Object)storageListener);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void checkAllState(String string, CameraStorageManager.DetailStorageState detailStorageState, boolean bl, boolean bl2) {
        if (this.mViewFinder.isHeadUpDesplayReady()) {
            for (String string2 : this.mStoragePriority.keySet()) {
                if (this.mCurrentStorage.equals((Object)string2)) {
                    this.showOrClearStorageErrorPopup(StorageState.getState(detailStorageState));
                }
                this.checkAndNotifyStateChanged(string2, bl2);
            }
        }
    }

    protected void checkAndNotifyStateChanged(String string, boolean bl) {
        if (this.mLatestCheckedStorageState.get((Object)string) != this.mStorageStatus.get((Object)string) || bl) {
            this.mLatestCheckedStorageState.put((Object)string, this.mStorageStatus.get((Object)string));
            super.notifyStateChanged(string);
        }
        super.notifyAvailableSize(this.mAvailableSize);
    }

    protected void closeDialog(DialogInterface dialogInterface) {
        if (this.mMessagePopup != null && this.mMessagePopup.isMemoryErrorPopupOpened()) {
            this.mMessagePopup.cancelMemoryErrorPopup(dialogInterface);
        }
    }

    protected void closeDialog(boolean bl) {
        if (this.mMessagePopup != null && this.mMessagePopup.isMemoryErrorPopupOpened()) {
            this.mMessagePopup.cancelMemoryErrorPopup(bl);
        }
    }

    public long getAvailableStorageSize() {
        return this.mAvailableSize;
    }

    public StorageState getCurrentStorageState() {
        return (StorageState)this.mStorageStatus.get((Object)this.mCurrentStorage);
    }

    public boolean isCurrentStorageExternal() {
        return ((Integer)this.mStoragePriority.get((Object)this.mCurrentStorage)).equals((Object)0);
    }

    protected boolean isOneShotMode() {
        return this.mIsOneShot;
    }

    public boolean isStorageDialogOpen() {
        return this.mMessagePopup.isMemoryErrorPopupOpened();
    }

    public boolean isToggledStorageReady() {
        boolean bl = false;
        for (String string : this.mStoragePriority.keySet()) {
            if (string.equals((Object)this.mCurrentStorage) || this.mStorageStatus.get((Object)string) != StorageState.AVAILABLE) continue;
            bl = true;
        }
        return bl;
    }

    public void pause() {
        this.closeDialog(false);
    }

    public void release() {
        this.mListeners.clear();
    }

    public void removeStorageListener(StorageListener storageListener) {
        if (this.mListeners.contains((Object)storageListener)) {
            this.mListeners.remove((Object)storageListener);
        }
    }

    protected void requestErrorCheckLater(String string) {
        this.mLatestCheckedStorageState.put((Object)string, (Object)StorageState.AVAILABLE);
    }

    protected void setAvailableStorageSize(long l) {
        this.mAvailableSize = l;
    }

    public void setCurrentStorage(String string) {
        this.mCurrentStorage = string;
        DcfPathBuilder.getInstance().resetStatus();
        super.notifyStorageChanged();
    }

    public void setMessegePopup(MessagePopup messagePopup) {
        this.mMessagePopup = messagePopup;
    }

    public void setOneShotMode(boolean bl) {
        this.mIsOneShot = bl;
    }

    public void setStorage(String string, int n) {
        this.mStoragePriority.put((Object)string, (Object)n);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected boolean showOrClearStorageErrorPopup(StorageState var1) {
        var2_2 = -1;
        var3_3 = R.string.cam_strings_error_memory_title_txt;
        if (this.isCurrentStorageExternal()) {
            switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$StorageController$StorageState[this.getCurrentStorageState().ordinal()]) {
                case 1: {
                    this.closeDialog(true);
                    ** break;
                }
                case 2: {
                    var2_2 = R.string.cam_strings_error_memory_full_txt;
                }
lbl10: // 3 sources:
                default: {
                    return this.showStoragePopup(var2_2, var3_3, true);
                }
                case 3: 
                case 4: 
            }
            var2_2 = R.string.cam_strings_error_memory_unavailable_txt;
            return this.showStoragePopup(var2_2, var3_3, true);
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$StorageController$StorageState[this.getCurrentStorageState().ordinal()]) {
            default: {
                return this.showStoragePopup(var2_2, var3_3, true);
            }
            case 1: {
                this.closeDialog(true);
                return this.showStoragePopup(var2_2, var3_3, true);
            }
            case 2: {
                var2_2 = R.string.cam_strings_error_memory_ims_full_txt;
                return this.showStoragePopup(var2_2, var3_3, true);
            }
            case 3: 
            case 4: 
        }
        var2_2 = R.string.cam_strings_error_memory_ims_unavailable_txt;
        return this.showStoragePopup(var2_2, var3_3, true);
    }

    protected boolean showStoragePopup(int n, int n2, boolean bl) {
        boolean bl2 = false;
        if (n > 0) {
            RotatableDialog rotatableDialog = this.mMessagePopup.showMemoryError(n, n2, bl);
            bl2 = false;
            if (rotatableDialog != null) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public void updateStorageState(String string, CameraStorageManager.DetailStorageState detailStorageState) {
        if (!this.mStoragePriority.containsKey((Object)string)) {
            return;
        }
        StorageState storageState = StorageState.getState(detailStorageState);
        this.mStorageStatus.put((Object)string, (Object)storageState);
    }

    public static interface StorageDialogStateListener {
        public void onCloseStorageDialog();

        public void onOpenStorageDialog();
    }

    public static interface StorageListener {
        public void onAvailableSizeUpdated(long var1);

        public void onDestinationToSaveChanged();

        public void onStorageStateChanged(String var1);
    }

    public static final class StorageState
    extends Enum<StorageState> {
        private static final /* synthetic */ StorageState[] $VALUES;
        public static final /* enum */ StorageState AVAILABLE;
        public static final /* enum */ StorageState FULL;
        public static final /* enum */ StorageState REMOVED;
        static final String TAG;
        public static final /* enum */ StorageState UNAVAILABLE;
        private final List<CameraStorageManager.DetailStorageState> mDetailStateList;

        static {
            CameraStorageManager.DetailStorageState[] arrdetailStorageState = new CameraStorageManager.DetailStorageState[]{CameraStorageManager.DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD};
            REMOVED = new StorageState(arrdetailStorageState);
            CameraStorageManager.DetailStorageState[] arrdetailStorageState2 = new CameraStorageManager.DetailStorageState[]{CameraStorageManager.DetailStorageState.MEMORY_READY, CameraStorageManager.DetailStorageState.MEMORY_READY_LOW};
            AVAILABLE = new StorageState(arrdetailStorageState2);
            CameraStorageManager.DetailStorageState[] arrdetailStorageState3 = new CameraStorageManager.DetailStorageState[]{CameraStorageManager.DetailStorageState.MEMORY_ERR_ACCESS, CameraStorageManager.DetailStorageState.MEMORY_ERR_FORMAT, CameraStorageManager.DetailStorageState.MEMORY_ERR_READ_ONLY, CameraStorageManager.DetailStorageState.MEMORY_ERR_SHARED, CameraStorageManager.DetailStorageState.MEMORY_NO_DCIM};
            UNAVAILABLE = new StorageState(arrdetailStorageState3);
            CameraStorageManager.DetailStorageState[] arrdetailStorageState4 = new CameraStorageManager.DetailStorageState[]{CameraStorageManager.DetailStorageState.MEMORY_ERR_FULL};
            FULL = new StorageState(arrdetailStorageState4);
            StorageState[] arrstorageState = new StorageState[]{REMOVED, AVAILABLE, UNAVAILABLE, FULL};
            $VALUES = arrstorageState;
            TAG = StorageState.class.getSimpleName();
        }

        private /* varargs */ StorageState(CameraStorageManager.DetailStorageState ... arrdetailStorageState) {
            super(string, n);
            this.mDetailStateList = Collections.unmodifiableList((List)Arrays.asList((Object[])arrdetailStorageState));
        }

        public static StorageState getState(CameraStorageManager.DetailStorageState detailStorageState) {
            for (StorageState storageState : StorageState.values()) {
                Iterator iterator = storageState.mDetailStateList.iterator();
                while (iterator.hasNext()) {
                    if (!detailStorageState.equals((Object)((CameraStorageManager.DetailStorageState)iterator.next()))) continue;
                    return storageState;
                }
            }
            return UNAVAILABLE;
        }

        public static StorageState valueOf(String string) {
            return (StorageState)Enum.valueOf((Class)StorageState.class, (String)string);
        }

        public static StorageState[] values() {
            return (StorageState[])$VALUES.clone();
        }
    }

}

