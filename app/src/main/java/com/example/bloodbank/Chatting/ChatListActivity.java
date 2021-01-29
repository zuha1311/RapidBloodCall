package com.example.bloodbank.Chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bloodbank.MsgActivity;
import com.example.bloodbank.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    String[] names = {"Donor #12345", "Donor #5678", "Donor #7564","Donor #12345", "Donor #5678", "Donor #7564","Donor #12345", "Donor #5678", "Donor #7564"};
    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    ArrayList<Name> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.recycler_view1);
        chatListAdapter = new ChatListAdapter(nameList, ChatListActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatListAdapter);


        for(int i = 0 ; i<names.length;i++)
        {
            String username = names[i];

            Name name = new Name(username);
            nameList.add(name);
        }
    }


    }
