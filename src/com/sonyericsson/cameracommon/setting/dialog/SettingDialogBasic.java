/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.GridView
 *  android.widget.ListAdapter
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialog;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogBasicParams;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;

/*
 * Failed to analyse overrides
 */
public class SettingDialogBasic
extends SettingDialog {
    private static final String TAG = SettingDialogBasic.class.getSimpleName();
    private final int ITEM_DIVIDER_HEIGHT;
    private final int TITLE_HEIGHT;
    protected GridView mGridView;
    private SettingDialogBasicParams mParams;
    private View mSelectedView;

    public SettingDialogBasic(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TITLE_HEIGHT = super.getPixel(R.dimen.title_text_height) + super.getPixel(R.dimen.divider_height);
        this.ITEM_DIVIDER_HEIGHT = super.getPixel(R.dimen.divider_height);
    }

    private void convertRectInLandscape(Rect rect) {
        if (this.mOrientation == 1) {
            int n = (int)((float)rect.top + this.getX());
            int n2 = (int)((float)rect.left - this.getX());
            int n3 = rect.height();
            int n4 = rect.width();
            rect.set(n, n2, n + n3, n2 + n4);
        }
    }

    private int getItemNum() {
        return this.mGridView.getAdapter().getCount();
    }

    private int getPixel(int n) {
        return this.getResources().getDimensionPixelSize(n);
    }

    private int getSelectorPadding() {
        Rect rect = new Rect();
        if (this.mGridView.getSelector() != null) {
            this.mGridView.getSelector().getPadding(rect);
            return rect.bottom + rect.top;
        }
        return 0;
    }

    private int getTitleHeight() {
        if (this.findViewById(R.id.setting_title_layout).getVisibility() == 0) {
            return this.TITLE_HEIGHT;
        }
        return 0;
    }

    private boolean isVisibleTitle() {
        if (this.findViewById(R.id.setting_title_layout).getVisibility() == 0) {
            return true;
        }
        return false;
    }

    public int computeHeight(int n) {
        int n2 = super.getSelectorPadding();
        Context context = this.getContext();
        if (n == 2) {
            return (int)((float)super.getItemNum() / 2.0f * (float)this.mParams.getItemHeight(this.getContext()) + (float)super.getItemNum() / 2.0f * (float)this.ITEM_DIVIDER_HEIGHT + (float)super.getTitleHeight() + (float)(2 * this.mParams.getPadding(context)) + (float)n2);
        }
        return n2 + (super.getItemNum() * this.mParams.getItemHeight(context) + super.getItemNum() * this.ITEM_DIVIDER_HEIGHT + super.getTitleHeight() + 2 * this.mParams.getPadding(context));
    }

    public int computeMaxHeight(int n) {
        return super.getSelectorPadding() + (n * this.mParams.getItemHeight(this.getContext()) + 2 * this.mParams.getPadding(this.getContext()) + super.getTitleHeight() + n * this.ITEM_DIVIDER_HEIGHT);
    }

    public int computeWidth(int n) {
        if (n == 2) {
            return this.getResources().getDimensionPixelSize(R.dimen.setting_dialog_shortcut_2_column_width);
        }
        return this.getResources().getDimensionPixelSize(R.dimen.setting_dialog_shortcut_width);
    }

    public SettingAdapter getAdapter() {
        return (SettingAdapter)this.mGridView.getAdapter();
    }

    public int getNumRows(int n) {
        return (n - 2 * this.mParams.getPadding(this.getContext()) - super.getTitleHeight() + this.ITEM_DIVIDER_HEIGHT) / (this.mParams.getItemHeight(this.getContext()) + this.ITEM_DIVIDER_HEIGHT);
    }

    public boolean getSelectedItemRect(Rect rect) {
        if (this.mSelectedView != null && this.mSelectedView.getGlobalVisibleRect(rect)) {
            super.convertRectInLandscape(rect);
            return true;
        }
        return false;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mGridView = (GridView)this.findViewById(R.id.setting_gridview);
        int n = this.getPixel(R.dimen.setting_dialog_scroll_fading_edge_length);
        this.mGridView.setFadingEdgeLength(n);
        this.mGridView.setVerticalFadingEdgeEnabled(true);
        this.mGridView.setChoiceMode(1);
        this.mGridView.setOnItemClickListener((AdapterView.OnItemClickListener)new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                SettingDialogBasic.this.mSelectedView = view;
                ((SettingItem)((SettingAdapter)SettingDialogBasic.this.mGridView.getAdapter()).getItem(n)).select();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setAdapter(SettingAdapter settingAdapter) {
        if (this.mParams != null) {
            settingAdapter.setItemHeight(this.mParams.getItemHeight(this.getContext()));
        }
        boolean bl = !super.isVisibleTitle();
        settingAdapter.setRoundTopItemBackground(bl);
        this.mGridView.setAdapter((ListAdapter)settingAdapter);
        if (settingAdapter.getCount() > 0) {
            this.mGridView.setSelection(settingAdapter.getSelectedPosition());
        }
    }

    public void setNumColumns(int n) {
        this.mGridView.setNumColumns(n);
    }

    public void setSensorOrientation(int n) {
        this.requestLayout();
        super.setSensorOrientation(n);
        this.mGridView.performAccessibilityAction(64, null);
    }

    public void setSettingDialogParams(SettingDialogBasicParams settingDialogBasicParams) {
        Context context = this.getContext();
        this.mParams = settingDialogBasicParams;
        this.findViewById(R.id.background).setBackgroundResource(this.mParams.getBackgroundId());
        this.findViewById(R.id.container).setPadding(this.mParams.getPadding(context), this.mParams.getPadding(context), this.mParams.getPadding(context), this.mParams.getPadding(context));
        if (this.mGridView.getAdapter() instanceof SettingAdapter) {
            ((SettingAdapter)this.mGridView.getAdapter()).setItemHeight(this.mParams.getItemHeight(context));
        }
    }

    public void setTitle(int n) {
        TextView textView = (TextView)this.findViewById(R.id.setting_title);
        textView.setText(n);
        textView.setContentDescription(textView.getText());
        this.findViewById(R.id.setting_title_layout).setVisibility(0);
        SettingAdapter settingAdapter = (SettingAdapter)this.mGridView.getAdapter();
        if (settingAdapter != null) {
            settingAdapter.setRoundTopItemBackground(false);
        }
    }

}

