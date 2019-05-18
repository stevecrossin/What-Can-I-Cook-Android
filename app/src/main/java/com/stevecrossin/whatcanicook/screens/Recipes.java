package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class Recipes extends AppCompatActivity {
    private AppDataRepo repository;
    RecipeViewAdapter recipeViewAdapter;
    Switch exactMatch;
    LinearLayout addingList;

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the view for the layout and set it as current, being activity_recipe_results, and load all other UI elements to the view, including the
     * exact match switch, and the addingList where missing ingredients are displayed, which is hidden by default.
     * <p>
     * 2. Set an onCheckChanged listener to the switch that determines exact match requested or not, and call the relevant method when clicked.
     * <p>
     * This gets ingredients from the database with an onClick listener defined as missingClicked that will then call the loadRecipes
     * and the initSuggestions method
     * <p>
     * Load Google Ads for the activity and send an adRequest to load an ad.
     * Call the initRecyclerItems and initSuggestions methods
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
                    loadRecipesExactMatch();
                else
                    loadRecipes();
            }
        });
        addingList = findViewById(R.id.adding_list);
        addingList.setVisibility(View.GONE);
        repository = new AppDataRepo(this);
        initRecyclerItems();
        initSuggestions();
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
            loadRecipes();
            initSuggestions();
        }
    };

    /**
     * Initialize the suggestion of ingredients.
     * Initially a list of suggested ingredient (addingList) is empty
     * The AsyncTask will be called and performed in the background to return a list of missing ingredients (an array list of string)
     * It will call the database method getAllRecipesByCheckedIngredients() to get all recipes whose ingredients are most similar to what user is having.
     * <p>
     * If some recipes exist for their selected ingredients:
     * (a) it will try to pick the top most recipe - the recipe with the most ingredients that the user has, then return in a descending order.
     * (b) Using the top most similar recipe, we again make a call to DB with getMissingIngredientsByName() that returns a list of 3 top most missing ingredients for that recipe.
     * If there existed some missing ingredients, return them
     * (c) If there is no missing ingredients, we will move on to pick the second top most similar recipe.
     * (d) We can achieve this by incrementing the offset value by one and call the DB method getAllRecipesByCheckedIngredientsWithOffset() with offset as parameter.
     * The above (a), (b), (c) and (d) step is then done again in a while loop.
     * The loop will not be entered anymore when ALL of the recipes have NO MORE missing ingredients.
     * This will enable the user to be able to continuously click on the suggested missing ingredient until there is no more.
     */
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
                    ArrayList<String> missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(similarRecipe.getRecipeName(), 3));
                    while (missingIngredients.size() == 0 && offset <= similarRecipes.size() - 1) {
                        offset += 1;
                        similarRecipes = new ArrayList<>(repository.getAllRecipesByCheckedIngredientsWithOffset(offset));
                        similarRecipe = similarRecipes.get(0);
                        missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(similarRecipe.getRecipeName(), 3));
                    }
                    return missingIngredients;
                }
                return null;
            }

            /**
             * Method to load the missing ingredients into the viewholder.
             * If missingIngredients are not empty and size is greater than zero, each missing will be displayed
             * in the viewholder (addingList), parameters such as size, length, padding, and onclick listeners are defined.
             * <p>
             * Also defined as single line only, with an ellipis to exist at the end of any ingredient to prevent cutoff.
             * Each missing ingredient will be set an onclick listener
             * The list will have its visibility set to true if missing ingredients exist, otherwise it well be set back to "false"
             */
            @Override
            protected void onPostExecute(ArrayList<String> missingIngredients) {
                super.onPostExecute(missingIngredients);
                if (missingIngredients != null && missingIngredients.size() > 0) {
                    addingList.setVisibility(View.VISIBLE);
                    for (String string : missingIngredients) {
                        final TextView ingredient = new TextView(Recipes.this);
                        ingredient.setText(string);
                        ingredient.setTag(string);
                        ingredient.setTextSize(15);
                        ingredient.setTypeface(null, Typeface.BOLD);
                        ingredient.setPadding(20, 0, 20, 0);
                        ingredient.setOnClickListener(missingClicked);
                        ingredient.setSingleLine(true);
                        ingredient.setEllipsize(TextUtils.TruncateAt.END);
                        addingList.addView(ingredient);
                    }
                } else {
                    addingList.setVisibility(View.GONE);
                }

            }
        }.execute();
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being saved_recipes_list
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on row clicked
     * <p>
     * Every time a row is clicked, user will be navigated to the RecipesDetails page
     * Extra data is also passed, including the recipe object.
     * <p>
     * 4. Set adapter for the RecyclerView
     * 5. Call loadRecipes() method to populate data into recycler
     * <p>
     * Additionally, all adapters have stable IDs. This was implemented to enable the recylerView elements to update in a clean way without visibly refreshing.
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
                        Intent intent = new Intent(Recipes.this, RecipesDetails.class);
                        intent.putExtra("RECIPE", recipe);
                        startActivity(intent);
                        return null;
                    }
                }.execute();
            }

        });
        recipeViewAdapter.setHasStableIds(true);
        recipesList.setAdapter(recipeViewAdapter);
        loadRecipes();
    }

    /**
     * Method serves to load all recipes from DB that match the users ingredients (can have missing ingredients) and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of all ingredients from the database, including recipes
     * with missing ingredients (getAllRecipesByCheckedIngredients)
     * It will then store those Recipes in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
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

    /**
     * Method serves to load all recipes from DB that exactly match the users ingredients (no missing) and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of all ingredients from the database that have no missing ingredients (getAllRecipesByCheckedIngredientsWithExactMatch)
     * It will then store those Recipes in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
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

    /**
     * This is an OnClick method that is called when the "Start Over" icon is clicked in the activity. It will create a new instance of AppDataRepo and then perform the clearIngredients function
     * which will reset all ingredients to "ingredient_selected=0" as per the method defined in the repo.
     * <p>
     * It will then load the MainActivity.class, and then start that activity.
     */
    public void resetIngredients(View view) {
        new AppDataRepo(this).clearIngredients();
        Intent intent = new Intent(this, MainActivity.class);
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

