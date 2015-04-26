/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.EnumMap
 *  java.util.List
 *  java.util.Map
 */
package com.sonyericsson.android.camera.view.settings;

import android.app.Activity;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.view.settings.SettingGroup;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabs;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import com.sonyericsson.cameracommon.utility.StaticConfigurationUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SettingList {
    private static final Shortcut[] AUTO;
    private static final Shortcut[] AUTO_TABLET;
    private static final Shortcut[] FRONT;
    private static final Shortcut[] FRONT_FLASH_SUPPORTED;
    private static final Shortcut[] FRONT_TABLET;
    public static final int LIST_APPEND = 1;
    public static final int LIST_REMOVE;
    private static final Shortcut[] PROGRAM_PHOTO;
    private static final Shortcut[] PROGRAM_PHOTO_TABLET;
    private static final Shortcut[] PROGRAM_VIDEO;
    private static final Shortcut[] PROGRAM_VIDEO_TABLET;
    private static Map<SettingGroup, List<ParameterKey>> sDisplayingParameterKeyMap;

    /*
     * Enabled aggressive block sorting
     */
    static {
        Shortcut[] arrshortcut = new Shortcut[]{Shortcut.BLANK, Shortcut.BLANK, Shortcut.FLASH_LIGHT, Shortcut.FACING, Shortcut.MENU};
        AUTO = arrshortcut;
        Shortcut[] arrshortcut2 = new Shortcut[]{Shortcut.SCENE, Shortcut.CONTROL, Shortcut.FLASH_LIGHT, Shortcut.FACING, Shortcut.MENU};
        PROGRAM_PHOTO = arrshortcut2;
        Shortcut[] arrshortcut3 = new Shortcut[]{Shortcut.SCENE, Shortcut.CONTROL, Shortcut.FLASH_LIGHT, Shortcut.FACING, Shortcut.MENU};
        PROGRAM_VIDEO = arrshortcut3;
        Shortcut[] arrshortcut4 = new Shortcut[]{Shortcut.BLANK, Shortcut.BLANK, Shortcut.SELF_TIMER, Shortcut.FACING, Shortcut.MENU};
        FRONT = arrshortcut4;
        Shortcut[] arrshortcut5 = new Shortcut[]{Shortcut.BLANK, Shortcut.SELF_TIMER, Shortcut.FLASH_LIGHT, Shortcut.FACING, Shortcut.MENU};
        FRONT_FLASH_SUPPORTED = arrshortcut5;
        Shortcut[] arrshortcut6 = new Shortcut[]{Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.FACING, Shortcut.MENU};
        AUTO_TABLET = arrshortcut6;
        Shortcut[] arrshortcut7 = new Shortcut[]{Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.SCENE, Shortcut.CONTROL, Shortcut.FACING, Shortcut.MENU};
        PROGRAM_PHOTO_TABLET = arrshortcut7;
        Shortcut[] arrshortcut8 = new Shortcut[]{Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.SCENE, Shortcut.CONTROL, Shortcut.FACING, Shortcut.MENU};
        PROGRAM_VIDEO_TABLET = arrshortcut8;
        Shortcut[] arrshortcut9 = new Shortcut[]{Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.BLANK, Shortcut.SELF_TIMER, Shortcut.FACING, Shortcut.MENU};
        FRONT_TABLET = arrshortcut9;
        sDisplayingParameterKeyMap = new EnumMap((Class)SettingGroup.class);
        for (SettingGroup settingGroup : SettingGroup.values()) {
            ParameterKey[] arrparameterKey = settingGroup.getSettingItemList();
            ArrayList arrayList = new ArrayList();
            block5 : for (ParameterKey parameterKey : arrparameterKey) {
                switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
                    default: {
                        arrayList.add((Object)parameterKey);
                        continue block5;
                    }
                    case 1: {
                        if (StaticConfigurationUtil.isForceSound()) continue block5;
                        arrayList.add((Object)parameterKey);
                        continue block5;
                    }
                    case 2: {
                        arrayList.add((Object)parameterKey);
                    }
                }
            }
            sDisplayingParameterKeyMap.put((Object)settingGroup, (Object)arrayList);
        }
    }

    public static SettingGroup getDefaultTab(CapturingMode capturingMode) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return SettingGroup.COMMON;
            }
            case 1: 
            case 2: 
            case 4: 
            case 5: {
                return SettingGroup.PHOTO;
            }
            case 3: 
            case 6: 
        }
        return SettingGroup.VIDEO;
    }

    public static List<ParameterKey> getSettingGroupItemListList(SettingGroup settingGroup) {
        return (List)sDisplayingParameterKeyMap.get((Object)settingGroup);
    }

    public static List<Shortcut> getSettingShortcutList(CapturingMode capturingMode, Activity activity) {
        if (ProductConfig.isTablet((Context)activity)) {
            switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                default: {
                    return Arrays.asList((Object[])AUTO_TABLET);
                }
                case 1: {
                    return Arrays.asList((Object[])AUTO_TABLET);
                }
                case 2: {
                    return Arrays.asList((Object[])PROGRAM_PHOTO_TABLET);
                }
                case 3: {
                    return Arrays.asList((Object[])PROGRAM_VIDEO_TABLET);
                }
                case 4: 
                case 5: 
                case 6: 
            }
            return Arrays.asList((Object[])FRONT_TABLET);
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return Arrays.asList((Object[])AUTO);
            }
            case 1: {
                return Arrays.asList((Object[])AUTO);
            }
            case 2: {
                return Arrays.asList((Object[])PROGRAM_PHOTO);
            }
            case 3: {
                return Arrays.asList((Object[])PROGRAM_VIDEO);
            }
            case 4: 
            case 5: 
            case 6: 
        }
        if (Flash.isSupported(capturingMode.getCameraId())) {
            return Arrays.asList((Object[])FRONT_FLASH_SUPPORTED);
        }
        return Arrays.asList((Object[])FRONT);
    }

    public static SettingTabs.Tab[] getSettingTabList(CapturingMode capturingMode) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                SettingTabs.Tab[] arrtab = new SettingTabs.Tab[]{SettingTabs.Tab.Photo, SettingTabs.Tab.Video, SettingTabs.Tab.Common};
                return arrtab;
            }
            case 1: 
            case 4: 
            case 5: {
                SettingTabs.Tab[] arrtab = new SettingTabs.Tab[]{SettingTabs.Tab.Photo, SettingTabs.Tab.Video, SettingTabs.Tab.Common};
                return arrtab;
            }
            case 2: {
                SettingTabs.Tab[] arrtab = new SettingTabs.Tab[]{SettingTabs.Tab.Photo, SettingTabs.Tab.Common};
                return arrtab;
            }
            case 3: 
            case 6: 
        }
        SettingTabs.Tab[] arrtab = new SettingTabs.Tab[]{SettingTabs.Tab.Video, SettingTabs.Tab.Common};
        return arrtab;
    }

    public static void updateList(ParameterKey parameterKey, int n) {
    }

    public static final class Shortcut
    extends Enum<Shortcut> {
        private static final /* synthetic */ Shortcut[] $VALUES;
        public static final /* enum */ Shortcut BLANK;
        public static final /* enum */ Shortcut CONTROL;
        public static final /* enum */ Shortcut FACING;
        public static final /* enum */ Shortcut FLASH_LIGHT;
        public static final /* enum */ Shortcut MENU;
        public static final /* enum */ Shortcut SCENE;
        public static final /* enum */ Shortcut SELF_TIMER;
        private final SettingGroup mGroup;

        static {
            FACING = new Shortcut(null);
            FLASH_LIGHT = new Shortcut(SettingGroup.FLASH_LIGHT);
            SCENE = new Shortcut(null);
            CONTROL = new Shortcut(SettingGroup.CONTROL);
            MENU = new Shortcut(null);
            SELF_TIMER = new Shortcut(null);
            BLANK = new Shortcut(null);
            Shortcut[] arrshortcut = new Shortcut[]{FACING, FLASH_LIGHT, SCENE, CONTROL, MENU, SELF_TIMER, BLANK};
            $VALUES = arrshortcut;
        }

        private Shortcut(SettingGroup settingGroup) {
            super(string, n);
            this.mGroup = settingGroup;
        }

        public static Shortcut valueOf(String string) {
            return (Shortcut)Enum.valueOf((Class)Shortcut.class, (String)string);
        }

        public static Shortcut[] values() {
            return (Shortcut[])$VALUES.clone();
        }

        public SettingGroup getGroup() {
            return this.mGroup;
        }
    }

}

