/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.ListActivity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Comparator
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.autoupload;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.autoupload.AutoUploadPluginAdapter;
import com.sonyericsson.cameracommon.autoupload.AutoUploadPluginItem;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettingNotifyListener;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettings;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class AutoUploadPluginListActivity
extends ListActivity
implements AutoUploadSettingNotifyListener {
    private static final String ACTION_NAME = "com.sonymobile.camera.autoupload.PLUGIN_UPLOAD_SETTING";
    private static final String META_DATA_ICON = "com.sonymobile.camera.autoupload.icon";
    private static final String META_DATA_TITLE = "com.sonymobile.camera.autoupload.title";
    private static final String TAG = "AutoUploadPluginListActivity";
    private AutoUploadPluginAdapter mAutoUploadPluginAdapter;
    private List<AutoUploadPluginItem> mPlugins = new ArrayList();

    public void onAutoUploadSettingNotified(Context context, String string, AutoUploadSettings.AutoUploadSetting autoUploadSetting) {
        Iterator iterator = this.mPlugins.iterator();
        while (iterator.hasNext()) {
            AutoUploadPluginItem autoUploadPluginItem = (AutoUploadPluginItem)iterator.next();
            if (!autoUploadPluginItem.getPackageClassName().equals((Object)string)) continue;
            if (autoUploadSetting == AutoUploadSettings.AutoUploadSetting.ON) {
                autoUploadPluginItem.setChecked(true);
                continue;
            }
            if (autoUploadSetting == AutoUploadSettings.AutoUploadSetting.OFF) {
                autoUploadPluginItem.setChecked(false);
                continue;
            }
            iterator.remove();
        }
        this.mAutoUploadPluginAdapter.notifyDataSetChanged();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.auto_upload_plugin_list);
        Intent intent = new Intent("com.sonymobile.camera.autoupload.PLUGIN_UPLOAD_SETTING");
        List list = this.getPackageManager().queryIntentActivities(intent, 128);
        this.mPlugins.clear();
        Iterator iterator = list.iterator();
        do {
            AutoUploadPluginItem autoUploadPluginItem;
            if (!iterator.hasNext()) {
                this.mAutoUploadPluginAdapter = new AutoUploadPluginAdapter((Context)this, 0, this.mPlugins);
                this.mAutoUploadPluginAdapter.sort((Comparator)new Comparator<AutoUploadPluginItem>(){

                    public int compare(AutoUploadPluginItem autoUploadPluginItem, AutoUploadPluginItem autoUploadPluginItem2) {
                        return autoUploadPluginItem.getTitle().compareToIgnoreCase(autoUploadPluginItem2.getTitle());
                    }
                });
                this.setListAdapter((ListAdapter)this.mAutoUploadPluginAdapter);
                return;
            }
            ActivityInfo activityInfo = ((android.content.pm.ResolveInfo)iterator.next()).activityInfo;
            try {
                Resources resources = this.getPackageManager().getResourcesForApplication(activityInfo.packageName);
                Bundle bundle2 = activityInfo.metaData;
                autoUploadPluginItem = null;
                if (resources != null) {
                    autoUploadPluginItem = null;
                    if (bundle2 != null) {
                        AutoUploadPluginItem autoUploadPluginItem2;
                        autoUploadPluginItem = autoUploadPluginItem2 = new AutoUploadPluginItem(resources.getString(bundle2.getInt("com.sonymobile.camera.autoupload.title")), resources.getDrawable(bundle2.getInt("com.sonymobile.camera.autoupload.icon")), activityInfo.name);
                    }
                }
            }
            catch (PackageManager.NameNotFoundException var6_10) {
                var6_10.printStackTrace();
                autoUploadPluginItem = null;
            }
            this.mPlugins.add((Object)autoUploadPluginItem);
        } while (true);
    }

    protected void onListItemClick(ListView listView, View view, int n, long l) {
        super.onListItemClick(listView, view, n, l);
        AutoUploadPluginItem autoUploadPluginItem = (AutoUploadPluginItem)listView.getItemAtPosition(n);
        if (autoUploadPluginItem != null) {
            AutoUploadSettings.getInstance().startPluginActivity((Context)this, autoUploadPluginItem.getPackageClassName());
        }
    }

    protected void onPause() {
        super.onPause();
        AutoUploadSettings.getInstance().cancel((Context)this);
    }

    protected void onResume() {
        super.onResume();
        AutoUploadSettings.getInstance().request((Context)this, (AutoUploadSettingNotifyListener)this);
    }

}

