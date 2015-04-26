/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.FrameLayout
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Locale
 */
package com.sonyericsson.android.camera.burst;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Locale;

public class CountUpView {
    public static final String TAG = CountUpView.class.getSimpleName();

    public static FrameLayout createCountUpView(Activity activity) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        FrameLayout frameLayout = null;
        if (layoutInflater != null) {
            frameLayout = (FrameLayout)layoutInflater.inflate(2130903048, null);
            frameLayout.findViewById(2131689512).setBackgroundResource(2130837611);
        }
        return frameLayout;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setCount(View view, int n) {
        TextView textView = (TextView)view.findViewById(2131689513);
        if (n > 0 && n <= 999) {
            textView.setText((CharSequence)("" + n));
            return;
        } else {
            if (999 >= n) return;
            {
                textView.setText((CharSequence)("" + 999 + String.format((Locale)Locale.US, (String)"+", (Object[])new Object[0])));
                return;
            }
        }
    }
}

