/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Locale
 */
package com.sonyericsson.cameracommon.contentsview.contents;

import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.contentsview.contents.ExtraIconResources;
import com.sonyericsson.cameracommon.contentsview.contents.PlayIconResources;
import java.util.Locale;

public class BurstShotContent
extends Content {
    public static final int COUNT_UP_MAX_NUM = 999;

    public BurstShotContent(Content.ContentInfo contentInfo) {
        super(contentInfo, ExtraIconResources.get(contentInfo.mContentType), PlayIconResources.get(contentInfo.mContentType));
        this.mCountText = super.createCountText(contentInfo.mGroupedImage);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String createCountText(int n) {
        if (n > 0 && n <= 999) {
            return String.valueOf((int)n);
        }
        String string = null;
        if (999 >= n) return string;
        return "" + 999 + String.format((Locale)Locale.US, (String)"+", (Object[])new Object[0]);
    }

    @Override
    public int getExtraIconResourceId() {
        return R.drawable.cam_photo_stack_burst_icn;
    }

    @Override
    public boolean shouldShowExtraIcon() {
        return true;
    }

    @Override
    public boolean shouldShowPlayableIcon() {
        return false;
    }
}

