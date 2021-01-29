package com.example.bloodbank;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbank.Chatting.Name;

import java.util.ArrayList;


public class DonatedFragment extends Fragment {

    RecyclerView recyclerView;
    DonatedFragmentAdapter adapter;
    ArrayList<DonatedBloodHistory> donatedList = new ArrayList<>();

    public DonatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_donated, container, false);
        recyclerView = view.findViewById(R.id.donatedRecyclerView);
        adapter = new DonatedFragmentAdapter(donatedList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        donatedList.add(new DonatedBloodHistory("11/12/18", "2", "Gulnuma", "435"));
        donatedList.add(new DonatedBloodHistory("13/11/00","3.5","Agra","891A"));
        donatedList.add(new DonatedBloodHistory("16/10/07","7.6","Aligarh","E432"));
        donatedList.add(new DonatedBloodHistory("31/05/20","5","Mumbai","R543"));
        donatedList.add(new DonatedBloodHistory("11/12/18", "2", "Gulnuma", "435"));
        donatedList.add(new DonatedBloodHistory("13/11/00","3.5","Agra","891A"));
        donatedList.add(new DonatedBloodHistory("16/10/07","7.6","Aligarh","E432"));
        donatedList.add(new DonatedBloodHistory("31/05/20","5","Mumbai","R543"));
    }
}