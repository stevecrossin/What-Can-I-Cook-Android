package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.adapter.CategoryIngredientsAutocompleteAdapter
import com.stevecrossin.whatcanicook.adapter.CategoryViewAdapter
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * CategoryPicker is the class which is run when the user clicks on a dish type. It handles the selection of ingredient categories and navigation to "my ingredients"
 * as well as navigation to the ingredients picker class to select ingredients
 */
class CategoryPicker : AppCompatActivity() {
    private var repository: AppDataRepo? = null
    private var categoryViewAdapter: CategoryViewAdapter? = null
    private var autoCompleteTextView: AutoCompleteTextView? = null

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_categoriespicker XML and load the UI elements in that XML file into that view.
     * Get the string passed from the previous activity (MainActivity) as an intent, and then set the dishchosentext textview to the contents of that string
     *
     *
     * Initialise an instance of the AppDataRepo
     * Call the loadIngredients, initRecyclerItems and setupAutoComplete methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoriespicker)

        val intent = intent

        val dishtype = intent.getStringExtra("DISH_TYPE")
        val textView = findViewById<TextView>(R.id.dishchosentext)
        textView.text = dishtype

        repository = AppDataRepo(this)

        loadIngredients()
        initRecyclerItems()
        setupAutoComplete()
    }

    /**
     * Method to setup the autoCompleteAdapter. Finds the textView by ID and sets up an onClick listener, and gets the items position in the adapter that
     * has been selected. Once it has been selected, call the  then calls the markIngredientAsSelected, then finally call the getAllIngredients method
     */
    private fun setupAutoComplete() {
        autoCompleteTextView = findViewById(R.id.recipe_name)
        autoCompleteTextView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            autoCompleteTextView!!.text = null
            val ingredient = parent.getItemAtPosition(position) as Ingredient
            markIngredientAsSelected(ingredient.ingredientName)
        }
        getAllIngredients()
    }

    /**
     * Method to call to mark ingredients as selected. This is called from the setupAutoComplete onClick method, which when called runs an async task
     * to run in background the database operation to select the ingredient from the database
     */
    @SuppressLint("StaticFieldLeak")
    fun markIngredientAsSelected(ingredientName: String?) {
        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg voids: Void): Void? {
                repository!!.selectIngredient(ingredientName)
                return null
            }

        }.execute()
    }

    /**
     * Get all ingredients from the AppRepo database via an async task and create a new instance of the AutoComplete adapter,
     * and populate the adapter with the contents of the database
     */
    @SuppressLint("StaticFieldLeak")
    fun getAllIngredients() {
        object : AsyncTask<Void, Void, List<Ingredient>>() {

            override fun doInBackground(vararg voids: Void): List<Ingredient> {
                val repo = AppDataRepo(this@CategoryPicker)
                return repo.allTolerantIngredients
            }

            override fun onPostExecute(ingredients: List<Ingredient>) {
                super.onPostExecute(ingredients)
                val adapter = CategoryIngredientsAutocompleteAdapter(this@CategoryPicker, ingredients)
                autoCompleteTextView!!.setAdapter(adapter)
            }
        }.execute()
    }

    /**
     * This will perform the initial load of the ingredients list, specifically the categories of ingredient,
     * by calling the getAllCategories operation from the App Data Repo as an async task, and return the categories.
     * Once this task is complete, the contents of the categoryViewAdapter will be updated.
     */
    @SuppressLint("StaticFieldLeak")
    fun loadIngredients() {

        object : AsyncTask<Void, Void, List<Ingredient>>() {
            override fun doInBackground(vararg voids: Void): List<Ingredient> {
                return repository!!.allCategories
            }

            override fun onPostExecute(categories: List<Ingredient>) {
                super.onPostExecute(categories)
                categoryViewAdapter!!.updateCategories(categories)
            }
        }.execute()

    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being ingredients_list.
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on row clicked
     * Every time a row is clicked, via intent the name of the category will selected will be passed to the IngredientPicker class, and then that activity will be loaded.
     *
     *
     * 4. Set up the adapter for the recycler view as categoryViewAdapter
     */
    private fun initRecyclerItems() {
        val ingredientsCategoryList = findViewById<RecyclerView>(R.id.ingredients_list)
        ingredientsCategoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        categoryViewAdapter = CategoryViewAdapter(ArrayList(), CategoryViewAdapter.rowClickedListener { category ->
            val intent = Intent(this@CategoryPicker, IngredientPicker::class.java)
            intent.putExtra("CATEGORY", category)
            startActivity(intent)
        })

        ingredientsCategoryList.adapter = categoryViewAdapter
    }

    /**
     * This is an OnClick method that is called when the "Check My Ingredients" button is clicked in the activity. It will load the MyIngredients class, and then start that activity.
     */
    fun myIngredients(view: View) {
        val intent = Intent(this@CategoryPicker, MyIngredients::class.java)
        startActivity(intent)
    }
}
