package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.TimeUnit;

public class MobileActivity extends AppCompatActivity {

    private EditText mobileNo;
    private Button disabledBtn, enabledBtn;

    private View ellipse1d, ellipse2d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        mobileNo = findViewById(R.id.mobileNumber);
        disabledBtn = findViewById(R.id.get_otp_button_disabled);
        enabledBtn = findViewById(R.id.get_otp_button_enabled);

        ellipse1d = findViewById(R.id.ellipse_6_disabled);
        ellipse2d = findViewById(R.id.ellipse_7_disabled);


        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = mobileNo.length();
                if (length == 10) {
                    enabledBtn.setVisibility(View.VISIBLE);

                    disabledBtn.setVisibility(View.INVISIBLE);

                    enabledBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                             Intent intent = new Intent(MobileActivity.this, OTPActivity.class);
                            intent.putExtra("mobile", mobileNo.getText().toString());

                            startActivity(intent);
                            finish();

                        }
                    });
                } else {
                    enabledBtn.setVisibility(View.INVISIBLE);
                    disabledBtn.setVisibility(View.VISIBLE);

                    ellipse1d.setVisibility(View.VISIBLE);
                    ellipse2d.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth mAuth;
        String currentUser;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            currentUser = mAuth.getCurrentUser().getUid();


            DatabaseReference checkRef;
            checkRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
            checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String namefromDB = snapshot.child("name").getValue(String.class);
                        String bloodgroupfromDB = snapshot.child("bloodGroup").getValue(String.class);
                        String statusfromDB = snapshot.child("donorStatus").getValue(String.class);



                        Intent homeIntent = new Intent(MobileActivity.this,HomeActivity.class);
                        homeIntent.putExtra("loc","exists");
                        homeIntent.putExtra("name",namefromDB);
                        homeIntent.putExtra("bloodGroup",bloodgroupfromDB);
                        homeIntent.putExtra("status",statusfromDB);
                        startActivity(homeIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if( firebaseUser != null)
        {

        }*/
    }


}
