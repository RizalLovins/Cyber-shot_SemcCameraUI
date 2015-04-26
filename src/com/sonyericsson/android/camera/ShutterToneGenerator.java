/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.media.MediaActionSound
 *  android.media.MediaPlayer
 *  android.os.Handler
 *  android.os.Message
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package com.sonyericsson.android.camera;

import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonymobile.cameracommon.media.recorder.RecorderInterface;
import java.io.IOException;

public class ShutterToneGenerator
implements RecorderInterface.RecordingSoundPlayer {
    public static final int MAX_VOLUME = 7;
    private static final String SOUND_HOME = "/system/media/audio/camera/";
    private static final int SOUND_STREAM = 7;
    private static final String TAG = ShutterToneGenerator.class.getSimpleName();
    public static final String VANILLA_SHUTTER_SOUND_FILE_PATH = "/system/media/audio/ui/camera_click.ogg";
    public static final String VANILLA_VIDEO_RECORD_SOUND_FILE_PATH = "/system/media/audio/ui/VideoRecord.ogg";
    private CameraDevice mCameraDevice;
    private final LedBlinkerHandler mLedBlinkerHandler;
    private final MediaActionSound mMediaActionSound = new MediaActionSound();
    private MediaPlayer mMediaPlayer;
    private ShutterSound mShutterSound;

    public ShutterToneGenerator() {
        this.mLedBlinkerHandler = new LedBlinkerHandler();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getSoundFilePath(Type type, ShutterSound shutterSound) {
        StringBuffer stringBuffer = new StringBuffer("/system/media/audio/camera/");
        if (shutterSound.getBooleanValue().booleanValue()) {
            if (type.isCommonSound()) {
                stringBuffer.append(type.getFileName());
                do {
                    return stringBuffer.toString();
                    break;
                } while (true);
            }
            stringBuffer.append(shutterSound.getDirectoryName());
            stringBuffer.append(type.getFileName());
            return stringBuffer.toString();
        }
        stringBuffer.append(shutterSound.getDirectoryName());
        stringBuffer.append(Type.SOUND_OFF.getFileName());
        return stringBuffer.toString();
    }

    private static void play(MediaPlayer mediaPlayer, String string) throws IllegalArgumentException, IllegalStateException, IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(string);
        mediaPlayer.setAudioStreamType(7);
        mediaPlayer.setVolume(7.0f, 7.0f);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public void blink() {
        this.mCameraDevice.turnOnLight();
        this.mLedBlinkerHandler.sendEmptyMessageDelayed(1, 100);
    }

    public void cancelPlayAndBlink() {
        this.mCameraDevice.turnOffLight();
        this.mLedBlinkerHandler.removeMessages(1);
        this.mMediaPlayer.stop();
    }

    public void clearBlink() {
        this.mLedBlinkerHandler.removeMessages(1);
    }

    public void play(Type type) {
        try {
            ShutterToneGenerator.play(this.mMediaPlayer, ShutterToneGenerator.getSoundFilePath(type, this.mShutterSound));
            return;
        }
        catch (IllegalArgumentException var4_2) {
            this.mMediaPlayer.reset();
            return;
        }
        catch (IllegalStateException var3_3) {
            this.mMediaPlayer.reset();
            return;
        }
        catch (IOException var2_4) {
            this.mMediaPlayer.reset();
            return;
        }
    }

    @Override
    public void playRecordingSound() {
        try {
            ShutterToneGenerator.play(this.mMediaPlayer, "/system/media/audio/ui/VideoRecord.ogg");
            return;
        }
        catch (IllegalArgumentException var3_1) {
            this.mMediaPlayer.reset();
            return;
        }
        catch (IllegalStateException var2_2) {
            this.mMediaPlayer.reset();
            return;
        }
        catch (IOException var1_3) {
            this.mMediaPlayer.reset();
            return;
        }
    }

    public void prepare(CameraDevice cameraDevice) {
        this.mMediaPlayer = new MediaPlayer();
        this.mCameraDevice = cameraDevice;
    }

    public void release() {
        this.mLedBlinkerHandler.removeMessages(1);
        this.mCameraDevice = null;
        this.mMediaActionSound.release();
    }

    public void releaseResources() {
        if (!(this.mCameraDevice != null && this.mCameraDevice.isRecorderWorking() || this.mMediaPlayer == null)) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public void setShutterSound(ShutterSound shutterSound) {
        this.mShutterSound = shutterSound;
    }

    public void testSound(int n) {
        switch (n) {
            default: {
                return;
            }
            case 1: {
                this.mMediaActionSound.play(0);
                return;
            }
            case 2: 
        }
        this.mMediaActionSound.play(2);
    }

    /*
     * Failed to analyse overrides
     */
    public class LedBlinkerHandler
    extends Handler {
        public static final int MSG_LIGHT_OFF = 1;

        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 1: 
            }
            ShutterToneGenerator.this.mCameraDevice.turnOffLight();
        }
    }

    public static final class Type
    extends Enum<Type> {
        private static final /* synthetic */ Type[] $VALUES;
        public static final /* enum */ Type SOUND_AF_SUCCESS = new Type("common/af_success.m4a", true);
        public static final /* enum */ Type SOUND_BURST_SHUTTER;
        public static final /* enum */ Type SOUND_FAST_CAPTURE_SHUTTER_DONE;
        public static final /* enum */ Type SOUND_OFF;
        public static final /* enum */ Type SOUND_SELFTIMER_10SEC;
        public static final /* enum */ Type SOUND_SELFTIMER_2SEC;
        private String mFileName;
        private boolean mIsCommonSound;

        static {
            SOUND_SELFTIMER_2SEC = new Type("common/selftimer_2sec.m4a", true);
            SOUND_SELFTIMER_10SEC = new Type("common/selftimer_10sec.m4a", true);
            SOUND_OFF = new Type("no_sound.m4a", false);
            SOUND_BURST_SHUTTER = new Type("shutter_done.wav", false);
            SOUND_FAST_CAPTURE_SHUTTER_DONE = new Type("fastcapture_launch_and_capture_done.wav", false);
            Type[] arrtype = new Type[]{SOUND_AF_SUCCESS, SOUND_SELFTIMER_2SEC, SOUND_SELFTIMER_10SEC, SOUND_OFF, SOUND_BURST_SHUTTER, SOUND_FAST_CAPTURE_SHUTTER_DONE};
            $VALUES = arrtype;
        }

        private Type(String string2, boolean bl) {
            super(string, n);
            this.mFileName = string2;
            this.mIsCommonSound = bl;
        }

        public static Type valueOf(String string) {
            return (Type)Enum.valueOf((Class)Type.class, (String)string);
        }

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        public String getFileName() {
            return this.mFileName;
        }

        public boolean isCommonSound() {
            return this.mIsCommonSound;
        }
    }

}

