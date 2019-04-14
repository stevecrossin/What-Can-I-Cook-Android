package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

public class RecipesDetails extends AppCompatActivity {
    Recipe recipe;
    ArrayList<String> missingIngredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("RECIPE");
        missingIngredients = intent.getStringArrayListExtra("MISSING");
        String missing = "";
        for (String string : missingIngredients)
            missing += string + ", ";
        missing = missing.substring(0, missing.length() - 2);
        TextView recipeName = findViewById(R.id.recipe_name);
        TextView recipeIngredients = findViewById(R.id.recipe_ingredients);
        TextView recipeSteps = findViewById(R.id.recipe_steps);
        TextView warning = findViewById(R.id.recipe_warning);

        recipeName.setText(recipe.getRecipeName());
        recipeIngredients.setText(recipe.getRecipeIngredients());
        recipeSteps.setText(recipe.getRecipeSteps());

        if (missingIngredients.size() > 0) {
            warning.setTextColor(Color.RED);
            warning.setText(String.format("You are missing: %s", missing));
        }
        else {
            warning.setTextColor(Color.GREEN);
            warning.setText(getString(R.string.enoughIngredients));
        }
    }
}
