/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Environment
 *  android.provider.MediaStore
 *  java.io.File
 *  java.io.FilenameFilter
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.Locale
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Locale;

public class DcfPathBuilder {
    public static final String DCF_DIR_NAME_FREE_WORD = "ANDRO";
    public static final String DCF_FILE_NAME_FREE_WORD_MOVIE = "MOV_";
    public static final String DCF_FILE_NAME_FREE_WORD_PICTURE = "DSC_";
    public static final int LENGTH_OF_DIR_NAME = 8;
    public static final int LENGTH_OF_FILE_NAME = 12;
    public static final int MAX_DIR_NAME = 999;
    public static final int MAX_FILE_NAME = 9999;
    public static final int MIN_DIR_NAME = 100;
    public static final int MIN_FILE_NAME = 1;
    private static final int SCAN_WAIT_TIME = 5000;
    private static final int SCAN_WAIT_TIME_FOR_MEDIASCAN = 60000;
    private static final String TAG = "DcfPathBuilder";
    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final String VOLUME_EXTERNAL = "external";
    private static DcfPathBuilder sInstance = new DcfPathBuilder();
    private DcfImageDirNameFilter mDirNameFilter = new DcfImageDirNameFilter(null);
    private Integer mDirNo = null;
    private DcfImageFileNameFilter mFileNameFilter = new DcfImageFileNameFilter(null);
    private Integer mFileNo = null;
    private boolean mIsScanned = false;
    private boolean mIsScanning = false;
    private Thread mPathScanner = null;
    private String mRootDirectory = Environment.getExternalStorageDirectory().getPath();

    private DcfPathBuilder() {
    }

