package com.project.bilal.soundchat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.bilal.soundchat.model.ChatError;
import com.project.bilal.soundchat.model.Message;
import com.project.bilal.soundchat.model.MessageType;
import com.project.bilal.soundchat.model.Chat;
import com.project.bilal.soundchat.util.SendChat;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;
    private ImageView imgSend;
    private EditText etSend;
    private ArrayList<Message> messages;
    private ChatAdapter listAdapter;
    private BroadcastReceiver receiver;
    private ChatReceiverService chatReceiverService;
    private boolean bounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages = new ArrayList<>();
        etSend = (EditText) findViewById(R.id.et_send);
        imgSend = (ImageView) findViewById(R.id.send);
        ListView lvChat = (ListView) findViewById(R.id.list_view_chat);

        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvChat.setStackFromBottom(true);

        listAdapter = new ChatAdapter(messages, this);
        lvChat.setAdapter(listAdapter);

        etSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){
                    imgSend.setImageResource(R.drawable.ic_chat_send);
                }else{
                    imgSend.setImageResource(R.drawable.ic_chat_send_active);
                }
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat(etSend.getText().toString());
                etSend.setText("");
            }
        });

        requestPermission();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Chat chat = intent.getParcelableExtra(ChatReceiverService.CHAT_MESSAGE);
                updateChat(chat, MessageType.RECEIVED);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, ChatReceiverService.class);
        bindService(mIntent, connection, BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(ChatReceiverService.UPDATE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        if(bounded) {
            unbindService(connection);
            bounded = false;
        }
    }

    ServiceConnection connection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            bounded = false;
            chatReceiverService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            bounded = true;
            ChatReceiverService.LocalBinder localBinder = (ChatReceiverService.LocalBinder)service;
            chatReceiverService = localBinder.getServerInstance();
        }
    };

    private void sendChat(final String msg)
    {
        if(msg.trim().length() == 0){
            return;
        }

        chatReceiverService.abortReceive();
        updateChat(new Chat(msg, null), MessageType.SENT);

        SendChat sendChat = new SendChat();
        sendChat.execute(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("App Cannot Work Without Mic!!! Exiting");
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    startReceiving();
                }
            }
        }
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
        }else {
            startReceiving();
        }
    }

    void updateChat(Chat chat, MessageType messageType){
        if (chat.error != null){
            if (chat.error == ChatError.INVALID_MSG || chat.error == ChatError.FORMAT){
                toast("Error Demodulating Voice!!!");
            }
        }else {
            final Message message = new Message();
            message.setMessage(chat.message);
            message.setType(messageType);
            message.setTime(new Date().getTime());
            messages.add(message);

            if(listAdapter != null){
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    void startReceiving(){
        startService(new Intent(this, ChatReceiverService.class));
    }
}
