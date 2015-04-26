/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$PreviewCallback
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.List
 */
package com.sonyericsson.android.camera.device;

import android.hardware.Camera;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PreviewFrameRetriever {
    private static final String TAG = PreviewFrameRetriever.class.getSimpleName();
    private Camera mCamera = null;
    private PreviewInfo mInfo = null;
    private boolean mIsPreviewCallbackRegistered = false;
    private final List<Request> mRequests = new LinkedList();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Request findWithListener(OnPreviewFrameAvarableListener onPreviewFrameAvarableListener) {
        for (Request request : this.mRequests) {
            if (request.callback != onPreviewFrameAvarableListener) continue;
            return request;
        }
        return null;
    }

    private void registerPreviewCallback() {
        if (!(this.mIsPreviewCallbackRegistered || this.mCamera == null)) {
            this.mIsPreviewCallbackRegistered = true;
            this.mCamera.setPreviewCallback((Camera.PreviewCallback)new OnPreviewFrameCallback(this, null));
        }
    }

    private void unregisterPreviewCallback() {
        this.mIsPreviewCallbackRegistered = false;
        if (this.mCamera != null) {
            this.mCamera.setPreviewCallback(null);
        }
    }

    public void attachCamera(Camera camera) {
        this.attachCamera(camera, camera.getParameters());
    }

    public void attachCamera(Camera camera, Camera.Parameters parameters) {
        this.dettachCamera();
        this.mCamera = camera;
        this.mInfo = new PreviewInfo(parameters.getPreviewFormat(), parameters.getPreviewSize().width, parameters.getPreviewSize().height);
    }

    public void dettachCamera() {
        this.mRequests.clear();
        this.unregisterPreviewCallback();
        this.mCamera = null;
    }

    public PreviewInfo getPreviewInfo() {
        return this.mInfo;
    }

    public void removeRequest(OnPreviewFrameAvarableListener onPreviewFrameAvarableListener) {
        Request request = super.findWithListener(onPreviewFrameAvarableListener);
        if (request != null) {
            this.mRequests.remove((Object)request);
        }
        if (this.mRequests.isEmpty()) {
            super.unregisterPreviewCallback();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean request(OnPreviewFrameAvarableListener onPreviewFrameAvarableListener) {
        if (super.findWithListener(onPreviewFrameAvarableListener) != null) {
            return false;
        }
        boolean bl = false;
        if (bl) {
            return false;
        }
        this.mRequests.add((Object)new Request(false, onPreviewFrameAvarableListener));
        super.registerPreviewCallback();
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean requestPeriodic(OnPreviewFrameAvarableListener onPreviewFrameAvarableListener) {
        if (super.findWithListener(onPreviewFrameAvarableListener) != null) {
            return false;
        }
        boolean bl = false;
        if (bl) {
            return false;
        }
        this.mRequests.add((Object)new Request(true, onPreviewFrameAvarableListener));
        super.registerPreviewCallback();
        return true;
    }

    public void setPreviewInfo(PreviewInfo previewInfo) {
        if (this.mInfo.format != previewInfo.format || this.mInfo.width != previewInfo.width || this.mInfo.height != previewInfo.height) {
            this.mRequests.clear();
            this.mInfo = previewInfo;
        }
    }

    public static interface OnPreviewFrameAvarableListener {
        public void onPreviewFrameAvarable(PreviewFrameRetriever var1, byte[] var2);
    }

    /*
     * Failed to analyse overrides
     */
    private class OnPreviewFrameCallback
    implements Camera.PreviewCallback {
        final /* synthetic */ PreviewFrameRetriever this$0;

        private OnPreviewFrameCallback(PreviewFrameRetriever previewFrameRetriever) {
            this.this$0 = previewFrameRetriever;
        }

        /* synthetic */ OnPreviewFrameCallback(PreviewFrameRetriever previewFrameRetriever,  var2_2) {
            super(previewFrameRetriever);
        }

        public void onPreviewFrame(byte[] arrby, Camera camera) {
            ArrayList arrayList = new ArrayList((Collection)this.this$0.mRequests);
            Iterator iterator = this.this$0.mRequests.iterator();
            while (iterator.hasNext()) {
                if (((Request)iterator.next()).periodic) continue;
                iterator.remove();
            }
            Iterator iterator2 = arrayList.iterator();
            while (iterator2.hasNext()) {
                ((Request)iterator2.next()).callback.onPreviewFrameAvarable(this.this$0, arrby);
            }
            if (this.this$0.mRequests.isEmpty()) {
                this.this$0.unregisterPreviewCallback();
            }
        }
    }

    public static class PreviewInfo {
        public final int format;
        public final int height;
        public final int width;

        public PreviewInfo(int n, int n2, int n3) {
            this.format = n;
            this.width = n2;
            this.height = n3;
        }
    }

    private static class Request {
        final OnPreviewFrameAvarableListener callback;
        final boolean periodic;

        Request(boolean bl, OnPreviewFrameAvarableListener onPreviewFrameAvarableListener) {
            this.periodic = bl;
            this.callback = onPreviewFrameAvarableListener;
        }
    }

}

