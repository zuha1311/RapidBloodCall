package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SendRequestsFromRequesterActivity extends AppCompatActivity {

    private DatabaseReference findDonorsRef, sendRequestsRef, acceptReqNotifyRef;
    private String currentUserId, current_state, senderID, username = "";
    private FirebaseAuth mAuth;
    private RecyclerView showDonorsRecyclerView;
    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_requests_from_requester);
        current_state = "new";


        backBtn = findViewById(R.id.requesterrequestsBackBtn);
        sendRequestsRef = FirebaseDatabase.getInstance().getReference().child("Send_Requests");
        acceptReqNotifyRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendRequestsFromRequesterActivity.super.onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        showDonorsRecyclerView = findViewById(R.id.sendrequestsRecyclerView);
        showDonorsRecyclerView.setHasFixedSize(true);
        showDonorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findDonorsRef = FirebaseDatabase.getInstance().getReference().child("Users");


    }

    @Override
    protected void onStart() {
        super.onStart();
        getRequesterBloodType();

    }

    private void getRequesterBloodType() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        try {
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Requests").child(currentUserId).exists()) {

                        long requestID = (snapshot.child("Requests").child(currentUserId).getChildrenCount());
                        senderID = snapshot.child("Users").child(currentUserId).child("uid").getValue().toString();

                        String bloodfromDB = snapshot.child("Requests").child(currentUserId).child(String.valueOf(requestID)).child("bloodGroup").getValue(String.class);

                        Toast.makeText(SendRequestsFromRequesterActivity.this, bloodfromDB, Toast.LENGTH_SHORT).show();
                        assert bloodfromDB != null;
                        findDonors(bloodfromDB, senderID);
                    } else {
                        Toast.makeText(SendRequestsFromRequesterActivity.this, "not fetching", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void findDonors(String bloodGroup, String senderID) {

        FirebaseRecyclerOptions<FindDonors> option = null;
        Query query = findDonorsRef.orderByChild("compatibleWith/".concat(bloodGroup)).equalTo(true);

        option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                .setQuery(query, FindDonors.class)
                .build();
        FirebaseRecyclerAdapter<FindDonors, FindDonorViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindDonors, FindDonorViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull FindDonorViewHolder holder, int position, @NonNull FindDonors model) {
                final String list_user_id = getRef(position).getKey();
                holder.userID.setText(model.getUserNumber());
                holder.bloodgroup.setText("Blood Group: " + model.getBloodGroup());
                String receiverID = model.getUid();


                holder.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manageRequests(receiverID, senderID);
                        if (current_state.equals("new")) {
                            holder.send.setText("CANCEL");
                        } else {
                            holder.send.setText("SEND");

                        }
                    }
                });

                holder.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage(list_user_id);


                    }
                });

            }


            @NonNull
            @Override
            public FindDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_requests_to_donor_layout, parent, false);
                return new FindDonorViewHolder(view);
            }
        };

        showDonorsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void sendMessage(String list_user_id) {
        findDonorsRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username = snapshot.child("userNumber").getValue().toString();
                    Intent intent = new Intent(SendRequestsFromRequesterActivity.this, MsgActivity.class);
                    intent.putExtra("loc", "requesterSide");
                    intent.putExtra("msg_user_id", list_user_id);
                    intent.putExtra("msg_user_name", username);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void manageRequests(String receiverID, String senderID) {


        sendRequestsRef.child(senderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(receiverID)) {
                    String request_type = snapshot.child(receiverID).child("request_type")
                            .getValue().toString();

                    if (request_type.equals("Sent")) {
                        current_state = "request_sent";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (!senderID.equals(receiverID)) {
            if (current_state.equals("new")) {
                sendRequest(receiverID, senderID);
            }
            if (current_state.equals("request_sent")) {
                cancelDonateRequest(receiverID, senderID);
            }
        } else {
            Toast.makeText(this, "You cannot send requests to yourself!", Toast.LENGTH_SHORT).show();

        }

    }

    private void cancelDonateRequest(String receiverID, String senderID) {

        sendRequestsRef.child(senderID).child(receiverID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            sendRequestsRef.child(receiverID).child(senderID)
                                    .removeValue().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SendRequestsFromRequesterActivity.this, "Request removed", Toast.LENGTH_SHORT).show();
                                                current_state = "new";

                                            }
                                        }
                                    }
                            );
                        }

                    }
                });

    }

    private void sendRequest(String reciverID, String senderID) {

        sendRequestsRef.child(senderID).child(reciverID)
                .child("request_type").setValue("Sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            sendRequestsRef.child(reciverID).child(senderID).child("request_type")
                                    .setValue("Received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                HashMap<String, Object> msgsNotificationMap = new HashMap<>();
                                                msgsNotificationMap.put("from", senderID);
                                                msgsNotificationMap.put("type", "msgsRequest");

                                                acceptReqNotifyRef.child(reciverID).push()
                                                        .setValue(msgsNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    current_state = "request_sent";

                                                                    Toast.makeText(SendRequestsFromRequesterActivity.this, "Request sent to database", Toast.LENGTH_SHORT).show();


                                                                }
                                                            }
                                                        });


                                            }

                                        }
                                    });
                        }

                    }
                });


    }


    public static class FindDonorViewHolder extends RecyclerView.ViewHolder {

        TextView userID, bloodgroup;
        Button send, message;

        public FindDonorViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.donorID);
            bloodgroup = itemView.findViewById(R.id.bloodGroupTextView);
            send = itemView.findViewById(R.id.sendRequestBtn);
            message = itemView.findViewById(R.id.messageDonorBtn);


        }


    }
}

