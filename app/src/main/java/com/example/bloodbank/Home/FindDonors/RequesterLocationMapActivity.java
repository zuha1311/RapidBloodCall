package com.example.bloodbank.Home.FindDonors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

public class RequesterLocationMapActivity extends AppCompatActivity {
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    private FirebaseAuth mAuth;
    private String currentUserId, senderID;
    private Button locationConfirm;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_location_map);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        client = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        locationConfirm = findViewById(R.id.confirmLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequesterLocationMapActivity.this, SendRequestsFromRequesterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        getMyLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            try {

                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                if (location != null) {
                                    double lat = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    LatLng latLng = new LatLng(lat, longitude);
                                    sendToDatabase(lat, longitude);

                                    Toast.makeText(RequesterLocationMapActivity.this,
                                            "Latitude:" + location.getLatitude() + "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();

                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                            .title("You are here");

                                    googleMap.addMarker(markerOptions);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25));
                                } else {
                                    Toast.makeText(RequesterLocationMapActivity.this, "Please enable location", Toast.LENGTH_SHORT).show();
                                }
                            }


                        });
                    }
                });


            } catch (SecurityException e) {
                Log.e("Exeption: %s", e.getMessage());
            }


        } else {
            onStart();
        }


    }


    private void sendToDatabase(double la, double lo) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.child("Requests").child(currentUserId).exists()) {
                        long requestID = (snapshot.child("Requests").child(currentUserId).getChildrenCount());

                        HashMap<String, Object> locDataMap = new HashMap<>();
                        locDataMap.put("latitude", String.valueOf(la));
                        locDataMap.put("longitude", String.valueOf(lo));

                        RootRef.child("Requests").child(currentUserId).child(String.valueOf(requestID)).updateChildren(locDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RequesterLocationMapActivity.this, "Location added to database", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RequesterLocationMapActivity.this, "error1", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(RequesterLocationMapActivity.this, "error2", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(RequesterLocationMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}