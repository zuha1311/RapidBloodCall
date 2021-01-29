package com.example.bloodbank;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Chatting.ChatListAdapter;
import com.example.bloodbank.Chatting.Name;

import java.util.ArrayList;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.RequestsListViewHolder>  {

    public static ArrayList<Requests> requestsList;
    private Context mContext;

    public RequestsListAdapter ( ArrayList<Requests> requestsList, Context mContext){
        this.requestsList= requestsList;
        this.mContext = mContext;

    }

    @Override
    public void onBindViewHolder(@NonNull RequestsListAdapter.RequestsListViewHolder holder, final int position) {
        holder.username.setText(requestsList.get(position).getRequestUsername());

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MsgActivity.class);
                intent.putExtra("sender" , "chatDetails");
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }


    @NonNull
    @Override
    public RequestsListAdapter.RequestsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.requests_item,parent,false);
        return new RequestsListAdapter.RequestsListViewHolder(itemView);
    }

    public class RequestsListViewHolder extends RecyclerView.ViewHolder {
        TextView username;

        public RequestsListViewHolder(View view)
        {
            super(view);
            username = view.findViewById(R.id.requesterID);
        }



    }
}
