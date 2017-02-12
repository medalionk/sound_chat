package com.project.bilal.soundchat.model;

/**
 * Created by Bilal Abdullah on 2/12/2017.
 */

public enum ChatError {
    ABORT(0), INVALID_MSG(1), FORMAT(2);

    private final int value;

    ChatError(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static ChatError fromInt(int value)
    {
        for(ChatError error : ChatError.values() )
        {
            if(error.toInt() == value)
            {
                return error;
            }
        }
        return null;
    }
}
