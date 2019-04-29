package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Recipe;

import java.util.ArrayList;

public class RecipesDetails extends AppCompatActivity {
    Recipe recipe;
    ArrayList<String> missingIngredients;
    ImageButton shareButton;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        shareButton = findViewById(R.id.share_button);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("RECIPE");
        missingIngredients = intent.getStringArrayListExtra("MISSING");
        String missing = "";
        for (String string : missingIngredients)
            missing += string + ", ";
        if (!missing.equals(""))
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

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = recipe.getRecipeName() + "\n\n" + recipe.getRecipeIngredients() + "\n\n" + recipe.getRecipeSteps();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, recipe.getRecipeName());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share recipe via"));
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

    }
}