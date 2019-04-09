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
import java.util.List;

/**
 * This class contains all possible intolerances.
 */
public class Intolerances extends AppCompatActivity {
    Switch switchNuts, switchGluten, switchSoy, switchPork, switchLactoVeg, switchAlcohol, switchVegan, switchRedMeat, switchSeafood, switchLactoOvo, switchLactose, switchPescaterian, switchEgg;
    private AppDataRepo repository;
    private static final String TAG = "Intolerances";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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

        repository = new AppDataRepo(this);
        loadIntolerancesToDb();
        updateUserTolerancePreference();

        switchNuts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "No nuts");
                Log.d(TAG, "Nut checked: " + isChecked);
            }
        });
        switchGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Gluten");

            }
        });
        switchSoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Soy");

            }
        });
        switchPork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Pork-free");

            }
        });
        switchLactoVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Lacto-veg");

            }
        });
        switchAlcohol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Alcohol");

            }
        });
        switchVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Vegan");

            }
        });
        switchRedMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Red meat-free");

            }
        });
        switchSeafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Seafood");

            }
        });
        switchLactoOvo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Lacto-ovo-veg");

            }
        });
        switchLactose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Dairy");

            }
        });
        switchPescaterian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Pescatarian");

            }
        });
        switchEgg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intoleranceSelected(isChecked, "Eggs");
            }
        });

    }

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
                String intoleranceName = record.get(1);
                String intoleranceIngredients = record.get(2);
                String[] ingredients = intoleranceIngredients.split(":");

                for (String ingredient : ingredients) {
                    Intolerance intolerance = new Intolerance(intoleranceName, ingredient);
                    intolerances.add(intolerance);
                }
            }
            return intolerances;
        } catch (FileNotFoundException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    public void intoleranceSelected(final boolean isSelected, final String intoleranceName) {
        /*
        This method is responsible for taking action when an intolerance is updated in the Intolerance activity
        When a user clicks on the on/off slider to mark it as active/inactive, in a background task, this information will be passed to the Users.updateUser method
        which will take appropriate action to update the user database.
        The intolerances database will also be updated to mark the relevant intolerance as active/inactive.
        The Intolerance activity UI will also be refreshed so that the intolerance clicked is made active/inactive.
        */

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Intolerance> list = new ArrayList<>();
                list.addAll(repository.getIntoleranceByName(intoleranceName));
                if (isSelected) {
                    repository.excludeIntolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.excludeIngredient(intolerance.getIngredientName());
                        Log.d(TAG, "Exclude ingredient: " + intolerance.getIngredientName());
                    }
                } else {
                    repository.includeIntolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.includeIngredient(intolerance.getIngredientName());
                        Log.d(TAG, "Include ingredient: " + intolerance.getIngredientName());
                    }
                }
                return null;
            }
        }.execute();

    }


    @SuppressLint("StaticFieldLeak")
    public void loadIntolerancesToDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Intolerance> intolerances = loadIntolerancesFromCsv();
                if (!repository.haveIntolerance()) {
                    for (Intolerance intolerance : intolerances)
                        repository.insertIntolerance(intolerance);
                }
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void updateUserTolerancePreference() {
        new AsyncTask<Void, List<Intolerance>, List<Intolerance>>() {
            @Override
            protected List<Intolerance> doInBackground(Void... voids) {
                return repository.getAllIntolerances();
            }

            @Override
            protected void onPostExecute(List<Intolerance> intolerances) {
                super.onPostExecute(intolerances);
                List<Intolerance> excludeCategory;
                excludeCategory = intolerances;
                if (!excludeCategory.isEmpty()) {
                    for (Intolerance categoryName : excludeCategory) {
                        if (categoryName.getIntoleranceName().equals("No nuts") && categoryName.isIntoleranceSelected()) {
                            switchNuts.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Gluten") && categoryName.isIntoleranceSelected()) {
                            switchGluten.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Soy") && categoryName.isIntoleranceSelected()) {
                            switchSoy.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Pork-free") && categoryName.isIntoleranceSelected()) {
                            switchPork.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Lacto-veg") && categoryName.isIntoleranceSelected()) {
                            switchLactoVeg.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Alcohol") && categoryName.isIntoleranceSelected()) {
                            switchAlcohol.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Vegan") && categoryName.isIntoleranceSelected()) {
                            switchVegan.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Red meat-free") && categoryName.isIntoleranceSelected()) {
                            switchRedMeat.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Seafood") && categoryName.isIntoleranceSelected()) {
                            switchSeafood.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Lacto-ovo-veg") && categoryName.isIntoleranceSelected()) {
                            switchLactoOvo.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Dairy") && categoryName.isIntoleranceSelected()) {
                            switchLactoOvo.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Pescatarian") && categoryName.isIntoleranceSelected()) {
                            switchPescaterian.setChecked(true);
                        } else if (categoryName.getIntoleranceName().equals("Eggs") && categoryName.isIntoleranceSelected()) {
                            switchEgg.setChecked(true);
                        }
                    }
                }
            }
        }.execute();
    }

}
