/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$AccessibilityDelegate
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.CheckBox
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.TextView
 *  com.sonymobile.camera.addon.capturingmode.CapturingModeSelector
 *  com.sonymobile.camera.addon.capturingmode.CapturingModeSelector$OnModeFinishListener
 *  com.sonymobile.camera.addon.capturingmode.CapturingModeSelector$OnModeSelectListener
 *  java.lang.CharSequence
 *  java.lang.InterruptedException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 */
package com.sonyericsson.cameracommon.viewfinder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.Geotag;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.AccessibilityEventFilter;
import com.sonyericsson.cameracommon.utility.BrandConfig;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.ParamSharedPrefWrapper;
import com.sonyericsson.cameracommon.utility.PresetConfigurationResolver;
import com.sonyericsson.cameracommon.viewfinder.BaseViewFinderLayout;
import com.sonyericsson.cameracommon.viewfinder.DefaultLayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import com.sonyericsson.cameracommon.viewfinder.InflateTask;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.LayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface;
import com.sonyericsson.cameracommon.viewfinder.balloontips.BalloonTips;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButton;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButtonAttributes;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.OnClickCapturingModeButtonListener;
import com.sonyericsson.cameracommon.viewfinder.indicators.GeotagIndicator;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.cameracommon.zoombar.Zoombar;
import com.sonymobile.camera.addon.capturingmode.CapturingModeSelector;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Failed to analyse overrides
 */
