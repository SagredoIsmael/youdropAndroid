package com.youdrop.youdrop.views.sections;

import android.content.Context;

import com.youdrop.youdrop.data.Notification;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface NotificationsView {

    Context getContext();

    void showPrivateNotifications();
    void showPublicNotifications();

    void reload();
}
