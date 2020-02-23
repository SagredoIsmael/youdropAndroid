package com.youdrop.youdrop.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.callbacks.GetContentCallback;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.results.PublicationsResult;
import com.youdrop.youdrop.views.adapters.ContentRecyclerViewAdapter;
import com.youdrop.youdrop.views.publications.PublicationActivity;
import com.youdrop.youdrop.views.sections.notifications.PublicNotificationsView;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class PublicNotificationsController implements ContentRecyclerViewAdapter.OnContentInteractionListener {

    private PublicNotificationsView view;

    private ContentManager api;

    private String authToken;

    private Location location;

    private FusedLocationProviderClient mFusedLocationClient;

    private PublicationsResult notifications = null;


    public PublicNotificationsController(PublicNotificationsView view) {
        this.view = view;
        api = ContentManager.getInstance(view.getContext());
    }

    private void getAuthToken() {
        SharedPreferences sharedPref = view.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        authToken = sharedPref.getString("authToken", null);
    }

    private boolean reloading = false;

    public void clear(){
        notifications = null;
    }

    public void reload() {
        if (authToken == null) getAuthToken();
        if (reloading) return;
        if (authToken != null && location != null && notifications == null) {
            api.getNotifications(authToken, location, callback);
            reloading = true;
        } else {
            if (notifications != null && notifications.getItems().size() != 0) {
                view.showEmpty(false);
                view.clearList();
                view.addContentToList(notifications.getItems());
            }
        }
    }

    GetContentCallback callback = new GetContentCallback() {
        @Override
        public void onDataReceived(PublicationsResult result) {
                notifications = result;
                if (result == null) return;
            if (result.getItems().size() != 0) {
                view.showEmpty(false);
                view.clearList();
                reloading = false;
                view.addContentToList(result.getItems());
            }
        }
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
                            PublicNotificationsController.this.location = location;
                            Utils.saveLocation(location);
                            reload();
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
                    PublicNotificationsController.this.location = location;
                    Utils.saveLocation(location);
                    reload();
                }
            }
        };
    };

    @Override
    public void onContentInteraction(Publication item) {
        Intent i = new Intent(view.getActivity(), PublicationActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(PublicationActivity.ARG_CONTENT, item);
        args.putParcelable("location", location);
        i.putExtras(args);
        view.getActivity().startActivityForResult(i, 123);
    }
}
