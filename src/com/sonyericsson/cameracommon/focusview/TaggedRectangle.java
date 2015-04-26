/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Animation
 *  android.view.animation.AnimationSet
 *  android.view.animation.AnimationUtils
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.focusview.NameTag;
import com.sonyericsson.cameracommon.focusview.Rectangle;
import com.sonyericsson.cameracommon.focusview.SmileGauge;
import com.sonyericsson.cameracommon.focusview.TagFrame;

/*
 * Failed to analyse overrides
 */
public class TaggedRectangle
extends RelativeLayout
implements NameTag.NameTagOnClickListener,
Rectangle.RectangleOnTouchListener {
    public static final int FACEDETECT_CAPTURE = 1;
    public static final int FACERECOGNITION_REVIEW = 2;
    public static final int OBJECT_TRACKING = 3;
    private static final int RECT_SIZE_LIFE_TIME_MILLIS = 300;
    public static final int SMILE_DETECTION_CAPTURE;
    private static final String TAG;
    private int mCurrentType;
    private String mFaceUuid;
    private boolean mIsAbleToTouch = true;
    private boolean mIsUpdate = false;
    private long mLastSizeUpdatedTimestamp = 0;
    private NameTag.NameTagOnClickListener mNameTagOnClickListener;
    private Rectangle mRectangle;
    private Rectangle.RectangleOnTouchListener mRectangleOnTouchListener;
    private SmileGauge[] mSmileGauges;
    private TagFrame mTagFrame;

    static {
        TAG = TaggedRectangle.class.getSimpleName();
    }

    public TaggedRectangle(Context context) {
        super(context);
    }

    public TaggedRectangle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TaggedRectangle(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private void adjustNamePosition(View view, int n) {
        super.clearAllLayoutRules((View)this.mTagFrame);
        super.defaultAllMargins((View)this.mTagFrame);
        if (n == 1) {
            super.adjustNamePositionPortrait(view, n);
            return;
        }
        super.adjustNamePositionLandScape(view, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void adjustNamePositionLandScape(View view, int n) {
        super.rotateNameTagLandScape();
        Rect rect = this.getFaceRect();
        Rect rect2 = new Rect();
        view.getGlobalVisibleRect(rect2);
        if (rect.bottom + this.mTagFrame.getHeight() > rect2.bottom) {
            if (rect.top - this.mTagFrame.getHeight() < rect2.top) {
                this.mTagFrame.setVisibility(4);
                return;
            }
            super.moveNameTagAboveLandScape();
            this.mTagFrame.setVisibility(0);
        } else {
            super.moveNameTagBelowLandScape();
            this.mTagFrame.setVisibility(0);
        }
        if (rect.centerX() - this.mTagFrame.getWidth() / 2 < rect2.left) {
            super.moveNameTagRightLandScape();
            return;
        }
        if (rect.centerX() + this.mTagFrame.getWidth() / 2 > rect2.right) {
            super.moveNameTagLeftLandScape();
            return;
        }
        super.moveNameTagCenterLandScape();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void adjustNamePositionPortrait(View view, int n) {
        super.rotateNameTagPortrait();
        Rect rect = this.getFaceRect();
        Rect rect2 = new Rect();
        view.getGlobalVisibleRect(rect2);
        if (rect.right + this.mTagFrame.getHeight() > rect2.right) {
            if (rect.left - this.mTagFrame.getHeight() < rect2.left) {
                this.mTagFrame.setVisibility(4);
                return;
            }
            super.moveNameTagAbovePortrait();
            this.mTagFrame.setVisibility(0);
        } else {
            super.moveNameTagBelowPortrait();
            this.mTagFrame.setVisibility(0);
        }
        if (rect.centerY() + this.mTagFrame.getWidth() / 2 > rect2.bottom) {
            super.moveNameTagRightPortrait();
            return;
        }
        if (rect.centerY() - this.mTagFrame.getWidth() / 2 < rect2.top) {
            super.moveNameTagLeftPortrait();
            return;
        }
        super.moveNameTagToCenterPortrait();
    }

    private void adjustSmileGaugesForNameTag(int n) {
        if (n == 1) {
            if (super.isNameTagBelow(n)) {
                super.moveSmileGaugeForBelowNameTagPortrait();
                return;
            }
            super.moveSmileGaugeForAboveNameTagPortrait();
            return;
        }
        if (super.isNameTagBelow(n)) {
            super.moveSmileGaugeForBelowNameTagLandscape();
            return;
        }
        super.moveSmileGaugeForAboveNameTagLandscape();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void adjustSmileGaugesForScreenEdge() {
        if (this.alignBottom()) {
            this.setAlignBottom();
        } else {
            this.clearAlignBottom();
        }
        if (this.alignRight()) {
            this.setAlignRight();
            return;
        }
        this.clearAlignRight();
    }

    private void adjustSmileGaugesPosition(int n) {
        if (!super.isSmileGaugeVisible()) {
            return;
        }
        if (super.isRefugeSmileGaugeForNameTag(n)) {
            super.adjustSmileGaugesForNameTag(n);
            return;
        }
        super.adjustSmileGaugesForScreenEdge();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean alignBottom() {
        if (this.mSmileGauges[0].mAlignBottom) return true;
        boolean bl = this.mSmileGauges[1].mAlignBottom;
        boolean bl2 = false;
        if (!bl) return bl2;
        return true;
    }

    private boolean alignRight() {
        if (this.mSmileGauges[2].mAlignRight || this.mSmileGauges[3].mAlignRight) {
            return true;
        }
        return false;
    }

    private void clearAllLayoutRules(View view) {
        if (view != null) {
            int[] arrn = ((RelativeLayout.LayoutParams)view.getLayoutParams()).getRules();
            for (int i = 0; i < arrn.length; ++i) {
                arrn[i] = 0;
            }
            this.requestLayout();
        }
    }

    private void defaultAllMargins(View view) {
        if (view != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.requestLayout();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isNameTagBelow(int n) {
        int[] arrn = ((RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams()).getRules();
        if (n == 1 ? arrn[1] != 0 : arrn[3] != 0) {
            return true;
        }
        return false;
    }

    private boolean isRectSizeAlreadyInvalid() {
        if (300 < System.currentTimeMillis() - this.mLastSizeUpdatedTimestamp) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isRefugeSmileGaugeForNameTag(int n) {
        int n2 = this.mTagFrame.getVisibility();
        boolean bl = false;
        if (n2 != 0) return bl;
        if (n == 1) {
            int n3 = this.mRectangle.getHeight();
            int n4 = this.mSmileGauges[0].getHeight();
            bl = false;
            if (n3 >= n4) return bl;
            int n5 = this.mRectangle.getWidth();
            int n6 = this.mTagFrame.getWidth();
            bl = false;
            if (n5 >= n6) return bl;
            return true;
        }
        int n7 = this.mRectangle.getWidth();
        int n8 = this.mSmileGauges[2].getWidth();
        bl = false;
        if (n7 >= n8) return bl;
        int n9 = this.mRectangle.getHeight();
        int n10 = this.mTagFrame.getWidth();
        bl = false;
        if (n9 >= n10) return bl;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isSmileGaugeVisible() {
        if (this.mSmileGauges[0].getVisibility() == 0) return true;
        if (this.mSmileGauges[1].getVisibility() == 0) return true;
        if (this.mSmileGauges[2].getVisibility() == 0) return true;
        int n = this.mSmileGauges[3].getVisibility();
        boolean bl = false;
        if (n != 0) return bl;
        return true;
    }

    private void moveNameTagAboveLandScape() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 3, 2, R.id.rect);
        }
    }

    private void moveNameTagAbovePortrait() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 1, 0, R.id.rect);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            int n = - this.mTagFrame.getWidth() / 2 - this.mTagFrame.getHeight() / 2;
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, n + layoutParams.rightMargin, layoutParams.bottomMargin);
            this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.requestLayout();
        }
    }

    private void moveNameTagBelowLandScape() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 2, 3, R.id.rect);
        }
    }

    private void moveNameTagBelowPortrait() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 0, 1, R.id.rect);
        }
    }

    private void moveNameTagCenterLandScape() {
        if (this.mTagFrame != null) {
            this.moveNameTagRightLandScape();
            int n = this.mRectangle.getWidth() / 2 - this.mTagFrame.getWidth() / 2;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            layoutParams.setMargins(n, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
            this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.requestLayout();
        }
    }

    private void moveNameTagLeftLandScape() {
        if (this.mTagFrame != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            layoutParams.setMargins(0, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
            this.switchRule((View)this.mTagFrame, 5, 7, R.id.rect);
        }
    }

    private void moveNameTagLeftPortrait() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 8, 6, R.id.rect);
            int n = this.mTagFrame.getWidth() / 2 - this.mTagFrame.getHeight() / 2;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, n, layoutParams.rightMargin, layoutParams.topMargin);
            this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
    }

    private void moveNameTagRightLandScape() {
        if (this.mTagFrame != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            layoutParams.setMargins(0, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
            this.switchRule((View)this.mTagFrame, 7, 5, R.id.rect);
        }
    }

    private void moveNameTagRightPortrait() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 6, 8, R.id.rect);
            int n = this.mTagFrame.getWidth() / 2 - this.mTagFrame.getHeight() / 2;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, n);
            this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
    }

    private void moveNameTagToCenterPortrait() {
        if (this.mTagFrame != null) {
            this.switchRule((View)this.mTagFrame, 6, 8, R.id.rect);
            int n = this.mTagFrame.getWidth() / 2 - this.mTagFrame.getHeight() / 2 + (this.mRectangle.getHeight() / 2 - this.mTagFrame.getWidth() / 2);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, n);
            this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.requestLayout();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void moveSmileGaugeForAboveNameTagLandscape() {
        SmileGauge smileGauge = this.mSmileGauges[0].getVisibility() == 0 ? this.mSmileGauges[0] : this.mSmileGauges[1];
        smileGauge.moveToBottom(2);
        this.clearAlignBottom();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void moveSmileGaugeForAboveNameTagPortrait() {
        SmileGauge smileGauge = this.mSmileGauges[2].getVisibility() == 0 ? this.mSmileGauges[2] : this.mSmileGauges[3];
        smileGauge.moveToRight(1);
        this.clearAlignRight();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void moveSmileGaugeForBelowNameTagLandscape() {
        SmileGauge smileGauge = this.mSmileGauges[0].getVisibility() == 0 ? this.mSmileGauges[0] : this.mSmileGauges[1];
        smileGauge.clearLayoutParams();
        this.setAlignBottom();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void moveSmileGaugeForBelowNameTagPortrait() {
        SmileGauge smileGauge = this.mSmileGauges[2].getVisibility() == 0 ? this.mSmileGauges[2] : this.mSmileGauges[3];
        smileGauge.clearLayoutParams();
        this.setAlignRight();
    }

    private void prepare() {
        this.mTagFrame = (TagFrame)this.findViewById(R.id.tag_frame);
        this.mTagFrame.setOnTagClickListener((NameTag.NameTagOnClickListener)this);
        this.mRectangle = (Rectangle)this.findViewById(R.id.rect);
        this.mSmileGauges = new SmileGauge[4];
        this.mSmileGauges[0] = (SmileGauge)this.findViewById(R.id.smile_gauge_left);
        this.mSmileGauges[1] = (SmileGauge)this.findViewById(R.id.smile_gauge_right);
        this.mSmileGauges[2] = (SmileGauge)this.findViewById(R.id.smile_gauge_top);
        this.mSmileGauges[3] = (SmileGauge)this.findViewById(R.id.smile_gauge_bottom);
    }

    private void rotateNameTagLandScape() {
        this.mTagFrame.setRotation(0.0f);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
        layoutParams.setMargins(0, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void rotateNameTagPortrait() {
        this.mTagFrame.setRotation(-90.0f);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mTagFrame.getLayoutParams();
        layoutParams.setMargins(- this.mTagFrame.getWidth() / 2 + this.mTagFrame.getHeight() / 2, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        this.mTagFrame.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setAlignBottom() {
        SmileGauge smileGauge = this.mSmileGauges[0].getVisibility() == 0 ? this.mSmileGauges[0] : this.mSmileGauges[1];
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)smileGauge.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(8, this.mRectangle.getId());
        smileGauge.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setAlignRight() {
        SmileGauge smileGauge = this.mSmileGauges[2].getVisibility() == 0 ? this.mSmileGauges[2] : this.mSmileGauges[3];
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)smileGauge.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(7, this.mRectangle.getId());
        smileGauge.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void setName(String string) {
        this.mTagFrame.setName(string);
    }

    private void setUuid(String string) {
        this.mFaceUuid = string;
    }

    private void startNameTagAnimation(int n) {
        AnimationSet animationSet = (AnimationSet)AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.tagged_nametag_show);
        this.mTagFrame.startAnimation((Animation)animationSet);
    }

    private void switchRule(View view, int n, int n2, int n3) {
        if (view != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
            int[] arrn = layoutParams.getRules();
            arrn[n2] = n3;
            arrn[n] = 0;
            view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.requestLayout();
        }
    }

    public final void changeRectangleResource(int n) {
        this.mRectangle.changeChildBackgroundResource(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void clearAlignBottom() {
        SmileGauge smileGauge = this.mSmileGauges[0].getVisibility() == 0 ? this.mSmileGauges[0] : this.mSmileGauges[1];
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)smileGauge.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(8, 0);
        smileGauge.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void clearAlignRight() {
        SmileGauge smileGauge = this.mSmileGauges[2].getVisibility() == 0 ? this.mSmileGauges[2] : this.mSmileGauges[3];
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)smileGauge.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(7, 0);
        smileGauge.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void clearUpdated() {
        this.mIsUpdate = false;
    }

    public Rect getFaceRect() {
        Rect rect = new Rect();
        this.mRectangle.getGlobalVisibleRect(rect);
        return rect;
    }

    public String getName() {
        return this.mTagFrame.getName();
    }

    Rect getNameRect() {
        Rect rect = new Rect();
        if (!this.mTagFrame.getGlobalVisibleRect(rect)) {
            rect.left = this.mTagFrame.getLeft() + this.mRectangle.getLeft();
            rect.top = this.mTagFrame.getTop() + this.mRectangle.getBottom();
            rect.right = this.mTagFrame.getRight() + this.mRectangle.getLeft();
            rect.bottom = this.mTagFrame.getBottom() + this.mRectangle.getBottom();
        }
        return rect;
    }

    public int getRectangleHeight() {
        return this.mRectangle.getHeight();
    }

    public int getRectangleLeft() {
        return this.mRectangle.getLeft();
    }

    public int getRectangleTop() {
        return this.mRectangle.getTop();
    }

    public int getRectangleWidth() {
        return this.mRectangle.getWidth();
    }

    public String getUuid() {
        return this.mFaceUuid;
    }

    public void hide() {
        if (this.getName() != null) {
            this.hideNameTag();
        }
        if (this.isSmileGaugeVisible()) {
            this.setSmileGaugeVisibility(4);
        }
        if (this.getVisibility() != 4) {
            this.setVisibility(4);
        }
    }

    public void hideNameTag() {
        this.setName(null);
    }

    public boolean isUpdate() {
        return this.mIsUpdate;
    }

    public void moveRectTopLeft(int n, int n2) {
        this.scrollBy(- n, - n2);
    }

    protected void onDetachedFromWindow() {
        this.mRectangleOnTouchListener = null;
        this.mRectangle.setRectangleOnTouchListener(null);
        super.onDetachedFromWindow();
    }

    public void onRectTouchCancel(View view, MotionEvent motionEvent) {
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchCancel((View)this, motionEvent);
        }
    }

    public void onRectTouchDown(View view, MotionEvent motionEvent) {
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchDown((View)this, motionEvent);
        }
    }

    public void onRectTouchLongPress(MotionEvent motionEvent) {
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchLongPress(motionEvent);
        }
    }

    public void onRectTouchUp(View view, MotionEvent motionEvent) {
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchUp((View)this, motionEvent);
        }
    }

    public void onTagClick(View view) {
        if (this.mNameTagOnClickListener != null) {
            this.mNameTagOnClickListener.onTagClick((View)this);
        }
    }

    public void prepare(int n) {
        this.mCurrentType = n;
        super.prepare();
        switch (n) {
            default: {
                return;
            }
            case 1: {
                this.mTagFrame.setVisibility(4);
                return;
            }
            case 2: {
                this.mRectangle.setVisibility(4);
                return;
            }
            case 0: {
                this.mTagFrame.setVisibility(0);
                this.mRectangle.setRectangleOnTouchListener((Rectangle.RectangleOnTouchListener)this);
                return;
            }
            case 3: 
        }
        this.mTagFrame.setVisibility(4);
    }

    public void prepare(int n, String string) {
        super.prepare();
        super.setName(string);
        this.prepare(n);
    }

    public void prepare(int n, String string, Rect rect) {
        this.prepare(n, string);
        this.setRawPosition(rect);
    }

    public final void setDisplayRect(Rect rect) {
        SmileGauge[] arrsmileGauge = this.mSmileGauges;
        int n = arrsmileGauge.length;
        for (int i = 0; i < n; ++i) {
            arrsmileGauge[i].setDisplayRect(rect);
        }
    }

    public void setIsAbleToTouch(boolean bl) {
        this.mIsAbleToTouch = bl;
    }

    public void setNameTagOnClickListener(NameTag.NameTagOnClickListener nameTagOnClickListener) {
        this.mNameTagOnClickListener = nameTagOnClickListener;
    }

    public void setRawPosition(Rect rect) {
        this.setRectSize(rect.width(), rect.height());
        this.setRectCenter(rect.centerX(), rect.centerY());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setRectCenter(int n, int n2) {
        int n3;
        int n4;
        switch (this.mCurrentType) {
            default: {
                n3 = this.mRectangle.getWidth() / 2;
                n4 = this.mRectangle.getHeight() / 2;
                break;
            }
            case 0: 
            case 1: {
                n3 = this.getWidth() / 2;
                n4 = this.getHeight() / 2;
            }
        }
        this.scrollTo(n3 + (- n), n4 + (- n2));
    }

    public void setRectImageSize(int n, int n2) {
        ImageView imageView = (ImageView)this.mRectangle.findViewById(R.id.rect_image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = n2;
            layoutParams.width = n;
            imageView.setLayoutParams(layoutParams);
            imageView.requestLayout();
        }
    }

    public void setRectName(String string, Rect rect) {
        this.setRectSize(rect.width(), rect.height());
        this.setRectCenter(rect.centerX(), rect.centerY());
        super.setName(string);
    }

    public void setRectOrientation(int n) {
        if (n == 1) {
            this.mRectangle.setRotation(-90.0f);
            return;
        }
        this.mRectangle.setRotation(0.0f);
    }

    public void setRectSize(int n, int n2) {
        ViewGroup.LayoutParams layoutParams;
        if (super.isRectSizeAlreadyInvalid() && (layoutParams = this.mRectangle.getLayoutParams()) != null) {
            layoutParams.height = n2;
            layoutParams.width = n;
            this.mRectangle.setLayoutParams(layoutParams);
            this.mLastSizeUpdatedTimestamp = System.currentTimeMillis();
        }
    }

    public void setRectangleOnTouchListener(Rectangle.RectangleOnTouchListener rectangleOnTouchListener) {
        this.mRectangleOnTouchListener = rectangleOnTouchListener;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSize(int n, int n2, boolean bl) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.getLayoutParams();
        layoutParams.gravity = bl ? 19 : 17;
        layoutParams.height = n2;
        layoutParams.width = n;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setSmileGaugeVisibility(int n) {
        this.mSmileGauges[0].setVisibility(n);
        this.mSmileGauges[1].setVisibility(n);
        this.mSmileGauges[2].setVisibility(n);
        this.mSmileGauges[3].setVisibility(n);
    }

    public void setSmileGaugesPosition(int n, int n2, int n3, int n4, int n5) {
        this.mSmileGauges[0].setPosition(n, n2, n3, n4, n5);
        this.mSmileGauges[1].setPosition(n, n2, n3, n4, n5);
        this.mSmileGauges[2].setPosition(n, n2, n3, n4, n5);
        this.mSmileGauges[3].setPosition(n, n2, n3, n4, n5);
        this.adjustSmileGaugesPosition(n5);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void setSmileLevel(int n) {
        if (n < 0) {
            return;
        }
        SmileGauge[] arrsmileGauge = this.mSmileGauges;
        int n2 = arrsmileGauge.length;
        for (int i = 0; i < n2; ++i) {
            arrsmileGauge[i].setSmileLevel(n);
        }
    }

    public void setSmileScore(int n) {
        this.mSmileGauges[0].setSmileScore(n);
        this.mSmileGauges[1].setSmileScore(n);
        this.mSmileGauges[2].setSmileScore(n);
        this.mSmileGauges[3].setSmileScore(n);
    }

    public void setUpdated() {
        this.mIsUpdate = true;
    }

    public void startRectangleAnimation(int n) {
        AnimationSet animationSet = (AnimationSet)AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.tagged_rectangle_show);
        this.mRectangle.startAnimation((Animation)animationSet);
    }

    public void startRectanglePressAnimation() {
        AnimationSet animationSet = (AnimationSet)AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.tagged_rectangle_press);
        this.mRectangle.startAnimation((Animation)animationSet);
    }

    public void stopAnimation() {
        if (this.mRectangle.getAnimation() != null) {
            this.mRectangle.clearAnimation();
            this.mRectangle.setAnimation(null);
        }
        if (this.mTagFrame.getAnimation() != null) {
            this.mTagFrame.clearAnimation();
            this.mTagFrame.setAnimation(null);
        }
    }

    public void updateNameTag(String string, String string2, View view, int n) {
        if (!string2.equals((Object)this.getUuid())) {
            this.hideNameTag();
        }
        boolean bl = false;
        if (string != null) {
            boolean bl2 = string.equals((Object)this.getName());
            bl = false;
            if (!bl2) {
                super.setName(string);
                bl = true;
            }
        }
        if (this.getName() != null) {
            super.adjustNamePosition(view, n);
        }
        super.adjustSmileGaugesPosition(n);
        if (bl) {
            super.startNameTagAnimation(n);
        }
        super.setUuid(string2);
    }
}

