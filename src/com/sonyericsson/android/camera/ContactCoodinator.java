/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ActivityNotFoundException
 *  android.content.Intent
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.sonyericsson.android.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import com.sonyericsson.cameracommon.utility.CameraLogger;

/*
 * Failed to analyse overrides
 */
public final class ContactCoodinator
extends Activity {
    private static final int SEQ_FIRST = 1;
    private static final int SEQ_SECOND = 2;
    private static final int SEQ_THIRD = 3;
    private static final String TAG = "ContactCoodinator";
    private int mSeq = 1;

    private void launchCropper(Intent intent) {
        Intent intent2 = new Intent("com.android.camera.action.CROP", intent.getData());
        intent2.setDataAndType(intent.getData(), intent.getType());
        intent2.putExtras(intent);
        try {
            this.startActivityForResult(intent2, 1);
            return;
        }
        catch (ActivityNotFoundException var5_3) {
            CameraLogger.e("ContactCoodinator", "Cropper start failed.", (Throwable)var5_3);
            return;
        }
    }

    private void setContactPhoto(Bitmap bitmap) {
        Intent intent = new Intent("android.intent.action.INSERT_OR_EDIT");
        intent.setType("vnd.android.cursor.item/contact");
        Bundle bundle = new Bundle();
        bundle.putParcelable("photo_bitmap", (Parcelable)bitmap);
        intent.putExtras(bundle);
        try {
            this.startActivityForResult(intent, 2);
            return;
        }
        catch (ActivityNotFoundException var6_4) {
            CameraLogger.e("ContactCoodinator", "Setting to the contact failed.", (Throwable)var6_4);
            return;
        }
    }

    protected void onActivityResult(int n, int n2, Intent intent) {
        super.onActivityResult(n, n2, intent);
        switch (n) {
            default: {
                return;
            }
            case 1: {
                if (!(n2 == -1 && intent.getAction().equals((Object)"inline-data"))) {
                    this.finish();
                    return;
                }
                Bitmap bitmap = (Bitmap)intent.getParcelableExtra("data");
                if (bitmap == null) {
                    this.finish();
                    return;
                }
                super.setContactPhoto(bitmap);
                return;
            }
            case 2: 
            case 3: 
        }
        this.finish();
    }

    protected void onResume() {
        super.onResume();
        if (this.mSeq == 1) {
            this.launchCropper(this.getIntent());
            this.mSeq = 2;
            return;
        }
        if (this.mSeq == 2) {
            this.mSeq = 3;
            return;
        }
        this.finish();
    }
}

