package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.CategoryIngredientsAutocompleteAdapter;
import com.stevecrossin.whatcanicook.adapter.CategoryViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryPicker is the class which is run when the user clicks on a dish type. It handles the selection of ingredient categories and navigation to "my ingredients"
 * as well as navigation to the ingredients picker class to select ingredients
 */
public class CategoryPicker extends AppCompatActivity {
    private AppDataRepo repository;
    private CategoryViewAdapter categoryViewAdapter;
    private AutoCompleteTextView autoCompleteTextView;

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_categoriespicker XML and load the UI elements in that XML file into that view.
     * Get the string passed from the previous activity (MainActivity) as an intent, and then set the dishchosentext textview to the contents of that string
     * <p>
     * Initialise an instance of the AppDataRepo
     * Call the loadIngredients, initRecyclerItems and setupAutoComplete methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoriespicker);

        Intent intent = getIntent();

        String dishtype = intent.getStringExtra("DISH_TYPE");
        TextView textView = findViewById(R.id.dishchosentext);
        textView.setText(dishtype);

        repository = new AppDataRepo(this);

        loadIngredients();
        initRecyclerItems();
        setupAutoComplete();
    }

    /**
     * Method to setup the autoCompleteAdapter. Finds the textView by ID and sets up an onClick listener, and gets the items position in the adapter that
     * has been selected. Once it has been selected, call the  then calls the markIngredientAsSelected, then finally call the getAllIngredients method
     */
    private void setupAutoComplete() {
        autoCompleteTextView = findViewById(R.id.recipe_name);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.setText(null);
                Ingredient ingredient = (Ingredient) parent.getItemAtPosition(position);
                markIngredientAsSelected(ingredient.getIngredientName());
            }
        });
        getAllIngredients();
    }

    /**
     * Method to call to mark ingredients as selected. This is called from the setupAutoComplete onClick method, which when called runs an async task
     * to run in background the database operation to select the ingredient from the database
     */
    @SuppressLint("StaticFieldLeak")
    public void markIngredientAsSelected(final String ingredientName) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                repository.selectIngredient(ingredientName);
                return null;
            }

        }.execute();
    }

    /**
     * Get all ingredients from the AppRepo database via an async task and create a new instance of the AutoComplete adapter,
     * and populate the adapter with the contents of the database
     */
    @SuppressLint("StaticFieldLeak")
    public void getAllIngredients() {
        new AsyncTask<Void, Void, List<Ingredient>>() {

            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(CategoryPicker.this);
                return repo.getAllIngredients();
            }

            @Override
            protected void onPostExecute(List<Ingredient> ingredients) {
                super.onPostExecute(ingredients);
                CategoryIngredientsAutocompleteAdapter adapter = new CategoryIngredientsAutocompleteAdapter(CategoryPicker.this, ingredients);
                autoCompleteTextView.setAdapter(adapter);
            }
        }.execute();
    }

    /**
     * This will perform the initial load of the ingredients list, specifically the categories of ingredient,
     * by calling the getAllCategories operation from the App Data Repo as an async task, and return the categories.
     * Once this task is complete, the contents of the categoryViewAdapter will be updated.
     */
    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {

        new AsyncTask<Void, Void, List<Ingredient>>() {
            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                return repository.getAllCategories();
            }

            @Override
            protected void onPostExecute(List<Ingredient> categories) {
                super.onPostExecute(categories);
                categoryViewAdapter.updateCategories(categories);
            }
        }.execute();

    }

    /**
     * Performs the setup for the recyclerView. The method will:
     * 1. Find the recyclerView in the layout, with the ID being ingredients_list.
     * 2. Set the layout manager as a LinerarLayout manager with elements in vertical order
     * 3. Set up onClick listener for recycleview on row clicked
     * Every time a row is clicked, via intent the name of the category will selected will be passed to the IngredientPicker class, and then that activity will be loaded.
     * <p>
     * 4. Set up the adapter for the recycler view as categoryViewAdapter
     */
    private void initRecyclerItems() {
        RecyclerView ingredientsCategoryList = findViewById(R.id.ingredients_list);
        ingredientsCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryViewAdapter = new CategoryViewAdapter(new ArrayList<Ingredient>(), new CategoryViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(String category) {
                Intent intent = new Intent(CategoryPicker.this, IngredientPicker.class);
                intent.putExtra("CATEGORY", category);
                startActivity(intent);
            }
        });

        ingredientsCategoryList.setAdapter(categoryViewAdapter);
    }

    /**
     * This is an OnClick method that is called when the "Check My Ingredients" button is clicked in the activity. It will load the MyIngredients class, and then start that activity.
     */
    public void myIngredients(View view) {
        Intent intent = new Intent(CategoryPicker.this, MyIngredients.class);
        startActivity(intent);
    }
}
