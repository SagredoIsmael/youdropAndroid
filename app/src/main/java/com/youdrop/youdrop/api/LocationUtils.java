package com.youdrop.youdrop.api;

import android.location.Location;

/**
 * Created by pau on 20/12/17.
 */

public class LocationUtils {

    private static Location currentLocation;

    public static Location getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(Location currentLocation) {
        LocationUtils.currentLocation = currentLocation;
    }
}
