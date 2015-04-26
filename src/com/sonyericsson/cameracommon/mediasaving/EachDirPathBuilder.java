/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Environment
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Locale
 */
package com.sonyericsson.cameracommon.mediasaving;

import android.os.Environment;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EachDirPathBuilder {
    private static final String CURRENT_DIR_FORMAT = "yyyyMMddHHmmssSSS";
    private static final int MAX_FILE_NAME = 999999;
    private static final int MAX_RETRY_TIMES_FOR_CREATING_CURRENT_DIR = 100;
    private static final int MIN_FILE_NAME = 1;
    private static final String TAG = EachDirPathBuilder.class.getSimpleName();
    private String mDirPath = null;
    private int mFileNo = -1;

    public EachDirPathBuilder(String string) throws IOException {
        super.initDirectory(DcfPathBuilder.getInstance().getDestinationToSave(), string);
    }

    private final String getCurrentDirName() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
    }

    private String getDcimDirectory(String string) {
        return string + File.separator + Environment.DIRECTORY_DCIM;
    }

    private void initDirectory(String string, String string2) throws IOException {
        File file;
        String string3 = super.searchSubDirectory(string, string2);
        if (string3 == null) {
            CameraLogger.e(TAG, "initDirectory(): Fail to search sub dir is null");
            throw new IOException("Fail to search sub dir.");
        }
        int n = 0;
        do {
            String string4;
            file = null;
            if (n >= 100 || !(file = new File(string3, string4 = super.getCurrentDirName())).isDirectory() || !file.exists()) break;
            CameraLogger.w(TAG, "initDirectory(): Already directory exists: " + string4);
            ++n;
        } while (true);
        if (file == null) {
            CameraLogger.e(TAG, "initDirectory(): Max retry times for creating current dir.");
            throw new IOException("Max retry times for creating current dir.");
        }
        this.mDirPath = super.searchDirectory(file);
        if (this.mDirPath == null) {
            CameraLogger.e(TAG, "initDirectory(): Fail to search current dir: " + (Object)file);
            throw new IOException("Fail to search current dir.");
        }
        this.mDirPath = this.mDirPath + File.separator;
        this.mFileNo = 0;
    }

    private final String searchDirectory(File file) {
        boolean bl = true;
        if (!(file.isDirectory() || file.mkdirs())) {
            bl = false;
        }
        if (bl) {
            return file.getAbsolutePath();
        }
        CameraLogger.e(TAG, "searchDirectory() failed: " + (Object)file);
        return null;
    }

    private final String searchSubDirectory(String string, String string2) {
        String string3 = super.searchDirectory(new File(super.getDcimDirectory(string)));
        String string4 = null;
        if (string3 != null) {
            string4 = super.searchDirectory(new File(string3, string2));
        }
        return string4;
    }

    public String assignImageFilePath() {
        this.mFileNo = 1 + this.mFileNo;
        if (this.mFileNo > 999999) {
            CameraLogger.w(TAG, "assignImageFilePath(): Max file name.");
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(this.mDirPath);
        stringBuilder.append("DSC_");
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{this.mFileNo};
        stringBuilder.append(String.format((Locale)locale, (String)"%06d", (Object[])arrobject));
        stringBuilder.append(".JPG");
        return stringBuilder.toString();
    }
}

