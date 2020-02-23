package com.youdrop.youdrop.views.sections;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.youdrop.youdrop.data.Publication;

import java.util.List;

/**
 * Created by pau on 16/09/17.
 */

public interface NearView {

    Context getContext();
    Activity getActivity();

    void clearList();
    void addContentToList(List<Publication> publication);

    void moveTo(Location location, float zoom);

}
