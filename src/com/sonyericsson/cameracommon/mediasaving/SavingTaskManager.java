/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.sqlite.SQLiteFullException
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.Message
 *  com.sonymobile.imageprocessor.superresolution.SuperResolutionProcessor
 *  com.sonymobile.imageprocessor.superresolution.SuperResolutionProcessor$Parameters
 *  com.sonymobile.imageprocessor.superresolution.SuperResolutionProcessor$Results
 *  java.io.File
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.io.OutputStream
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.Runtime
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.Queue
 *  java.util.concurrent.ConcurrentLinkedQueue
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteFullException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.ThreadSafeOutputStream;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.VideoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonymobile.imageprocessor.superresolution.SuperResolutionProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingTaskManager {
    private static final int MSG_ON_STORE_CALLBACK = 1;
    private static final String TAG = SavingTaskManager.class.getSimpleName();
    protected Activity mActivity = null;
    private String mCurrentStorage;
    private ExecutorService mExecutor;
    private Object mExecutorLock = new Object();
    private final boolean mIsOneShotPhoto;
    private Queue<SavingTask> mSavingTaskQueue = new ConcurrentLinkedQueue();
    private CameraStorageManager mStorageManager = null;
    private StoreDataHandler mStoreDataHandler = null;
    private Thread mStoreVideoThread = null;
    private MediaProviderUpdator mUpdator;

    public SavingTaskManager(Activity activity, CameraStorageManager cameraStorageManager, boolean bl) {
        this.mActivity = activity;
        this.mStorageManager = cameraStorageManager;
        this.mCurrentStorage = cameraStorageManager.getCurrentStorage();
        DcfPathBuilder.getInstance().resetStatus();
        this.mStoreDataHandler = new StoreDataHandler(null);
        this.mUpdator = new MediaProviderUpdator((Context)activity, bl);
        this.mIsOneShotPhoto = bl;
    }

    static /* synthetic */ MediaProviderUpdator access$200(SavingTaskManager savingTaskManager) {
        return savingTaskManager.mUpdator;
    }

    static /* synthetic */ String access$500(SavingTaskManager savingTaskManager) {
        return savingTaskManager.mCurrentStorage;
    }

    static /* synthetic */ Thread access$802(SavingTaskManager savingTaskManager, Thread thread) {
        savingTaskManager.mStoreVideoThread = thread;
        return thread;
    }

    private void popPhotoSavingTask(SavingTask savingTask) {
        this.mSavingTaskQueue.remove((Object)savingTask);
    }

    private void pushPhotoSavingTask(byte[] arrby, PhotoSavingRequest photoSavingRequest) {
        this.request((SavingTask)new PhotoSavingTask(photoSavingRequest));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void shutdownExecutor() {
        Object object;
        Object object2 = object = this.mExecutorLock;
        synchronized (object2) {
            if (this.mExecutor != null) {
                this.mExecutor.shutdown();
                this.mExecutor = null;
                this.mSavingTaskQueue.clear();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void submitExecutor(SavingTask savingTask) {
        Object object;
        Object object2 = object = this.mExecutorLock;
        synchronized (object2) {
            if (this.mExecutor == null) {
                this.mExecutor = Executors.newSingleThreadExecutor();
            }
            this.mExecutor.execute((Runnable)savingTask);
            return;
        }
    }

    public boolean canPushStoreTask() {
        if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() < 7 * Runtime.getRuntime().maxMemory() / 10) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void cancelSavingVideoTask() {
        if (this.mStoreVideoThread == null) return;
        this.mStoreVideoThread.interrupt();
        try {
            this.mStoreVideoThread.join();
        }
        catch (InterruptedException interruptedException) {
            CameraLogger.e(TAG, "cancelSavingVideoTask interrupted.", (Throwable)interruptedException);
            return;
        }
        finally {
            this.mStoreVideoThread = null;
        }
        return;
    }

    public long getExpectedTotalSavedPicturesSize() {
        long l = 0;
        Iterator iterator = new LinkedList(this.mSavingTaskQueue).iterator();
        while (iterator.hasNext()) {
            l+=(long)((SavingTask)iterator.next()).getExpectedFileSize();
        }
        return l;
    }

    protected void notifyStoreComplete(StoreDataResult storeDataResult) {
        Message message = Message.obtain();
        message.what = 1;
        message.obj = storeDataResult;
        if (this.mStoreDataHandler != null) {
            this.mStoreDataHandler.sendMessage(message);
        }
    }

    public void onResume() {
        DcfPathBuilder.getInstance().resetStatus();
    }

    public void release() {
        this.mStoreDataHandler = null;
    }

    public void request(SavingTask savingTask) {
        this.mSavingTaskQueue.add((Object)savingTask);
        super.submitExecutor(savingTask);
    }

    public void storePicture(final PhotoSavingRequest photoSavingRequest) {
        photoSavingRequest.log();
        if (photoSavingRequest.getImageData() != null) {
            super.pushPhotoSavingTask(photoSavingRequest.getImageData(), photoSavingRequest);
            return;
        }
        CameraLogger.e(TAG, "### can't store a specified image file.");
        CameraLogger.e(TAG, "### so, notify a failure of storing the specified image file.");
        this.mActivity.runOnUiThread((Runnable)new Runnable(){

            public void run() {
                photoSavingRequest.notifyStoreFailed(MediaSavingResult.FAIL);
                SavingTaskManager.this.shutdownExecutor();
                SavingTaskManager.this.mStorageManager.updateRemain(0, true);
            }
        });
    }

    /*
     * Exception decompiling
     */
    public void storeVideo(VideoSavingRequest var1) {
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
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    public static final class GeoMode
    extends Enum<GeoMode> {
        private static final /* synthetic */ GeoMode[] $VALUES;
        public static final /* enum */ GeoMode GEO_OFF;
        public static final /* enum */ GeoMode GEO_ON;
        public static final /* enum */ GeoMode GEO_RESHOW;

        static {
            GEO_ON = new GeoMode();
            GEO_OFF = new GeoMode();
            GEO_RESHOW = new GeoMode();
            GeoMode[] arrgeoMode = new GeoMode[]{GEO_ON, GEO_OFF, GEO_RESHOW};
            $VALUES = arrgeoMode;
        }

        private GeoMode() {
            super(string, n);
        }

        public static GeoMode valueOf(String string) {
            return (GeoMode)Enum.valueOf((Class)GeoMode.class, (String)string);
        }

        public static GeoMode[] values() {
            return (GeoMode[])$VALUES.clone();
        }
    }

    class ImageToFile {
        private byte[] mJpegData;
        ThreadSafeOutputStream mOutputStream;
        private final String mPath;
        private final Uri mUri;
        final /* synthetic */ SavingTaskManager this$0;

        public ImageToFile(SavingTaskManager savingTaskManager, byte[] arrby, Uri uri) {
            this.this$0 = savingTaskManager;
            this.mOutputStream = null;
            this.mJpegData = arrby;
            this.mUri = uri;
            if ("file".equals((Object)uri.getScheme())) {
                this.mPath = this.mUri.getPath();
                return;
            }
            this.mPath = null;
        }

        public ImageToFile(SavingTaskManager savingTaskManager, byte[] arrby, String string) {
            this.this$0 = savingTaskManager;
            this.mOutputStream = null;
            this.mJpegData = arrby;
            this.mPath = string;
            this.mUri = null;
        }

        private OutputStream createOutputStream() throws FileNotFoundException {
            if (this.mPath != null) {
                return new FileOutputStream(this.mPath);
            }
            if (this.mUri != null) {
                return this.this$0.mActivity.getContentResolver().openOutputStream(this.mUri);
            }
            CameraLogger.e(TAG, "Save path and uri is not set.");
            return null;
        }

        public String getPath() {
            if (this.mPath != null) {
                return this.mPath;
            }
            if (this.mUri != null) {
                return this.mUri.getPath();
            }
            CameraLogger.e(TAG, "Save path and uri is not set.");
            return null;
        }

        /*
         * Exception decompiling
         */
        public boolean storeData(Exception var1) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [6[TRYBLOCK]], but top level block is 16[CATCHBLOCK]
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:392)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:444)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2783)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:764)
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

    /*
     * Failed to analyse overrides
     */
    public class PhotoSavingTask
    extends SavingTask {
        final PhotoSavingRequest mRequest;

        public PhotoSavingTask(PhotoSavingRequest photoSavingRequest) {
            super(photoSavingRequest);
            this.mRequest = photoSavingRequest;
        }

        protected int getExpectedFileSize() {
            if (this.getResult() != null) {
                return 0;
            }
            return this.mRequest.getImageData().length;
        }

        protected boolean preProccess() {
            SuperResolutionProcessor superResolutionProcessor;
            if (this.mRequest.common.doPostProcessing && (superResolutionProcessor = SuperResolutionProcessor.create()) != null) {
                SuperResolutionProcessor.Parameters parameters = new SuperResolutionProcessor.Parameters(this.mRequest.common.width, this.mRequest.common.height, 256);
                SuperResolutionProcessor.Results results = superResolutionProcessor.process(this.mRequest.getImageData(), parameters);
                this.mRequest.setImageData(results.imageBuffer);
                superResolutionProcessor.release();
            }
            return true;
        }

        protected void register(SavingTask.Result result) {
            if (result.result == MediaSavingResult.SUCCESS) {
                if (!SavingTaskManager.this.mIsOneShotPhoto) {
                    MediaProviderUpdator.sendBroadcastCameraShot((Context)SavingTaskManager.this.mActivity, result.uri, false);
                }
                SavingTaskManager.this.notifyStoreComplete(new StoreDataResult(result.result, result.uri, this.mRequest));
                return;
            }
            SavingTaskManager.this.notifyStoreComplete(new StoreDataResult(result.result, Uri.EMPTY, this.mRequest));
        }

        /*
         * Exception decompiling
         */
        protected SavingTask.Result store(Uri var1) {
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

    public static final class SavedFileType
    extends Enum<SavedFileType> {
        private static final /* synthetic */ SavedFileType[] $VALUES;
        public static final /* enum */ SavedFileType BURST;
        public static final /* enum */ SavedFileType PHOTO;
        public static final /* enum */ SavedFileType PHOTO_DURING_REC;
        public static final /* enum */ SavedFileType TIME_SHIFT;
        public static final /* enum */ SavedFileType VIDEO;

        static {
            PHOTO = new SavedFileType();
            PHOTO_DURING_REC = new SavedFileType();
            VIDEO = new SavedFileType();
            BURST = new SavedFileType();
            TIME_SHIFT = new SavedFileType();
            SavedFileType[] arrsavedFileType = new SavedFileType[]{PHOTO, PHOTO_DURING_REC, VIDEO, BURST, TIME_SHIFT};
            $VALUES = arrsavedFileType;
        }

        private SavedFileType() {
            super(string, n);
        }

        public static SavedFileType valueOf(String string) {
            return (SavedFileType)Enum.valueOf((Class)SavedFileType.class, (String)string);
        }

        public static SavedFileType[] values() {
            return (SavedFileType[])$VALUES.clone();
        }
    }

    /*
     * Failed to analyse overrides
     */
    public abstract class SavingTask
    implements Runnable {
        private final SavingRequest mRequest;
        private Result mResult;

        public SavingTask(SavingRequest savingRequest) {
            this.mRequest = savingRequest;
            this.mResult = null;
        }

        protected Uri assignOutput() {
            if (this.mRequest.getExtraOutput() != null) {
                return this.mRequest.getExtraOutput();
            }
            String string = DcfPathBuilder.getInstance().getPhotoPath((Context)SavingTaskManager.this.mActivity);
            if (string == null) {
                return null;
            }
            return Uri.fromFile((File)new File(string));
        }

        protected abstract int getExpectedFileSize();

        protected Result getResult() {
            return this.mResult;
        }

        protected final void notifyResult(MediaSavingResult mediaSavingResult, Uri uri) {
            SavingTaskManager.this.notifyStoreComplete(new StoreDataResult(mediaSavingResult, uri, this.mRequest));
        }

        protected abstract boolean preProccess();

        protected abstract void register(Result var1);

        /*
         * Enabled aggressive block sorting
         */
        public final void run() {
            this.mResult = new Result(MediaSavingResult.FAIL, Uri.EMPTY);
            if (this.preProccess()) {
                Uri uri = this.assignOutput();
                if (uri != null) {
                    this.mResult = this.store(uri);
                } else {
                    SavingTaskManager.this.mActivity.runOnUiThread((Runnable)new Runnable(){

                        public void run() {
                            SavingTaskManager.this.mStorageManager.updateRemain(0, true);
                        }
                    });
                }
            }
            this.register(this.mResult);
            SavingTaskManager.this.popPhotoSavingTask(this);
        }

        protected abstract Result store(Uri var1);

        protected boolean writeToStorage(byte[] arrby, Uri uri) {
            return new ImageToFile(SavingTaskManager.this, arrby, uri).storeData(null);
        }

        protected class Result {
            public final MediaSavingResult result;
            public final Uri uri;

            public Result(MediaSavingResult mediaSavingResult, Uri uri) {
                this.result = mediaSavingResult;
                this.uri = uri;
            }
        }

    }

    /*
     * Failed to analyse overrides
     */
    class SavingVideoTask
    implements Runnable {
        final VideoSavingRequest mRequest;

        SavingVideoTask(VideoSavingRequest videoSavingRequest) {
            this.mRequest = videoSavingRequest;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Lifted jumps to return sites
         */
        public void run() {
            var2_1 = null;
            try {
                var6_2 = this.mRequest.getExtraOutput();
                var2_1 = null;
                ** if (var6_2 == null) goto lbl-1000
lbl6: // 1 sources:
                var7_3 = this.mRequest.common.addToMediaStore;
                var2_1 = null;
                if (!var7_3) {
                    var2_1 = this.mRequest.getExtraOutput();
                } else lbl-1000: // 2 sources:
                {
                    var2_1 = SavingTaskManager.access$200(SavingTaskManager.this).insertVideoAndSendIntent(this.mRequest, "", true);
                }
                var4_4 = var2_1 != null ? MediaSavingResult.SUCCESS : MediaSavingResult.FAIL;
            }
            catch (SQLiteFullException var3_5) {
                var4_4 = MediaSavingResult.FAIL_MEMORY_FULL;
            }
            SavingTaskManager.this.notifyStoreComplete(new StoreDataResult(var4_4, var2_1, this.mRequest));
            SavingTaskManager.access$802(SavingTaskManager.this, null);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class StoreDataHandler
    extends Handler {
        private StoreDataHandler() {
        }

        /* synthetic */ StoreDataHandler( var1) {
        }

        /*
         * Enabled aggressive block sorting
         */
        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 1: {
                    StoreDataResult storeDataResult = (StoreDataResult)message.obj;
                    if (storeDataResult == null || storeDataResult.savingRequest == null) return;
                    storeDataResult.savingRequest.notifyStoreResult(storeDataResult);
                    return;
                }
            }
        }
    }

}

