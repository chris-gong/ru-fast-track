package com.example.daniel.rufasttrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;

public class DirectionsActivity extends AppCompatActivity {

    TextView infoDisplay;
    private static HashMap<String, BusTimeHandler> busTimeHandlers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        infoDisplay = (TextView) findViewById(R.id.info);
        NextBusAPI.createHandlers();
        busTimeHandlers  = NextBusAPI.getBusTimeHandlers();
    }
}
