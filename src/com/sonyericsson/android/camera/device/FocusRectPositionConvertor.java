/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.List
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 */
package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import android.graphics.RectF;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FocusRectPositionConvertor {
    private static final Pattern FOCUS_RECTS_PATTERN;
    public static final float MULTIFOCUS_RECT_HEIGHT_RATIO = 0.14f;
    public static final float MULTIFOCUS_RECT_WIDTH_RATIO = 0.11f;
    public static final float MULTIFOCUS_RECT_XDIST_RATIO = 0.22f;
    public static final float MULTIFOCUS_RECT_YDIST_RATIO = 0.14f;
    private static final String TAG;

    static {
        TAG = FocusRectPositionConvertor.class.getSimpleName();
        FOCUS_RECTS_PATTERN = Pattern.compile((String)"\\( *([0-9]+) *, *([0-9]+) *, *([0-9]+) *, *([0-9]+) *\\)");
    }

    public static Rect convertToPreview(Rect rect, Rect rect2, Rect rect3) {
        float f = (float)rect3.width() / (float)rect3.height();
        float f2 = (float)rect2.width() / (float)rect2.height();
        float f3 = rect2.width();
        float f4 = rect2.height();
        if (f2 > f) {
            float f5 = (float)rect2.width() / (float)rect3.width();
            float f6 = f5 * (float)rect3.height();
            return FocusRectPositionConvertor.getRect((int)(f5 * ((float)rect.left / 100.0f * (float)rect3.width())), (int)(f5 * ((float)rect.top / 100.0f * (float)rect3.height()) - (f6 - f4) / 2.0f), (int)(f5 * ((float)rect.width() / 100.0f * (float)rect3.width())), (int)(f5 * ((float)rect.height() / 100.0f * (float)rect3.height())));
        }
        float f7 = (float)rect2.height() / (float)rect3.height();
        float f8 = f7 * (float)rect3.width();
        return FocusRectPositionConvertor.getRect((int)(f7 * ((float)rect.left / 100.0f * (float)rect3.width()) - (f8 - f3) / 2.0f), (int)(f7 * ((float)rect.top / 100.0f * (float)rect3.height())), (int)(f7 * ((float)rect.width() / 100.0f * (float)rect3.width())), (int)(f7 * ((float)rect.height() / 100.0f * (float)rect3.height())));
    }

    public static RectF[] getFixedMultiFocusRectangles(Rect rect) {
        float f = (float)rect.height() / (float)rect.width();
        float f2 = 0.75f * (0.14f / f);
        float f3 = 0.75f * (0.14f / f);
        RectF rectF = new RectF(0.5f - 0.11f / 2.0f, 0.5f - f2 / 2.0f, 0.5f + 0.11f / 2.0f, 0.5f + f2 / 2.0f);
        RectF[] arrrectF = new RectF[]{new RectF(rectF), new RectF(rectF), new RectF(rectF), new RectF(rectF), new RectF(rectF)};
        arrrectF[0].offset(0.0f, - f3);
        arrrectF[1].offset(- 0.22f, 0.0f);
        arrrectF[2].offset(0.0f, 0.0f);
        arrrectF[3].offset(0.22f, 0.0f);
        arrrectF[4].offset(0.0f, f3);
        return arrrectF;
    }

    public static final List<Rect> getFocusAreasOnPreview(String string, Rect rect, Rect rect2) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = FOCUS_RECTS_PATTERN.matcher((CharSequence)string);
        while (matcher.find()) {
            arrayList.add((Object)FocusRectPositionConvertor.convertToPreview(FocusRectPositionConvertor.getRect(Integer.parseInt((String)matcher.group(1)), Integer.parseInt((String)matcher.group(2)), Integer.parseInt((String)matcher.group(3)), Integer.parseInt((String)matcher.group(4))), rect, rect2));
        }
        return arrayList;
    }

    public static Rect getMaxPictureSize(List<Rect> list) {
        return (Rect)Collections.max(list, (Comparator)new Comparator<Rect>(){

            public int compare(Rect rect, Rect rect2) {
                return rect.width() * rect.height() - rect2.width() * rect2.height();
            }
        });
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static RectF[] getMultiFocusRectangles(int n) {
        Rect rect = PositionConverter.getInstance().getPreviewSize();
        if (false) {
            return FocusRectPositionConvertor.getFixedMultiFocusRectangles(rect);
        }
        CapabilityList capabilityList = HardwareCapability.getCapability(n);
        int n2 = capabilityList.MAX_MULTI_FOCUS_AREAS.get();
        if (n2 > 0) {
            List<Rect> list = FocusRectPositionConvertor.getFocusAreasOnPreview(capabilityList.MULTI_FOCUS_RECTS.get(), rect, FocusRectPositionConvertor.getMaxPictureSize(capabilityList.PICTURE_SIZE.get()));
            if (list.isEmpty()) {
                CameraLogger.e(TAG, "Camera doesn't have 'sony-multi-focus-rects' parameter, but 'sony-max-multi-focus-num' is " + n2 + ".");
                return FocusRectPositionConvertor.getFixedMultiFocusRectangles(rect);
            }
            RectF[] arrrectF = new RectF[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                Rect rect2 = (Rect)list.get(i);
                arrrectF[i] = new RectF((float)rect2.left / (float)rect.width(), (float)rect2.top / (float)rect.height(), (float)rect2.right / (float)rect.width(), (float)rect2.bottom / (float)rect.height());
            }
            return arrrectF;
        }
        CameraLogger.e(TAG, "Camera doesn't have 'sony-max-multi-focus-num'.");
        return FocusRectPositionConvertor.getFixedMultiFocusRectangles(rect);
    }

    private static Rect getRect(int n, int n2, int n3, int n4) {
        return new Rect(n, n2, n + n3, n2 + n4);
    }

    public static RectF getSingleFocusRectangle() {
        float f = (1.0f - 0.21f) / 2.0f;
        float f2 = (1.0f - 0.16f) / 2.0f;
        return new RectF(f, f2, f + 0.21f, f2 + 0.16f);
    }

}

