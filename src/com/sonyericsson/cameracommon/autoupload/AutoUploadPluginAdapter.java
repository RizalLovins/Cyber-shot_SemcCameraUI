/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.ArrayAdapter
 *  android.widget.ImageView
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.cameracommon.autoupload;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.autoupload.AutoUploadPluginItem;
import java.util.List;

public class AutoUploadPluginAdapter
extends ArrayAdapter<AutoUploadPluginItem> {
    public AutoUploadPluginAdapter(Context context, int n, List<AutoUploadPluginItem> list) {
        super(context, n, list);
    }

    /*
     * Enabled aggressive block sorting
     */
    public View getView(int n, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        AutoUploadPluginItem autoUploadPluginItem = (AutoUploadPluginItem)this.getItem(n);
        if (view == null) {
            view = ((LayoutInflater)this.getContext().getSystemService("layout_inflater")).inflate(R.layout.auto_upload_plugin_list_item, null);
            viewHolder = new ViewHolder(null);
            viewHolder.icon = (ImageView)view.findViewById(R.id.auto_upload_plugin_list_item_icon);
            viewHolder.title = (TextView)view.findViewById(R.id.auto_upload_plugin_list_item_title);
            viewHolder.checkMark = (ImageView)view.findViewById(R.id.auto_upload_plugin_list_item_check_mark);
            view.setTag((Object)viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.icon.setImageDrawable(autoUploadPluginItem.getIcon());
        viewHolder.title.setText((CharSequence)autoUploadPluginItem.getTitle());
        ImageView imageView = viewHolder.checkMark;
        int n2 = autoUploadPluginItem.isChecked() ? R.drawable.cam_auto_upload_enable_icn : R.drawable.cam_auto_upload_disable_icn;
        imageView.setImageResource(n2);
        return view;
    }

    private static class ViewHolder {
        public ImageView checkMark;
        public ImageView icon;
        public TextView title;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder( var1) {
        }
    }

}

