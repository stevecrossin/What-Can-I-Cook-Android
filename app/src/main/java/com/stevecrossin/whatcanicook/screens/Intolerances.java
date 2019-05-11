package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all possible intolerances.
 */
public class Intolerances extends AppCompatActivity {
    Switch switchNuts, switchGluten, switchSoy, switchPork, switchLactoVeg, switchAlcohol, switchVegan, switchRedMeat, switchSeafood, switchLactoOvo, switchLactose, switchPescaterian, switchEgg;
    private AppDataRepo repository;
    private static final String TAG = "Intolerances";
    //private ArrayList<String> intoleranceList = new ArrayList<>(); //unused, possible delete
    private AdView mAdView;

    /**
     * Scene initalization. This also performs the loading of intolerances into the database.
     * Get every switches by their id in the layout and set a listener on check/uncheck event (see intoleranceSelected())
     * @param savedInstanceState
     */

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

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
                    repository.addTolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.excludeIngredient(intolerance.getIngredientName());
                        repository.excludeRecipe(intolerance.getIngredientName());
                        Log.d(TAG, "Exclude ingredient: " + intolerance.getIngredientName());
                    }
                } else {
                    repository.includeIntolerance(intoleranceName);
                    repository.removeTolerance(intoleranceName);
                    for (Intolerance intolerance : list) {
                        repository.includeIngredient(intolerance.getIngredientName());
                        repository.includeRecipe(intolerance.getIngredientName());
                        Log.d(TAG, "Include ingredient: " + intolerance.getIngredientName());
                    }
                }
                return null;
            }
        }.execute();

    }

    public void updateUserTolerancePreference() {

        new AsyncTask<Void, List<Intolerance>, List<Intolerance>>() {
            @Override
            protected List<Intolerance> doInBackground(Void... voids) {
                return repository.getAllIntolerances();
            }

            @Override
            protected void onPostExecute(List<Intolerance> intolerances) {
                super.onPostExecute(intolerances);
                List<Intolerance> excludeCategory = intolerances;
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