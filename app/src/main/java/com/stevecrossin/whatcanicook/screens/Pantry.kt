package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.adapter.PantryAutocompleteAdapter
import com.stevecrossin.whatcanicook.adapter.PantryRecycleViewAdapter
import com.stevecrossin.whatcanicook.entities.Ingredient
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import java.util.*

/**
 * Pantry is the class which is run when the user clicks on "My Pantry" from the MainActivity. It will show the user the ingredients they
 * have saved as being in their pantry, allow them to add, and remove additional ingredients to their pantry. It is mainly designed to act like a shopping list
 * so the user knows what they have at home when they are shopping.
 */
class Pantry : AppCompatActivity() {

    private var autoCompleteTextView: AutoCompleteTextView? = null
    private var recyclerView: RecyclerView? = null

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_pantry XML and load the UI elements in that XML file into that view.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     * Call initView, getPantryIngredient and loadPantry methods.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.loadAd(adRequest)
        initView()
        getPantryIngredient()
        loadPantry()
    }


    /**
     * This is method is responsible for setting the OnClick listener for the Pantry autocompleteTextView.
     * The OnClick listener has multiple elements, to handle clearing users ingredients from the pantry and deleting individual user ingredients,
     * depending on whether the end user clicks the "Clear Ingredients" button or an individual ingredient row.
     *
     *
     *
     * It also initialises the view by finding the elements that should be displayed in the view, specifically the autocomplete textview and the recycler view, also sets the adapter for the
     * recycler view
     *
     *
     * When an onClick method is triggered, an async task is called in the background which creates a new instance of AppDataRepo
     * and calls the methods to clear the ingredient from the pantry database. Once this is completed the RecyclerView will be updated.
     */
    private fun initView() {
        autoCompleteTextView = findViewById(R.id.pantryIngredientsSearchBox)
        autoCompleteTextView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            autoCompleteTextView!!.text = null
            val ingredient = parent.getItemAtPosition(position) as Ingredient
            addPantryIngredientToDB(ingredient)
        }

        recyclerView = findViewById(R.id.pantryRecycler)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = PantryRecycleViewAdapter(ArrayList())

        val clearAll = findViewById<Button>(R.id.pantryClearIngredients)
        clearAll.setOnClickListener {
            object : AsyncTask<Void, Void, Void>() {

                override fun doInBackground(vararg voids: Void): Void? {
                    val repo = AppDataRepo(this@Pantry)
                    repo.clearUserIngredientsPantry()
                    repo.deleteIngredientsPantry()
                    return null
                }

                override fun onPostExecute(aVoid: Void) {
                    super.onPostExecute(aVoid)
                    recyclerView!!.adapter = PantryRecycleViewAdapter(ArrayList())
                }
            }.execute()
        }
    }

    /**
     * This is method is responsible for getting a list of all ingredients that are to be selectable in the Pantry
     * autocomplete textbox. It calls an Async task which will run the getAllTolerant ingredients from the appdata repo -
     * ingredients that are in the database that the user hasn't noted an intolerance to, and return these to a new instance of the
     * Pantry AutoComplete adapter.
     */
    @SuppressLint("StaticFieldLeak")
    fun getPantryIngredient() {
        val repository = AppDataRepo(this)

        object : AsyncTask<Void, Void, List<Ingredient>>() {
            override fun doInBackground(vararg voids: Void): List<Ingredient> {
                val categories = repository.allTolerantIngredients
                categories.addAll(repository.allCategories)
                return categories
            }

            override fun onPostExecute(categories: List<Ingredient>) {
                super.onPostExecute(categories)
                val adapter = PantryAutocompleteAdapter(this@Pantry, categories)
                autoCompleteTextView!!.setAdapter(adapter)
            }
        }.execute()
    }


    /**
     * This method handles the adding of ingredients that were added into the pantry, into the pantry database.
     * This is performed as an Async task, which will call the AppRepo method to add the ingredient to the pantry.
     *
     *
     * It is possible for the end user to attempt to add an ingredient to the pantry twice - this causes an
     * SQLLiteConstraint exception which has been caught. A toast will also be shown to the user advising them that the ingredient already exists in the pantry.
     *
     *
     * After this background task is completed, the recyclerView will be updated with the new list of ingredients that exist in the pantry.
     */
    @SuppressLint("StaticFieldLeak")
    fun addPantryIngredientToDB(ingredient: Ingredient) {
        object : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String? {
                val repo = AppDataRepo(this@Pantry)
                try {
                    repo.addIngredientToPantry(ingredient)
                } catch (e: SQLiteConstraintException) {
                    repo.insertLogs("Attempted to add an ingredient in the pantry that already existed")
                    return "Ingredient already exists in pantry"
                } finally {
                    repo.savePantryToUserDB()
                }
                return null
            }

            override fun onPostExecute(message: String) {
                super.onPostExecute(message)
                if (!TextUtils.isEmpty(message)) {
                    Toast.makeText(this@Pantry, message, Toast.LENGTH_SHORT).show()
                } else {
                    (Objects.requireNonNull<Adapter>(recyclerView!!.adapter) as PantryRecycleViewAdapter).updateCategories(ingredient)
                }
            }
        }.execute()
    }

    /**
     * This method will load the contents of the pantry database. It is called when the user clicks the "My Pantry" button.
     * It will:
     * 1. Perform a query of the pantry database via an async task referenced in the AppDataRepo to get all records in the database, and return these values
     * 2. Pass that data to the pantry scene, and update the recyclerview with the items in the database
     */
    @SuppressLint("StaticFieldLeak")
    fun loadPantry() {
        object : AsyncTask<Void, Void, List<Ingredient>>() {

            override fun doInBackground(vararg voids: Void): List<Ingredient> {
                val repo = AppDataRepo(this@Pantry)
                return repo.allPantryIngredients
            }

            override fun onPostExecute(ingredients: List<Ingredient>) {
                super.onPostExecute(ingredients)
                (Objects.requireNonNull<Adapter>(recyclerView!!.adapter) as PantryRecycleViewAdapter).updateCategories(ingredients)
            }
        }.execute()
    }
}