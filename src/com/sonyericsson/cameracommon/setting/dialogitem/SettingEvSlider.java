/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.setting.dialogitem;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import java.util.Iterator;
import java.util.List;

public class SettingEvSlider
extends SettingDialogItem {
    private static final String TAG = SettingEvSlider.class.getSimpleName();
    private final int mBottomPadding;
    private final ViewHolder mHolder;
    private int mSelectedPosition;
    private final int mTopPadding;

    public SettingEvSlider(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mTopPadding = context.getResources().getDimensionPixelSize(R.dimen.ev_slider_memory_top_padding);
        this.mBottomPadding = context.getResources().getDimensionPixelSize(R.dimen.ev_slider_memory_bottom_padding);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder(null);
        this.mHolder.mContainer = new FrameLayout(context){

            protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
                super.onLayout(bl, n, n2, n3, n4);
                SettingEvSlider.this.updateIndicator(SettingEvSlider.this.getSelectedItemDisplyPosition());
            }
        };
        this.mHolder.mContainer.addView(layoutInflater.inflate(R.layout.setting_item_ev_slider, null));
        this.mHolder.mBackground = this.mHolder.mContainer.findViewById(R.id.background);
        this.mHolder.mIndicator = (ImageView)this.mHolder.mContainer.findViewById(R.id.indicator);
        this.mHolder.mScaleNumber = (ImageView)this.mHolder.mContainer.findViewById(R.id.scale_number);
        this.mHolder.mBackground.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener(){

            /*
             * Enabled aggressive block sorting
             */
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0: {
                        view.setPressed(true);
                        SettingEvSlider.this.updateIndicator(motionEvent.getY(), motionEvent.getAction());
                        return true;
                    }
                    case 2: {
                        if (!view.isPressed()) return false;
                        SettingEvSlider.this.updateIndicator(motionEvent.getY(), motionEvent.getAction());
                    }
                    default: {
                        return false;
                    }
                    case 1: 
                }
                if (view.isPressed()) {
                    SettingEvSlider.this.updateIndicator(motionEvent.getY(), motionEvent.getAction());
                }
                view.setPressed(false);
                return false;
            }
        });
    }

    private float getMemoryStepSize() {
        return (float)(this.mHolder.mBackground.getMeasuredHeight() - this.mTopPadding - this.mBottomPadding) / (float)(-1 + this.getItem().getChildren().size());
    }

    private int getSelectedItemDisplyPosition() {
        for (int i = 0; i < this.getValueItemCount(); ++i) {
            if (!this.getValueItem(i).isSelected()) continue;
            this.mSelectedPosition = i;
            return this.mSelectedPosition;
        }
        return 0;
    }

    private SettingItem getValueItem(int n) {
        return (SettingItem)this.getItem().getChildren().get(-1 + (super.getValueItemCount() - n));
    }

    private int getValueItemCount() {
        return this.getItem().getChildren().size();
    }

    private void updateIndicator(float f, int n) {
        super.updateIndicator(Math.min((int)Math.max((int)((int)((f - (float)this.mTopPadding) / super.getMemoryStepSize())), (int)0), (int)(-1 + super.getValueItemCount())));
    }

    private void updateIndicator(int n) {
        float f = super.getMemoryStepSize() * (float)n + (float)this.mTopPadding - (float)this.mHolder.mIndicator.getMeasuredHeight() / 2.0f;
        this.mHolder.mIndicator.setY(f);
        if (n != this.mSelectedPosition) {
            Iterator iterator = this.getItem().getChildren().iterator();
            while (iterator.hasNext()) {
                ((SettingItem)iterator.next()).setSelected(false);
            }
            super.getValueItem(n).select();
        }
        this.mSelectedPosition = n;
    }

    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }

    @Override
    public void setUiOrientation(int n) {
        if (n == 2) {
            this.mHolder.mBackground.setBackgroundResource(R.drawable.cam_ev_level_dialog_scale_land_icn);
            this.mHolder.mScaleNumber.setImageResource(R.drawable.cam_ev_level_dialog_scale_number_land_icn);
            this.mHolder.mIndicator.setImageResource(R.drawable.setting_ev_indicator_selector_land);
            return;
        }
        this.mHolder.mBackground.setBackgroundResource(R.drawable.cam_ev_level_dialog_scale_port_icn);
        this.mHolder.mScaleNumber.setImageResource(R.drawable.cam_ev_level_dialog_scale_number_port_icn);
        this.mHolder.mIndicator.setImageResource(R.drawable.setting_ev_indicator_selector_port);
    }

    @Override
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        super.updateIndicator(super.getSelectedItemDisplyPosition());
    }

    private static class ViewHolder {
        View mBackground;
        ViewGroup mContainer;
        ImageView mIndicator;
        ImageView mScaleNumber;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder( var1) {
        }
    }

}

