/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.media.MediaPlayer
 *  android.media.MediaPlayer$OnCompletionListener
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.Timer
 *  java.util.TimerTask
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 */
package com.sonyericsson.android.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.googleanalytics.parameters.CustomDimension;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Failed to analyse overrides
 */
public class CameraButtonIntentReceiver
extends BroadcastReceiver {
    private static final int CAMERA_DEVICE_AUTO_RELEASE_TIMER_DURATION = 5000;
    private static final String NORMAL_LAUNCH_FAST_CAPTURE_START_SUBJECT = "start";
    private static final String TAG = CameraButtonIntentReceiver.class.getSimpleName();
    private static boolean mIsStartUpNotificationRequired;
    private static MediaPlayer mStartUpNotifier;
    private static final Object mStartUpNotifierLock;
    private static ExecutorService mStartUpNotifyerService;
    private static ReceiverState sCurrentState;
    private static IntentKind sLatestIntent;
    private static Timer sReleaseTimer;
    private static final Object sReleaseTimerLock;

    static {
        sReleaseTimer = null;
        sReleaseTimerLock = new Object();
        sCurrentState = ReceiverState.IDLE;
        sLatestIntent = IntentKind.NULL;
        mIsStartUpNotificationRequired = false;
        mStartUpNotifyerService = Executors.newSingleThreadExecutor();
        mStartUpNotifier = null;
        mStartUpNotifierLock = new Object();
    }

    private static void changeStateTo(ReceiverState receiverState) {
        sCurrentState = receiverState;
    }

    public static void enableStartUpNotificationFlag() {
        mIsStartUpNotificationRequired = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static void killStartUpNotifierImmediately() {
        Object object;
        Object object2 = object = mStartUpNotifierLock;
        synchronized (object2) {
            if (mStartUpNotifier != null) {
                mStartUpNotifier.stop();
                mStartUpNotifier.release();
                mStartUpNotifier = null;
            }
            return;
        }
    }

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void notifyGoogleAnalytics(Context var1_1, IntentKind var2_2) {
        GoogleAnalyticsUtil.setContext(var1_1);
        var3_3 = .$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$IntentKind[var2_2.ordinal()];
        var4_4 = null;
        switch (var3_3) {
            case 1: {
                var4_4 = CustomDimension.LaunchedFrom.HW_CAMERA_KEY;
                ** break;
            }
            case 2: {
                var4_4 = CustomDimension.LaunchedFrom.HW_CAMERA_KEY_LOCK;
            }
lbl10: // 3 sources:
            default: {
                ** GOTO lbl14
            }
            case 3: 
            case 4: 
        }
        var4_4 = CustomDimension.LaunchedFrom.LOCK_SCREEN;
lbl14: // 2 sources:
        if (var4_4 == null) return;
        GoogleAnalyticsUtil.setCustomDimensionLaunchedFrom(var4_4);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void notifyStartUpIfRequired(Context context) {
        if (!mIsStartUpNotificationRequired) return;
        mIsStartUpNotificationRequired = false;
        if (mStartUpNotifier != null) {
            return;
        }
        StartUpNotifierTask startUpNotifierTask = new StartUpNotifierTask(context);
        mStartUpNotifyerService.execute((Runnable)startUpNotifierTask);
    }

    private void onActivityPausedReceived(Context context, boolean bl) {
        if (bl) {
            CameraButtonIntentReceiver.releaseStartUpNotificationFlag();
        }
    }

    private void onActivityResumedReceived(Context context) {
    }

    private void onCancelReceived(Context context) {
        FastCapturingActivity.staticCameraDevice().releaseCameraInstance();
        CameraButtonIntentReceiver.releaseCameraDeviceReleaseTimer();
    }

    private void onDirectStartReceived(Context context, String string) {
        FastCapturingActivity.staticCameraDevice().startCameraOpen(context, 0, null, false);
        FastCapturingActivity.requestReloadFastCaptureSetting();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(context, (Class)FastCapturingActivity.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", string);
        context.startActivity(intent);
        mIsStartUpNotificationRequired = true;
    }

    private void onNullReceived(Context context) {
        FastCapturingActivity.requestReloadFastCaptureSetting();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(context, (Class)FastCapturingActivity.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", "start");
        context.startActivity(intent);
        mIsStartUpNotificationRequired = true;
    }

    private void onPrepareReceived(Context context) {
        FastCapturingActivity.staticCameraDevice().startCameraOpen(context, 0, null, false);
        CameraButtonIntentReceiver.startCameraDeviceReleaseTimer();
    }

    private void onStartReceived(Context context, String string) {
        FastCapturingActivity.staticCameraDevice().startCameraOpen(context, 0, null, false);
        FastCapturingActivity.requestReloadFastCaptureSetting();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(context, (Class)FastCapturingActivity.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", string);
        context.startActivity(intent);
        mIsStartUpNotificationRequired = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private static void releaseCameraDeviceReleaseTimer() {
        reference var4 = CameraButtonIntentReceiver.class;
        synchronized (CameraButtonIntentReceiver.class) {
            Object object;
            Object object2 = object = sReleaseTimerLock;
            synchronized (object2) {
                if (sReleaseTimer != null) {
                    sReleaseTimer.cancel();
                    sReleaseTimer.purge();
                    sReleaseTimer = null;
                }
            }
            // ** MonitorExit[var4] (shouldn't be in output)
            return;
        }
    }

    public static void releaseStartUpNotificationFlag() {
        mIsStartUpNotificationRequired = false;
    }

    private static void setLatestIntent(IntentKind intentKind) {
        sLatestIntent = intentKind;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private static void startCameraDeviceReleaseTimer() {
        reference var3 = CameraButtonIntentReceiver.class;
        synchronized (CameraButtonIntentReceiver.class) {
            Object object;
            Object object2 = object = sReleaseTimerLock;
            synchronized (object2) {
                if (sReleaseTimer == null) {
                    sReleaseTimer = new Timer(true);
                    sReleaseTimer.schedule((TimerTask)new CameraDeviceReleaseTimerTask(), 5000);
                }
            }
            // ** MonitorExit[var3] (shouldn't be in output)
            return;
        }
    }

    /*
     * Exception decompiling
     */
    public final void onReceive(Context var1, Intent var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:141)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:380)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:62)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:400)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Failed to analyse overrides
     */
    static class CameraDeviceReleaseTimerTask
    extends TimerTask {
        CameraDeviceReleaseTimerTask() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void run() {
            Object object;
            Object object2 = object = sReleaseTimerLock;
            synchronized (object2) {
                if (sReleaseTimer != null) {
                    sReleaseTimer.cancel();
                    sReleaseTimer.purge();
                    sReleaseTimer = null;
                }
            }
            sCurrentState = ReceiverState.IDLE;
            FastCapturingActivity.staticCameraDevice().releaseCameraInstance();
        }
    }

    private static final class IntentKind
    extends Enum<IntentKind> {
        private static final /* synthetic */ IntentKind[] $VALUES;
        public static final /* enum */ IntentKind ACTIVITY_PAUSED;
        public static final /* enum */ IntentKind ACTIVITY_RESUMED;
        public static final /* enum */ IntentKind CANCEL;
        public static final /* enum */ IntentKind NULL;
        public static final /* enum */ IntentKind PREPARE;
        public static final /* enum */ IntentKind START;
        public static final /* enum */ IntentKind START_SECURE;

        static {
            NULL = new IntentKind();
            PREPARE = new IntentKind();
            START = new IntentKind();
            START_SECURE = new IntentKind();
            CANCEL = new IntentKind();
            ACTIVITY_RESUMED = new IntentKind();
            ACTIVITY_PAUSED = new IntentKind();
            IntentKind[] arrintentKind = new IntentKind[]{NULL, PREPARE, START, START_SECURE, CANCEL, ACTIVITY_RESUMED, ACTIVITY_PAUSED};
            $VALUES = arrintentKind;
        }

        private IntentKind() {
            super(string, n);
        }

        public static IntentKind valueOf(String string) {
            return (IntentKind)Enum.valueOf((Class)IntentKind.class, (String)string);
        }

        public static IntentKind[] values() {
            return (IntentKind[])$VALUES.clone();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static final class OnStartUpNotificationCompletedTask
    implements MediaPlayer.OnCompletionListener {
        private OnStartUpNotificationCompletedTask() {
        }

        /* synthetic */ OnStartUpNotificationCompletedTask( var1) {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            ReleaseStartUpNotifierTask releaseStartUpNotifierTask = new ReleaseStartUpNotifierTask(null);
            mStartUpNotifyerService.execute((Runnable)releaseStartUpNotifierTask);
        }
    }

    private static final class ReceiverState
    extends Enum<ReceiverState> {
        private static final /* synthetic */ ReceiverState[] $VALUES;
        public static final /* enum */ ReceiverState ACTIVE;
        public static final /* enum */ ReceiverState IDLE;
        public static final /* enum */ ReceiverState PREPARE;

        static {
            IDLE = new ReceiverState();
            PREPARE = new ReceiverState();
            ACTIVE = new ReceiverState();
            ReceiverState[] arrreceiverState = new ReceiverState[]{IDLE, PREPARE, ACTIVE};
            $VALUES = arrreceiverState;
        }

        private ReceiverState() {
            super(string, n);
        }

        public static ReceiverState valueOf(String string) {
            return (ReceiverState)Enum.valueOf((Class)ReceiverState.class, (String)string);
        }

        public static ReceiverState[] values() {
            return (ReceiverState[])$VALUES.clone();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static final class ReleaseStartUpNotifierTask
    implements Runnable {
        private ReleaseStartUpNotifierTask() {
        }

        /* synthetic */ ReleaseStartUpNotifierTask( var1) {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void run() {
            Object object;
            Object object2 = object = mStartUpNotifierLock;
            synchronized (object2) {
                if (mStartUpNotifier != null) {
                    mStartUpNotifier.release();
                    mStartUpNotifier = null;
                }
                return;
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static final class StartUpNotifierTask
    implements Runnable {
        private final Context mContext;

        public StartUpNotifierTask(Context context) {
            this.mContext = context;
        }

        /*
         * Exception decompiling
         */
        public void run() {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // java.util.ConcurrentModificationException
            // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
            // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
            // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:631)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:714)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
            // org.benf.cfr.reader.Main.doJar(Main.java:109)
            // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
            // java.lang.Thread.run(Thread.java:818)
            throw new IllegalStateException("Decompilation failed");
        }
    }

}

