package com.nolanmeeks.iris_morningassistant;

/**
 * Created by wip on 3/2/17.
 */
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;
//import android.support.v7.app.AppCompatActivity;
//import java.io.*;

public class Weather extends AsyncTask<String, Void, HashMap<String, String>> {
  public HashMap doInBackground(String ... a) {
    double location[] = getLocation();
    try {
      String locKey = ProcessJSON.getLocationKey(location[0], location[1]);
      System.out.println("[!!] Location Key Acquired!!");
      if(a[0].equals("current")) {
            return ProcessJSON.getCurrentForecast(locKey);
      } else if (a[0].equals("hourly")) {
        return ProcessJSON.getHourlyForecast(locKey);
      } else{
        return ProcessJSON.getDailyForecast(locKey);
      }
    } catch (Exception e) {
      System.err.println("[!!] Failed to get Document!!");
      System.err.println(e);
    }
    return null;
  }

    public double[] getLocation() {
        double[] loc = new double[2];
        try {
            Location location = HomeScreen.locMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location == null) {
                throw new IOException("Location returned null!!!");
            }
            loc[0] = location.getLatitude();
            loc[1] = location.getLongitude();
        }   catch (SecurityException e) {
            //Do something if permissions not set
            System.out.println("[!!] Permissions not set!!");
            System.err.println(e);
        }  catch (Exception e) {
            System.err.println(e);
        }
        return loc;
    }
}

