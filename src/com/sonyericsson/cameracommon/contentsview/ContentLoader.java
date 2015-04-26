/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.Message
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.List
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.LinkedBlockingDeque
 */
package com.sonyericsson.cameracommon.contentsview;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.sonyericsson.cameracommon.contentsview.DataLoader;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.contentsview.contents.ContentFactory;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

public class ContentLoader {
    private static final int MAX_LOCAL_CACHE_NUM = 100;
    public static final float PANORAMA_ASPECT_THRESHOLD = 1.8777778f;
    private static final String TAG = ContentLoader.class.getSimpleName();
    private final ContentCreationCallback mContentCallback;
    private Context mContext;
    private DataLoader.DataLoadCallback mDataCallback;
    private LinkedBlockingDeque<Future<Integer>> mDataLoaderQueue;
    private ExecutorService mExecutor;
    private DataLoaderHander mHandler;
    private LinkedList<Content.ContentInfo> mLocalCache;
    private SecurityLevel mSecurityLevel;

    public ContentLoader(Context context, SecurityLevel securityLevel, ContentCreationCallback contentCreationCallback, int n) {
        this.mDataCallback = new DataCallback((ContentLoader)this, null);
        this.mExecutor = Executors.newSingleThreadExecutor();
        this.mLocalCache = new LinkedList();
        this.mContext = context;
        this.mSecurityLevel = securityLevel;
        this.mDataLoaderQueue = new LinkedBlockingDeque(n);
        this.mContentCallback = contentCreationCallback;
        this.mHandler = new DataLoaderHander((ContentLoader)this, null);
    }

    private void addLocalCache(Content.ContentInfo contentInfo) {
        this.mLocalCache.addFirst((Object)contentInfo);
        if (this.mLocalCache.size() > 100) {
            this.mLocalCache.removeLast();
        }
    }

    private void clearLocalCache() {
        if (this.mLocalCache != null) {
            this.mLocalCache.clear();
        }
    }

    private LinkedList<Content.ContentInfo> getLocalCache() {
        return this.mLocalCache;
    }

    private void loadData(DataLoader dataLoader) {
        Future future;
        if (this.mDataLoaderQueue.remainingCapacity() <= 0 && (future = (Future)this.mDataLoaderQueue.pollFirst()) != null) {
            future.cancel(true);
        }
        Future future2 = this.mExecutor.submit((Callable)dataLoader);
        this.mDataLoaderQueue.addLast((Object)future2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void removeFuture(int n) {
        if (this.mDataLoaderQueue != null) {
            Iterator iterator = this.mDataLoaderQueue.iterator();
            while (iterator.hasNext()) {
                int n2;
                Future future = (Future)iterator.next();
                if (!future.isDone() || future.isCancelled()) continue;
                try {
                    int n3;
                    n2 = n3 = ((Integer)future.get()).intValue();
                }
                catch (ExecutionException var6_7) {
                    n2 = 0;
                }
                catch (InterruptedException var4_5) {
                    n2 = 0;
                }
                if (n2 != n) continue;
                iterator.remove();
            }
        }
    }

    public void pause() {
        if (this.mDataLoaderQueue != null) {
            Iterator iterator = this.mDataLoaderQueue.iterator();
            while (iterator.hasNext()) {
                ((Future)iterator.next()).cancel(false);
            }
            this.mDataLoaderQueue.clear();
        }
        this.clearLocalCache();
    }

    public void release() {
        this.clearLocalCache();
        this.mLocalCache = null;
        this.mDataCallback = null;
        this.mExecutor.shutdownNow();
        this.mExecutor = null;
        this.mDataLoaderQueue.clear();
        this.mDataLoaderQueue = null;
        this.mHandler.removeAllMessages();
    }

    public void reload(int n, List<String> list) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$contentsview$ContentLoader$SecurityLevel[this.mSecurityLevel.ordinal()]) {
            default: {
                for (int i = 0; i < n; ++i) {
                    super.loadData(new DataLoader(this.mContext, list, i, this.mDataCallback, false));
                }
                break;
            }
            case 1: {
                LinkedList<Content.ContentInfo> linkedList = super.getLocalCache();
                if (linkedList == null || linkedList.size() <= 0) break;
                Content content = ContentFactory.create((Content.ContentInfo)linkedList.getFirst());
                this.mContentCallback.onContentCreated(-1, content);
            }
        }
    }

    public void removeTopContent() {
        if (this.mLocalCache != null && this.mLocalCache.size() > 0) {
            this.mLocalCache.removeFirst();
        }
    }

    public void request(int n, Uri uri) {
        super.loadData(new DataLoader(n, uri, this.mContext, this.mDataCallback, true));
    }

    public void updateSecurityLevel(SecurityLevel securityLevel) {
        this.mSecurityLevel = securityLevel;
    }

    static interface ContentCreationCallback {
        public void onContentCreated(int var1, Content var2);
    }

    private class DataCallback
    implements DataLoader.DataLoadCallback {
        final /* synthetic */ ContentLoader this$0;

        private DataCallback(ContentLoader contentLoader) {
            this.this$0 = contentLoader;
        }

        /* synthetic */ DataCallback(ContentLoader contentLoader,  var2_2) {
            super(contentLoader);
        }

        @Override
        public void onDataLoaded(boolean bl, Content.ContentInfo contentInfo, int n, boolean bl2) {
            if (bl) {
                Content content = ContentFactory.create(contentInfo);
                if (bl2) {
                    this.this$0.addLocalCache(contentInfo);
                }
                this.this$0.mHandler.notifyContentCreated(n, content);
                return;
            }
            CameraLogger.w(TAG, "Loading data is failed.");
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class DataLoaderHander
    extends Handler {
        private static final int NOTIFY_CONTENT_CREATED = 1;
        final /* synthetic */ ContentLoader this$0;

        private DataLoaderHander(ContentLoader contentLoader) {
            this.this$0 = contentLoader;
        }

        /* synthetic */ DataLoaderHander(ContentLoader contentLoader,  var2_2) {
            super(contentLoader);
        }

        private void notifyContentCreated(int n, Content content) {
            Message message = Message.obtain((Handler)this, (int)1);
            message.arg1 = n;
            message.obj = content;
            this.sendMessage(message);
        }

        private void removeAllMessages() {
            this.removeMessages(1);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 1: 
            }
            int n = message.arg1;
            Content content = (Content)message.obj;
            this.this$0.removeFuture(content.getContentInfo().mId);
            this.this$0.mContentCallback.onContentCreated(n, content);
        }
    }

    public static final class SecurityLevel
    extends Enum<SecurityLevel> {
        private static final /* synthetic */ SecurityLevel[] $VALUES;
        public static final /* enum */ SecurityLevel NEWLY_ADDED_CONTENT_ONLY;
        public static final /* enum */ SecurityLevel NORMAL;

        static {
            NORMAL = new SecurityLevel();
            NEWLY_ADDED_CONTENT_ONLY = new SecurityLevel();
            SecurityLevel[] arrsecurityLevel = new SecurityLevel[]{NORMAL, NEWLY_ADDED_CONTENT_ONLY};
            $VALUES = arrsecurityLevel;
        }

        private SecurityLevel() {
            super(string, n);
        }

        public static SecurityLevel valueOf(String string) {
            return (SecurityLevel)Enum.valueOf((Class)SecurityLevel.class, (String)string);
        }

        public static SecurityLevel[] values() {
            return (SecurityLevel[])$VALUES.clone();
        }
    }

}

