package com.nolanmeeks.iris_morningassistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        displayWeather();
        displayHourlyWeather();
        displayDailyWeather();


    }

    public void displayWeather() {
        AsyncTask a = new Weather().execute("current");
        try {
            HashMap<String, String> data = (HashMap<String, String>) a.get();

                TextView location = (TextView)findViewById(R.id.location);
                TextView temp = (TextView)findViewById(R.id.cond);

                location.setText(ProcessJSON.location);
                temp.setText(data.get("condition"));
        }catch (Exception e) {
            System.err.println("[!!] Failed to set Document!!");
            System.err.println(e);
        }
    }

    public void displayHourlyWeather() {
        AsyncTask a = new Weather().execute("hourly");
        try {
            HashMap<Integer, HashMap<String, String>> data =
                    (HashMap<Integer, HashMap<String, String>>) a.get();
            for (Integer hour : data.keySet()) {
                HashMap<String, String> hourData = data.get(hour);
                Button weather = (Button)getView(hour);
                String display = null;
                if (hourData != null)
                    display = String.format("@%s \n%s F\nChance of Rain - %s%%",
                            hourData.get("time"), hourData.get("temp"), hourData.get("rain"));
                weather.setText(display);
                String condition = hourData.get("condition").toLowerCase().replaceAll(" ", "");
                int res = getIcon(condition, hourData.get("day?").equals("true"));
                weather.setCompoundDrawablesWithIntrinsicBounds(0,
                        0, 0, res);
            }
        }catch (Exception e) {
            System.err.println("[!!] Failed to get Document!!");
            System.err.println(e);
        }
    }

    public void displayDailyWeather() {
        AsyncTask a = new Weather().execute("");
        try {
            HashMap<Integer, HashMap<String, String>> data =
                    (HashMap<Integer, HashMap<String, String>>) a.get();
            for (Integer day : data.keySet()) {
                HashMap<String, String> dayData = data.get(day);
                Button weather = (Button)getView(day+10);
                String display = null;
                if (dayData != null)
                    display = String.format("%s \n%s\nHigh: %s\t\tLow: %s",
                            dayData.get("date"), dayData.get("condition"), dayData.get("high"), dayData.get("low"));
                weather.setText(display);
                String condition = dayData.get("condition").toLowerCase().replaceAll(" ", "");
                int res = getIcon(condition, true);
                weather.setCompoundDrawablesWithIntrinsicBounds(0,
                        0, res, 0);
            }
        }catch (Exception e) {
            System.err.println("[!!] Failed to get Document!!");
            System.err.println(e);
        }
    }

    public View getView(int i) {
        switch (i) {
            case 0:
                return findViewById(R.id.h1);
            case 1:
                return findViewById(R.id.h2);
            case 2:
                return findViewById(R.id.h3);
            case 3:
                return findViewById(R.id.h4);
            case 4:
                return findViewById(R.id.h5);
            case 5:
                return findViewById(R.id.h6);
            case 6:
                return findViewById(R.id.h7);
            case 10:
                return findViewById(R.id.d1);
            case 11:
                return findViewById(R.id.d2);
            case 12:
                return findViewById(R.id.d3);
            case 13:
                return findViewById(R.id.d4);
            case 14:
                return findViewById(R.id.d5);
        }
        return null;
    }

    public static int getIcon(String condition, boolean isDay) {
        int res;
        if (condition.contains("partlysunny") && isDay) res = R.drawable.partlysunny;
        else if (condition.contains("storms")) res = R.drawable.storms;
        else if (condition.contains("rain")&& isDay) res = R.drawable.rain02;
        else if (condition.contains("scatteredshowers")) res = R.drawable.flurries;
        else if (condition.contains("showers")) res = R.drawable.rain01;
        else if (condition.contains("partlycloudy") && isDay) res = R.drawable.partlycloudy;
        else if  (isDay && (condition.contains("sunny") || condition.contains("clear")))
            res = R.drawable.clear;
        else if (condition.contains("windy")) res = R.drawable.windy;
        else if (isDay && condition.contains("mostlycloudy")) res = R.drawable.scatteredclouds;
        else if (isDay && condition.contains("cloud")) res = R.drawable.cloudy;
        else if (condition.contains("snow")) res = R.drawable.snow;
        else if (isDay && condition.contains("sunny")) res = R.drawable.mostlysunny;
        else if (condition.contains("fog")) res = R.drawable.fog;
        else if (condition.contains("clear") && !isDay) res = R.drawable.clearnight;
        else if (condition.contains("cloudy") && !isDay) res = R.drawable.cloudynight;
        else if (condition.contains("partlycloudy") && !isDay) res = R.drawable.partlycloundynight;
        else if (condition.contains("rain") && !isDay) res = R.drawable.rainnight;
        else if (condition.contains("snow") && !isDay) res = R.drawable.snownight;
        else res = R.drawable.unknown;
        return res;
    }
}
