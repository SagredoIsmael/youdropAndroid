package com.youdrop.youdrop.controllers;

import android.Manifest;
import android.content.Context;
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
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.YoudropApi;
import com.youdrop.youdrop.api.callbacks.GetSingleContentCallback;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.views.publications.PublicationView;

/**
 * Created by pau on 16/09/17.
 */

public class PublicationController {

    private PublicationView view;

    private YoudropApi api;

    private String authToken;

    private Location location;
    private Publication publication;

    private String contentId;

    private boolean dirty = true;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }



    public PublicationController(PublicationView view) {
        this.view = view;
        api = YoudropApi.getInstance();
    }

    private void getAuthToken() {
        authToken = Utils.getAuthToken(view.getContext());
    }

    public void reload() {
        if (!dirty) return;
        if (authToken == null) getAuthToken();
        location = LocationUtils.getCurrentLocation();
        if (publication == null && contentId != null && location != null){
            getPublicationData(contentId);
        }

        if (authToken != null && location != null && publication != null) {
            if (publication.isLocked()){
                view.showLockedPublication(publication, location);
            } else {
                view.showUnlockedPublication(publication, location);
            }
            dirty = false;
        } else {
            //Show empty view
        }
    }








    private boolean loading = false;

    public void getPublicationData(String contentId){
        if (loading) return;
        loading = true;
        api.getContent(authToken, contentId, location, callback);
    }

    GetSingleContentCallback callback = new GetSingleContentCallback() {
        @Override
        public void onDataReceived(Publication publication) {
            loading = false;
            setPublication(publication);
            reload();
        }
    };

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
