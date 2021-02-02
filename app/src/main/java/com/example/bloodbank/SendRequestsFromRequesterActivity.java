package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SendRequestsFromRequesterActivity extends AppCompatActivity {

    String bloodGroup1;
    private DatabaseReference findDonorsRef;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private RecyclerView showDonorsRecyclerView;
    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_requests_from_requester);

        backBtn = findViewById(R.id.requesterrequestsBackBtn);

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
        getRequesterBloodType();
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
                    if (!snapshot.child("Requests").child(currentUserId).exists()) {
                        long requestID = (snapshot.child("Requests").getChildrenCount());

                        String bloodfromDB = snapshot.child("Requests").child(String.valueOf(requestID)).child("bloodGroup").getValue(String.class);
                        Toast.makeText(SendRequestsFromRequesterActivity.this, bloodfromDB, Toast.LENGTH_SHORT).show();
                        assert bloodfromDB != null;
                        findDonors(bloodfromDB);
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


    private void findDonors(String bloodGroup) {

        FirebaseRecyclerOptions<FindDonors> option = null;
        Query query = findDonorsRef.orderByChild("bloodGroup");

        switch (bloodGroup) {
            case "O-":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-"), FindDonors.class)
                        .build();
                break;
            case "O+":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-").equalTo("A-"), FindDonors.class)
                        .build();
                break;
            case "A-":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-").equalTo("O+"), FindDonors.class)
                        .build();
                break;
            case "A+":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-").equalTo("O+").equalTo("A+").equalTo("A-"), FindDonors.class)
                        .build();
                break;
            case "B-":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-").equalTo("B-"), FindDonors.class)
                        .build();
                break;
            case "B+":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-").equalTo("O+").equalTo("B-").equalTo("B+"), FindDonors.class)
                        .build();
                break;
            case "AB-":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.equalTo("O-").equalTo("A-").equalTo("B-").equalTo("AB-"), FindDonors.class)
                        .build();
                break;
            case "AB+":
                option = new FirebaseRecyclerOptions.Builder<FindDonors>()
                        .setQuery(query.endAt("\uf8ff"), FindDonors.class)
                        .build();
                break;
        }
        FirebaseRecyclerAdapter<FindDonors, FindDonorViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindDonors, FindDonorViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull FindDonorViewHolder holder, int position, @NonNull FindDonors model) {
                holder.userID.setText(model.getUserNumber());
                holder.bloodgroup.setText("Blood Group: " + model.getBloodGroup());

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


    public static class FindDonorViewHolder extends RecyclerView.ViewHolder {

        TextView userID, bloodgroup;

        public FindDonorViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.donorID);
            bloodgroup = itemView.findViewById(R.id.bloodGroupTextView);
        }


    }
}