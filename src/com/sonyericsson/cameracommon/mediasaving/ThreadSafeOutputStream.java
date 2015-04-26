/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.io.FileDescriptor
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.mediasaving;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
 * Failed to analyse overrides
 */
public class ThreadSafeOutputStream
extends OutputStream {
    boolean mClosed;
    private OutputStream mDelegateStream;

    public ThreadSafeOutputStream(OutputStream outputStream) {
        this.mDelegateStream = outputStream;
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
            if (FileOutputStream.class.isInstance((Object)this.mDelegateStream)) {
                ((FileOutputStream)this.mDelegateStream).getFD().sync();
            }
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
        void var6_4 = this;
        synchronized (var6_4) {
            boolean bl;
            if (!(n2 <= 0 || (bl = this.mClosed))) {
                this.mDelegateStream.write(arrby, n, n2);
            }
            return;
        }
    }
}

