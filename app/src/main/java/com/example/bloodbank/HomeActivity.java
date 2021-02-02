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

import com.example.bloodbank.util.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView fullNameTextView, bloodGroup, donorLine;
    private ImageView donateBloodEnabled,donateBloodDisabled, findDonors, navMenu, donorStatus;
    private DatabaseReference usersSignUpRef,usersDirectRef;
    private FirebaseAuth mAuth;
    private String currentUserid;
    String uid;
    String loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        donateBloodEnabled = findViewById(R.id.donateBloodBtnEnabled);
        donateBloodDisabled = findViewById(R.id.donateBloodBtnDisabled);
        loc = getIntent().getStringExtra("loc").toString();
        findDonors = findViewById(R.id.findDonorsBtn);
        donorStatus = findViewById(R.id.donor_status_icon_approved);
        donorLine = findViewById(R.id.donation_Line);
        navMenu = findViewById(R.id.nav_menu);
        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        if(loc.equals("signUp"))
        {
            retrieveUserInfoSignUp();

        }
        else if(loc.equals("exists"))
        {
            retireveUserInfoDirect();
        }


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

    private void retireveUserInfoDirect() {

        String name = getIntent().getStringExtra("name").toString();
        String bloodGroup1 = getIntent().getStringExtra("bloodGroup").toString();
        String donorStatus1 = getIntent().getStringExtra("status").toString();


        if (donorStatus1.equals("Unapproved")) {
            donorStatus.setImageResource(R.drawable.waiting);
            donorLine.setText("You cannot donate");
            donateBloodDisabled.setVisibility(View.VISIBLE);
            donateBloodEnabled.setVisibility(View.INVISIBLE);
        } else {
           donorStatus.setImageResource(R.drawable.approved);
            donorLine.setText("You can donate");
            donateBloodDisabled.setVisibility(View.INVISIBLE);
            donateBloodEnabled.setVisibility(View.VISIBLE);
        }
        fullNameTextView = findViewById(R.id.helloTextName);
        bloodGroup = findViewById(R.id.bloodGroupTextBloodDrop);

        fullNameTextView.setText("Hello " + name);
        bloodGroup.setText(bloodGroup1.toUpperCase());


    }

    private void retrieveUserInfoSignUp() {
        usersSignUpRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);
        usersSignUpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String namefromDB = snapshot.child("name").getValue(String.class);
                    String bloodgroupfromDB = snapshot.child("bloodGroup").getValue(String.class);
                    String statusfromDB = snapshot.child("donorStatus").getValue(String.class);

                    assert statusfromDB != null;
                    if (statusfromDB.equals("Unapproved")) {
                        donorStatus.setImageResource(R.drawable.waiting);
                        donorLine.setText("You cannot donate");
                        donateBloodDisabled.setVisibility(View.VISIBLE);
                        donateBloodEnabled.setVisibility(View.INVISIBLE);
                    } else if(statusfromDB.equals("Approved")) {
                        donorStatus.setImageResource(R.drawable.approved);
                        donorLine.setText("You can donate");
                        donateBloodDisabled.setVisibility(View.INVISIBLE);
                        donateBloodEnabled.setVisibility(View.VISIBLE);
                    }
                    fullNameTextView = findViewById(R.id.helloTextName);
                    bloodGroup = findViewById(R.id.bloodGroupTextBloodDrop);

                    fullNameTextView.setText("Hello " + namefromDB);
                    bloodGroup.setText(bloodgroupfromDB.toUpperCase());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error in retrieval ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}