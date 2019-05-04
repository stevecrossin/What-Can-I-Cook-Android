package com.stevecrossin.whatcanicook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

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
    public void loadIntolerancesToDb(Context context) {
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

    private ArrayList<Intolerance> loadIntolerancesFromCsv(Context context) {
    /*
    This method will handle the loading of the intolerances list. It will perform the following steps.
    1. Load all possible intolerances from nintolerances.csv file, and use that data to update the Intolerance room database with any new entries
    2. Query the savedintolerances column i the Users database for the current user. It will parse out multiple intolerances that are inside quotes and brackets, with the comma between
    each intolerance separating them.
    3. It will then mark the relevant intolerance as active in the intolerance database, ands also update the UI of the activity to mark the selected intolerances as active.
    */
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
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
        return null;
    }


    public void loadIngredientsTODb(Context context) {
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

        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveIngredient()) {
            ArrayList<Ingredient> ingredients = loadIngredientsFromCsv(context);
            if (ingredients != null && !ingredients.isEmpty())
                repository.insertIngredients(ingredients);
        }
    }

    private ArrayList<Ingredient> loadIngredientsFromCsv(Context context) {
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
            Log.d(TAG, "loadIngredientsFromCsv: File not found exception" + ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, "loadIngredientsFromCsv: IO exception" + ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, "loadIngredientsFromCsv: Other exception (could be parsing)" + ex.toString());
        }
        return null;
    }


    /**
     * This method will parse data in the CSV files into 2 ArrayList: recipesFromCSV and recipeIngredientsFromCsv
     * Note*: The recipes raw data structure is more special than others
     */
    public void loadRecipesFromCsvToDB(Context context) {
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
     *
     * @param context
     * @param recipeIngredientsTotalsFromCsv
     */
    private void loadRecipeIngredientsTotalToDb(Context context, ArrayList<RecipeIngredientsTotal> recipeIngredientsTotalsFromCsv) {
        AppDataRepo repository = new AppDataRepo(context);
        if (!repository.haveRecipeIngredientsTotal()) {
            repository.insertRecipeIngredientsTotal(recipeIngredientsTotalsFromCsv);
        }
    }

}
