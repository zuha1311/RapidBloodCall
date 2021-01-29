package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class FindDonorsActivity extends AppCompatActivity {
        ChipGroup bloodTypeChipGroup, genderChipGroup, relationChipGroup;
        EditText ageEditText;
        ImageView sendRequestsShadow, sendRequestsBtn;


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

        int bloodTypeChipCount = bloodTypeChipGroup.getChildCount();
        int relationChipCount = relationChipGroup.getChildCount();
        int genderChipCount = genderChipGroup.getChildCount();



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
                    Intent intent = new Intent(FindDonorsActivity.this, HistoryActivity.class);
                    startActivity(intent);
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

   /* private String getSelectedText(ChipGroup group, int checkedId) {

        Chip chip = group.findViewById(checkedId);
        return chip.toString();
    }*/


}