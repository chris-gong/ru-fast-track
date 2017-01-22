package com.example.daniel.rufasttrack;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class NextBusAPI{

    private static BusRouteHandler busRouteHandler;
    private static ActiveBusHandler activeBusHandler;
    String XML;
    private static final String BASE_URL = "http://webservices.nextbus.com/service/publicXMLFeed?a=rutgers&command=";
    public static final String VEHICLE_LOCATIONS_URL = BASE_URL + "vehicleLocations";
    public static final String ALL_ROUTES_URL = BASE_URL + "routeConfig";
    public static final String PREDICTIONS_URL = BASE_URL + "predictionsForMultiStops";
    private static final String TAG = "NextBusAPI";
    private static OkHttpClient okHttpClient;
    private static SAXParser saxParser;
    private static ArrayList<String> predictionLinks;
    private static HashMap<String, BusTimeHandler> busTimeHandlers;

    public static BusRouteHandler getBusRouteHandler(){
        return busRouteHandler;
    }
    public static ActiveBusHandler getActiveRouteHandler(){
        return activeBusHandler;
    }
    // Downloads data from a url and returns it as an input stream
    private static InputStream downloadUrl(String url) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().byteStream();
        } catch (IOException e) {
            Log.e(TAG, e.toString(), e);
        }

        return null;
    }
    // Setups the SAX parser and parses the XML from the url
    private static void parseXML(String url, DefaultHandler handler) {
        try {
            if (saxParser == null) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                saxParser = factory.newSAXParser();
            }
            InputStream inputStream = downloadUrl(url);
            if (inputStream == null) {
                throw new IOException("Can't connect to the Internet");
            } else {
                saxParser.parse(inputStream, handler);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void makeActiveBusHandler(){
       new asyncTaskActiveBuses().execute();
    }

    public static void makeBusTimeHandler(){
        new asyncTaskBusTimes().execute();
    }
    private static class asyncTaskBusRoutes extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");

        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            //must do network tasks/web requests in this asynctask method
            busRouteHandler = new BusRouteHandler();
            //XML = convertStreamToString(downloadUrl(ALL_ROUTES_URL));
            parseXML(ALL_ROUTES_URL, busRouteHandler);

            //Log.d("Bus Route Handler", handler.toString());
            /*
            for(int i=0; i<10; i++){
                Integer in = new Integer(i);
                publishProgress(i);
            }*/
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a){
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG + " onPostExecute", "" + result);
            //display.setText(handler.toString());
            busRouteHandler.printHandler();
        }
    }
    private static class asyncTaskActiveBuses extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");

        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            //must do network tasks/web requests in this asynctask method
            activeBusHandler = new ActiveBusHandler();
            parseXML(VEHICLE_LOCATIONS_URL, activeBusHandler);

            //Log.d("Bus Route Handler", handler.toString());
            /*
            for(int i=0; i<10; i++){
                Integer in = new Integer(i);
                publishProgress(i);
            }*/
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a){
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG + " onPostExecute", "" + result);
            //display.setText(handler.toString());
            activeBusHandler.printHandler();
        }
    }
    private static class asyncTaskBusTimes extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");
            predictionLinks = new ArrayList<String>();
            busTimeHandlers = new HashMap<String, BusTimeHandler>();
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            //must do network tasks/web requests in this asynctask method
            //busTimeHandler = new BusTimeHandler();
            HashMap<String, String> stopNames = busRouteHandler.getStopNames();
            //gets the urls for the predictions
            //for each route tag-route title combo in activeBusHandler.getActiveBuses(), a link will be produced
            for(Map.Entry<String, String> entry : activeBusHandler.getActiveBuses().entrySet()){
                StringBuilder link = new StringBuilder(PREDICTIONS_URL);
                String key = entry.getKey(); //route tag
                String value = entry.getValue(); //route title
                //key for busRouteHander.getRoutes() is route title, value is arraylist of stop tags in order
                for(String stopTag : busRouteHandler.getRoutes().get(value)){

                    link.append("&stops=").append(key).append("%7Cnull%7C").append(stopTag);

                }

                predictionLinks.add(link.toString()); //arraylist of prediction links for each route
                Log.d("Link", link.toString());
                //link.append("&stops=").append(route.getTag()).append("%7Cnull%7C").append(stop.getTag());
            }
            for(String link : predictionLinks){
                parseXML(link, new BusTimeHandler());

            }
            //parseXML(NextBusAPI.VEHICLE_LOCATIONS_URL, activeBusHandler);

            //Log.d("Bus Route Handler", handler.toString());
            /*
            for(int i=0; i<10; i++){
                Integer in = new Integer(i);
                publishProgress(i);
            }*/
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a){
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG + " onPostExecute", "" + result);
            for(Map.Entry<String, BusTimeHandler> entry : busTimeHandlers.entrySet()){
                BusTimeHandler handler = entry.getValue();
                handler.printHandler();
            }
            RouteActivity.setFinishedState(true);
        }
    }
    public static void createHandlers(){
        new asyncTaskBusRoutes().execute();
    }
    public static HashMap<String, BusTimeHandler> getBusTimeHandlers(){
        return busTimeHandlers;
    }
}
