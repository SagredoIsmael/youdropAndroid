package com.youdrop.youdrop.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.youdrop.youdrop.api.YoudropApi;
import com.youdrop.youdrop.api.callbacks.GetContentCallback;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.results.PublicationsResult;
import com.youdrop.youdrop.views.sections.NearView;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class NearController {

    private NearView view;

    private YoudropApi api;

    private String authToken;

    private Location location;

    private FusedLocationProviderClient mFusedLocationClient;


    public NearController(NearView view) {
        this.view = view;
        api = YoudropApi.getInstance();

    }

    private void getAuthToken() {
        SharedPreferences sharedPref = view.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        authToken = sharedPref.getString("authToken", null);
    }

    public void reload() {
        if (authToken == null) getAuthToken();

        _reload();
    }
    private boolean reloading = false;

    private void _reload() {
        if (reloading) return;

        if (authToken != null && location != null) {
            reloading = true;
            api.getNearNotifications(authToken, location, callback);
        } else {
            //Show empty view
        }
    }

    GetContentCallback callback = new GetContentCallback() {
        @Override
        public void onDataReceived(PublicationsResult notifications) {
            reloading = false;
            view.clearList();
            if (notifications != null){
                view.addContentToList(notifications.getItems());
            }
        }
    };

    public boolean checkLocationPermission() {
        LocationManager service = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            view.getContext().startActivity(intent);
        }
        return enabled;
    }

    public void setupLocation() {
        if (ActivityCompat.checkSelfPermission(view.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(view.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);

            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(view.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            NearController.this.location = location;
                            reload();
                            view.moveTo(location, 10);
                        }
                    }
                });

        mFusedLocationClient.requestLocationUpdates(LocationRequest.create(),
                mLocationCallback,
                null /* Looper */);
    }

    public void pause() {
        if (mFusedLocationClient != null)mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public void resume() {
        setupLocation();
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    NearController.this.location = location;
                   // reload();
                    view.moveTo(location, 18);

                }
            }
        };
    };

}
