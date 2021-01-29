package com.example.bloodbank.Chatting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.MsgActivity;
import com.example.bloodbank.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    public static ArrayList<Name> nameList;
    private Context mContext;


    public ChatListAdapter (ArrayList<Name> nameList, Context mContext){
        this.nameList = nameList;
        this.mContext = mContext;

    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, final int position) {
        holder.username.setText(nameList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MsgActivity.class);
                intent.putExtra("sender" , "chatDetails");
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
          return nameList.size();
    }


    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        return new ChatListAdapter.ChatListViewHolder(itemView);
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        TextView username;

        public ChatListViewHolder(View view)
        {
            super(view);
            username = view.findViewById(R.id.chatListUsername);
        }



    }
}
