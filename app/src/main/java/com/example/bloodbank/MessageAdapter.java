package com.example.bloodbank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    public MessageAdapter(List<Messages> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMsgText, receiverMsgText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsgText = itemView.findViewById(R.id.sender_message_text);
            receiverMsgText = itemView.findViewById(R.id.receiver_message_text);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        String fromMsgType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);


        if(fromMsgType.equals("text"))
        {
            holder.receiverMsgText.setVisibility(View.INVISIBLE);
            holder.senderMsgText.setVisibility(View.INVISIBLE);

            if(fromUserId.equals(messageSenderId))
            {
                holder.senderMsgText.setBackgroundResource(R.drawable.sender_msg_layout);
                holder.senderMsgText.setText(messages.getMessage());
                holder.senderMsgText.setVisibility(View.VISIBLE);
            }
            else
            {

                holder.receiverMsgText.setVisibility(View.VISIBLE);
                holder.receiverMsgText.setBackgroundResource(R.drawable.receiver_msg_layout);
                holder.receiverMsgText.setText(messages.getMessage());
            }
        }


    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


}
