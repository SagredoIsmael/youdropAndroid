package com.youdrop.youdrop.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.Utils;
import com.youdrop.youdrop.api.callbacks.GetSingleUserCallback;
import com.youdrop.youdrop.api.callbacks.GetUserImagesCallback;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.data.UserImage;
import com.youdrop.youdrop.data.results.UsersImageResult;
import com.youdrop.youdrop.views.adapters.UserImageRecyclerViewAdapter;
import com.youdrop.youdrop.views.sections.FriendsTabView;
import com.youdrop.youdrop.views.sections.ProfileView;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class ProfileController implements UserImageRecyclerViewAdapter.OnUserImageInteractionListener {

    private ProfileView view;

    private String authToken;
    String userId;

    private User user;
    UsersImageResult images;

    public void setImages(UsersImageResult images) {
        this.images = images;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private ContentManager contentManager;

    public ProfileController(ProfileView view) {
        this.view = view;
        getAuthToken();
        contentManager = ContentManager.getInstance(view.getContext());
    }

    private String getAuthToken(){
        authToken = Utils.getAuthToken(view.getContext());
        userId = Utils.getUserId(view.getContext());
        return authToken;
    }

    public void loadFromUserId(String id){
        contentManager.getUser(authToken, id, callback);
        contentManager.getUserImages(authToken, id, imagesCallback);
    }

    public void loadFromCurrentUser(){
        loadFromUserId(userId);
    }

    GetSingleUserCallback callback = new GetSingleUserCallback() {
        @Override
        public void onDataReceived(User user) {
            setUser(user);
            view.setUserData(user);
        }

        @Override
        public void onError(String error) {

        }
    };

    GetUserImagesCallback imagesCallback = new GetUserImagesCallback() {
        @Override
        public void onDataReceived(UsersImageResult images) {
            setImages(images);
            view.setUserImages(images.getItems());
        }
    };

    @Override
    public void onUserImageInteraction(UserImage item) {

    }
}
