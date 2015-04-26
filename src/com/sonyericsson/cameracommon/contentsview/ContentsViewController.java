/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.net.Uri
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.cameracommon.contentsview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.contentsview.ContentLoader;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.ContentsContainer;
import com.sonyericsson.cameracommon.contentsview.PreloadThumbnail;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.IncrementalId;
import java.util.List;

public class ContentsViewController
implements ContentLoader.ContentCreationCallback,
StorageController.StorageListener {
    public static final int MAX_CONTENT_NUMBER = 1;
    private static final String TAG = ContentsViewController.class.getSimpleName();
    private Activity mActivity;
    private ClickListener mClickListener = null;
    private OnClickThumbnailProgressListener mClickThumbnailProgressListener = null;
    private boolean mClickable = true;
    private final ContentsContainer mContentContainer;
    private ContentLoader mContentLoader;
    private int mOrientation;
    private final IncrementalId mRequestIdGenerator;
    private CameraStorageManager mStorageManager;
    private PreloadThumbnail mThumbnail;
    private ContentPallet.ThumbnailClickListener mThumbnailClickListener = null;

    public ContentsViewController(Activity activity, ContentLoader.SecurityLevel securityLevel, CameraStorageManager cameraStorageManager, ContentPallet.ThumbnailClickListener thumbnailClickListener) {
        this.mActivity = activity;
        this.mContentLoader = new ContentLoader((Context)activity, securityLevel, (ContentLoader.ContentCreationCallback)this, 1);
        this.mContentContainer = (ContentsContainer)activity.findViewById(R.id.contents_container);
        this.mRequestIdGenerator = new IncrementalId();
        this.mStorageManager = cameraStorageManager;
        this.mStorageManager.addStorageListener((StorageController.StorageListener)this);
        this.mThumbnailClickListener = thumbnailClickListener;
    }

    private ContentPallet searchPallet(int n) {
        for (int i = 0; i < this.mContentContainer.getChildCount(); ++i) {
            ContentPallet contentPallet = (ContentPallet)this.mContentContainer.getChildAt(i);
            if (n != contentPallet.getRequestId()) continue;
            return contentPallet;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void addContent(int n, Uri uri) {
        if (this.mContentLoader == null) {
            return;
        }
        if (super.searchPallet(n) != null) {
            this.mContentLoader.request(n, uri);
            return;
        }
        if (n == -1) return;
        if (this.mContentContainer.getChildCount() != 0) return;
        this.reload();
    }

    public void addContentOverlayView(int n, View view) {
        ContentPallet contentPallet;
        ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        if ((contentPallet = super.searchPallet(n)) != null) {
            contentPallet.addView(view);
        }
    }

    public void addContentOverlayView(int n, View view, RelativeLayout.LayoutParams layoutParams) {
        ContentPallet contentPallet = super.searchPallet(n);
        if (contentPallet != null) {
            contentPallet.addView(view, (ViewGroup.LayoutParams)layoutParams);
        }
    }

    public void clearContents() {
        this.mContentLoader.pause();
        this.mContentContainer.removeAllViews();
    }

    public int createClearContentFrame() {
        if (this.mActivity == null) {
            CameraLogger.w(TAG, "Activity has already been released at createClearContentFrame.");
            return -1;
        }
        LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        if (layoutInflater == null) {
            CameraLogger.w(TAG, "could not get inflater.");
            return -1;
        }
        int n = this.mRequestIdGenerator.getNext();
        ContentPallet contentPallet = (ContentPallet)layoutInflater.inflate(R.layout.content_pallet, null);
        contentPallet.initialize(n, this.mThumbnailClickListener);
        if (!this.mClickable) {
            contentPallet.disableClick();
        }
        if (this.mContentContainer.getChildCount() >= 1) {
            this.mContentContainer.removeViewAt(0);
        }
        this.mContentContainer.addView((View)contentPallet);
        this.mContentContainer.setSensorOrientation(this.mOrientation);
        return n;
    }

    public int createContentFrame() {
        int n = this.createClearContentFrame();
        this.showProgress(n);
        return n;
    }

    public void disableClick() {
        this.mContentContainer.disableClick();
        this.mClickable = false;
    }

    public void enableClick() {
        this.mContentContainer.enableClick();
        this.mClickable = true;
    }

    public void hide() {
        this.mContentContainer.setVisibility(4);
    }

    public void hideThumbnail() {
        if (this.mThumbnail == null) {
            this.mContentContainer.hide();
        }
    }

    public boolean isLoading() {
        for (int i = 0; i < this.mContentContainer.getChildCount(); ++i) {
            if (((ContentPallet)this.mContentContainer.getChildAt(i)).hasContent()) continue;
            return true;
        }
        return false;
    }

    @Override
    public void onAvailableSizeUpdated(long l) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onContentCreated(int n, Content content) {
        if (this.mActivity == null) {
            CameraLogger.w(TAG, "Activity has already been released.");
            return;
        } else {
            ContentPallet contentPallet;
            if (!(n != -1 || this.isLoading())) {
                n = this.createClearContentFrame();
            }
            if ((contentPallet = super.searchPallet(n)) == null) return;
            {
                contentPallet.set(content);
                if (this.mThumbnail == null) return;
                {
                    this.mThumbnail.onContentCreated();
                    return;
                }
            }
        }
    }

    @Override
    public void onDestinationToSaveChanged() {
    }

    @Override
    public void onStorageStateChanged(String string) {
        if (!this.mStorageManager.isReadable()) {
            this.clearContents();
        }
        if (!this.isLoading()) {
            this.reload();
        }
    }

    public void pause() {
        if (this.mContentLoader != null) {
            this.mContentLoader.pause();
        }
        this.mContentContainer.pause();
    }

    public void release() {
        this.mContentLoader.release();
        this.mContentLoader = null;
        this.mStorageManager.removeStorageListener(this);
        this.mThumbnail = null;
        this.mActivity = null;
    }

    public void reload() {
        List<String> list;
        this.mContentContainer.removeAllViews();
        if (this.mContentLoader != null && (list = this.mStorageManager.getReadableStorage()).size() > 0) {
            this.mContentLoader.reload(1, list);
        }
    }

    public void removeContentInfo() {
        this.mContentLoader.removeTopContent();
    }

    public void removeContentOverlayView(int n, View view) {
        ContentPallet contentPallet;
        View view2 = view.findViewById(R.id.content_progress_bar);
        if (view2 != null) {
            this.setClickThumbnailProgressListener(null);
            view2.setOnClickListener(null);
        }
        if ((contentPallet = super.searchPallet(n)) != null) {
            contentPallet.removeView(view);
            View view3 = contentPallet.findViewById(R.id.content_progress_bar);
            if (view3 != null) {
                view3.setVisibility(4);
                view3.setOnClickListener(null);
            }
        }
    }

    public void removeEarlyThumbnailView() {
        if (this.mThumbnail != null) {
            this.removeContentOverlayView(this.mThumbnail.getRequestId(), this.mThumbnail.getThumbnailView());
            this.mThumbnail = null;
        }
    }

    public void requestLayout() {
        this.mContentContainer.requestLayout();
    }

    public void setClickThumbnailProgressListener(OnClickThumbnailProgressListener onClickThumbnailProgressListener) {
        this.mClickThumbnailProgressListener = onClickThumbnailProgressListener;
        if (onClickThumbnailProgressListener == null) {
            this.mClickListener = null;
            return;
        }
        this.mClickListener = new ClickListener((ContentsViewController)this, null);
    }

    public void setEarlyThumbnailView(View view) {
        this.mThumbnail = new PreloadThumbnail((ContentsViewController)this, view);
        view.findViewById(R.id.content_progress_bar).setOnClickListener((View.OnClickListener)this.mClickListener);
    }

    public void setSensorOrientation(int n) {
        this.mContentContainer.setSensorOrientation(n);
        this.mOrientation = n;
    }

    public void show() {
        this.mContentContainer.setVisibility(0);
        this.mContentContainer.cancelRequestHide();
    }

    public void showProgress(int n) {
        View view;
        ContentPallet contentPallet = super.searchPallet(n);
        if (contentPallet != null && (view = contentPallet.findViewById(R.id.content_progress_bar)) != null) {
            view.setVisibility(0);
            view.setOnClickListener((View.OnClickListener)this.mClickListener);
        }
    }

    public void startAnimation(Animation animation) {
        this.stopAnimation(false);
        if (animation != null) {
            animation.reset();
            this.mContentContainer.startAnimation(animation);
        }
    }

    public void startInsertAnimation(int n) {
        this.startInsertAnimation(n, (Animation.AnimationListener)this.mThumbnail);
    }

    public void startInsertAnimation(int n, Animation.AnimationListener animationListener) {
        if (this.mThumbnail != null) {
            this.mThumbnail.setRequestId(n);
            this.mThumbnail.prepareAnimation();
            this.addContentOverlayView(n, this.mThumbnail.getThumbnailView());
            Animation animation = AnimationUtils.loadAnimation((Context)this.mActivity, (int)R.anim.early_thumbnail_insert);
            animation.setAnimationListener(animationListener);
            this.startAnimation(animation);
        }
    }

    public void stopAnimation(boolean bl) {
        Animation animation = this.mContentContainer.getAnimation();
        if (animation != null) {
            if (!bl) {
                animation.setAnimationListener(null);
            }
            animation.cancel();
            this.mContentContainer.setAnimation(null);
        }
    }

    public void updateSecurityLevel(ContentLoader.SecurityLevel securityLevel) {
        this.mContentLoader.updateSecurityLevel(securityLevel);
    }

    /*
     * Failed to analyse overrides
     */
    private class ClickListener
    implements View.OnClickListener {
        final /* synthetic */ ContentsViewController this$0;

        private ClickListener(ContentsViewController contentsViewController) {
            this.this$0 = contentsViewController;
        }

        /* synthetic */ ClickListener(ContentsViewController contentsViewController,  var2_2) {
            super(contentsViewController);
        }

        public void onClick(View view) {
            if (this.this$0.mClickThumbnailProgressListener != null && view != null && view.getId() == R.id.content_progress_bar) {
                this.this$0.mClickThumbnailProgressListener.onClickThumbnailProgress();
            }
        }
    }

    public static interface OnClickThumbnailProgressListener {
        public void onClickThumbnailProgress();
    }

}

