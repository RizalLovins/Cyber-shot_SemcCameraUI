/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.widget.FrameLayout
 *  android.widget.RelativeLayout
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.sonyericsson.cameracommon.viewfinder;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.viewfinder.BaseViewFinderLayout;
import com.sonyericsson.cameracommon.viewfinder.DefaultLayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.LayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.balloontips.BalloonTips;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButton;
import com.sonyericsson.cameracommon.viewfinder.indicators.GeotagIndicator;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingIndicator;
import com.sonyericsson.cameracommon.zoombar.Zoombar;
import java.util.HashMap;
import java.util.Map;

public class DefaultLayoutPatternApplier
implements LayoutPatternApplier {
    public static final int D = 3;
    public static final int H = 2;
    public static final int N = 0;
    public static final int S = 1;
    protected BaseViewFinderLayout mLayout;
    protected Map<LayoutPattern, Map<Component, Integer>> mPatternComponentMap = new HashMap();

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void apply(LayoutPattern var1) {
        var2_2 = (Map)this.mPatternComponentMap.get((Object)var1);
        switch ((Integer)var2_2.get((Object)DefaultComponent.CAPTURE_BUTTON)) {
            default: {
                if (!this.mLayout.getOnScreenButtonGroup().isTouched()) break;
                this.mLayout.getOnScreenButtonGroup().setVisibility(0);
                ** GOTO lbl14
            }
            case 1: {
                this.mLayout.getOnScreenButtonGroup().setVisibility(0);
                ** GOTO lbl14
            }
            case 2: {
                this.mLayout.getOnScreenButtonGroup().setVisibility(4);
                ** GOTO lbl14
            }
        }
        this.mLayout.getOnScreenButtonGroup().setVisibility(4);
lbl14: // 4 sources:
        switch ((Integer)var2_2.get((Object)DefaultComponent.CONTENT_VIEW)) {
            case 1: {
                this.mLayout.showContentsViewController();
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.hideContentsViewController();
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.MODE_SELECTOR_BUTTON)) {
            case 1: {
                this.mLayout.getCapturingModeButton().setVisibility(0);
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getCapturingModeButton().setVisibility(4);
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.CAPTURE_METHOD_INDICATORS)) {
            case 1: {
                this.mLayout.getCaptureMethodIndicatorContainer().setVisibility(0);
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getCaptureMethodIndicatorContainer().setVisibility(4);
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.MODE_INDICATORS)) {
            case 1: {
                this.mLayout.getModeIndicatorContainer().setVisibility(0);
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getModeIndicatorContainer().setVisibility(4);
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.GEOTAG_INDICATORS)) {
            case 1: {
                this.mLayout.getGeoTagIndicator().show();
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getGeoTagIndicator().hide();
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.STORAGE_INDICATORS)) {
            case 1: {
                this.mLayout.getLowMemoryIndicator().show();
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getLowMemoryIndicator().hide();
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.ZOOM_BAR)) {
            case 1: {
                this.mLayout.getZoomBar().show();
                ** break;
            }
            case 2: {
                this.mLayout.getZoomBar().hide();
            }
lbl62: // 3 sources:
            default: {
                ** GOTO lbl66
            }
            case 3: 
        }
        this.mLayout.getZoomBar().hideDelayed();
lbl66: // 2 sources:
        switch ((Integer)var2_2.get((Object)DefaultComponent.BALLOON_TIPS)) {
            case 1: {
                if (this.mLayout.getBalloonTips().isBalloonTipsEnabled()) {
                    this.mLayout.getBalloonTips().show();
                }
            }
            default: {
                ** GOTO lbl74
            }
            case 2: 
        }
        this.mLayout.getBalloonTips().hide();
lbl74: // 2 sources:
        switch ((Integer)var2_2.get((Object)DefaultComponent.RECORDING_PROGRESS)) {
            case 1: {
                this.mLayout.getRecordingIndicator().setVisibility(0);
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getRecordingIndicator().setVisibility(4);
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.RIGHT_BOTTOM_CAPTURE_BUTTON)) {
            case 1: {
                this.mLayout.getCaptureButtonGroup().setVisibility(0);
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getCaptureButtonGroup().setVisibility(4);
            }
        }
        switch ((Integer)var2_2.get((Object)DefaultComponent.THERMAL_INDICATORS)) {
            case 1: {
                this.mLayout.getThermalIndicator().show();
            }
            default: {
                break;
            }
            case 2: {
                this.mLayout.getThermalIndicator().hide();
            }
        }
        this.mLayout.refresh();
    }

    protected void set(LayoutPattern layoutPattern, int[] arrn) {
        if (DefaultComponent.values().length != arrn.length) {
            throw new IllegalArgumentException("Not equal components count : " + DefaultComponent.values().length + " visibility count : " + arrn.length);
        }
        int n = 0;
        Map map = (Map)this.mPatternComponentMap.get((Object)layoutPattern);
        DefaultComponent[] arrdefaultComponent = DefaultComponent.values();
        int n2 = arrdefaultComponent.length;
        for (int i = 0; i < n2; ++i) {
            map.put((Object)arrdefaultComponent[i], (Object)arrn[n]);
            ++n;
        }
    }

    @Override
    public void setup(BaseViewFinderLayout baseViewFinderLayout, boolean bl) {
        this.mLayout = baseViewFinderLayout;
        this.setupLayoutPattern();
        this.setupVisibilities(bl);
    }

    protected void setupLayoutPattern() {
        for (DefaultLayoutPattern defaultLayoutPattern : DefaultLayoutPattern.values()) {
            this.mPatternComponentMap.put((Object)defaultLayoutPattern, (Object)new HashMap());
        }
    }

    protected void setupVisibilities(boolean bl) {
        if (bl) {
            this.set(DefaultLayoutPattern.PREVIEW, new int[]{1, 1, 1, 1, 1, 3, 2, 2, 2, 2, 2, 1});
            this.set(DefaultLayoutPattern.CLEAR, new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.ZOOMING, new int[]{2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.FOCUS_SEARCHING, new int[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.FOCUS_DONE, new int[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.CAPTURE, new int[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.RECORDING, new int[]{1, 1, 1, 2, 1, 3, 2, 2, 2, 1, 2, 1});
            this.set(DefaultLayoutPattern.BURST_SHOOTING, new int[]{0, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1});
            this.set(DefaultLayoutPattern.MODE_SELECTOR, new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.SETTING, new int[]{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.SELFTIMER, new int[]{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
            this.set(DefaultLayoutPattern.PAUSE_RECORDING, new int[]{1, 1, 1, 2, 1, 3, 2, 2, 2, 1, 2, 1});
            return;
        }
        this.set(DefaultLayoutPattern.PREVIEW, new int[]{1, 1, 1, 1, 1, 3, 1, 1, 0, 2, 2, 1});
        this.set(DefaultLayoutPattern.CLEAR, new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.ZOOMING, new int[]{2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.FOCUS_SEARCHING, new int[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.FOCUS_DONE, new int[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.CAPTURE, new int[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.RECORDING, new int[]{1, 1, 1, 2, 1, 3, 1, 2, 2, 1, 1, 1});
        this.set(DefaultLayoutPattern.BURST_SHOOTING, new int[]{0, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1});
        this.set(DefaultLayoutPattern.MODE_SELECTOR, new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.SETTING, new int[]{1, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.SELFTIMER, new int[]{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        this.set(DefaultLayoutPattern.PAUSE_RECORDING, new int[]{1, 1, 1, 2, 1, 3, 2, 2, 2, 1, 1, 1});
    }

    protected static interface Component {
    }

    protected static final class DefaultComponent
    extends Enum<DefaultComponent>
    implements Component {
        private static final /* synthetic */ DefaultComponent[] $VALUES;
        public static final /* enum */ DefaultComponent BALLOON_TIPS;
        public static final /* enum */ DefaultComponent CAPTURE_BUTTON;
        public static final /* enum */ DefaultComponent CAPTURE_METHOD_INDICATORS;
        public static final /* enum */ DefaultComponent CONTENT_VIEW;
        public static final /* enum */ DefaultComponent GEOTAG_INDICATORS;
        public static final /* enum */ DefaultComponent MODE_INDICATORS;
        public static final /* enum */ DefaultComponent MODE_SELECTOR_BUTTON;
        public static final /* enum */ DefaultComponent RECORDING_PROGRESS;
        public static final /* enum */ DefaultComponent RIGHT_BOTTOM_CAPTURE_BUTTON;
        public static final /* enum */ DefaultComponent STORAGE_INDICATORS;
        public static final /* enum */ DefaultComponent THERMAL_INDICATORS;
        public static final /* enum */ DefaultComponent ZOOM_BAR;

        static {
            CAPTURE_BUTTON = new DefaultComponent();
            CAPTURE_METHOD_INDICATORS = new DefaultComponent();
            MODE_INDICATORS = new DefaultComponent();
            GEOTAG_INDICATORS = new DefaultComponent();
            STORAGE_INDICATORS = new DefaultComponent();
            ZOOM_BAR = new DefaultComponent();
            CONTENT_VIEW = new DefaultComponent();
            MODE_SELECTOR_BUTTON = new DefaultComponent();
            BALLOON_TIPS = new DefaultComponent();
            RECORDING_PROGRESS = new DefaultComponent();
            RIGHT_BOTTOM_CAPTURE_BUTTON = new DefaultComponent();
            THERMAL_INDICATORS = new DefaultComponent();
            DefaultComponent[] arrdefaultComponent = new DefaultComponent[]{CAPTURE_BUTTON, CAPTURE_METHOD_INDICATORS, MODE_INDICATORS, GEOTAG_INDICATORS, STORAGE_INDICATORS, ZOOM_BAR, CONTENT_VIEW, MODE_SELECTOR_BUTTON, BALLOON_TIPS, RECORDING_PROGRESS, RIGHT_BOTTOM_CAPTURE_BUTTON, THERMAL_INDICATORS};
            $VALUES = arrdefaultComponent;
        }

        private DefaultComponent() {
            super(string, n);
        }

        public static DefaultComponent valueOf(String string) {
            return (DefaultComponent)Enum.valueOf((Class)DefaultComponent.class, (String)string);
        }

        public static DefaultComponent[] values() {
            return (DefaultComponent[])$VALUES.clone();
        }
    }

}

