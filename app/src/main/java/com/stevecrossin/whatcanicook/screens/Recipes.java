package com.stevecrossin.whatcanicook.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.MyIngredientViewAdapter;
import com.stevecrossin.whatcanicook.adapter.RecipeViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.entities.RecipeIngredients;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

//This class handles all the recipes functions for this application, including reading the recipes and providing recipe results
public class Recipes extends AppCompatActivity {
    String recipename;
    String recipeingredients;//ingredients
    String recipesteps;
    private AppDataRepo repository;
    RecipeViewAdapter recipeViewAdapter;
    private static final String TAG = "Recipes";
    ArrayList<Recipe> recipesFromCsv = new ArrayList<>();
    ArrayList<RecipeIngredients> recipeIngredientsFromCsv = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);

        repository = new AppDataRepo(this);
        loadRecipesFromCsv();
        loadRecipesToDb();
        loadRecipeIngredientsToDb();
        initRecyclerItems();
    }

    private void initRecyclerItems() {
        RecyclerView recipesList = findViewById(R.id.recipes_list);
        recipesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        recipeViewAdapter = new RecipeViewAdapter(new ArrayList<Recipe>(), new RecipeViewAdapter.rowClickedListener() {
            @Override
            public void onRowClicked(Recipe recipe) {
                Log.d(TAG, "onRowClicked: " + recipe.getRecipeName() + " " + recipe.getRecipeIngredients());
            }

        });
        recipesList.setAdapter(recipeViewAdapter);
        loadRecipes();
    }

    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
    public void loadRecipes() {
        /*
        This method will need to
        1. Load all recipes from recipes.csv stored in permanent storage
        2. Update the contents of the recipes room database with any new entries in the csv
       */
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                recipes.addAll(repository.getAllRecipes());
                for (Recipe recipe : recipes) {
                    Log.d(TAG, "Recipe name: " + recipe.getRecipeName());
                    Log.d(TAG, "Recipe ingredients : " + recipe.getRecipeIngredients());
                }
                return recipes;
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> recipes) {
                super.onPostExecute(recipes);
                recipeViewAdapter.updateRecipes(recipes);
            }
        }.execute();
    }

    private void loadRecipesFromCsv() {
        try {
            Reader in = new InputStreamReader(getResources().openRawResource(R.raw.recipes));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String recipeName = record.get(0);
                String ingredientLists = record.get(1);
                String ingredientString = record.get(2);
                String recipeSteps = record.get(3);
                String[] ingredients = ingredientLists.split(":");

                for (String ingredient : ingredients) {
                    RecipeIngredients recipeIngredients = new RecipeIngredients(recipeName, ingredient);
                    recipeIngredientsFromCsv.add(recipeIngredients);
                }
                Recipe recipe = new Recipe(recipeName, ingredientString, recipeSteps);
                recipesFromCsv.add(recipe);
            }
        } catch (FileNotFoundException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void loadRecipesToDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!repository.haveRecipe()) {
                    repository.insertRecipes(recipesFromCsv);
                }
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void loadRecipeIngredientsToDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!repository.haveRecipeIngredients()) {
                    repository.insertRecipeIngredients(recipeIngredientsFromCsv);
                }
                return null;
            }
        }.execute();

    }

    //Perform queries with ingredients data to find recipe results
    //public void findRecipes() {
       /*
        This method will perform the following actions to check for recipe results
        1. Take the parcel passed by the CategoryPicker.searchRecipe class
        2. Query the ingredients provided against the recipes database, and check each row's ingredients column.
        Data in the ingredients column is stored in this format, separated by semicolons.
        "(chicken, 200, grams); (oil, 50, ml)"

        The code will parse the ingredients names in that row and compare them to the selected ingredients passed.

        If all ingredients are available, the recipe will be flagged as a valid ingredient, and will be passed to the next
        function for display once query complete.

        If some ingredients are available, the alternative ingredient for missing ingredients will be selected.
        If the alternative ingredient matches the recipe ingredient, this recipe will be noted as possible to make
        using an alternative and passed to the next function to display once query complete

        If, even using alternative ingredients, the recipe cannot be reproduced, the recipe will be skipped and the
        query will move onto the next row.

        This method will continue until all rows in the table have been queried
       */
    //}

    //Display recipe results
    public void displayRecipes() {
       /*
       This method will handle outputting recipe results to display to the user.
       Once the findRecipes method is complete, recipe results will be sorted and displayed, in order of
       most number of ingredients used. Recipes that can be used using ingredients the user has, where the recipe
       calls for a different ingredient, will be returned in the same priority, by most primary
       ingredients used.

       The recyclerview of the reciperesults will be updated, and output will be need to be presented in the following format:

       Recipe Name, CategoryPicker user has e.g. "Roast chicken with potatoes, uses your chicken, potatoes and olive oil"
       If a substituite is recommended, it will instead state
       "Recipe Name, CategoryPicker they have, "You can substituite your X instead of Y"
       e.g. "Roast chicken with potatoes, uses your chicken and potatoes. You can use your canola oil instead of olive oil"
       */
    }

    //Display details of recipe when the recipe row is clicked
    public void recipeInfo() {
        /*
        When the recipe row is clicked, a query will be run on the recipes database to extract the
        recipe name, recipe ingredients including quantities and recipe steps, for that selected recipe.
        This data will then be passed to the Recipe Details scene and displayed in a scrollable text view
        */
    }


    /*
        This method is executed when the "Start Over" button is clicked on the "Recipe Details" activity.
        As the selected ingredients need to be reset to defaults, this button will perform a database update on
        the ingredients database table to set all ingredientselected fields back to false, which will have the effect
        of clearing the "My CategoryPicker" list. It will then navigate back to the MainActivity.
    */
    public void resetIngredients(View view) {
        new AppDataRepo(this).clearIngredients();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

