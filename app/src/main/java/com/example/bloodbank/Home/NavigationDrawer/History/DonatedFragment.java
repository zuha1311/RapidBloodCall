package com.example.bloodbank.Home.NavigationDrawer.History;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloodbank.Home.NavigationDrawer.History.DonatedBloodHistory;
import com.example.bloodbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DonatedFragment extends Fragment {

    private RecyclerView recyclerView;
    private View donatedView;
    private DatabaseReference acceptedRequestsRef,usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    public DonatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        donatedView = inflater.inflate(R.layout.fragment_donated, container, false);
        recyclerView = donatedView.findViewById(R.id.donatedRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        acceptedRequestsRef = FirebaseDatabase.getInstance().getReference().child("Accepted_Requests").child(currentUserId).child("Request_Accepted");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        return donatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<DonatedBloodHistory> option = new
                FirebaseRecyclerOptions.Builder<DonatedBloodHistory>()
                .setQuery(acceptedRequestsRef, DonatedBloodHistory.class)
                .build();

        FirebaseRecyclerAdapter<DonatedBloodHistory, DonatedBloodHistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DonatedBloodHistory, DonatedBloodHistoryViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull DonatedBloodHistoryViewHolder holder, int position, @NonNull DonatedBloodHistory model) {
                final String list_user_id = getRef(position).getKey();

                usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String userNumber = snapshot.child("userNumber").getValue().toString();
                            holder.receiverId.setText("#"+userNumber);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                acceptedRequestsRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String dage = snapshot.child("patientAge").getValue().toString();
                            String drelation = snapshot.child("patientRelation").getValue().toString();
                            String dgender = snapshot.child("patientGender").getValue().toString();
                            holder.relation.setText(drelation);
                            holder.age.setText(dage);
                            holder.gender.setText(dgender);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public DonatedBloodHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donated_item, parent, false);
                DonatedBloodHistoryViewHolder viewHolder = new DonatedBloodHistoryViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class DonatedBloodHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView age, receiverId, relation, gender;

        public DonatedBloodHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            age = itemView.findViewById(R.id.ageDonated);
            receiverId = itemView.findViewById(R.id.receiverIDdonated);
            relation = itemView.findViewById(R.id.relationDonated);
            gender = itemView.findViewById(R.id.genderDonated);

        }
    }
}