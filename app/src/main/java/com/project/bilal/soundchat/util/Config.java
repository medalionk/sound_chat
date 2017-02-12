package com.project.bilal.soundchat.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;

/**
 * Created by Bilal Abdullah on 2/11/2017.
 */

public class Config {
    public final static double FACTOR = 3e4;
    public final static int RATE = 8000;
    public final static int TYPE = AudioManager.STREAM_MUSIC;
    public final static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public final static int MODE = AudioTrack.MODE_STATIC;
    public final static int OUTPUT_FORMAT = AudioFormat.CHANNEL_OUT_MONO;
    public final static int IN_FORMAT = AudioFormat.CHANNEL_IN_MONO;
    public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.VOICE_RECOGNITION;
}
