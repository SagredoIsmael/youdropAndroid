package com.youdrop.youdrop.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.youdrop.youdrop.api.ContentManager;
import com.youdrop.youdrop.api.callbacks.GetNotificationsCallback;
import com.youdrop.youdrop.data.Notification;
import com.youdrop.youdrop.data.results.NotificationsResult;
import com.youdrop.youdrop.views.ProfileActivity;
import com.youdrop.youdrop.views.sections.notifications.PrivateNotificationsFragment;
import com.youdrop.youdrop.views.sections.notifications.PrivateNotificationsView;
import com.youdrop.youdrop.views.publications.PublicationActivity;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public class PrivateNotificationsController implements PrivateNotificationsFragment.OnNotificationInteractionListener {

    private PrivateNotificationsView view;

    private ContentManager api;

    private String authToken;


    public PrivateNotificationsController(PrivateNotificationsView view) {
        this.view = view;
        api = ContentManager.getInstance(view.getContext());
    }

    private void getAuthToken(){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences( "session", Context.MODE_PRIVATE);
        authToken = sharedPref.getString("authToken", null);
    }
    private boolean reloading = false;

    private NotificationsResult notifications = null;

    public void clear(){
        notifications = null;
    }

    public void reload(){
        if (authToken == null) getAuthToken();
        if (reloading)return;
        if (authToken != null && notifications == null){
            reloading = true;
            api.getPrivateNotifications(authToken, callback);
        } else {
            if (notifications != null && notifications.getItems().size() != 0) {
                view.showEmpty(false);
                view.clearList();
                view.addNotificationsToList(notifications.getItems());            }
        }
    }

    GetNotificationsCallback callback = new GetNotificationsCallback() {

        @Override
        public void onDataReceived(NotificationsResult result) {
            reloading = false;
            notifications = result;
            if (result.getItems().size() != 0) {
                view.showEmpty(false);
                view.clearList();
                view.addNotificationsToList(result.getItems());            }

        }
    };

    @Override
    public void onNotificationInteraction(Notification item) {
        if (item.getPublication() != null && item.getKey() != null && ( item.getKey().equals("PUBLICATION") || item.getKey().equals("COMMENT") || item.getKey().equals("LIKE"))){  //PUBLICATION COMMENT LIKE
            Intent i = new Intent(view.getActivity(), PublicationActivity.class);
            Bundle args = new Bundle();
            args.putSerializable(PublicationActivity.ARG_CONTENT, item.getPublication());
            //args.putParcelable("location", new Location(""));
            i.putExtras(args);
            view.getActivity().startActivity(i);
        }else
        if (item.getPublication() != null && item.getKey() == null ){  //PUBLICATION COMMENT LIKE
            Intent i = new Intent(view.getActivity(), PublicationActivity.class);
            Bundle args = new Bundle();
            args.putSerializable(PublicationActivity.ARG_CONTENT, item.getPublication());
            //args.putParcelable("location", new Location(""));
            i.putExtras(args);
            view.getActivity().startActivity(i);
        }else
        if (item.getProfile() != null && ( item.getKey().equals("FRIENDSHIP") || item.getKey().equals("FOLLOW"))){ // FRIENDSHIP FOLLOW
            Intent i = new Intent(view.getActivity(), ProfileActivity.class);
            Bundle args = new Bundle();
            args.putString(ProfileActivity.ARG_USERID, item.getProfile().getId());
            i.putExtras(args);
            view.getActivity().startActivity(i);
        }
    }
}
