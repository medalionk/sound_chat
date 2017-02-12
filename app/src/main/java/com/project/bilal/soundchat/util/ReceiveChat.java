package com.project.bilal.soundchat.util;

import android.media.AudioRecord;
import android.os.AsyncTask;

import com.project.bilal.soundchat.model.Chat;
import com.project.bilal.soundchat.model.ChatError;
import com.project.bilal.soundchat.modem.VoiceInputData;
import com.project.bilal.j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Bilal Abdullah on 2/11/2017.
 */

public class ReceiveChat extends AsyncTask<Void, Double, Chat> {

    private VoiceInputData input;

    public void abort() {
        if(input != null){
            input.abortRead();
        }
    }

    @Override
    protected Chat doInBackground(Void... params) {

        int bufferSize = AudioRecord.getMinBufferSize(Config.RATE, Config.IN_FORMAT, Config.ENCODING);
        bufferSize = Math.max(bufferSize, 1024);

        AudioRecord audioRecord = new AudioRecord(Config.AUDIO_SOURCE, Config.RATE, Config.IN_FORMAT,
                Config.ENCODING, bufferSize * 8);

        input = new VoiceInputData(audioRecord, bufferSize);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        audioRecord.startRecording();
        try {
            j.receive(input, output);
        } catch (IOException e) {
            if (e.getMessage().equals("ABORT")){
                return new Chat(null, ChatError.ABORT);
            }
            return new Chat(null, ChatError.INVALID_MSG);
        } finally {
            audioRecord.stop();
            audioRecord.release();
        }

        try {
            return new Chat(new String(output.toByteArray(), "UTF-8"), null);
        } catch (UnsupportedEncodingException e) {
            return new Chat(null, ChatError.FORMAT);
        }
    }
}
