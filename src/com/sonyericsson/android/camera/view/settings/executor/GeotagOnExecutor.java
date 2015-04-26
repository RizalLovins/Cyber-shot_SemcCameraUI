/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.view.settings.executor;

import android.content.Context;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.view.settings.SettingController;
import com.sonyericsson.android.camera.view.settings.SettingGroup;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.commonsetting.values.Geotag;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagSettingListener;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogController;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogStack;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabDialogBasic;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabs;
import com.sonyericsson.cameracommon.setting.executor.SettingChangeExecutor;
import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;
import com.sonyericsson.cameracommon.utility.RegionConfig;

public class GeotagOnExecutor
extends SettingChangeExecutor<ParameterValue> {
    private final ExtendedActivity mActivity;
    private final SettingController mSettingController;

    public GeotagOnExecutor(ExtendedActivity extendedActivity, SettingChangerInterface<ParameterValue> settingChangerInterface, SettingController settingController) {
        super(settingChangerInterface);
        this.mActivity = extendedActivity;
        this.mSettingController = settingController;
    }

    @Override
    public void onExecute(TypedSettingItem<ParameterValue> typedSettingItem) {
        if (RegionConfig.isChinaRegion((Context)this.mActivity)) {
            this.mSettingController.closeDialogs();
        }
        this.mActivity.getGeoTagManager().setGeotag(com.sonyericsson.cameracommon.commonsetting.values.Geotag.ON, (BaseActivity)this.mActivity, this.mSettingController.getSettingDialogController(), new GeotagDialogListener((GeotagOnExecutor)this, null));
    }

    private class GeotagDialogListener
    implements GeotagSettingListener {
        final /* synthetic */ GeotagOnExecutor this$0;

        private GeotagDialogListener(GeotagOnExecutor geotagOnExecutor) {
            this.this$0 = geotagOnExecutor;
        }

        /* synthetic */ GeotagDialogListener(GeotagOnExecutor geotagOnExecutor,  var2_2) {
            super(geotagOnExecutor);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onSet(boolean bl) {
            Geotag geotag = bl ? Geotag.ON : Geotag.OFF;
            this.this$0.mActivity.getParameterManager().set(geotag);
            if (RegionConfig.isChinaRegion((Context)this.this$0.mActivity) && !bl) {
                this.this$0.mSettingController.openMenuDialog(SettingGroup.COMMON);
                this.this$0.mSettingController.getSettingDialogStack().getMenuDialog().setSelectedTab(SettingTabs.Tab.Common);
            }
        }
    }

}