    /*
     * Exception decompiling
     */
    private String assignImageFilePath(int var1, ContentResolver var2_2) throws IOException {
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
     * Enabled aggressive block sorting
     */
    private String createImageDir() {
        String string;
        File file;
        Locale locale;
        Object[] arrobject;
        StringBuilder stringBuilder;
        this.mDirNo = 1 + this.mDirNo;
        if (!(this.mDirNo <= 999 && (file = new File(string = this.getDcimDirectory(), (stringBuilder = new StringBuilder()).append(String.format((Locale)(locale = Locale.US), (String)"%03d", (Object[])(arrobject = new Object[]{this.mDirNo}))).append("ANDRO").toString())).mkdirs())) {
            return null;
        }
        this.mFileNo = 0;
        return file.getAbsolutePath();
    }

    public static DcfPathBuilder getInstance() {
        return sInstance;
    }

    private boolean isExistCurrentImageDir() {
        String string = this.getDcimDirectory();
        StringBuilder stringBuilder = new StringBuilder();
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{this.mDirNo};
        return new File(string, stringBuilder.append(String.format((Locale)locale, (String)"%03d", (Object[])arrobject)).append("ANDRO").toString()).isDirectory();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private boolean isMediaScannerScanning(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(MediaStore.getMediaScannerUri(), new String[]{"volume"}, null, null, null);
        boolean bl = false;
        if (cursor == null) return bl;
        try {
            boolean bl2;
            int n = cursor.getCount();
            bl = false;
            if (n != 1) return bl;
            cursor.moveToFirst();
            bl = bl2 = "external".equals((Object)cursor.getString(0));
            return bl;
        }
        finally {
            cursor.close();
        }
    }

    private String searchImageDir(String string) {
        File file = new File(string);
        this.mDirNameFilter.mDirNo = 100;
        String[] arrstring = file.list((FilenameFilter)this.mDirNameFilter);
        if (arrstring == null || arrstring.length == 0) {
            this.mDirNo = 99;
            return super.createImageDir();
        }
        this.mDirNo = this.mDirNameFilter.mDirNo;
        String string2 = this.mDirNameFilter.mDirName;
        super.searchImageNo(string + "/" + new File(string2).getName());
        return string2;
    }

    private void searchImageNo(String string) {
        File file = new File(string);
        this.mFileNameFilter.mFileNo = 1;
        String[] arrstring = file.list((FilenameFilter)this.mFileNameFilter);
        if (arrstring == null || arrstring.length == 0) {
            this.mFileNo = 0;
            return;
        }
        this.mFileNo = this.mFileNameFilter.mFileNo;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void startScan() {
        DcfPathBuilder dcfPathBuilder = this;
        synchronized (dcfPathBuilder) {
            boolean bl = this.mIsScanning;
            if (!bl) {
                this.mIsScanned = false;
                this.mPathScanner = new Thread((Runnable)new Runnable(){

                    /*
                     * Enabled aggressive block sorting
                     */
                    public void run() {
                        DcfPathBuilder.this.mIsScanning = true;
                        if (DcfPathBuilder.this.search() != null) {
                            DcfPathBuilder.this.mIsScanned = true;
                        } else {
                            DcfPathBuilder.this.mIsScanned = true;
                        }
                        DcfPathBuilder.this.mIsScanning = false;
                    }
                }, "DCF Path Builder");
                this.mPathScanner.start();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void cancel() {
        DcfPathBuilder dcfPathBuilder = this;
        // MONITORENTER : dcfPathBuilder
        boolean bl = this.mIsScanning;
        if (!bl) {
            // MONITOREXIT : dcfPathBuilder
            return;
        }
        this.mIsScanned = false;
        this.mIsScanning = false;
        this.mPathScanner.interrupt();
        try {
            this.mPathScanner.join();
            return;
        }
        catch (InterruptedException var4_3) {
            CameraLogger.e("DcfPathBuilder", "Cancel failed.", (Throwable)var4_3);
            return;
        }
        finally {
            this.mPathScanner = null;
            return;
        }
    }

    protected boolean checkAndCreateDirectory(String string, boolean bl) {
        File file = new File(string);
        boolean bl2 = true;
        if (!file.isDirectory()) {
            if (!file.mkdirs()) {
                bl2 = false;
            }
            if (bl) {
                this.resetStatus();
                super.startScan();
            }
        }
        if (!super.isExistCurrentImageDir() && bl) {
            this.resetStatus();
            super.startScan();
        }
        return bl2;
    }

    public String getDcimDirectory() {
        return this.mRootDirectory + "/" + Environment.DIRECTORY_DCIM;
    }

    public String getDcimDirectory(String string) {
        return string + "/" + Environment.DIRECTORY_DCIM;
    }

    public String getDestinationToSave() {
        return this.mRootDirectory;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getPhotoPath(Context context) {
        String string;
        try {
            String string2;
            string = string2 = super.assignImageFilePath(0, context.getContentResolver());
            if (string == null) return string;
        }
        catch (IOException var2_4) {
            return null;
        }
        return string + ".JPG";
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getVideoPath(Context var1, String var2_2) {
        var3_3 = "/dev/null";
        try {
            var3_3 = var5_4 = super.assignImageFilePath(1, var1.getContentResolver());
lbl4: // 2 sources:
            do {
                if (var3_3 != null) return var3_3 + var2_2;
                return "/dev/null";
                break;
            } while (true);
        }
        catch (IOException var4_5) {
            ** continue;
        }
    }

    public boolean isAlreadyLastFileExist(String string) {
        Locale locale;
        Locale locale2;
        Object[] arrobject;
        Locale locale3;
        Object[] arrobject2;
        Object[] arrobject3;
        Locale locale4;
        Object[] arrobject4;
        StringBuilder stringBuilder = new StringBuilder().append(string).append("/");
        String string2 = stringBuilder.append(String.format((Locale)(locale2 = Locale.US), (String)"%03d", (Object[])(arrobject3 = new Object[]{999}))).append("ANDRO").toString();
        StringBuilder stringBuilder2 = new StringBuilder().append(string2).append("/DSC_");
        if (new File(stringBuilder2.append(String.format((Locale)(locale3 = Locale.US), (String)"%04d", (Object[])(arrobject2 = new Object[]{9999}))).append(".JPG").toString()).isFile()) {
            return true;
        }
        StringBuilder stringBuilder3 = new StringBuilder().append(string).append("/");
        String string3 = stringBuilder3.append(String.format((Locale)(locale = Locale.US), (String)"%03d", (Object[])(arrobject4 = new Object[]{999}))).append("ANDRO").toString();
        StringBuilder stringBuilder4 = new StringBuilder().append(string3).append("/MOV_");
        String string4 = stringBuilder4.append(String.format((Locale)(locale4 = Locale.US), (String)"%04d", (Object[])(arrobject = new Object[]{9999}))).toString();
        if (new File(string4 + ".mp4").isFile()) {
            return true;
        }
        if (new File(string4 + ".3gp").isFile()) {
            return true;
        }
        return false;
    }

    public void resetStatus() {
        DcfPathBuilder dcfPathBuilder = this;
        synchronized (dcfPathBuilder) {
            this.mIsScanned = false;
            this.mPathScanner = null;
            return;
        }
    }

    public void returnUnusedFile() {
        this.mFileNo = -1 + this.mFileNo;
        if (this.mFileNo < 0) {
            this.mFileNo = 0;
        }
    }

    public final String search() {
        if (this.checkAndCreateDirectory(this.getDcimDirectory(), false)) {
            return this.searchImageDir(this.getDcimDirectory());
        }
        return null;
    }

    public void setDestinationToSave(String string) {
        this.mRootDirectory = string;
        this.resetStatus();
    }

    /*
     * Failed to analyse overrides
     */
    private static class DcfImageDirNameFilter
    implements FilenameFilter {
        private String mDirName;
        private int mDirNo;

        private DcfImageDirNameFilter() {
        }

        /* synthetic */ DcfImageDirNameFilter( var1) {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean accept(File file, String string) {
            int n;
            File file2;
            try {
                if (string.length() != 8 || (n = Integer.parseInt((String)((String)string.subSequence(0, 3)))) < this.mDirNo || 100 > n || n > 999) return false;
            }
            catch (NumberFormatException var3_8) {
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();
            Locale locale = Locale.US;
            Object[] arrobject = new Object[]{n};
            if (!stringBuilder.append(String.format((Locale)locale, (String)"%03d", (Object[])arrobject)).append("ANDRO").toString().equalsIgnoreCase(string) || !(file2 = new File(file, string)).isDirectory()) return false;
            this.mDirNo = n;
            this.mDirName = file2.getAbsolutePath();
            return true;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class DcfImageFileNameFilter
    implements FilenameFilter {
        private int mFileNo;

        private DcfImageFileNameFilter() {
        }

        /* synthetic */ DcfImageFileNameFilter( var1) {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean accept(File file, String string) {
            int n;
            try {
                if (string.length() != 12 || (n = Integer.parseInt((String)((String)string.subSequence(4, 8)))) < this.mFileNo || 1 > n || n > 9999) return false;
            }
            catch (NumberFormatException var3_4) {
                return false;
            }
            this.mFileNo = n;
            return true;
        }
    }

}

