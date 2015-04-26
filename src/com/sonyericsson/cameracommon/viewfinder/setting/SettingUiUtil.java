/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  java.lang.Object
 *  java.lang.Runnable
 */
package com.sonyericsson.cameracommon.viewfinder.setting;

import android.os.Handler;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogStack;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabs;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.viewfinder.setting.SettingUi;

public class SettingUiUtil {
    private final Object mMenuShortcutTag;
    private final SettingUi mUi;

    public SettingUiUtil(SettingUi settingUi, Object object) {
        this.mUi = settingUi;
        this.mMenuShortcutTag = object;
    }

    static /* synthetic */ SettingUi access$100(SettingUiUtil settingUiUtil) {
        return settingUiUtil.mUi;
    }

    public void openMenuDialogAndSelectItem(SettingAdapter settingAdapter, Object object, SettingTabs.Tab[] arrtab, SettingTabs.Tab tab, int n) {
        this.mUi.selectShortcut(this.mMenuShortcutTag);
        int n2 = 0;
        do {
            int n3 = settingAdapter.getCount();
            SettingItem settingItem = null;
            if (n2 < n3) {
                if (((SettingItem)settingAdapter.getItem(n2)).compareData(object)) {
                    settingItem = (SettingItem)settingAdapter.getItem(n2);
                    settingItem.setSelected(true);
                }
            } else {
                this.mUi.openMenuDialog(settingAdapter, arrtab, tab, this.mMenuShortcutTag, n);
                if (settingItem != null) {
                    final SettingItem settingItem2 = settingItem;
                    new Handler().post((Runnable)new Runnable(){

                        public void run() {
                            if (SettingUiUtil.access$100((SettingUiUtil)SettingUiUtil.this).mDialogStack.isOpened(SettingUiUtil.this.mMenuShortcutTag)) {
                                settingItem2.select();
                            }
                        }
                    });
                }
                return;
            }
            ++n2;
        } while (true);
    }

}

