package com.example.bloodbank.Chatting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdpater extends RecyclerView.Adapter<MessageAdpater.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    public MessageAdpater(List<Messages> userMessagesList)
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
       /* usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        if(fromMsgType.equals("text"))
        {
            holder.receiverMsgText.setVisibility(View.INVISIBLE);

            if(fromUserId.equals(messageSenderId))
            {
                holder.senderMsgText.setBackgroundResource(R.drawable.sender_msg_layout);
                holder.senderMsgText.setText(messages.getMessage());
            }
            else
            {
                holder.senderMsgText.setVisibility(View.INVISIBLE);
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
