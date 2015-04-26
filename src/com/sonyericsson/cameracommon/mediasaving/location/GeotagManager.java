/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  java.lang.IllegalArgumentException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.sonyericsson.cameracommon.mediasaving.location;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.CompoundButton;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.DoNotShowAgainCheckForGeotagDialog;
import com.sonyericsson.cameracommon.commonsetting.values.Geotag;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagLocationListener;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagSettingListener;
import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogController;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.RegionConfig;

public class GeotagManager {
    private static final String TAG = GeotagManager.class.getSimpleName();
    private boolean mAcquiring = false;
    private final Context mContext;
    private boolean mIsItemChecked;
    private LocationAcquiredListener mLocationAcquiredListener;
    private GeotagLocationListener mLocationListenerGps;
    private GeotagLocationListener mLocationListenerNetwork;
    private LocationManager mLocationManager;

    public GeotagManager(Context context) {
        this.mContext = context;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkLocationService(Geotag geotag, BaseActivity baseActivity, SettingDialogController settingDialogController, GeotagSettingListener geotagSettingListener) {
        this.updateLocation(geotag);
        baseActivity.getCommonSettings().set(geotag);
        if (geotagSettingListener != null) {
            boolean bl = geotag == Geotag.ON;
            geotagSettingListener.onSet(bl);
        }
        if (!(geotag != Geotag.ON || GeotagManager.isLocationServiceAvailable(baseActivity))) {
            if (settingDialogController != null) {
                settingDialogController.closeCurrentDialog();
            }
            baseActivity.getCommonSettings().set(Geotag.ON);
            baseActivity.getMessagePopup().showLaunchSettingAppDialog((Activity)baseActivity, geotagSettingListener);
        }
    }

    public static Geotag getGeotag(BaseActivity baseActivity, Geotag geotag) {
        Geotag geotag2 = geotag;
        if (!(geotag != Geotag.ON || GeotagManager.isLocationServiceAvailable(baseActivity))) {
            geotag2 = Geotag.OFF;
        }
        return geotag2;
    }

    private static boolean isLocationServiceAvailable(BaseActivity baseActivity) {
        baseActivity.readLocationSettings();
        if (baseActivity.isGpsLocationAllowed() || baseActivity.isNetworkLocationAllowed()) {
            return true;
        }
        return false;
    }

    public void assignResource() {
        if (this.mLocationListenerGps == null) {
            this.mLocationListenerGps = new GeotagLocationListener(this, "gps");
        }
        if (this.mLocationListenerNetwork == null) {
            this.mLocationListenerNetwork = new GeotagLocationListener(this, "network");
        }
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager)this.mContext.getSystemService("location");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void confirmLocationService(BaseActivity baseActivity, CommonSettings commonSettings, GeotagSettingListener geotagSettingListener) {
        if ((DoNotShowAgainCheckForGeotagDialog)commonSettings.get(CommonSettingKey.DO_NOT_SHOW_AGAIN_CHECK_FOR_GEOTAG_DIALOG) == DoNotShowAgainCheckForGeotagDialog.CHECKED) {
            return;
        }
        Geotag geotag = (Geotag)commonSettings.get(CommonSettingKey.GEO_TAG);
        if (geotag == Geotag.ON && GeotagManager.isLocationServiceAvailable(baseActivity)) {
            commonSettings.set(Geotag.ON);
            geotagSettingListener.onSet(true);
        } else {
            commonSettings.set(Geotag.OFF);
            geotagSettingListener.onSet(false);
            geotag = Geotag.OFF;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$Geotag[geotag.ordinal()]) {
            case 1: {
                return;
            }
        }
        this.mIsItemChecked = false;
        baseActivity.getMessagePopup().showConfirmLocationAccess(baseActivity, true, (DialogInterface.OnClickListener)new ConfirmLocationAccessDialogListener(baseActivity, commonSettings, geotagSettingListener), (DialogInterface.OnClickListener)new ConfirmLocationAccessDialogListener(baseActivity, commonSettings, geotagSettingListener), (DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener(){

            public void onCancel(DialogInterface dialogInterface) {
            }
        }, (CompoundButton.OnCheckedChangeListener)new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                GeotagManager.this.mIsItemChecked = bl;
            }
        });
    }

    public Location getCurrentLocation() {
        Location location = this.mLocationListenerNetwork.current();
        Location location2 = this.mLocationListenerGps.current();
        if (location2 != null) {
            return location2;
        }
        if (location != null) {
            return location;
        }
        return null;
    }

    public GeotagLocationListener getLocationListener(String string) {
        if (this.mLocationListenerGps != null && this.mLocationListenerGps.mProvider.equals((Object)string)) {
            return this.mLocationListenerGps;
        }
        if (this.mLocationListenerNetwork != null && this.mLocationListenerNetwork.mProvider.equals((Object)string)) {
            return this.mLocationListenerNetwork;
        }
        return null;
    }

    public boolean isAcquiring() {
        return this.mAcquiring;
    }

    public boolean isDisabled() {
        GeotagLocationListener geotagLocationListener = this.mLocationListenerGps;
        boolean bl = false;
        if (geotagLocationListener != null) {
            bl = this.mLocationListenerGps.isDisabled();
        }
        GeotagLocationListener geotagLocationListener2 = this.mLocationListenerNetwork;
        boolean bl2 = false;
        if (geotagLocationListener2 != null) {
            bl2 = this.mLocationListenerNetwork.isDisabled();
        }
        if (bl && bl2) {
            return true;
        }
        return false;
    }

    public boolean isGpsAcquired() {
        GeotagLocationListener geotagLocationListener = this.mLocationListenerGps;
        boolean bl = false;
        if (geotagLocationListener != null) {
            Location location = this.mLocationListenerGps.current();
            bl = false;
            if (location != null) {
                bl = true;
            }
        }
        return bl;
    }

