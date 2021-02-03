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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Chatting.ChatListAdapter;
import com.example.bloodbank.Chatting.Name;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    private DatabaseReference receiveRequestsRef, usersRef;
    private String currentUserId, current_state, senderID;
    private FirebaseAuth mAuth;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        recyclerView = findViewById(R.id.requestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        receiveRequestsRef = FirebaseDatabase.getInstance().getReference().child("Send_Requests");

        backBtn = findViewById(R.id.requestsBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestsActivity.super.onBackPressed();
            }
        });

        getRequests();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getRequests();
    }


    private void getRequests() {
        FirebaseRecyclerOptions<ReceiveRequests> option = new
                FirebaseRecyclerOptions.Builder<ReceiveRequests>()
                .setQuery(receiveRequestsRef.child(currentUserId), ReceiveRequests.class)
                .build();

        FirebaseRecyclerAdapter<ReceiveRequests, ReceiveRequestsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReceiveRequests, ReceiveRequestsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ReceiveRequestsViewHolder holder, int position, @NonNull ReceiveRequests model) {

                final String list_user_id = getRef(position).getKey();

                DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String type = snapshot.getValue().toString();
                            if (type.equals("Received")) {
                                usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String requesterUserNumber = snapshot.child("userNumber").getValue().toString();
                                        String requesterbloodGroup = snapshot.child("bloodGroup").getValue().toString();
                                        holder.userID.setText(requesterUserNumber);
                                        holder.bloodgroup.setText("Blood Group: " + requesterbloodGroup);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(RequestsActivity.this, "Still fetching default", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RequestsActivity.this, DonorLocationActivity.class);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ReceiveRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_item, parent, false);
                return new ReceiveRequestsViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }

    public static class ReceiveRequestsViewHolder extends RecyclerView.ViewHolder {

        TextView userID, bloodgroup;
        Button accept, decline;

        public ReceiveRequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.requesterID);
            bloodgroup = itemView.findViewById(R.id.bloodGroupTextViewRequest);
            accept = itemView.findViewById(R.id.acceptRequestBtn);
            decline = itemView.findViewById(R.id.declineRequestBtn);

        }


    }

}