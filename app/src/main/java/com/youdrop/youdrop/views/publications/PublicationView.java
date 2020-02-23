package com.youdrop.youdrop.views.publications;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.youdrop.youdrop.data.Publication;

/**
 * Created by pau on 26/09/17.
 */

public interface PublicationView {
    Context getContext();
    Activity getActivity();

    void showLockedPublication(Publication publication, Location location);
    void showUnlockedPublication(Publication publication, Location location);
}
