/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Interpolator
 *  android.view.animation.OvershootInterpolator
 *  android.view.animation.ScaleAnimation
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialogitem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;

public class SettingIcon
extends SettingDialogItem {
    private static final int ICON_FADE_SWITCH_ANIMATION_DURATION = 250;
    private static final int ICON_FADE_SWITCH_ANIMATION_OFFSET = 50;
    private static final int PRESSED_COLOR_FILTER = 1711276032;
    private static final String TAG = SettingIcon.class.getSimpleName();
    private final ViewHolder mHolder;
    private final View.OnClickListener mOnClickListener;

    public SettingIcon(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mOnClickListener = new View.OnClickListener(){

            public void onClick(View view) {
                if (SettingIcon.this.getView().isShown() && SettingIcon.this.getItem().isSelectable()) {
                    SettingIcon.this.select(SettingIcon.this.getItem());
                }
            }
        };
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = new FrameLayout(context);
        this.mHolder.mBackground = new FrameLayout(context);
        this.mHolder.mContainer.addView((View)this.mHolder.mBackground);
        this.mHolder.mBackground.getLayoutParams().width = -2;
        this.mHolder.mBackground.getLayoutParams().height = -2;
        this.mHolder.mIcon = new ImageView(context);
        this.mHolder.mBackground.addView((View)this.mHolder.mIcon);
        this.mHolder.mIcon.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.shortcut_icon_background_width);
        this.mHolder.mIcon.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.shortcut_icon_background_height);
        this.mHolder.mIcon.setScaleType(ImageView.ScaleType.CENTER);
        ((android.widget.FrameLayout$LayoutParams)this.mHolder.mIcon.getLayoutParams()).gravity = 17;
        this.mHolder.mIconRes = -1;
    }

    private Animation createIconAnimation(Context context) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 0, (float)this.mHolder.mIcon.getLayoutParams().width / 2.0f, 0, (float)this.mHolder.mIcon.getLayoutParams().height / 2.0f);
        scaleAnimation.setInterpolator((Interpolator)new OvershootInterpolator());
        scaleAnimation.setDuration(250);
        scaleAnimation.setStartOffset(50);
        return scaleAnimation;
    }

    public ImageView getIcon() {
        return this.mHolder.mIcon;
    }

    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }

    @Override
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        Context context = this.mHolder.mContainer.getContext();
        this.mHolder.mBackground.getLayoutParams().width = -1;
        this.mHolder.mBackground.getLayoutParams().height = itemLayoutParams.height;
        if (this.getItem().getIconId() != -1) {
            this.mHolder.mContainer.setVisibility(0);
            this.mHolder.mIcon.setClickable(true);
            this.mHolder.mIcon.setOnClickListener(this.mOnClickListener);
            this.mHolder.mIcon.setSoundEffectsEnabled(this.getItem().isSoundEnabled());
            this.mHolder.mIcon.setContentDescription((CharSequence)this.getItem().getContentDescription(context.getResources()));
            if (!(this.mHolder.mIconRes == this.getItem().getIconId() || this.mHolder.mIconRes == -1 || this.getItem().isSelected())) {
                this.mHolder.mIcon.startAnimation(super.createIconAnimation(this.mHolder.mContainer.getContext()));
            }
            this.mHolder.mIconRes = this.getItem().getIconId();
            this.mHolder.mIcon.setImageResource(this.getItem().getIconId());
            this.mHolder.mIcon.setBackgroundResource(R.drawable.setting_shortcut_selector);
            this.mHolder.mIcon.setSelected(this.getItem().isSelected());
            if (this.getItem().isSelectable()) {
                this.mHolder.mIcon.clearColorFilter();
                return;
            }
            this.mHolder.mIcon.setColorFilter(1711276032);
            return;
        }
        this.mHolder.mContainer.setVisibility(4);
        this.mHolder.mIcon.setClickable(false);
        this.mHolder.mIcon.setOnClickListener(null);
        this.mHolder.mIcon.setContentDescription((CharSequence)this.getItem().getContentDescription(context.getResources()));
        this.mHolder.mIconRes = -1;
        this.mHolder.mIcon.setImageDrawable(null);
        this.mHolder.mIcon.setBackground(null);
        this.mHolder.mIcon.setSelected(false);
    }

    static class ViewHolder {
        FrameLayout mBackground;
        FrameLayout mContainer;
        ImageView mIcon;
        int mIconRes;

        ViewHolder() {
        }
    }

}

