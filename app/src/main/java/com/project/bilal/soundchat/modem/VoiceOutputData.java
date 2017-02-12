package com.project.bilal.soundchat.modem;

import com.project.bilal.OutputData;
import com.project.bilal.soundchat.util.Config;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Bilal Abdullah on 2/10/2017.
 */

public class VoiceOutputData implements OutputData {

    private final ByteArrayOutputStream arrayStream;
    private final DataOutputStream dataStream;

    public VoiceOutputData() {
        arrayStream = new ByteArrayOutputStream();
        dataStream = new DataOutputStream(arrayStream);
    }

    @Override
    public void write(double size) throws IOException {
        dataStream.writeShort((short) (Config.FACTOR * size));
    }

    public short[] data() {
        ShortBuffer shortBuffer = ByteBuffer.wrap(arrayStream.toByteArray()).asShortBuffer();
        short[] result = new short[shortBuffer.capacity()];
        shortBuffer.get(result);
        return result;
    }
}
