package com.example.bloodbank.Chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bloodbank.MsgActivity;
import com.example.bloodbank.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    ArrayList<Name> nameList = new ArrayList<>();
    ImageView backBtn;

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
        backBtn = findViewById(R.id.chatListBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatListActivity.super.onBackPressed();
            }
        });



    }


    }
