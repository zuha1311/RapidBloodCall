package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {

    Button registerForCheckeupBtn;
    EditText name,bloodGroup,dob,healthConditions,age;
    String[]  bloodTypes = {"A+","A-","B+","B-","AB+","AB-","O+","O-","a+","a-","b+","b-","ab+","ab-","o+","o-","Ab+","Ab-","aB+","aB-"};
    String checkblood;
    FirebaseDatabase database;
    private DatabaseReference UserReference;
    String phone;


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




        registerForCheckeupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkblood = bloodGroup.getText().toString();
                final int result = checkBloodTypes(bloodTypes,checkblood);
                if(name.getText().toString().isEmpty() ||
                        bloodGroup.getText().toString().isEmpty() ||
                        age.getText().toString().isEmpty()||
                       dob.getText().toString().isEmpty()||
                       healthConditions.getText().toString().isEmpty())
                {
                    Toast.makeText(SignUpActivity.this, "Please enter all the details to proceed", Toast.LENGTH_SHORT).show();
                }

                else if(result == -1)
                {
                    Toast.makeText(SignUpActivity.this, "Please enter valid blood type", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    Users obj = new Users(name.getText().toString(),
                            age.getText().toString(),dob.getText().toString(),
                            healthConditions.getText().toString(),
                            checkblood,"unapproved");

                    UserReference = database.getReference("Users");
                    UserReference.child(phone).setValue(obj);
                    Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);

                    intent.putExtra("fullName",name.getText().toString().toUpperCase());
                    intent.putExtra("bloodGroup",bloodGroup.getText().toString().toUpperCase());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

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


}