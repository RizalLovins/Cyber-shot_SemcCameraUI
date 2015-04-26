/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.view.View
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationSet
 *  android.view.animation.AnimationUtils
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.sonyericsson.cameracommon.animation;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.animation.ScaleLayoutAnimation;
import java.util.HashMap;
import java.util.Map;

public class FocusRectanglesAnimation {
    private final Map<Animation, View> mAnimationMap;
    private final Context mContext;
    private AlphaAnimation mFadeOutAnimation;
    private ScaleLayoutAnimation mFocusInAnimationObject;
    private ScaleLayoutAnimation mFocusInAnimationSingle;
    private ScaleLayoutAnimation mFocusInAnimationTouch;
    private final AnimationConfig mObjectConfig;
    private final AnimationConfig mSingleConfig;
    private final AnimationConfig mTouchConfig;
    private AnimationSet mTouchDownAnimation;
    private AnimationSet mTouchUpAnimation;

    public FocusRectanglesAnimation(Context context) {
        this.mContext = context;
        this.mAnimationMap = new HashMap();
        this.mSingleConfig = new AnimationConfig(R.dimen.focus_rect_single_width, R.dimen.focus_rect_single_height, R.attr.focus_indicator_animation_mag_single, R.integer.focus_indicator_animation_focusin_duration);
        this.mTouchConfig = new AnimationConfig(R.dimen.focus_rect_single_width, R.dimen.focus_rect_single_height, R.attr.focus_indicator_animation_mag_single, R.integer.focus_indicator_animation_focusin_duration);
        this.mObjectConfig = new AnimationConfig(R.dimen.focus_rect_object_width, R.dimen.focus_rect_object_height, R.attr.focus_indicator_animation_mag_object, R.integer.focus_indicator_animation_focusin_duration);
    }

