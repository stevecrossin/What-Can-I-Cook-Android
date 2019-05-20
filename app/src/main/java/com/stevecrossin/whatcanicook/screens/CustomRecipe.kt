package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AutoCompleteTextView

import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Recipe
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo

/**
 * CustomRecipe is the class which is run when the user clicks on "Add a Recipe" in the saved recipes view, which allows the user to enter their own recipe
 */
class CustomRecipe : AppCompatActivity() {
    private var repository: AppDataRepo? = null
    internal var recipeName: AutoCompleteTextView
    internal var recipeIngredients: AutoCompleteTextView
    internal var recipeSteps: AutoCompleteTextView

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_customrecipes XML, find by ID and then load the UI elements in that XML file into that view.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customrecipes)
        repository = AppDataRepo(this)
        recipeName = findViewById(R.id.recipe_name)
        recipeIngredients = findViewById(R.id.recipe_ingredients)
        recipeSteps = findViewById(R.id.recipe_steps)
    }

    /**
     * This method is called when the user clicks the "Save Recipe" button in the activity.
     * It will get the text contents that are currently stored in the text boxes, being the recipe name, ingredients and steps and convert them to a string. It will then
     * through an async task insert this recipe into the recipe database, also noting the recipe as saved, and a custom recipe (not a recipe that came with the app).
     *
     *
     * Finally, the application will load the SavedRecipes.class into memory and start that activity.
     */
    @SuppressLint("StaticFieldLeak")
    fun addCustomRecipe(view: View) {

        val customRecipe = Recipe(recipeName.text.toString(), "", recipeIngredients.text.toString(), recipeSteps.text.toString())
        customRecipe.isCustomed = true
        customRecipe.isSaved = true
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                repository!!.insertRecipe(customRecipe)
                return null
            }
        }.execute()

        val intent = Intent(this@CustomRecipe, SavedRecipes::class.java)
        startActivity(intent)
    }
}