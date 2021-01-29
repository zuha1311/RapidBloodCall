package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView fullNameTextView, bloodGroup;
    private ImageView donateBlood,findDonors,navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fullNameTextView = findViewById(R.id.helloTextName);
        bloodGroup = findViewById(R.id.bloodGroupTextBloodDrop);
        donateBlood = findViewById(R.id.donateBloodBtn);
        findDonors = findViewById(R.id.findDonorsBtn);
        navMenu = findViewById(R.id.nav_menu);

        findDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,FindDonorsActivity.class);
                startActivity(intent);
            }
        });


        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,NavDrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        Intent intent = getIntent();
        String str = intent.getStringExtra("fullName");

        fullNameTextView.setText("Hello "+str);
        bloodGroup.setText(getIntent().getStringExtra("bloodGroup"));
    }



}