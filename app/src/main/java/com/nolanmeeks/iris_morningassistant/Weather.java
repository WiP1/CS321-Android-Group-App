package com.nolanmeeks.iris_morningassistant;

/**
 * Created by wip on 3/2/17.
 */
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.location.Address;

//import java.io.*;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.*;

import static com.group1.wip.weather.MainActivity.geocoder;

public class Weather extends AsyncTask<String, Void, HashMap<String, String>> {
  public HashMap<String, String> doInBackground(String ... a) {
    String loc = getLocation();
    try {
      Document doc = ProcessXML.gatherXML(loc);
      System.out.println("[!!] Document Acquired!!");
      int day = Integer.parseInt(a[0]);
      HashMap<String, String> ret = ProcessXML.getData(doc, day);
      ret.put("location", loc);
        return ret;
    } catch (Exception e) {
      System.out.println("[!!] Failed to get Document!!");
      System.err.println(e);
    }
    return null;
  }
    public String getLocation() {
        List<Address> addresses;
        String loc = "fairfax,VA";
        // getting GPS status
        try {
            Location location = MainActivity.locMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location == null) {
                throw new IOException("Location returned null!!!");
            }
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            //Taken from - www.developer.android.com/training/location/display-address.html#fetch-address
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            loc = String.format("%s,%s", city, state);
        }   catch (SecurityException e) {
            //Do something if permissions not set
            System.out.println("[!!] Permissions not set!!");
            System.err.println(e);
        } catch (IOException e) {
            //Do something if XML fails
            System.out.println("[!!] Failed to read Data!!");
            System.err.println(e);
            System.out.println(loc);
        } catch (Exception e) {
            System.err.println(e);
        }
        return loc;
    }
}

