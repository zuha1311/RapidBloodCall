package com.example.bloodbank.Home.FindDonors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

public class FindDonorsActivity extends AppCompatActivity {
    private ChipGroup bloodTypeChipGroup, genderChipGroup, relationChipGroup;
    private EditText ageEditText;
    private ImageView sendRequestsShadow, sendRequestsBtn, backBtn;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private String requestStatus = "Pending";
    private long requestID = 0;
    private String bloodGroupSelection = null, relationSelection = null, genderSelection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donors);

        bloodTypeChipGroup = findViewById(R.id.bloodTypeChipGroup);
        relationChipGroup = findViewById(R.id.RelationChipGroup);
        genderChipGroup = findViewById(R.id.GenderChipGroup);
        ageEditText = findViewById(R.id.ageFindDonor);
        sendRequestsShadow = findViewById(R.id.sendRequestsShadow);
        sendRequestsBtn = findViewById(R.id.sendrequestsbtn);
        backBtn = findViewById(R.id.findDonorsBackBtn);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindDonorsActivity.super.onBackPressed();
            }
        });


        bloodTypeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                Chip chip = findViewById(checkedId);
                if (chip != null) {
                    genderChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(ChipGroup group, int checkedId) {
                            Chip chip = findViewById(checkedId);
                            if (chip != null) {
                                relationChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                                        Chip chip = findViewById(checkedId);

                                        if (chip != null) {
                                            sendRequestsShadow.setVisibility(View.INVISIBLE);
                                            sendRequestsBtn.setVisibility(View.VISIBLE);

                                        } else {
                                            sendRequestsShadow.setVisibility(View.VISIBLE);
                                            sendRequestsBtn.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }

                        }

                    });

                }

            }
        });
        onStart();



    }

    @Override
    protected void onStart() {
        super.onStart();
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        sendRequestsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (ageEditText.getText().toString().isEmpty()) {
                                    Toast.makeText(FindDonorsActivity.this, "Please enter the age to proceed", Toast.LENGTH_SHORT).show();
                                } else {
                                    getChipSelection();
                                }


                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void getChipSelection() {
        int bloodTypeChipCount = bloodTypeChipGroup.getChildCount();
        int relationChipCount = relationChipGroup.getChildCount();
        int genderChipCount = genderChipGroup.getChildCount();
        String ageSelection = ageEditText.getText().toString();


        for (int i = 0; i < bloodTypeChipCount; i++) {
            Chip child = (Chip) bloodTypeChipGroup.getChildAt(i);
            if (!child.isChecked()) {
                continue;
            } else {
                bloodGroupSelection = child.getText().toString();

            }
        }
        for (int i = 0; i < relationChipCount; i++) {
            Chip child = (Chip) relationChipGroup.getChildAt(i);
            if (!child.isChecked()) {
                continue;
            } else {
                relationSelection = child.getText().toString();
            }
        }
        for (int i = 0; i < genderChipCount; i++) {
            Chip child = (Chip) genderChipGroup.getChildAt(i);
            if (!child.isChecked()) {
                continue;
            } else {
                genderSelection = child.getText().toString();
            }
        }

        sendDatatoDatabase(bloodGroupSelection, genderSelection, relationSelection, ageSelection);

    }


    private void sendDatatoDatabase(String bloodGroupSelection, String genderSelection, String relationSelection, String ageSelection) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        try {

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    requestID = (snapshot.child("Requests").child(currentUserId).getChildrenCount());
                    long requestNo = requestID + 1;
                    if (!(snapshot.child("Requests").child(currentUserId)).exists()) {

                        HashMap<String, Object> requestsDataMap = new HashMap<>();
                        requestsDataMap.put("uid", currentUserId);
                        requestsDataMap.put("bloodGroup", bloodGroupSelection);
                        requestsDataMap.put("gender", genderSelection);
                        requestsDataMap.put("relation", relationSelection);
                        requestsDataMap.put("age", ageSelection);
                        requestsDataMap.put("status", requestStatus);
                        requestsDataMap.put("latitude", "");
                        requestsDataMap.put("longitude", "");
                        requestsDataMap.put("RequestId", String.valueOf(requestNo));


                        RootRef.child("Requests").child(currentUserId).child(String.valueOf(requestNo)).updateChildren(requestsDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(FindDonorsActivity.this, "Request sent to database", Toast.LENGTH_SHORT).show();
                                            bloodTypeChipGroup.clearCheck();
                                            genderChipGroup.clearCheck();
                                            relationChipGroup.clearCheck();
                                            ageEditText.setText("");

                                            Intent intent = new Intent(FindDonorsActivity.this, RequesterLocationMapActivity.class);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        } else {
                                            Toast.makeText(FindDonorsActivity.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    } else if (snapshot.child("Requests").child(currentUserId).exists()) {


                        Toast.makeText(FindDonorsActivity.this, "You have already made a request", Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(FindDonorsActivity.this, "Error", Toast.LENGTH_SHORT).show();

        }


    }

}