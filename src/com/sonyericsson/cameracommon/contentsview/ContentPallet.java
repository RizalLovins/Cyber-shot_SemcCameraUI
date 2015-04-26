/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationSet
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.ScaleAnimation
 *  android.widget.ImageView
 *  android.widget.ProgressBar
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.sonyericsson.cameracommon.contentsview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.utility.CameraLogger;

/*
 * Failed to analyse overrides
 */
public class ContentPallet
extends RelativeLayout {
    private static final int INSERTANIMATION_DURATION = 300;
    private static final float INSERTANIMATION_FADE_END = 1.0f;
    private static final float INSERTANIMATION_FADE_START = 0.0f;
    private static final float INSERTANIMATION_SCALE_END = 1.0f;
    private static final float INSERTANIMATION_SCALE_START = 0.7f;
    private static final String TAG = ContentPallet.class.getSimpleName();
    private static final long intervalTime = 3000;
    private long curTime = 0;
    private final ClickListener mClickListener;
    private Content mContent;
    private boolean mIsRequestHide;
    private int mRequestId;
    private ThumbnailClickListener mThumbnailClickListener;

    public ContentPallet(Context context) {
        super(context);
        this.mClickListener = new ClickListener((ContentPallet)this, null);
    }

    public ContentPallet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mClickListener = new ClickListener((ContentPallet)this, null);
    }

    public void cancelRequestHide() {
        this.mIsRequestHide = false;
    }

    Animation createInsertAnimation(Animation.AnimationListener animationListener) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation((Animation)new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, (float)this.getMeasuredWidth() / 2.0f, (float)this.getMeasuredHeight() / 2.0f));
        animationSet.addAnimation((Animation)new AlphaAnimation(0.0f, 1.0f));
        animationSet.setInterpolator((Interpolator)new DecelerateInterpolator());
        animationSet.setDuration(300);
        animationSet.setAnimationListener(animationListener);
        return animationSet;
    }

    void disableClick() {
        this.findViewById(R.id.content_thumbnail_frame).setOnClickListener(null);
        this.findViewById(R.id.content_progress_bar).setClickable(false);
    }

    void enableClick() {
        this.findViewById(R.id.content_thumbnail_frame).setOnClickListener((View.OnClickListener)this.mClickListener);
        this.findViewById(R.id.content_progress_bar).setClickable(true);
    }

    int getRequestId() {
        return this.mRequestId;
    }

    public boolean hasContent() {
        if (this.mContent != null) {
            return true;
        }
        return false;
    }

    void initialize(int n, ThumbnailClickListener thumbnailClickListener) {
        this.mRequestId = n;
        this.mThumbnailClickListener = thumbnailClickListener;
        this.findViewById(R.id.content_thumbnail_frame).setOnClickListener((View.OnClickListener)this.mClickListener);
    }

    void release() {
        if (this.mContent != null) {
            Bitmap bitmap = this.mContent.getThumbnail();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
            }
            this.mContent = null;
        }
        this.findViewById(R.id.content_thumbnail_frame).setOnClickListener(null);
    }

    public void requestHide() {
        this.mIsRequestHide = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    void set(Content content) {
        this.mContent = content;
        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.content_progress_bar);
        progressBar.setVisibility(4);
        progressBar.setOnClickListener(null);
        ImageView imageView = (ImageView)this.findViewById(R.id.content_thumbnail);
        Bitmap bitmap = content.getThumbnail();
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ImageView imageView2 = new ImageView(this.getContext());
            this.addView((View)imageView2);
            imageView2.getLayoutParams().width = -2;
            imageView2.getLayoutParams().height = -2;
            ((RelativeLayout.LayoutParams)imageView2.getLayoutParams()).addRule(13);
            imageView2.setImageResource(R.drawable.cam_photo_stack_file_corrupted_icn);
        }
        if (content.shouldShowPlayableIcon()) {
            ImageView imageView3 = new ImageView(this.getContext());
            this.addView((View)imageView3);
            imageView3.getLayoutParams().width = -2;
            imageView3.getLayoutParams().height = -2;
            ((RelativeLayout.LayoutParams)imageView3.getLayoutParams()).addRule(13);
            imageView3.setImageResource(content.getPlayIconResourceId());
        }
        if (content.shouldShowExtraIcon()) {
            LayoutInflater layoutInflater = ((Activity)this.getContext()).getLayoutInflater();
            if (layoutInflater != null) {
                ((ImageView)layoutInflater.inflate(R.layout.content_extra_icon, (ViewGroup)this).findViewById(R.id.content_extra_icon_image)).setBackgroundResource(content.getExtraIconResourceId());
            } else {
                CameraLogger.w(TAG, "could not get inflater.");
            }
        }
        this.findViewById(R.id.content_thumbnail_frame).setVisibility(0);
        if (this.mIsRequestHide) {
            this.setVisibility(4);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ClickListener
    implements View.OnClickListener {
        final /* synthetic */ ContentPallet this$0;

        private ClickListener(ContentPallet contentPallet) {
            this.this$0 = contentPallet;
        }

        /* synthetic */ ClickListener(ContentPallet contentPallet,  var2_2) {
            super(contentPallet);
        }

        public void onClick(View view) {
            if (System.currentTimeMillis() - this.this$0.curTime > 3000 && this.this$0.mContent != null && this.this$0.mThumbnailClickListener != null) {
                this.this$0.mThumbnailClickListener.onClick(this.this$0.mContent);
            }
        }
    }

    public static interface ThumbnailClickListener {
        public void onClick(Content var1);
    }

}

