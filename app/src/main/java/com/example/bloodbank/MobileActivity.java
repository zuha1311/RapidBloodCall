package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MobileActivity extends AppCompatActivity {

    private EditText mobileNo;
    private Button disabledBtn, enabledBtn;

    private View ellipse1d,ellipse2d;

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
                if(length==10)
                {
                    enabledBtn.setVisibility(View.VISIBLE);

                    disabledBtn.setVisibility(View.INVISIBLE);

                    enabledBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MobileActivity.this,OTPActivity.class);
                            intent.putExtra("mobile",mobileNo.getText().toString());
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }
                    });

                }
                else
                {
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
}