package com.project.bilal.soundchat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.project.bilal.soundchat.model.Chat;
import com.project.bilal.soundchat.util.ReceiveChat;

/**
 * Created by Bilal Abdullah on 2/10/2017.
 */

public class ChatReceiverService extends Service {

    static final public String UPDATE = "com.project.bilal.soundchat.CHAT_UPDATE";
    static final public String CHAT_MESSAGE = "com.project.bilal.soundchat.CHAT_MSG";

    private LocalBroadcastManager broadcaster;
    private ReceiveChat receiveChat = null;

    IBinder mBinder = new LocalBinder();

    public void sendChatMessage(Chat chat) {
        Intent intent = new Intent(UPDATE);
        intent.putExtra(CHAT_MESSAGE, chat);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        broadcaster = LocalBroadcastManager.getInstance(this);
        receiveChat();
    }

    void receiveChat(){
        receiveChat = new ReceiveChat() {
            @Override
            protected void onPostExecute(Chat chat) {
                sendChatMessage(chat);
                receiveChat();
            }
        };

        receiveChat.execute();
    }

    @Override
    public void onDestroy() {
        abortReceive();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public ChatReceiverService getServerInstance() {
            return ChatReceiverService.this;
        }
    }

    public void abortReceive() {
        receiveChat.abort();
    }
}
