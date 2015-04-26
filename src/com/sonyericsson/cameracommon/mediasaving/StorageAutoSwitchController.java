/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 *  java.util.Set
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.content.DialogInterface;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface;
import java.util.Map;
import java.util.Set;

public class StorageAutoSwitchController
extends StorageController {
    static final String TAG = StorageAutoSwitchController.class.getSimpleName();
    private RotatableDialog mDualStorageDialog = null;
    private StorageAutoSwitchListener mStorageAutoSwitchListener;

    public StorageAutoSwitchController(StorageAutoSwitchListener storageAutoSwitchListener, ViewFinderInterface viewFinderInterface) {
        super(viewFinderInterface);
        this.mStorageAutoSwitchListener = storageAutoSwitchListener;
    }

    private boolean checkBetterStorage(String string, boolean bl) {
        if (this.hasBetterStorage(string) && (bl || this.mStorageStatus.get((Object)this.mCurrentStorage) != StorageController.StorageState.AVAILABLE)) {
            return true;
        }
        return false;
    }

    private void switchStorage() {
        if (this.getCurrentStorageState() == StorageController.StorageState.AVAILABLE) {
            this.closeDialog(false);
            this.showPopupDualStorageAvailable();
            return;
        }
        this.showDialogForForceChanged(this.getCurrentStorageState());
        this.mStorageAutoSwitchListener.onStorageAutoSwitch(this.mCurrentStorage);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void checkAllState(String string, CameraStorageManager.DetailStorageState detailStorageState, boolean bl, boolean bl2) {
        if (this.mViewFinder.isHeadUpDesplayReady()) {
            for (String string2 : this.mStoragePriority.keySet()) {
                this.checkAndNotifyStateChanged(string, bl2);
                if (!this.mCurrentStorage.equals((Object)string2)) continue;
                if (super.checkBetterStorage(string, bl)) {
                    super.switchStorage();
                    continue;
                }
                this.showOrClearStorageErrorPopup(this.getCurrentStorageState());
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected int getTextIdForForceChanged(StorageController.StorageState storageState) {
        if (this.isCurrentStorageExternal()) {
            switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$StorageController$StorageState[storageState.ordinal()]) {
                default: {
                    do {
                        return -1;
                        break;
                    } while (true);
                }
                case 2: {
                    return R.string.cam_strings_change_full_storage_to_internal_txt;
                }
                case 3: 
                case 4: 
            }
            return R.string.cam_strings_error_storage_changing_to_internal_txt;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$StorageController$StorageState[storageState.ordinal()]) {
            case 1: {
                return -1;
            }
            default: {
                return -1;
            }
            case 2: {
                return R.string.cam_strings_error_internal_memory_full_txt;
            }
            case 3: 
            case 4: 
        }
        return R.string.cam_strings_error_internal_memory_unavailable_txt;
    }

    protected boolean hasBetterStorage(String string) {
        int n = (Integer)this.mStoragePriority.get((Object)this.mCurrentStorage);
        if (!this.mStorageAutoSwitchListener.canSwitchStorage()) {
            this.requestErrorCheckLater(string);
            return false;
        }
        if (this.mStorageStatus.get((Object)this.mCurrentStorage) != StorageController.StorageState.AVAILABLE) {
            n = 100;
        }
        for (String string2 : this.mStoragePriority.keySet()) {
            if ((Integer)this.mStoragePriority.get((Object)string2) >= n || this.mStorageStatus.get((Object)string2) != StorageController.StorageState.AVAILABLE) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isToggledStorageReady() {
        boolean bl = false;
        for (String string : this.mStoragePriority.keySet()) {
            if (string.equals((Object)this.mCurrentStorage) || this.mStorageStatus.get((Object)string) != StorageController.StorageState.AVAILABLE) continue;
            bl = true;
        }
        return bl;
    }

    @Override
    public void setStorage(String string, int n) {
        this.mStoragePriority.put((Object)string, (Object)n);
    }

    protected boolean showDialogForForceChanged(StorageController.StorageState storageState) {
        int n = this.getTextIdForForceChanged(storageState);
        int n2 = R.string.cam_strings_save_destination_title_txt;
        this.closeDialog(false);
        return this.showStoragePopup(n, n2, false);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    protected boolean showOrClearStorageErrorPopup(StorageController.StorageState var1) {
        var2_2 = -1;
        var3_3 = R.string.cam_strings_error_memory_title_txt;
        if (this.isCurrentStorageExternal()) {
            switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$StorageController$StorageState[this.getCurrentStorageState().ordinal()]) {
                case 1: {
                    this.closeDialog(true);
                    ** break;
                }
                case 2: {
                    var2_2 = R.string.cam_strings_error_internal_sd_full_txt;
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
                var2_2 = R.string.cam_strings_error_internal_sd_full_txt;
                return this.showStoragePopup(var2_2, var3_3, true);
            }
            case 3: 
            case 4: 
        }
        var2_2 = R.string.cam_strings_error_memory_ims_unavailable_txt;
        return this.showStoragePopup(var2_2, var3_3, true);
    }

    protected boolean showPopupDualStorageAvailable() {
        int n = R.string.cam_strings_change_storage_to_sd_txt;
        boolean bl = false;
        if (n > 0) {
            if (this.mDualStorageDialog != null) {
                return true;
            }
            RotatableDialog rotatableDialog = this.mDualStorageDialog = this.mMessagePopup.showOkAndCancelStorage(n, R.string.cam_strings_save_destination_title_txt, false, R.string.cam_strings_change_txt, R.string.cam_strings_cancel_txt, (DialogInterface.OnClickListener)new DialogOkClickListener(this, null), (DialogInterface.OnClickListener)new DialogCancelClickListener(this, null), (DialogInterface.OnCancelListener)new DialogCancelListener(this, null));
            bl = false;
            if (rotatableDialog != null) {
                bl = true;
            }
        }
        return bl;
    }

    /*
     * Failed to analyse overrides
     */
    private class DialogCancelClickListener
    implements DialogInterface.OnClickListener {
        final /* synthetic */ StorageAutoSwitchController this$0;

        private DialogCancelClickListener(StorageAutoSwitchController storageAutoSwitchController) {
            this.this$0 = storageAutoSwitchController;
        }

        /* synthetic */ DialogCancelClickListener(StorageAutoSwitchController storageAutoSwitchController,  var2_2) {
            super(storageAutoSwitchController);
        }

        public void onClick(DialogInterface dialogInterface, int n) {
            this.this$0.closeDialog(dialogInterface);
            this.this$0.mDualStorageDialog = null;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class DialogCancelListener
    implements DialogInterface.OnCancelListener {
        final /* synthetic */ StorageAutoSwitchController this$0;

        private DialogCancelListener(StorageAutoSwitchController storageAutoSwitchController) {
            this.this$0 = storageAutoSwitchController;
        }

        /* synthetic */ DialogCancelListener(StorageAutoSwitchController storageAutoSwitchController,  var2_2) {
            super(storageAutoSwitchController);
        }

        public void onCancel(DialogInterface dialogInterface) {
            this.this$0.closeDialog(dialogInterface);
            this.this$0.mDualStorageDialog = null;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class DialogOkClickListener
    implements DialogInterface.OnClickListener {
        final /* synthetic */ StorageAutoSwitchController this$0;

        private DialogOkClickListener(StorageAutoSwitchController storageAutoSwitchController) {
            this.this$0 = storageAutoSwitchController;
        }

        /* synthetic */ DialogOkClickListener(StorageAutoSwitchController storageAutoSwitchController,  var2_2) {
            super(storageAutoSwitchController);
        }

        public void onClick(DialogInterface dialogInterface, int n) {
            this.this$0.mStorageAutoSwitchListener.onStorageAutoSwitch(this.this$0.mCurrentStorage);
            this.this$0.closeDialog(dialogInterface);
            this.this$0.mDualStorageDialog = null;
        }
    }

    public static interface StorageAutoSwitchListener {
        public boolean canSwitchStorage();

        public void onStorageAutoSwitch(String var1);
    }

}

