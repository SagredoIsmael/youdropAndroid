package com.youdrop.youdrop.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.youdrop.youdrop.api.LocationUtils;
import com.youdrop.youdrop.views.MainView;
import com.youdrop.youdrop.views.sections.friends.SearchUsersActivity;

/**
 * Created by pau on 16/09/17.
 */

public class MainController {

    public enum Section {
        NOTIFICATIONS,NEAR,ADD,FRIENDS,PROFILE
    }

    private Section section= Section.NOTIFICATIONS;

    private MainView view;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;


    public MainController(MainView view) {
        this.view = view;
    }

    public void checkAuth(){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
        String authToken = sharedPref.getString("authToken", null);
        if (authToken == null){
            view.showLogin();
        }
    }

    public String grtAUthToken(){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
        String authToken = sharedPref.getString("authToken", null);
        return authToken;
    }

    public void menuNotificationsClicked(){
        section = Section.NOTIFICATIONS;

        view.showNotiicationsPanel();
    }

    public void menuNearClicked(){
        view.showNearPanel();
        section = Section.NEAR;
    }

    public void menuAddClicked(){
        view.showAddPanel();
        section = Section.ADD;
    }

    public void menuFriendsClicked(){
        view.showFriendsPanel();
        section = Section.FRIENDS;
    }

    public void menuProfileClicked(){
        view.showProfilePanel();
        section = Section.PROFILE;
    }

    public Section getSection() {
        return section;
    }

    public void showFriendSearch(){

        Intent i = new Intent(view.getActivity(), SearchUsersActivity.class);
        view.getActivity().startActivity(i);
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
                    MainController.this.location = location;
                    LocationUtils.setCurrentLocation(location);
                }
            }
        };
    };


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
                            MainController.this.location = location;
                            LocationUtils.setCurrentLocation(location);
                        }
                    }
                });

        mFusedLocationClient.requestLocationUpdates(LocationRequest.create(),
                mLocationCallback,
                null /* Looper */);
    }
}
