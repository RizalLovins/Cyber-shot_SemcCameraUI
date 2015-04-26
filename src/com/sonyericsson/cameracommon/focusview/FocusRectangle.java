/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  java.lang.Object
 *  java.util.HashMap
 *  java.util.Set
 */
package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import java.util.HashMap;
import java.util.Set;

public abstract class FocusRectangle {
    private final HashMap<TaggedRectangle, Animation> mMapFadeOutAnimation;
    protected final ViewGroup mParentView;

    public FocusRectangle(ViewGroup viewGroup) {
        this.mParentView = viewGroup;
        this.mMapFadeOutAnimation = new HashMap();
    }

    protected abstract void finish();

    public void release() {
        this.mParentView.removeAllViews();
        this.finish();
    }

    protected void startFadeoutAnimation(TaggedRectangle taggedRectangle) {
        View view = taggedRectangle.findViewById(R.id.rect_image);
        AlphaAnimation alphaAnimation = (AlphaAnimation)this.mMapFadeOutAnimation.get((Object)taggedRectangle);
        if (alphaAnimation == null) {
            alphaAnimation = (AlphaAnimation)AnimationUtils.loadAnimation((Context)this.mParentView.getContext(), (int)R.anim.focus_indicator_fade_out);
            alphaAnimation.setAnimationListener((Animation.AnimationListener)new FadeOutAnimationListener((FocusRectangle)this, null));
            this.mMapFadeOutAnimation.put((Object)taggedRectangle, (Object)alphaAnimation);
        }
        view.startAnimation((Animation)alphaAnimation);
    }

    protected void stopAnimation(TaggedRectangle taggedRectangle) {
        View view = taggedRectangle.findViewById(R.id.rect_image);
        if (view.getAnimation() != null) {
            view.clearAnimation();
            view.setAnimation(null);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class FadeOutAnimationListener
    implements Animation.AnimationListener {
        final /* synthetic */ FocusRectangle this$0;

        private FadeOutAnimationListener(FocusRectangle focusRectangle) {
            this.this$0 = focusRectangle;
        }

        /* synthetic */ FadeOutAnimationListener(FocusRectangle focusRectangle,  var2_2) {
            super(focusRectangle);
        }

        public void onAnimationEnd(Animation animation) {
            if (this.this$0.mMapFadeOutAnimation.containsValue((Object)animation)) {
                for (TaggedRectangle taggedRectangle : this.this$0.mMapFadeOutAnimation.keySet()) {
                    if (!((Animation)this.this$0.mMapFadeOutAnimation.get((Object)taggedRectangle)).equals((Object)animation)) continue;
                    taggedRectangle.setVisibility(4);
                }
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

}

