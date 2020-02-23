package com.youdrop.youdrop.views;

import android.app.Activity;
import android.content.Context;

/**
 * Created by pau on 16/09/17.
 */

public interface MainView {

    public Activity getActivity();
    public void showNotiicationsPanel();
    public void showNearPanel();
    public void showAddPanel();
    public void showFriendsPanel();
    public void showProfilePanel();
    public Context getContext();
    public void showLogin();

}
