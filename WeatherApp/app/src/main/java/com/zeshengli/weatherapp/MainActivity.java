package com.zeshengli.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    JSONArray jsonArray;
    JSONObject jsonObject;
    String temp = " ";

    EditText zipcodeInput;

    JsonDownload jsonDownload;
    SharedPreferences mSharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

         zipcodeInput = (EditText) findViewById(R.id.InputZipCode);
         mSharedPreferences = this.getSharedPreferences("com.zeshengli.weatherapp",Context.MODE_PRIVATE);




        //Log.i("1",jsonDownload.result);
    }

    public class JsonDownload extends AsyncTask<String, Void, String>{

        String result ="";
        URL url;
        HttpURLConnection urlConnection = null;

        @Override
        public String doInBackground(String... urls) {

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader =  new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Could not fin weather",Toast.LENGTH_LONG);

            }


            return null;
        }


        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                jsonObject = new JSONObject(s);
                jsonArray = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast");
                JSONObject Jlocation = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location");


                JSONObject weatherInfo = jsonArray.getJSONObject(0);
                String Weather = weatherInfo.getString("text");
                String HighTemp = weatherInfo.getString("high");
                String LowTemp = weatherInfo.getString("low");

                String city = Jlocation.getString("city");
                String country = Jlocation.getString("country");
                String state = Jlocation.getString("region");

                Log.i("city",city);
                Log.i("country",country);
                Log.i("state",state);




                mSharedPreferences.edit().putString("weather",Weather).apply();
                mSharedPreferences.edit().putString("high",HighTemp).apply();
                mSharedPreferences.edit().putString("low",LowTemp).apply();

                mSharedPreferences.edit().putString("city",city).apply();
                mSharedPreferences.edit().putString("country",country).apply();
                mSharedPreferences.edit().putString("state",state).apply();

                Intent intent = new Intent(getApplicationContext(),WeatherResults.class);

                startActivity(intent);

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Could not fin weather",Toast.LENGTH_LONG);

            }
        }
    }


    public void checkWeatherButtom(View view){

        if (zipcodeInput.getText().toString().equals("") ){

            Toast.makeText(getApplicationContext(),"Please enter a zip code",Toast.LENGTH_LONG);

        }else{

            //temp = jsonDownload.result;
            Log.i("1", temp);

            //Log.i("CITY ZIP", String.valueOf(zipcodeInput.getText()));
            temp = mSharedPreferences.getString("temp", "null");

            //Log.i("check",temp);

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(zipcodeInput.getWindowToken(), 0);

            try {
                String encodedZip = URLEncoder.encode(zipcodeInput.getText().toString(), "UTF-8");
                jsonDownload = new JsonDownload();
                jsonDownload.execute(
                        "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from" +
                                "%20weather.forecast%20where%20woeid%20in%20" +
                                "(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
                                + encodedZip + "%22)&format=json");

            } catch (UnsupportedEncodingException e) {

                Toast.makeText(getApplicationContext(), "Could not fin weather", Toast.LENGTH_LONG);

            }
        }





    }

}
