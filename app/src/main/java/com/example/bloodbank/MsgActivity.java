package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.Chatting.MessageAdpater;
import com.example.bloodbank.Chatting.Messages;
import com.example.bloodbank.Chatting.Name;

import java.util.ArrayList;
import java.util.List;

import static com.example.bloodbank.Chatting.ChatListAdapter.nameList;

public class MsgActivity extends AppCompatActivity {

    TextView username;
    public static ArrayList<Name> chatRecipient = new ArrayList<>();
    int position = -1;
    private final List<Messages> msgsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdpater messageAdpater;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        username = findViewById(R.id.chatActivityUsername);
        getIntentMethod();
        username.setText(chatRecipient.get(position).getUsername());
        backBtn = findViewById(R.id.chatScreenBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgActivity.super.onBackPressed();
            }
        });
        initializeControllers();
    }

    private void initializeControllers() {


    }

    private void getIntentMethod() {

        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender!=null && sender.equals("chatDetails"))
        {
           chatRecipient = nameList;
        }

    }
}