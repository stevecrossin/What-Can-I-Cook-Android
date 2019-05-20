package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.adapter.IngredientViewAdapter
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * IngredientPicker is the class which is run when the user clicks on a category in CategoryPicker, which allows the user to select ingredients they have to cook with.
 */
class IngredientPicker : AppCompatActivity() {
    private var repository: AppDataRepo? = null
    private var category: String? = null
    internal var ingredientViewAdapter: IngredientViewAdapter

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_ingredientsPicker XML and load the UI elements in that XML file into that view.
     * Get the string passed from the previous activity (CategoriesPicker) as an intent, and then set the categorychosentext textview to the contents of that string
     *
     *
     * Initialise an instance of the AppDataRepo
     * Call the initRecyclerItems method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredientspicker)
        val intent = intent
        category = intent.getStringExtra("CATEGORY")
        val textView = findViewById<TextView>(R.id.categorychosentext)
        textView.text = category

        repository = AppDataRepo(this)
        initRecyclerItems()
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being ingredients_list.
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up the adapter for the recycler view as categoryViewAdapter
     * Finally, call the loadIngredients method.
     */
    private fun initRecyclerItems() {
        val ingredientsList = findViewById<RecyclerView>(R.id.ingredients_list)
        ingredientsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ingredientViewAdapter = IngredientViewAdapter(object : ArrayList<Ingredient>() {

        })

        ingredientsList.adapter = ingredientViewAdapter
        loadIngredients()
    }

    /**
     * Core function to load ingredients and update recycleview adapter.
     * This function performs an async task in the background to get a list of ingredient from the database (getByCategory)
     * An example of a returned ingredient would be "chicken"
     * It will then store those ingredients in an ArrayList and return the list.
     */
    @SuppressLint("StaticFieldLeak")
    fun loadIngredients() {
        object : AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            override fun doInBackground(vararg voids: Void): ArrayList<Ingredient> {
                return ArrayList(repository!!.getIngredientsByCategory(category))
            }

            override fun onPostExecute(ingredients: ArrayList<Ingredient>) {
                super.onPostExecute(ingredients)
                ingredientViewAdapter.updateIngredients(ingredients)
            }
        }.execute()
    }

    /**
     * This is an OnClick method that is called when the "Check My Ingredients" button is clicked in the activity. It will load the MyIngredients class, and then start that activity.
     */
    fun myIngredients(view: View) {
        val intent = Intent(this@IngredientPicker, MyIngredients::class.java)
        startActivity(intent)
    }
}
