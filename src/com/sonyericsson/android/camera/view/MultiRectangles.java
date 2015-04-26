/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.device.AutoFocusListener;
import com.sonyericsson.android.camera.view.FocusRectangleWithStateMachine;
import com.sonyericsson.android.camera.view.FocusRectanglesManager;
import com.sonyericsson.cameracommon.focusview.CommonResources;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiRectangles
extends FocusRectangleWithStateMachine {
    private static final String TAG = MultiRectangles.class.getSimpleName();
    private final List<TaggedRectangle> mRectangles = new ArrayList();

    public MultiRectangles(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    protected void deselect() {
    }

    @Override
    protected void finish() {
        this.mRectangles.clear();
    }

    @Override
    protected void hide() {
        Iterator iterator = this.mRectangles.iterator();
        while (iterator.hasNext()) {
            ((TaggedRectangle)iterator.next()).setVisibility(4);
        }
    }

    public void init(LayoutInflater layoutInflater, int n) {
        int n2 = n - this.mRectangles.size();
        if (n2 > 0) {
            for (int i = 0; i < n2; ++i) {
                TaggedRectangle taggedRectangle = (TaggedRectangle)layoutInflater.inflate(2130903040, null);
                int n3 = this.mParentView.getContext().getResources().getDimensionPixelSize(2131296281);
                int n4 = this.mParentView.getContext().getResources().getDimensionPixelSize(2131296280);
                this.mParentView.addView((View)taggedRectangle);
                taggedRectangle.prepare(1);
                taggedRectangle.setRectImageSize(n4, n3);
                this.mRectangles.add((Object)taggedRectangle);
            }
        }
    }

    @Override
    protected void init(LayoutInflater layoutInflater, View[] arrview) {
    }

    @Override
    protected void onAfFail(AutoFocusListener.Result result) {
        Iterator iterator = this.mRectangles.iterator();
        while (iterator.hasNext()) {
            this.startFadeoutAnimation((TaggedRectangle)iterator.next());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void onAfSuccess(AutoFocusListener.Result result) {
        for (int i = 0; i < this.mRectangles.size(); ++i) {
            boolean bl = result != null && result.isFocused(i);
            if (bl) {
                ((TaggedRectangle)this.mRectangles.get(i)).changeRectangleResource(CommonResources.SingleIndicator.SUCCESS);
                continue;
            }
            ((TaggedRectangle)this.mRectangles.get(i)).changeRectangleResource(CommonResources.SingleIndicator.NORMAL);
        }
    }

    @Override
    protected void onRecording(AutoFocusListener.Result result) {
    }

    @Override
    protected void resetColor() {
        Iterator iterator = this.mRectangles.iterator();
        while (iterator.hasNext()) {
            ((TaggedRectangle)iterator.next()).changeRectangleResource(CommonResources.SingleIndicator.NORMAL);
        }
    }

    @Override
    protected void select(Rect rect, boolean bl) {
    }

    @Override
    protected void setPosition(Rect[] arrrect) {
        for (int i = 0; i < arrrect.length; ++i) {
            if (i >= this.mRectangles.size()) continue;
            ((TaggedRectangle)this.mRectangles.get(i)).setRectCenter(arrrect[i].centerX(), arrrect[i].centerY());
        }
    }

    @Override
    protected void setSurfaceSize(int n, int n2, int n3, boolean bl) {
        Iterator iterator = this.mRectangles.iterator();
        while (iterator.hasNext()) {
            ((TaggedRectangle)iterator.next()).setSize(n, n2, bl);
        }
    }

    @Override
    protected void show() {
        if (this.mState != FocusRectanglesManager.RectangleState.INACTIVE) {
            Iterator iterator = this.mRectangles.iterator();
            while (iterator.hasNext()) {
                ((TaggedRectangle)iterator.next()).setVisibility(0);
            }
        }
    }
}

