package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bloodbank.Chatting.ChatListActivity;

public class NavDrawerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        drawerLayout = findViewById(R.id.drawer_layout1);
    }

    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);
    }

    private static  void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);

    }

    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public void ClickHome(View view)
    {
      super.onBackPressed();
    }

    public void ClickMsg(View view)
    {
        redirectActivity(this, ChatListActivity.class);
    }

    private void redirectActivity(Activity activity, Class aClass) {

        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void ClickRequests(View view)
    {
        redirectActivity(this,RequestsActivity.class);
    }

    public void ClickHistory(View view)
    {
        redirectActivity(this,HistoryActivity.class);
    }

    public void ClickSettings(View view)
    {
        redirectActivity(this,SettingActivity.class);
    }
    
    public void ClickSignOut(View view)
    {
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