package com.juangm.retrofit_weather_demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juangm.retrofit_weather_demo.models.WeatherAPIResult;
import com.juangm.retrofit_weather_demo.rest.RestClient;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();
        setUpFab();
    }

    private void setupView(WeatherAPIResult result) {
        final TextView textView = (TextView) findViewById(R.id.resulttext);

        //Just turn the result into a string and set the Textview
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        textView.setText(gson.toJson(result));
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpFab() {
        //FAB onClick
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending request...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                //Send get weather request
                RestClient.WeatherApiInterface service = RestClient.getClient();
                //We need to pass our city ID and our openweathermap APPID
                Call<WeatherAPIResult> call = service.getWeather(2519240, "476f28ed531b9477e89ddb6ab463dbd5");
                call.enqueue(new Callback<WeatherAPIResult>() {
                    @Override
                    public void onResponse(Response<WeatherAPIResult> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            //Handle the received weather data here
                            WeatherAPIResult result = response.body();
                            setupView(result);
                        } else {
                            Log.e("MainActivity", "Response received but request not successful. Response: " + response.raw());

                            Toast.makeText(MainActivity.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("MainActivity", "Request error!");
                    }
                });
            }
        });
    }
}
