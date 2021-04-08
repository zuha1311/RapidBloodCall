package com.example.bloodbank.Chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.MsgActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.ReceiveRequests;
import com.example.bloodbank.RequestsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatListActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageView backBtn;
    private View chatListView;
    private DatabaseReference msgsRef,usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.recycler_view1);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        msgsRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        
        backBtn = findViewById(R.id.chatListBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatListActivity.super.onBackPressed();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        getChatList();
    }

    private void getChatList() {
        FirebaseRecyclerOptions<Name> option = new
                FirebaseRecyclerOptions.Builder<Name>()
                .setQuery(msgsRef.child(currentUserId), Name.class)
                .build();

        FirebaseRecyclerAdapter<Name,ChatListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Name, ChatListViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ChatListViewHolder holder, int position, @NonNull Name model) {
                final String list_user_id = getRef(position).getKey();
                usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String userNumber = snapshot.child("userNumber").getValue().toString();
                            holder.userID.setText("User #"+userNumber);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChatListActivity.this,MsgActivity.class);
                                    intent.putExtra("loc", "chatList");
                                    intent.putExtra("msg_user_id", list_user_id);
                                    intent.putExtra("msg_user_name", userNumber);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                return new ChatListViewHolder(view);

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView userID;


        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.chatListUsername);



        }


    }
}
