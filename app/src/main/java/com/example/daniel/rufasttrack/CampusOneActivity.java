package com.example.daniel.rufasttrack;

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

public class CampusOneActivity extends AppCompatActivity {


    Button next;
    AutoCompleteTextView campus;
    String[] campuses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_one);

        campus=(AutoCompleteTextView) findViewById(R.id.campus);
        campuses= new String[]{"Busch", "College Ave", "Cook/Douglas", "Livingston", "Downtown New Brunswick"};
        ArrayAdapter<String> campusAdp= new ArrayAdapter<String>(this, R.layout.item,R.id.item, campuses);
        campus.setThreshold(0);
        campus.setAdapter(campusAdp);
        campus.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                campus.showDropDown();
                return false;
            }
        });
        //Makes keyboard go down on click
        campus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if user selects a list item, keyboard goes down
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        //Touch background, hides keyboard
        campus.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        });
        //if user presses done button, hides keyboard
        campus.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                //check if input is a valid campus
                boolean isCampus = false;
                String input = String.valueOf(campus.getText());
                for(int i = 0; i < campuses.length; i++){
                    if(input.equals(campuses[i])){
                        isCampus = true;
                        break;
                    }
                }
                if(isCampus) {
                    Intent intent = new Intent(CampusOneActivity.this, LocationOneActivity.class);
                    intent.putExtra("campus", input);
                    startActivity(intent);
                }
                else{
                    //clear the textview and tell user to enter a valid campus
                    campus.setText("");
                    Toast.makeText(getApplicationContext(),"Enter a valid campus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
