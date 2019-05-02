package com.stevecrossin.whatcanicook.screens;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.PantryAutocompleteAdapter;
import com.stevecrossin.whatcanicook.adapter.PantryRecycleViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;
import java.util.List;

/*
This class covers the Pantry screen and database. CategoryPicker added here will be stored in a Room Database
called Pantry, with ingredients selected by the user displayed
 */
public class Pantry extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private AdView mAdView;

    /*Set view as pantry activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        initView();
        getPantryIngredient();
        loadPantry();
    }

    private void initView() {
        autoCompleteTextView = findViewById(R.id.pantryIngredientsSearchBox);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.setText(null);
                Ingredient ingredient = (Ingredient) parent.getItemAtPosition(position);
                ((PantryRecycleViewAdapter) recyclerView.getAdapter()).updateCategories(ingredient);
                addPantryIngredientToDB(ingredient);
            }
        });

        recyclerView = findViewById(R.id.pantryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PantryRecycleViewAdapter(new ArrayList<Ingredient>()));

        Button clearAll = findViewById(R.id.pantryClearIngredients);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        AppDataRepo repo = new AppDataRepo(Pantry.this);
                        repo.clearUserIngredientsPantry();
                        repo.deleteIngredientsPantry();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        recyclerView.setAdapter(new PantryRecycleViewAdapter(new ArrayList<Ingredient>()));
                    }
                }.execute();
            }
        });
    }

    public void getPantryIngredient() {
        final AppDataRepo repository = new AppDataRepo(this);

        new AsyncTask<Void, Void, List<Ingredient>>() {
            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                List<Ingredient> categories = repository.getAllTolerantIngredients();
                categories.addAll(repository.getAllCategories());
                return categories;
            }

            @Override
            protected void onPostExecute(List<Ingredient> categories) {
                super.onPostExecute(categories);
                PantryAutocompleteAdapter adapter = new PantryAutocompleteAdapter(Pantry.this, categories);
                autoCompleteTextView.setAdapter(adapter);
            }
        }.execute();
    }

    public void addPantryIngredientToDB(final Ingredient ingredient) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(Pantry.this);
                repo.addIngredientToPantry(ingredient);
                return null;
            }
        }.execute();
    }

    /**
     * This will load the contents of the pantry database
     */

    public void loadPantry() {
        /*
        This method will be called when a user clicks the "My Pantry" button, or is otherwise navigated back to the pantry scene through the app
        1. Perform a query of the pantry database, and get all records in the database
        2. Pass that data to the pantry scene, and update the recyclerview with the items in the database
        */

        new AsyncTask<Void, Void, List<Ingredient>>() {

            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(Pantry.this);
                return repo.getAllPantryIngredients();
            }

            @Override
            protected void onPostExecute(List<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                ((PantryRecycleViewAdapter) recyclerView.getAdapter()).updateCategories(ingredients);
            }
        }.execute();


    }

    /**
     * This is the method to add ingredients to the pantry. Steps to code it are below.
     */

    private void addPantryItem() {
        /*
        This method will be called when a user clicks the "Add CategoryPicker" button from the Pantry activity. The following steps need to occur
        1. The UI will navigate from the "Pantry" activity to a modified view of the Add CategoryPicker activity normally used for recipe searches.
        The label at the top needs to be updated to "Add CategoryPicker"
        The ingredientselected method as noted in CategoryPicker.java will be duplicated partially here, when a user selects an ingredient, it will be added to a list
        in the background.
        Once the user has selected all their ingredients and clicks "Check My CategoryPicker", the user will be taken to the "My CategoryPicker".
        If any ingredients selected are already in the pantry, an alert or some notification needs to be presented notifying the user of this.
        activity and be presented with their ingredients. The button presented will say "save ingredients to pantry" instead of "Find Recipes".
        2. The app will then take the strings for each individual ingredient, and add them to the pantry database, one per line, in the background.
        3. An exception handler needs to be coded to account for the possiblity the user will not add any ingredients.
        4. The application will then navigate back to the "My Pantry" activity, which will then refresh with the loadPantry method.
        */


    }

    /**
     * This method will will delete an ingredient from the users pantry list
     */

    public void deletePantryItem() {
        /*
        This method will be called when a user selects an ingredient in the pantry activity and clicks the delete cross.
        When this done, it will need to perform a few actions
        1. Take the name of the ingredient marked for deletion
        2. Find that ingredient in the Pantry database, and delete its entry
        3. Remove the ingredient from the list in the pantry activity
        */
    }
}