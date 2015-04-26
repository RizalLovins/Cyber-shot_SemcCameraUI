/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.TextView
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
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.DrawableStateChanger;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;
import java.util.List;

public class SettingCategoryButton
extends SettingDialogItem {
    private final ViewHolder mHolder;
    private final View.OnClickListener mOnClickListener;

    public SettingCategoryButton(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mOnClickListener = new View.OnClickListener(){

            public void onClick(View view) {
                if (SettingCategoryButton.this.getView().isShown() && SettingCategoryButton.this.getItem().isSelectable()) {
                    SettingCategoryButton.this.select(SettingCategoryButton.this.getItem());
                }
            }
        };
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder(null);
        this.mHolder.mContainer = layoutInflater.inflate(R.layout.setting_item_category_button, null);
        this.mHolder.mBackground = this.mHolder.mContainer.findViewById(R.id.background);
        this.mHolder.mDivider = this.mHolder.mContainer.findViewById(R.id.setting_divider);
        this.mHolder.mCategory = (TextView)this.mHolder.mContainer.findViewById(R.id.category);
        this.mHolder.mValue = (TextView)this.mHolder.mContainer.findViewById(R.id.value);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private SettingItem getSelectedItem() {
        for (SettingItem settingItem : this.getItem().getChildren()) {
            if (!settingItem.isSelectable() || !settingItem.isSelected()) continue;
            return settingItem;
        }
        return null;
    }

    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        Resources resources = this.mHolder.mContainer.getContext().getResources();
        this.mHolder.mCategory.setText((CharSequence)this.getItem().getText(resources));
        SettingItem settingItem = super.getSelectedItem();
        if (settingItem != null) {
            if (settingItem.getLongText(resources) != null && settingItem.getLongText(resources).length() > 0) {
                this.mHolder.mValue.setText((CharSequence)settingItem.getLongText(resources));
            } else {
                this.mHolder.mValue.setText((CharSequence)settingItem.getText(resources));
            }
            this.mHolder.mValue.setVisibility(0);
        } else {
            SettingItem settingItem2 = this.getItem();
            if (settingItem2 instanceof TypedSettingItem) {
                String string = settingItem2.getLongText(resources) != null && settingItem2.getLongText(resources).length() > 0 ? settingItem2.getLongText(resources) : ((TypedSettingItem)settingItem2).getValueText();
                if (string != null && string.length() > 0) {
                    this.mHolder.mValue.setVisibility(0);
                } else {
                    this.mHolder.mValue.setVisibility(8);
                }
            } else {
                this.mHolder.mValue.setVisibility(8);
            }
        }
        this.mHolder.mBackground.setClickable(true);
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        int n = this.getItem().isSelectable() ? viewGroup.getResources().getColor(R.color.default_text_col) : viewGroup.getResources().getColor(R.color.grayout_text_col);
        this.mHolder.mCategory.setTextColor(n);
        this.mHolder.mValue.setTextColor(n);
        this.mHolder.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(resources));
        this.changeDrawableState(itemLayoutParams).background(this.mHolder.mBackground).dividerHorizontal(this.mHolder.mDivider).apply();
    }

    private static class ViewHolder {
        View mBackground;
        TextView mCategory;
        View mContainer;
        View mDivider;
        TextView mValue;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder( var1) {
        }
    }

}

