package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.MyRecipesViewAdapter;
import com.stevecrossin.whatcanicook.adapter.MyRecipesViewHolder;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class SavedRecipes extends AppCompatActivity {

    MyRecipesViewAdapter recipeViewAdapter;
    private AppDataRepo repository;
    ConstraintLayout mainLayout;
    Snackbar snackBar;

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_savedrecipes XML and load the UI elements in that XML file into that view.
     * Set anOnClick listener to the "Add Custom Recipe" button, when user clicks it will navigate them to the CustomRecipe scene
     * Call the initRecyclerItems method
     * Load Google Ads for the activity and send an adRequest to load an ad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedrecipes);
        repository = new AppDataRepo(this);
        Button addRecipeButton = findViewById(R.id.add_recipe);
        mainLayout = findViewById(R.id.main_container);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedRecipes.this, CustomRecipe.class);
                startActivity(intent);
            }
        });

        initRecyclerItems();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
     * After this, a new instance of removeClickListener will be created. When the user clicks the "X" in the row, a snackbar will be shown asking them to confirm deletion. Once they click "Yes", the removeRecipeFromSaved function in myRecipesViewHolder
     * wll be executed
     */
    private void initRecyclerItems() {
        RecyclerView recipesList = findViewById(R.id.saved_recipes_list);
        recipesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recipeViewAdapter = new MyRecipesViewAdapter(new ArrayList<Recipe>(), new MyRecipesViewAdapter.rowClickedListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onRowClicked(final Recipe recipe) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Intent intent = new Intent(SavedRecipes.this, RecipesDetails.class);
                        intent.putExtra("RECIPE", recipe);
                        startActivity(intent);
                        return null;
                    }
                }.execute();
            }

        },
                new MyRecipesViewAdapter.removeClickedListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onRemoveClicked(final Recipe recipe, final MyRecipesViewHolder myRecipesViewHolder) {
                        showSnackBar("Are you sure you want to remove the recipe?", "YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recipeViewAdapter.removeRecipefromSaved(recipe, myRecipesViewHolder);
                            }
                        });
                    }
                });
        recipesList.setAdapter(recipeViewAdapter);
        loadRecipes();
    }

    /**
     * Method serves to load saved recipes and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of Saved Recipes from the database (getAllSavedRecipes)
     * It will then store those Saved Recipes in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
    public void loadRecipes() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {

                return new ArrayList<>(repository.getAllSavedRecipes());
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> recipes) {
                super.onPostExecute(recipes);
                recipeViewAdapter.updateRecipes(recipes);
            }
        }.execute();
    }

    /**
     * Shows the snackbar. Will also hide the keypad when this pops up. Snackbar will be shown for a "long" period of time". Also sets the text in the snackbar
     */

    public void showSnackBar(String message, String actionText, View.OnClickListener listener) {
        if (!TextUtils.isEmpty(message)) {
            hideSnackBar();

            showForceKeypad(this, getWindow().getDecorView(), false);
            snackBar = Snackbar.make(mainLayout,
                    message, Snackbar.LENGTH_LONG);


            if (!TextUtils.isEmpty(actionText)) {
                snackBar.setAction(actionText, listener);
                snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.primaryColor));
            }

            snackBar.show();
        }
    }
    /**
     * Hides the snackbar
     */
    public void hideSnackBar() {
        if (snackBar != null && snackBar.isShown())
            snackBar.dismiss();
    }

    /**
     * Function to force show/hide the keypad. Method learned and implemented from a snippet on StackOverFlow - https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     */
    public void showForceKeypad(Context context, View view, boolean show) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null && imm != null) {
                if (show) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

}