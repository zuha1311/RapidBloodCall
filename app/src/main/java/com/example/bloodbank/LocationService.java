package com.example.bloodbank;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.bloodbank.util.Constants;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();

                Log.d("Location Update:","Latitude"+latitude+ "Longitude"+longitude);
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startLocationService() {
        String channelID = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), channelID
        );
        builder.setContentText("Blood Bank");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Your location is being updated");
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelID,
                        "Blood Bank",
                        NotificationManager.IMPORTANCE_HIGH

                );
                notificationChannel.setDescription("This channel is being used by blood bank");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constants.LOCATION_SERVICE_ID,builder.build());

    }

    private void stopLocationService()
    {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null)
        {
            String action = intent.getAction();
            if(action != null)
            {
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE))
                {
                    startLocationService();
                }
                else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE))
                {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
