/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.Camera
 *  android.hardware.Camera$ErrorCallback
 *  android.location.LocationListener
 *  android.os.Environment
 *  java.io.File
 *  java.io.IOException
 *  java.io.InputStreamReader
 *  java.lang.CharSequence
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.Thread
 */
package com.sonyericsson.android.camera.util;

import android.content.Context;
import android.hardware.Camera;
import android.location.LocationListener;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Failed to analyse overrides
 */
public final class DebugEventIntruder
extends Thread {
    private static final String EVENT_DATA_FILENAME = "DebugEvent.dat";
    private static int EVENT_MAX = 0;
    private static int LOCATION_LISTENER_GPS = 0;
    private static int LOCATION_LISTENER_NET = 0;
    private static final String TAG = "DebugEventIntruder";
    private File mDebugEventDataFile;
    private Camera.ErrorCallback mListener;
    private LocationListener[] mLocationListeners = new LocationListener[2];
    private String[] mLocationProviders = new String[2];

    static {
        EVENT_MAX = 16;
        LOCATION_LISTENER_GPS = 0;
        LOCATION_LISTENER_NET = 1;
    }

    public DebugEventIntruder(Context context) {
    }

    /*
     * Exception decompiling
     */
    private void behaviour(InputStreamReader var1) throws IOException {
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private void checkFile() {
        File[] arrfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).listFiles();
        if (arrfile == null) {
            return;
        }
        int n = 0;
        do {
            if (n >= arrfile.length) {
                if (this.mDebugEventDataFile != null) return;
                return;
            }
            if (arrfile[n].isFile()) {
                try {
                    if (arrfile[n].getCanonicalPath().contains((CharSequence)"DebugEvent.dat")) {
                        this.mDebugEventDataFile = new File(arrfile[n].getAbsolutePath());
                    }
                }
                catch (IOException var3_3) {
                    var3_3.printStackTrace();
                }
            }
            ++n;
        } while (true);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private int getEventData(InputStreamReader inputStreamReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        do {
            int n;
            if ((n = inputStreamReader.read()) == -1 || (char)n == '\n') {
                String string = stringBuffer.toString();
                return Integer.parseInt((String)string);
            }
            stringBuffer.append((char)n);
        } while (true);
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private Double getExtraDataDouble(InputStreamReader inputStreamReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        do {
            int n;
            if ((n = inputStreamReader.read()) == -1 || (char)n == '\n') {
                String string = stringBuffer.toString();
                return Double.parseDouble((String)string);
            }
            stringBuffer.append((char)n);
        } while (true);
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private Long getExtraDataLong(InputStreamReader inputStreamReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        do {
            int n;
            if ((n = inputStreamReader.read()) == -1 || (char)n == '\n') {
                String string = stringBuffer.toString();
                return Long.parseLong((String)string);
            }
            stringBuffer.append((char)n);
        } while (true);
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    private void removeEvent() {
        boolean bl = this.mDebugEventDataFile.delete();
        this.mDebugEventDataFile = null;
        if (!bl) {
            // empty if block
        }
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
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredWhile.transformStructuredChildren(StructuredWhile.java:50)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:485)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.transformStructuredChildren(Block.java:378)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:485)
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

    public void setListener(Camera.ErrorCallback errorCallback) {
        this.mListener = errorCallback;
    }

    public void setLocationListenerGps(LocationListener locationListener) {
        this.mLocationListeners[DebugEventIntruder.LOCATION_LISTENER_GPS] = locationListener;
        this.mLocationProviders[DebugEventIntruder.LOCATION_LISTENER_GPS] = "gps";
    }

    public void setLocationListenerNet(LocationListener locationListener) {
        this.mLocationListeners[DebugEventIntruder.LOCATION_LISTENER_NET] = locationListener;
        this.mLocationProviders[DebugEventIntruder.LOCATION_LISTENER_NET] = "network";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void startDebug() {
        if (this.mDebugEventDataFile != null) return;
        try {
            this.start();
            return;
        }
        catch (Exception var1_1) {
            return;
        }
    }
}

