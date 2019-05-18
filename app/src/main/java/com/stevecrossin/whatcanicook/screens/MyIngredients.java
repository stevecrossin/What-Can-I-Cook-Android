package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.MyIngredientViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class MyIngredients extends AppCompatActivity {
    private AppDataRepo repository;
    MyIngredientViewAdapter myIngredientViewAdapter;
    private static final String TAG = "MyIngredients";

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_myingredients XML and load the UI elements in that XML file into that view.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     * Initialise an instance of the AppDataRepo
     * Call the initRecyclerItems method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myingredients);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        repository = new AppDataRepo(this);
        initRecyclerItems();
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being my_ingredients_list
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on click which will call the rowClickListener in the adapter
     * <p>
     * 4. Set adapter for the RecyclerView
     * 5. Call loadIngredients() method to populate data into recycler
     */
    private void initRecyclerItems() {
        RecyclerView myIngredientsList = findViewById(R.id.my_ingredients_list);
        myIngredientsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myIngredientViewAdapter = new MyIngredientViewAdapter(new ArrayList<Ingredient>(), new MyIngredientViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(Ingredient ingredient) {
            }
        });
        myIngredientsList.setAdapter(myIngredientViewAdapter);
        loadIngredients();
    }

    /**
     * Method serves to load ingredients and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of ingredient from the database (getAllCheckedIngredients)
     * It will then store those ingredients in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {
        new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            @Override
            protected ArrayList<Ingredient> doInBackground(Void... voids) {
                return new ArrayList<>(repository.getAllCheckedIngredients());
            }

            @Override
            protected void onPostExecute(ArrayList<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                myIngredientViewAdapter.updateIngredients(ingredients);
            }
        }.execute();
    }

    /**
     * This is an OnClick method that is called when the "Find Recipes" button is clicked in the activity. It will load the Recipes.class, and then start that activity.
     */
    public void findRecipes(View view) {
        Intent intent = new Intent(MyIngredients.this, Recipes.class);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Home" icon is clicked in the activity. It will load the MainActivity.class, and then start that activity.
     */
    public void navigateHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
