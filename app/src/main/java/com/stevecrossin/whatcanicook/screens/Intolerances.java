package com.stevecrossin.whatcanicook.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.stevecrossin.whatcanicook.R;

/**
 * This class contains all possible intolerances.
 */
public class Intolerances extends AppCompatActivity {
    Switch switchNuts, switchGluten, switchSoy, switchPork, switchLactoVeg, switchAlcohol, switchVegan, switchRedMeat, switchSeafood, switchLactoOvo, switchLactose, switchPescaterian, switchEgg;
    private static final String TAG = "Intolerances";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietaryneeds);

        switchNuts = findViewById(R.id.switchNuts);
        switchGluten = findViewById(R.id.switchGluten);
        switchSoy = findViewById(R.id.switchSoy);
        switchPork = findViewById(R.id.switchPork);
        switchLactoVeg = findViewById(R.id.switchLactoVeg);
        switchAlcohol = findViewById(R.id.switchAlcohol);
        switchVegan = findViewById(R.id.switchVegan);
        switchRedMeat = findViewById(R.id.switchRedMeat);
        switchSeafood = findViewById(R.id.switchSeafood);
        switchLactoOvo = findViewById(R.id.switchLactoOvo);
        switchLactose = findViewById(R.id.switchLactose);
        switchPescaterian = findViewById(R.id.switchPescaterian);
        switchEgg = findViewById(R.id.switchEgg);
        switchNuts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchSoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchPork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchLactoVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchAlcohol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchRedMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchSeafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchLactoOvo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchLactose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchPescaterian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
        switchEgg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Log.d(TAG, "onCheckedChanged: " + isChecked);

            }
        });
    }

    String intolerancename;
    String intoleranceexcludes;
    Boolean intolerancesactive = false; //By default, no intolerances   are active

    //Handles loading of ingredient intolerances list
    public void loadIntolerances() {
    /*
    This method will handle the loading of the intolerances list. It will perform the following steps.
    1. Load all possible intolerances from intolerances.csv file, and use that data to update the Intolerance room database with any new entries
    2. Query the savedintolerances column in the Users database for the current user. It will parse out multiple intolerances that are inside quotes and brackets, with the comma between
    each intolerance separating them.
    3. It will then mark the relevant intolerance as active in the intolerance database, ands also update the UI of the activity to mark the selected intolerances as active.
    */
    }

    public void intoleranceSelected() {
        /*
        This method is responsible for taking action when an intolerance is updated in the Intolerance activity
        When a user clicks on the on/off slider to mark it as active/inactive, in a background task, this information will be passed to the Users.updateUser method
        which will take appropriate action to update the user database.
        The intolerances database will also be updated to mark the relevant intolerance as active/inactive.
        The Intolerance activity UI will also be refreshed so that the intolerance clicked is made active/inactive.
        */

    }
}








