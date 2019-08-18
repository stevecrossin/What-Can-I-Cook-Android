package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.IngredientViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;

/**
 * IngredientPicker is the class which is run when the user clicks on a category in CategoryPicker, which allows the user to select ingredients they have to cook with.
 */
public class IngredientPicker extends AppCompatActivity {
    private AppDataRepo repository;
    private String category;
    IngredientViewAdapter ingredientViewAdapter;

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_ingredientsPicker XML and load the UI elements in that XML file into that view.
     * Get the string passed from the previous activity (CategoriesPicker) as an intent, and then set the categorychosentext textview to the contents of that string
     * <p>
     * Initialise an instance of the AppDataRepo
     * Call the initRecyclerItems method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientspicker);
        Intent intent = getIntent();
        category = intent.getStringExtra("CATEGORY");
        TextView textView = findViewById(R.id.categorychosentext);
        textView.setText(category);

        repository = new AppDataRepo(this);
        initRecyclerItems();
    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being ingredients_list.
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up the adapter for the recycler view as categoryViewAdapter
     * Finally, call the loadIngredients method.
     */
    private void initRecyclerItems() {
        RecyclerView ingredientsList = findViewById(R.id.ingredients_list);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ingredientViewAdapter = new IngredientViewAdapter(new ArrayList<Ingredient>() {
        });

        ingredientsList.setAdapter(ingredientViewAdapter);
        loadIngredients();
    }

    /**
     * Core function to load ingredients and update recycleview adapter.
     * This function performs an async task in the background to get a list of ingredient from the database (getByCategory)
     * An example of a returned ingredient would be "chicken"
     * It will then store those ingredients in an ArrayList and return the list.
     */
    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {
        new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
            @Override
            protected ArrayList<Ingredient> doInBackground(Void... voids) {
                return new ArrayList<>(repository.getIngredientsByCategory(category));
            }

            @Override
            protected void onPostExecute(ArrayList<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                ingredientViewAdapter.updateIngredients(ingredients);
            }
        }.execute();
    }

    /**
     * This is an OnClick method that is called when the "Check My Ingredients" button is clicked in the activity. It will load the MyIngredients class, and then start that activity.
     */
    public void myIngredients(View view) {
        Intent intent = new Intent(IngredientPicker.this, MyIngredients.class);
        startActivity(intent);
    }
}
