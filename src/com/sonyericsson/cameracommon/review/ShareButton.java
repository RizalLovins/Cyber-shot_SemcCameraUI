/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.net.Uri
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.widget.ListAdapter
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.cameracommon.review;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import com.sonyericsson.cameracommon.launcher.ApplicationLauncher;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.ReviewMenuButton;
import com.sonyericsson.cameracommon.review.ShareListAdapter;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class ShareButton
extends ReviewMenuButton
implements DialogInterface.OnClickListener {
    private static final String TAG = ShareButton.class.getSimpleName();
    private Intent mShareIntent;
    private List<ResolveInfo> mShareResolveInfoList;

    public ShareButton(Context context) {
        super(context);
    }

    public ShareButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ShareButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private List<ResolveInfo> getShareResolveInfoList(Context context, Uri uri, String string) {
        this.mShareIntent = new Intent();
        this.mShareIntent.setAction("android.intent.action.SEND");
        this.mShareIntent.putExtra("android.intent.extra.STREAM", (Parcelable)uri);
        this.mShareIntent.setType(string);
        this.mShareIntent.addFlags(1);
        return context.getPackageManager().queryIntentActivities(this.mShareIntent, 65600);
    }

    public void onClick(DialogInterface dialogInterface, int n) {
        ResolveInfo resolveInfo = (ResolveInfo)this.mShareResolveInfoList.get(n);
        ApplicationLauncher.startResolvedActivity(this.getContext(), this.mShareIntent, resolveInfo);
    }

    /*
     * Enabled aggressive block sorting
     */
    public RotatableDialog select() {
        this.mShareResolveInfoList = this.getShareResolveInfoList(this.getContext(), this.mReviewScreen.mUri, this.mReviewScreen.mMime);
        if (this.mShareResolveInfoList == null) {
            CameraLogger.e(TAG, "No activity found.");
            return null;
        } else {
            ShareListAdapter shareListAdapter = new ShareListAdapter(this.getContext(), this.mShareResolveInfoList);
            if (shareListAdapter.getCount() < 1) return null;
            return this.getMessagePopup().showShareSelection((DialogInterface.OnClickListener)this, null, (ListAdapter)shareListAdapter);
        }
    }
}

