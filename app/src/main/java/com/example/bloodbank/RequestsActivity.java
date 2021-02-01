package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bloodbank.Chatting.ChatListAdapter;
import com.example.bloodbank.Chatting.Name;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {

    String[] names = {"12345", "5678", "7564","12345", "5678", "7564","12345", "5678", "7564"};
    RecyclerView recyclerView;
    RequestsListAdapter requestsListAdapter;
    ArrayList<Requests> requestsList = new ArrayList<>();

    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        recyclerView = findViewById(R.id.requestsRecyclerView);
        requestsListAdapter = new RequestsListAdapter(requestsList,RequestsActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestsListAdapter);
        backBtn = findViewById(R.id.requestsBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestsActivity.super.onBackPressed();
            }
        });

        for(int i = 0 ; i<names.length;i++)
        {
            String username = names[i];

            Requests name = new Requests(username);
            requestsList.add(name);
        }


    }


}