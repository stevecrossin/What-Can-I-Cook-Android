package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * This class contains all possible intolerances.
 */
public class Intolerances extends AppCompatActivity {
    Switch switchNuts, switchGluten, switchSoy, switchPork, switchLactoVeg, switchAlcohol, switchVegan, switchRedMeat, switchSeafood, switchLactoOvo, switchLactose, switchPescaterian, switchEgg;
    private AppDataRepo repository;
    private static final String TAG = "Intolerances";
    private ArrayList<String> intoleranceList = new ArrayList<>();
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

        loadIntolerancesToDb();

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

        repository = new AppDataRepo(this);
    }

    String intolerancename;
    String intoleranceexcludes;
    Boolean intolerancesactive = false; //By default, no intolerances   are active

    //Handles loading of ingredient intolerances list
    public ArrayList<Intolerance> loadIntolerancesFromCsv() {
    /*
    This method will handle the loading of the intolerances list. It will perform the following steps.
    1. Load all possible intolerances from intolerances.csv file, and use that data to update the Intolerance room database with any new entries
    2. Query the savedintolerances column in the Users database for the current user. It will parse out multiple intolerances that are inside quotes and brackets, with the comma between
    each intolerance separating them.
    3. It will then mark the relevant intolerance as active in the intolerance database, ands also update the UI of the activity to mark the selected intolerances as active.
    */
        try {
            ArrayList<Intolerance> intolerances = new ArrayList<>();
            Reader in = new InputStreamReader(getResources().openRawResource(R.raw.intolerances));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String intoleranceID =  record.get(0);
                String intoleranceName = record.get(1);
                String intoleranceIngredients = record.get(2);
                intoleranceIngredients = intoleranceIngredients.replace("''", "");
                String[] ingredients = intoleranceIngredients.split(":");

                for (String ingredient : ingredients){
                    Intolerance intolerance = new Intolerance(Integer.parseInt(intoleranceID), intoleranceName, ingredient);
                    intolerances.add(intolerance);
                }

            }
            return intolerances;
        } catch (FileNotFoundException ex){
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex){
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex){
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
        return  null;
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

    @SuppressLint("StaticFieldLeak")
    public void loadIntolerancesToDb() {

        final ArrayList<Intolerance> intolerances = loadIntolerancesFromCsv();

        new AsyncTask<Void, Void, ArrayList<Intolerance>>() {
            @Override
            protected ArrayList<Intolerance> doInBackground(Void... voids) {
                if (!repository.haveIntolerance()) {
                    repository.insertIntolerances(intolerances);
                }
                ArrayList<Intolerance> intoleranceArrayList = new ArrayList<>();
                intoleranceArrayList.addAll(repository.getAllIntolerances());
                return intoleranceArrayList;
            }
        }.execute();

    }
}








