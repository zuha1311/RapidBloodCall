package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonateBloodActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private ImageView backBtn;
    private DatabaseReference hospitalRef;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);
        
        recyclerView = findViewById(R.id.donateBloodRecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.donateBloodBackBtn);
        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
        
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonateBloodActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getHospitals();
    }

    private void getHospitals() {
        FirebaseRecyclerOptions<Hospitals> option = new
                FirebaseRecyclerOptions.Builder<Hospitals>()
                .setQuery(hospitalRef, Hospitals.class)
                .build();

        FirebaseRecyclerAdapter<Hospitals,DonateBloodViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Hospitals, DonateBloodViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull DonateBloodViewHolder holder, int position, @NonNull Hospitals model) {

                final String listUserId = getRef(position).getKey();
                hospitalRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists())
                       {
                           String hname = snapshot.child("name").getValue().toString();
                           String haddress = snapshot.child("address").getValue().toString();
                           String htimings = snapshot.child("timings").getValue().toString();
                           String hmobile = snapshot.child("mobile").getValue().toString();

                           holder.name.setText(hname);
                           holder.address.setText(haddress);
                           holder.timings.setText("Timings: "+ htimings);
                           holder.mobile.setText("Mobile No: "+hmobile);
                       }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public DonateBloodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_item, parent, false);
                return new DonateBloodActivity.DonateBloodViewHolder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class DonateBloodViewHolder extends RecyclerView.ViewHolder {

        TextView name,address,timings,mobile;

        public DonateBloodViewHolder(@NonNull View itemView) {
            super(itemView);

           name  = itemView.findViewById(R.id.hospitalName);
           address = itemView.findViewById(R.id.hospitalAddress);
           timings = itemView.findViewById(R.id.hospitalTimings);
           mobile = itemView.findViewById(R.id.hospitalMobileNo);


        }


    }
}