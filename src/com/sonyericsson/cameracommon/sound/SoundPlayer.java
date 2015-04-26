/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.Resources
 *  android.media.AudioManager
 *  android.media.MediaPlayer
 *  android.media.MediaPlayer$OnCompletionListener
 *  java.io.FileDescriptor
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.sonyericsson.cameracommon.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.sonyericsson.cameracommon.apihelper.ApiHelper;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonymobile.cameracommon.media.recorder.RecorderInterface;
import java.io.FileDescriptor;
import java.io.IOException;

public class SoundPlayer
implements RecorderInterface.RecordingSoundPlayer {
    private static final String TAG = "SoundPlayer";
    private static final int sStreamType = ApiHelper.getIntFieldIfExists(AudioManager.class, "STREAM_SYSTEM_ENFORCED", null, 2);
    private final MediaPlayer mMediaPlayer = new MediaPlayer();

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private boolean playSound(Context context, int n) {
        MediaPlayer mediaPlayer;
        MediaPlayer mediaPlayer2 = mediaPlayer = this.mMediaPlayer;
        // MONITORENTER : mediaPlayer2
        AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(n);
        if (assetFileDescriptor == null) {
            CameraLogger.e("SoundPlayer", "playSound: open RawResourceFd fail");
            // MONITOREXIT : mediaPlayer2
            return false;
        }
        this.mMediaPlayer.reset();
        this.mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        this.mMediaPlayer.setAudioStreamType(sStreamType);
        this.mMediaPlayer.prepare();
        try {
            assetFileDescriptor.close();
        }
        catch (IOException var14_6) {
            CameraLogger.e("SoundPlayer", "playSound: close RawResourceFd fail");
        }
        this.mMediaPlayer.start();
        // MONITOREXIT : mediaPlayer2
        return true;
        catch (IOException iOException) {
            try {
                CameraLogger.e("SoundPlayer", "playSound: IOException", (Throwable)iOException);
                this.mMediaPlayer.reset();
            }
            catch (Throwable var7_9) {
                try {
                    assetFileDescriptor.close();
                }
                catch (IOException var8_10) {
                    CameraLogger.e("SoundPlayer", "playSound: close RawResourceFd fail");
                    throw var7_9;
                }
                throw var7_9;
            }
            try {
                assetFileDescriptor.close();
                // MONITOREXIT : mediaPlayer2
                return false;
            }
            catch (IOException var12_8) {
                CameraLogger.e("SoundPlayer", "playSound: close RawResourceFd fail");
                return false;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private boolean playSound(String string) {
        MediaPlayer mediaPlayer;
        MediaPlayer mediaPlayer2 = mediaPlayer = this.mMediaPlayer;
        synchronized (mediaPlayer2) {
            if (string == null) {
                return false;
            }
            try {
                this.mMediaPlayer.reset();
                this.mMediaPlayer.setDataSource(string);
                this.mMediaPlayer.setAudioStreamType(sStreamType);
                this.mMediaPlayer.prepare();
            }
            catch (IOException var3_4) {
                CameraLogger.e("SoundPlayer", "playSound: IOException", (Throwable)var3_4);
                this.mMediaPlayer.reset();
                return false;
            }
            this.mMediaPlayer.start();
            return true;
        }
    }

    public boolean playAfSuccessSound() {
        return this.playSound("/system/media/audio/camera/common/af_success.m4a");
    }

    @Override
    public void playRecordingSound() {
        this.playSound("/system/media/audio/ui/VideoRecord.ogg");
    }

    public boolean playShutterSound(Context context, int n) {
        return super.playSound(context, n);
    }

    public boolean playShutterSound(String string) {
        return super.playSound(string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void release() {
        MediaPlayer mediaPlayer;
        MediaPlayer mediaPlayer2 = mediaPlayer = this.mMediaPlayer;
        synchronized (mediaPlayer2) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            return;
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.mMediaPlayer.setOnCompletionListener(onCompletionListener);
    }
}

