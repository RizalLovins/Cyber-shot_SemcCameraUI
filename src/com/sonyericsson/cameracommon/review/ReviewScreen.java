/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.net.Uri
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.review;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtilListener;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.ReviewMenuButton;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public abstract class ReviewScreen
extends FrameLayout {
    private static final String TAG = ReviewScreen.class.getSimpleName();
    protected View mAutoReviewRight;
    protected List<ReviewMenuButton> mButtonList;
    private RotatableDialog mDialog;
    private int mDisplayOrientation = 2;
    protected boolean mHasMpo;
    protected String mMime;
    private Rect mOrientedPictureSize = new Rect();
    protected ImageView mPictureImage;
    protected Uri mUri;

    public ReviewScreen(Context context) {
        super(context);
    }

    public ReviewScreen(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ReviewScreen(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private Bitmap rotateThumbnail(Bitmap bitmap, int n) {
        int n2 = bitmap.getWidth();
        int n3 = bitmap.getHeight();
        Bitmap bitmap2 = bitmap;
        if (n != 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate((float)n, (float)n2 / 2.0f, (float)n3 / 2.0f);
            Bitmap bitmap3 = Bitmap.createBitmap((Bitmap)bitmap2, (int)0, (int)0, (int)n2, (int)n3, (Matrix)matrix, (boolean)false);
            bitmap2.recycle();
            bitmap2 = bitmap3;
        }
        return bitmap2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setVisible(boolean bl) {
        int n = bl ? 0 : 4;
        this.setVisibility(n);
        Iterator iterator = this.mButtonList.iterator();
        while (iterator.hasNext()) {
            ((ReviewMenuButton)iterator.next()).setVisibility(n);
        }
        if ("image/jpeg".equals((Object)this.mMime) || this.mUri == null) {
            super.setVisiblePlayIcon(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setVisiblePlayIcon(boolean bl) {
        int n = bl ? 0 : 4;
        ((ReviewMenuButton)this.mButtonList.get(0)).setVisibility(n);
    }

    abstract void backToViewFinder();

    protected void cancelDialog() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    public void clearScreen() {
        this.mPictureImage.setImageBitmap(null);
    }

    abstract ContentResolverUtilListener getContentResolverUtilListener();

    abstract MessagePopup getMessagePopup();

    public Uri getUri() {
        return this.mUri;
    }

    public void hideScreen() {
        this.setVisible(false);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mButtonList != null) {
            this.mButtonList.clear();
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPictureImage = (ImageView)this.findViewById(R.id.auto_review_picture_image);
        this.mButtonList = new ArrayList();
        ReviewMenuButton reviewMenuButton = (ReviewMenuButton)this.findViewById(R.id.auto_review_play);
        this.mButtonList.add((Object)reviewMenuButton);
        ReviewMenuButton reviewMenuButton2 = (ReviewMenuButton)this.findViewById(R.id.auto_review_view);
        this.mButtonList.add((Object)reviewMenuButton2);
        ReviewMenuButton reviewMenuButton3 = (ReviewMenuButton)this.findViewById(R.id.auto_review_edit);
        this.mButtonList.add((Object)reviewMenuButton3);
        ReviewMenuButton reviewMenuButton4 = (ReviewMenuButton)this.findViewById(R.id.auto_review_share);
        this.mButtonList.add((Object)reviewMenuButton4);
        ReviewMenuButton reviewMenuButton5 = (ReviewMenuButton)this.findViewById(R.id.auto_review_delete);
        this.mButtonList.add((Object)reviewMenuButton5);
        this.mAutoReviewRight = this.findViewById(R.id.auto_review_right);
        ((ViewGroup.MarginLayoutParams)this.mAutoReviewRight.getLayoutParams()).setMargins(0, 0, LayoutDependencyResolver.getSystemBarMargin(this.getContext()), 0);
    }

    protected void setCurrentDialog(RotatableDialog rotatableDialog) {
        this.cancelDialog();
        this.mDialog = rotatableDialog;
    }

    public void setOrientation(int n) {
        float f = RotationUtil.getAngle(n);
        Iterator iterator = this.mButtonList.iterator();
        while (iterator.hasNext()) {
            ((ReviewMenuButton)iterator.next()).setRotation(f);
        }
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
        this.mHasMpo = false;
    }

    public void setUri(Uri uri, boolean bl) {
        this.mUri = uri;
        this.mHasMpo = bl;
    }

    /*
     * Exception decompiling
     */
    public boolean setupScreen(Activity var1_6, Uri var2_9, byte[] var3_4, String var4_5, String var5_3, Rect var6_8, int var7_7, int var8, boolean var9_2) {
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

    public void showRightIcons(Boolean bl) {
        if (this.mAutoReviewRight != null) {
            if (bl.booleanValue()) {
                this.mAutoReviewRight.setVisibility(0);
            }
        } else {
            return;
        }
        this.mAutoReviewRight.setVisibility(4);
    }

    public void showScreen() {
        this.setVisible(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updatePictureImageLayout(int n, Rect rect) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mPictureImage.getLayoutParams();
        if (n == 1) {
            layoutParams.width = this.getHeight();
            layoutParams.height = rect.height();
            layoutParams.gravity = rect.width() < rect.height() ? 17 : 17;
        } else {
            layoutParams.width = rect.width();
            layoutParams.height = this.getHeight();
            layoutParams.gravity = rect.width() < rect.height() ? 17 : 3;
        }
        this.mPictureImage.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }
}

