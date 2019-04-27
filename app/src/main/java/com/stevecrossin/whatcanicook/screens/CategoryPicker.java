package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.List;

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

        String dishtype = intent.getStringExtra("DISH_TYPE");
        TextView textView = findViewById(R.id.dishchosentext);
        textView.setText(dishtype);

        repository = new AppDataRepo(this);

        initRecyclerItems();
    }


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

        new AsyncTask<Void, Void, List<Ingredient>>() {
            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                if (!repository.haveIngredient()) {
                    repository.insertIngredients(ingredients);
                }
                ArrayList<Ingredient> categories = new ArrayList<>();
                categories.addAll(repository.getAllCategories());
                return categories;
            }

            @Override
            protected void onPostExecute(List<Ingredient> categories) {
                super.onPostExecute(categories);
                categoryViewAdapter.updateCategories(categories);
            }
        }.execute();
    }


    /**
     * This will perform the initial load of the ingredients from the ingredients.csv file
     * For each of the record in the file, split the line by ',' delimiter
     * Then construct a new Ingredient object with id, category, sub-category, name and alternative
     * Finally return a list of ingredient
     */
    private ArrayList<Ingredient> loadIngredientsFromCsv() {
        try {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            Reader in = new InputStreamReader(getResources().openRawResource(R.raw.ingredients));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String ingredientID = record.get(0);
                String ingredientCategory = record.get(1);
                String ingredientImage = record.get(2);
                String ingredientSubCat = record.get(3);
                String ingredientName = record.get(4);
                String ingredientAlternative = record.get(5);

                Ingredient ingredient = new Ingredient(Integer.parseInt(ingredientID), ingredientCategory, ingredientImage, ingredientSubCat, ingredientName, ingredientAlternative);
                ingredients.add(ingredient);
            }
            return ingredients;
        } catch (FileNotFoundException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
        return null;
    }

    /**
     * This will do the setup step for our recycle view:
     * 1. find the recycle view in the layout
     * 2. set the layout manager
     * 3. set up event listener for recycleview on row clicked
     * 4. set adapter for the recycle view
     * 5. finall, call loadingredients method to populate data
     */
    private void initRecyclerItems() {
        RecyclerView ingredientsCategoryList = findViewById(R.id.ingredients_list);
        ingredientsCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        categoryViewAdapter = new CategoryViewAdapter(new ArrayList<Ingredient>(), new CategoryViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(String category) {
                Log.d(TAG, "Row is clicked");
                if (category != null) {
                    Log.d(TAG, "Category: " + category);
                } else
                    Log.d(TAG, "Category is null");

                Intent intent = new Intent(CategoryPicker.this, IngredientPicker.class);
                intent.putExtra("CATEGORY", category);
                startActivity(intent);
            }
        });

        ingredientsCategoryList.setAdapter(categoryViewAdapter);

        loadIngredients();
    }

    public void myIngredients(View view) {
        Intent intent = new Intent(CategoryPicker.this, MyIngredients.class);
        startActivity(intent);
    }
}
