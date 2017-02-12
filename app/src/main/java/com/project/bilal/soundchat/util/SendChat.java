package com.project.bilal.soundchat.util;

import android.media.AudioTrack;
import android.os.AsyncTask;
import com.project.bilal.soundchat.modem.VoiceOutputData;
import com.project.bilal.e;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Bilal Abdullah on 2/11/2017.
 */

public class SendChat extends AsyncTask<String, Double, Void> {

    @Override
    protected Void doInBackground(String... params) {

        final int bufferSize = AudioTrack.getMinBufferSize(Config.RATE, Config.OUTPUT_FORMAT, Config.ENCODING);

        VoiceOutputData voiceOutputData = new VoiceOutputData();
        final byte[] data = params[0].getBytes();

        try {
            e.send(new ByteArrayInputStream(data), voiceOutputData);
        } catch (IOException e) {
            return null;
        }

        short[] sourceData = voiceOutputData.data();

        AudioTrack audioTrack = new AudioTrack(Config.TYPE, Config.RATE, Config.OUTPUT_FORMAT, Config.ENCODING,
                Math.max(sourceData.length, bufferSize) * 2, Config.MODE
        );

        int numWritten = audioTrack.write(sourceData, 0, sourceData.length);
        double length = ((double) numWritten) / Config.RATE;

        audioTrack.play();

        try {
            final double delay = 0.1;
            for (double i = 0; i < length; i += delay) {
                Thread.sleep((long) (delay * 1000));
            }
        } catch (InterruptedException ignored) {

        }

        audioTrack.stop();
        audioTrack.release();

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }

    @Override
    protected void onProgressUpdate(Double... values) {

    }
}
