package com.example.bloodbank.Home.DonateBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HospitalRegistration extends AppCompatActivity {

    EditText hname,haddress,htimings,hmobile;
    Button registerBtn;
    DatabaseReference hospitalRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_r);

        hname = findViewById(R.id.hNameEditText);
        haddress = findViewById(R.id.hAddressEditText);
        htimings = findViewById(R.id.hTimingsEditText);
        hmobile = findViewById(R.id.hmobileEditText);

        registerBtn = findViewById(R.id.registerashospital);

        hospitalRef  = FirebaseDatabase.getInstance().getReference().child("Hospitals");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = hname.getText().toString();
                final String address= haddress.getText().toString();
                final String timings = htimings.getText().toString();
                final String mobile = hmobile.getText().toString();
                saveProfileInfo(name,address,timings,mobile);
            }
        });
    }

    private void saveProfileInfo(String name, String address, String timings, String mobile) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userMessageKeyRef = RootRef.child("Hospitals").push();
        String messagePushId = userMessageKeyRef.getKey();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Hospitals").child(messagePushId).exists())
                {


                    HashMap<String,Object> hospitalMap = new HashMap<>();
                    hospitalMap.put("name",name);
                    hospitalMap.put("address",address);
                    hospitalMap.put("timings",timings);
                    hospitalMap.put("mobile","+91"+mobile);

                    RootRef.child("Hospitals").child(messagePushId).updateChildren(hospitalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(HospitalRegistration.this, "Hospital created", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}