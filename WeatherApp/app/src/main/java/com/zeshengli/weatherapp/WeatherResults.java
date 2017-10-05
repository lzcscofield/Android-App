package com.zeshengli.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherResults extends AppCompatActivity {

    MainActivity mainActivity = new MainActivity();

    SharedPreferences sharedPreferences;

    public void returnToMain(View view){

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        startActivity(intent);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_weather_results);



        TextView LocationTextView = (TextView) findViewById(R.id.Location);
        TextView weatherTextView = (TextView) findViewById(R.id.Weather);
        TextView highTempTextView = (TextView) findViewById(R.id.highTemp);
        TextView lowTempTextView = (TextView) findViewById(R.id.lowTemp);


        sharedPreferences = this.getSharedPreferences("com.zeshengli.weatherapp",Context.MODE_PRIVATE);

        String outWeather =sharedPreferences.getString("weather","null");
        String outHighTemp =sharedPreferences.getString("high","null");
        String outLowTemp =sharedPreferences.getString("low","null");

        String outCity =sharedPreferences.getString("city","null");
        String outCountry = sharedPreferences.getString("country","null");
        String outState =sharedPreferences.getString("state","null");


        //Vermillion, SD, United States
        String outputLocation = outCity+", "+outState+", "+outCountry;


        LocationTextView.setText(outputLocation);
        weatherTextView.setText(outWeather);
        highTempTextView.setText("Hight: "+outHighTemp);
        lowTempTextView.setText("Low: " +outLowTemp);

    }



}
