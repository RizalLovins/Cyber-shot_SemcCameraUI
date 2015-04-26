/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.cameracommon.setting.dialogitem;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.DrawableStateChanger;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.widget.Switch;
import java.util.List;

public class SettingCategorySwitch
extends SettingDialogItem {
    private final ViewHolder mHolder;
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public SettingCategorySwitch(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                if (SettingCategorySwitch.this.getView().isShown()) {
                    if (bl) {
                        SettingCategorySwitch.this.select(SettingCategorySwitch.this.getOnItem());
                    }
                } else {
                    return;
                }
                SettingCategorySwitch.this.select(SettingCategorySwitch.this.getOffItem());
            }
        };
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = layoutInflater.inflate(R.layout.setting_item_category_switch, null);
        this.mHolder.mDivider = this.mHolder.mContainer.findViewById(R.id.setting_divider);
        this.mHolder.mSwitch = (Switch)this.mHolder.mContainer.findViewById(R.id.switch_layout);
    }

    private SettingItem getOffItem() {
        return (SettingItem)this.getItem().getChildren().get(1);
    }

    private SettingItem getOnItem() {
        return (SettingItem)this.getItem().getChildren().get(0);
    }

    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }

    @Override
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        Resources resources = this.mHolder.mContainer.getContext().getResources();
        this.mHolder.mSwitch.setText((CharSequence)this.getItem().getText(resources));
        this.mHolder.mContainer.setClickable(true);
        this.mHolder.mSwitch.setChecked(super.getOnItem().isSelected());
        this.mHolder.mSwitch.setEnabled(this.getItem().isSelectable());
        this.mHolder.mContainer.setContentDescription((CharSequence)this.getItem().getContentDescription(resources));
        this.mHolder.mSwitch.setOnCheckedChangeListener(null);
        this.mHolder.mSwitch.setChecked(super.getOnItem().isSelected());
        this.mHolder.mSwitch.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        this.changeDrawableState(itemLayoutParams).dividerHorizontal(this.mHolder.mDivider).apply();
    }

    static class ViewHolder {
        View mContainer;
        View mDivider;
        Switch mSwitch;

        ViewHolder() {
        }
    }

}

