/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.Iterator
 */
package com.sonyericsson.cameracommon.setting.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.BrandConfig;
import java.util.Arrays;
import java.util.Iterator;

/*
 * Failed to analyse overrides
 */
public class SettingTabs
extends LinearLayout {
    private static final String TAG = SettingTabs.class.getSimpleName();
    private OnTabSelectedListener mListener;
    private TabView mTabLeft;
    private TabView mTabMiddle;
    private TabView mTabRight;

    public SettingTabs(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void clearSelected() {
        Object[] arrobject = new TabView[]{this.mTabLeft, this.mTabMiddle, this.mTabRight};
        Iterator iterator = Arrays.asList((Object[])arrobject).iterator();
        while (iterator.hasNext()) {
            ((TabView)iterator.next()).mFrame.setSelected(false);
        }
    }

    public Tab getSelected() {
        Object[] arrobject = new TabView[]{this.mTabLeft, this.mTabMiddle, this.mTabRight};
        for (TabView tabView : Arrays.asList((Object[])arrobject)) {
            if (!tabView.mFrame.isSelected()) continue;
            return tabView.mTab;
        }
        return null;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTabLeft = new TabView(R.id.tab_left);
        this.mTabMiddle = new TabView(R.id.tab_middle);
        this.mTabRight = new TabView(R.id.tab_right);
        this.mListener = null;
        this.setVisibility(8);
    }

    public void setOnSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mListener = onTabSelectedListener;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSelected(Tab tab) {
        Object[] arrobject = new TabView[]{this.mTabLeft, this.mTabMiddle, this.mTabRight};
        Iterator iterator = Arrays.asList((Object[])arrobject).iterator();
        while (iterator.hasNext()) {
            TabView tabView = (TabView)iterator.next();
            FrameLayout frameLayout = tabView.mFrame;
            boolean bl = tabView.mTab == tab;
            frameLayout.setSelected(bl);
        }
        return;
    }

    public /* varargs */ void setTabs(Tab ... arrtab) {
        switch (arrtab.length) {
            default: {
                throw new IllegalArgumentException("this argument is not supported.");
            }
            case 0: {
                this.mTabLeft.clear();
                this.mTabMiddle.clear();
                this.mTabRight.clear();
                this.setVisibility(8);
                super.clearSelected();
                return;
            }
            case 1: {
                this.mTabLeft.clear();
                this.mTabMiddle.clear();
                this.mTabRight.clear();
                this.setVisibility(8);
                super.clearSelected();
                return;
            }
            case 2: {
                this.mTabLeft.set(arrtab[0]);
                this.mTabMiddle.clear();
                this.mTabRight.set(arrtab[1]);
                this.setVisibility(0);
                this.setSelected(arrtab[0]);
                return;
            }
            case 3: 
        }
        this.mTabLeft.set(arrtab[0]);
        this.mTabMiddle.set(arrtab[1]);
        this.mTabRight.set(arrtab[2]);
        this.setVisibility(0);
        this.setSelected(arrtab[0]);
    }

    public static interface OnTabSelectedListener {
        public void onTabSelected(Tab var1);
    }

    public static final class Tab
    extends Enum<Tab> {
        private static final /* synthetic */ Tab[] $VALUES;
        public static final /* enum */ Tab Common;
        public static final /* enum */ Tab Photo;
        public static final /* enum */ Tab Video;
        private final int descriptionId;
        private final int iconId;

        static {
            Photo = new Tab(R.drawable.setting_tab_photo, R.string.cam_strings_accessibility_photo_setting_txt);
            Video = new Tab(R.drawable.setting_tab_video, R.string.cam_strings_accessibility_video_setting_txt);
            Common = new Tab(R.drawable.setting_tab_common, R.string.cam_strings_accessibility_other_settings_txt);
            Tab[] arrtab = new Tab[]{Photo, Video, Common};
            $VALUES = arrtab;
        }

        private Tab(int n2, int n3) {
            super(string, n);
            this.iconId = n2;
            this.descriptionId = n3;
        }

        public static Tab valueOf(String string) {
            return (Tab)Enum.valueOf((Class)Tab.class, (String)string);
        }

        public static Tab[] values() {
            return (Tab[])$VALUES.clone();
        }

        public int getIconId() {
            if (this.equals((Object)Common) && BrandConfig.isVerizonBrand()) {
                return R.drawable.setting_tab_common_vzw;
            }
            return this.iconId;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class TabView
    implements View.OnClickListener {
        final FrameLayout mFrame;
        final ImageView mIcon;
        Tab mTab;

        TabView(int n) {
            this.mFrame = (FrameLayout)SettingTabs.this.findViewById(n);
            this.mIcon = (ImageView)this.mFrame.findViewById(R.id.icon);
            this.mFrame.setOnClickListener((View.OnClickListener)this);
            this.clear();
        }

        public void clear() {
            this.mTab = null;
            this.mFrame.setVisibility(8);
            this.mIcon.setImageDrawable(null);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void onClick(View view) {
            if (this.mTab == null) {
                return;
            }
            if (this.mFrame.isSelected()) return;
            SettingTabs.this.setSelected(this.mTab);
            if (SettingTabs.this.mListener == null) return;
            SettingTabs.this.mListener.onTabSelected(this.mTab);
        }

        public void set(Tab tab) {
            this.mTab = tab;
            this.mFrame.setVisibility(0);
            this.mFrame.setContentDescription((CharSequence)SettingTabs.this.getContext().getString(this.mTab.descriptionId));
            this.mIcon.setImageResource(this.mTab.getIconId());
        }
    }

}

