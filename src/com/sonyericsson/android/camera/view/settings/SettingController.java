/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnKeyListener
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.view.settings;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.android.camera.view.settings.SettingGroup;
import com.sonyericsson.android.camera.view.settings.SettingList;
import com.sonyericsson.android.camera.view.settings.SettingUtil;
import com.sonyericsson.android.camera.view.settings.executor.SettingExecutorFactory;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogController;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogStack;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogBasic;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabDialogBasic;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabs;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SettingController
implements SettingTabs.OnTabSelectedListener {
    private static final String TAG = SettingController.class.getSimpleName();
    private final CameraActivity mActivity;
    private final SettingDialogItemFactory mDialogItemFactory;
    private final View.OnKeyListener mInterceptSettingDialogKeyListener;
    private boolean mIsClosedTemporaryControlDialog = false;
    private KeyEventTranslator mKeyEventTranslator;
    private SettingDialogController mSettingDialogController;
    private final SettingDialogStack mSettingDialogStack;

    public SettingController(CameraActivity cameraActivity, SettingDialogStack settingDialogStack) {
        this.mSettingDialogController = new SettingDialogControllerImpl((SettingController)this, null);
        this.mInterceptSettingDialogKeyListener = new View.OnKeyListener(){

            /*
             * Exception decompiling
             */
            public boolean onKey(View var1, int var2_2, KeyEvent var3_3) {
                // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[CASE]], but top level block is 3[SWITCH]
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:392)
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:444)
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2783)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:764)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
                // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
                // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
                // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:631)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:714)
                // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
                // org.benf.cfr.reader.Main.doJar(Main.java:109)
                // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
                // java.lang.Thread.run(Thread.java:818)
                throw new IllegalStateException("Decompilation failed");
            }
        };
        this.mActivity = cameraActivity;
        this.mDialogItemFactory = new SettingDialogItemFactory();
        this.mSettingDialogStack = settingDialogStack;
        this.mKeyEventTranslator = new KeyEventTranslator(this.mActivity.getCommonSettings());
        this.mSettingDialogStack.setOnInterceptKeyListener(this.mInterceptSettingDialogKeyListener);
    }

    static /* synthetic */ KeyEventTranslator access$200(SettingController settingController) {
        return settingController.mKeyEventTranslator;
    }

    static /* synthetic */ CameraActivity access$300(SettingController settingController) {
        return settingController.mActivity;
    }

    /*
     * Exception decompiling
     */
    private boolean checkValueIsSelectable(ParameterKey var1_2, ParameterManager var2_4, ParameterValue var3_3, boolean var4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:436)
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

    /*
     * Enabled aggressive block sorting
     */
    private List<SettingItem> generateChildrenSettingItem(Context context, ParameterKey parameterKey, SettingGroup settingGroup, ParameterManager parameterManager, boolean bl) {
        ArrayList arrayList = new ArrayList();
        ParameterValue[] arrparameterValue = parameterManager.getOptions(parameterKey);
        ParameterValue parameterValue = parameterManager.get(parameterKey);
        for (ParameterValue parameterValue2 : arrparameterValue) {
            if (parameterValue2 == null) continue;
            SettingExecutorInterface<ParameterValue> settingExecutorInterface = SettingExecutorFactory.createSettingChangeExecutor(context, parameterKey, parameterValue2, parameterManager, this, bl);
            boolean bl2 = parameterValue == parameterValue2;
            boolean bl3 = this.checkValueIsSelectable(parameterKey, parameterManager, parameterValue2, bl2);
            arrayList.add((Object)SettingItemBuilder.build(parameterValue2).iconId(parameterValue2.getIconId()).textId(parameterValue2.getTextId()).dialogItemType(this.getChildDialogItemType(parameterKey, settingGroup)).executor(settingExecutorInterface).selected(bl2).selectable(bl3).commit());
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    private SettingAdapter generateGroupItemAdapter(SettingGroup settingGroup) {
        SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory);
        if (this.mActivity.isOneShotPhoto() && !settingGroup.equals((Object)SettingGroup.COMMON)) {
            settingGroup = SettingGroup.PHOTO;
        } else if (this.mActivity.isOneShotVideo() && !settingGroup.equals((Object)SettingGroup.COMMON)) {
            settingGroup = SettingGroup.VIDEO;
        }
        super.updateStorageSetting();
        for (ParameterKey parameterKey : settingGroup.getSettingItemList()) {
            if (!super.isVisible(parameterKey, settingGroup)) continue;
            settingAdapter.add((Object)super.generateParameterKeyItem(parameterKey, settingGroup, SettingExecutorFactory.createSelectMenuItemExecutor((Context)this.mActivity, parameterKey, settingGroup, (SettingController)this)));
        }
        return settingAdapter;
    }

    /*
     * Enabled aggressive block sorting
     */
    private SettingItem generateParameterKeyItem(ParameterKey parameterKey, SettingGroup settingGroup, SettingExecutorInterface<ParameterKey> settingExecutorInterface) {
        List<SettingItem> list = super.generateChildrenSettingItem((Context)this.mActivity, parameterKey, settingGroup, this.mActivity.getParameterManager(), false);
        SettingItem settingItem = super.getSelectedSettingItem(list);
        int n = -1;
        String string = "";
        if (settingItem != null) {
            n = settingItem.getIconId();
            string = settingItem.getText(this.mActivity.getResources());
            String string2 = settingItem.getSubText(this.mActivity.getResources());
            if (string2 != null) {
                string = string + " " + string2;
            }
        }
        boolean bl = parameterKey.isSelectable() || parameterKey == ParameterKey.DESTINATION_TO_SAVE;
        CapturingMode capturingMode = this.mActivity.getParameterManager().getParameters().capturingMode;
        SettingItemBuilder<ParameterKey> settingItemBuilder = SettingItemBuilder.build(parameterKey).iconId(n).textId(parameterKey.getTitleTextId(capturingMode)).text(parameterKey.getTitleText((Context)this.mActivity)).additionalTextForAccessibility(string).dialogItemType(super.getDialogItemType(parameterKey)).selectable(bl).executor(settingExecutorInterface);
        if (parameterKey != ParameterKey.AUTO_UPLOAD && parameterKey != ParameterKey.TOUCH_BLOCK) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                settingItemBuilder.item((SettingItem)iterator.next());
            }
        }
        return settingItemBuilder.commit();
    }

    private SettingAdapter generateShortcutItemAdapter() {
        SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory);
        Iterator iterator = SettingList.getSettingShortcutList(this.mActivity.getParameterManager().getParameters().capturingMode, (Activity)this.mActivity).iterator();
        while (iterator.hasNext()) {
            settingAdapter.add((Object)this.generateShortcutSettingItem((SettingList.Shortcut)iterator.next()));
        }
        return settingAdapter;
    }

    /*
     * Enabled aggressive block sorting
     */
    private SettingItem generateShortcutSettingItem(SettingList.Shortcut shortcut) {
        Parameters parameters = this.mActivity.getParameterManager().getParameters();
        CapturingMode capturingMode = parameters.capturingMode;
        switch (.$SwitchMap$com$sonyericsson$android$camera$view$settings$SettingList$Shortcut[shortcut.ordinal()]) {
            default: {
                throw new IllegalArgumentException("Shortcut type[ " + (Object)shortcut + "] is not supported.");
            }
            case 1: {
                Facing facing = parameters.getFacing();
                if (!super.isVisible(facing.getParameterKey(), null)) return SettingUtil.getBlankItem();
                return SettingItemBuilder.build(facing).iconId(2130837600).textId(facing.getTextId()).dialogItemType(0).selectable(ParameterKey.FACING.isSelectable()).executor(SettingExecutorFactory.createSettingChangeExecutor((Context)this.mActivity, ParameterKey.FACING, facing, this.mActivity.getParameterManager(), (SettingController)this)).commit();
            }
            case 2: {
                if (!super.isVisible(ParameterKey.FLASH, null) && !super.isVisible(ParameterKey.PHOTO_LIGHT, null)) return SettingUtil.getBlankItem();
                {
                    if (!(capturingMode == CapturingMode.VIDEO || this.mActivity.isOneShotVideo())) {
                        Flash flash = parameters.getFlash();
                        return SettingItemBuilder.build(shortcut.getGroup()).iconId(flash.getIconId()).textId(flash.getParameterKeyTextId()).dialogItemType(0).executor(SettingExecutorFactory.createSelectShortcutExecutor((SettingController)this, shortcut.getGroup())).selectable(flash.getParameterKey().isSelectable()).commit();
                    }
                    PhotoLight photoLight = parameters.getPhotoLight();
                    return SettingItemBuilder.build(shortcut.getGroup()).iconId(photoLight.getIconId()).textId(photoLight.getParameterKeyTextId()).dialogItemType(0).executor(SettingExecutorFactory.createSelectShortcutExecutor((SettingController)this, shortcut.getGroup())).selectable(photoLight.getParameterKey().isSelectable()).commit();
                }
            }
            case 3: {
                if (!super.isVisible(shortcut.getGroup())) return SettingUtil.getBlankItem();
                {
                    Ev ev = parameters.getEv();
                    WhiteBalance whiteBalance = parameters.getWhiteBalance();
                    int n = ev == Ev.ZERO && whiteBalance == WhiteBalance.AUTO ? 2130837582 : 2130837581;
                    String string = this.mActivity.getResources().getString(2131361850);
                    return SettingItemBuilder.build(shortcut.getGroup()).iconId(n).textId(ev.getParameterKeyTextId()).additionalTextForAccessibility(string).dialogItemType(0).executor(SettingExecutorFactory.createSelectShortcutExecutor((SettingController)this, shortcut.getGroup())).selected(this.mSettingDialogStack.isControlDialogOpened()).selectable(ev.getParameterKey().isSelectable()).commit();
                }
            }
            case 4: {
                Scene scene = parameters.getScene();
                if (!super.isVisible(scene.getParameterKey(), null)) return SettingUtil.getBlankItem();
                return SettingItemBuilder.build(ParameterKey.SCENE).iconId(scene.getIconId()).textId(scene.getParameterKeyTextId()).dialogItemType(0).executor(SettingExecutorFactory.createSelectShortcutExecutor((SettingController)this, ParameterKey.SCENE)).selectable(ParameterKey.SCENE.isSelectable()).commit();
            }
            case 5: {
                return SettingItemBuilder.build(SettingList.getDefaultTab(capturingMode)).iconId(2130837605).textId(2131361964).dialogItemType(6).executor(SettingExecutorFactory.createSelectMenuShortcutExecutor((SettingController)this)).selected(this.mSettingDialogStack.isMenuDialogOpened()).commit();
            }
            case 6: {
                SelfTimer selfTimer = parameters.getSelfTimer();
                if (super.isVisible(selfTimer.getParameterKey(), null)) return SettingItemBuilder.build(ParameterKey.SELF_TIMER).iconId(selfTimer.getIconId()).textId(selfTimer.getParameterKeyTextId()).dialogItemType(0).executor(SettingExecutorFactory.createSelectShortcutExecutor((SettingController)this, ParameterKey.SELF_TIMER)).selectable(ParameterKey.SELF_TIMER.isSelectable()).commit();
                return SettingUtil.getBlankItem();
            }
            case 7: 
        }
        return SettingUtil.getBlankItem();
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getChildDialogItemType(ParameterKey parameterKey, SettingGroup settingGroup) {
        int n = 1;
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                n = 2;
            }
            case 13: 
            case 14: 
            case 15: {
                return n;
            }
            case 16: {
                return 5;
            }
            case 17: {
                return 7;
            }
            case 18: {
                if (!super.isInMenuDialog(settingGroup)) return n;
                return 2;
            }
        }
    }

    private int getControlDialogItemType(ParameterKey parameterKey) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                return 2;
            }
            case 16: {
                return 5;
            }
            case 17: 
        }
        return 7;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int getDialogItemType(ParameterKey parameterKey) {
        int n = 4;
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                n = 3;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: {
                return n;
            }
            case 11: {
                if (!HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 7)) return n;
                return 3;
            }
            case 12: 
            case 13: 
        }
        return 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private SettingItem getSelectedSettingItem(List<SettingItem> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The specified list is empty.");
        }
        for (SettingItem settingItem : list) {
            if (!settingItem.isSelected() || !settingItem.isSelectable()) continue;
            return settingItem;
        }
        return (SettingItem)list.get(0);
    }

    /*
     * Enabled aggressive block sorting
     */
    private SettingTabs.Tab[] getTabs() {
        CapturingMode capturingMode = this.mActivity.getParameterManager().getParameters().capturingMode;
        if (this.mActivity.isOneShotPhoto()) {
            capturingMode = CapturingMode.NORMAL;
            return SettingList.getSettingTabList(capturingMode);
        }
        if (!this.mActivity.isOneShotVideo()) return SettingList.getSettingTabList(capturingMode);
        capturingMode = CapturingMode.VIDEO;
        return SettingList.getSettingTabList(capturingMode);
    }

    private boolean isInMenuDialog(SettingGroup settingGroup) {
        if (settingGroup == null) {
            return false;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$view$settings$SettingGroup[settingGroup.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
            case 2: 
            case 3: 
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isVisible(ParameterKey parameterKey, SettingGroup settingGroup) {
        ParameterSelectability parameterSelectability;
        if (this.mActivity.getParameterManager().getOptions(parameterKey).length <= 1 || ParameterKey.FAST_CAPTURE.equals((Object)parameterKey) && this.mActivity.isOneShot() || ParameterKey.FAST_CAPTURE.equals((Object)parameterKey) && !CommonUtility.isSystemApp((Context)this.mActivity) || ParameterKey.SELF_TIMER.equals((Object)parameterKey) && this.mActivity.isOneShotVideo() || super.isInMenuDialog(settingGroup) && ParameterKey.SELF_TIMER.equals((Object)parameterKey) && this.mActivity.getParameterManager().getParameters().capturingMode.isFront() || (parameterSelectability = parameterKey.getSelectability()) != ParameterSelectability.SELECTABLE && parameterSelectability != ParameterSelectability.UNAVAILABLE) {
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isVisible(SettingGroup settingGroup) {
        if (settingGroup == null) {
            return false;
        }
        ParameterKey[] arrparameterKey = settingGroup.getSettingItemList();
        int n = arrparameterKey.length;
        for (int i = 0; i < n; ++i) {
            if (!super.isVisible(arrparameterKey[i], settingGroup)) continue;
            return true;
        }
        return false;
    }

    private int itemCount(SettingGroup settingGroup) {
        int n = 0;
        if (settingGroup != null) {
            ParameterKey[] arrparameterKey = settingGroup.getSettingItemList();
            int n2 = arrparameterKey.length;
            for (int i = 0; i < n2; ++i) {
                if (!super.isVisible(arrparameterKey[i], settingGroup)) continue;
                ++n;
            }
        }
        return n;
    }

    private int menuDialogMaxItemCount() {
        CapturingMode capturingMode = this.mActivity.getParameterManager().getParameters().capturingMode;
        int n = this.itemCount(SettingGroup.COMMON);
        if (capturingMode == CapturingMode.NORMAL) {
            return Math.max((int)n, (int)this.itemCount(SettingGroup.PHOTO));
        }
        if (capturingMode == CapturingMode.VIDEO) {
            return Math.max((int)n, (int)this.itemCount(SettingGroup.VIDEO));
        }
        int n2 = this.itemCount(SettingGroup.PHOTO);
        int n3 = this.itemCount(SettingGroup.VIDEO);
        return Math.max((int)Math.max((int)n, (int)n2), (int)n3);
    }

    private void updateStorageSetting() {
        if (this.mActivity.hasExtraOutputPath()) {
            ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.FIXED);
            return;
        }
        if (this.mActivity.getStorageManager().isToggledStorageReady()) {
            ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.SELECTABLE);
            this.mActivity.getCommonSettings().setSelectability(CommonSettingKey.SAVE_DESTINATION, true);
            return;
        }
        ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.UNAVAILABLE);
        this.mActivity.getCommonSettings().setSelectability(CommonSettingKey.SAVE_DESTINATION, false);
    }

    public void clearShortcutTray() {
        this.mSettingDialogStack.updateShortcutTray(new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory));
    }

    public void closeDialogTemporary() {
        boolean bl = this.mSettingDialogStack.isControlDialogOpened();
        this.mSettingDialogStack.closeDialogs();
        this.mIsClosedTemporaryControlDialog = bl;
    }

    public void closeDialogs() {
        this.mSettingDialogStack.closeDialogs();
    }

    public void closeDialogsWithAnimation() {
        this.mSettingDialogStack.closeDialogs(true);
    }

    public CommonSettingKey findCommonSettingKeyShownBySettingDialog() {
        for (CommonSettingKey commonSettingKey : CommonSettingKey.values()) {
            if (!this.mSettingDialogStack.isOpened(commonSettingKey)) continue;
            return commonSettingKey;
        }
        return null;
    }

    public void forgetTemporaryClosedDialog() {
        this.mIsClosedTemporaryControlDialog = false;
    }

    public SettingAdapter generateParameterItemAdapter(ParameterKey parameterKey, SettingGroup settingGroup) {
        SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory);
        settingAdapter.addAll(super.generateChildrenSettingItem((Context)this.mActivity, parameterKey, settingGroup, this.mActivity.getParameterManager(), true));
        return settingAdapter;
    }

    public SettingDialogController getSettingDialogController() {
        return this.mSettingDialogController;
    }

    public SettingDialogStack getSettingDialogStack() {
        return this.mSettingDialogStack;
    }

    public void hideShortcutTray() {
        this.mSettingDialogStack.hideShortcutTray();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onCaptureButtonEvent(int n, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            default: {
                do {
                    return false;
                    break;
                } while (true);
            }
            case 0: 
        }
        this.mSettingDialogStack.closeDialogs();
        return false;
    }

    @Override
    public void onTabSelected(SettingTabs.Tab tab) {
        this.updateSettingMenu(true);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void openControlDialog(SettingGroup settingGroup) {
        if (!ControllerManager.isMenuAvailable()) {
            this.mSettingDialogStack.clearShortcutSelected();
            return;
        }
        this.mSettingDialogStack.updateShortcutSelected(settingGroup);
        SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory);
        ParameterKey[] arrparameterKey = settingGroup.getSettingItemList();
        int n = arrparameterKey.length;
        int n2 = 0;
        do {
            if (n2 >= n) {
                if (settingAdapter.getCount() == 0) return;
                if (this.mSettingDialogStack.openControlDialog(settingAdapter)) return;
                this.mSettingDialogStack.closeCurrentDialog();
                return;
            }
            ParameterKey parameterKey = arrparameterKey[n2];
            if (super.isVisible(parameterKey, settingGroup)) {
                SettingItemBuilder<ParameterKey> settingItemBuilder = SettingItemBuilder.build(parameterKey).dialogItemType(super.getControlDialogItemType(parameterKey)).selectable(parameterKey.isSelectable());
                ParameterManager parameterManager = this.mActivity.getParameterManager();
                Iterator iterator = super.generateChildrenSettingItem((Context)this.mActivity, parameterKey, settingGroup, parameterManager, false).iterator();
                while (iterator.hasNext()) {
                    settingItemBuilder.item((SettingItem)iterator.next());
                }
                settingAdapter.add((Object)settingItemBuilder.commit());
            }
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void openMenuDialog(SettingGroup settingGroup) {
        if (!ControllerManager.isMenuAvailable()) {
            this.mSettingDialogStack.clearShortcutSelected();
            return;
        } else {
            if (!this.mSettingDialogStack.isMenuDialogOpened()) {
                this.mSettingDialogStack.updateShortcutSelected(settingGroup);
            }
            SettingAdapter settingAdapter = super.generateGroupItemAdapter(settingGroup);
            boolean bl = this.mSettingDialogStack.openMenuDialog(settingAdapter, super.getTabs(), (SettingTabs.OnTabSelectedListener)this, null, super.menuDialogMaxItemCount());
            if (this.mSettingDialogStack.getSecondLayerDialog() != null) {
                this.mSettingDialogStack.closeCurrentDialog();
            }
            if (bl) return;
            {
                this.mSettingDialogStack.closeCurrentDialog();
                return;
            }
        }
    }

    public void openSecondLayerDialog(SettingAdapter settingAdapter, Object object) {
        this.mSettingDialogStack.openSecondLayerDialog(settingAdapter, object);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void openShortcutDialog(ParameterKey parameterKey) {
        if (!ControllerManager.isMenuAvailable()) {
            this.mSettingDialogStack.clearShortcutSelected();
            return;
        } else {
            if (this.getSettingDialogStack().openShortcutDialog(this.generateParameterItemAdapter(parameterKey, null), parameterKey.getTitleTextId(this.mActivity.getParameterManager().getParameters().capturingMode))) return;
            {
                this.mSettingDialogStack.closeCurrentDialog();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void openShortcutDialog(SettingGroup settingGroup) {
        if (!ControllerManager.isMenuAvailable()) {
            this.mSettingDialogStack.clearShortcutSelected();
            return;
        }
        ParameterKey parameterKey = null;
        SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory);
        for (ParameterKey parameterKey2 : settingGroup.getSettingItemList()) {
            if (!super.isVisible(parameterKey2, settingGroup)) continue;
            if (parameterKey == null) {
                parameterKey = parameterKey2;
            }
            Iterator iterator = super.generateChildrenSettingItem((Context)this.mActivity, parameterKey2, settingGroup, this.mActivity.getParameterManager(), true).iterator();
            while (iterator.hasNext()) {
                settingAdapter.add((Object)((SettingItem)iterator.next()));
            }
        }
        int n = parameterKey != null ? parameterKey.getTitleTextId(this.mActivity.getParameterManager().getParameters().capturingMode) : settingGroup.getTextId();
        if (this.mSettingDialogStack.openShortcutDialog(settingAdapter, n)) return;
        this.mSettingDialogStack.closeCurrentDialog();
    }

    public void reopenTemporaryClosedDialog() {
        if (this.mIsClosedTemporaryControlDialog) {
            this.updateShortcutTray();
            this.openControlDialog(SettingGroup.CONTROL);
            this.mIsClosedTemporaryControlDialog = false;
        }
    }

    public void setSensorOrientation(int n) {
        this.mSettingDialogStack.setUiOrientation(n);
    }

    public void showShortcutTray() {
        this.mSettingDialogStack.showShortcutTray();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updatSecondLayerDialog(boolean bl) {
        SettingAdapter settingAdapter;
        SettingDialogBasic settingDialogBasic = this.mSettingDialogStack.getSecondLayerDialog();
        if (settingDialogBasic == null || (settingAdapter = settingDialogBasic.getAdapter()) == null) {
            return;
        }
        settingAdapter.clear();
        SettingAdapter settingAdapter2 = null;
        for (ParameterKey parameterKey : ParameterKey.values()) {
            if (!this.mSettingDialogStack.isOpened((Object)parameterKey)) continue;
            settingAdapter2 = this.generateParameterItemAdapter(parameterKey, null);
        }
        if (settingAdapter2 != null) {
            for (int i = 0; i < settingAdapter2.getCount(); ++i) {
                settingAdapter.add(settingAdapter2.getItem(i));
            }
        }
        if (bl) {
            settingAdapter.notifyDataSetInvalidated();
            return;
        }
        settingAdapter.notifyDataSetChanged();
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void updateSettingMenu(boolean bl) {
        SettingGroup settingGroup;
        SettingTabDialogBasic settingTabDialogBasic = this.mSettingDialogStack.getMenuDialog();
        if (settingTabDialogBasic == null) {
            return;
        }
        SettingAdapter settingAdapter = settingTabDialogBasic.getAdapter();
        SettingTabs.Tab tab = settingTabDialogBasic.getSelectedTab();
        if (settingAdapter == null) return;
        if (tab == null) return;
        super.updateStorageSetting();
        settingAdapter.clear();
        switch (.$SwitchMap$com$sonyericsson$cameracommon$setting$dialog$SettingTabs$Tab[tab.ordinal()]) {
            default: {
                settingGroup = null;
                break;
            }
            case 1: {
                settingGroup = SettingGroup.PHOTO;
                break;
            }
            case 2: {
                settingGroup = SettingGroup.VIDEO;
                break;
            }
            case 3: {
                settingGroup = SettingGroup.COMMON;
            }
        }
        if (settingGroup != null) {
            for (ParameterKey parameterKey : settingGroup.getSettingItemList()) {
                if (!super.isVisible(parameterKey, settingGroup)) continue;
                settingAdapter.add((Object)super.generateParameterKeyItem(parameterKey, settingGroup, SettingExecutorFactory.createSelectMenuItemExecutor((Context)this.mActivity, parameterKey, settingGroup, (SettingController)this)));
            }
        }
        if (bl) {
            settingAdapter.notifyDataSetInvalidated();
            return;
        }
        settingAdapter.notifyDataSetChanged();
    }

    public void updateShortcutTray() {
        this.mSettingDialogStack.clearShortcutSelected();
        this.mSettingDialogStack.updateShortcutTray(this.generateShortcutItemAdapter());
    }

    private static class SettingDialogControllerImpl
    implements SettingDialogController {
        SettingController mController;

        private SettingDialogControllerImpl(SettingController settingController) {
            this.mController = settingController;
        }

        /* synthetic */ SettingDialogControllerImpl(SettingController settingController,  var2_2) {
            super(settingController);
        }

        @Override
        public void closeCurrentDialog() {
            this.mController.mSettingDialogStack.closeCurrentDialog();
        }

        @Override
        public void closeDialogs(boolean bl) {
            this.mController.mSettingDialogStack.closeDialogs(bl);
        }

        @Override
        public void openSecondLayerDialog(SettingAdapter settingAdapter, Object object) {
            this.mController.openSecondLayerDialog(settingAdapter, object);
        }
    }

}

