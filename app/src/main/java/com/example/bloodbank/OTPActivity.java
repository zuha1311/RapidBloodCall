package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3,inputCode4,inputCode5,inputCode6;
    private TextView mobileTextView, resendOTP;
    private Button verifyEnabled, verifyDisabled;

    FirebaseAuth mAuth;
    String otpid, newotpid;
    String phoneNumber;
    public String code;
    FirebaseDatabase database;
    DatabaseReference mobReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode1.requestFocus();
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);


        verifyEnabled = findViewById(R.id.verify_button_enabled);
        verifyDisabled = findViewById(R.id.verify_button_disabled);
        resendOTP = findViewById(R.id.resendOTPBtn);
        mobileTextView = findViewById(R.id.mobileText);
        mAuth = FirebaseAuth.getInstance();



        mobileTextView.setText("Enter the OTP sent to +91 "+ getIntent().getStringExtra("mobile"));
        phoneNumber = "+91"+ getIntent().getStringExtra("mobile");
        
        
        setOTPInputs();
        initiateOTP();
        verifyEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = inputCode1.getText().toString() + inputCode2.getText().toString() + inputCode3.getText().toString() + inputCode4.getText().toString() + inputCode5.getText().toString()
                + inputCode6.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, code);
                signInWithPhoneAuthCredential(credential);

            }
        });
        
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendtheOTP();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(newotpid, code);
                signInWithPhoneAuthCredential(credential);

            }
        });




    }

    private void resendtheOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                newotpid = s ;
                                Toast.makeText(OTPActivity.this, "OTP Resent", Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void initiateOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid = s ;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void setOTPInputs()
    {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {




            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {




            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!charSequence.toString().trim().isEmpty()) {
                inputCode6.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {




        }
    });
        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    verifyEnabled.setVisibility(View.VISIBLE);
                    verifyDisabled.setVisibility(View.INVISIBLE);


                }
                else
                {
                    verifyEnabled.setVisibility(View.INVISIBLE);
                    verifyDisabled.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Intent intent = new Intent(OTPActivity.this, SignUpActivity.class);
                            intent.putExtra("mobile",phoneNumber);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        } else {
                            Toast.makeText(OTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}