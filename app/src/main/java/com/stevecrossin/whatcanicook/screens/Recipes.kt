package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.adapter.RecipeViewAdapter
import com.stevecrossin.whatcanicook.entities.Recipe
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * Recipes is the class which is run when the user clicks on the FindRecipes button from MyIngredients class. It will show the user recipe results for the ingredients they have selected,
 * allow them to select additional ingredients that they are missing based on suggestions they have been presented by the app, save recipes for future use and through clicking on the recipe, navigate to RecipeDetails
 * to see the details of the recipe
 */
class Recipes : AppCompatActivity() {
    private var repository: AppDataRepo? = null
    internal var recipeViewAdapter: RecipeViewAdapter
    internal var exactMatch: Switch
    internal var addingList: LinearLayout

    internal var missingClicked: View.OnClickListener = View.OnClickListener { v ->
        val ingredient = v as TextView
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                repository!!.selectIngredient(ingredient.text.toString())
                return null
            }
        }.execute()
        loadRecipes()
        initSuggestions()
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the view for the layout and set it as current, being activity_recipe_results, and load all other UI elements to the view, including the
     * exact match switch, and the addingList where missing ingredients are displayed, which is hidden by default.
     *
     *
     * 2. Set an onCheckChanged listener to the switch that determines exact match requested or not, and call the relevant method when clicked.
     *
     *
     * This gets ingredients from the database with an onClick listener defined as missingClicked that will then call the loadRecipes
     * and the initSuggestions method
     *
     *
     * Load Google Ads for the activity and send an adRequest to load an ad.
     * Call the initRecyclerItems and initSuggestions methods
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_results)
        exactMatch = findViewById(R.id.exactMatch_switch)
        exactMatch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                loadRecipesExactMatch()
            else
                loadRecipes()
        }
        addingList = findViewById(R.id.adding_list)
        addingList.visibility = View.GONE
        repository = AppDataRepo(this)
        initRecyclerItems()
        initSuggestions()
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    /**
     * Initialize the suggestion of ingredients.
     * Initially a list of suggested ingredient (addingList) is empty
     * The AsyncTask will be called and performed in the background to return a list of missing ingredients (an array list of string)
     * It will call the database method getAllRecipesByCheckedIngredients() to get all recipes whose ingredients are most similar to what user is having.
     *
     *
     * If some recipes exist for their selected ingredients:
     * (a) it will try to pick the top most recipe - the recipe with the most ingredients that the user has, then return in a descending order.
     * (b) Using the top most similar recipe, we again make a call to DB with getMissingIngredientsByName() that returns a list of 3 top most missing ingredients for that recipe.
     *
     * If there are some missing ingredients, return them. An example of a returned missing ingredient would be "chicken"
     * (c) If there is no missing ingredients, we will move on to pick the second top most similar recipe.
     * (d) We can achieve this by incrementing the offset value by one and call the DB method getAllRecipesByCheckedIngredientsWithOffset() with offset as parameter.
     * The above (a), (b), (c) and (d) step is then done again in a while loop.
     * The loop will not be entered anymore when ALL of the recipes have NO MORE missing ingredients.
     * This will enable the user to be able to continuously click on the suggested missing ingredient until there is no more.
     */
    @SuppressLint("StaticFieldLeak")
    private fun initSuggestions() {
        addingList.removeAllViews()
        object : AsyncTask<Void, Void, ArrayList<String>>() {
            override fun doInBackground(vararg voids: Void): ArrayList<String>? {
                var offset = 0
                var similarRecipes = ArrayList(repository!!.getAllRecipesByCheckedIngredients(0))
                if (similarRecipes.size > 0) {
                    var similarRecipe = similarRecipes[0]
                    var missingIngredients = ArrayList(repository!!.getMissingIngredientsByName(similarRecipe.recipeName, 3))
                    while (missingIngredients.size == 0 && offset <= similarRecipes.size - 1) {
                        offset += 1
                        similarRecipes = ArrayList(repository!!.getAllRecipesByCheckedIngredientsWithOffset(offset))
                        similarRecipe = similarRecipes[0]
                        missingIngredients = ArrayList(repository!!.getMissingIngredientsByName(similarRecipe.recipeName, 3))
                    }
                    return missingIngredients
                }
                return null
            }

            /**
             * Method to load the missing ingredients into the viewholder.
             * If missingIngredients are not empty and size is greater than zero, each missing ingredient will be displayed
             * in the viewholder (addingList), parameters such as size, length, padding, and onclick listeners are defined.
             *
             *
             * Also defined as single line only, with an ellipis to exist at the end of any ingredient to prevent cutoff.
             * Each missing ingredient will be set an onclick listener
             * The list will have its visibility set to true if missing ingredients exist, otherwise it well be set back to "false"
             *
             */
            override fun onPostExecute(missingIngredients: ArrayList<String>?) {
                super.onPostExecute(missingIngredients)
                if (missingIngredients != null && missingIngredients.size > 0) {
                    addingList.visibility = View.VISIBLE
                    for (string in missingIngredients) {
                        val ingredient = TextView(this@Recipes)
                        ingredient.text = string
                        ingredient.tag = string
                        ingredient.textSize = 15f
                        ingredient.setTypeface(null, Typeface.BOLD)
                        ingredient.setPadding(20, 0, 20, 0)
                        ingredient.setOnClickListener(missingClicked)
                        ingredient.setSingleLine(true)
                        ingredient.ellipsize = TextUtils.TruncateAt.END
                        addingList.addView(ingredient)
                    }
                } else {
                    addingList.visibility = View.GONE
                }

            }
        }.execute()
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being saved_recipes_list
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on row clicked
     *
     *
     * Every time a row is clicked, user will be navigated to the RecipesDetails page
     * Recipe data is packaged together and passed to RecipesDetails via an intent in a bundle called "RECIPE" via an Async task
     *
     *
     * 4. Set adapter for the RecyclerView
     * 5. Call loadRecipes() method to populate data into recycler
     *
     *
     * Additionally, all adapters have stable IDs. This was implemented to enable the recylerView elements to update in a clean way without visibly refreshing.
     */
    private fun initRecyclerItems() {
        val recipesList = findViewById<RecyclerView>(R.id.recipes_list)
        recipesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recipeViewAdapter = RecipeViewAdapter(ArrayList(), RecipeViewAdapter.rowClickedListener { recipe ->
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {
                    val intent = Intent(this@Recipes, RecipesDetails::class.java)
                    intent.putExtra("RECIPE", recipe)
                    startActivity(intent)
                    return null
                }
            }.execute()
        })
        recipeViewAdapter.setHasStableIds(true)
        recipesList.adapter = recipeViewAdapter
        loadRecipes()
    }

    /**
     * Method serves to load all recipes from DB that match the users ingredients (can have missing ingredients) and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of all ingredients from the database, including recipes
     * with missing ingredients (getAllRecipesByCheckedIngredients)
     * It will then store those Recipes in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
    fun loadRecipes() {
        object : AsyncTask<Void, Void, ArrayList<Recipe>>() {
            override fun doInBackground(vararg voids: Void): ArrayList<Recipe> {
                return ArrayList(repository!!.getAllRecipesByCheckedIngredients(0))
            }

            override fun onPostExecute(recipes: ArrayList<Recipe>) {
                super.onPostExecute(recipes)
                recipeViewAdapter.updateRecipes(recipes)
            }
        }.execute()
    }

    /**
     * Method serves to load all recipes from DB that exactly match the users ingredients (no missing) and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of all ingredients from the database that have no missing ingredients (getAllRecipesByCheckedIngredientsWithExactMatch)
     * It will then store those Recipes in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
    fun loadRecipesExactMatch() {
        object : AsyncTask<Void, Void, ArrayList<Recipe>>() {
            override fun doInBackground(vararg voids: Void): ArrayList<Recipe> {
                return ArrayList(repository!!.allRecipesByCheckedIngredientsWithExactMatch)
            }

            override fun onPostExecute(recipes: ArrayList<Recipe>) {
                super.onPostExecute(recipes)
                recipeViewAdapter.updateRecipes(recipes)
            }
        }.execute()
    }

    /**
     * This is an OnClick method that is called when the "Start Over" icon is clicked in the activity. It will create a new instance of AppDataRepo and then perform the clearIngredients function
     * which will reset all ingredients to "ingredient_selected=0" as per the method defined in the repo.
     *
     *
     * It will then load the MainActivity.class, and then start that activity.
     */
    fun resetIngredients(view: View) {
        AppDataRepo(this).clearIngredients()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Home" icon is clicked in the activity. It will load the MainActivity.class, and then start that activity.
     */
    fun navigateHome(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

