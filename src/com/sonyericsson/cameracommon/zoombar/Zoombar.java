/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.zoombar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;

/*
 * Failed to analyse overrides
 */
public class Zoombar
extends RelativeLayout {
    public static final int DELAY_ZOOMBAR_HIDE = 1000;
    public static final int MIN_VALUE;
    private static final String TAG;
    private float SRZ_LIMIT_POSITION;
    private Type mCurrentType;
    private final Runnable mHideEvent;
    private int mLastMaxValue;
    private int mLastValue;
    private Point mMaxPosition;
    private Point mMinPosition;
    private RelativeLayout mThumbBase;

    static {
        TAG = Zoombar.class.getSimpleName();
    }

    public Zoombar(Context context) {
        super(context, null);
    }

    public Zoombar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public Zoombar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mLastMaxValue = 0;
        this.mMinPosition = new Point();
        this.mMaxPosition = new Point();
        this.mCurrentType = Type.NORMAL;
        this.SRZ_LIMIT_POSITION = 0.5f;
        this.mHideEvent = new Runnable(){

            public void run() {
                Zoombar.this.setVisibility(4);
            }
        };
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.zoombar, (ViewGroup)this, true);
        this.mThumbBase = (RelativeLayout)this.findViewById(R.id.zoombar_indicator);
        super.prepareMinMaxPositions();
    }

    /*
     * Enabled aggressive block sorting
     */
    private int onZoomChanged(int n, int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n > n2) {
            n = n2;
        }
        this.mLastValue = n;
        this.mLastMaxValue = n2;
        int n3 = n2 != 0 ? n * (this.mMinPosition.x - this.mMaxPosition.x) / n2 : 0;
        this.mThumbBase.scrollTo(n3, 0);
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int onZoomChangedWithSuperResolutionInternal(int n, int n2, int n3) {
        int n4;
        if (n < n2) {
            n4 = (int)((float)n3 * this.SRZ_LIMIT_POSITION / (float)n2 * (float)n);
            do {
                return super.onZoomChanged(n4, n3);
                break;
            } while (true);
        }
        n4 = (int)((float)n3 * (1.0f - this.SRZ_LIMIT_POSITION) / (float)(n3 - n2) * (float)(n - n2) + (float)n3 * this.SRZ_LIMIT_POSITION);
        return super.onZoomChanged(n4, n3);
    }

    private void prepareMinMaxPositions() {
        this.mMaxPosition.x = this.getResources().getDimensionPixelSize(R.dimen.zoom_padding_left);
        this.mMaxPosition.y = this.getResources().getDimensionPixelSize(R.dimen.zoom_padding_top);
        this.mMinPosition.x = this.mMaxPosition.x + this.getResources().getDimensionPixelSize(R.dimen.zoom_slider_width) - this.getResources().getDimensionPixelSize(R.dimen.zoom_thumb_width);
        this.mMinPosition.y = this.mMaxPosition.y;
        if (this.mThumbBase != null) {
            ((RelativeLayout.LayoutParams)this.mThumbBase.getLayoutParams()).setMargins(this.mMaxPosition.x, this.mMaxPosition.y, 0, 0);
        }
    }

    private void updateLookAndFeel() {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$zoombar$Zoombar$Type[this.mCurrentType.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                this.setBackgroundResource(R.drawable.cam_zoom_bar_portrait_icn);
                return;
            }
            case 2: 
        }
        this.setBackgroundResource(R.drawable.cam_zoom_bar_clear_supported_dz_on_clear_work_portrait_icn);
    }

    public void hide() {
        Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        this.setVisibility(4);
    }

    public void hideDelayed() {
        Handler handler = this.getHandler();
        if (handler != null) {
            handler.postDelayed(this.mHideEvent, 1000);
        }
    }

    public void initZoom() {
        this.onZoomChanged(0, 0);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public int onZoomChanged(int n, int n2, int n3) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$zoombar$Zoombar$Type[this.mCurrentType.ordinal()]) {
            default: {
                return 0;
            }
            case 1: {
                return super.onZoomChanged(n, n3);
            }
            case 2: 
        }
        return super.onZoomChangedWithSuperResolutionInternal(n, n2, n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSensorOrientation(int n) {
        super.updateLookAndFeel();
        this.setPivotX(this.mContext.getResources().getDimension(R.dimen.zoom_icon_height) / 2.0f);
        this.setPivotY(this.mContext.getResources().getDimension(R.dimen.zoom_icon_height) / 2.0f);
        if (n == 1) {
            this.setRotation(0.0f);
        } else {
            this.setRotation(90.0f);
        }
        if (this.mLastMaxValue >= 0) {
            super.onZoomChanged(this.mLastValue, this.mLastMaxValue);
        }
    }

    public void show() {
        Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        this.setVisibility(0);
    }

    public void updateZoombarType(Type type) {
        this.mCurrentType = type;
        super.updateLookAndFeel();
    }

    public int zoom(int n, int n2) {
        return super.onZoomChanged(n, n2);
    }

    public static final class Type
    extends Enum<Type> {
        private static final /* synthetic */ Type[] $VALUES;
        public static final /* enum */ Type NORMAL = new Type();
        public static final /* enum */ Type PARTIAL_SUPER_RESOLUTION = new Type();

        static {
            Type[] arrtype = new Type[]{NORMAL, PARTIAL_SUPER_RESOLUTION};
            $VALUES = arrtype;
        }

        private Type() {
            super(string, n);
        }

        public static Type valueOf(String string) {
            return (Type)Enum.valueOf((Class)Type.class, (String)string);
        }

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }
    }

}

