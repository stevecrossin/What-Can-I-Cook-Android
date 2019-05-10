package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

public class CustomRecipe extends AppCompatActivity {
    private AppDataRepo repository;
    AutoCompleteTextView recipeName, recipeIngredients, recipeSteps;
    private static final String TAG = "CustomRecipe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customrecipes);
        repository = new AppDataRepo(this);

        recipeName = findViewById(R.id.recipeManualEntry);
        recipeIngredients = findViewById(R.id.recipeIngredientsEntry);
        recipeSteps = findViewById(R.id.recipeStepsEntry);
    }

    @SuppressLint("StaticFieldLeak")
    public void addCustomRecipe(View view) {

        Log.d(TAG, "addCustomRecipe: " + recipeName.getText().toString() + " " + recipeIngredients.getText().toString() + " " + recipeSteps.getText().toString());
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