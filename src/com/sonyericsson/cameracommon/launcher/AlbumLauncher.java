/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ActivityNotFoundException
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.launcher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.sonyericsson.cameracommon.utility.CommonUtility;

/*
 * Failed to analyse overrides
 */
public final class AlbumLauncher
extends Activity {
    public static final String EXTRA_BURST_BUCKETID = "burst_bucketId";
    public static final String EXTRA_PLAYBACK = "android.intent.extra.finishOnCompletion";
    private static final boolean EXTRA_PLAYBACK_DEFAULT = 0;
    public static final String EXTRA_SOMCTYPE = "somc_type";
    private static final String PLAYER_PACKAGE_NAME = "com.sonyericsson.android.camera3d";
    private static final String TAG = AlbumLauncher.class.getSimpleName();

    /*
     * Enabled aggressive block sorting
     */
    public static void launchAlbum(Activity activity, Uri uri, String string, int n, boolean bl) {
        Intent intent = CommonUtility.isCoreCameraApp((Context)activity) ? new Intent("com.sonymobile.album.action.VIEW") : new Intent("com.android.camera.action.REVIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        if (MimeType.fromText(string) == MimeType.MPO) {
            intent.setDataAndType(uri, MimeType.PHOTO.mText);
        } else {
            intent.setDataAndType(uri, string);
        }
        if (bl) {
            intent.putExtra("burst_bucketId", n);
        }
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
            activity.startActivityForResult(intent, 8);
        }
    }

    public static void launchPlayer(Activity activity, Uri uri, String string) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("android.intent.extra.finishOnCompletion", true);
        intent.setDataAndType(uri, string);
        if (CommonUtility.isActivityAvailable((Context)activity, intent)) {
            activity.startActivityForResult(intent, 1);
        }
    }

    private boolean launchPlayer(Uri uri, String string, int n, boolean bl) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$launcher$AlbumLauncher$MimeType[MimeType.fromText(string).ordinal()]) {
            default: {
                AlbumLauncher.launchAlbum((Activity)this, uri, string, n, bl);
                return true;
            }
            case 1: 
            case 2: {
                AlbumLauncher.launchPlayer((Activity)this, uri, string);
                return false;
            }
            case 3: 
        }
        super.playMpo(uri, string);
        return false;
    }

    private void playMpo(Uri uri, String string) {
        Intent intent = new Intent();
        intent.setPackage("com.sonyericsson.android.camera3d");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("android.intent.extra.finishOnCompletion", true);
        intent.setDataAndType(uri, string);
        if (CommonUtility.isActivityAvailable(this.getApplicationContext(), intent)) {
            this.startActivityForResult(intent, 1);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onActivityResult(int n, int n2, Intent intent) {
        super.onActivityResult(n, n2, intent);
        Uri uri = this.getIntent().getData();
        String string = this.getIntent().getType();
        switch (n) {
            default: {
                break;
            }
            case 1: {
                AlbumLauncher.launchAlbum((Activity)this, uri, string, -1, false);
            }
        }
        this.finish();
    }

    protected void onPause() {
        super.onPause();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    protected void onResume() {
        super.onResume();
        var1_1 = this.getIntent().getData();
        var2_2 = this.getIntent().getType();
        var3_3 = this.getIntent().getBooleanExtra("android.intent.extra.finishOnCompletion", false);
        var4_4 = this.getIntent().getIntExtra("somc_type", 0);
        var5_5 = false;
        if (var4_4 == 2) {
            var5_5 = true;
        }
        var6_6 = this.getIntent().getIntExtra("burst_bucketId", -1);
        ** if (!var3_3) goto lbl15
lbl11: // 1 sources:
        try {
            if (this.launchPlayer(var1_1, var2_2, var6_6, var5_5) == false) return;
            this.finish();
            return;
lbl15: // 1 sources:
            AlbumLauncher.launchAlbum((Activity)this, var1_1, var2_2, var6_6, var5_5);
            this.finish();
            return;
        }
        catch (ActivityNotFoundException var7_7) {
            this.finish();
        }
    }

    private static final class MimeType
    extends Enum<MimeType> {
        private static final /* synthetic */ MimeType[] $VALUES;
        public static final /* enum */ MimeType MP4;
        public static final /* enum */ MimeType MPO;
        public static final /* enum */ MimeType PHOTO;
        public static final /* enum */ MimeType THREEGPP;
        public static final /* enum */ MimeType UNKOWN;
        final String mText;

        static {
            PHOTO = new MimeType("image/jpeg");
            MPO = new MimeType("image/mpo");
            MP4 = new MimeType("video/mp4");
            THREEGPP = new MimeType("video/3gpp");
            UNKOWN = new MimeType("");
            MimeType[] arrmimeType = new MimeType[]{PHOTO, MPO, MP4, THREEGPP, UNKOWN};
            $VALUES = arrmimeType;
        }

        private MimeType(String string2) {
            super(string, n);
            this.mText = string2;
        }

        static MimeType fromText(String string) {
            for (MimeType mimeType : MimeType.values()) {
                if (!mimeType.mText.equals((Object)string)) continue;
                return mimeType;
            }
            return UNKOWN;
        }

        public static MimeType valueOf(String string) {
            return (MimeType)Enum.valueOf((Class)MimeType.class, (String)string);
        }

        public static MimeType[] values() {
            return (MimeType[])$VALUES.clone();
        }
    }

}

