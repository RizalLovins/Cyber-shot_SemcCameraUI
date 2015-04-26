/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.sonyericsson.android.camera.controller;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.controller.CaptureButtonGestureListener;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.EventAction;
import com.sonyericsson.android.camera.controller.EventFilter;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.controller.TouchFocusDetector;
import com.sonyericsson.android.camera.controller.ZoomDirection;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.android.camera.view.DetectedFaceRectangles;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.interaction.TouchActionTranslator;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.utility.CameraLogger;

/*
 * Failed to analyse overrides
 */
public class EventDispatcher
implements View.OnTouchListener,
TouchFocusDetector.MotionEventDetectorListener,
DetectedFaceRectangles.ChangeFocusFaceListener,
TouchActionTranslator.TouchActionListener,
StorageController.StorageListener {
    private static final long DETECTION_LIMIT_TIME = 1000;
    private static final String TAG = EventDispatcher.class.getSimpleName();
    private final CameraActivity mActivity;
    private CameraWindow mCameraWindow;
    private final GestureDetector mCaptureButtonGestureDetector;
    private EventFilter mEventFilter = EventFilter.UNKNOWN;
    private ControllerEventSource mExclusiveEventSouce;
    private KeyEventTranslator mKeyEventTranslator;
    private KeyState mKeyState = KeyState.NON;
    final ControllerEventSender mNoEvent = new NoEvent();
    final ControllerEventSender mObjectTracking = new ObjectTrackingEvent();
    private boolean mSendLongPressEvent;
    private boolean mShouldSelfTimerStart;
    private TouchActionTranslator mTouchActionTranslator;
    final TouchCapturing mTouchCaptureOff;
    final TouchCapturing mTouchCaptureOn;
    final ControllerEventSender mTouchCapturingOnly;
    private long mTouchDetectedTime;
    final ControllerEventSender mTouchFocus = new TouchFocusEvent();
    private final TouchFocusDetector mTouchFocusDetector;
    final ControllerEventSender mTouchFocusWhenever;
    TouchEventTranslator mTranslator;

    public EventDispatcher(CameraActivity cameraActivity) {
        this.mTouchFocusWhenever = new TouchFocusWheneverEvent();
        this.mTouchCapturingOnly = new TouchCapturingOnlyEvent();
        this.mTouchCaptureOn = new TouchCaptureOn();
        this.mTouchCaptureOff = new TouchCaptureOff();
        this.mActivity = cameraActivity;
        this.mTouchFocusDetector = new TouchFocusDetector(ViewConfiguration.get((Context)cameraActivity).getScaledTouchSlop());
        this.mCaptureButtonGestureDetector = new GestureDetector((Context)cameraActivity, (GestureDetector.OnGestureListener)new CaptureButtonGestureListener((EventDispatcher)this));
        this.mCaptureButtonGestureDetector.setIsLongpressEnabled(true);
        this.mTranslator = new TranslatorNoPosition(this.mTouchCaptureOff);
        this.mKeyEventTranslator = new KeyEventTranslator(this.mActivity.getCommonSettings());
    }

    /*
     * Exception decompiling
     */
    private boolean consumedByZoom(MotionEvent var1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    public static EventAction getEventAction(int n) {
        switch (n) {
            default: {
                return EventAction.UNKNOWN;
            }
            case 0: {
                return EventAction.DOWN;
            }
            case 3: {
                return EventAction.CANCEL;
            }
            case 1: 
        }
        return EventAction.UP;
    }

    private KeyState getKeyState() {
        return this.mKeyState;
    }

    private boolean isExclusivedEventSource(ControllerEventSource controllerEventSource) {
        ControllerEventSource controllerEventSource2 = this.mExclusiveEventSouce;
        boolean bl = false;
        if (controllerEventSource2 != null) {
            ControllerEventSource controllerEventSource3 = this.mExclusiveEventSouce;
            bl = false;
            if (controllerEventSource3 != controllerEventSource) {
                bl = true;
            }
        }
        return bl;
    }

    private void resetKeyState(KeyState keyState) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventDispatcher$KeyState[keyState.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                this.sendCaptureEvent(EventAction.CANCEL, ControllerEventSource.OTHER);
                super.setKeyState(KeyState.NON);
                return;
            }
            case 3: {
                super.setKeyState(KeyState.AFDONE);
                return;
            }
            case 2: 
        }
        super.setKeyState(KeyState.NON);
    }

    private void sendEventFilterMessage(int n) {
        this.mEventFilter = EventFilter.getEventFilter(this.mEventFilter, n);
    }

    private void setCurrentEventSource(ControllerEventSource controllerEventSource) {
        this.mExclusiveEventSouce = controllerEventSource;
    }

    private void setKeyState(KeyState keyState) {
        this.mKeyState = keyState;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean shouldSelfTimerStart(ControllerEventSource controllerEventSource) {
        if (!this.mShouldSelfTimerStart) {
            return false;
        }
        boolean bl = this.mActivity.getParameterManager().getSelfTimerType(controllerEventSource) == 2 ? this.mActivity.getParameterManager().isVideoSelfTimerOn() : this.mActivity.getParameterManager().isSelfTimerOn();
        if (!Executor.isRecording()) return bl;
        return false;
    }

    public void cancel() {
        if (this.mTouchActionTranslator != null) {
            this.mTouchActionTranslator.cancel();
        }
    }

    void dispatch(EventAction eventAction, int n, int n2, ControllerEventSource controllerEventSource) {
        Rect rect = this.mCameraWindow.getPosition(n, n2);
        this.mTranslator.translate(eventAction, controllerEventSource, rect);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void dispatchZoomEvent(EventAction eventAction, ControllerEventSource controllerEventSource, ZoomDirection zoomDirection) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventFilter[this.mEventFilter.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                if (eventAction != EventAction.DOWN) return;
                switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerEventSource.ordinal()]) {
                    default: {
                        return;
                    }
                    case 2: {
                        this.preparePinchZoom(controllerEventSource);
                        return;
                    }
                    case 4: 
                }
                Executor.sendEvent(ControllerEvent.EV_ZOOM_PREPARE, controllerEventSource);
                Executor.sendEvent(ControllerEvent.EV_ZOOM_START, controllerEventSource, 0, (Object)zoomDirection);
                return;
            }
            case 2: 
            case 3: {
                if (eventAction != EventAction.DOWN) return;
                switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerEventSource.ordinal()]) {
                    default: {
                        return;
                    }
                    case 2: 
                }
                this.preparePinchZoom(controllerEventSource);
                return;
            }
            case 4: {
                if (eventAction != EventAction.UP) return;
                Executor.sendEvent(ControllerEvent.EV_ZOOM_STOP, controllerEventSource);
                Executor.sendEvent(ControllerEvent.EV_ZOOM_FINISH, controllerEventSource);
                return;
            }
            case 5: 
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[eventAction.ordinal()]) {
            default: {
                return;
            }
            case 4: {
                Executor.sendEvent(ControllerEvent.EV_ZOOM_FINISH, controllerEventSource);
                return;
            }
            case 6: 
        }
        Executor.sendEvent(ControllerEvent.EV_ZOOM_START, controllerEventSource, 0, (Object)zoomDirection);
        Executor.sendEvent(ControllerEvent.EV_ZOOM_STOP, controllerEventSource);
    }

    public TouchCapturing getTouchCapturing(boolean bl, ControllerEventSender controllerEventSender) {
        if (bl) {
            this.mTouchCaptureOn.mSender = controllerEventSender;
            return this.mTouchCaptureOn;
        }
        this.mTouchCaptureOff.mSender = controllerEventSender;
        return this.mTouchCaptureOff;
    }

    public void init(CameraWindow cameraWindow) {
        this.mCameraWindow = cameraWindow;
        this.resetStatus();
    }

    public boolean isCaptureValid() {
        if (this.mEventFilter == EventFilter.PREVIEWING || this.mEventFilter == EventFilter.SELF_TIMER_RUNNING) {
            return true;
        }
        return false;
    }

    public boolean isDragging() {
        return this.mTouchFocusDetector.isDragging();
    }

    public boolean isPinchZoomValid() {
        if (this.mEventFilter == EventFilter.PREVIEWING || this.mEventFilter == EventFilter.TOUCH_FOCUS_DRAGGING || this.mEventFilter == EventFilter.TOUCH_CAPTURE_HOLDING || this.mEventFilter == EventFilter.PINCH_ZOOMING) {
            return true;
        }
        return false;
    }

    public boolean isSelfTimerRunning() {
        if (this.mEventFilter == EventFilter.SELF_TIMER_RUNNING) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isShutterKeyValid(int n) {
        if (!this.isCaptureValid()) return false;
        {
            switch (n) {
                case 24: 
                case 25: 
                case 66: 
                case 80: {
                    if (super.getKeyState() != KeyState.NON) return false;
                    return true;
                }
                default: {
                    return false;
                }
                case 27: 
            }
            if (super.getKeyState() != KeyState.AFDONE) return false;
            return true;
        }
    }

    public boolean isTouchEventValid() {
        if (this.mEventFilter == EventFilter.PREVIEWING || this.mEventFilter == EventFilter.TOUCH_FOCUS_DRAGGING || this.mEventFilter == EventFilter.TOUCH_CAPTURE_HOLDING || this.mEventFilter == EventFilter.SELF_TIMER_RUNNING) {
            return true;
        }
        return false;
    }

    public boolean isVolumeKeyValid() {
        if ((this.mEventFilter == EventFilter.PREVIEWING || this.mEventFilter == EventFilter.KEY_ZOOMING) && this.getKeyState() == KeyState.NON) {
            return true;
        }
        return false;
    }

    public void onAvailableSizeUpdated(long l) {
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onCaptureButtonEvent(int n, MotionEvent motionEvent) {
        if (!(this.isCaptureValid() && (n != 1 || this.mActivity.getControllerManager().getCapturingMode() != CapturingMode.NORMAL))) {
            return true;
        }
        if (n == 0) {
            this.mCaptureButtonGestureDetector.onTouchEvent(motionEvent);
        }
        ControllerEventSource controllerEventSource = ControllerEventSource.getButtonEvent(n);
        this.sendCaptureEvent(EventDispatcher.getEventAction(motionEvent.getAction()), controllerEventSource);
        return true;
    }

    public void onChangeFocusFace(String string, int n, int n2, int n3, int n4) {
        Executor.sendEvent(ControllerEvent.EV_FACE_DETECT_CHANGE, ControllerEventSource.TOUCH_FACE, -1, new NamedFace(string, string, new Rect(n, n2, n + n3, n2 + n4), -1));
        this.dispatch(EventAction.DOWN, n, n2, ControllerEventSource.TOUCH_FACE);
    }

    public void onDestinationToSaveChanged() {
        if (this.mActivity.getStorageManager().isReady()) {
            Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_MOUNTED);
            return;
        }
        Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_ERROR);
    }

    public void onDoubleCanceled() {
        this.dispatchZoomEvent(EventAction.UP, ControllerEventSource.TOUCH, ZoomDirection.NONE);
    }

    public void onDoubleMoved(Point point, Point point2) {
    }

    public void onDoubleRotated(float f, float f2) {
    }

    public void onDoubleScaled(float f, float f2, float f3) {
        float f4 = f - f2;
        ZoomDirection zoomDirection = ZoomDirection.IN_OUT;
        zoomDirection.setScaleLength(f4);
        this.dispatchZoomEvent(EventAction.STRETCH, ControllerEventSource.TOUCH, zoomDirection);
    }

    public void onDoubleTouched(Point point, Point point2) {
        if (this.isPinchZoomValid()) {
            this.dispatchZoomEvent(EventAction.DOWN, ControllerEventSource.TOUCH, ZoomDirection.NONE);
        }
    }

    public void onFaceRectLongPress(MotionEvent motionEvent) {
        this.sendLongPressEvent(ControllerEventSource.TOUCH_FACE);
    }

    public void onFaceRectTouchCancel(MotionEvent motionEvent) {
        this.dispatch(EventAction.CANCEL, (int)motionEvent.getRawX(), (int)motionEvent.getRawY(), ControllerEventSource.TOUCH_FACE);
    }

    public void onFaceRectTouchUp(MotionEvent motionEvent) {
        this.dispatch(EventAction.UP, (int)motionEvent.getRawX(), (int)motionEvent.getRawY(), ControllerEventSource.TOUCH_FACE);
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        KeyEventTranslator.TranslatedKeyCode translatedKeyCode = this.mKeyEventTranslator.translateKeyCodeOnDown(n);
        if (keyEvent.getRepeatCount() > 0 && translatedKeyCode != KeyEventTranslator.TranslatedKeyCode.VOLUME) {
            return true;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            case 8: {
                return true;
            }
            default: {
                return false;
            }
            case 1: {
                if (!this.isVolumeKeyValid()) return true;
                this.dispatchZoomEvent(EventAction.DOWN, ControllerEventSource.KEY, ZoomDirection.get(n, (ExtendedActivity)this.mActivity));
                return true;
            }
            case 2: {
                if (Executor.isRecording()) return true;
                return false;
            }
            case 3: {
                if (!this.isShutterKeyValid(n)) return true;
                this.sendCaptureEvent(EventAction.DOWN, ControllerEventSource.KEY);
                super.setKeyState(KeyState.AFDONE);
                return true;
            }
            case 4: {
                if (Configurations.isLogForOperatorsEnabled()) {
                    CameraLogger.dForOperators("The shutter key is pressed");
                }
                if (!this.isShutterKeyValid(n)) return true;
                if (super.shouldSelfTimerStart(ControllerEventSource.KEY)) {
                    this.sendSelfTimerEvent(EventAction.UP, ControllerEventSource.KEY, null, null);
                } else if (this.mSendLongPressEvent) {
                    this.sendLongPressEvent(ControllerEventSource.KEY);
                } else {
                    this.sendCaptureEvent(EventAction.UP, ControllerEventSource.KEY);
                }
                super.setKeyState(KeyState.SHUTTERDONE);
                return true;
            }
            case 5: 
            case 6: {
                if (!this.isShutterKeyValid(n)) return true;
                this.sendCaptureEvent(EventAction.DOWN, ControllerEventSource.KEY);
                if (super.shouldSelfTimerStart(ControllerEventSource.KEY)) {
                    this.sendSelfTimerEvent(EventAction.UP, ControllerEventSource.KEY, null, null);
                } else if (this.mSendLongPressEvent) {
                    this.sendLongPressEvent(ControllerEventSource.KEY);
                } else {
                    this.sendCaptureEvent(EventAction.UP, ControllerEventSource.KEY);
                }
                super.setKeyState(KeyState.SHUTTERDONE);
                return true;
            }
            case 7: {
                if (!Executor.isBackKeyValid()) return true;
                if (!this.isSelfTimerRunning()) return false;
                Executor.sendEvent(ControllerEvent.EV_SELFTIMER_CANCEL, ControllerEventSource.KEY);
                return true;
            }
            case 9: 
        }
        if (!this.isShutterKeyValid(n)) return true;
        this.sendCaptureEvent(EventAction.DOWN, ControllerEventSource.KEY);
        if (super.shouldSelfTimerStart(ControllerEventSource.KEY)) {
            this.sendSelfTimerEvent(EventAction.UP, ControllerEventSource.KEY, null, null);
        } else if (this.mSendLongPressEvent) {
            this.sendLongPressEvent(ControllerEventSource.KEY);
        } else {
            this.sendCaptureEvent(EventAction.UP, ControllerEventSource.KEY);
        }
        super.setKeyState(KeyState.SHUTTERDONE);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        boolean bl = true;
        KeyEventTranslator.TranslatedKeyCode translatedKeyCode = this.mKeyEventTranslator.translateKeyCodeOnUp(n);
        switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            default: {
                bl = false;
            }
            case 8: 
            case 10: {
                return bl;
            }
            case 1: {
                if (!this.isVolumeKeyValid()) return bl;
                this.dispatchZoomEvent(EventAction.UP, ControllerEventSource.KEY, ZoomDirection.get(n, (ExtendedActivity)this.mActivity));
                return bl;
            }
            case 2: {
                if (Executor.isRecording()) return bl;
                return false;
            }
            case 3: {
                switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventDispatcher$KeyState[super.getKeyState().ordinal()]) {
                    default: {
                        return bl;
                    }
                    case 1: {
                        this.sendCaptureEvent(EventAction.CANCEL, ControllerEventSource.KEY);
                        super.setKeyState(KeyState.NON);
                        return bl;
                    }
                    case 2: {
                        super.setKeyState(KeyState.NON);
                        return bl;
                    }
                    case 3: 
                }
                super.setKeyState(KeyState.NON);
                if (!this.mSendLongPressEvent) return bl;
                Executor.sendEvent(ControllerEvent.EV_BURST_STOP, ControllerEventSource.KEY);
                return bl;
            }
            case 4: {
                if (super.getKeyState() != KeyState.SHUTTERDONE) return bl;
                super.setKeyState(KeyState.CAPTURED);
                if (!this.mSendLongPressEvent) return bl;
                Executor.sendEvent(ControllerEvent.EV_BURST_STOP, ControllerEventSource.KEY);
                return bl;
            }
            case 5: 
            case 6: {
                if (super.getKeyState() != KeyState.SHUTTERDONE) return bl;
                super.setKeyState(KeyState.NON);
                if (!this.mSendLongPressEvent) return bl;
                Executor.sendEvent(ControllerEvent.EV_BURST_STOP, ControllerEventSource.KEY);
                return bl;
            }
            case 7: {
                super.resetKeyState(super.getKeyState());
                if (!Executor.isBackKeyValid()) return bl;
                if (!Executor.isRecording()) return false;
                Executor.sendEvent(ControllerEvent.EV_KEY_BACK, ControllerEventSource.KEY);
                return bl;
            }
            case 9: 
        }
        if (super.getKeyState() != KeyState.SHUTTERDONE) return bl;
        super.setKeyState(KeyState.NON);
        if (!this.mSendLongPressEvent) return bl;
        Executor.sendEvent(ControllerEvent.EV_BURST_STOP, ControllerEventSource.KEY);
        return bl;
    }

    public void onLongPress(MotionEvent motionEvent) {
        this.sendLongPressEvent(ControllerEventSource.TOUCH);
    }

    public void onOverTripleCanceled() {
        this.dispatchZoomEvent(EventAction.UP, ControllerEventSource.TOUCH, ZoomDirection.NONE);
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public void onSingleCanceled() {
        this.dispatchZoomEvent(EventAction.UP, ControllerEventSource.TOUCH, ZoomDirection.NONE);
    }

    public void onSingleMoved(Point point, Point point2, Point point3) {
    }

    public void onSingleReleased(Point point) {
        this.dispatchZoomEvent(EventAction.UP, ControllerEventSource.TOUCH, ZoomDirection.NONE);
    }

    public void onSingleReleasedInDouble(Point point, Point point2) {
    }

    public void onSingleStopped(Point point, Point point2, Point point3) {
    }

    public void onSingleTapUp(MotionEvent motionEvent) {
    }

    public void onSingleTouched(Point point) {
        if (this.isPinchZoomValid()) {
            this.mTouchDetectedTime = System.currentTimeMillis();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onStorageStateChanged(String string) {
        if (this.mActivity.getStorageManager().isReady()) {
            Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_MOUNTED);
        } else {
            Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_ERROR);
        }
        if (this.mActivity.hasExtraOutputPath()) {
            ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.FIXED);
            return;
        }
        if (this.mActivity.getStorageManager().isToggledStorageReady()) {
            ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.SELECTABLE);
            return;
        }
        ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.UNAVAILABLE);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bl = this.mTranslator.consumeTouchEvent() || this.isPinchZoomValid();
        if (!this.mActivity.isTouchEventValid()) {
            if (motionEvent.getActionMasked() != 3 || this.mTouchActionTranslator == null) return bl;
            {
                this.mTouchActionTranslator.onTouchEvent(motionEvent);
                return bl;
            }
        } else {
            if (super.consumedByZoom(motionEvent) || !this.isTouchEventValid()) return bl;
            {
                this.mTranslator.notifyTouchFocusDetector(motionEvent);
                this.dispatch(EventDispatcher.getEventAction(motionEvent.getAction()), (int)motionEvent.getRawX(), (int)motionEvent.getRawY(), ControllerEventSource.TOUCH);
                return bl;
            }
        }
    }

    public void onTouchMoveDetected(MotionEvent motionEvent) {
        this.dispatch(EventAction.MOVE, (int)motionEvent.getRawX(), (int)motionEvent.getRawY(), ControllerEventSource.TOUCH);
    }

    public void onTouchStopDetected(int n, int n2) {
        if (this.mEventFilter == EventFilter.TOUCH_FOCUS_DRAGGING) {
            this.dispatch(EventAction.STOP, n, n2, ControllerEventSource.TOUCH);
        }
    }

    public void preparePinchZoom(ControllerEventSource controllerEventSource) {
        if (System.currentTimeMillis() - this.mTouchDetectedTime < 1000) {
            Executor.sendEvent(ControllerEvent.EV_ZOOM_PREPARE, controllerEventSource);
        }
    }

    public void release() {
        if (this.mCameraWindow != null) {
            this.mCameraWindow.setTouchEventListener(null);
            this.mCameraWindow.setChangeFocusFaceListener(null);
        }
        this.mCameraWindow = null;
        this.mTouchFocusDetector.setMotionEventDetectorListener(null);
        if (this.mTouchActionTranslator != null) {
            this.mTouchActionTranslator.setInteractionListener(null);
        }
    }

    public void resetStatus() {
        if (this.mEventFilter == EventFilter.KEY_ZOOMING || this.mEventFilter == EventFilter.PINCH_ZOOMING) {
            return;
        }
        this.setKeyState(KeyState.NON);
        this.sendEventFilterMessage(7);
        this.setCurrentEventSource(null);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void sendCaptureEvent(EventAction var1, ControllerEventSource var2_2) {
        if (super.shouldSelfTimerStart(var2_2)) {
            this.sendSelfTimerEvent(var1, var2_2, null, null);
            return;
        }
        if (super.isExclusivedEventSource(var2_2) != false) return;
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[var1.ordinal()]) {
            case 1: {
                super.setCurrentEventSource(var2_2);
                Executor.sendEvent(ControllerEvent.EV_AF_START, var2_2, 0, (Object)var1);
                ** break;
            }
            case 2: {
                super.setCurrentEventSource(var2_2);
                Executor.sendEvent(ControllerEvent.EV_AF_START, var2_2, 0, (Object)var1);
                ** break;
            }
            case 3: {
                super.setCurrentEventSource(null);
                Executor.sendEvent(ControllerEvent.EV_AF_CANCEL, var2_2, 0, (Object)var1);
                ** break;
            }
            case 4: {
                super.setCurrentEventSource(null);
                switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[var2_2.ordinal()]) {
                    default: {
                        ** break;
                    }
                    case 1: {
                        Executor.postEvent(ControllerEvent.EV_CAPTURE, var2_2, 0, (Object)var1, null);
                        ** break;
                    }
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: {
                        Executor.sendEvent(ControllerEvent.EV_CAPTURE, var2_2, 0, (Object)var1);
                        ** break;
                    }
                    case 6: 
                }
                Executor.postEvent(ControllerEvent.EV_VIDEO_PAUSE_RESUME, var2_2, 0, (Object)var1, null);
            }
lbl31: // 8 sources:
            default: {
                ** GOTO lbl37
            }
            case 5: 
        }
        super.setCurrentEventSource(null);
        Executor.sendEvent(ControllerEvent.EV_AF_CANCEL, var2_2, 0, (Object)var1);
        Executor.sendEvent(ControllerEvent.EV_SELFTIMER_CANCEL, var2_2, 0, (Object)var1);
