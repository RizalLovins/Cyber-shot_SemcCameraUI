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
 *  android.widget.ImageView
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialogitem;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.DrawableStateChanger;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;

public class SettingButton
extends SettingDialogItem {
    private final ViewHolder mHolder;
    private final View.OnClickListener mOnClickListener;

    public SettingButton(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mOnClickListener = new View.OnClickListener(){

            public void onClick(View view) {
                if (SettingButton.this.getView().isShown()) {
                    SettingButton.this.select(SettingButton.this.getItem());
                }
            }
        };
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder(null);
        this.mHolder.mContainer = layoutInflater.inflate(R.layout.setting_item_button, null);
        this.mHolder.mBackground = this.mHolder.mContainer.findViewById(R.id.background);
        this.mHolder.mDivider = this.mHolder.mContainer.findViewById(R.id.setting_divider);
        this.mHolder.mCenterDividerRight = this.mHolder.mContainer.findViewById(R.id.setting_center_divider_right);
        this.mHolder.mCenterDividerLeft = this.mHolder.mContainer.findViewById(R.id.setting_center_divider_left);
        this.mHolder.mImage = (ImageView)this.mHolder.mContainer.findViewById(R.id.icon);
        this.mHolder.mText = (TextView)this.mHolder.mContainer.findViewById(R.id.text);
        this.mHolder.mBackground.setClickable(true);
    }

    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }

    @Override
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        Resources resources = this.mHolder.mContainer.getContext().getResources();
        this.mHolder.mText.setText((CharSequence)this.getItem().getText(resources));
        this.mHolder.mImage.setImageResource(this.getItem().getIconId());
        this.mHolder.mBackground.setClickable(true);
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(this.getItem().isSelected());
        this.mHolder.mBackground.getLayoutParams().width = -1;
        this.mHolder.mBackground.getLayoutParams().height = itemLayoutParams.height;
        this.mHolder.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(resources));
        this.changeDrawableState(itemLayoutParams).background(this.mHolder.mBackground).dividerHorizontal(this.mHolder.mDivider).dividerVertical(this.mHolder.mCenterDividerLeft, this.mHolder.mCenterDividerRight).apply();
    }

    private static class ViewHolder {
        View mBackground;
        View mCenterDividerLeft;
        View mCenterDividerRight;
        View mContainer;
        View mDivider;
        ImageView mImage;
        TextView mText;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder( var1) {
        }
    }

}

