package com.example.bloodbank.Home.NavigationDrawer.Requests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;


public class RouteActivity extends AppCompatActivity {

    private MapViewLite mapView;

    private static final String TAG = RouteActivity.class.getSimpleName();
    private DirectionParser directionParser;
    private final static int MY_PERMISSIONS_REQUEST = 32;
    private PermissionRequester permissionsRequestor;
    private String senderiD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        senderiD = getIntent().getStringExtra("senderId");
        Toast.makeText(this, senderiD, Toast.LENGTH_SHORT).show();

        handleAndroidPermissions();


    }

    private void handleAndroidPermissions() {
        permissionsRequestor = new PermissionRequester(this);
        permissionsRequestor.request(new PermissionRequester.ResultListener(){

            @Override
            public void permissionsGranted() {
                loadMapScene();
            }

            @Override
            public void permissionsDenied() {
                Log.e(TAG, "Permissions denied by user.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsRequestor.onRequestPermissionsResult(requestCode, grantResults);
    }

    private void loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null) {
                    directionParser = new DirectionParser(senderiD,RouteActivity.this, mapView);
                } else {
                    Log.d(TAG, "onLoadScene failed: " + errorCode.toString());
                }
            }
        });
    }

    public void addRouteButtonClicked(View view) {
        directionParser.addRoute();
    }

    public void addWaypointsButtonClicked(View view) {
        directionParser.addWaypoints();
    }

    public void clearMapButtonClicked(View view) {
        directionParser.clearMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