lbl37: // 2 sources:
        if (var2_2 != ControllerEventSource.PHOTO_BUTTON) return;
        this.mActivity.restartAutoOffTimer();
    }

    public void sendCaptureEventWithPosition(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect, ControllerEventSender controllerEventSender) {
        if (super.shouldSelfTimerStart(controllerEventSource)) {
            this.sendSelfTimerEvent(eventAction, controllerEventSource, rect, controllerEventSender);
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[eventAction.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                controllerEventSender.send(eventAction, controllerEventSource, rect);
                this.sendCaptureEvent(eventAction, controllerEventSource);
                return;
            }
            case 2: {
                controllerEventSender.send(eventAction, controllerEventSource, rect);
                this.sendCaptureEvent(eventAction, controllerEventSource);
                return;
            }
            case 3: {
                this.sendCaptureEvent(eventAction, controllerEventSource);
                controllerEventSender.send(eventAction, controllerEventSource, rect);
                return;
            }
            case 4: {
                this.sendCaptureEvent(eventAction, controllerEventSource);
                return;
            }
            case 5: 
        }
        this.sendCaptureEvent(eventAction, controllerEventSource);
        controllerEventSender.send(eventAction, controllerEventSource, rect);
    }

    public void sendLongPressEvent(ControllerEventSource controllerEventSource) {
        if (this.mSendLongPressEvent) {
            Executor.sendEvent(ControllerEvent.EV_BURST_START, controllerEventSource);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void sendSelfTimerEvent(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect, ControllerEventSender controllerEventSender) {
        if (this.mEventFilter != EventFilter.PREVIEWING && this.mEventFilter != EventFilter.TOUCH_FOCUS_DRAGGING && this.mEventFilter != EventFilter.TOUCH_CAPTURE_HOLDING) {
            return;
        }
        if (eventAction == EventAction.UP) {
            int n = this.mActivity.getParameterManager().getSelfTimerType(controllerEventSource);
            this.setShouldSelfTimerStart(false);
            Executor.sendEvent(ControllerEvent.EV_SELFTIMER_START, controllerEventSource, n, (Object)rect);
        }
        if (controllerEventSender == null) return;
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[eventAction.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                controllerEventSender.send(eventAction, controllerEventSource, rect);
                return;
            }
            case 3: {
                controllerEventSender.send(eventAction, controllerEventSource, rect);
                return;
            }
            case 5: 
        }
        controllerEventSender.send(eventAction, controllerEventSource, rect);
    }

    public void setSendLongPressEvent(boolean bl) {
        this.mSendLongPressEvent = bl;
    }

    public void setShouldSelfTimerStart(boolean bl) {
        this.mShouldSelfTimerStart = bl;
    }

    public void startTouchEventTranslation() {
        this.mTouchFocusDetector.setMotionEventDetectorListener((TouchFocusDetector.MotionEventDetectorListener)this);
        this.mTouchActionTranslator = new TouchActionTranslator((Context)this.mActivity, this.mCameraWindow.getTouchArea());
        this.mTouchActionTranslator.setInteractionListener((TouchActionTranslator.TouchActionListener)this);
        this.mCameraWindow.setTouchEventListener((View.OnTouchListener)this);
        this.mCameraWindow.setChangeFocusFaceListener((DetectedFaceRectangles.ChangeFocusFaceListener)this);
        this.mCameraWindow.enableTouchEvent();
    }

    public void stopTranslation() {
        this.setKeyState(KeyState.NON);
        this.sendEventFilterMessage(8);
    }

    public void updateLongPressStatus(int n, boolean bl) {
        if (this.mActivity.isOneShot()) {
            this.setSendLongPressEvent(false);
            return;
        }
        if (n == 2) {
            this.setSendLongPressEvent(false);
            return;
        }
        boolean bl2 = false;
        if (bl) {
            boolean bl3 = this.isSelfTimerRunning();
            bl2 = false;
            if (!bl3) {
                bl2 = true;
            }
        }
        this.setSendLongPressEvent(bl2);
    }

    public void updateSelfTimerStatus(boolean bl) {
        if (bl) {
            super.sendEventFilterMessage(0);
            if (this.mEventFilter == EventFilter.SELF_TIMER_RUNNING) {
                this.setSendLongPressEvent(false);
            }
            return;
        }
        super.sendEventFilterMessage(1);
    }

    public void updateTouchCaptureStatus(boolean bl) {
        if (bl) {
            super.sendEventFilterMessage(9);
            return;
        }
        super.sendEventFilterMessage(10);
    }

    /*
     * Exception decompiling
     */
    public void updateTouchEventTranslator(Parameters var1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:141)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:380)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:62)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:400)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    public void updateTouchFocusStatus(boolean bl) {
        if (bl) {
            super.sendEventFilterMessage(2);
            return;
        }
        super.sendEventFilterMessage(3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updateZoomStatus(boolean bl, ControllerEventSource controllerEventSource) {
        if (!bl) {
            super.sendEventFilterMessage(5);
            return;
        }
        if (controllerEventSource == ControllerEventSource.KEY) {
            super.sendEventFilterMessage(4);
            return;
        } else {
            if (controllerEventSource != ControllerEventSource.TOUCH) return;
            {
                super.sendEventFilterMessage(6);
                return;
            }
        }
    }

    static interface ControllerEventSender {
        public void send(EventAction var1, ControllerEventSource var2, Rect var3);
    }

    public static final class KeyState
    extends Enum<KeyState> {
        private static final /* synthetic */ KeyState[] $VALUES;
        public static final /* enum */ KeyState AFDONE;
        public static final /* enum */ KeyState CAPTURED;
        public static final /* enum */ KeyState NON;
        public static final /* enum */ KeyState SHUTTERDONE;

        static {
            NON = new KeyState();
            AFDONE = new KeyState();
            SHUTTERDONE = new KeyState();
            CAPTURED = new KeyState();
            KeyState[] arrkeyState = new KeyState[]{NON, AFDONE, SHUTTERDONE, CAPTURED};
            $VALUES = arrkeyState;
        }

        private KeyState() {
            super(string, n);
        }

        public static KeyState valueOf(String string) {
            return (KeyState)Enum.valueOf((Class)KeyState.class, (String)string);
        }

        public static KeyState[] values() {
            return (KeyState[])$VALUES.clone();
        }
    }

    static class NoEvent
    implements ControllerEventSender {
        NoEvent() {
        }

        @Override
        public void send(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
        }
    }

    static class ObjectTrackingEvent
    implements ControllerEventSender {
        ObjectTrackingEvent() {
        }

        @Override
        public void send(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[eventAction.ordinal()]) {
                default: {
                    return;
                }
                case 1: {
                    Executor.sendEvent(ControllerEvent.EV_OBJECT_TRACKING_START, controllerEventSource, 0, (Object)rect);
                    return;
                }
                case 5: 
            }
            Executor.sendEvent(ControllerEvent.EV_OBJECT_TRACKING_LOST, controllerEventSource);
        }
    }

    class TouchCaptureOff
    extends TouchCapturing {
        TouchCaptureOff() {
            super();
        }
    }

    class TouchCaptureOn
    extends TouchCapturing {
        TouchCaptureOn() {
            super();
        }

        @Override
        public void capture(EventAction eventAction, ControllerEventSource controllerEventSource) {
            EventDispatcher.this.sendCaptureEvent(eventAction, controllerEventSource);
        }

        @Override
        public boolean consumeTouchEvent() {
            return true;
        }

        @Override
        public void sendEventWithPosition(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            EventDispatcher.this.sendCaptureEventWithPosition(eventAction, controllerEventSource, rect, this.mSender);
        }
    }

    abstract class TouchCapturing {
        public ControllerEventSender mSender;

        TouchCapturing() {
            this.mSender = EventDispatcher.this.mNoEvent;
        }

        public void capture(EventAction eventAction, ControllerEventSource controllerEventSource) {
        }

        public boolean consumeTouchEvent() {
            return false;
        }

        public void sendEventWithPosition(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            if (EventDispatcher.this.isSelfTimerRunning()) {
                return;
            }
            this.mSender.send(eventAction, controllerEventSource, rect);
        }
    }

    class TouchCapturingOnlyEvent
    implements ControllerEventSender {
        TouchCapturingOnlyEvent() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void send(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            if (controllerEventSource != ControllerEventSource.TOUCH) return;
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[eventAction.ordinal()]) {
                default: {
                    return;
                }
                case 1: {
                    EventDispatcher.this.updateTouchCaptureStatus(true);
                    return;
                }
                case 5: 
            }
            EventDispatcher.this.updateTouchCaptureStatus(false);
        }
    }

    abstract class TouchEventTranslator {
        public TouchCapturing mTouchCapture;

        public TouchEventTranslator(TouchCapturing touchCapturing) {
            this.mTouchCapture = touchCapturing;
        }

        public abstract boolean consumeTouchEvent();

        public abstract void notifyTouchFocusDetector(MotionEvent var1);

        public abstract void translate(EventAction var1, ControllerEventSource var2, Rect var3);
    }

    static class TouchFocusEvent
    implements ControllerEventSender {
        TouchFocusEvent() {
        }

        @Override
        public void send(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$EventAction[eventAction.ordinal()]) {
                default: {
                    return;
                }
                case 1: {
                    Executor.sendEvent(ControllerEvent.EV_FOCUS_POSITION_START, controllerEventSource, 0, (Object)rect);
                    return;
                }
                case 2: {
                    Executor.sendEvent(ControllerEvent.EV_FOCUS_POSITION_CHANGE, controllerEventSource, 0, (Object)rect);
                    return;
                }
                case 3: {
                    Executor.sendEvent(ControllerEvent.EV_FOCUS_POSITION_CONTINUE, controllerEventSource, 0, (Object)rect);
                    return;
                }
                case 4: {
                    Executor.sendEvent(ControllerEvent.EV_FOCUS_POSITION_FINISH, controllerEventSource, 0, (Object)rect);
                    return;
                }
                case 5: 
            }
            Executor.sendEvent(ControllerEvent.EV_FOCUS_POSITION_CANCEL, controllerEventSource);
        }
    }

    class TouchFocusWheneverEvent
    implements ControllerEventSender {
        TouchFocusWheneverEvent() {
        }

        @Override
        public void send(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            if (controllerEventSource == ControllerEventSource.TOUCH) {
                EventDispatcher.this.mTouchFocus.send(eventAction, controllerEventSource, rect);
            }
        }
    }

    class TranslatorNoPosition
    extends TouchEventTranslator {
        public TranslatorNoPosition(TouchCapturing touchCapturing) {
            super(touchCapturing);
        }

        @Override
        public boolean consumeTouchEvent() {
            return this.mTouchCapture.consumeTouchEvent();
        }

        @Override
        public void notifyTouchFocusDetector(MotionEvent motionEvent) {
        }

        @Override
        public void translate(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            this.mTouchCapture.sendEventWithPosition(eventAction, controllerEventSource, null);
        }
    }

    class TranslatorWithPosition
    extends TouchEventTranslator {
        public TranslatorWithPosition(TouchCapturing touchCapturing) {
            super(touchCapturing);
        }

        @Override
        public boolean consumeTouchEvent() {
            return true;
        }

        @Override
        public void notifyTouchFocusDetector(MotionEvent motionEvent) {
            EventDispatcher.this.mTouchFocusDetector.onTouch(motionEvent);
        }

        @Override
        public void translate(EventAction eventAction, ControllerEventSource controllerEventSource, Rect rect) {
            this.mTouchCapture.sendEventWithPosition(eventAction, controllerEventSource, rect);
        }
    }

}

