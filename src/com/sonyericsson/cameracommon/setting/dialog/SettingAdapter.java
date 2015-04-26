/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.ArrayAdapter
 *  android.widget.GridView
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.cameracommon.setting.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItemBuilder;
import java.util.ArrayList;
import java.util.List;

public class SettingAdapter
extends ArrayAdapter<SettingItem> {
    public static final int INVALID_VALUE = -1;
    private static final String TAG = SettingAdapter.class.getSimpleName();
    private final SettingDialogItemFactory mDialogItemFactory;
    private int mItemHeight;
    private boolean mSetRoundBackgroundTop;

    public SettingAdapter(Context context) {
        super(context, (List<SettingItem>)new ArrayList(), new SettingDialogItemFactory());
    }

    public SettingAdapter(Context context, SettingDialogItemFactory settingDialogItemFactory) {
        super(context, (List<SettingItem>)new ArrayList(), settingDialogItemFactory);
    }

    public SettingAdapter(Context context, List<SettingItem> list, SettingDialogItemFactory settingDialogItemFactory) {
        super(context, 0, list);
        this.mDialogItemFactory = settingDialogItemFactory;
        this.mSetRoundBackgroundTop = false;
        this.mItemHeight = -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ItemLayoutParams generateItemLayoutParams(ViewGroup viewGroup, int n) {
        boolean bl = true;
        int n2 = 1;
        if (viewGroup instanceof GridView) {
            n2 = ((GridView)viewGroup).getNumColumns();
        }
        int n3 = -1 + this.getCount() / n2;
        int n4 = n / n2;
        int n5 = n2 - 1;
        int n6 = n % n2;
        int n7 = this.mItemHeight;
        boolean bl2 = this.mSetRoundBackgroundTop && n4 == 0 ? bl : false;
        boolean bl3 = n4 == n3 ? bl : false;
        boolean bl4 = n6 == 0 ? bl : false;
        if (n6 == n5) {
            return new ItemLayoutParams(n7, bl2, bl3, bl4, bl);
        }
        bl = false;
        return new ItemLayoutParams(n7, bl2, bl3, bl4, bl);
    }

    public int getItemViewType(int n) {
        Object object = this.getItem(n);
        if (object instanceof SettingItem) {
            return ((SettingItem)object).getDialogItemType();
        }
        return super.getItemViewType(n);
    }

    public SettingItem getSelected() {
        for (int i = 0; i < this.getCount(); ++i) {
            SettingItem settingItem = (SettingItem)this.getItem(i);
            if (!settingItem.isSelected()) continue;
            return settingItem;
        }
        return null;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < this.getCount(); ++i) {
            if (!((SettingItem)this.getItem(i)).isSelected()) continue;
            return i;
        }
        return 0;
    }

    public View getView(int n, View view, ViewGroup viewGroup) {
        SettingItem settingItem = (SettingItem)this.getItem(n);
        ItemLayoutParams itemLayoutParams = super.generateItemLayoutParams(viewGroup, n);
        if (view != null && view.getTag() instanceof SettingDialogItem) {
            SettingDialogItem settingDialogItem = (SettingDialogItem)view.getTag();
            if (settingDialogItem.getItem() != settingItem) {
                settingDialogItem.setItem(settingItem);
            }
            settingDialogItem.update(viewGroup, itemLayoutParams);
            return settingDialogItem.getView();
        }
        SettingDialogItem settingDialogItem = this.mDialogItemFactory.create(settingItem, viewGroup);
        settingDialogItem.update(viewGroup, itemLayoutParams);
        settingDialogItem.getView().setTag((Object)settingDialogItem);
        return settingDialogItem.getView();
    }

    public int getViewTypeCount() {
        return this.mDialogItemFactory.getDialogItemTypeCount();
    }

    /*
     * Enabled aggressive block sorting
     */
    public <T> void selectByData(T t) {
        SettingItem settingItem = SettingItemBuilder.build(t).commit();
        for (int i = 0; i < this.getCount(); ++i) {
            SettingItem settingItem2 = (SettingItem)this.getItem(i);
            if (!settingItem2.isSelectable()) continue;
            if (settingItem.compareData(settingItem2)) {
                settingItem2.setSelected(true);
                continue;
            }
            settingItem2.setSelected(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void selectByItem(SettingItem settingItem) {
        if (settingItem == null) {
            return;
        }
        for (int i = 0; i < this.getCount(); ++i) {
            SettingItem settingItem2 = (SettingItem)this.getItem(i);
            if (settingItem.compareData(settingItem2)) {
                settingItem2.setSelected(true);
                continue;
            }
            settingItem2.setSelected(false);
        }
    }

    public void setItemHeight(int n) {
        if (this.mItemHeight != n) {
            this.mItemHeight = n;
            this.notifyDataSetChanged();
        }
    }

    public void setRoundTopItemBackground(boolean bl) {
        if (this.mSetRoundBackgroundTop != bl) {
            this.mSetRoundBackgroundTop = bl;
            this.notifyDataSetChanged();
        }
    }

    public static class ItemLayoutParams {
        public final boolean bottom;
        public final int height;
        public final boolean left;
        public final boolean right;
        public final boolean top;

        public ItemLayoutParams(int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
            this.height = n;
            this.top = bl;
            this.bottom = bl2;
            this.left = bl3;
            this.right = bl4;
        }
    }

}

