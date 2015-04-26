/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.device;

public class SizeConstants {
    public static final int HEIGHT_10MP = 2322;
    public static final int HEIGHT_12MP = 3000;
    public static final int HEIGHT_12MP_HDR = 2940;
    public static final int HEIGHT_13MP = 3096;
    public static final int HEIGHT_15MP_WIDE = 2952;
    public static final int HEIGHT_1MP = 960;
    public static final int HEIGHT_1MP_NARROW = 720;
    public static final int HEIGHT_20MP = 3936;
    public static final int HEIGHT_20MP_HDR = 3748;
    public static final int HEIGHT_20MP_WIDE_HDR = 2810;
    public static final int HEIGHT_2MP = 1224;
    public static final int HEIGHT_2MP_HDR = 1140;
    public static final int HEIGHT_2MP_WIDE = 1080;
    public static final int HEIGHT_2MP_WIDE_HDR = 1026;
    public static final int HEIGHT_3MP = 1536;
    public static final int HEIGHT_3MP_WIDE = 1440;
    public static final int HEIGHT_3_7MP_WIDE = 1458;
    public static final int HEIGHT_5MP = 1944;
    public static final int HEIGHT_5MP_WIDE = 1746;
    public static final int HEIGHT_6MP = 1836;
    public static final int HEIGHT_6MP_HDR = 1746;
    public static final int HEIGHT_7MP_HDR = 2328;
    public static final int HEIGHT_8MP = 2448;
    public static final int HEIGHT_8MP_WIDE = 2160;
    public static final int HEIGHT_9MP = 2250;
    public static final int HEIGHT_9MP_HDR = 2204;
    public static final int HEIGHT_PREVIEW_4K_UHD = 2160;
    public static final int HEIGHT_PREVIEW_FULL_HD = 1080;
    public static final int HEIGHT_PREVIEW_FWVGA = 480;
    public static final int HEIGHT_PREVIEW_HD = 720;
    public static final int HEIGHT_PREVIEW_HVGA = 320;
    public static final int HEIGHT_PREVIEW_QCIF = 144;
    public static final int HEIGHT_PREVIEW_QHD = 540;
    public static final int HEIGHT_PREVIEW_QVGA = 240;
    public static final int HEIGHT_PREVIEW_UXGA = 1200;
    public static final int HEIGHT_PREVIEW_VGA = 480;
    public static final int HEIGHT_PREVIEW_WQHD = 1440;
    public static final int HEIGHT_PREVIEW_WQXGA = 1600;
    public static final int HEIGHT_UXGA = 1200;
    public static final int HEIGHT_VGA = 480;
    public static final int WIDTH_10MP = 4128;
    public static final int WIDTH_12MP = 4000;
    public static final int WIDTH_12MP_HDR = 3920;
    public static final int WIDTH_13MP = 4128;
    public static final int WIDTH_15MP_WIDE = 5248;
    public static final int WIDTH_1MP = 1280;
    public static final int WIDTH_1MP_NARROW = 1280;
    public static final int WIDTH_20MP = 5248;
    public static final int WIDTH_20MP_HDR = 4998;
    public static final int WIDTH_20MP_WIDE_HDR = 4998;
    public static final int WIDTH_2MP = 1632;
    public static final int WIDTH_2MP_HDR = 1520;
    public static final int WIDTH_2MP_WIDE = 1920;
    public static final int WIDTH_2MP_WIDE_HDR = 1824;
    public static final int WIDTH_3MP = 2048;
    public static final int WIDTH_3MP_WIDE = 2560;
    public static final int WIDTH_3_7MP_WIDE = 2592;
    public static final int WIDTH_5MP = 2592;
    public static final int WIDTH_5MP_WIDE = 3104;
    public static final int WIDTH_6MP = 3264;
    public static final int WIDTH_6MP_HDR = 3104;
    public static final int WIDTH_7MP_HDR = 3104;
    public static final int WIDTH_8MP = 3264;
    public static final int WIDTH_8MP_WIDE = 3840;
    public static final int WIDTH_9MP = 4000;
    public static final int WIDTH_9MP_HDR = 3920;
    public static final int WIDTH_PREVIEW_4K_UHD = 3840;
    public static final int WIDTH_PREVIEW_FULL_HD = 1920;
    public static final int WIDTH_PREVIEW_FWVGA = 864;
    public static final int WIDTH_PREVIEW_HD = 1280;
    public static final int WIDTH_PREVIEW_HD_WIDE = 1440;
    public static final int WIDTH_PREVIEW_HVGA = 480;
    public static final int WIDTH_PREVIEW_QCIF = 176;
    public static final int WIDTH_PREVIEW_QHD = 960;
    public static final int WIDTH_PREVIEW_QVGA = 320;
    public static final int WIDTH_PREVIEW_UXGA = 1600;
    public static final int WIDTH_PREVIEW_VGA = 640;
    public static final int WIDTH_PREVIEW_WQHD = 2560;
    public static final int WIDTH_PREVIEW_WQXGA = 2560;
    public static final int WIDTH_UXGA = 1600;
    public static final int WIDTH_VGA = 640;

