package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.ads.AdRequest;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class CustomRecipe extends AppCompatActivity {
    private AppDataRepo repository;
    AutoCompleteTextView recipeName, recipeIngredients, recipeSteps;
    private static final String TAG = "CustomRecipe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customrecipes);
        repository = new AppDataRepo(this);

        recipeName = findViewById(R.id.recipe_name);
        recipeIngredients = findViewById(R.id.recipe_ingredients);
        recipeSteps = findViewById(R.id.recipe_steps);
    }

    @SuppressLint("StaticFieldLeak")
    public void addCustomRecipe(View view) {

        final Recipe customRecipe = new Recipe(recipeName.getText().toString(), "", recipeIngredients.getText().toString(), recipeSteps.getText().toString());
        customRecipe.setCustomed(true);
        customRecipe.setSaved(true);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                repository.insertRecipe(customRecipe);
                return null;
            }
        }.execute();

        Intent intent = new Intent(CustomRecipe.this, SavedRecipes.class);
        startActivity(intent);
    }
}