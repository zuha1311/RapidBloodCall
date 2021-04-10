package com.example.bloodbank.Home.NavigationDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Home.NavigationDrawer.Messages.ChatListActivity;
import com.example.bloodbank.Home.NavigationDrawer.History.HistoryActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.Home.NavigationDrawer.Requests.RequestsActivity;
import com.example.bloodbank.Home.NavigationDrawer.Settings.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavDrawerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView userID, donorStatusNavDrawer;
    private FirebaseAuth mAuth;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        drawerLayout = findViewById(R.id.drawer_layout1);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        showDetailsinNavDrawer();
    }

    private void showDetailsinNavDrawer() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uniqueIdfromDB = snapshot.child("userNumber").getValue(String.class);
                    String statusfromDB = snapshot.child("donorStatus").getValue(String.class);

                    userID = findViewById(R.id.donorUniqueIdNavDrawer);
                    donorStatusNavDrawer = findViewById(R.id.donorStatusNavDrawer);

                    userID.setText("Donor #" + uniqueIdfromDB);
                    donorStatusNavDrawer.setText("Status : " + statusfromDB);
                } else {
                    Toast.makeText(NavDrawerActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);

    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public void ClickHome(View view) {
        super.onBackPressed();
    }

    public void ClickMsg(View view) {
        redirectActivity(this, ChatListActivity.class);
    }

    private void redirectActivity(Activity activity, Class aClass) {

        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void ClickRequests(View view) {
        redirectActivity(this, RequestsActivity.class);
    }

    public void ClickHistory(View view) {
        redirectActivity(this, HistoryActivity.class);
    }

    public void ClickSettings(View view) {
        redirectActivity(this, SettingActivity.class);
    }

    public void ClickSignOut(View view) {
        logout(this);
    }

    private static void logout(Activity activity) {

        activity.finishAffinity();
        System.exit(0);

    }

    @Override
    protected void onPause() {
        super.onPause();

        closeDrawer(drawerLayout);
    }
}