/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Environment
 *  java.io.File
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.Runtime
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.sonyericsson.cameracommon.utility;

import android.os.Environment;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurePerformance {
    private static final String FILE = "camera_perform.csv";
    private static final String TAG = "MeasurePerformance";
    public static final String TAG_DEVICE = "[DEVICE]";
    public static final String TAG_SEQ = "[SEQ]";
    public static final String TAG_SHOT = "[SHOT]";
    public static final String TAG_STARTUP = "[START UP]";
    public static final String TAG_SURFACE = "[SURFACE]";
    public static final String TAG_TASK = "[TASK]";
    private static List<MeasureResource> mResourceList;
    private static List<MeasureTime> mTimeList;
    private static Map<String, FpsStatistics> sFpsStatisticsMap;
    private static MeasurePerformance sInstance;
    private static boolean sMemoryFlag;
    private static boolean sTimerFlag;
    private long originalTime;

    static {
        sTimerFlag = false;
        sMemoryFlag = false;
        mTimeList = Collections.synchronizedList((List)new ArrayList());
        mResourceList = Collections.synchronizedList((List)new ArrayList());
        sInstance = new MeasurePerformance();
        sFpsStatisticsMap = new HashMap();
    }

    protected MeasurePerformance() {
        MeasurePerformance.init();
        this.originalTime = System.currentTimeMillis();
    }

    public static void debugShowPreviewFPS(String string) {
    }

    private static void init() {
        mTimeList.clear();
        mResourceList.clear();
    }

    private static final boolean isLastMeasuredTime(List<MeasureTime> list, int n) {
        MeasureTime measureTime = (MeasureTime)list.get(n);
        for (int i = n + 1; i < list.size(); ++i) {
            MeasureTime measureTime2 = (MeasureTime)list.get(i);
            if (measureTime2.kind != measureTime.kind || measureTime2.id != measureTime.id) continue;
            return false;
        }
        return true;
    }

    public static final void measureResource(int n) {
        if (!sMemoryFlag) {
            return;
        }
        MeasureResource measureResource = new MeasureResource(null);
        Runtime runtime = Runtime.getRuntime();
        long l = runtime.totalMemory();
        long l2 = runtime.freeMemory();
        measureResource.mTotalMemory = l;
        measureResource.mFreeMemory = l2;
        mResourceList.add((Object)measureResource);
    }

    public static final void measureResource(String string) {
        if (!sMemoryFlag) {
            return;
        }
        MeasureResource measureResource = new MeasureResource(null);
        Runtime runtime = Runtime.getRuntime();
        long l = runtime.totalMemory();
        long l2 = runtime.freeMemory();
        measureResource.mTag = string;
        measureResource.mTotalMemory = l;
        measureResource.mFreeMemory = l2;
        mResourceList.add((Object)measureResource);
    }

    private static final void measureTime(PerformanceIds performanceIds, MeasureKind measureKind, String string) {
        if (!sTimerFlag) {
            return;
        }
        long l = System.currentTimeMillis();
        MeasureTime measureTime = new MeasureTime(null);
        measureTime.id = performanceIds;
        measureTime.additionalInfo = string;
        measureTime.time = l;
        measureTime.kind = measureKind;
        mTimeList.add((Object)measureTime);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final void measureTime(PerformanceIds performanceIds, boolean bl) {
        MeasureKind measureKind = bl ? MeasureKind.MEASURE_START : MeasureKind.MEASURE_END;
        MeasurePerformance.measureTime(performanceIds, measureKind, "");
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final void measureTime(PerformanceIds performanceIds, boolean bl, String string) {
        MeasureKind measureKind = bl ? MeasureKind.MEASURE_START : MeasureKind.MEASURE_END;
        MeasurePerformance.measureTime(performanceIds, measureKind, string);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final void measureTimeOverwrite(PerformanceIds performanceIds, boolean bl) {
        MeasureKind measureKind = bl ? MeasureKind.MEASURE_START_OVERWRITE : MeasureKind.MEASURE_END;
        MeasurePerformance.measureTime(performanceIds, measureKind, "");
    }

    public static final void outResult() {
        if (!(sTimerFlag || sMemoryFlag)) {
            return;
        }
        ArrayList arrayList = new ArrayList(mTimeList);
        ArrayList arrayList2 = new ArrayList(mResourceList);
        String string = "";
        MeasurePerformance.init();
        if (sTimerFlag) {
            string = string + MeasurePerformance.setResultTime(arrayList);
        }
        if (sMemoryFlag) {
            string = string + MeasurePerformance.setResultResource(arrayList2);
        }
        MeasurePerformance.writeFile(string);
    }

    public static final void outResultDelay(int n) {
    }

    public static final void setMemoryFlag(boolean bl) {
        sMemoryFlag = bl;
    }

    private static String setResultResource(List<MeasureResource> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---Measure Resource Start---\n");
        stringBuilder.append("ID,Total,Used,free\n");
        for (int i = 0; list.size() > i; ++i) {
            String string = ((MeasureResource)list.get((int)i)).mTag;
            long l = ((MeasureResource)list.get((int)i)).mTotalMemory;
            long l2 = ((MeasureResource)list.get((int)i)).mFreeMemory;
            long l3 = l - l2;
            stringBuilder.append(string + "," + l + "," + l3 + "," + l2 + "\n");
        }
        stringBuilder.append("---Measure Resource End---\n");
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    private static String setResultTime(List<MeasureTime> list) {
        MeasureTime[] arrmeasureTime = new MeasureTime[PerformanceIds.values().length];
        MeasureAmountTime[] arrmeasureAmountTime = new MeasureAmountTime[PerformanceIds.values().length];
        for (int i = 0; i < PerformanceIds.values().length; ++i) {
            arrmeasureAmountTime[i] = new MeasureAmountTime(null);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---Measure Time Start---\n");
        stringBuilder.append("[PERFORMANCE]ID,Time[ms],Comment\n");
        for (int j = 0; j < list.size(); ++j) {
            MeasureTime measureTime = (MeasureTime)list.get(j);
            if (measureTime.kind == MeasureKind.MEASURE_START) {
                if (arrmeasureTime[measureTime.id.ordinal()] != null) continue;
                arrmeasureTime[measureTime.id.ordinal()] = measureTime;
                continue;
            }
            if (measureTime.kind == MeasureKind.MEASURE_START_OVERWRITE) {
                arrmeasureTime[measureTime.id.ordinal()] = measureTime;
                continue;
            }
            if (arrmeasureTime[measureTime.id.ordinal()] == null) continue;
            arrmeasureAmountTime[measureTime.id.ordinal()].id = measureTime.id;
            MeasureAmountTime measureAmountTime = arrmeasureAmountTime[measureTime.id.ordinal()];
            measureAmountTime.total+=measureTime.time - arrmeasureTime[measureTime.id.ordinal()].time;
            MeasureAmountTime measureAmountTime2 = arrmeasureAmountTime[measureTime.id.ordinal()];
            measureAmountTime2.count = 1 + measureAmountTime2.count;
            if (!MeasurePerformance.isLastMeasuredTime(list, j)) continue;
            stringBuilder.append("[PERFORMANCE]");
            stringBuilder.append(measureTime.id.tag);
            stringBuilder.append(measureTime.id.name());
            stringBuilder.append(", ");
            stringBuilder.append(Long.toString((long)(measureTime.time - arrmeasureTime[measureTime.id.ordinal()].time)));
            stringBuilder.append(", ");
            stringBuilder.append(measureTime.additionalInfo);
            stringBuilder.append(arrmeasureTime[measureTime.id.ordinal()].additionalInfo);
            stringBuilder.append("\n");
            arrmeasureTime[measureTime.id.ordinal()] = null;
        }
        stringBuilder.append("[PERFORMANCE]---Measure Time End---\n\n");
        stringBuilder.append("---Measure Time Dump Start---\n");
        stringBuilder.append("ID,Type(1:Start/2:End),SytemTime,RelativeTime,Comment\n");
        for (int k = 0; k < list.size(); ++k) {
            MeasureTime measureTime = (MeasureTime)list.get(k);
            stringBuilder.append(measureTime.id.name());
            stringBuilder.append(", ");
            stringBuilder.append((Object)measureTime.kind);
            stringBuilder.append(", ");
            stringBuilder.append(Long.toString((long)measureTime.time));
            stringBuilder.append(", ");
            stringBuilder.append(Long.toString((long)(measureTime.time - MeasurePerformance.sInstance.originalTime)));
            stringBuilder.append(", ");
            stringBuilder.append(measureTime.additionalInfo);
            stringBuilder.append("\n");
        }
        int n = 0;
        do {
            if (n >= PerformanceIds.values().length) {
                stringBuilder.append("---Measure Time Dump End---\n");
                return stringBuilder.toString();
            }
            MeasureAmountTime measureAmountTime = arrmeasureAmountTime[n];
            if (measureAmountTime.id != null) {
                stringBuilder.append("AmountTime: ");
                stringBuilder.append(measureAmountTime.id.name());
                stringBuilder.append(" total: ");
                stringBuilder.append(Long.toString((long)measureAmountTime.total));
                stringBuilder.append(", count: ");
                stringBuilder.append(Long.toString((long)measureAmountTime.count));
                stringBuilder.append(", avalage: ");
                stringBuilder.append(Long.toString((long)(measureAmountTime.total / (long)measureAmountTime.count)));
                stringBuilder.append("\n");
            }
            ++n;
        } while (true);
    }

    public static final void setTimerFlag(boolean bl) {
        sTimerFlag = bl;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private static void writeFile(String var0) {
        var1_1 = new File((Object)Environment.getExternalStorageDirectory() + "/" + "camera_perform.csv");
        if (!var1_1.exists()) {
            var1_1.createNewFile();
        }
        var2_2 = null;
        var3_3 = new OutputStreamWriter((OutputStream)new FileOutputStream((Object)Environment.getExternalStorageDirectory() + "/" + "camera_perform.csv"));
        var3_3.write(var0);
        if (var3_3 == null) return;
        try {
            var3_3.flush();
            var3_3.close();
            return;
        }
        catch (Exception var15_5) {
            CameraLogger.e("MeasurePerformance", "[MeasurePerformance::writeFile]:Error = " + (Object)var15_5);
            return;
        }
        catch (IOException var17_4) {
            CameraLogger.e("MeasurePerformance", "Create output file failed");
            return;
        }
        catch (FileNotFoundException var4_6) {}
        ** GOTO lbl-1000
        catch (IOException var11_10) {}
        ** GOTO lbl-1000
        catch (Throwable var5_16) {
            var2_2 = var3_3;
            ** GOTO lbl-1000
        }
        catch (IOException var11_12) {
            var2_2 = var3_3;
        }
lbl-1000: // 2 sources:
        {
            CameraLogger.e("MeasurePerformance", "[MeasurePerformance::writeFile]:Error = " + var11_11);
            if (var2_2 == null) return;
            {
                catch (Throwable var5_14) lbl-1000: // 2 sources:
                {
                    if (var2_2 == null) throw var5_15;
                    try {
                        var2_2.flush();
                        var2_2.close();
                    }
                    catch (Exception var6_17) {
                        CameraLogger.e("MeasurePerformance", "[MeasurePerformance::writeFile]:Error = " + (Object)var6_17);
                        throw var5_15;
                    }
                    throw var5_15;
                }
            }
            try {
                var2_2.flush();
                var2_2.close();
                return;
            }
            catch (Exception var13_13) {
                CameraLogger.e("MeasurePerformance", "[MeasurePerformance::writeFile]:Error = " + (Object)var13_13);
                return;
            }
            catch (FileNotFoundException var4_8) {
                var2_2 = var3_3;
            }
lbl-1000: // 2 sources:
            {
                CameraLogger.e("MeasurePerformance", "[MeasurePerformance::writeFile]:Error = " + var4_7);
                if (var2_2 == null) return;
                try {
                    var2_2.flush();
                    var2_2.close();
                    return;
                }
                catch (Exception var9_9) {
                    CameraLogger.e("MeasurePerformance", "[MeasurePerformance::writeFile]:Error = " + (Object)var9_9);
                    return;
                }
            }
        }
    }

    private static class FpsStatistics {
        private int mFrameCount = 0;
        private long mLastFpsTime = 0;
        private int mLastFrameCount = 0;

        private FpsStatistics() {
        }
    }

    private static class MeasureAmountTime {
        public int count;
        public PerformanceIds id;
        public long total;

        private MeasureAmountTime() {
        }

        /* synthetic */ MeasureAmountTime( var1) {
        }
    }

    static final class MeasureKind
    extends Enum<MeasureKind> {
        private static final /* synthetic */ MeasureKind[] $VALUES;
        public static final /* enum */ MeasureKind MEASURE_END;
        public static final /* enum */ MeasureKind MEASURE_START;
        public static final /* enum */ MeasureKind MEASURE_START_OVERWRITE;

        static {
            MEASURE_START = new MeasureKind();
            MEASURE_START_OVERWRITE = new MeasureKind();
            MEASURE_END = new MeasureKind();
            MeasureKind[] arrmeasureKind = new MeasureKind[]{MEASURE_START, MEASURE_START_OVERWRITE, MEASURE_END};
            $VALUES = arrmeasureKind;
        }

        private MeasureKind() {
            super(string, n);
        }

        public static MeasureKind valueOf(String string) {
            return (MeasureKind)Enum.valueOf((Class)MeasureKind.class, (String)string);
        }

        public static MeasureKind[] values() {
            return (MeasureKind[])$VALUES.clone();
        }
    }

    private static class MeasureResource {
        public long mFreeMemory;
        public String mTag;
        public long mTotalMemory;

        private MeasureResource() {
        }

        /* synthetic */ MeasureResource( var1) {
        }
    }

    private static class MeasureTime {
        public String additionalInfo;
        public PerformanceIds id;
        public MeasureKind kind;
        public long time;

        private MeasureTime() {
        }

        /* synthetic */ MeasureTime( var1) {
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class OutResultDelayTask
    implements Runnable {
        private OutResultDelayTask() {
        }

        public void run() {
            MeasurePerformance.outResult();
        }
    }

    public static final class PerformanceIds
    extends Enum<PerformanceIds> {
        private static final /* synthetic */ PerformanceIds[] $VALUES;
        public static final /* enum */ PerformanceIds CREATE_EFFECT_RENDERER_PACK;
        public static final /* enum */ PerformanceIds HANDLE_EVENT;
        public static final /* enum */ PerformanceIds INFLATE_VIEWS;
        public static final /* enum */ PerformanceIds LAUNCH;
        public static final /* enum */ PerformanceIds LAUNCH_TO_DISPATCH_DRAW;
        public static final /* enum */ PerformanceIds LAZY_INITIALIZATION_TASK;
        public static final /* enum */ PerformanceIds MSG_ON_STORE_CALLBACK_END;
        public static final /* enum */ PerformanceIds MSG_ON_STORE_CALLBACK_START;
        public static final /* enum */ PerformanceIds NOTIFY_STORE_COMPLETE;
        public static final /* enum */ PerformanceIds ON_CREATE;
        public static final /* enum */ PerformanceIds ON_DESTROY;
        public static final /* enum */ PerformanceIds ON_NEW_INTENT;
        public static final /* enum */ PerformanceIds ON_PAUSE;
        public static final /* enum */ PerformanceIds ON_RESTART;
        public static final /* enum */ PerformanceIds ON_RESUME;
        public static final /* enum */ PerformanceIds ON_RESUME_TO_SURFACE_CHANGED;
        public static final /* enum */ PerformanceIds ON_START;
        public static final /* enum */ PerformanceIds ON_STOP;
        public static final /* enum */ PerformanceIds OPEN_CAMERA_DEVICE_TASK;
        public static final /* enum */ PerformanceIds RECORDING_START;
        public static final /* enum */ PerformanceIds RECORDING_STOP;
        public static final /* enum */ PerformanceIds RESUME_TO_LAUNCH;
        public static final /* enum */ PerformanceIds SETTUP_RELATED_TO_SURFACE_SIZE;
        public static final /* enum */ PerformanceIds SETUP_CAMERA_DEVICE_TASK;
        public static final /* enum */ PerformanceIds SET_CONTENT_VIEWS;
        public static final /* enum */ PerformanceIds STARTUP_TIME;
        public static final /* enum */ PerformanceIds STORE_DATA_INTO_SD_CARD;
        public static final /* enum */ PerformanceIds STOT_TO_ON_PICT_TAKEN;
        public static final /* enum */ PerformanceIds STOT_TO_SHOT;
        public static final /* enum */ PerformanceIds SURFACE_CHANGED;
        public static final /* enum */ PerformanceIds SURFACE_CHANGED_TO_LAUNCH;
        public static final /* enum */ PerformanceIds SWITCH_CAMERA_DEVICE;
        public static final /* enum */ PerformanceIds UPDATE_REMAIN;
        String tag;

        static {
            ON_CREATE = new PerformanceIds("[SEQ][START UP]");
            ON_NEW_INTENT = new PerformanceIds("[START UP]");
            ON_START = new PerformanceIds("[START UP]");
            ON_RESTART = new PerformanceIds("[START UP]");
            ON_RESUME = new PerformanceIds("[SEQ][START UP]");
            ON_PAUSE = new PerformanceIds("[START UP]");
            ON_STOP = new PerformanceIds("[START UP]");
            ON_DESTROY = new PerformanceIds("[START UP]");
            NOTIFY_STORE_COMPLETE = new PerformanceIds("");
            MSG_ON_STORE_CALLBACK_START = new PerformanceIds("");
            MSG_ON_STORE_CALLBACK_END = new PerformanceIds("");
            HANDLE_EVENT = new PerformanceIds("");
            SET_CONTENT_VIEWS = new PerformanceIds("");
            INFLATE_VIEWS = new PerformanceIds("[TASK]");
            STARTUP_TIME = new PerformanceIds("[SEQ]");
            ON_RESUME_TO_SURFACE_CHANGED = new PerformanceIds("[SURFACE]");
            SURFACE_CHANGED = new PerformanceIds("[SURFACE]");
            SURFACE_CHANGED_TO_LAUNCH = new PerformanceIds("[SURFACE]");
            RESUME_TO_LAUNCH = new PerformanceIds("[SEQ]");
            LAUNCH = new PerformanceIds("[SEQ]");
            LAUNCH_TO_DISPATCH_DRAW = new PerformanceIds("[SEQ]");
            OPEN_CAMERA_DEVICE_TASK = new PerformanceIds("[TASK][DEVICE]");
            SWITCH_CAMERA_DEVICE = new PerformanceIds("[DEVICE]");
            SETUP_CAMERA_DEVICE_TASK = new PerformanceIds("[TASK]");
            CREATE_EFFECT_RENDERER_PACK = new PerformanceIds("[TASK]");
            LAZY_INITIALIZATION_TASK = new PerformanceIds("[TASK]");
            SETTUP_RELATED_TO_SURFACE_SIZE = new PerformanceIds("[TASK]");
            STOT_TO_SHOT = new PerformanceIds("[SHOT]");
            STOT_TO_ON_PICT_TAKEN = new PerformanceIds("[SHOT]");
            RECORDING_START = new PerformanceIds("[SHOT]");
            RECORDING_STOP = new PerformanceIds("[SHOT]");
            STORE_DATA_INTO_SD_CARD = new PerformanceIds("");
            UPDATE_REMAIN = new PerformanceIds("");
            PerformanceIds[] arrperformanceIds = new PerformanceIds[]{ON_CREATE, ON_NEW_INTENT, ON_START, ON_RESTART, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, NOTIFY_STORE_COMPLETE, MSG_ON_STORE_CALLBACK_START, MSG_ON_STORE_CALLBACK_END, HANDLE_EVENT, SET_CONTENT_VIEWS, INFLATE_VIEWS, STARTUP_TIME, ON_RESUME_TO_SURFACE_CHANGED, SURFACE_CHANGED, SURFACE_CHANGED_TO_LAUNCH, RESUME_TO_LAUNCH, LAUNCH, LAUNCH_TO_DISPATCH_DRAW, OPEN_CAMERA_DEVICE_TASK, SWITCH_CAMERA_DEVICE, SETUP_CAMERA_DEVICE_TASK, CREATE_EFFECT_RENDERER_PACK, LAZY_INITIALIZATION_TASK, SETTUP_RELATED_TO_SURFACE_SIZE, STOT_TO_SHOT, STOT_TO_ON_PICT_TAKEN, RECORDING_START, RECORDING_STOP, STORE_DATA_INTO_SD_CARD, UPDATE_REMAIN};
            $VALUES = arrperformanceIds;
        }

        private PerformanceIds(String string2) {
            super(string, n);
            this.tag = string2;
        }

        public static PerformanceIds valueOf(String string) {
            return (PerformanceIds)Enum.valueOf((Class)PerformanceIds.class, (String)string);
        }

        public static PerformanceIds[] values() {
            return (PerformanceIds[])$VALUES.clone();
        }
    }

}

