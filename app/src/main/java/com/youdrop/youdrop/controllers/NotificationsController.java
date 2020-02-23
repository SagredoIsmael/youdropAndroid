package com.youdrop.youdrop.controllers;

import com.youdrop.youdrop.views.sections.NotificationsView;

/**
 * Created by pau on 16/09/17.
 */

public class NotificationsController {

    private NotificationsView view;



    public NotificationsController(NotificationsView view) {
        this.view = view;
    }


    public void showPrivateNotifications(){
        view.showPrivateNotifications();
    }

    public void showPublicNotifications(){
        view.showPublicNotifications();
    }


}
