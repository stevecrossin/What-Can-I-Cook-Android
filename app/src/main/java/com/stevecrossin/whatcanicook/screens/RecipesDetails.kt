package com.stevecrossin.whatcanicook.screens

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Recipe

/**
 * RecipesDetails is the class which is run when the user clicks on a recipe in the Recipes, or SavedRecipes view. It will show the user the details of the recipe, such as the full list of ingredients needed,
 * and the steps to cook the recipe, and allow the user to share the recipe if desired.
 */
class RecipesDetails : AppCompatActivity() {
    internal var recipe: Recipe
    internal var shareButton: ImageButton

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the view for the layout and set it as current, being activity_recipe_details
     * Get the data passed through an intent ("RECIPE") from the Recipes activity, and then set the relevent fields in the UI for each field passed to the contents passed.
     * For example, Recipes will pass a recipeName, recipeIngredients, recipeSteps, and the recipeImage, if it exists. This onCreate method will replace the contents of each
     * UI element in RecipeDetails to the contents of the information passed, whether it is with the setText or setImageResource method.
     * An example would look like ("Chicken Salad", "1/2 cup cooked chicken:200 grams lettuce", "Combine chicken and lettuce into a bowl then serve","chickensalad.png")
     *
     *
     * 2. Set an onClick listener for the share button. When clicked, the contents of the recipeName, recipeIngredients, RecipeSteps will be gathered into plain text and then
     * a dialog will open prompting the user on how they wish to share the recipe, via the ACTION_SEND intent built into Android.
     *
     *
     * 3. Load Google Ads for the activity and send an adRequest to load an ad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        shareButton = findViewById(R.id.share_button)

        val intent = intent
        recipe = intent.getSerializableExtra("RECIPE") as Recipe

        val recipeName = findViewById<TextView>(R.id.recipe_name)
        recipeName.text = recipe.recipeName

        val recipeIngredients = findViewById<TextView>(R.id.recipe_ingredients_content)
        recipeIngredients.text = recipe.recipeIngredients!!.replace(":".toRegex(), "\n")

        val recipeSteps = findViewById<TextView>(R.id.recipe_steps_content)
        recipeSteps.text = recipe.recipeSteps!!.replace(":".toRegex(), "\n")

        val recipeImage = findViewById<ImageView>(R.id.recipe_img)
        val drawableResourceId = this.resources.getIdentifier(recipe.recipeImage, "drawable", this.packageName)
        recipeImage.setImageResource(drawableResourceId)

        shareButton.setOnClickListener {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = recipe.recipeName + "\n\n" + recipe.recipeIngredients + "\n\n" + recipe.recipeSteps
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, recipe.recipeName)
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share recipe via"))
        }

        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }

    /**
     * This is an OnClick method that is called when the "Home" icon is clicked in the activity. It will load the MainActivity.class, and then start that activity.
     */
    fun navigateHome(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}