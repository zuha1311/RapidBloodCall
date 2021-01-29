package com.example.bloodbank;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ReceivedFragment extends Fragment {

    RecyclerView recyclerView;
    ReceivedFragmentAdapter adapter;
    ArrayList<ReceivedBloodHistory> RecListHistory = new ArrayList<>();



    public ReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_received, container, false);
        recyclerView = view.findViewById(R.id.receivedRecyclerView);
        adapter = new ReceivedFragmentAdapter(RecListHistory,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecListHistory.add(new ReceivedBloodHistory("11/12/08","WEQ2","5","Agra"));
        RecListHistory.add(new ReceivedBloodHistory("31/01/06","T094","2","Aligarh"));
        RecListHistory.add(new ReceivedBloodHistory("04/05/07","5673","4.5","Mumbai"));
        RecListHistory.add(new ReceivedBloodHistory("11/12/08","WEQ2","5","Agra"));
        RecListHistory.add(new ReceivedBloodHistory("31/01/06","T094","2","Aligarh"));
        RecListHistory.add(new ReceivedBloodHistory("04/05/07","5673","4.5","Mumbai"));

        RecListHistory.add(new ReceivedBloodHistory("11/12/08","WEQ2","5","Agra"));
        RecListHistory.add(new ReceivedBloodHistory("31/01/06","T094","2","Aligarh"));
        RecListHistory.add(new ReceivedBloodHistory("04/05/07","5673","4.5","Mumbai"));



    }
}