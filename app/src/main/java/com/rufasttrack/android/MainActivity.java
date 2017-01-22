package com.rufasttrack.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.rufasttrack.android.R.id.route;

public class MainActivity extends AppCompatActivity {

    Button next;
    Button routeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        next=(Button) findViewById(R.id.next_page);
        routeButton=(Button) findViewById(route);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CampusOneActivity.class);
                startActivity(intent);
            }
        });
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RouteActivity.class);
                startActivity(intent);
            }
        });
    }


}
