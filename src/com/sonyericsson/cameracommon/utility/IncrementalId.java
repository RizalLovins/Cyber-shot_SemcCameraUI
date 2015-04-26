/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.utility;

public final class IncrementalId {
    public static final int INCREMENTAL_INVALID = -1;
    private static final int INCREMENTAL_MAX = 2147483646;
    private static final int INCREMENTAL_MIN;
    private int mId = 0;

    public void clear() {
        IncrementalId incrementalId = this;
        synchronized (incrementalId) {
            this.mId = 0;
            return;
        }
    }

    public int generateNext() {
        IncrementalId incrementalId = this;
        synchronized (incrementalId) {
            if (this.mId >= 2147483646) {
                this.mId = 0;
            }
            int n = this.mId = 1 + this.mId;
            return n;
        }
    }

    public int getNext() {
        return this.generateNext();
    }
}

