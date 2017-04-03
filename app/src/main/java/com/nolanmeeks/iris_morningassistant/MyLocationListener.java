package com.nolanmeeks.iris_morningassistant;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by wip on 3/26/17.
 */

public class MyLocationListener implements LocationListener {
    public static double longitude, latitude;

    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
    public void onProviderEnabled(String provider)
    {
        // TODO Auto-generated method stub

    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
