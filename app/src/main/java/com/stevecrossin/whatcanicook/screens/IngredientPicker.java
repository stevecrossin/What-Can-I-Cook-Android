package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.CategoryViewAdapter;
import com.stevecrossin.whatcanicook.adapter.IngredientViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class IngredientPicker extends AppCompatActivity {
    private AppDataRepo repository;
    private static final String TAG = "IngredientPicker";
    private String category;
    IngredientViewAdapter ingredientViewAdapter;
    private AdView mAdView;

    /**
     * Initialization of this scene. This will get the category string passed by last scene and display to 'categorychosentext'
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientspicker);

        Intent intent = getIntent();

        category = intent.getStringExtra("CATEGORY");
        TextView textView = findViewById(R.id.categorychosentext);
        textView.setText(category);


        repository = new AppDataRepo(this);
        initRecyclerItems();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
    }

    /**
     * This will do the setup step for our recycle view:
     * 1. find the recycle view in the layout with id ingredients_list
     * 2. set the layout manager
     * 3. set up event listener for recycleview on row clicked [DEBUGGING PURPOSE]
     * 4. set adapter for the recycle view
     * 5. finall, call loadingredients method to populate data
     */
    private void initRecyclerItems() {
        RecyclerView ingredientsList = findViewById(R.id.ingredients_list);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        ingredientViewAdapter = new IngredientViewAdapter(new ArrayList<Ingredient>(), new IngredientViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(Ingredient ingredient) {
                Log.d(TAG, "onRowClicked: " + ingredient.getIngredientName());
            }
        });

        ingredientsList.setAdapter(ingredientViewAdapter);

        loadIngredients();
    }

    /**
     * Core function to load ingredients and update recycleview adapter.
     * This function performs an async task in the background to get a list of ingredient from the database (getByCategory)
     * It will then store those ingredients in an ArrayList and return the list.
     */
    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {
        new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            @Override
            protected ArrayList<Ingredient> doInBackground(Void... voids) {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients.addAll(repository.getIngredientsByCategory(category));
                for (Ingredient ingredient : ingredients) {
                    Log.d(TAG, "ingredient name: " + ingredient.getIngredientName());
                    Log.d(TAG, "excluded : " + ingredient.isIngredientExcluded());
                }
                return ingredients;
            }

            @Override
            protected void onPostExecute(ArrayList<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                ingredientViewAdapter.updateIngredients(ingredients);
            }
        }.
                execute();
    }

    public void myIngredients(View view) {
        Intent intent = new Intent(IngredientPicker.this, MyIngredients.class);
        startActivity(intent);
    }
}
