package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.adapter.MyRecipesViewAdapter
import com.stevecrossin.whatcanicook.entities.Recipe
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * SavedRecipes is the class which is run when the user clicks on the SavedRecipes button in MainActivity. It will allow the user to see all the recipes they've saved,
 * remove saved recipes, and through navigation to CustomRecipe class, add their own recipes which will also be displayed in this view.
 */
class SavedRecipes : AppCompatActivity() {

    internal var recipeViewAdapter: MyRecipesViewAdapter
    private var repository: AppDataRepo? = null
    internal var mainLayout: ConstraintLayout
    internal var snackBar: Snackbar? = null

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_savedrecipes XML and load the UI elements in that XML file into that view.
     * Set anOnClick listener to the "Add Custom Recipe" button, when user clicks it will navigate them to the CustomRecipe scene
     * Call the initRecyclerItems method
     * Load Google Ads for the activity and send an adRequest to load an ad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savedrecipes)
        repository = AppDataRepo(this)
        val addRecipeButton = findViewById<Button>(R.id.add_recipe)
        mainLayout = findViewById(R.id.main_container)

        addRecipeButton.setOnClickListener {
            val intent = Intent(this@SavedRecipes, CustomRecipe::class.java)
            startActivity(intent)
        }

        initRecyclerItems()

        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being saved_recipes_list
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on row clicked
     *
     *
     * Every time a row is clicked, user will be navigated to the RecipesDetails page
     * Extra data is also passed, including the recipe object.
     *
     *
     * 4. Set adapter for the RecyclerView
     * 5. Call loadRecipes() method to populate data into recycler
     *
     *
     * After this, a new instance of removeClickListener will be created. When the user clicks the "X" in the row, a snackbar will be shown asking them to confirm deletion. Once they click "Yes", the removeRecipeFromSaved function in myRecipesViewHolder
     * wll be executed
     */
    private fun initRecyclerItems() {
        val recipesList = findViewById<RecyclerView>(R.id.saved_recipes_list)
        recipesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recipeViewAdapter = MyRecipesViewAdapter(ArrayList(), MyRecipesViewAdapter.rowClickedListener { recipe ->
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {
                    val intent = Intent(this@SavedRecipes, RecipesDetails::class.java)
                    intent.putExtra("RECIPE", recipe)
                    startActivity(intent)
                    return null
                }
            }.execute()
        },
                MyRecipesViewAdapter.removeClickedListener { recipe, myRecipesViewHolder -> showSnackBar("Are you sure you want to remove the recipe?", "YES", View.OnClickListener { recipeViewAdapter.removeRecipefromSaved(recipe, myRecipesViewHolder) }) })
        recipesList.adapter = recipeViewAdapter
        loadRecipes()
    }

    /**
     * Method serves to load saved recipes and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of Saved Recipes from the database (getAllSavedRecipes)
     * It will then store those Saved Recipes in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
    fun loadRecipes() {
        object : AsyncTask<Void, Void, ArrayList<Recipe>>() {
            override fun doInBackground(vararg voids: Void): ArrayList<Recipe> {

                return ArrayList(repository!!.allSavedRecipes)
            }

            override fun onPostExecute(recipes: ArrayList<Recipe>) {
                super.onPostExecute(recipes)
                recipeViewAdapter.updateRecipes(recipes)
            }
        }.execute()
    }

    /**
     * Shows the snackbar. Will also hide the keypad when this pops up. Snackbar will be shown for a "long" period of time". Also sets the text in the snackbar
     */

    fun showSnackBar(message: String, actionText: String, listener: View.OnClickListener) {
        if (!TextUtils.isEmpty(message)) {
            hideSnackBar()

            showForceKeypad(this, window.decorView, false)
            snackBar = Snackbar.make(mainLayout,
                    message, Snackbar.LENGTH_LONG)


            if (!TextUtils.isEmpty(actionText)) {
                snackBar!!.setAction(actionText, listener)
                snackBar!!.setActionTextColor(ContextCompat.getColor(this, R.color.primaryColor))
            }

            snackBar!!.show()
        }
    }

    /**
     * Hides the snackbar
     */
    fun hideSnackBar() {
        if (snackBar != null && snackBar!!.isShown)
            snackBar!!.dismiss()
    }

    /**
     * Function to force show/hide the keypad. Method learned and implemented from a snippet on StackOverFlow - https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     */
    fun showForceKeypad(context: Context?, view: View?, show: Boolean) {
        if (context != null) {
            val imm = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (view != null && imm != null) {
                if (show) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY)
                } else {
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
    }

}