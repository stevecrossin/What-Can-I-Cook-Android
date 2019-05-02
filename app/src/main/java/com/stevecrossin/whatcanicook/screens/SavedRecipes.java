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
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.RecipeViewAdapter;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class SavedRecipes extends AppCompatActivity {

    RecipeViewAdapter recipeViewAdapter;
    private AppDataRepo repository;
    private AdView mAdView;
    private Button addRecipeButton;

    /*Set view as saved recipes activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedrecipes);
        repository = new AppDataRepo(this);
        addRecipeButton = findViewById(R.id.add_recipe);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedRecipes.this, CustomRecipe.class);
                //intent.putExtra("CATEGORY", category);
                startActivity(intent);
            }
        });

        initRecyclerItems();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
    }

    private void initRecyclerItems() {
        RecyclerView recipesList = findViewById(R.id.saved_recipes_list);
        recipesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        recipeViewAdapter = new RecipeViewAdapter(new ArrayList<Recipe>(), new RecipeViewAdapter.rowClickedListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onRowClicked(final Recipe recipe) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        ArrayList<String> missingIngredients = new ArrayList<>();
                        missingIngredients.addAll(repository.getMissingIngredientsByName(recipe.getRecipeName(), 0));
                        //for (String string : missingIngredients)
                        // Log.d(TAG, "Missing ingredients: " + string + "\n");
                        Intent intent = new Intent(SavedRecipes.this, RecipesDetails.class);
                        intent.putExtra("RECIPE", recipe);
                        intent.putExtra("MISSING", missingIngredients);
                        startActivity(intent);
                        return null;
                    }
                }.execute();
            }

        });
        recipesList.setAdapter(recipeViewAdapter);
        loadRecipes();
    }

    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
    public void loadRecipes() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                recipes.addAll(repository.getAllSavedRecipes());

                return recipes;
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> recipes) {
                super.onPostExecute(recipes);
                recipeViewAdapter.updateRecipes(recipes);
            }
        }.execute();
    }
}