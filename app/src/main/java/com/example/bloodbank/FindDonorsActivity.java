package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.HashMap;

public class FindDonorsActivity extends AppCompatActivity {
        ChipGroup bloodTypeChipGroup, genderChipGroup, relationChipGroup;
        EditText ageEditText;
        ImageView sendRequestsShadow, sendRequestsBtn;

        private FirebaseAuth mAuth;
        private String currentUserId;
        private String requestStatus = "Pending";
        private long requestID = 0;


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
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        bloodTypeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                Chip chip = findViewById(checkedId);
                if(chip != null)
                {
                    genderChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(ChipGroup group, int checkedId) {
                            Chip chip = findViewById(checkedId);
                            if(chip != null)
                            {
                                relationChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                                        Chip chip = findViewById(checkedId);

                                        if(chip != null )
                                        {
                                            sendRequestsShadow.setVisibility(View.INVISIBLE);
                                            sendRequestsBtn.setVisibility(View.VISIBLE);

                                        }
                                        else
                                        {
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

        sendRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ageEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(FindDonorsActivity.this, "Please enter the age to proceed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getChipSelection();
                }



            }
        });








       /* if(FLAG && relationChipCount == 0 && genderChipCount ==0 )
        {
            Toast.makeText(this, "Please select all the details", Toast.LENGTH_SHORT).show();
        }
        else if(bloodTypeChipCount == 1 && relationChipCount == 1 && genderChipCount ==1)
        {
                    sendRequestsShadow.setVisibility(View.INVISIBLE);
        }*/



    }

    private void getChipSelection() {
        int bloodTypeChipCount = bloodTypeChipGroup.getChildCount();
        int relationChipCount = relationChipGroup.getChildCount();
        int genderChipCount = genderChipGroup.getChildCount();
        String ageSelection = ageEditText.getText().toString();
        String bloodGroupSelection = null, relationSelection = null,genderSelection = null;

        for(int i=0; i<bloodTypeChipCount; i++)
        {
            Chip child = (Chip) bloodTypeChipGroup.getChildAt(i);
            if(!child.isChecked())
            {
                continue;
            }
            else
            {
                bloodGroupSelection = child.getText().toString();
            }
        }
        for(int i=0; i<relationChipCount; i++)
        {
            Chip child = (Chip)relationChipGroup.getChildAt(i);
            if(!child.isChecked())
            {
                continue;
            }
            else
            {
                relationSelection = child.getText().toString();
            }
        }
        for(int i=0; i<genderChipCount; i++)
        {
            Chip child = (Chip) genderChipGroup.getChildAt(i);
            if(!child.isChecked())
            {
                continue;
            }
            else
            {
                genderSelection = child.getText().toString();
            }
        }

        sendDatatoDatabase(bloodGroupSelection,genderSelection,relationSelection,ageSelection);

    }

    private void sendDatatoDatabase(String bloodGroupSelection, String genderSelection, String relationSelection, String ageSelection) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Requests").child(currentUserId)).exists())
                {
                    requestID =  (snapshot.child("Requests").getChildrenCount());
                    long requestNo = requestID + 1;
                    HashMap<String,Object> requestsDataMap  = new HashMap<>();
                    requestsDataMap.put("uid", currentUserId);
                    requestsDataMap.put("bloodGroup",bloodGroupSelection);
                    requestsDataMap.put("gender",genderSelection);
                    requestsDataMap.put("relation",relationSelection);
                    requestsDataMap.put("age",ageSelection);
                    requestsDataMap.put("status",requestStatus);
                    requestsDataMap.put("RequestId",String.valueOf(requestNo));


                    RootRef.child("Requests").child(String.valueOf(requestNo)).updateChildren(requestsDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(FindDonorsActivity.this, "Request sent to database", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(FindDonorsActivity.this, HistoryActivity.class);
                                        startActivity(intent);

                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    }
                                    else
                                    {
                                        Toast.makeText(FindDonorsActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else
                {
                    Toast.makeText(FindDonorsActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

   /* private String getSelectedText(ChipGroup group, int checkedId) {

        Chip chip = group.findViewById(checkedId);
        return chip.toString();
    }*/


}