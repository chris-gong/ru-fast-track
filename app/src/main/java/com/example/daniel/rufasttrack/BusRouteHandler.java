package com.example.daniel.rufasttrack;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;

/**
 * Created by Daniel on 1/3/2017.
 */

public class BusRouteHandler extends DefaultHandler {
    private HashMap<String, ArrayList<String>> routes; //key is route title, value is array of stop tags in order for keys
    private HashMap<String, String> busNames; //key is route tag, value is route title
    private HashMap<String, String> stopNames; //key is stop tag, value is stop title
    private ActiveBusHandler activeBuses;
    String currentRoute;
    boolean onRoute;
    private static OkHttpClient okHttpClient;
    private static SAXParser saxParser;

    @Override
    public void startDocument() throws SAXException {
        routes = new HashMap<String, ArrayList<String>>();
        busNames = new HashMap<String, String>();
        onRoute = false;
        currentRoute = "";
        stopNames = new HashMap<String, String>();
    }
    @Override
    public void endDocument() throws SAXException {
        NextBusAPI.makeActiveBusHandler();
    }
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        if(onRoute && qName.equals("stop")){
            String stopTitle = atts.getValue("title");
            String stopTag = atts.getValue("tag");
            routes.get(currentRoute).add(stopTag);
            //there are multiple stopTags for each stopTitle
            if(!stopNames.containsKey(stopTag)){ //this is not done for routes because route tags and titles are one-to-one
                stopNames.put(stopTag, stopTitle);
            }
        }
        else if(qName.equals("route")) {
            String routeTitle = atts.getValue("title");
            String routeTag = atts.getValue("tag");
            //busTags and busTitle are one-to-one
            routes.put(routeTitle, new ArrayList<String>());
            busNames.put(routeTag, routeTitle);
            onRoute = true;
            currentRoute = routeTitle;
        }
        else{
            onRoute = false;
        }

    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    public void printHandler(){
        for (Map.Entry<String, ArrayList<String>> entry : routes.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            Log.d("Route", "Key: "+key);
            for(int i = 0; i < value.size(); i++){
                Log.d(key + " stop", "Stop " + (i+1) + ": " + value.get(i));
            }
        }
        for (Map.Entry<String, String> entry : busNames.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("Bus Name", "Key: "+key + " and " + value);

        }
    }
    public HashMap<String, ArrayList<String>> getRoutes(){
        return routes;
    }
    public HashMap<String, String> getBusNames(){
        return busNames;
    }
    public HashMap<String, String> getStopNames(){
        return stopNames;
    }

}
