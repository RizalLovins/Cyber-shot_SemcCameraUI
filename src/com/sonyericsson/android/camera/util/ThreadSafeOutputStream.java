/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Math
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util;

import java.io.IOException;
import java.io.OutputStream;

/*
 * Failed to analyse overrides
 */
public class ThreadSafeOutputStream
extends OutputStream {
    private static final String TAG = "ThreadSafeOutputStream";
    boolean mClosed;
    private OutputStream mDelegateStream;

    public ThreadSafeOutputStream(OutputStream outputStream) {
        this.mDelegateStream = outputStream;
        if (this.mDelegateStream != null) {
            // empty if block
        }
    }

    public void close() throws IOException {
        ThreadSafeOutputStream threadSafeOutputStream = this;
        synchronized (threadSafeOutputStream) {
            this.mClosed = true;
            this.mDelegateStream.close();
            return;
        }
    }

    public void flush() throws IOException {
        ThreadSafeOutputStream threadSafeOutputStream = this;
        synchronized (threadSafeOutputStream) {
            super.flush();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void write(int n) throws IOException {
        void var4_2 = this;
        synchronized (var4_2) {
            boolean bl = this.mClosed;
            if (!bl) {
                this.mDelegateStream.write(n);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void write(byte[] arrby, int n, int n2) throws IOException {
        while (n2 > 0) {
            void var6_4 = this;
            synchronized (var6_4) {
                if (this.mClosed) {
                    return;
                }
                int n3 = Math.min((int)8192, (int)n2);
                this.mDelegateStream.write(arrby, n, n3);
                n+=n3;
                n2-=n3;
            }
        }
        return;
    }
}

