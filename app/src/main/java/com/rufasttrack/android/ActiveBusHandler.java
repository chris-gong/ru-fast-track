package com.rufasttrack.android;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 1/3/2017.
 */

public class ActiveBusHandler extends DefaultHandler {
    private HashMap<String, String> activeBuses; //key is route tag and value is route title
    private BusRouteHandler busRouteHandler; //handler containing information about all the bus routes
    HashMap<String, String> busNames; //key is route tag and value is route title

    public HashMap<String, String> getActiveBuses(){
        return activeBuses;
    }
    @Override
    public void startDocument() throws SAXException {
        activeBuses = new HashMap<String, String>();
        busRouteHandler = NextBusAPI.getBusRouteHandler();
        busNames = busRouteHandler.getBusNames();

    }
    @Override
    public void endDocument() throws SAXException {
        NextBusAPI.makeBusTimeHandler();
    }
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        if(qName.equals("vehicle")) {
            String routeTag = atts.getValue("routeTag");
            if(!activeBuses.containsKey(routeTag)){
                activeBuses.put(routeTag, busNames.get(routeTag));
            }
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }
    public void printHandler(){
        for(Map.Entry<String, String> entry : activeBuses.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("Active Bus: ", "Bus tag: " + key + " and Bus name: " + value);
        }
    }
}
