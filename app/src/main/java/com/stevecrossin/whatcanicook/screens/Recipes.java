package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.RecipeViewAdapter;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

//This class handles all the recipes functions for this application, including reading the recipes and providing recipe results
public class Recipes extends AppCompatActivity {
    private AppDataRepo repository;
    RecipeViewAdapter recipeViewAdapter;
    private static final String TAG = "Recipes";
    Switch exactMatch;
    LinearLayout addingList;

    /**
     * Scene initalization. This also loads the neccessary ingredient from the CSV to the database
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);
        exactMatch = findViewById(R.id.exactMatch_switch);
        exactMatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    //update exact match
                    loadRecipesExactMatch();
                else
                    //normal search
                    loadRecipes();
            }
        });
        addingList = findViewById(R.id.adding_list);
        repository = new AppDataRepo(this);
        initRecyclerItems();
        initSuggestions();
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @SuppressLint("StaticFieldLeak")
    private void initSuggestions() {
        addingList.removeAllViews();
        new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                int offset = 0;
                ArrayList<Recipe> similarRecipes = new ArrayList<>(repository.getAllRecipesByCheckedIngredients(0));
                if (similarRecipes.size() > 0) {
                    Recipe similarRecipe = similarRecipes.get(0);
                    ArrayList<String> missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(similarRecipe.getRecipeName(), 4));
                    while (missingIngredients.size() == 0 && offset <= similarRecipes.size() - 1){
                        offset += 1;
                        similarRecipes = new ArrayList<>(repository.getAllRecipesByCheckedIngredientsWithOffset(offset));
                        similarRecipe = similarRecipes.get(0);
                        missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(similarRecipe.getRecipeName(), 4));
                    }
                    return missingIngredients;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> missingIngredients) {
                super.onPostExecute(missingIngredients);
                if (missingIngredients != null)
                    for (String string : missingIngredients) {
                        final TextView ingredient = new TextView(Recipes.this);
                        ingredient.setText(string);
                        ingredient.setTag(string);
                        ingredient.setTextSize(14);
                        ingredient.setPadding(20, 0, 20, 0);
                        ingredient.setOnClickListener(missingClicked);
                        addingList.addView(ingredient);
                    }
            }
        }.execute();
    }

    View.OnClickListener missingClicked = new View.OnClickListener() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onClick(View v) {
            final TextView ingredient = (TextView) v;
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    repository.selectIngredient(ingredient.getText().toString());
                    return null;
                }
            }.execute();
            initRecyclerItems();
            initSuggestions();
        }
    };

    /**
     * This will do the setup step for our recycle view:
     * 1. find the recycle view in the layout with id recipes_list
     * 2. set the layout manager
     * 3. set up event listener for recycleview on row clicked
     * 4. set adapter for the recycle view
     * 5. finall, call loadRecipes() method to populate data
     */
    private void initRecyclerItems() {
        RecyclerView recipesList = findViewById(R.id.recipes_list);
        recipesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recipeViewAdapter = new RecipeViewAdapter(new ArrayList<Recipe>(), new RecipeViewAdapter.rowClickedListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onRowClicked(final Recipe recipe) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        ArrayList<String> missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(recipe.getRecipeName(), 0));
                        Intent intent = new Intent(Recipes.this, RecipesDetails.class);
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

    /**
     * Testing method. Trying to load all recipes into the recycleview to simulate recipe search
     * This in the future will be replaced with findRecipes and displayRecipes below
     */
    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
    public void loadRecipes() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {
                return new ArrayList<>(repository.getAllRecipesByCheckedIngredients(0));
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> recipes) {
                super.onPostExecute(recipes);
                recipeViewAdapter.updateRecipes(recipes);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
    public void loadRecipesExactMatch() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {
                return new ArrayList<>(repository.getAllRecipesByCheckedIngredientsWithExactMatch());
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> recipes) {
                super.onPostExecute(recipes);
                recipeViewAdapter.updateRecipes(recipes);
            }
        }.execute();
    }

    public void resetIngredients(View view) {
        new AppDataRepo(this).clearIngredients();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

