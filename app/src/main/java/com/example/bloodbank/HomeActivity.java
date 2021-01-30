package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView fullNameTextView, bloodGroup,donorLine;
    private ImageView donateBlood,findDonors,navMenu,donorStatus;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fullNameTextView = findViewById(R.id.helloTextName);
        bloodGroup = findViewById(R.id.bloodGroupTextBloodDrop);
        donateBlood = findViewById(R.id.donateBloodBtn);
        findDonors = findViewById(R.id.findDonorsBtn);
        donorStatus = findViewById(R.id.donor_status_icon_approved);
        donorLine = findViewById(R.id.donation_Line);
        navMenu = findViewById(R.id.nav_menu);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        retrieveUserInfo();

        findDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FindDonorsActivity.class);
                startActivity(intent);
            }
        });


        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NavDrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });




    }

    private void retrieveUserInfo()
    {
        usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String namefromDB = snapshot.child("name").getValue(String.class);
                            String bloodgroupfromDB = snapshot.child("bloodGroup").getValue(String.class);
                            String statusfromDB = snapshot.child("donorStatus").getValue(String.class);

                            assert statusfromDB != null;
                            if(statusfromDB.equals("unapproved"))
                            {
                                donorStatus.setImageResource(R.drawable.waiting);
                                donorLine.setText("You have not been approved to donate blood");
                            }
                            else
                            {
                                donorStatus.setImageResource(R.drawable.approved);
                                donorLine.setText("You can donate");
                            }


                            fullNameTextView.setText("Hello " + namefromDB);
                            bloodGroup.setText(bloodgroupfromDB);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Error in retrieval ", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}