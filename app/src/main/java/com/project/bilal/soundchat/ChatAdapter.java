package com.project.bilal.soundchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.bilal.soundchat.model.Message;
import com.project.bilal.soundchat.model.MessageType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Bilal Abdullah on 2/11/2017.
 */

class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ArrayList<Message> messages;

    ChatAdapter(ArrayList<Message> chatMessages, Context context) {
        this.messages = chatMessages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        RelativeLayout.LayoutParams params;
        RelativeLayout layout;
        Chat chat;

        Message message = messages.get(i);

        if (view == null) {
            v = inflater.inflate(R.layout.chat_bubble, null);

            chat = new Chat();

            chat.tvMessage = (TextView) v.findViewById(R.id.tv_message);
            chat.tvUser = (TextView) v.findViewById(R.id.tv_user);
            chat.tvTime = (TextView) v.findViewById(R.id.tv_time);

            v.setTag(chat);
        } else {
            v = view;
            chat = (Chat) v.getTag();
        }

        layout = (RelativeLayout) v.findViewById(R.id.bubble_layout);
        params = (RelativeLayout.LayoutParams)layout.getLayoutParams();

        if (message.getType() == MessageType.SENT) {
            layout.setBackgroundResource(R.drawable.bubble_sent);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            chat.tvUser.setText(R.string.sent);
        }else {
            layout.setBackgroundResource(R.drawable.bubble_received);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            chat.tvUser.setText(R.string.received);
        }

        layout.setLayoutParams(params);
        chat.tvMessage.setText(message.getMessage());
        chat.tvTime.setText(new SimpleDateFormat("HH:mm").format(message.getTime()));

        return v;
    }

    @Override
    public int getItemViewType(int i) {
        Message message = messages.get(i);
        return message.getType().ordinal();
    }

    private class Chat {
        TextView tvUser;
        TextView tvMessage;
        TextView tvTime;
    }
}
