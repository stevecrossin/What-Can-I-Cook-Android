package com.stevecrossin.whatcanicook.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.stevecrossin.whatcanicook.R;
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

public class DBPopulatorUtil {


    private static final String TAG = "DBPopulatorUtil";

    /**
     * Perform an async task in the background to load intolerances into the DB
     * It will first construct an array list of Intolerance from the csv files
     * Then insert those into the database if there is no data.
     */
    @SuppressLint("StaticFieldLeak")
    void loadIntolerancesToDb(Context context) {
        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveIntolerance()) {
            DBPopulatorUtil dbPopulatorUtil = new DBPopulatorUtil();
            ArrayList<Intolerance> intolerances = dbPopulatorUtil.loadIntolerancesFromCsv(context);
            if (intolerances != null && !intolerances.isEmpty()) {
                for (Intolerance intolerance : intolerances)
                    repository.insertIntolerance(intolerance);
            }
        }
    }

    /**
     * This method will handle the loading of the intolerances list. It will perform the following steps.
     * 1. Load all possible intolerances from intolerances.csv file, and use that data to update the Intolerance room database with any new entries
     * 2. Query the savedintolerances column i the Users database for the current user. It will parse out multiple intolerances that are inside quotes and brackets, with the comma between each intolerance separating them.
     * 3. It will then mark the relevant intolerance as active in the intolerance database, ands also update the UI of the activity to mark the selected intolerances as active.
     * If any errors exist with the permanent CSV files, an exception is caught, logged and the end user is notified via a toast.
     */
    private ArrayList<Intolerance> loadIntolerancesFromCsv(Context context) {
        AppDataRepo repo = new AppDataRepo(DBPopulatorUtil.this);

        try {
            ArrayList<Intolerance> intolerances = new ArrayList<>();
            Reader in = new InputStreamReader(context.getResources().openRawResource(R.raw.intolerances));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String intoleranceName = record.get(1);
                String intoleranceIngredients = record.get(2);
                String[] ingredients = intoleranceIngredients.split(":");

                for (String ingredient : ingredients) {
                    Intolerance intolerance = new Intolerance(intoleranceName, ingredient);
                    intolerances.add(intolerance);
                }
            }
            return intolerances;
        } catch (FileNotFoundException ex) {
            repo.insertLogs("Tried to load from a CSV file that doesn't exist");
            Toast.makeText(context, "Loading from CSV file failed as it doesn't exist", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            repo.insertLogs("IO error with file");
            Toast.makeText(context, "Input/output error with CSV file", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            repo.insertLogs("Parsing error with CSV file");
            Toast.makeText(context, "Error parsing the CSV file into the database", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * This method will be performed in the background on app load and handles loading of the ingredients from CSV into the database.
     * <p>
     * It will
     * 1. Read all information from ingredients.csv, a read only document stored in permanent storage
     * 2. Append the ingredients room database with any new ingredients/delete any ingredients that are no longer present in the csv
     * 3. Perform query on intolerances database to determine which intolerances are currently active
     * 4. Update ingredientselectable column in ingredients database to false when ingredient matches exclusion criteria
     * 5. Query database for ingredients that are not excluded, and update recyclerview in ingredient chooser activity with this list.
     * If any errors exist with the permanent CSV files, an exception is caught, logged and the end user is notified via a toast.
     */

    void loadIngredientsTODb(Context context) {

        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveIngredient()) {
            ArrayList<Ingredient> ingredients = loadIngredientsFromCsv(context);
            if (ingredients != null && !ingredients.isEmpty())
                repository.insertIngredients(ingredients);
        }
    }

    private ArrayList<Ingredient> loadIngredientsFromCsv(Context context) {
        AppDataRepo repo = new AppDataRepo(DBPopulatorUtil.this);
        try {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            Reader in = new InputStreamReader(context.getResources().openRawResource(R.raw.ingredients));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            for (CSVRecord record : records) {
                String ingredientID = record.get(0);
                String ingredientCategory = record.get(1);
                String categoryIconName = record.get(2);
                String ingredientSubCat = record.get(3);
                String ingredientName = record.get(4);
                String ingredientAlternative = record.get(5);
                Ingredient ingredient = new Ingredient(Integer.parseInt(ingredientID), ingredientCategory, categoryIconName, ingredientSubCat, ingredientName, ingredientAlternative);
                ingredients.add(ingredient);
            }
            return ingredients;
        } catch (FileNotFoundException ex) {
            repo.insertLogs("Tried to load from a CSV file that doesn't exist");
            Toast.makeText(context, "Loading from CSV file failed as it doesn't exist", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            repo.insertLogs("IO error with file");
            Toast.makeText(context, "Input/output error with CSV file", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            repo.insertLogs("Parsing error with CSV file");
            Toast.makeText(context, "Error parsing the CSV file into the database", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    /**
     * This method will parse data in the CSV files into 2 ArrayList: recipesFromCSV and recipeIngredientsFromCsv
     * A split function also exists to parse out the recipe steps.
     * If any errors exist with the permanent CSV files, an exception is caught, logged and the end user is notified via a toast.
     */
    void loadRecipesFromCsvToDB(Context context) {
        AppDataRepo repo = new AppDataRepo(DBPopulatorUtil.this);
        try {
            Reader in = new InputStreamReader(context.getResources().openRawResource(R.raw.recipes));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(in);
            ArrayList<Recipe> recipesFromCsv = new ArrayList<>();
            ArrayList<RecipeIngredients> recipeIngredientsFromCsv = new ArrayList<>();
            ArrayList<RecipeIngredientsTotal> recipeIngredientsTotalsFromCsv = new ArrayList<>();

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

            loadRecipesToDb(context, recipesFromCsv);
            loadRecipeIngredientsToDb(context, recipeIngredientsFromCsv);
            loadRecipeIngredientsTotalToDb(context, recipeIngredientsTotalsFromCsv);
        } catch (FileNotFoundException ex) {
            repo.insertLogs("Tried to load from a CSV file that doesn't exist");
            Toast.makeText(context, "Loading from CSV file failed as it doesn't exist", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            repo.insertLogs("IO error with file");
            Toast.makeText(context, "Input/output error with CSV file", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            repo.insertLogs("Parsing error with CSV file");
            Toast.makeText(context, "Error parsing the CSV file into the database", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads recipes to the DB if there is no data
     */
    private void loadRecipesToDb(Context context, ArrayList<Recipe> recipesFromCsv) {
        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveRecipe()) {
            repository.insertRecipes(recipesFromCsv);
        }
    }

    /**
     * Loads recipes + ingredients to the DB if there is no data
     */
    private void loadRecipeIngredientsToDb(Context context, ArrayList<RecipeIngredients> recipeIngredientsFromCsv) {
        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveRecipeIngredients())
            repository.insertRecipeIngredients(recipeIngredientsFromCsv);

    }

    /**
     * Loads recipes + ingredients to the DB if there is no data
     */

    private void loadRecipeIngredientsTotalToDb(Context context, ArrayList<RecipeIngredientsTotal> recipeIngredientsTotalsFromCsv) {
        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveRecipeIngredientsTotal()) {
            repository.insertRecipeIngredientsTotal(recipeIngredientsTotalsFromCsv);
        }
    }

}
