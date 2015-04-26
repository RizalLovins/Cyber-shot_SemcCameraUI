/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.RelativeLayout
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.focusview.NameTag;

/*
 * Failed to analyse overrides
 */
public class TagFrame
extends RelativeLayout {
    private NameTag mNameTag;

    public TagFrame(Context context) {
        super(context);
    }

    public TagFrame(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TagFrame(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public String getName() {
        return this.mNameTag.getName();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mNameTag = (NameTag)this.findViewById(R.id.tag);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setName(String string) {
        this.mNameTag.setName(string);
        if (string == null) {
            this.setVisibility(4);
            return;
        } else {
            if (string.length() != 0) return;
            {
                this.setVisibility(4);
                return;
            }
        }
    }

    public void setOnTagClickListener(NameTag.NameTagOnClickListener nameTagOnClickListener) {
        this.mNameTag.setOnTagClickListener(nameTagOnClickListener);
    }

    public void setVisibility(int n) {
        super.setVisibility(n);
        this.mNameTag.setVisibility(n);
    }
}

