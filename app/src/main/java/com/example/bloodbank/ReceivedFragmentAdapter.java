package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceivedFragmentAdapter extends RecyclerView.Adapter<ReceivedFragmentAdapter.ReceivedFragmentViewHolder> {

    public static ArrayList<ReceivedBloodHistory> receivedhistoryList;
    private Context mContext;


    public ReceivedFragmentAdapter (ArrayList<ReceivedBloodHistory> receivedhistoryList, Context mContext){
        this.receivedhistoryList = receivedhistoryList;
        this.mContext = mContext;

    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedFragmentAdapter.ReceivedFragmentViewHolder holder, int position) {

        holder.date.setText(receivedhistoryList.get(position).getDate());
        holder.loc.setText(receivedhistoryList.get(position).getLocation());
        holder.rid.setText(receivedhistoryList.get(position).getId());
        holder.qty.setText(receivedhistoryList.get(position).getQty());

    }

    @Override
    public int getItemCount() {
        return receivedhistoryList.size();
    }



    @NonNull
    @Override
    public ReceivedFragmentAdapter.ReceivedFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.received_item,parent,false);
        return new ReceivedFragmentAdapter.ReceivedFragmentViewHolder(itemView);
    }

    public class ReceivedFragmentViewHolder extends RecyclerView.ViewHolder {
        TextView date,qty,rid,loc;

        public ReceivedFragmentViewHolder(View view)
        {
            super(view);
            date = view.findViewById(R.id.dateReceived);
            qty = view.findViewById(R.id.quantityReceived);
            rid = view.findViewById(R.id.donorID);
            loc = view.findViewById(R.id.locationReceived);
        }



    }
}
