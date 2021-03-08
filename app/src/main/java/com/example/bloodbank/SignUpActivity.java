package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.bloodbank.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    Button registerForCheckeupBtn;
    EditText name, bloodGroup, dob, healthConditions, age;
    String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "a+", "a-", "b+", "b-", "ab+", "ab-", "o+", "o-", "Ab+", "Ab-", "aB+", "aB-"};
    String checkblood;
    FirebaseDatabase database;
    private DatabaseReference UserReference;
    String phone;
    private String defaultStatus = "Unapproved";
    String currentUserId;
    private FirebaseAuth mAuth;
    long maxID = 1040;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerForCheckeupBtn = findViewById(R.id.register_for_checkup_btn);
        bloodGroup = findViewById(R.id.bloodGroupEditText);
        name = findViewById(R.id.fullNameEditText);
        dob = findViewById(R.id.dobEditText);
        healthConditions = findViewById(R.id.healthEditText);
        age = findViewById(R.id.AGEEditText);
        database = FirebaseDatabase.getInstance();
        UserReference = FirebaseDatabase.getInstance().getReference();
        phone = getIntent().getStringExtra("mobile");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        registerForCheckeupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkblood = bloodGroup.getText().toString();
                final int result = checkBloodTypes(bloodTypes, checkblood);
                if (name.getText().toString().isEmpty() ||
                        bloodGroup.getText().toString().isEmpty() ||
                        age.getText().toString().isEmpty() ||
                        dob.getText().toString().isEmpty() ||
                        healthConditions.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter all the details to proceed", Toast.LENGTH_SHORT).show();
                } else if (result == -1) {
                    Toast.makeText(SignUpActivity.this, "Please enter valid blood type", Toast.LENGTH_SHORT).show();

                } else {
                    final String nameforDB = name.getText().toString();
                    final String ageforDB = age.getText().toString();
                    final String dobforDB = dob.getText().toString();
                    final String healthConditionsforDB = healthConditions.getText().toString();
                    final String bloodGroupforDB = bloodGroup.getText().toString().toUpperCase();


                    saveProfileInfo(nameforDB, ageforDB, dobforDB, healthConditionsforDB, bloodGroupforDB, defaultStatus, phone);
                    //startLocationService();


                }

            }
        });
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length >0)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                startLocationService();
            }
            else
            {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private void saveProfileInfo(final String name, final String age, final String dob, final String healthCondition, final String bloodType, final String donorStatus, final String mobile) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("AB-")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();
                                                    compatibleMap.put("A-", true);
                                                    compatibleMap.put("B-", true);
                                                    compatibleMap.put("O-", true);
                                                    compatibleMap.put("AB-", true);

                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("AB+")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();
                                                    compatibleMap.put("A-", true);
                                                    compatibleMap.put("B-", true);
                                                    compatibleMap.put("O-", true);
                                                    compatibleMap.put("AB-", true);
                                                    compatibleMap.put("A+", true);
                                                    compatibleMap.put("B+", true);
                                                    compatibleMap.put("O+", true);
                                                    compatibleMap.put("AB+", true);

                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("O-")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();

                                                    compatibleMap.put("O-", true);


                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("O+")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();

                                                    compatibleMap.put("O-", true);
                                                    compatibleMap.put("O+", true);

                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("A-")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();
                                                    compatibleMap.put("A-", true);
                                                    compatibleMap.put("O-", true);

                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("A+")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();
                                                    compatibleMap.put("A-", true);
                                                    compatibleMap.put("A+", true);
                                                    compatibleMap.put("O-", true);
                                                    compatibleMap.put("O+", true);

                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("B-")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();

                                                    compatibleMap.put("B-", true);
                                                    compatibleMap.put("O-", true);


                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!(snapshot.child("Users").child(currentUserId).exists()) && bloodType.equals("B+")) {
                    maxID = (snapshot.child("Users").getChildrenCount());
                    long userID = 1040 + maxID + 1;
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("uid", currentUserId);
                    userDataMap.put("name", name);
                    userDataMap.put("age", age);
                    userDataMap.put("dateofBirth", dob);
                    userDataMap.put("healthConditions", healthCondition);
                    userDataMap.put("bloodGroup", bloodType);
                    userDataMap.put("compatibleWith", "");
                    userDataMap.put("donorStatus", donorStatus);
                    userDataMap.put("mobileNumber", mobile);
                    userDataMap.put("userNumber", String.valueOf(userID));

                    RootRef.child("Users").child(currentUserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        RootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if ((snapshot.child("Users").child(currentUserId).exists())) {
                                                    HashMap<String, Object> compatibleMap = new HashMap<>();
                                                    compatibleMap.put("O+", true);
                                                    compatibleMap.put("B-", true);
                                                    compatibleMap.put("O-", true);
                                                    compatibleMap.put("B+", true);

                                                    RootRef.child("Users").child(currentUserId).child("compatibleWith").updateChildren(compatibleMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        intent.putExtra("loc", "signUp");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static int checkBloodTypes(String[] arr, String toCheckValue) {
        Arrays.sort(arr);
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            int res = toCheckValue.compareTo(arr[m]);

            // Check if x is present at mid
            if (res == 0)
                return m;

            // If x greater, ignore left half
            if (res > 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }

        return -1;
    }

   /* private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;

    }
    private void startLocationService()
    {
        if(!isLocationServiceRunning())
        {
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }

    }

    private void stopLocationService()
    {
        if(isLocationServiceRunning())
        {
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
        }
    }*/

}

