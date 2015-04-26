/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import java.util.List;

public class LedOptionsResolver {
    private static LedOptionsResolver sInstance = new LedOptionsResolver();
    private Resolver mResolver;

    private LedOptionsResolver() {
        this.mResolver = new Unsolved(this, null);
    }

    public static LedOptionsResolver getInstance() {
        return sInstance;
    }

    public Flash getDefaultFlash() {
        return this.mResolver.getDefaultFlash();
    }

    public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
        if (list == null || list.size() == 0) {
            return new Flash[0];
        }
        return this.mResolver.getFlashOptions(actionMode, list);
    }

    public int getParameterKeyTextId() {
        return this.mResolver.getParameterKeyTextId();
    }

    public int getParameterKeyTitleTextId() {
        return this.mResolver.getParameterKeyTitleTextId();
    }

    public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
        return this.mResolver.getPhotoLightOptions(actionMode, list);
    }

    private class FlashIn
    extends Resolver {
        final /* synthetic */ LedOptionsResolver this$0;

        private FlashIn(LedOptionsResolver ledOptionsResolver) {
            this.this$0 = ledOptionsResolver;
            super(ledOptionsResolver, null);
        }

        /* synthetic */ FlashIn(LedOptionsResolver ledOptionsResolver,  var2_2) {
            super(ledOptionsResolver);
        }

        @Override
        public Flash getDefaultFlash() {
            return Flash.AUTO;
        }

        @Override
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return new Flash[0];
            }
            Flash[] arrflash = new Flash[]{Flash.AUTO, Flash.ON, Flash.RED_EYE, Flash.OFF, Flash.LED_ON};
            return arrflash;
        }

        @Override
        public int getParameterKeyTextId() {
            return 2131361818;
        }

        @Override
        public int getParameterKeyTitleTextId() {
            return 2131361921;
        }

        @Override
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return PhotoLight.values();
            }
            return new PhotoLight[0];
        }
    }

    private class FlashNotSupported
    extends PhotoLightIn {
        final /* synthetic */ LedOptionsResolver this$0;

        private FlashNotSupported(LedOptionsResolver ledOptionsResolver) {
            this.this$0 = ledOptionsResolver;
            super(ledOptionsResolver, null);
        }

        /* synthetic */ FlashNotSupported(LedOptionsResolver ledOptionsResolver,  var2_2) {
            super(ledOptionsResolver);
        }

        @Override
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return new Flash[0];
            }
            Flash[] arrflash = new Flash[]{Flash.PHOTO_LIGHT_ON_AS_FLASH, Flash.LED_OFF};
            return arrflash;
        }

        @Override
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            if (actionMode.mType == 2) {
                return PhotoLight.values();
            }
            return new PhotoLight[0];
        }
    }

    private class PhotoLightIn
    extends Resolver {
        final /* synthetic */ LedOptionsResolver this$0;

        private PhotoLightIn(LedOptionsResolver ledOptionsResolver) {
            this.this$0 = ledOptionsResolver;
            super(ledOptionsResolver, null);
        }

        /* synthetic */ PhotoLightIn(LedOptionsResolver ledOptionsResolver,  var2_2) {
            super(ledOptionsResolver);
        }

        @Override
        public Flash getDefaultFlash() {
            return Flash.LED_OFF;
        }

        @Override
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            return new Flash[0];
        }

        @Override
        public int getParameterKeyTextId() {
            return 2131362016;
        }

        @Override
        public int getParameterKeyTitleTextId() {
            return 2131362016;
        }

        @Override
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            return PhotoLight.values();
        }
    }

    private abstract class Resolver {
        final /* synthetic */ LedOptionsResolver this$0;

        private Resolver(LedOptionsResolver ledOptionsResolver) {
            this.this$0 = ledOptionsResolver;
        }

        /* synthetic */ Resolver(LedOptionsResolver ledOptionsResolver,  var2_2) {
            super(ledOptionsResolver);
        }

        public abstract Flash getDefaultFlash();

        public abstract Flash[] getFlashOptions(ActionMode var1, List<String> var2);

        public abstract int getParameterKeyTextId();

        public abstract int getParameterKeyTitleTextId();

        public abstract PhotoLight[] getPhotoLightOptions(ActionMode var1, List<String> var2);
    }

    private class Unsolved
    extends Resolver {
        final /* synthetic */ LedOptionsResolver this$0;

        private Unsolved(LedOptionsResolver ledOptionsResolver) {
            this.this$0 = ledOptionsResolver;
            super(ledOptionsResolver, null);
        }

        /* synthetic */ Unsolved(LedOptionsResolver ledOptionsResolver,  var2_2) {
            super(ledOptionsResolver);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private Resolver getResolver(int n, List<String> list) {
            if (list == null || list.isEmpty()) return null;
            for (String string : list) {
                if (Flash.ON.getValue().equals((Object)string)) {
                    return new FlashIn(this.this$0, null);
                }
                if (!Flash.LED_ON.getValue().equals((Object)string)) continue;
                if (n != 1) return new PhotoLightIn(this.this$0, null);
                return new FlashNotSupported(this.this$0, null);
            }
            return null;
        }

        @Override
        public Flash getDefaultFlash() {
            Resolver resolver = this.getResolver(1, com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)0).FLASH.get());
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getDefaultFlash();
            }
            return Flash.LED_OFF;
        }

        @Override
        public Flash[] getFlashOptions(ActionMode actionMode, List<String> list) {
            Resolver resolver = super.getResolver(actionMode.mType, list);
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getFlashOptions(actionMode, list);
            }
            return new Flash[0];
        }

        @Override
        public int getParameterKeyTextId() {
            return -1;
        }

        @Override
        public int getParameterKeyTitleTextId() {
            return -1;
        }

        @Override
        public PhotoLight[] getPhotoLightOptions(ActionMode actionMode, List<String> list) {
            Resolver resolver = super.getResolver(actionMode.mType, list);
            if (resolver != null) {
                this.this$0.mResolver = resolver;
                return this.this$0.mResolver.getPhotoLightOptions(actionMode, list);
            }
            return new PhotoLight[0];
        }
    }

}

