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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.adapter.MyIngredientViewAdapter;
import com.stevecrossin.whatcanicook.adapter.RecipeViewAdapter;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.entities.RecipeIngredients;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsTotal;
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
    private AppDataRepo repository;
    RecipeViewAdapter recipeViewAdapter;
    private static final String TAG = "Recipes";
    ArrayList<Recipe> recipesFromCsv = new ArrayList<>();
    ArrayList<RecipeIngredients> recipeIngredientsFromCsv = new ArrayList<>();
    ArrayList<RecipeIngredientsTotal> recipeIngredientsTotalsFromCsv = new ArrayList<>();
    Switch exactMatch;
    LinearLayout addingList;

    /**
     * Scene initalization. This also loads the neccessary ingredient from the CSV to the database
     *
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);
        exactMatch = findViewById(R.id.exactMatch_switch);
        exactMatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    //update exact match
                    loadRecipesExactMatch();
                else
                    //normal search
                    loadRecipes();
            }
        });
        addingList = findViewById(R.id.adding_list);
        repository = new AppDataRepo(this);
        loadRecipesFromCsv();
        loadRecipesToDb();
        loadRecipeIngredientsToDb();
        loadRecipeIngredientsTotalToDb();
        initRecyclerItems();
        initSuggestions();
    }

    @SuppressLint("StaticFieldLeak")
    private void initSuggestions() {
        addingList.removeAllViews();
        new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                ArrayList<Recipe> similarRecipes = new ArrayList<>();
                similarRecipes.addAll(repository.getAllRecipesByCheckedIngredients(1));
                Log.d(TAG, "doInBackground: " + similarRecipes.get(0).getRecipeName());
                if (similarRecipes.size() > 0) {
                    Recipe similarRecipe = similarRecipes.get(0);
                    ArrayList<String> missingIngredients = new ArrayList<>();
                    missingIngredients.addAll(repository.getMissingIngredientsByName(similarRecipe.getRecipeName(), 3));
                    Log.d(TAG, "doInBackground: " + missingIngredients.get(0) + missingIngredients.get(1) + missingIngredients.get(2));
                    return missingIngredients;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> missingIngredients) {
                super.onPostExecute(missingIngredients);
                for (String string : missingIngredients) {
                    final Button ingredient = new Button(Recipes.this);
                    ingredient.setText(string);
                    ingredient.setTag(string);
                    ingredient.setMaxWidth(10);
                    ingredient.setTextSize(10);
                    ingredient.setOnClickListener(btnClicked);
                    addingList.addView(ingredient);
                }
            }
        }.execute();
    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onClick(View v) {
            final Button ingredient = (Button) v;
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    repository.selectIngredient(ingredient.getText().toString());
                    return null;
                }
            }.execute();
            initRecyclerItems();
            initSuggestions();
        }
    };

    /**
     * This will do the setup step for our recycle view:
     * 1. find the recycle view in the layout with id recipes_list
     * 2. set the layout manager
     * 3. set up event listener for recycleview on row clicked
     * 4. set adapter for the recycle view
     * 5. finall, call loadRecipes() method to populate data
     */
    private void initRecyclerItems() {
        RecyclerView recipesList = findViewById(R.id.recipes_list);
        recipesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ingredientsList.setHasFixedSize(false);
        recipeViewAdapter = new RecipeViewAdapter(new ArrayList<Recipe>(), new RecipeViewAdapter.rowClickedListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onRowClicked(final Recipe recipe) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        ArrayList<String> missingIngredients = new ArrayList<>();
                        missingIngredients.addAll(repository.getMissingIngredientsByName(recipe.getRecipeName(), 0));
                        //for (String string : missingIngredients)
                        // Log.d(TAG, "Missing ingredients: " + string + "\n");
                        Intent intent = new Intent(Recipes.this, RecipesDetails.class);
                        intent.putExtra("RECIPE", recipe);
                        intent.putExtra("MISSING", missingIngredients);
                        startActivity(intent);
                        return null;
                    }
                }.execute();
            }

        });
        recipesList.setAdapter(recipeViewAdapter);
        loadRecipes();
    }

    /**
     * Testing method. Trying to load all recipes into the recycleview to simulate recipe search
     * This in the future will be replaced with findRecipes and displayRecipes below
     */
    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
    public void loadRecipes() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                recipes.addAll(repository.getAllRecipesByCheckedIngredients(0));
                for (Recipe recipe : recipes) {
                    Log.d(TAG, "Recipe name: " + recipe.getRecipeName());
                    Log.d(TAG, "Recipe ingredients: " + recipe.getRecipeIngredients());
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

    @SuppressLint("StaticFieldLeak")
    //Load recipes into memory from csv file, apply filters.
    public void loadRecipesExactMatch() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                recipes.addAll(repository.getAllRecipesByCheckedIngredientsWithExactMatch());
                for (Recipe recipe : recipes) {
                    Log.d(TAG, "Recipe name: " + recipe.getRecipeName());
                    Log.d(TAG, "Recipe ingredients: " + recipe.getRecipeIngredients());
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

    /**
     * This method will parse data in the CSV files into 2 ArrayList: recipesFromCSV and recipeIngredientsFromCsv
     * Note*: The recipes raw data structure is more special than others
     */
    private void loadRecipesFromCsv() {
        try {
            Reader in = new InputStreamReader(getResources().openRawResource(R.raw.recipes));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String recipeName = record.get(0);
                String recipeImage = record.get(1);
                String ingredientLists = record.get(2);
                String ingredientString = record.get(3);
                String recipeSteps = record.get(4);
                String[] ingredients = ingredientLists.split(":");

                for (String ingredient : ingredients) {
                    RecipeIngredients recipeIngredients = new RecipeIngredients(recipeName, recipeImage, ingredient);
                    recipeIngredientsFromCsv.add(recipeIngredients);
                }
                RecipeIngredientsTotal recipeIngredientsTotal = new RecipeIngredientsTotal(recipeName, ingredients.length);
                recipeIngredientsTotalsFromCsv.add(recipeIngredientsTotal);
                Recipe recipe = new Recipe(recipeName, recipeImage, ingredientString, recipeSteps);
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

    /**
     * Loads recipes to the DB if there is no data
     */
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

    /**
     * Loads recipes + ingredients to the DB if there is no data
     */
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

    /**
     * Loads recipes + ingredients to the DB if there is no data
     */
    @SuppressLint("StaticFieldLeak")
    public void loadRecipeIngredientsTotalToDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!repository.haveRecipeIngredientsTotal()) {
                    repository.insertRecipeIngredientsTotal(recipeIngredientsTotalsFromCsv);
                }
                return null;
            }
        }.execute();

    }

    public void resetIngredients(View view) {
        new AppDataRepo(this).clearIngredients();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

