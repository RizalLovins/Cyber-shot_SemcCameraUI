/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.executor;

import android.content.Context;
import android.content.DialogInterface;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogController;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

public class TermOfUseSettingExecutor<CommonSettingKey>
implements SettingExecutorInterface<CommonSettingKey> {
    private final Context mContext;
    private final SettingDialogController mSettingDialogController;

    public TermOfUseSettingExecutor(Context context, SettingDialogController settingDialogController) {
        this.mContext = context;
        this.mSettingDialogController = settingDialogController;
    }

    private void showTermsAndConditions() {
        BaseActivity baseActivity = (BaseActivity)this.mContext;
        baseActivity.getMessagePopup().showTermsAndConditions(baseActivity, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialogInterface, int n) {
                TermOfUseSettingExecutor.this.mSettingDialogController.closeDialogs(true);
            }
        }, (DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener(){

            public void onCancel(DialogInterface dialogInterface) {
                TermOfUseSettingExecutor.this.mSettingDialogController.closeDialogs(true);
            }
        });
    }

    @Override
    public void onExecute(TypedSettingItem<CommonSettingKey> typedSettingItem) {
        super.showTermsAndConditions();
    }

}

