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
import android.widget.TextView;

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

    /*Set view as saved recipes activity*/
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
                        ArrayList<String> missingIngredients = new ArrayList<>(repository.getMissingIngredientsByName(recipe.getRecipeName(), 0));
                        Intent intent = new Intent(SavedRecipes.this, RecipesDetails.class);
                        intent.putExtra("RECIPE", recipe);
                        intent.putExtra("MISSING", missingIngredients);
                        startActivity(intent);
                        return null;
                    }
                }.execute();
            }

        },
                //New instance of remove click listener. OnClick of the X, a snackbar will be shown asking them to confirm the deletion/removal. Once clicked, it will be removed
                //from saved recipes
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

    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
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

    //Shows and hides the snackbar. Will also hide the keypad when this pops up. Snackbar will be shown for a "long" period of time". Also sets the text in the snackbar
    //and colours
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

            // This is a tweak to show snackBar text
            // with more than two lines
            TextView snackText = snackBar.getView().findViewById(R.id.snackbar_text);
            if (snackText != null) {
                snackText.setMaxLines(6);
            }

            snackBar.show();
        }
    }

    //Hides snacbar
    public void hideSnackBar() {
        if (snackBar != null && snackBar.isShown())
            snackBar.dismiss();
    }

    //Functon to hide and show the keypad
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