    public static final class LcdSize
    extends Enum<LcdSize> {
        private static final /* synthetic */ LcdSize[] $VALUES;
        public static final /* enum */ LcdSize EXT_LARGE;
        public static final /* enum */ LcdSize FULL_HD;
        public static final /* enum */ LcdSize LARGE;
        public static final /* enum */ LcdSize MIDDLE;
        public static final /* enum */ LcdSize SMALL;
        public static final /* enum */ LcdSize WQHD;
        public static final /* enum */ LcdSize WQXGA;
        public static final /* enum */ LcdSize WUXGA;
        public final int height;
        public final int width;

        static {
            WQXGA = new LcdSize(2560, 1600);
            WQHD = new LcdSize(2560, 1440);
            WUXGA = new LcdSize(1920, 1200);
            FULL_HD = new LcdSize(1920, 1080);
            EXT_LARGE = new LcdSize(1280, 720);
            LARGE = new LcdSize(960, 540);
            MIDDLE = new LcdSize(854, 480);
            SMALL = new LcdSize(480, 320);
            LcdSize[] arrlcdSize = new LcdSize[]{WQXGA, WQHD, WUXGA, FULL_HD, EXT_LARGE, LARGE, MIDDLE, SMALL};
            $VALUES = arrlcdSize;
        }

        private LcdSize(int n2, int n3) {
            super(string, n);
            this.width = n2;
            this.height = n3;
        }

        public static LcdSize getLcdSize(int n) {
            for (LcdSize lcdSize : LcdSize.values()) {
                if (n != lcdSize.height) continue;
                return lcdSize;
            }
            if (LcdSize.FULL_HD.height < n && n < LcdSize.WUXGA.height) {
                return WUXGA;
            }
            return LARGE;
        }

        public static LcdSize valueOf(String string) {
            return (LcdSize)Enum.valueOf((Class)LcdSize.class, (String)string);
        }

        public static LcdSize[] values() {
            return (LcdSize[])$VALUES.clone();
        }
    }

    public static final class PictureSize
    extends Enum<PictureSize> {
        private static final /* synthetic */ PictureSize[] $VALUES;
        public static final /* enum */ PictureSize SIZE_15MP_WIDE;
        public static final /* enum */ PictureSize SIZE_20MP;
        public static final /* enum */ PictureSize SIZE_20MP_HDR;
        public static final /* enum */ PictureSize SIZE_2MP_WIDE;
        public static final /* enum */ PictureSize SIZE_3MP;
        public static final /* enum */ PictureSize SIZE_8MP;
        public static final /* enum */ PictureSize SIZE_8MP_WIDE;
        int height;
        int width;

        static {
            SIZE_20MP = new PictureSize(5248, 3936);
            SIZE_20MP_HDR = new PictureSize(4998, 3748);
            SIZE_15MP_WIDE = new PictureSize(5248, 2952);
            SIZE_8MP = new PictureSize(3264, 2448);
            SIZE_8MP_WIDE = new PictureSize(3840, 2160);
            SIZE_3MP = new PictureSize(2048, 1536);
            SIZE_2MP_WIDE = new PictureSize(1920, 1080);
            PictureSize[] arrpictureSize = new PictureSize[]{SIZE_20MP, SIZE_20MP_HDR, SIZE_15MP_WIDE, SIZE_8MP, SIZE_8MP_WIDE, SIZE_3MP, SIZE_2MP_WIDE};
            $VALUES = arrpictureSize;
        }

        private PictureSize(int n2, int n3) {
            super(string, n);
            this.width = n2;
            this.height = n3;
        }

        public static PictureSize valueOf(String string) {
            return (PictureSize)Enum.valueOf((Class)PictureSize.class, (String)string);
        }

        public static PictureSize[] values() {
            return (PictureSize[])$VALUES.clone();
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }
    }

}

