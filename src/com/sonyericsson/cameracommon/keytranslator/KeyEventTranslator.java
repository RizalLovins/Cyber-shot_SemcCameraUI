/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.keytranslator;

import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.VolumeKey;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class KeyEventTranslator {
    private static String TAG = KeyEventTranslator.class.getSimpleName();
    private KeyType mCurrentKeyType = KeyType.NON;
    private final CommonSettings mSettings;

    public KeyEventTranslator(CommonSettings commonSettings) {
        this.mSettings = commonSettings;
    }

    private boolean isAvailableNow(TranslatedKeyCode translatedKeyCode, boolean bl) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            default: {
                return true;
            }
            case 1: {
                return super.isExpectedKeyType(bl, KeyType.NON, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.NON);
            }
            case 2: {
                return super.isExpectedKeyType(bl, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY);
            }
            case 3: {
                return super.isExpectedKeyType(bl, KeyType.NON, KeyType.VOLUME_UP_KEY, KeyType.VOLUME_UP_KEY, KeyType.NON);
            }
            case 4: 
        }
        return super.isExpectedKeyType(bl, KeyType.NON, KeyType.VOLUME_DOWN_KEY, KeyType.VOLUME_DOWN_KEY, KeyType.NON);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isExpectedKeyType(boolean bl, KeyType keyType, KeyType keyType2, KeyType keyType3, KeyType keyType4) {
        boolean bl2;
        if (bl) {
            KeyType keyType5 = this.mCurrentKeyType;
            bl2 = false;
            if (keyType5 != keyType) return bl2;
            this.mCurrentKeyType = keyType2;
            return true;
        }
        KeyType keyType6 = this.mCurrentKeyType;
        bl2 = false;
        if (keyType6 != keyType3) return bl2;
        this.mCurrentKeyType = keyType4;
        return true;
    }

    public TranslatedKeyCode translateKeyCode(int n) {
        switch (n) {
            default: {
                return TranslatedKeyCode.NON;
            }
            case 80: {
                return TranslatedKeyCode.FOCUS;
            }
            case 27: {
                return TranslatedKeyCode.SHUTTER;
            }
            case 24: 
            case 25: {
                VolumeKey volumeKey = (VolumeKey)this.mSettings.get(CommonSettingKey.VOLUME_KEY);
                switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$VolumeKey[volumeKey.ordinal()]) {
                    default: {
                        CameraLogger.e(TAG, "Volume key parameter is invalid state.");
                        return TranslatedKeyCode.ZOOM;
                    }
                    case 1: {
                        return TranslatedKeyCode.ZOOM;
                    }
                    case 2: {
                        return TranslatedKeyCode.VOLUME;
                    }
                    case 3: 
                }
                if (n == 24) {
                    return TranslatedKeyCode.FOCUS_AND_SHUTTER_UP_KEY;
                }
                return TranslatedKeyCode.FOCUS_AND_SHUTTER_DOWN_KEY;
            }
            case 4: {
                return TranslatedKeyCode.BACK;
            }
            case 82: {
                return TranslatedKeyCode.MENU;
            }
            case 66: 
        }
        return TranslatedKeyCode.ENTER;
    }

    public TranslatedKeyCode translateKeyCodeOnDown(int n) {
        TranslatedKeyCode translatedKeyCode = this.translateKeyCode(n);
        if (!super.isAvailableNow(translatedKeyCode, true)) {
            translatedKeyCode = TranslatedKeyCode.IGNORED;
        }
        return translatedKeyCode;
    }

    public TranslatedKeyCode translateKeyCodeOnUp(int n) {
        TranslatedKeyCode translatedKeyCode = this.translateKeyCode(n);
        if (!super.isAvailableNow(translatedKeyCode, false)) {
            translatedKeyCode = TranslatedKeyCode.IGNORED;
        }
        return translatedKeyCode;
    }

    private static final class KeyType
    extends Enum<KeyType> {
        private static final /* synthetic */ KeyType[] $VALUES;
        public static final /* enum */ KeyType CAMERA_KEY;
        public static final /* enum */ KeyType NON;
        public static final /* enum */ KeyType VOLUME_DOWN_KEY;
        public static final /* enum */ KeyType VOLUME_UP_KEY;

        static {
            NON = new KeyType();
            CAMERA_KEY = new KeyType();
            VOLUME_UP_KEY = new KeyType();
            VOLUME_DOWN_KEY = new KeyType();
            KeyType[] arrkeyType = new KeyType[]{NON, CAMERA_KEY, VOLUME_UP_KEY, VOLUME_DOWN_KEY};
            $VALUES = arrkeyType;
        }

        private KeyType() {
            super(string, n);
        }

        public static KeyType valueOf(String string) {
            return (KeyType)Enum.valueOf((Class)KeyType.class, (String)string);
        }

        public static KeyType[] values() {
            return (KeyType[])$VALUES.clone();
        }
    }

    public static final class TranslatedKeyCode
    extends Enum<TranslatedKeyCode> {
        private static final /* synthetic */ TranslatedKeyCode[] $VALUES;
        public static final /* enum */ TranslatedKeyCode BACK;
        public static final /* enum */ TranslatedKeyCode ENTER;
        public static final /* enum */ TranslatedKeyCode FOCUS;
        public static final /* enum */ TranslatedKeyCode FOCUS_AND_SHUTTER_DOWN_KEY;
        public static final /* enum */ TranslatedKeyCode FOCUS_AND_SHUTTER_UP_KEY;
        public static final /* enum */ TranslatedKeyCode IGNORED;
        public static final /* enum */ TranslatedKeyCode MENU;
        public static final /* enum */ TranslatedKeyCode NON;
        public static final /* enum */ TranslatedKeyCode SHUTTER;
        public static final /* enum */ TranslatedKeyCode VOLUME;
        public static final /* enum */ TranslatedKeyCode ZOOM;

        static {
            NON = new TranslatedKeyCode();
            ZOOM = new TranslatedKeyCode();
            VOLUME = new TranslatedKeyCode();
            FOCUS = new TranslatedKeyCode();
            SHUTTER = new TranslatedKeyCode();
            FOCUS_AND_SHUTTER_UP_KEY = new TranslatedKeyCode();
            FOCUS_AND_SHUTTER_DOWN_KEY = new TranslatedKeyCode();
            BACK = new TranslatedKeyCode();
            MENU = new TranslatedKeyCode();
            IGNORED = new TranslatedKeyCode();
            ENTER = new TranslatedKeyCode();
            TranslatedKeyCode[] arrtranslatedKeyCode = new TranslatedKeyCode[]{NON, ZOOM, VOLUME, FOCUS, SHUTTER, FOCUS_AND_SHUTTER_UP_KEY, FOCUS_AND_SHUTTER_DOWN_KEY, BACK, MENU, IGNORED, ENTER};
            $VALUES = arrtranslatedKeyCode;
        }

        private TranslatedKeyCode() {
            super(string, n);
        }

        public static TranslatedKeyCode valueOf(String string) {
            return (TranslatedKeyCode)Enum.valueOf((Class)TranslatedKeyCode.class, (String)string);
        }

        public static TranslatedKeyCode[] values() {
            return (TranslatedKeyCode[])$VALUES.clone();
        }
    }

}

