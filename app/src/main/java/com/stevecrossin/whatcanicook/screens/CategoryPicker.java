package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.CategoryViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * This class handles the ingredients functions of the application.
 */

public class CategoryPicker extends AppCompatActivity {
    private AppDataRepo repository;
    private CategoryViewAdapter categoryViewAdapter;
    private static final String TAG = "CategoryPicker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoriespicker);

        Intent intent = getIntent();

        String breakfast = intent.getStringExtra("DISH_TYPE");
        TextView textView = findViewById(R.id.dishchosentext);
        textView.setText(breakfast);

        repository = new AppDataRepo(this);

        initRecyclerItems();
    }


    String ingredienttype;//Type of ingredient e.g meat, veg
    String ingredientname;//Name of ingredient
    String ingredientalternative;//Possible alternative ingredients e.g. canola to sunflower oil
    Boolean ingredientselectable = true;//Whether or not the ingredient is selectable. Defaults to true.
    Boolean ingredientselected = false;//Whether or not the ingredient has been selected for recipe searching. Defaults to false.

    /**
     * This will perform the initial load of the ingredients from the ingredients.csv file and its display in the CategoryPicker activity
     */
    @SuppressLint("StaticFieldLeak")
    public void loadIngredients() {
    /*
    This method will be performed in the background once the user navigates to the CategoryPicker chooser activity from the main app landing page.
    MainActivity will pass the dish option that was clicked (e.g. breakfast, dessert) and pass this to CategoryPicker activity, which will update the
    label at the top of the activity to "What's for breakfast/dinner/dessert etc.

    It also needs to
    1. Read all information from ingredients.csv, a read only document stored in permanent storage
    2. Append the ingredients room database with any new ingredients/delete any ingredients that are no longer present in the csv
    3. Perform query on intolerances database to determine which intolerances are currently active
    4. Update ingredientselectable column in ingredients database to false when ingredient matches exclusion criteria
    5. Query database for ingredients that are not excluded, and update recyclerview in ingredient chooser activity with this list.
    */

    final ArrayList<Ingredient> ingredients = loadIngredientsFromCsv();

        new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                if (!repository.haveIngredient()) {
                    repository.insertIngredients(ingredients);
                }
                ArrayList<String> categories = new ArrayList<>();
                categories.addAll(repository.getAllCategories());
                return categories;
            }

            @Override
            protected void onPostExecute(ArrayList<String> categories) {
                super.onPostExecute(categories);
                categoryViewAdapter.updateCategories(categories);
            }
        }.execute();


    }

    //Notes when ingredients have been selected by the user, and handles searching of ingredients with the text box
    public void ingredientSelected() {
        /*
        When a user navigates the ingredients chooser, they can either click on a category in the recyclerview, or search with a search box that filters the ingredients view,
        that can then be clicked. Each ingredient has its own row in the database, and has an onClick listener to determine if clicked.
        When any row is clicked, for that particular ingredient, the
        */
    }


    //Handles the display of the selected ingredients in the My CategoryPicker activity
    public void checkIngredients() {
        /*
        When the user clicks the "Check my ingredients" the UI will navigate to the My CategoryPicker activity,
        and the recyclerview in that activity will be updated with the list of selected ingredients, where ingredientselected = true

        CategoryPicker can be removed from the list individually, and when an ingredient is removed from the My CategoryPicker Activity,
        it will perform a background task to change the ingredientselected database field to false
        */
    }

    //Passes the selected ingredients to Recipe class
    public void searchRecipe() {
        /*
        Once the user clicks the "Find Recipes" button in the My CategoryPicker activity, the following actions need to occur
        1. Query the ingredients database for all ingredients where ingredientselectable = true, and ingredientselected = true with a getter method
        This will pull a list of all currently selected valid ingredients. As a secondary piece of information, if any selected ingredients have alternatives,
        this will be collected as well.
        2. Pass that information in a parcel to the Recipes activity, which will perform the recipe results
        */

    }

    private ArrayList<Ingredient> loadIngredientsFromCsv() {
        try {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            Reader in = new InputStreamReader(getResources().openRawResource(R.raw.ingredients));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String ingredientID =  record.get(0);
                String ingredientCategory = record.get(1);
                String ingredientSubCat = record.get(2);
                String ingredientName = record.get(3);
                String ingredientAlternative = record.get(4);
                Ingredient ingredient = new Ingredient(Integer.parseInt(ingredientID), ingredientCategory, ingredientSubCat, ingredientName, ingredientAlternative);
                ingredients.add(ingredient);
            }
            return ingredients;
        } catch (FileNotFoundException ex){
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex){
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex){
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
        return  null;
    }

    private void initRecyclerItems() {
        RecyclerView ingredientsCategoryList = findViewById(R.id.ingredients_list);
        ingredientsCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        categoryViewAdapter = new CategoryViewAdapter(new ArrayList<String>(), new CategoryViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(String category) {
                Log.d(TAG, "Row is clicked");
                if (category != null){
                    Log.d(TAG, "Category: " + category);
                } else
                    Log.d(TAG, "Category is null");

                Intent intent = new Intent(CategoryPicker.this, IngredientPicker.class);
                intent.putExtra("CATEGORY", category);
                startActivity(intent);
            }
        } );

        ingredientsCategoryList.setAdapter(categoryViewAdapter);

        loadIngredients();
    }
}