    private AlphaAnimation getFadeOutAnimation() {
        if (this.mFadeOutAnimation == null) {
            this.mFadeOutAnimation = (AlphaAnimation)AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.focus_indicator_fade_out);
            this.mFadeOutAnimation.setAnimationListener((Animation.AnimationListener)new FadeOutAnimationListener(this, null));
        }
        return this.mFadeOutAnimation;
    }

    private void playAfFadeOutAnimation(View view) {
        AlphaAnimation alphaAnimation = super.getFadeOutAnimation();
        alphaAnimation.setAnimationListener((Animation.AnimationListener)new FadeOutAnimationListener((FocusRectanglesAnimation)this, null));
        view.startAnimation((Animation)alphaAnimation);
        this.mAnimationMap.put((Object)alphaAnimation, (Object)view);
    }

    private ScaleLayoutAnimation playAfFocusInAnimation(View view, ScaleLayoutAnimation scaleLayoutAnimation, AnimationConfig animationConfig) {
        ScaleLayoutAnimation scaleLayoutAnimation2 = this.getFocusInAnimation(view, scaleLayoutAnimation, animationConfig);
        view.startAnimation((Animation)scaleLayoutAnimation2);
        return scaleLayoutAnimation2;
    }

    private AnimationSet playTouchDownAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        AnimationSet animationSet2 = this.getTouchDownAnimation(view, animationSet, animationConfig);
        view.startAnimation((Animation)animationSet2);
        return animationSet2;
    }

    private AnimationSet playTouchUpAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        AnimationSet animationSet2 = this.getTouchUpAnimation(view, animationSet, animationConfig);
        view.startAnimation((Animation)animationSet2);
        return animationSet2;
    }

    public void cancelAfFocusAnimationObject(View view) {
        if (view.getWidth() != this.mObjectConfig.mToWidth || view.getHeight() != this.mObjectConfig.mToHeight) {
            view.getLayoutParams().width = this.mObjectConfig.mToWidth;
            view.getLayoutParams().height = this.mObjectConfig.mToHeight;
            view.requestLayout();
        }
    }

    public void cancelAfFocusAnimationSingle(View view) {
        if (view.getWidth() != this.mSingleConfig.mToWidth || view.getHeight() != this.mSingleConfig.mToHeight) {
            view.getLayoutParams().width = this.mSingleConfig.mToWidth;
            view.getLayoutParams().height = this.mSingleConfig.mToHeight;
            view.requestLayout();
        }
    }

    public void cancelAfFocusAnimationTouch(View view) {
        if (view.getWidth() != this.mTouchConfig.mToWidth || view.getHeight() != this.mTouchConfig.mToHeight) {
            view.getLayoutParams().width = this.mTouchConfig.mToWidth;
            view.getLayoutParams().height = this.mTouchConfig.mToHeight;
            view.requestLayout();
        }
    }

    public ScaleLayoutAnimation getFocusInAnimation(View view, ScaleLayoutAnimation scaleLayoutAnimation, AnimationConfig animationConfig) {
        if (scaleLayoutAnimation == null) {
            return new ScaleLayoutAnimation.Builder(view).setFromSize(animationConfig.mFromWidth, animationConfig.mFromHeight).setToSize(animationConfig.mToWidth, animationConfig.mToHeight).setDuration(animationConfig.mDuration).create();
        }
        scaleLayoutAnimation.recycle();
        return scaleLayoutAnimation;
    }

    public AnimationConfig getObjectAnimationConfig() {
        return this.mObjectConfig;
    }

    public ScaleLayoutAnimation getTouchAnimation(View view) {
        return this.getFocusInAnimation(view, this.mFocusInAnimationTouch, this.mTouchConfig);
    }

    public AnimationConfig getTouchAnimationConfig() {
        return this.mTouchConfig;
    }

    public AnimationSet getTouchDownAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        if (animationSet == null) {
            animationSet = (AnimationSet)AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.focus_touch_down);
        }
        return animationSet;
    }

    public AnimationSet getTouchUpAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        if (animationSet == null) {
            animationSet = (AnimationSet)AnimationUtils.loadAnimation((Context)this.mContext, (int)R.anim.focus_touch_up);
        }
        return animationSet;
    }

    public void playAfFadeOutAnimationObject(View view) {
        super.playAfFadeOutAnimation(view);
    }

    public void playAfFadeOutAnimationSingle(View view) {
        super.playAfFadeOutAnimation(view);
    }

    public void playAfFadeOutAnimationTouch(View view) {
        super.playAfFadeOutAnimation(view);
    }

    public void playAfFocusInAnimationObject(View view) {
        this.mFocusInAnimationObject = super.playAfFocusInAnimation(view, this.mFocusInAnimationObject, this.mObjectConfig);
    }

    public void playAfFocusInAnimationSingle(View view) {
        this.mFocusInAnimationSingle = super.playAfFocusInAnimation(view, this.mFocusInAnimationSingle, this.mSingleConfig);
    }

    public void playAfFocusInAnimationTouch(View view) {
        this.mFocusInAnimationTouch = super.playAfFocusInAnimation(view, this.mFocusInAnimationTouch, this.mTouchConfig);
    }

    public void playTouchDownAnimation(View view) {
        this.mTouchDownAnimation = super.playTouchDownAnimation(view, this.mTouchDownAnimation, this.mSingleConfig);
    }

    public void playTouchUpAnimation(View view) {
        this.mTouchUpAnimation = super.playTouchUpAnimation(view, this.mTouchUpAnimation, this.mSingleConfig);
    }

    public class AnimationConfig {
        public final int mDuration;
        public final int mFromHeight;
        public final int mFromWidth;
        public final int mToHeight;
        public final int mToWidth;

        public AnimationConfig(int n, int n2, int n3, int n4) {
            float f = Float.valueOf((String)FocusRectanglesAnimation.this.mContext.getResources().getString(n3)).floatValue();
            this.mToWidth = FocusRectanglesAnimation.this.mContext.getResources().getDimensionPixelSize(n);
            this.mToHeight = FocusRectanglesAnimation.this.mContext.getResources().getDimensionPixelSize(n2);
            this.mFromWidth = (int)(f * (float)this.mToWidth);
            this.mFromHeight = (int)(f * (float)this.mToHeight);
            this.mDuration = FocusRectanglesAnimation.this.mContext.getResources().getInteger(n4);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class FadeOutAnimationListener
    implements Animation.AnimationListener {
        final /* synthetic */ FocusRectanglesAnimation this$0;

        private FadeOutAnimationListener(FocusRectanglesAnimation focusRectanglesAnimation) {
            this.this$0 = focusRectanglesAnimation;
        }

        /* synthetic */ FadeOutAnimationListener(FocusRectanglesAnimation focusRectanglesAnimation,  var2_2) {
            super(focusRectanglesAnimation);
        }

        public void onAnimationEnd(Animation animation) {
            View view = (View)this.this$0.mAnimationMap.get((Object)animation);
            if (view != null) {
                view.setVisibility(4);
            }
            this.this$0.mAnimationMap.remove((Object)animation);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

}

