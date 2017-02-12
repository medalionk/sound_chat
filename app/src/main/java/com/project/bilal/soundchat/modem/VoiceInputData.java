package com.project.bilal.soundchat.modem;

import android.media.AudioRecord;

import com.project.bilal.InputData;
import com.project.bilal.soundchat.util.Config;
import java.io.IOException;

/**
 * Created by Bilal Abdullah on 2/10/2017.
 */

public class VoiceInputData implements InputData {
    private volatile boolean abort = false;

    private AudioRecord audioRecord;
    private short[] temp;
    private int offset;
    private int size;

    public VoiceInputData(AudioRecord audioRecord, int size) {
        this.audioRecord = audioRecord;
        temp = new short[size];
    }

    @Override
    public double read() throws IOException {
        if (abort) {
            throw new IOException("ABORT");
        }

        while (offset >= size) {
            offset = 0;
            size = audioRecord.read(temp, 0, temp.length);
        }

        return temp[offset++] / Config.FACTOR;
    }

    public void abortRead() {
        abort = true;
    }
}
