package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bloodbank.Chatting.Name;

import java.util.ArrayList;

import static com.example.bloodbank.Chatting.ChatListAdapter.nameList;

public class MsgActivity extends AppCompatActivity {

    TextView username;
    public static ArrayList<Name> chatRecipient = new ArrayList<>();
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        username = findViewById(R.id.chatActivityUsername);
        getIntentMethod();
        username.setText(chatRecipient.get(position).getUsername());
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