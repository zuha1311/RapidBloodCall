package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Chatting.Name;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bloodbank.Chatting.ChatListAdapter.nameList;

public class MsgActivity extends AppCompatActivity {

    private TextView username;
    public static ArrayList<Name> chatRecipient = new ArrayList<>();
    private int position = -1;
    private final List<Messages> msgsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private ImageView backBtn;
    private String location, msgReceiverID = "", msgUsername = "", msgSenderID = "", senderUserId;
    private DatabaseReference usersRef, RootRef;
    private EditText message;
    private ImageButton sendMsg;
    private FirebaseAuth mAuth;
    private RecyclerView msgsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        location = getIntent().getExtras().get("loc").toString();
        msgReceiverID = getIntent().getExtras().get("msg_user_id").toString();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        username = findViewById(R.id.chatActivityUsername);
        message = findViewById(R.id.message1);
        sendMsg = findViewById(R.id.sendMsgIcon);

        mAuth = FirebaseAuth.getInstance();
        msgSenderID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        getIntentMethod();

        messageAdapter = new MessageAdapter(msgsList);
        msgsRecyclerView = findViewById(R.id.chatRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        msgsRecyclerView.setLayoutManager(linearLayoutManager);
        msgsRecyclerView.setAdapter(messageAdapter);

        backBtn = findViewById(R.id.chatScreenBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgActivity.super.onBackPressed();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        RootRef.child("Messages").child(msgSenderID).child(msgReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        Messages messages = snapshot.getValue(Messages.class);
                        msgsList.add(messages);
                        messageAdapter.notifyDataSetChanged();
                        msgsRecyclerView.smoothScrollToPosition(msgsRecyclerView.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendMessage() {

        String msgInputText = message.getText().toString();

        if (TextUtils.isEmpty(msgInputText)) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
        } else {
            String messageSenderRef = "Messages/" + msgSenderID + "/" + msgReceiverID;
            String messageReceiverRef = "Messages/" + msgReceiverID + "/" + msgSenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages").child(msgSenderID).child(msgReceiverID).push();

            String messagePushId = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", msgInputText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", msgSenderID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushId, messageTextBody);

            messageBodyDetails.put(messageReceiverRef + "/" + messagePushId, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MsgActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MsgActivity.this, "Message sending unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                    message.setText("");
                }
            });


        }
    }


    private void getIntentMethod() {

      /* if(location.equals("requests"))
       {
           username.setText("Receiver #"+chatRecipient.get(position).getUsername());
           position = getIntent().getIntExtra("position", -1);
           String sender = getIntent().getStringExtra("sender");
           if (sender!=null && sender.equals("chatDetails"))
           {
               chatRecipient = nameList;
           }
       }*/

        if (location.equals("requesterSide")) {
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(msgReceiverID).exists()) {
                        msgUsername = snapshot.child(msgReceiverID).child("userNumber").getValue().toString();
                        username.setText("Donor #" + msgUsername);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(msgReceiverID).exists()) {
                        msgUsername = snapshot.child(msgReceiverID).child("userNumber").getValue().toString();
                        username.setText("Receiver #" + msgUsername);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}