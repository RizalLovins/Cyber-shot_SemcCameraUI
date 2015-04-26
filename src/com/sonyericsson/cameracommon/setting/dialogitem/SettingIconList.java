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
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  java.lang.CharSequence
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import java.util.Iterator;
import java.util.List;

public class SettingIconList
extends SettingDialogItem {
    private static final String TAG = SettingIconList.class.getSimpleName();
    private final ViewHolder mHolder;

    public SettingIconList(Context context, SettingItem settingItem) {
        super(settingItem);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder(null);
        this.mHolder.mContainer = (ViewGroup)layoutInflater.inflate(R.layout.setting_item_icon_list, null);
        this.mHolder.mList = (LinearLayout)this.mHolder.mContainer.findViewById(R.id.list);
    }

    private ImageView createIcon(final SettingItem settingItem) {
        Context context = this.mHolder.mContainer.getContext();
        ImageView imageView = new ImageView(context);
        imageView.setTag((Object)settingItem);
        imageView.setSelected(settingItem.isSelected());
        imageView.setImageResource(settingItem.getIconId());
        imageView.setBackgroundResource(R.drawable.setting_item_icon_selector);
        imageView.setContentDescription((CharSequence)settingItem.getContentDescription(context.getResources()));
        imageView.setClickable(true);
        imageView.setOnClickListener((View.OnClickListener)new View.OnClickListener(){

            public void onClick(View view) {
                if (SettingIconList.this.getView().isShown()) {
                    SettingIconList.this.updateSelected(settingItem);
                }
            }
        });
        return imageView;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateSelected(SettingItem settingItem) {
        for (SettingItem settingItem2 : this.getItem().getChildren()) {
            if (settingItem2 == settingItem) continue;
            settingItem2.setSelected(false);
        }
        settingItem.select();
        for (int i = 0; i < this.mHolder.mList.getChildCount(); ++i) {
            View view = this.mHolder.mList.getChildAt(i);
            if (view.getTag() == settingItem) {
                view.setSelected(true);
                continue;
            }
            view.setSelected(false);
        }
    }

    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }

    @Override
    public void setUiOrientation(int n) {
        float f = RotationUtil.getAngle(n);
        for (int i = 0; i < this.mHolder.mList.getChildCount(); ++i) {
            this.mHolder.mList.getChildAt(i).setRotation(f);
        }
    }

    @Override
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        int n = this.mHolder.mList.getLayoutParams().height / Math.max((int)1, (int)this.getItem().getChildren().size());
        this.mHolder.mList.removeAllViews();
        Iterator iterator = this.getItem().getChildren().iterator();
        while (iterator.hasNext()) {
            ImageView imageView = super.createIcon((SettingItem)iterator.next());
            this.mHolder.mList.addView((View)imageView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)imageView.getLayoutParams();
            layoutParams.width = n;
            layoutParams.height = 0;
            layoutParams.weight = 1.0f;
        }
    }

    private static class ViewHolder {
        ViewGroup mContainer;
        LinearLayout mList;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder( var1) {
        }
    }

}

