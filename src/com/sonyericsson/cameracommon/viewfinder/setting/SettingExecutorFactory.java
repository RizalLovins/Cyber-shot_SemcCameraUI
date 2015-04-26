/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.viewfinder.setting;

import android.content.Context;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogController;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.cameracommon.setting.executor.SettingChangeAndCloseExecutor;
import com.sonyericsson.cameracommon.setting.executor.SettingChangeExecutor;
import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;
import java.util.Iterator;
import java.util.List;

public class SettingExecutorFactory<Key, Value> {
    private final Context mContext;
    private final SettingChangerInterface<Value> mSettingChanger;
    private final SettingDialogController mSettingDialogController;

    public SettingExecutorFactory(Context context, SettingDialogController settingDialogController, SettingChangerInterface<Value> settingChangerInterface) {
        this.mContext = context;
        this.mSettingDialogController = settingDialogController;
        this.mSettingChanger = settingChangerInterface;
    }

    private SettingAdapter generateChildrenAdapter(SettingItem settingItem, SettingDialogItemFactory settingDialogItemFactory) {
        SettingAdapter settingAdapter = new SettingAdapter(this.mContext, settingDialogItemFactory);
        Iterator iterator = settingItem.getChildren().iterator();
        while (iterator.hasNext()) {
            settingAdapter.add((Object)((SettingItem)iterator.next()));
        }
        return settingAdapter;
    }

    public SettingExecutorInterface<Value> getChangeValueExecutor(int n) {
        switch (n) {
            default: {
                return new SettingChangeAndCloseExecutor<Value>(this.mSettingChanger, this.mSettingDialogController);
            }
            case 4: 
            case 5: 
            case 7: 
        }
        return new SettingChangeExecutor<Value>(this.mSettingChanger);
    }

    public SettingExecutorInterface<Key> getOpenValueSelectDialogExecutor(Key Key, final SettingDialogItemFactory settingDialogItemFactory) {
        return new SettingExecutorInterface<Key>(){

            @Override
            public void onExecute(TypedSettingItem<Key> typedSettingItem) {
                SettingExecutorFactory.this.mSettingDialogController.openSecondLayerDialog(SettingExecutorFactory.this.generateChildrenAdapter(typedSettingItem, settingDialogItemFactory), typedSettingItem.getData());
            }
        };
    }

}

