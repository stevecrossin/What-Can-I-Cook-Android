package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);mAdView.loadAd(adRequest);
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


    /**
     * This adds the ingredient in the pantry to the pantry database. It is possible for the end user to attempt to add an ingredient to the pantry twice - this causes an
     * SQLLiteConstraint exception which has been caught. A toast will also be shown to the user advising them that the ingredient already exists in the pantry.
     * caught to
     */

    @SuppressLint("StaticFieldLeak")
    public void addPantryIngredientToDB(final Ingredient ingredient) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(Pantry.this);
                try {
                    repo.addIngredientToPantry(ingredient);
                } catch (SQLiteConstraintException e) {
                    repo.insertLogs("Attempted to add an ingredient in the pantry that already existed");
                    return "Ingredient already exists in pantry";
                } finally {
                    repo.savePantryToUserDB();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                if (!TextUtils.isEmpty(message)){
                    Toast.makeText(Pantry.this,message,Toast.LENGTH_SHORT).show();
                }
                else {
                    ((PantryRecycleViewAdapter) recyclerView.getAdapter()).updateCategories(ingredient);
                }
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

}