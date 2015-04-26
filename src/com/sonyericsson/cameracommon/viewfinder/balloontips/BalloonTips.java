/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.viewfinder.balloontips;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.BalloonTipsCounter;
import com.sonyericsson.cameracommon.utility.PresetConfigurationResolver;

/*
 * Failed to analyse overrides
 */
public class BalloonTips
extends RelativeLayout {
    RelativeLayout mBalloonTips;
    CommonSettings mCommonSettings;
    private int mDistanceToModeSelector = 0;
    private boolean mIsBalloonTipsOpen = false;
    private int mLandscapePosition = 0;

    public BalloonTips(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void ajustHeightMargin() {
        ImageView imageView = (ImageView)this.findViewById(R.id.balloon_tips_background_port_1);
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        this.findViewById(R.id.balloon_tips_background_port_2).getGlobalVisibleRect(rect);
        this.findViewById(R.id.balloon_tips_background_land_2).getGlobalVisibleRect(rect2);
        int n = this.findViewById(R.id.balloon_tips_background_port_2).getMeasuredWidth();
        int n2 = this.mLandscapePosition == 15 ? this.mBalloonTips.getMeasuredWidth() / 2 - n / 2 : this.mBalloonTips.getMeasuredHeight() / 2 - n / 2;
        imageView.setMinimumWidth(n2);
    }

    private int getBalloonTipsBottomMargin(int n) {
        RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(n);
        int n2 = this.getMeasuredHeight() - this.mDistanceToModeSelector;
        return relativeLayout.getBottom() - relativeLayout.getMeasuredHeight() / 2 - n2;
    }

    public void hide() {
        this.mBalloonTips.setVisibility(4);
        this.mIsBalloonTipsOpen = false;
    }

    public boolean isBalloonTipsEnabled() {
        return PresetConfigurationResolver.isBalloonTipsEnabled(this.mCommonSettings);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.rotateBalloonTips();
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (this.mBalloonTips != null) {
            super.ajustHeightMargin();
        }
        super.onMeasure(n, n2);
    }

    public void releaseBalloonTips() {
        this.mLandscapePosition = 0;
        this.mBalloonTips = null;
    }

    public void rotateBalloonTips() {
        RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(R.id.balloon_tips_port);
        RelativeLayout relativeLayout2 = (RelativeLayout)this.findViewById(R.id.balloon_tips_land);
        if (this.mLandscapePosition != 15) {
            relativeLayout2.setTranslationY((float)(- this.getBalloonTipsBottomMargin(R.id.balloon_tips_land)));
        }
        if (this.mLandscapePosition == 15) {
            relativeLayout.setPivotX((float)relativeLayout2.getMeasuredWidth() / 2.0f);
            relativeLayout.setPivotY((float)relativeLayout2.getMeasuredHeight() / 2.0f);
            relativeLayout.setRotation(-90.0f);
            relativeLayout.setTranslationX((float)relativeLayout2.getMeasuredWidth() / 2.0f - (float)relativeLayout2.getMeasuredHeight() / 2.0f);
            return;
        }
        relativeLayout.setPivotX((float)relativeLayout2.getMeasuredWidth() - (float)relativeLayout2.getMeasuredHeight() / 2.0f);
        relativeLayout.setPivotY((float)relativeLayout2.getMeasuredHeight() / 2.0f);
        relativeLayout.setRotation(-90.0f);
        relativeLayout.setTranslationY((float)(- relativeLayout2.getMeasuredWidth() - relativeLayout2.getMeasuredHeight() + this.getBalloonTipsBottomMargin(R.id.balloon_tips_port)));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setBalloonTipsOrientation(int n) {
        this.mBalloonTips.setVisibility(4);
        this.mBalloonTips = n == 1 ? (RelativeLayout)this.findViewById(R.id.balloon_tips_port) : (RelativeLayout)this.findViewById(R.id.balloon_tips_land);
        if (this.mIsBalloonTipsOpen) {
            this.show();
        }
    }

    public void setContentsText(int n) {
        ((TextView)this.findViewById(R.id.balloon_tips_contents_land)).setText(n);
        ((TextView)this.findViewById(R.id.balloon_tips_contents_port)).setText(n);
    }

    public void setTitleText(int n) {
        ((TextView)this.findViewById(R.id.balloon_tips_title_land)).setText(n);
        ((TextView)this.findViewById(R.id.balloon_tips_title_port)).setText(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setupBalloonTips(CommonSettings commonSettings, int n, boolean bl) {
        this.mCommonSettings = commonSettings;
        this.mDistanceToModeSelector = n;
        this.mLandscapePosition = bl ? 15 : 12;
        Resources resources = this.getContext().getResources();
        String string = resources.getString(R.string.cam_strings_balloon_tips_modeselector_title_txt) + " " + resources.getString(R.string.cam_strings_balloon_tips_modeselector_txt);
        this.mBalloonTips = (RelativeLayout)this.findViewById(R.id.balloon_tips_land);
        ((RelativeLayout.LayoutParams)this.mBalloonTips.getLayoutParams()).addRule(this.mLandscapePosition);
        this.mBalloonTips.setVisibility(4);
        this.mBalloonTips.setContentDescription((CharSequence)string);
        this.mBalloonTips = (RelativeLayout)this.findViewById(R.id.balloon_tips_port);
        ((RelativeLayout.LayoutParams)this.mBalloonTips.getLayoutParams()).addRule(this.mLandscapePosition);
        this.mBalloonTips.setVisibility(4);
        this.mBalloonTips.setContentDescription((CharSequence)string);
    }

    public void setupBalloonTips(CommonSettings commonSettings, boolean bl) {
        this.setupBalloonTips(commonSettings, 0, bl);
    }

    public void show() {
        this.mBalloonTips.setVisibility(0);
        this.mIsBalloonTipsOpen = true;
    }

    public void stopBalloonTipsCounter() {
        this.mCommonSettings.set(BalloonTipsCounter.COUNT_STOP);
    }
}

