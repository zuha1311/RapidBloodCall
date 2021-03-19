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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RequestsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    private DatabaseReference receiveRequestsRef, usersRef, msgsRef;
    private String currentUserId, current_state, senderID;
    private FirebaseAuth mAuth;
    private ImageView backBtn;
    private String requesterUserNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);


        current_state = "new";

        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        receiveRequestsRef = FirebaseDatabase.getInstance().getReference().child("Send_Requests");
        msgsRef = FirebaseDatabase.getInstance().getReference().child("Messages from Not Accepted");

        recyclerView = findViewById(R.id.requestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        backBtn = findViewById(R.id.requestsBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestsActivity.super.onBackPressed();
            }
        });


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
                final String requesterUID = model.getUid();




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

                                        requesterUserNumber = snapshot.child("userNumber").getValue().toString();
                                        String requesterbloodGroup = snapshot.child("bloodGroup").getValue().toString();

                                        holder.userID.setText(requesterUserNumber);
                                        holder.bloodgroup.setText("Blood Group: " + requesterbloodGroup);
                                        holder.decline.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CancelDonationRequest(list_user_id);
                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            holder.message.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(RequestsActivity.this, MsgActivity.class);
                                    intent.putExtra("loc", "requests");
                                    intent.putExtra("msg_user_id", list_user_id);
                                    intent.putExtra("msg_user_name", requesterUserNumber);
                                    startActivity(intent);

                                }
                            });


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
                        intent.putExtra("senderID",list_user_id);
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

   /* private void messageRequester(String receiverUID) {

        receiveRequestsRef.child(senderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child(receiverUID).child("request_type").getValue().toString();

                if(type.equals("Received"))
                {
                    current_state = "request_received";
                    newMessageNotAccepted(senderID,receiverUID);

                }

                else
                {
                    msgsRef.child(senderID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(receiverUID))
                            {
                                current_state = "Acceptance Pending";

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void newMessageNotAccepted(String senderID, String receiverUID) {

        msgsRef.child(senderID).child(receiverUID).child("Status").setValue("Acceptance Pending")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            msgsRef.child(receiverUID).child(senderID).child("Status").setValue("Acceptance Pending")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    receiveRequestsRef.child(senderID).child(receiverUID)
                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                receiveRequestsRef.child(receiverUID).child(senderID)
                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        current_state = "Acceptance Pending";
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                        }
                                    });
                        }

                    }
                });
    }*/

    private void CancelDonationRequest(String requesterUID) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();

        receiveRequestsRef.child(currentUserId).child(requesterUID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            receiveRequestsRef.child(requesterUID).child(currentUserId)
                                    .removeValue().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RequestsActivity.this, "Request removed", Toast.LENGTH_SHORT).show();
                                                current_state = "new";

                                            }
                                        }
                                    }
                            );
                        }

                    }
                });


    }

    public static class ReceiveRequestsViewHolder extends RecyclerView.ViewHolder {

        TextView userID, bloodgroup;
        Button accept, decline, message;

        public ReceiveRequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.requesterID);
            bloodgroup = itemView.findViewById(R.id.bloodGroupTextViewRequest);
            accept = itemView.findViewById(R.id.acceptRequestBtn);
            decline = itemView.findViewById(R.id.declineRequestBtn);
            message = itemView.findViewById(R.id.messageRequesterBtn);


        }


    }

}