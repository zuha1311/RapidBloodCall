package com.example.bloodbank;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReceivedFragment extends Fragment {

    private RecyclerView recyclerView;
    private View receivedView;
    private DatabaseReference acceptedRequestsRef,usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;


    public ReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        receivedView = inflater.inflate(R.layout.fragment_received, container, false);
        recyclerView = receivedView.findViewById(R.id.receivedRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        acceptedRequestsRef = FirebaseDatabase.getInstance().getReference().child("Accepted_Requests").child(currentUserId).child("RequestMade");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        return receivedView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ReceivedBloodHistory> option = new
                FirebaseRecyclerOptions.Builder<ReceivedBloodHistory>()
                .setQuery(acceptedRequestsRef, ReceivedBloodHistory.class)
                .build();

        FirebaseRecyclerAdapter<ReceivedBloodHistory,ReceivedBloodHistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReceivedBloodHistory, ReceivedBloodHistoryViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ReceivedBloodHistoryViewHolder holder, int position, @NonNull ReceivedBloodHistory model) {
                final String list_user_id = getRef(position).getKey();

                usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String userNumber = snapshot.child("userNumber").getValue().toString();
                            holder.donorId.setText("#"+userNumber);
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
                            String rage = snapshot.child("patientAge").getValue().toString();
                            String rrelation = snapshot.child("patientRelation").getValue().toString();
                            String rgender = snapshot.child("patientGender").getValue().toString();
                            holder.relation.setText(rrelation);
                            holder.age.setText(rage);
                            holder.gender.setText(rgender);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public ReceivedBloodHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_item, parent, false);
                ReceivedBloodHistoryViewHolder viewHolder = new ReceivedBloodHistoryViewHolder((view));
                return viewHolder;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    public static class ReceivedBloodHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView age, donorId, relation, gender;

        public ReceivedBloodHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            age = itemView.findViewById(R.id.ageReceived);
            donorId = itemView.findViewById(R.id.donorIDreceiedfrgament);
            relation = itemView.findViewById(R.id.relationReceived);
            gender = itemView.findViewById(R.id.genderReceived);

        }
    }
}
