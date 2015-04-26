/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  java.lang.Boolean
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.content.res.Resources;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.android.camera.util.capability.ResolutionDependence;

public class ResolutionOptions {
    private final String mBurstResolution;
    private final String mDefaultResolution;
    private final String mDefaultVideoSize;
    private final String mHdr3DefaultResolution;
    private final String[] mHdr3ResolutionOptions;
    private final int mHighQualityVideoSize;
    private final int mMaxPictureSize;
    private final String[] mResolutionOptions;
    private final String[] mSuperiorAutoResolutionOptions;
    private final String[] mVideoSizeOptions;

    public ResolutionOptions() {
        this.mMaxPictureSize = 0;
        this.mHighQualityVideoSize = 0;
        this.mResolutionOptions = new String[0];
        this.mSuperiorAutoResolutionOptions = new String[0];
        this.mVideoSizeOptions = new String[0];
        this.mDefaultResolution = "";
        this.mBurstResolution = "";
        this.mDefaultVideoSize = "";
        this.mHdr3ResolutionOptions = new String[0];
        this.mHdr3DefaultResolution = "";
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    public ResolutionOptions(Context var1_1, int var2_4, int var3_3, boolean var4) {
        super();
        this.mMaxPictureSize = var2_4;
        this.mHighQualityVideoSize = var3_3;
        var5_5 = ResolutionDependence.isDependOnAspect(var1_1);
        switch (this.mMaxPictureSize) {
            default: {
                var6_6 = 2131623953;
                var7_7 = 2131623953;
                var8_8 = 2131623963;
                var9_9 = 2131623974;
                var10_10 = 2131362280;
                var11_11 = 2131362280;
                var12_12 = 2131362300;
                var13_13 = 2131362291;
                ** GOTO lbl126
            }
            case 5248: {
                var6_6 = 2131623942;
                var8_8 = 2131623954;
                var7_7 = 2131623942;
                var9_9 = 2131623964;
                if (var5_5.booleanValue()) {
                    var10_10 = 2131362281;
                    var11_11 = 2131362281;
                } else {
                    var10_10 = 2131362272;
                    var11_11 = 2131362272;
                }
                var13_13 = 2131362288;
                var12_12 = 2131362292;
                ** GOTO lbl126
            }
            case 4128: {
                var6_6 = 2131623943;
                var7_7 = 2131623943;
                var8_8 = 2131623955;
                var9_9 = 2131623965;
                if (var5_5.booleanValue()) {
                    var10_10 = 2131362282;
                    var11_11 = 2131362282;
                } else {
                    var10_10 = 2131362273;
                    var11_11 = 2131362273;
                }
                var13_13 = 2131362289;
                var12_12 = 2131362293;
                ** GOTO lbl126
            }
            case 4000: {
                var6_6 = 2131623944;
                var7_7 = 2131623944;
                var8_8 = 2131623956;
                var9_9 = 2131623966;
                if (var5_5.booleanValue()) {
                    var10_10 = 2131362283;
                    var11_11 = 2131362283;
                } else {
                    var10_10 = 2131362274;
                    var11_11 = 2131362283;
                }
                var13_13 = 2131362291;
                var12_12 = 2131362294;
                ** GOTO lbl126
            }
            case 3264: {
                var6_6 = 2131623945;
                var7_7 = 2131623946;
                var8_8 = var4 != false ? 2131623958 : 2131623957;
                var9_9 = super.getVideoSizeOptions(this.mHighQualityVideoSize);
                if (var5_5.booleanValue()) {
                    var10_10 = 2131362284;
                    var11_11 = 2131362284;
                } else {
                    var10_10 = 2131362275;
                    var11_11 = 2131362276;
                }
                var13_13 = 2131362290;
                var12_12 = super.getDefaultVideoSize(this.mHighQualityVideoSize);
                ** GOTO lbl126
            }
            case 2592: {
                var6_6 = 2131623947;
                var7_7 = 2131623947;
                var8_8 = 2131623959;
                var9_9 = 2131623970;
                if (var5_5.booleanValue()) {
                    var10_10 = 2131362285;
                    var11_11 = 2131362285;
                } else {
                    var10_10 = 2131362277;
                    var11_11 = 2131362277;
                }
                var13_13 = 2131362291;
                var12_12 = 2131362297;
                ** GOTO lbl126
            }
            case 1920: {
                var16_14 = HardwareCapability.getCapability(0);
                var15_15 = 0;
                if (var16_14 != null) {
                    var15_15 = var17_16 = var16_14.getMaxPictureSize();
                } else {
                    ** GOTO lbl109
                }
            }
            case 1280: 
        }
        var6_6 = 2131623952;
        var7_7 = 2131623952;
        var8_8 = 2131623962;
        var9_9 = 2131623973;
        if (var5_5.booleanValue()) {
            var10_10 = 2131362287;
            var11_11 = 2131362287;
        } else {
            var10_10 = 2131362279;
            var11_11 = 2131362279;
        }
        var12_12 = 2131362299;
        var13_13 = 2131362291;
        ** GOTO lbl126
        catch (IllegalArgumentException var14_17) {
            var15_15 = 0;
        }
lbl109: // 4 sources:
        if (var15_15 == 5248) {
            var6_6 = 2131623948;
            var7_7 = 2131623949;
            var9_9 = 2131623971;
        } else {
            var6_6 = 2131623950;
            var7_7 = 2131623951;
            var9_9 = 2131623972;
        }
        var8_8 = var4 != false ? 2131623960 : 2131623961;
        if (var5_5.booleanValue()) {
            var10_10 = 2131362286;
            var11_11 = 2131362286;
        } else {
            var10_10 = 2131362278;
            var11_11 = 2131362278;
        }
        var13_13 = 2131362291;
        var12_12 = 2131362298;
lbl126: // 8 sources:
        this.mResolutionOptions = var1_1.getResources().getStringArray(var6_6);
        this.mHdr3ResolutionOptions = var1_1.getResources().getStringArray(var7_7);
        this.mSuperiorAutoResolutionOptions = var1_1.getResources().getStringArray(var8_8);
        this.mVideoSizeOptions = var1_1.getResources().getStringArray(var9_9);
        this.mDefaultResolution = var1_1.getResources().getString(var10_10);
        this.mBurstResolution = var1_1.getResources().getString(var13_13);
        this.mDefaultVideoSize = var1_1.getResources().getString(var12_12);
        this.mHdr3DefaultResolution = var1_1.getResources().getString(var11_11);
    }

    private int getDefaultVideoSize(int n) {
        switch (n) {
            default: {
                return 2131362296;
            }
            case 1080: {
                return 2131362295;
            }
            case 720: 
        }
        return 2131362296;
    }

    private int getVideoSizeOptions(int n) {
        switch (n) {
            default: {
                return -1;
            }
            case 1080: {
                return 2131623968;
            }
            case 720: 
        }
        return 2131623969;
    }

    public String getBurstResolution() {
        return this.mBurstResolution;
    }

    public String getDefaultResolution() {
        return this.mDefaultResolution;
    }

    public String getDefaultVideoSize() {
        return this.mDefaultVideoSize;
    }

    public String getHdr3DefaultResolution() {
        return this.mHdr3DefaultResolution;
    }

    public String[] getHdr3ResolutionOptions() {
        return (String[])this.mHdr3ResolutionOptions.clone();
    }

    public int getPictureSize() {
        return this.mMaxPictureSize;
    }

    public String[] getResolutionOptions() {
        return (String[])this.mResolutionOptions.clone();
    }

    public String[] getSuperiorAutoResolutionOptions() {
        return (String[])this.mSuperiorAutoResolutionOptions.clone();
    }

    public String[] getVideoSizeOptions() {
        return (String[])this.mVideoSizeOptions.clone();
    }
}

