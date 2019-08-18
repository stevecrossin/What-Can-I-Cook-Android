package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

/**
 * CustomRecipe is the class which is run when the user clicks on "Add a Recipe" in the saved recipes view, which allows the user to enter their own recipe
 */
public class CustomRecipe extends AppCompatActivity {
    private AppDataRepo repository;
    AutoCompleteTextView recipeName, recipeIngredients, recipeSteps;

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_customrecipes XML, find by ID and then load the UI elements in that XML file into that view.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customrecipes);
        repository = new AppDataRepo(this);
        recipeName = findViewById(R.id.recipe_name);
        recipeIngredients = findViewById(R.id.recipe_ingredients);
        recipeSteps = findViewById(R.id.recipe_steps);
    }

    /**
     * This method is called when the user clicks the "Save Recipe" button in the activity.
     * It will get the text contents that are currently stored in the text boxes, being the recipe name, ingredients and steps and convert them to a string. It will then
     * through an async task insert this recipe into the recipe database, also noting the recipe as saved, and a custom recipe (not a recipe that came with the app).
     * <p>
     * Finally, the application will load the SavedRecipes.class into memory and start that activity.
     */
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