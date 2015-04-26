/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Point
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewConfiguration
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.interaction;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.sonyericsson.cameracommon.interaction.TouchMoveAndStopDetector;
import com.sonyericsson.cameracommon.interaction.TouchScaleAndRotateDetector;

/*
 * Failed to analyse overrides
 */
public class TouchActionTranslator
implements GestureDetector.OnGestureListener,
TouchMoveAndStopDetector.TouchStopDetectorListener,
TouchScaleAndRotateDetector.ScaleAndRotateDetectorListener {
    private static final TouchActionListener NULL_LISTENER = new NullInteractionListener(null);
    static final String TAG = "TouchActionTranslator";
    private GestureDetector mAndroidGestureDetector;
    private TouchActionListener mClientListener;
    private Context mContext;
    private InteractionState mCurrentInteractionState;
    private TouchScaleAndRotateDetector mDoubleTouchScaleAndRotateDetector;
    private boolean mIsAllTouchEventInTargetArea;
    private final int mMargin;
    private TouchMoveAndStopDetector mSingleTouchMoveAndStopDetector;
    private View mTargetView;
    private final int mTouchSlop;

    public TouchActionTranslator(Context context, View view) {
        super(context, view, 0);
    }

    public TouchActionTranslator(Context context, View view, int n) {
        super(context, view, n, ViewConfiguration.get((Context)context).getScaledTouchSlop());
    }

    public TouchActionTranslator(Context context, View view, int n, int n2) {
        this.mIsAllTouchEventInTargetArea = true;
        this.mClientListener = NULL_LISTENER;
        this.mCurrentInteractionState = new Idle((TouchActionTranslator)this, null);
        this.mContext = context;
        this.mTargetView = view;
        this.mMargin = n;
        this.mTouchSlop = n2;
        this.setInteractionListener(null);
        this.mDoubleTouchScaleAndRotateDetector = new TouchScaleAndRotateDetector();
        this.mDoubleTouchScaleAndRotateDetector.setScaleAndRotateDetectorListener((TouchScaleAndRotateDetector.ScaleAndRotateDetectorListener)this);
    }

    private void changeTo(InteractionState interactionState) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mCurrentInteractionState = interactionState;
            return;
        }
    }

    private GestureDetector getAndroidGestureDetector() {
        if (this.mAndroidGestureDetector == null) {
            this.mAndroidGestureDetector = new GestureDetector(this.mContext, (GestureDetector.OnGestureListener)this, new Handler(), true);
        }
        return this.mAndroidGestureDetector;
    }

    private TouchMoveAndStopDetector getSingleTouchMoveAndStopDetector() {
        if (this.mSingleTouchMoveAndStopDetector == null) {
            this.mSingleTouchMoveAndStopDetector = new TouchMoveAndStopDetector(this.mTouchSlop);
            this.mSingleTouchMoveAndStopDetector.setTouchStopDetectorListener((TouchMoveAndStopDetector.TouchStopDetectorListener)this);
        }
        return this.mSingleTouchMoveAndStopDetector;
    }

    private boolean hitTest(View view, int n, int n2, int n3) {
        return new Rect(n, n, view.getWidth() - n, view.getHeight() - n).contains(n2, n3);
    }

    public void cancel() {
        this.changeTo(new Idle(this, null));
    }

    public void onDoubleTouchRotateDetected(float f, float f2) {
        void var4_3 = this;
        synchronized (var4_3) {
            this.mCurrentInteractionState.handleTouchRotateEvent(f, f2);
            return;
        }
    }

    public void onDoubleTouchScaleDetected(float f, float f2, float f3) {
        void var5_4 = this;
        synchronized (var5_4) {
            this.mCurrentInteractionState.handleTouchScaleEvent(f, f2, f3);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean onDown(MotionEvent motionEvent) {
        void var2_2 = this;
        // MONITORENTER : var2_2
        // MONITOREXIT : var2_2
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        void var6_5 = this;
        synchronized (var6_5) {
            this.mClientListener.onFling(motionEvent, motionEvent2, f, f2);
            return true;
        }
    }

    public void onLongPress(MotionEvent motionEvent) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mClientListener.onLongPress(motionEvent);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        void var5_5 = this;
        // MONITORENTER : var5_5
        // MONITOREXIT : var5_5
        return true;
    }

    public void onShowPress(MotionEvent motionEvent) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mClientListener.onShowPress(motionEvent);
            return;
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mClientListener.onSingleTapUp(motionEvent);
            return true;
        }
    }

    public void onSingleTouchMoveDetected(Point point, Point point2, Point point3) {
        void var5_4 = this;
        synchronized (var5_4) {
            this.mCurrentInteractionState.handleSingleTouchMoveEvent(point, point2, point3);
            return;
        }
    }

    public void onSingleTouchStopDetected(Point point, Point point2, Point point3) {
        void var5_4 = this;
        synchronized (var5_4) {
            this.mCurrentInteractionState.handleSingleTouchStopEvent(point, point2, point3);
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1) {
        var6_2 = this;
        // MONITORENTER : var6_2
        if (this.mTargetView != null) {
            for (var3_3 = 0; var3_3 < var1.getPointerCount(); ++var3_3) {
                if (super.hitTest(this.mTargetView, this.mMargin, (int)var1.getX(var3_3), (int)var1.getY(var3_3))) {
                    this.mIsAllTouchEventInTargetArea = true;
                    continue;
                }
                this.mIsAllTouchEventInTargetArea = false;
            }
        }
        switch (var1.getActionMasked()) {
            case 0: {
                super.getSingleTouchMoveAndStopDetector().startTouchStopDetection((int)var1.getX(), (int)var1.getY());
                ** break;
            }
            case 2: {
                if (1 == var1.getPointerCount()) {
                    super.getSingleTouchMoveAndStopDetector().updateCurrentPosition((int)var1.getX(), (int)var1.getY());
                }
            }
lbl16: // 5 sources:
            default: {
                ** GOTO lbl20
            }
            case 1: 
            case 3: 
        }
        super.getSingleTouchMoveAndStopDetector().stopTouchStopDetection();
lbl20: // 2 sources:
        this.mCurrentInteractionState.handleMotionEvent(var1);
        super.getAndroidGestureDetector().onTouchEvent(var1);
        var5_4 = this.mIsAllTouchEventInTargetArea;
        // MONITOREXIT : var6_2
        return var5_4;
    }

    public void release() {
        TouchActionTranslator touchActionTranslator = this;
        synchronized (touchActionTranslator) {
            this.mContext = null;
            this.mTargetView = null;
            if (this.mSingleTouchMoveAndStopDetector != null) {
                this.mSingleTouchMoveAndStopDetector.release();
                this.mSingleTouchMoveAndStopDetector = null;
            }
            this.mDoubleTouchScaleAndRotateDetector.release();
            this.mDoubleTouchScaleAndRotateDetector = null;
            this.mAndroidGestureDetector = null;
            this.mClientListener = NULL_LISTENER;
            return;
        }
    }

    public void setInteractionListener(TouchActionListener touchActionListener) {
        if (touchActionListener != null) {
            this.mClientListener = touchActionListener;
            return;
        }
        this.mClientListener = NULL_LISTENER;
    }

    private class DoubleDown
    implements InteractionState {
        final /* synthetic */ TouchActionTranslator this$0;

        private DoubleDown(TouchActionTranslator touchActionTranslator) {
            this.this$0 = touchActionTranslator;
        }

        /* synthetic */ DoubleDown(TouchActionTranslator touchActionTranslator,  var2_2) {
            super(touchActionTranslator);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            int n;
            int n2;
            switch (motionEvent.getActionMasked()) {
                case 2: {
                    if (motionEvent.getPointerCount() != 2) return;
                    {
                        Point point = new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0));
                        Point point2 = new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1));
                        this.this$0.mClientListener.onDoubleMoved(point, point2);
                        this.this$0.changeTo(new DoubleMove(point, point2));
                        return;
                    }
                }
                default: {
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onDoubleCanceled();
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 5: {
                    this.this$0.changeTo(new OverTriple(this.this$0, null));
                    return;
                }
                case 6: 
            }
            if (motionEvent.getPointerCount() == 1) return;
            {
                n = motionEvent.getActionIndex();
                n2 = n == 0 ? 1 : 0;
            }
            this.this$0.mClientListener.onSingleReleasedInDouble(new Point((int)motionEvent.getX(n), (int)motionEvent.getY(n)), new Point((int)motionEvent.getX(n2), (int)motionEvent.getY(n2)));
            this.this$0.getSingleTouchMoveAndStopDetector().updateCurrentAndLastPosition((int)motionEvent.getX(n2), (int)motionEvent.getY(n2));
            this.this$0.changeTo(new SingleMove(this.this$0, null));
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }
    }

    private class DoubleMove
    implements InteractionState {
        DoubleMove(Point point, Point point2) {
            TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.startScaleAndRotateDetection(new PointF(point), new PointF(point2));
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 2: {
                    if (motionEvent.getPointerCount() == 2) {
                        TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.updateCurrentPosition(new PointF(motionEvent.getX(0), motionEvent.getY(0)), new PointF(motionEvent.getX(1), motionEvent.getY(1)));
                        TouchActionTranslator.this.mClientListener.onDoubleMoved(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
                        return;
                    }
                }
                default: {
                    return;
                }
                case 3: {
                    TouchActionTranslator.this.mClientListener.onDoubleCanceled();
                    TouchActionTranslator.this.changeTo(new Idle(TouchActionTranslator.this, null));
                    return;
                }
                case 5: {
                    TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.stopScaleAndRotateDetection();
                    TouchActionTranslator.this.changeTo(new OverTriple(TouchActionTranslator.this, null));
                    return;
                }
                case 6: 
            }
            TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.stopScaleAndRotateDetection();
            int n = motionEvent.getActionIndex();
            int n2 = n == 0 ? 1 : 0;
            TouchActionTranslator.this.mClientListener.onSingleReleasedInDouble(new Point((int)motionEvent.getX(n), (int)motionEvent.getY(n)), new Point((int)motionEvent.getX(n2), (int)motionEvent.getY(n2)));
            TouchActionTranslator.this.getSingleTouchMoveAndStopDetector().updateCurrentAndLastPosition((int)motionEvent.getX(n2), (int)motionEvent.getY(n2));
            TouchActionTranslator.this.changeTo(new SingleMove(TouchActionTranslator.this, null));
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
            TouchActionTranslator.this.mClientListener.onDoubleRotated(f, f2);
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
            TouchActionTranslator.this.mClientListener.onDoubleScaled(f, f2, f3);
        }
    }

    private class Idle
    implements InteractionState {
        final /* synthetic */ TouchActionTranslator this$0;

        private Idle(TouchActionTranslator touchActionTranslator) {
            this.this$0 = touchActionTranslator;
        }

        /* synthetic */ Idle(TouchActionTranslator touchActionTranslator,  var2_2) {
            super(touchActionTranslator);
        }

        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    return;
                }
                case 0: 
            }
            this.this$0.mClientListener.onSingleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
            this.this$0.changeTo(new SingleDown(this.this$0, null));
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }
    }

    private static interface InteractionState {
        public void handleMotionEvent(MotionEvent var1);

        public void handleSingleTouchMoveEvent(Point var1, Point var2, Point var3);

        public void handleSingleTouchStopEvent(Point var1, Point var2, Point var3);

        public void handleTouchRotateEvent(float var1, float var2);

        public void handleTouchScaleEvent(float var1, float var2, float var3);
    }

    private static final class NullInteractionListener
    implements TouchActionListener {
        private NullInteractionListener() {
        }

        /* synthetic */ NullInteractionListener( var1) {
        }

        @Override
        public void onDoubleCanceled() {
        }

        @Override
        public void onDoubleMoved(Point point, Point point2) {
        }

        @Override
        public void onDoubleRotated(float f, float f2) {
        }

        @Override
        public void onDoubleScaled(float f, float f2, float f3) {
        }

        @Override
        public void onDoubleTouched(Point point, Point point2) {
        }

        @Override
        public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public void onOverTripleCanceled() {
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public void onSingleCanceled() {
        }

        @Override
        public void onSingleMoved(Point point, Point point2, Point point3) {
        }

        @Override
        public void onSingleReleased(Point point) {
        }

        @Override
        public void onSingleReleasedInDouble(Point point, Point point2) {
        }

        @Override
        public void onSingleStopped(Point point, Point point2, Point point3) {
        }

        @Override
        public void onSingleTapUp(MotionEvent motionEvent) {
        }

        @Override
        public void onSingleTouched(Point point) {
        }
    }

    private class OverTriple
    implements InteractionState {
        final /* synthetic */ TouchActionTranslator this$0;

        private OverTriple(TouchActionTranslator touchActionTranslator) {
            this.this$0 = touchActionTranslator;
        }

        /* synthetic */ OverTriple(TouchActionTranslator touchActionTranslator,  var2_2) {
            super(touchActionTranslator);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onOverTripleCanceled();
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 6: {
                    if (motionEvent.getPointerCount() != 3) return;
                    this.this$0.changeTo(new DoubleDown(this.this$0, null));
                    return;
                }
            }
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }
    }

    private class SingleDown
    implements InteractionState {
        final /* synthetic */ TouchActionTranslator this$0;

        private SingleDown(TouchActionTranslator touchActionTranslator) {
            this.this$0 = touchActionTranslator;
        }

        /* synthetic */ SingleDown(TouchActionTranslator touchActionTranslator,  var2_2) {
            super(touchActionTranslator);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    return;
                }
                case 2: {
                    this.this$0.changeTo(new SingleMove(this.this$0, null));
                    return;
                }
                case 1: {
                    this.this$0.mClientListener.onSingleReleased(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onSingleCanceled();
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 5: {
                    if (motionEvent.getPointerCount() == 1) return;
                    this.this$0.mClientListener.onDoubleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
                    this.this$0.changeTo(new DoubleDown(this.this$0, null));
                    return;
                }
            }
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }
    }

    private class SingleMove
    implements InteractionState {
        final /* synthetic */ TouchActionTranslator this$0;

        private SingleMove(TouchActionTranslator touchActionTranslator) {
            this.this$0 = touchActionTranslator;
        }

        /* synthetic */ SingleMove(TouchActionTranslator touchActionTranslator,  var2_2) {
            super(touchActionTranslator);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    return;
                }
                case 1: {
                    this.this$0.mClientListener.onSingleReleased(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onSingleCanceled();
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 5: {
                    if (motionEvent.getPointerCount() == 1) return;
                    this.this$0.mClientListener.onDoubleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
                    this.this$0.changeTo(new DoubleDown(this.this$0, null));
                    return;
                }
            }
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
            this.this$0.mClientListener.onSingleMoved(point, point2, point3);
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
            this.this$0.mClientListener.onSingleStopped(point, point2, point3);
            this.this$0.changeTo(new SingleStop(this.this$0, null));
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }
    }

    private class SingleStop
    implements InteractionState {
        final /* synthetic */ TouchActionTranslator this$0;

        private SingleStop(TouchActionTranslator touchActionTranslator) {
            this.this$0 = touchActionTranslator;
        }

        /* synthetic */ SingleStop(TouchActionTranslator touchActionTranslator,  var2_2) {
            super(touchActionTranslator);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    return;
                }
                case 1: {
                    this.this$0.mClientListener.onSingleReleased(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onSingleCanceled();
                    this.this$0.changeTo(new Idle(this.this$0, null));
                    return;
                }
                case 5: {
                    if (motionEvent.getPointerCount() == 1) return;
                    this.this$0.mClientListener.onDoubleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
                    this.this$0.changeTo(new DoubleDown(this.this$0, null));
                    return;
                }
            }
        }

        @Override
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
            this.this$0.mClientListener.onSingleMoved(point, point2, point3);
            this.this$0.changeTo(new SingleMove(this.this$0, null));
        }

        @Override
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }
    }

    public static interface TouchActionListener {
        public void onDoubleCanceled();

        public void onDoubleMoved(Point var1, Point var2);

        public void onDoubleRotated(float var1, float var2);

        public void onDoubleScaled(float var1, float var2, float var3);

        public void onDoubleTouched(Point var1, Point var2);

        public void onFling(MotionEvent var1, MotionEvent var2, float var3, float var4);

        public void onLongPress(MotionEvent var1);

        public void onOverTripleCanceled();

        public void onShowPress(MotionEvent var1);

        public void onSingleCanceled();

        public void onSingleMoved(Point var1, Point var2, Point var3);

        public void onSingleReleased(Point var1);

        public void onSingleReleasedInDouble(Point var1, Point var2);

        public void onSingleStopped(Point var1, Point var2, Point var3);

        public void onSingleTapUp(MotionEvent var1);

        public void onSingleTouched(Point var1);
    }

}

