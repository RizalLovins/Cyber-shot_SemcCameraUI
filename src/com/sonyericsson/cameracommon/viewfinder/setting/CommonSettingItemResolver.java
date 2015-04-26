/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ServiceInfo
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.viewfinder.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettings;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.StaticConfigurationUtil;
import com.sonyericsson.cameracommon.viewfinder.setting.CommonSettingExecutorFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommonSettingItemResolver {
    private final Context mContext;
    private final SettingDialogItemFactory mDialogItemFactory;
    private final CommonSettingExecutorFactory mExecutorFactory;
    private final CommonSettingKey[] mItems;
    private final CommonSettings mSettings;

    public CommonSettingItemResolver(Context context, CommonSettings commonSettings, CommonSettingExecutorFactory commonSettingExecutorFactory, CommonSettingKey[] arrcommonSettingKey) {
        this.mContext = context;
        this.mSettings = commonSettings;
        this.mExecutorFactory = commonSettingExecutorFactory;
        this.mDialogItemFactory = new SettingDialogItemFactory();
        this.mItems = arrcommonSettingKey;
    }

    private int getDialogItemType(CommonSettingKey commonSettingKey) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$CommonSettingKey[commonSettingKey.ordinal()]) {
            default: {
                return 3;
            }
            case 2: 
            case 6: 
            case 7: 
        }
        return 4;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private SettingItem getSettingItem(CommonSettingValue commonSettingValue, SettingExecutorInterface<CommonSettingValue> settingExecutorInterface) {
        boolean bl;
        CommonSettingKey commonSettingKey = commonSettingValue.getCommonSettingKey();
        boolean bl2 = commonSettingValue.equals((Object)this.mSettings.get(commonSettingKey));
        if (bl2 || this.mSettings.isSelectable(commonSettingKey)) {
            bl = true;
            do {
                return SettingItemBuilder.build(commonSettingValue).iconId(commonSettingValue.getIconId()).textId(commonSettingValue.getTextId()).dialogItemType(2).executor(settingExecutorInterface).selected(bl2).selectable(bl).commit();
                break;
            } while (true);
        }
        bl = false;
        return SettingItemBuilder.build(commonSettingValue).iconId(commonSettingValue.getIconId()).textId(commonSettingValue.getTextId()).dialogItemType(2).executor(settingExecutorInterface).selected(bl2).selectable(bl).commit();
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean isTouchBlockSupported() {
        ServiceInfo serviceInfo;
        if (!CommonUtility.isPackageExist("com.sonymobile.touchblocker", this.mContext)) {
            return false;
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager == null) return false;
        Intent intent = new Intent();
        if (intent == null) return false;
        intent.setComponent(new ComponentName("com.sonymobile.touchblocker", "com.sonymobile.touchblocker.TouchBlockerService"));
        intent.putExtra("launched_from_smallapp", false);
        Iterator iterator = packageManager.queryIntentServices(intent, 4).iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while ((serviceInfo = ((android.content.pm.ResolveInfo)iterator.next()).serviceInfo) == null || !"com.sonymobile.touchblocker".equals((Object)serviceInfo.packageName));
        return serviceInfo.exported;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isVisible(CommonSettingKey commonSettingKey) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$CommonSettingKey[commonSettingKey.ordinal()]) {
            case 1: {
                return AutoUploadSettings.getInstance().isAvailable();
            }
            case 2: {
                if (!StaticConfigurationUtil.isForceSound()) return true;
                return false;
            }
            case 3: {
                if (!CommonUtility.isPreinstalledApp(this.mContext)) return true;
                return false;
            }
            default: {
                return true;
            }
            case 4: {
                return super.isTouchBlockSupported();
            }
            case 5: 
        }
        if (!CommonUtility.shouldStorageForceInternal(this.mContext)) return true;
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public SettingAdapter generateItemAdapter() {
        SettingAdapter settingAdapter = new SettingAdapter(this.mContext, this.mDialogItemFactory);
        ArrayList arrayList = new ArrayList();
        for (CommonSettingKey commonSettingKey : this.mItems) {
            if (!this.isVisible(commonSettingKey)) continue;
            arrayList.add((Object)commonSettingKey);
        }
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            CommonSettingKey commonSettingKey2 = (CommonSettingKey)iterator.next();
            int n = this.getDialogItemType(commonSettingKey2);
            SettingItemBuilder<CommonSettingKey> settingItemBuilder = SettingItemBuilder.build(commonSettingKey2).iconId(this.mSettings.get(commonSettingKey2).getIconId()).textId(commonSettingKey2.getTitleId()).text(commonSettingKey2.getTitle(this.mContext)).dialogItemType(n).executor(this.mExecutorFactory.getExecutor(commonSettingKey2, this.mDialogItemFactory));
            if (!commonSettingKey2.equals((Object)CommonSettingKey.AUTO_UPLOAD)) {
                SettingExecutorInterface<CommonSettingValue> settingExecutorInterface = this.mExecutorFactory.getChangeValueExecutor(n);
                CommonSettingValue[] arrcommonSettingValue = commonSettingKey2.getValues();
                int n2 = arrcommonSettingValue.length;
                for (int i = 0; i < n2; ++i) {
                    settingItemBuilder.item(this.getSettingItem(arrcommonSettingValue[i], settingExecutorInterface));
                }
            }
            settingAdapter.add((Object)settingItemBuilder.commit());
        }
        return settingAdapter;
    }

    public SettingAdapter generateItemAdapter(CommonSettingKey commonSettingKey) {
        SettingAdapter settingAdapter = new SettingAdapter(this.mContext, this.mDialogItemFactory);
        int n = super.getDialogItemType(commonSettingKey);
        SettingExecutorInterface<CommonSettingValue> settingExecutorInterface = this.mExecutorFactory.getChangeValueExecutor(n);
        CommonSettingValue[] arrcommonSettingValue = commonSettingKey.getValues();
        int n2 = arrcommonSettingValue.length;
        for (int i = 0; i < n2; ++i) {
            settingAdapter.add((Object)super.getSettingItem(arrcommonSettingValue[i], settingExecutorInterface));
        }
        return settingAdapter;
    }

}

