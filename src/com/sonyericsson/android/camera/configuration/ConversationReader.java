/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.sonyericsson.android.camera.configuration.MmsOptions;

public class ConversationReader {
    private static final int INVALID_INT = -1;
    private static final String TAG = "ConversationReader";
    private MmsOptions mMmsOptions;

    public MmsOptions getMmsOptions() {
        return this.mMmsOptions;
    }

    public void readConversationProvider(Context context) {
        MmsTable mmsTable = new MmsTable(context.getContentResolver());
        MmsVideoTable mmsVideoTable = new MmsVideoTable(context.getContentResolver());
        MmsOptions.Builder builder = new MmsOptions.Builder();
        mmsTable.read(builder);
        mmsVideoTable.read(builder);
        this.mMmsOptions = builder.build();
    }

    private static class MmsTable
    extends Table {
        private static final String KEY_MMS_MAX_SIZE = "mms_max_size";
        private static final String URI = "content://com.sonyericsson.conversations/settings";

        MmsTable(ContentResolver contentResolver) {
            super("content://com.sonyericsson.conversations/settings", contentResolver);
        }

        @Override
        void readFromCursor(Cursor cursor, MmsOptions.Builder builder) {
            builder.maxFileSizeBytes(MmsTable.getLong(cursor, "mms_max_size", 1024));
        }
    }

    private static class MmsVideoTable
    extends Table {
        private static final String KEY_MMS_BIT_RATE = "mms_video_bit_rate_in_kbps";
        private static final String KEY_MMS_CODEC = "mms_video_codec";
        private static final String KEY_MMS_MAX_DURATION = "mms_video_max_duration_in_seconds";
        private static final String KEY_MMS_MIME_TYPE = "mms_video_codec_mime_type";
        private static final String KEY_MMS_RESOLUTIONS = "mms_video_resolution";
        private static final String URI = "content://com.sonyericsson.conversations/settings/video";

        MmsVideoTable(ContentResolver contentResolver) {
            super("content://com.sonyericsson.conversations/settings/video", contentResolver);
        }

        @Override
        void readFromCursor(Cursor cursor, MmsOptions.Builder builder) {
            builder.maxDuration(MmsVideoTable.getInt(cursor, "mms_video_max_duration_in_seconds", 1));
            builder.bitRate(MmsVideoTable.getInt(cursor, "mms_video_bit_rate_in_kbps", 1024));
            builder.codec(MmsVideoTable.getString(cursor, "mms_video_codec"));
            builder.mimeType(MmsVideoTable.getString(cursor, "mms_video_codec_mime_type"));
            builder.recommendSizeList(MmsVideoTable.getString(cursor, "mms_video_resolution"));
        }
    }

    private static abstract class Table {
        private final ContentResolver mResolver;
        private final String mUri;

        Table(String string, ContentResolver contentResolver) {
            this.mUri = string;
            this.mResolver = contentResolver;
        }

        /*
         * Enabled aggressive block sorting
         */
        static int getInt(Cursor cursor, String string, int n) {
            int n2;
            int n3 = cursor.getColumnIndex(string);
            if (n3 < 0 || (n2 = cursor.getInt(n3)) == -1) {
                return -1;
            }
            return n2 * n;
        }

        /*
         * Enabled aggressive block sorting
         */
        static long getLong(Cursor cursor, String string, long l) {
            long l2;
            int n = cursor.getColumnIndex(string);
            if (n < 0 || (l2 = cursor.getLong(n)) == -1) {
                return -1;
            }
            return l2 * l;
        }

        static String getString(Cursor cursor, String string) {
            int n = cursor.getColumnIndex(string);
            if (n < 0) {
                return null;
            }
            return cursor.getString(n);
        }

        /*
         * Exception decompiling
         */
        void read(MmsOptions.Builder var1) {
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

        abstract void readFromCursor(Cursor var1, MmsOptions.Builder var2);
    }

}

