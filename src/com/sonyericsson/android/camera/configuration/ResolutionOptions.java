/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.util.SparseArray
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import com.sonyericsson.android.camera.util.capability.ResolutionDependence;

public class ResolutionOptions {
    private static final SparseArray<PreviewVideoResolutionDependentValues> sPreviewVideoValueArray = new SparseArray<PreviewVideoResolutionDependentValues>(){};
    private static final SparseArray<ResolutionDependentValues> sResolutionValueArray = new SparseArray<ResolutionDependentValues>(){};
    private final String mDefaultResolution;
    private final String mDefaultVideoSize;
    private final int mHighQualityVideoSize;
    private final int mMaxPictureSize;
    private final String[] mResolutionOptions;
    private final String[] mVideoSizeOptions;

    public ResolutionOptions() {
        this.mMaxPictureSize = 0;
        this.mHighQualityVideoSize = 0;
        this.mResolutionOptions = new String[0];
        this.mVideoSizeOptions = new String[0];
        this.mDefaultResolution = "";
        this.mDefaultVideoSize = "";
    }

    public ResolutionOptions(Context context, int n, int n2) {
        this.mMaxPictureSize = n;
        this.mHighQualityVideoSize = n2;
        this.mDefaultResolution = ResolutionOptions.getDefaultResolution(context, n);
        this.mResolutionOptions = super.getResolutionOptions(context, n);
        this.mDefaultVideoSize = super.getDefaultVideoSize(context, n, n2);
        this.mVideoSizeOptions = super.getVideoSizeOptions(context, n, n2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getDefaultResolution(Context context, int n) {
        int n2;
        if (ResolutionDependence.isDependOnAspect(context)) {
            n2 = ResolutionOptions.getValueFromResolutionMap(n).mNormalResolutionValue;
            do {
                return context.getResources().getString(n2);
                break;
            } while (true);
        }
        n2 = ResolutionOptions.getValueFromResolutionMap(n).mWideResolutionValue;
        return context.getResources().getString(n2);
    }

    private static int getDefaultVideoSize(int n) {
        return ResolutionOptions.getValueFromPreviewVideoMap(n).mDefaultVideoSizeValue;
    }

    private String getDefaultVideoSize(Context context, int n, int n2) {
        int n3 = ResolutionOptions.getValueFromResolutionMap(n).mDefaultVideoSize.get(n2);
        return context.getResources().getString(n3);
    }

    private String[] getResolutionOptions(Context context, int n) {
        int n2 = ResolutionOptions.getValueFromResolutionMap(n).mResolutionOptionsValue;
        return context.getResources().getStringArray(n2);
    }

    private static final PreviewVideoResolutionDependentValues getValueFromPreviewVideoMap(int n) {
        if (sPreviewVideoValueArray.indexOfKey(n) >= 0) {
            return (PreviewVideoResolutionDependentValues)sPreviewVideoValueArray.get(n);
        }
        return PreviewVideoResolutionDependentValues.HD;
    }

    private static final ResolutionDependentValues getValueFromResolutionMap(int n) {
        if (sResolutionValueArray.indexOfKey(n) >= 0) {
            return (ResolutionDependentValues)sResolutionValueArray.get(n);
        }
        return ResolutionDependentValues.VGA;
    }

    private static int getVideoSizeOptions(int n) {
        return ResolutionOptions.getValueFromPreviewVideoMap(n).mVideoSizeOptionsValue;
    }

    private String[] getVideoSizeOptions(Context context, int n, int n2) {
        int n3 = ResolutionOptions.getValueFromResolutionMap(n).mVideoSizeOptions.get(n2);
        return context.getResources().getStringArray(n3);
    }

    public String getDefaultResolution() {
        return this.mDefaultResolution;
    }

    public String getDefaultVideoSize() {
        return this.mDefaultVideoSize;
    }

    public int getHighQualityPreviewSize() {
        return this.mHighQualityVideoSize;
    }

    public int getPictureSize() {
        return this.mMaxPictureSize;
    }

    public String[] getResolutionOptions() {
        return (String[])this.mResolutionOptions.clone();
    }

    public String[] getVideoSizeOptions() {
        return (String[])this.mVideoSizeOptions.clone();
    }

    private static final class PreviewVideoResolutionDependentValues
    extends Enum<PreviewVideoResolutionDependentValues> {
        private static final /* synthetic */ PreviewVideoResolutionDependentValues[] $VALUES;
        public static final /* enum */ PreviewVideoResolutionDependentValues FOUR_K_UHD = new PreviewVideoResolutionDependentValues(2131623964, 2131362292);
        public static final /* enum */ PreviewVideoResolutionDependentValues FULL_HD;
        public static final /* enum */ PreviewVideoResolutionDependentValues FULL_HD_60FPS;
        public static final /* enum */ PreviewVideoResolutionDependentValues HD;
        private final int mDefaultVideoSizeValue;
        private final int mVideoSizeOptionsValue;

        static {
            FULL_HD_60FPS = new PreviewVideoResolutionDependentValues(2131623968, 2131362295);
            FULL_HD = new PreviewVideoResolutionDependentValues(2131623968, 2131362295);
            HD = new PreviewVideoResolutionDependentValues(2131623969, 2131362296);
            PreviewVideoResolutionDependentValues[] arrpreviewVideoResolutionDependentValues = new PreviewVideoResolutionDependentValues[]{FOUR_K_UHD, FULL_HD_60FPS, FULL_HD, HD};
            $VALUES = arrpreviewVideoResolutionDependentValues;
        }

        private PreviewVideoResolutionDependentValues(int n2, int n3) {
            super(string, n);
            this.mVideoSizeOptionsValue = n2;
            this.mDefaultVideoSizeValue = n3;
        }

        public static PreviewVideoResolutionDependentValues valueOf(String string) {
            return (PreviewVideoResolutionDependentValues)Enum.valueOf((Class)PreviewVideoResolutionDependentValues.class, (String)string);
        }

        public static PreviewVideoResolutionDependentValues[] values() {
            return (PreviewVideoResolutionDependentValues[])$VALUES.clone();
        }
    }

    private static final class ResolutionDependentValues
    extends Enum<ResolutionDependentValues> {
        private static final /* synthetic */ ResolutionDependentValues[] $VALUES;
        public static final /* enum */ ResolutionDependentValues EIGHT;
        public static final /* enum */ ResolutionDependentValues FIVE;
        public static final /* enum */ ResolutionDependentValues ONE;
        public static final /* enum */ ResolutionDependentValues THIRTEEN;
        public static final /* enum */ ResolutionDependentValues TWELVE;
        public static final /* enum */ ResolutionDependentValues TWENTY;
        public static final /* enum */ ResolutionDependentValues TWO;
        public static final /* enum */ ResolutionDependentValues VGA;
        private final DefaltVideoSizeProvider mDefaultVideoSize;
        private final int mNormalResolutionValue;
        private final int mResolutionOptionsValue;
        private final VideoSizeOptionsProvider mVideoSizeOptions;
        private final int mWideResolutionValue;

        static {
            TWENTY = new ResolutionDependentValues(2131362281, 2131362272, 2131623942, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362292;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623964;
                }
            });
            THIRTEEN = new ResolutionDependentValues(2131362282, 2131362273, 2131623943, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362293;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623965;
                }
            });
            TWELVE = new ResolutionDependentValues(2131362283, 2131362274, 2131623944, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362294;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623966;
                }
            });
            EIGHT = new ResolutionDependentValues(2131362284, 2131362275, 2131623945, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return ResolutionOptions.getDefaultVideoSize(n);
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return ResolutionOptions.getVideoSizeOptions(n);
                }
            });
            FIVE = new ResolutionDependentValues(2131362285, 2131362277, 2131623947, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362297;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623970;
                }
            });
            TWO = new ResolutionDependentValues(2131362286, 2131362278, 2131623950, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362298;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623973;
                }
            });
            ONE = new ResolutionDependentValues(2131362287, 2131362279, 2131623952, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362299;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623973;
                }
            });
            VGA = new ResolutionDependentValues(2131362280, 2131362280, 2131623953, new DefaltVideoSizeProvider(){

                @Override
                public int get(int n) {
                    return 2131362300;
                }
            }, new VideoSizeOptionsProvider(){

                @Override
                public int get(int n) {
                    return 2131623974;
                }
            });
            ResolutionDependentValues[] arrresolutionDependentValues = new ResolutionDependentValues[]{TWENTY, THIRTEEN, TWELVE, EIGHT, FIVE, TWO, ONE, VGA};
            $VALUES = arrresolutionDependentValues;
        }

        private ResolutionDependentValues(int n2, int n3, int n4, DefaltVideoSizeProvider defaltVideoSizeProvider, VideoSizeOptionsProvider videoSizeOptionsProvider) {
            super(string, n);
            this.mNormalResolutionValue = n2;
            this.mWideResolutionValue = n3;
            this.mResolutionOptionsValue = n4;
            this.mDefaultVideoSize = defaltVideoSizeProvider;
            this.mVideoSizeOptions = videoSizeOptionsProvider;
        }

        public static ResolutionDependentValues valueOf(String string) {
            return (ResolutionDependentValues)Enum.valueOf((Class)ResolutionDependentValues.class, (String)string);
        }

        public static ResolutionDependentValues[] values() {
            return (ResolutionDependentValues[])$VALUES.clone();
        }

        static interface DefaltVideoSizeProvider {
            public int get(int var1);
        }

        static interface VideoSizeOptionsProvider {
            public int get(int var1);
        }

    }

}

