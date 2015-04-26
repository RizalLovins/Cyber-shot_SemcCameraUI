/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.LinearLayout
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.contentsview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;

/*
 * Failed to analyse overrides
 */
public class ContentsContainer
extends LinearLayout {
    private static final String TAG = ContentsContainer.class.getSimpleName();

    public ContentsContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void cancelRequestHide() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).cancelRequestHide();
        }
    }

    public void disableClick() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).disableClick();
        }
    }

    public void enableClick() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).enableClick();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void hide() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ContentPallet contentPallet = (ContentPallet)this.getChildAt(i);
            if (contentPallet.hasContent()) {
                contentPallet.setVisibility(4);
                continue;
            }
            contentPallet.requestHide();
        }
    }

    public void pause() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).release();
        }
        this.removeAllViews();
    }

    public void setSensorOrientation(int n) {
        int n2 = 0;
        if (n == 1) {
            n2 = -90;
        }
        for (int i = 0; i < this.getChildCount(); ++i) {
            this.getChildAt(i).setRotation((float)n2);
        }
    }
}

