package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Chatting.ChatListAdapter;
import com.example.bloodbank.Chatting.Name;
import com.example.bloodbank.Chatting.itemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DonatedFragmentAdapter extends RecyclerView.Adapter<DonatedFragmentAdapter.DonatedFragmentViewHolder> {

    public static ArrayList<DonatedBloodHistory> historyList;
    private Context mContext;


    public DonatedFragmentAdapter (ArrayList<DonatedBloodHistory> historyList, Context mContext){
        this.historyList = historyList;
        this.mContext = mContext;

    }

    @Override
    public void onBindViewHolder(@NonNull DonatedFragmentAdapter.DonatedFragmentViewHolder holder, int position) {

        holder.date.setText(historyList.get(position).getDate());
        holder.loc.setText(historyList.get(position).getLocation());
        holder.rid.setText(historyList.get(position).getId());
        holder.qty.setText(historyList.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }



    @NonNull
    @Override
    public DonatedFragmentAdapter.DonatedFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.donated_item,parent,false);
        return new DonatedFragmentAdapter.DonatedFragmentViewHolder(itemView);
    }

    public class DonatedFragmentViewHolder extends RecyclerView.ViewHolder {
        TextView date,qty,rid,loc;

        public DonatedFragmentViewHolder(View view)
        {
            super(view);
            date = view.findViewById(R.id.dateDonated);
            qty = view.findViewById(R.id.quantitydonated);
            rid = view.findViewById(R.id.receiverIDdonated);
            loc = view.findViewById(R.id.locationdonated);
        }



}
}
