package com.rufasttrack.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationOneActivity extends AppCompatActivity {

    Button next;
    String campus;
    AutoCompleteTextView location;
    ArrayAdapter<String> locationAdp;
    String[] buschLocations;
    String[] collegeAveLocations;
    String[] cookDougLocations;
    String[] liviLocations;
    String[] downTownNBLocations;
    String[] chosenLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_one);

        buschLocations=new String[]{"Visitor Center","Stadium","Werblin Back","Hill Center","Allison Road Classrooms",
        "Library of Science & Medicine","Davidson Hall","Busch Campus Center","Buell Apartments","Werblin Rec Center"};

        collegeAveLocations=new String[]{"Rutgers Student Center","Scott Hall","Zimmerli Art Museum"
        ,"Student Activities Center"};

        cookDougLocations=new String[]{"Public Safety","College Hall","Gibbons","Katzenbach","Henderson",
        "Biel Road","Food Science","Lipman Hall","Red Oak Lane","Cabaret Theater"};

        liviLocations=new String[]{"Livingston Plaza","Livingston Student Center","Quads","Health Center"};

        downTownNBLocations=new String[]{"Train Station","Paterson ST","Rock Hall","Public Safety"};

        location =(AutoCompleteTextView) findViewById(R.id.location);


        location.setThreshold(0);

        location.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                location.showDropDown();
                return false;
            }
        });
        campus = getIntent().getStringExtra("campus");
        switch(campus){
            case "Busch":
                locationAdp = new ArrayAdapter<String>(this, R.layout.item,R.id.item, buschLocations);
                chosenLocations = buschLocations;
                break;
            case "College Ave":
                locationAdp = new ArrayAdapter<String>(this, R.layout.item,R.id.item, collegeAveLocations);
                chosenLocations = collegeAveLocations;
                break;
            case "Cook/Douglas":
                locationAdp = new ArrayAdapter<String>(this, R.layout.item,R.id.item, cookDougLocations);
                chosenLocations = cookDougLocations;
                break;
            case "Livingston":
                locationAdp = new ArrayAdapter<String>(this, R.layout.item,R.id.item, liviLocations);
                chosenLocations = liviLocations;
                break;
            case "Downtown New Brunswick":
                locationAdp = new ArrayAdapter<String>(this, R.layout.item,R.id.item, downTownNBLocations);
                chosenLocations = downTownNBLocations;
                break;
            default:
                //dont do anything
        }
        location.setAdapter(locationAdp);

        location.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                location.showDropDown();
                return false;
            }
        });

        //Makes keyboard go down on click
        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if user selects a list item, keyboard goes down
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        //Touch background, hides keyboard
        location.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        });

        //if user presses done button, hides keyboard
        location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        next=(Button) findViewById(R.id.next_page);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLocation = false;
                String input = String.valueOf(location.getText());
                for(int i = 0; i < chosenLocations.length; i++){
                    if(input.equals(chosenLocations[i])){
                        isLocation = true;
                        break;
                    }
                }
                if(isLocation) {
                    Intent intent=new Intent(LocationOneActivity.this,CampusTwoActivity.class);
                    intent.putExtra("campusOne", campus);
                    intent.putExtra("locationOne", input);
                    startActivity(intent);
                }
                else{
                    //clear the textview and tell user to enter a valid campus
                    location.setText("");
                    Toast.makeText(getApplicationContext(),"Enter a valid bus stop", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
