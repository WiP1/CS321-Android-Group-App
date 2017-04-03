package com.nolanmeeks.iris_morningassistant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import java.util.HashMap;
import java.util.Locale;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener{
    //CONSTANTS, will be set from settings file
    boolean CITY, WEATHER, CALENDAR, ALARM, V2T;
    int GPS_PERMISSIONS;
    public static LocationManager locMan;
    public static Geocoder geocoder;
    public static LocationListener locListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_home_screen);

        // Locate the button in activity_main.xml
        Button weatherButton = (Button) findViewById(R.id.WeatherButton);
        weatherButton.setOnClickListener(this);
        locationSetup();
        displayWeather();

        Button calendarButton = (Button) findViewById(R.id.CalendarButton);
        calendarButton.setOnClickListener(this);
        Button alarmButton = (Button) findViewById(R.id.AlarmButton);
        alarmButton.setOnClickListener(this);

        FloatingActionButton theFab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        theFab.setOnClickListener(this);
    }
    // Capture button clicks
    public void onClick(View clicked) {
        switch(clicked.getId()){

            // Start Weather Activity
            case R.id.WeatherButton:
                Intent weatherIntent = new Intent(HomeScreen.this, WeatherActivity.class);
                startActivity(weatherIntent);
                break;

            // Start Calendar Activity
            case R.id.CalendarButton:
                Intent calendarIntent = new Intent(HomeScreen.this, CalendarActivity.class);
                startActivity(calendarIntent);
                break;

            // Start Alarm Activity
            case R.id.AlarmButton:
                // Start Alarm
                Intent alarmIntent = new Intent(HomeScreen.this, AlarmActivity.class);
                startActivity(alarmIntent);
                break;

            //Floating Action Button Click
            case R.id.floatingActionButton:
                //Expand FAB
                //Intent fabIntent = new Intent()
            default:
                break;
        }
    }

    public void displayWeather() {
        AsyncTask a = new Weather().execute("current");
        try {
            HashMap<String, String> data = (HashMap<String, String>) a.get();
            String display = "Unable to fetch Weather data";
            Button weather = (Button) findViewById(R.id.WeatherButton);
            if (data != null) display = String.format("%s: \nCurrent Temperature: %s\n %s\n",
                    ProcessJSON.location,data.get("temp"),data.get("condition"));
            weather.setText(display);
            String condition = data.get("condition").toLowerCase().replaceAll(" ","");
            int res = WeatherActivity.getIcon(condition, data.get("day?").equals("true"));
            weather.setCompoundDrawablesWithIntrinsicBounds( 0,
                    0, res, 0 );
        } catch (Exception e) {

        }
    }

    public void locationSetup() {
        locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        locListener = new MyLocationListener();

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GPS_PERMISSIONS);
        }
        else {
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locMan.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, HomeScreen.locListener);

        }
    }
}