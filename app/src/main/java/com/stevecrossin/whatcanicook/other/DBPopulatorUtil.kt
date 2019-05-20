package com.stevecrossin.whatcanicook.other

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.*
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import org.apache.commons.csv.CSVFormat
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * Database populator class. It handles the population of application databases on load of the application.
 */
class DBPopulatorUtil {

    /**
     * Perform an async task in the background to load intolerances into the DB
     * It will first construct an array list of Intolerance from the csv files
     * Then insert those into the database if there is no data.
     */
    @SuppressLint("StaticFieldLeak")
    internal fun loadIntolerancesToDb(context: Context) {
        val repository = AppDataRepo(context)
        if (!repository.haveIntolerance()) {
            val dbPopulatorUtil = DBPopulatorUtil()
            val intolerances = dbPopulatorUtil.loadIntolerancesFromCsv(context)
            if (intolerances != null && !intolerances.isEmpty()) {
                for (intolerance in intolerances)
                    repository.insertIntolerance(intolerance)
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
    private fun loadIntolerancesFromCsv(context: Context): ArrayList<Intolerance>? {
        val repo = AppDataRepo(this@DBPopulatorUtil)

        try {
            val intolerances = ArrayList<Intolerance>()
            val `in` = InputStreamReader(context.resources.openRawResource(R.raw.intolerances))
            val records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(`in`)
            for (record in records) {
                val intoleranceName = record.get(1)
                val intoleranceIngredients = record.get(2)
                val ingredients = intoleranceIngredients.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                for (ingredient in ingredients) {
                    val intolerance = Intolerance(intoleranceName, ingredient)
                    intolerances.add(intolerance)
                }
            }
            return intolerances
        } catch (ex: FileNotFoundException) {
            repo.insertLogs("Tried to load from a CSV file that doesn't exist")
            Toast.makeText(context, "Loading from CSV file failed as it doesn't exist", Toast.LENGTH_SHORT).show()
        } catch (ex: IOException) {
            repo.insertLogs("IO error with file")
            Toast.makeText(context, "Input/output error with CSV file", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            repo.insertLogs("Parsing error with CSV file")
            Toast.makeText(context, "Error parsing the CSV file into the database", Toast.LENGTH_SHORT).show()
        }

        return null
    }

    /**
     * This method will be performed in the background on app load and handles loading of the ingredients from CSV into the database.
     *
     *
     * It will
     * 1. Read all information from ingredients.csv, a read only document stored in permanent storage
     * 2. Append the ingredients room database with any new ingredients/delete any ingredients that are no longer present in the csv
     * 3. Perform query on intolerances database to determine which intolerances are currently active
     * 4. Update ingredientselectable column in ingredients database to false when ingredient matches exclusion criteria
     * 5. Query database for ingredients that are not excluded, and update recyclerview in ingredient chooser activity with this list.
     * If any errors exist with the permanent CSV files, an exception is caught, logged and the end user is notified via a toast.
     */

    internal fun loadIngredientsTODb(context: Context) {

        val repository = AppDataRepo(context)
        if (!repository.haveIngredient()) {
            val ingredients = loadIngredientsFromCsv(context)
            if (ingredients != null && !ingredients.isEmpty())
                repository.insertIngredients(ingredients)
        }
    }

    private fun loadIngredientsFromCsv(context: Context): ArrayList<Ingredient>? {
        val repo = AppDataRepo(this@DBPopulatorUtil)
        try {
            val ingredients = ArrayList<Ingredient>()
            val `in` = InputStreamReader(context.resources.openRawResource(R.raw.ingredients))
            val records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(`in`)
            for (record in records) {
                val ingredientID = record.get(0)
                val ingredientCategory = record.get(1)
                val categoryIconName = record.get(2)
                val ingredientSubCat = record.get(3)
                val ingredientName = record.get(4)
                val ingredientAlternative = record.get(5)
                val ingredient = Ingredient(Integer.parseInt(ingredientID), ingredientCategory, categoryIconName, ingredientSubCat, ingredientName, ingredientAlternative)
                ingredients.add(ingredient)
            }
            return ingredients
        } catch (ex: FileNotFoundException) {
            repo.insertLogs("Tried to load from a CSV file that doesn't exist")
            Toast.makeText(context, "Loading from CSV file failed as it doesn't exist", Toast.LENGTH_SHORT).show()
        } catch (ex: IOException) {
            repo.insertLogs("IO error with file")
            Toast.makeText(context, "Input/output error with CSV file", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            repo.insertLogs("Parsing error with CSV file")
            Toast.makeText(context, "Error parsing the CSV file into the database", Toast.LENGTH_SHORT).show()
        }

        return null
    }


    /**
     * This method will parse data in the CSV files into 2 ArrayList: recipesFromCSV and recipeIngredientsFromCsv
     * A split function also exists to parse out the recipe steps.
     * If any errors exist with the permanent CSV files, an exception is caught, logged and the end user is notified via a toast.
     */
    internal fun loadRecipesFromCsvToDB(context: Context) {
        val repo = AppDataRepo(this@DBPopulatorUtil)
        try {
            val `in` = InputStreamReader(context.resources.openRawResource(R.raw.recipes))
            val records = CSVFormat.EXCEL.withHeader().withDelimiter(',').parse(`in`)
            val recipesFromCsv = ArrayList<Recipe>()
            val recipeIngredientsFromCsv = ArrayList<RecipeIngredients>()
            val recipeIngredientsTotalsFromCsv = ArrayList<RecipeIngredientsTotal>()

            for (record in records) {
                val recipeName = record.get(0)
                val recipeImage = record.get(1)
                val ingredientLists = record.get(2)
                val ingredientString = record.get(3)
                val recipeSteps = record.get(4)
                val ingredients = ingredientLists.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                for (ingredient in ingredients) {
                    val recipeIngredients = RecipeIngredients(recipeName, recipeImage, ingredient)
                    recipeIngredientsFromCsv.add(recipeIngredients)
                }
                val recipeIngredientsTotal = RecipeIngredientsTotal(recipeName, ingredients.size)
                recipeIngredientsTotalsFromCsv.add(recipeIngredientsTotal)
                val recipe = Recipe(recipeName, recipeImage, ingredientString, recipeSteps)
                recipesFromCsv.add(recipe)
            }

            loadRecipesToDb(context, recipesFromCsv)
            loadRecipeIngredientsToDb(context, recipeIngredientsFromCsv)
            loadRecipeIngredientsTotalToDb(context, recipeIngredientsTotalsFromCsv)
        } catch (ex: FileNotFoundException) {
            repo.insertLogs("Tried to load from a CSV file that doesn't exist")
            Toast.makeText(context, "Loading from CSV file failed as it doesn't exist", Toast.LENGTH_SHORT).show()
        } catch (ex: IOException) {
            repo.insertLogs("IO error with file")
            Toast.makeText(context, "Input/output error with CSV file", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            repo.insertLogs("Parsing error with CSV file")
            Toast.makeText(context, "Error parsing the CSV file into the database", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Loads recipes to the DB if there is no data
     */
    private fun loadRecipesToDb(context: Context, recipesFromCsv: ArrayList<Recipe>) {
        val repository = AppDataRepo(context)
        if (!repository.haveRecipe()) {
            repository.insertRecipes(recipesFromCsv)
        }
    }

    /**
     * Loads recipes + ingredients to the DB if there is no data
     */
    private fun loadRecipeIngredientsToDb(context: Context, recipeIngredientsFromCsv: ArrayList<RecipeIngredients>) {
        val repository = AppDataRepo(context)
        if (!repository.haveRecipeIngredients())
            repository.insertRecipeIngredients(recipeIngredientsFromCsv)

    }

    /**
     * Loads recipes + ingredients to the DB if there is no data
     */

    private fun loadRecipeIngredientsTotalToDb(context: Context, recipeIngredientsTotalsFromCsv: ArrayList<RecipeIngredientsTotal>) {
        val repository = AppDataRepo(context)
        if (!repository.haveRecipeIngredientsTotal()) {
            repository.insertRecipeIngredientsTotal(recipeIngredientsTotalsFromCsv)
        }
    }

}
