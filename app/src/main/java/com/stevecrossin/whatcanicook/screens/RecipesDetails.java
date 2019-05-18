package com.stevecrossin.whatcanicook.screens;

import android.content.Intent;
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

/**
 * RecipesDetails is the class which is run when the user clicks on a recipe in the Recipes, or SavedRecipes view. It will show the user the details of the recipe, such as the full list of ingredients needed,
 * and the steps to cook the recipe, and allow the user to share the recipe if desired.
 */
public class RecipesDetails extends AppCompatActivity {
    Recipe recipe;
    ImageButton shareButton;

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the view for the layout and set it as current, being activity_recipe_details
     * Get the data passed through an intent ("RECIPE") from the Recipes activity, and then set the relevent fields in the UI for each field passed to the contents passed.
     * For example, Recipes will pass a recipeName, recipeIngredients, recipeSteps, and the recipeImage, if it exists. This onCreate method will replace the contents of each
     * UI element in RecipeDetails to the contents of the information passed, whether it is with the setText or setImageResource method.
     * <p>
     * 2. Set an onClick listener for the share button. When clicked, the contents of the recipeName, recipeIngredients, RecipeSteps will be gathered into plain text and then
     * a dialog will open prompting the user on how they wish to share the recipe, via the ACTION_SEND intent built into Android.
     * <p>
     * 3. Load Google Ads for the activity and send an adRequest to load an ad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        shareButton = findViewById(R.id.share_button);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("RECIPE");

        TextView recipeName = findViewById(R.id.recipe_name);
        recipeName.setText(recipe.getRecipeName());

        TextView recipeIngredients = findViewById(R.id.recipe_ingredients_content);
        recipeIngredients.setText(recipe.getRecipeIngredients().replaceAll(":", "\n"));

        TextView recipeSteps = findViewById(R.id.recipe_steps_content);
        recipeSteps.setText(recipe.getRecipeSteps().replaceAll(":", "\n"));

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

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    /**
     * This is an OnClick method that is called when the "Home" icon is clicked in the activity. It will load the MainActivity.class, and then start that activity.
     */
    public void navigateHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}