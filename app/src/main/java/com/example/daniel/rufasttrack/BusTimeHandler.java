package com.example.daniel.rufasttrack;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Daniel on 1/3/2017.
 */

public class BusTimeHandler extends DefaultHandler {

    public class Prediction{
        String stopTag;
        String stopTitle;
        int vehicle;
        int minutes;
        boolean isDeparting;
        Prediction(String stopTag, String stopTitle, int vehicle, int minutes, boolean isDeparting){
            this.stopTag = stopTag;
            this.stopTitle = stopTitle;
            this.vehicle = vehicle;
            this.minutes = minutes;
            this.isDeparting = isDeparting;
        }
    }
    public class PredictionComparator implements Comparator<Prediction>
    {
        @Override
        public int compare(Prediction p1, Prediction p2)
        {

            if (p1.minutes < p2.minutes)
            {
                return -1;
            }
            if (p1.minutes > p2.minutes)
            {
                return 1;
            }
            return 0;
        }
    }
    //key is stop tag, value is a minheap of prediction objects for each stop
    private HashMap<String, PriorityQueue<Prediction>> predictedTimes;
    String currentStopTag;
    String currentStopTitle;
    String currentRouteTitle;

    public HashMap<String, PriorityQueue<Prediction>> getPredictedTimes(){
        return predictedTimes;
    }
    @Override
    public void startDocument() throws SAXException {
        predictedTimes = new HashMap<String, PriorityQueue<Prediction>>();
        currentStopTitle = "";
        currentStopTag = "";
    }
    @Override
    public void endDocument() throws SAXException {
        //Log.d("Route ", currentRouteTitle);
        NextBusAPI.getBusTimeHandlers().put(currentRouteTitle, this); //add newly made handler to hashmap of handlers (key is route title, value is handler)
    }
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        if(qName.equals("prediction")) {
            int vehicle = Integer.parseInt(atts.getValue("vehicle"));
            int minutes = Integer.parseInt(atts.getValue("minutes"));
            boolean isDeparting = Boolean.parseBoolean(atts.getValue("isDeparture"));
            predictedTimes.get(currentStopTag).add(new Prediction(currentStopTag, currentStopTitle,
                    vehicle, minutes, isDeparting));
        }
        else if(qName.equals("predictions")){
            currentRouteTitle = atts.getValue("routeTitle");
            currentStopTag = atts.getValue("stopTag");
            currentStopTitle = atts.getValue("stopTitle");
            Comparator<Prediction> comparator = new PredictionComparator();

            predictedTimes.put(currentStopTag, new PriorityQueue<Prediction>(10, comparator));

        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }
    public void printHandler(){

        Log.d("Route ", currentRouteTitle);
        for(Map.Entry<String, PriorityQueue<Prediction>> e : predictedTimes.entrySet()){
            String k = e.getKey();
            PriorityQueue<Prediction> minHeap = e.getValue();
            Comparator<Prediction> comparator = new PredictionComparator();
            PriorityQueue<Prediction> temp = new PriorityQueue<Prediction>(10, comparator);
            int size = minHeap.size();
            for(int i = 0; i < size; i++){
                Prediction p = minHeap.remove();
                Log.d(p.stopTag, String.valueOf(p.minutes));
                temp.add(p);
            }
            for(int i = 0; i < size; i++){
                minHeap.add(temp.remove());
            }
        }

    }
}
