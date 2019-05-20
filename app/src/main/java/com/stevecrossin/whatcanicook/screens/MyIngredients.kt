package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.adapter.MyIngredientViewAdapter
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * MyIngredients is the class which is run when the user clicks on "Check My Ingredients" in either the CategoryPicker or IngredientPicker classes. It will show the user the ingredients they
 * have selected, allow them to remove ingredients, and then perform a search for recipes.
 */
class MyIngredients : AppCompatActivity() {
    private var repository: AppDataRepo? = null
    internal var myIngredientViewAdapter: MyIngredientViewAdapter

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_myingredients XML and load the UI elements in that XML file into that view.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     * Initialise an instance of the AppDataRepo
     * Call the initRecyclerItems method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myingredients)
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        repository = AppDataRepo(this)
        initRecyclerItems()
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being my_ingredients_list
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on click which will call the rowClickListener in the adapter
     *
     *
     * 4. Set adapter for the RecyclerView
     * 5. Call loadIngredients() method to populate data into recycler
     */
    private fun initRecyclerItems() {
        val myIngredientsList = findViewById<RecyclerView>(R.id.my_ingredients_list)
        myIngredientsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myIngredientViewAdapter = MyIngredientViewAdapter(ArrayList(), MyIngredientViewAdapter.rowClickedListener { })
        myIngredientsList.adapter = myIngredientViewAdapter
        loadIngredients()
    }

    /**
     * Method serves to load ingredients and then update the recycleView adapter.
     * This function performs an async task in the background to get a list of ingredient from the database (getAllCheckedIngredients)
     * It will then store those ingredients in an ArrayList and return the list to the recipeViewAdapter so that the recyclerView is updated.
     */
    @SuppressLint("StaticFieldLeak")
    fun loadIngredients() {
        object : AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            override fun doInBackground(vararg voids: Void): ArrayList<Ingredient> {
                return ArrayList(repository!!.allCheckedIngredients)
            }

            override fun onPostExecute(ingredients: ArrayList<Ingredient>) {
                super.onPostExecute(ingredients)
                myIngredientViewAdapter.updateIngredients(ingredients)
            }
        }.execute()
    }

    /**
     * This is an OnClick method that is called when the "Find Recipes" button is clicked in the activity. It will load the Recipes.class, and then start that activity.
     */
    fun findRecipes(view: View) {
        val intent = Intent(this@MyIngredients, Recipes::class.java)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Home" icon is clicked in the activity. It will load the MainActivity.class, and then start that activity.
     */
    fun navigateHome(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = "MyIngredients"
    }

}
