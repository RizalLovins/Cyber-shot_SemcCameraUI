/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.AnimationDrawable
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 */
package com.sonyericsson.cameracommon.viewfinder.indicators;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.BrandConfig;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;

public class GeotagIndicator
extends Indicator {
    public GeotagIndicator(ImageView imageView) {
        super(imageView);
    }

    private int getAcquiredGpsIcon() {
        if (BrandConfig.isVerizonBrand()) {
            return R.drawable.cam_acquired_gps_vzw_icn;
        }
        return R.drawable.cam_acquired_gps_icn;
    }

    private int getAcquiringGpsResource() {
        if (BrandConfig.isVerizonBrand()) {
            return R.drawable.cam_acquiring_gps_vzw_anim;
        }
        return R.drawable.cam_acquiring_gps_anim;
    }

    public void isAcquired(boolean bl) {
        if (bl) {
            this.mView.setImageResource(super.getAcquiredGpsIcon());
            return;
        }
        this.mView.setImageResource(super.getAcquiringGpsResource());
        ((AnimationDrawable)this.mView.getDrawable()).start();
    }
}

