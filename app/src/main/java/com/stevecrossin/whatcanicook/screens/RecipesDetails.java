package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;

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
        recipeName.setText(recipe.getRecipeName());

        TextView recipeIngredients = findViewById(R.id.recipe_ingredients_content);
        recipeIngredients.setText(recipe.getRecipeIngredients().replaceAll(":", ",\n"));

        TextView recipeSteps = findViewById(R.id.recipe_steps_content);
        recipeSteps.setText(recipe.getRecipeSteps().replaceAll(":", ",\n"));

        TextView warning = findViewById(R.id.recipe_warning);
        if (missingIngredients.size() > 0) {
            warning.setTextColor(Color.RED);
            warning.setText(String.format("You are missing: %s", missing));
        } else {
            warning.setTextColor(Color.GREEN);
            warning.setText(getString(R.string.enoughIngredients));
        }


        ImageView recipeImage = findViewById(R.id.recipe_img);
        int drawableResourceId = this.getResources().getIdentifier(recipe.getRecipeImage(), "drawable", this.getPackageName());
        recipeImage.setImageResource(drawableResourceId);

    }
}