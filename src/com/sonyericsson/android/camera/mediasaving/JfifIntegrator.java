/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package com.sonyericsson.android.camera.mediasaving;

import com.sonyericsson.android.camera.mediasaving.IntegrationMakerException;
import java.math.BigInteger;

public class JfifIntegrator {
    private static final int M_DHT = 196;
    private static final int M_DQT = 219;
    private static final int M_MARKER = 255;
    private static final int M_SOI = 216;
    private static final int M_SOS = 218;

    /*
     * Enabled aggressive block sorting
     */
    public static int integrate(byte[] arrby, byte[] arrby2) throws IntegrationMakerException {
        int n = arrby.length;
        byte[] arrby3 = new byte[2];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n2 + 1 < n) {
            arrby3[0] = arrby[n2];
            arrby3[1] = arrby[n2 + 1];
            n2+=2;
            if (-1 != arrby3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == arrby3[1]) break;
            if (-40 == arrby3[1]) continue;
            byte[] arrby4 = new byte[]{arrby[n2], arrby[n2 + 1]};
            int n7 = new BigInteger(arrby4).intValue();
            if (-60 == arrby3[1]) {
                int n8 = n4 == 0 ? n7 : n7 - 2;
                n4+=n8;
                n6+=n7 + 2;
            } else if (-37 == arrby3[1]) {
                int n9 = n3 == 0 ? n7 : n7 - 2;
                n3+=n9;
                n5+=n7 + 2;
            }
            n2+=n7;
        }
        byte[] arrby5 = new byte[n3 + 2];
        byte[] arrby6 = new byte[n4 + 2];
        arrby5[0] = -1;
        arrby5[1] = -37;
        arrby6[0] = -1;
        arrby6[1] = -60;
        String string = String.valueOf((int)n3);
        String string2 = String.valueOf((int)n4);
        byte[] arrby7 = new BigInteger(string).toByteArray();
        arrby5[2] = arrby7[0];
        arrby5[3] = arrby7[1];
        byte[] arrby8 = new BigInteger(string2).toByteArray();
        arrby6[2] = arrby8[0];
        arrby6[3] = arrby8[1];
        int n10 = 4;
        int n11 = 4;
        int n12 = 2 + (n4 + (2 + (n3 + (n - n5 - n6))));
        int n13 = 0;
        int n14 = 0;
        while (n13 + 1 < n) {
            arrby3[0] = arrby[n13];
            arrby3[1] = arrby[n13 + 1];
            n13+=2;
            if (-1 != arrby3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == arrby3[1]) {
                int n15 = n3 + 2;
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby2, (int)n14, (int)n15);
                int n16 = n14 + (n3 + 2);
                System.arraycopy((Object)arrby6, (int)0, (Object)arrby2, (int)n16, (int)(n4 + 2));
                int n17 = n16 + (n4 + 2);
                System.arraycopy((Object)arrby, (int)(n13 - 2), (Object)arrby2, (int)n17, (int)(n - (n13 - 2)));
                return n12;
            }
            if (-40 == arrby3[1]) {
                arrby2[n13 - 2] = arrby3[0];
                arrby2[n13 - 1] = arrby3[1];
                n14+=2;
                continue;
            }
            byte[] arrby9 = new byte[]{arrby[n13], arrby[n13 + 1]};
            int n18 = new BigInteger(arrby9).intValue();
            if (-60 == arrby3[1]) {
                int n19 = n13 + 2;
                int n20 = n18 - 2;
                System.arraycopy((Object)arrby, (int)n19, (Object)arrby6, (int)n11, (int)n20);
                n11+=n18 - 2;
            } else if (-37 == arrby3[1]) {
                int n21 = n13 + 2;
                int n22 = n18 - 2;
                System.arraycopy((Object)arrby, (int)n21, (Object)arrby5, (int)n10, (int)n22);
                n10+=n18 - 2;
            } else {
                int n23 = n13 - 2;
                int n24 = n18 + 2;
                System.arraycopy((Object)arrby, (int)n23, (Object)arrby2, (int)n14, (int)n24);
                n14+=n18 + 2;
            }
            n13+=n18;
        }
        return n12;
    }
}

