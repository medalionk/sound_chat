package com.project.bilal.soundchat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bilal Abdullah on 2/11/2017.
 */

public class Chat implements Parcelable {
    public final String message;
    public final ChatError error;

    public Chat(String message, ChatError error) {
        this.message = message;
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(message);
        out.writeInt(error.toInt());
    }

    public static final Parcelable.Creator<Chat> CREATOR = new Parcelable.Creator<Chat>() {
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    private Chat(Parcel in) {
        message = in.readString();
        error = ChatError.fromInt(in.readInt());
    }
}