    public boolean isNetworkAcquired() {
        GeotagLocationListener geotagLocationListener = this.mLocationListenerNetwork;
        boolean bl = false;
        if (geotagLocationListener != null) {
            Location location = this.mLocationListenerNetwork.current();
            bl = false;
            if (location != null) {
                bl = true;
            }
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void notifyStatus() {
        if (this.isDisabled()) {
            this.updateLocation(Geotag.OFF);
            if (this.mLocationAcquiredListener == null) return;
            {
                this.mLocationAcquiredListener.onDisabled();
                return;
            }
        } else {
            boolean bl = this.isGpsAcquired();
            boolean bl2 = this.isNetworkAcquired();
            if (bl || bl2) {
                if (this.mLocationAcquiredListener == null) return;
                {
                    this.mLocationAcquiredListener.onAcquired(bl, bl2);
                    return;
                }
            } else {
                if (this.mLocationAcquiredListener == null) return;
                {
                    this.mLocationAcquiredListener.onLost();
                    return;
                }
            }
        }
    }

    public void release() {
        this.setLocationAcquiredListener(null);
        this.mLocationListenerGps = null;
        this.mLocationListenerNetwork = null;
    }

    public void releaseResource() {
        this.stopReceivingLocationUpdates();
        this.mLocationManager = null;
    }

    public void setGeotag(final Geotag geotag, final BaseActivity baseActivity, final SettingDialogController settingDialogController, final GeotagSettingListener geotagSettingListener) {
        if (geotag == Geotag.ON) {
            if (RegionConfig.isChinaRegion((Context)baseActivity)) {
                baseActivity.getMessagePopup().showConfirmLocationAccess(baseActivity, false, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialogInterface, int n) {
                        GeotagManager.this.checkLocationService(geotag, baseActivity, settingDialogController, geotagSettingListener);
                    }
                }, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialogInterface, int n) {
                        GeotagManager.this.checkLocationService(Geotag.OFF, baseActivity, settingDialogController, geotagSettingListener);
                    }
                }, (DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener(){

                    public void onCancel(DialogInterface dialogInterface) {
                        GeotagManager.this.checkLocationService(Geotag.OFF, baseActivity, settingDialogController, geotagSettingListener);
                    }
                }, null);
                return;
            }
            super.checkLocationService(geotag, baseActivity, settingDialogController, geotagSettingListener);
            return;
        }
        super.checkLocationService(geotag, baseActivity, settingDialogController, geotagSettingListener);
    }

    public void setLocationAcquiredListener(LocationAcquiredListener locationAcquiredListener) {
        this.mLocationAcquiredListener = locationAcquiredListener;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void startLocationUpdates(boolean var1, boolean var2_2) throws SecurityException {
        var7_3 = this;
        // MONITORENTER : var7_3
        ** if (!var1) goto lbl8
lbl4: // 1 sources:
        try {
            this.mLocationManager.requestLocationUpdates("gps", 60000, 0.0f, (LocationListener)this.mLocationListenerGps);
            this.mLocationListenerGps.reset();
            this.mAcquiring = true;
lbl8: // 2 sources:
            if (var2_2) {
                this.mLocationManager.requestLocationUpdates("network", 60000, 0.0f, (LocationListener)this.mLocationListenerNetwork);
                this.mLocationListenerNetwork.reset();
                this.mAcquiring = true;
            }
            // MONITOREXIT : var7_3
            return;
        }
        catch (SecurityException var5_4) {
            CameraLogger.e(GeotagManager.TAG, "provider can't access.", (Throwable)var5_4);
            this.mAcquiring = false;
            throw var5_4;
        }
        catch (IllegalArgumentException var3_5) {
            this.mAcquiring = false;
            return;
        }
    }

    public void stopReceivingLocationUpdates() {
        GeotagManager geotagManager = this;
        synchronized (geotagManager) {
            if (this.mAcquiring) {
                if (this.mLocationManager != null) {
                    this.mLocationListenerGps.reset();
                    this.mLocationListenerNetwork.reset();
                    this.mLocationManager.removeUpdates((LocationListener)this.mLocationListenerGps);
                    this.mLocationManager.removeUpdates((LocationListener)this.mLocationListenerNetwork);
                }
                this.mAcquiring = false;
            }
            return;
        }
    }

    public void updateLocation(CommonSettingValue commonSettingValue) {
        this.assignResource();
        this.stopReceivingLocationUpdates();
        if (commonSettingValue == Geotag.ON) {
            if (this.mLocationAcquiredListener != null) {
                this.mLocationAcquiredListener.onLost();
            }
            this.startLocationUpdates(LocationSettingsReader.isLocationProviderAllowed(this.mContext, "gps"), LocationSettingsReader.isLocationProviderAllowed(this.mContext, "network"));
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ConfirmLocationAccessDialogListener
    implements DialogInterface.OnClickListener {
        private final BaseActivity mActivity;
        private final GeotagSettingListener mListener;
        private final CommonSettings mSettings;

        public ConfirmLocationAccessDialogListener(BaseActivity baseActivity, CommonSettings commonSettings, GeotagSettingListener geotagSettingListener) {
            this.mActivity = baseActivity;
            this.mSettings = commonSettings;
            this.mListener = geotagSettingListener;
        }

        public void onClick(DialogInterface dialogInterface, int n) {
            if (GeotagManager.this.mIsItemChecked) {
                this.mSettings.set(DoNotShowAgainCheckForGeotagDialog.CHECKED);
            }
            switch (n) {
                default: {
                    return;
                }
                case -1: 
            }
            GeotagManager.this.checkLocationService(Geotag.ON, this.mActivity, null, this.mListener);
        }
    }

}

