/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.Callable
 */
package com.sonyericsson.cameracommon.viewfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class InflateTask
implements Callable<Map<InflateItem, List<View>>> {
    private static final String TAG = InflateTask.class.getSimpleName();
    private final List<InflateItem> mInflateItemList;
    private Map<InflateItem, List<View>> mInflatedItemMap;
    private LayoutInflater mLayoutInflater;

    public InflateTask(LayoutInflater layoutInflater, List<InflateItem> list) {
        this.mLayoutInflater = layoutInflater;
        this.mInflateItemList = list;
        this.mInflatedItemMap = new HashMap();
    }

    private void register(InflateItem inflateItem) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < inflateItem.getViewCount(); ++i) {
            arrayList.add((Object)this.mLayoutInflater.inflate(inflateItem.getLayoutId(), null));
        }
        this.mInflatedItemMap.put((Object)inflateItem, (Object)arrayList);
    }

    public Map<InflateItem, List<View>> call() {
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.INFLATE_VIEWS, true);
        Iterator iterator = this.mInflateItemList.iterator();
        while (iterator.hasNext()) {
            this.register((InflateItem)iterator.next());
        }
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.INFLATE_VIEWS, false);
        return this.mInflatedItemMap;
    }
}

