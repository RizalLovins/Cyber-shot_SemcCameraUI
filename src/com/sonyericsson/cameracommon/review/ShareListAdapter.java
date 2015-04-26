/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ResolveInfo$DisplayNameComparator
 *  android.graphics.drawable.Drawable
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.BaseAdapter
 *  android.widget.ImageView
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.review;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class ShareListAdapter
extends BaseAdapter {
    private static final String TAG = ShareListAdapter.class.getSimpleName();
    Context mContext;
    private final LayoutInflater mInflater;
    List<ResolveInfo> mResolveInfoList;

    public ShareListAdapter(Context context, List<ResolveInfo> list) {
        int n;
        this.mContext = context;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService("layout_inflater");
        this.mResolveInfoList = list;
        if (this.mResolveInfoList != null && (n = this.mResolveInfoList.size()) > 1) {
            ResolveInfo resolveInfo = (ResolveInfo)this.mResolveInfoList.get(0);
            for (int i = 1; i < n; ++i) {
                ResolveInfo resolveInfo2 = (ResolveInfo)this.mResolveInfoList.get(i);
                if (resolveInfo.priority == resolveInfo2.priority && resolveInfo.isDefault == resolveInfo2.isDefault) continue;
                for (; i < n; --n) {
                    this.mResolveInfoList.remove(i);
                }
            }
            Collections.sort(this.mResolveInfoList, (Comparator)new ResolveInfo.DisplayNameComparator(this.mContext.getPackageManager()));
        }
    }

    private void bindView(View view, ResolveInfo resolveInfo) {
        TextView textView = (TextView)view.findViewById(R.id.text);
        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        PackageManager packageManager = this.mContext.getPackageManager();
        Object object = resolveInfo.loadLabel(packageManager);
        if (object == null) {
            object = resolveInfo.activityInfo.name;
        }
        textView.setText((CharSequence)object);
        imageView.setImageDrawable(resolveInfo.loadIcon(packageManager));
    }

    public int getCount() {
        if (this.mResolveInfoList != null) {
            return this.mResolveInfoList.size();
        }
        return 0;
    }

    public Object getItem(int n) {
        return this.mResolveInfoList.get(n);
    }

    public long getItemId(int n) {
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public View getView(int n, View view, ViewGroup viewGroup) {
        View view2 = view == null ? this.mInflater.inflate(R.layout.list_item, viewGroup, false) : view;
        super.bindView(view2, (ResolveInfo)this.mResolveInfoList.get(n));
        return view2;
    }
}