public abstract class ViewFinder
implements BaseActivity.LayoutOrientationChangedListener,
StorageController.StorageDialogStateListener,
StorageController.StorageListener,
LocationAcquiredListener,
ViewFinderInterface,
OnClickCapturingModeButtonListener,
CapturingModeSelector.OnModeFinishListener,
CapturingModeSelector.OnModeSelectListener {
    public static final String SHARED_PREFS_KEY_THERMAL_DISABLED = "THERMAL_DISABLED";
    private static final String TAG = ViewFinder.class.getSimpleName();
    private final BaseActivity mActivity;
    private BaseViewFinderLayout mBaseLayout;
    private CapturingModeSelector mCapturingModeSelector;
    private Future<Map<InflateItem, List<View>>> mInflateFuture;
    private Map<InflateItem, List<View>> mInflateItemMap;
    private boolean mIsRequestedLaterSetup = false;
    private LayoutPatternApplier mLayoutPatternApplier;
    private View mPreInflatedHeadUpDispay;
    protected ParamSharedPrefWrapper mPref;
    private Rect mTargetPreviewRect;
    private RotatableDialog mThermalDialog;

    public ViewFinder(BaseActivity baseActivity, LayoutPatternApplier layoutPatternApplier, ParamSharedPrefWrapper paramSharedPrefWrapper) {
        this.mActivity = baseActivity;
        this.mBaseLayout = new BaseViewFinderLayout(this.mActivity);
        this.mLayoutPatternApplier = layoutPatternApplier;
        this.mPref = paramSharedPrefWrapper;
        this.mActivity.addOrienationListener((BaseActivity.LayoutOrientationChangedListener)this);
    }

    private int getThermalString() {
        if (BrandConfig.isVerizonBrand()) {
            return R.string.cam_strings_dialog_high_temp_vzw_txt;
        }
        return R.string.cam_strings_dialog_high_temp_txt;
    }

    private boolean isAcquired() {
        return this.mActivity.getGeoTagManager().isGpsAcquired() | this.mActivity.getGeoTagManager().isNetworkAcquired();
    }

    private boolean isGeoTagEnabled() {
        return PresetConfigurationResolver.isGeoTagEnabled(this.mActivity.getCommonSettings().get(CommonSettingKey.GEO_TAG), (Context)this.mActivity);
    }

    private boolean isSetupNeeded() {
        if (this.mIsRequestedLaterSetup && this.mCapturingModeSelector == null) {
            return true;
        }
        return false;
    }

    private void setupCapturingModeSelectorLater() {
        this.setupCapturingModeSelector();
        this.setOrientation(this.mBaseLayout.getCurrentOrientation());
        this.mIsRequestedLaterSetup = false;
    }

    private void setupHeadUpDisplay() {
        View view = this.mPreInflatedHeadUpDispay;
        boolean bl = false;
        if (view == null) {
            bl = true;
        }
        if (this.mPreInflatedHeadUpDispay != null) {
            this.mBaseLayout.setPreInflatedHeadUpDisplay(this.mPreInflatedHeadUpDispay);
            this.mPreInflatedHeadUpDispay = null;
        }
        this.mBaseLayout.setOrientation(this.mActivity.getOrientation());
        this.mBaseLayout.setup((OnClickCapturingModeButtonListener)this, this.getThumbnailClickListener());
        this.updateIndicatorState();
        if (!bl) {
            this.mBaseLayout.reloadContentsViewController(this.getThumbnailClickListener());
        }
        if (this.mActivity.getGeoTagManager() != null) {
            this.mActivity.getGeoTagManager().setLocationAcquiredListener((LocationAcquiredListener)this);
        }
        if (this.mActivity.getStorageManager() != null) {
            this.mActivity.getStorageManager().addStorageListener((StorageController.StorageListener)this);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateIndicatorState() {
        boolean bl = true;
        if (PresetConfigurationResolver.isGeoTagEnabled(this.mActivity.getCommonSettings().get(CommonSettingKey.GEO_TAG), (Context)this.mActivity)) {
            if (this.mActivity.getGeoTagManager() != null) {
                boolean bl2 = this.mActivity.getGeoTagManager().isAcquiring();
                this.mBaseLayout.getGeoTagIndicator().set(bl);
                GeotagIndicator geotagIndicator = this.mBaseLayout.getGeoTagIndicator();
                if (bl2) {
                    bl = false;
                }
                geotagIndicator.isAcquired(bl);
            }
        } else {
            this.mBaseLayout.getGeoTagIndicator().set(false);
        }
        this.updateLowMemoryIndicator();
        this.updateThermalIndicator();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLowMemoryIndicator() {
        Indicator indicator = this.mBaseLayout.getLowMemoryIndicator();
        boolean bl = !this.mActivity.getStorageManager().hasEnoughFreeSpace();
        indicator.set(bl);
    }

    private void updateThermalIndicator() {
        this.mBaseLayout.getThermalIndicator().set(this.mActivity.isThermalWarningState());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void changeLayoutTo(LayoutPattern layoutPattern) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mLayoutPatternApplier.apply(layoutPattern);
        if (!layoutPattern.equals((Object)DefaultLayoutPattern.PREVIEW)) return;
        this.mBaseLayout.getGeoTagIndicator().set(super.isGeoTagEnabled());
        if (this.mActivity.getGeoTagManager() == null) return;
        this.mBaseLayout.getGeoTagIndicator().isAcquired(super.isAcquired());
    }

    protected void clearPreInflatedViews() {
        if (this.mInflateItemMap != null) {
            this.mInflateItemMap.clear();
            this.mInflateItemMap = null;
        }
    }

    public void closeCapturingModeSelector() {
        if (this.isCapturingModeSelectorOpened()) {
            this.mCapturingModeSelector.close();
            this.getBaseLayout().setOnTouchListener(null);
            this.onCloseCapturingModeSelector();
        }
    }

    protected Rect computePreviewRect(Activity activity, int n, int n2) {
        if (n == 0 && n2 == 0) {
            CameraLogger.e(TAG, "Preview size is not set.");
            return new Rect(0, 0, 0, 0);
        }
        return LayoutDependencyResolver.getSurfaceRect(activity, (float)n / (float)n2);
    }

    protected void disableAccessibilityTalkBack() {
        this.getBaseLayout().getRootView().setAccessibilityDelegate((View.AccessibilityDelegate)new AccessibilityEventFilter());
    }

    protected BaseActivity getActivity() {
        return this.mActivity;
    }

    protected BaseViewFinderLayout getBaseLayout() {
        return this.mBaseLayout;
    }

    protected LayoutPatternApplier getLayoutPatternApplier() {
        return this.mLayoutPatternApplier;
    }

    public List<View> getPreInflatedView(InflateItem inflateItem) {
        if (this.mInflateItemMap != null) {
            return (List)this.mInflateItemMap.get((Object)inflateItem);
        }
        return null;
    }

    protected DialogInterface.OnCancelListener getThermalDialogOnCancelListener() {
        return null;
    }

    protected DialogInterface.OnClickListener getThermalDialogOnClickListener() {
        return null;
    }

    protected ContentPallet.ThumbnailClickListener getThumbnailClickListener() {
        return new ContentPallet.ThumbnailClickListener(){

            @Override
            public void onClick(Content content) {
                if (content != null) {
                    content.viewContent((Activity)ViewFinder.this.mActivity);
                }
            }
        };
    }

    protected void invalidatePreviewSize() {
        this.mTargetPreviewRect = null;
    }

    public boolean isCapturingModeSelectorOpened() {
        if (this.mCapturingModeSelector == null) {
            return false;
        }
        return this.mCapturingModeSelector.isOpened();
    }

    public boolean isHeadUpDesplayReady() {
        return this.mBaseLayout.isHeadUpDesplayReady();
    }

    public boolean isInflated() {
        if (this.mInflateItemMap != null) {
            return true;
        }
        return false;
    }

    protected boolean isInvalidatedPreviewSize() {
        if (this.mTargetPreviewRect == null) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected boolean isRequestingPreviewSizeChange() {
        if (this.isInvalidatedPreviewSize() || this.mTargetPreviewRect.width() == this.getBaseLayout().getPreview().getWidth() && this.mTargetPreviewRect.height() == this.getBaseLayout().getPreview().getHeight()) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    protected void joinInflateTask() {
        if (this.mInflateFuture != null) {
            try {
                this.mInflateItemMap = (Map)this.mInflateFuture.get();
            }
            catch (InterruptedException var3_1) {
                CameraLogger.e(TAG, "join", (Throwable)var3_1);
            }
            catch (ExecutionException var1_2) {
                CameraLogger.e(TAG, "join", (Throwable)var1_2);
            }
            this.mInflateFuture = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onAcquired(boolean bl, boolean bl2) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        GeotagIndicator geotagIndicator = this.mBaseLayout.getGeoTagIndicator();
        boolean bl3 = bl || bl2;
        geotagIndicator.isAcquired(bl3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onAvailableSizeUpdated(long l) {
        Indicator indicator = this.mBaseLayout.getLowMemoryIndicator();
        boolean bl = !this.mActivity.getStorageManager().hasEnoughFreeSpace();
        indicator.set(bl);
    }

    public void onCaptureDone() {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mBaseLayout.getOnScreenButtonGroup().clearTouched();
    }

    public void onClickCapturingModeButton(CapturingModeButton capturingModeButton) {
        CapturingModeButtonAttributes capturingModeButtonAttributes;
        if (super.isSetupNeeded()) {
            super.setupCapturingModeSelectorLater();
        }
        if ((capturingModeButtonAttributes = capturingModeButton.getCurrentCapturingMode()) != null) {
            this.openCapturingModeSelector(capturingModeButtonAttributes.getName());
        }
    }

    protected abstract void onCloseCapturingModeSelector();

    public void onDestinationToSaveChanged() {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mBaseLayout.reloadContentsViewController(this.getThumbnailClickListener());
    }

    public void onDisabled() {
        this.mActivity.getCommonSettings().set(Geotag.OFF);
        this.mBaseLayout.getGeoTagIndicator().set(false);
        this.mActivity.readLocationSettings();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onLayoutOrientationChanged(BaseActivity.LayoutOrientation layoutOrientation) {
        int n;
        switch (.$SwitchMap$com$sonyericsson$cameracommon$activity$BaseActivity$LayoutOrientation[layoutOrientation.ordinal()]) {
            default: {
                n = 2;
                break;
            }
            case 1: {
                n = 1;
            }
        }
        this.setOrientation(n);
    }

    public void onLost() {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mBaseLayout.getGeoTagIndicator().isAcquired(false);
    }

    protected void onNotifyThermalStatus(boolean bl) {
        if (this.getBaseLayout().getThermalIndicator() != null) {
            this.getBaseLayout().getThermalIndicator().set(bl);
        }
    }

    protected abstract void onOpenCapturingModeSelector();

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onShutterDone(boolean bl) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mBaseLayout.getOnScreenButtonGroup().clearTouched();
        if (!bl) return;
        if (this.getActivity().isOneShot()) return;
        this.getBaseLayout().getContentsViewController().show();
    }

    public void onStorageStateChanged(String string) {
        if (!this.isHeadUpDesplayReady()) {
            // empty if block
        }
    }

    public void onZoomChanged(int n, int n2) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().getZoomBar().zoom(n, n2);
    }

    public void onZoomChanged(int n, int n2, int n3) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().getZoomBar().onZoomChanged(n, n3, n2);
    }

    public void openCapturingModeSelector(String string) {
        if (!(this.isCapturingModeSelectorOpened() || this.mCapturingModeSelector == null)) {
            this.mCapturingModeSelector.open(string);
            this.onOpenCapturingModeSelector();
            this.getBaseLayout().getBalloonTips().hide();
            this.getBaseLayout().getBalloonTips().stopBalloonTipsCounter();
        }
    }

    public void pause() {
        this.mBaseLayout.pause();
        this.closeCapturingModeSelector();
        this.releaseCapturingModeSelector();
    }

    public void release() {
        this.mBaseLayout.release();
        this.releaseCapturingModeSelector();
        this.mActivity.removeOrienationListener((BaseActivity.LayoutOrientationChangedListener)this);
        if (this.mActivity.getGeoTagManager() != null) {
            this.mActivity.getGeoTagManager().setLocationAcquiredListener(null);
        }
        if (this.mActivity.getStorageManager() != null) {
            this.mActivity.getStorageManager().removeStorageListener((StorageController.StorageListener)this);
        }
    }

    public void releaseCapturingModeSelector() {
        if (this.mCapturingModeSelector != null) {
            this.mCapturingModeSelector.release();
            this.mCapturingModeSelector = null;
        }
        this.mIsRequestedLaterSetup = false;
    }

    public void requestSetupHeadUpDisplay() {
        this.setupHeadUpDisplay();
        MeasurePerformance.outResultDelay(1000);
    }

    protected void requestToDimSystemUi() {
        this.getBaseLayout().requestToDimSystemUi();
    }

    protected void requestToRecoverSystemUi() {
        this.getBaseLayout().requestToRecoverSystemUi();
    }

    protected void requestToRemoveSystemUi() {
        this.getBaseLayout().requestToRemoveSystemUi();
    }

    public void requestUpdateSurfaceSize(int n, int n2) {
        float f = (float)n / (float)n2;
        Rect rect = LayoutDependencyResolver.getSurfaceRect((Activity)this.mActivity, f);
        if (this.mTargetPreviewRect != null && this.mTargetPreviewRect.width() == rect.width() && this.mTargetPreviewRect.height() == rect.height()) {
            return;
        }
        this.mTargetPreviewRect = LayoutDependencyResolver.getSurfaceRect((Activity)this.mActivity, f);
        View view = this.getBaseLayout().getPreview();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        layoutParams.width = this.mTargetPreviewRect.width();
        layoutParams.height = this.mTargetPreviewRect.height();
        view.setLeft(this.mTargetPreviewRect.left);
        view.setTop(this.mTargetPreviewRect.top);
        view.requestLayout();
    }

    public void resume() {
        if (this.isHeadUpDesplayReady()) {
            this.mBaseLayout.hideContentsViewController();
            this.mBaseLayout.setupBlankScreen();
            this.mBaseLayout.getZoomBar().initZoom();
        }
    }

    protected void setOrientation(int n) {
        this.mBaseLayout.setOrientation(n);
        if (this.mBaseLayout.isHeadUpDesplayReady() && this.mCapturingModeSelector != null) {
            this.mCapturingModeSelector.setUiOrientation(this.mBaseLayout.getCurrentOrientation());
        }
    }

    protected void setOrientation(int n, int n2) {
        this.mBaseLayout.setOrientation(n, n2);
        if (this.mBaseLayout.isHeadUpDesplayReady() && this.mCapturingModeSelector != null) {
            this.mCapturingModeSelector.setUiOrientation(this.mBaseLayout.getCurrentOrientation());
        }
    }

    public void setPreInflatedHeadUpDisplay(View view) {
        this.mPreInflatedHeadUpDispay = view;
    }

    protected void setup(View view) {
        this.mLayoutPatternApplier.setup(this.mBaseLayout, this.mActivity.isOneShot());
        this.mBaseLayout.attachToWindow(view);
        LayoutDependencyResolver.setupRotatableToast((Activity)this.mActivity);
    }

    public void setupCapturingModeSelector() {
        if (this.mCapturingModeSelector == null) {
            this.mCapturingModeSelector = new CapturingModeSelector((Context)this.mActivity, (ViewGroup)this.getBaseLayout().getLazyInflatedUiComponentContainerFront());
            this.mCapturingModeSelector.setOnModeSelectListener((CapturingModeSelector.OnModeSelectListener)this);
            this.mCapturingModeSelector.setOnModeFinishListener((CapturingModeSelector.OnModeFinishListener)this);
            this.mCapturingModeSelector.setUiOrientation(this.mBaseLayout.getCurrentOrientation());
        }
    }

    protected void showThermalDialog() {
        this.showThermalDialog(this.getThermalString());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void showThermalDialog(int n) {
        StartupAlertDialogListener startupAlertDialogListener = new StartupAlertDialogListener(this.mPref, "THERMAL_DISABLED");
        if (this.mActivity == null) {
            return;
        }
        LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        if (layoutInflater == null) return;
        View view = layoutInflater.inflate(R.layout.thermal_popup_content, null);
        ((TextView)view.findViewById(R.id.header_text)).setText(n);
        ((CheckBox)view.findViewById(R.id.check_box)).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)new CheckBoxListener((CompoundButton.OnCheckedChangeListener)startupAlertDialogListener, (TextView)view.findViewById(R.id.footer_text), -1));
        this.mThermalDialog = this.mActivity.getMessagePopup().showOkAndCustomViewContinuouslyUsed(view, R.string.cam_strings_dialog_high_temp_title_txt, false, R.string.cam_strings_ok_txt, this.getThermalDialogOnClickListener(), this.getThermalDialogOnCancelListener());
        if (this.mThermalDialog == null) return;
        this.mThermalDialog.setOnDismissListener((DialogInterface.OnDismissListener)startupAlertDialogListener);
    }

    protected void startInflateTask(LayoutInflater layoutInflater, List<InflateItem> list) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.mInflateFuture = executorService.submit((Callable)new InflateTask(layoutInflater, list));
        executorService.shutdown();
    }

    public void startSetupCapturingModeSelectorTask() {
        this.mIsRequestedLaterSetup = true;
        new Handler().post((Runnable)new SetupCapturingModeSelectorTask(this, null));
    }

    /*
     * Failed to analyse overrides
     */
    protected static class CheckBoxListener
    implements CompoundButton.OnCheckedChangeListener {
        private final TextView mFooterView;
        private final CompoundButton.OnCheckedChangeListener mListener;
        private final int mMessage;

        public CheckBoxListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener, TextView textView, int n) {
            this.mListener = onCheckedChangeListener;
            this.mFooterView = textView;
            this.mMessage = n;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
            if (bl && this.mMessage != -1) {
                this.mFooterView.setText(this.mMessage);
                this.mFooterView.setVisibility(0);
            } else {
                this.mFooterView.setText((CharSequence)"");
                this.mFooterView.setVisibility(8);
            }
            this.mListener.onCheckedChanged(compoundButton, bl);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SetupCapturingModeSelectorTask
    implements Runnable {
        final /* synthetic */ ViewFinder this$0;

        private SetupCapturingModeSelectorTask(ViewFinder viewFinder) {
            this.this$0 = viewFinder;
        }

        /* synthetic */ SetupCapturingModeSelectorTask(ViewFinder viewFinder,  var2_2) {
            super(viewFinder);
        }

        public void run() {
            if (this.this$0.isSetupNeeded()) {
                this.this$0.setupCapturingModeSelectorLater();
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    protected class StartupAlertDialogListener
    implements DialogInterface.OnDismissListener,
    CompoundButton.OnCheckedChangeListener {
        private final ParamSharedPrefWrapper mSharedPrefs;
        private final String mSharedPrefsKey;

        public StartupAlertDialogListener(ParamSharedPrefWrapper paramSharedPrefWrapper, String string) {
            this.mSharedPrefs = paramSharedPrefWrapper;
            this.mSharedPrefsKey = string;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
            boolean bl2 = false;
            if (bl) {
                bl2 = true;
            }
            this.mSharedPrefs.setParamToSP(this.mSharedPrefsKey, bl2);
        }

        public void onDismiss(DialogInterface dialogInterface) {
            ViewFinder.this.mActivity.setAlertDialogIsOpened(false);
        }
    }

}

