package com.example.daniel.rufasttrack;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class RouteActivity extends AppCompatActivity {

    private static boolean finishedParsing = false;
    private HashMap<String, BusTimeHandler> busTimeHandlers;
    ScrollView scrollView;
    LinearLayout innerRelativeLayout;
    Handler h;

    public static void setFinishedState(boolean finished){
        finishedParsing = finished;
    }
    private void setDisplay(){ //add paramter to createHandlers method and call this method in endDocument in BusTimeHandler
        for(Map.Entry<String, BusTimeHandler> entry : busTimeHandlers.entrySet()){
            String route= entry.getKey(); //route title because we assumed one-to-one
            BusTimeHandler handler = entry.getValue();
            TextView routeDisplay = new TextView(this);
            routeDisplay.setText(route);
            routeDisplay.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
            innerRelativeLayout.addView(routeDisplay);
            for(Map.Entry<String, PriorityQueue<BusTimeHandler.Prediction>> e : handler.getPredictedTimes().entrySet()){
                String stop = e.getKey();
                PriorityQueue<BusTimeHandler.Prediction> minHeap = e.getValue();
                Comparator<BusTimeHandler.Prediction> comparator = handler.new PredictionComparator();
                PriorityQueue<BusTimeHandler.Prediction> temp = new PriorityQueue<BusTimeHandler.Prediction>(10, comparator);
                int size = minHeap.size();
                for(int i = 0; i < size; i++){
                    BusTimeHandler.Prediction p = minHeap.remove();
                    TextView stopDisplay = new TextView(this);
                    stopDisplay.setText(p.stopTitle + " in " + p.minutes + " minutes");
                    stopDisplay.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT)); //doesn't work when width is wrap_content
                    innerRelativeLayout.addView(stopDisplay);
                    temp.add(p);
                }
                for(int i = 0; i < size; i++){
                    minHeap.add(temp.remove());
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        innerRelativeLayout = (LinearLayout) findViewById(R.id.innerLayout);
        NextBusAPI.createHandlers();
        //runnable thread executes every second
        h = new Handler();
        final Runnable r = new Runnable() {
            public void run(){
                h.postDelayed(this, 1000);
                if(finishedParsing){
                    Log.d("Message: ", "Data Finished Parsing");
                    busTimeHandlers  = NextBusAPI.getBusTimeHandlers(); //createHandlers method is not asynchronous
                    //print statement below checks if hashmap is initialized with handlers
                    /*for(Map.Entry<String, BusTimeHandler> entry : busTimeHandlers.entrySet()){
                        BusTimeHandler handler = entry.getValue();
                        handler.printHandler();
                    }*/
                    setDisplay();
                    finishedParsing = false;
                }
            }
        };
        h.postDelayed(r, 1000);


    }
}
