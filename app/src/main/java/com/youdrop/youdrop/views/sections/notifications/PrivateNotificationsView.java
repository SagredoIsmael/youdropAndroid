package com.youdrop.youdrop.views.sections.notifications;

import android.content.Context;

import com.youdrop.youdrop.data.Notification;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface PrivateNotificationsView {

    Context getContext();
    Context getActivity();

    void clearList();
    void addNotificationsToList(List<Notification> notifications);
    void showEmpty(boolean showEmpty);

}
