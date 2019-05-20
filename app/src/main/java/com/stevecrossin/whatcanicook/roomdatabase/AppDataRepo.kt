package com.stevecrossin.whatcanicook.roomdatabase

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stevecrossin.whatcanicook.entities.*
import com.stevecrossin.whatcanicook.other.DBPopulatorUtil
import java.util.*

/**
 * Data repository for application. The below contains all the data entry objects (dao) for the room databases of the app.
 */
class AppDataRepo {
    private val ingredientDao: IngredientDao
    private val intoleranceDao: IntoleranceDao
    private val userDao: UserDao
    private val recipeDao: RecipeDao
    private val recipeIngredientsDao: RecipeIngredientsDao
    private val recipeIngredientsTotalDao: RecipeIngredientsTotalDao
    private val logDao: LogDao
    private val pantryDao: PantryDao

    /**
     * Wrapper method. Perform dao operation to get all the ingredients from the db.
     */
    val allIngredients: List<Ingredient>
        get() = ingredientDao.allIngredients

    /**
     * Wrapper method. Perform dao operation to get all checked ingredients from the db.
     */
    val allCheckedIngredients: List<Ingredient>
        get() = ingredientDao.allCheckedIngredients

    /**
     * Wrapper method. Perform dao operation to get all categories from the db.
     */
    val allCategories: List<Ingredient>
        get() = ingredientDao.allCategories

    /**
     * Wrapper method. Perform dao operation to get all intolerances from the db.
     */
    val allIntolerances: List<Intolerance>
        get() = intoleranceDao.allIntolerances

    /**
     * Wrapper method. Perform dao operation to get the unique intolerance from the db.
     */
    val uniqueTolerance: List<Intolerance>
        get() = intoleranceDao.uniqueIntolerance

    /**
     * Wrapper method. Perform dao operation to get all recipes from the db.
     */
    val allRecipes: List<Recipe>
        get() = recipeDao.allDefaultRecipes

    /**
     * Wrapper method. Perform dao operation to get all RecipeIngredient from the db.
     */
    val allRecipesAndIngredients: List<RecipeIngredients>
        get() = recipeIngredientsDao.allRecipesAndIngredients

    /**
     * Wrapper method. Perform dao operation to get all RecipeIngredientsTotal from the db.
     */
    val allRecipesAndIngredientsTotal: List<RecipeIngredientsTotal>
        get() = recipeIngredientsTotalDao.allRecipesAndIngredientsTotal

    /**
     * Wrapper method. Perform dao operation to get all recipes of which users have ingredient from the db with exact match.
     *
     * @return all recipes by checked ingredients ONLY if users have ALL the ingredients from that recipe.
     */
    val allRecipesByCheckedIngredientsWithExactMatch: List<Recipe>
        get() = recipeDao.allRecipesByCheckedIngredientsWithExactMatch

    /**
     * Wrapper method. Perform dao operation to get all saved recipes from the db.
     */
    val allSavedRecipes: List<Recipe>
        get() = recipeDao.allSavedRecipes

    /**
     * Wrapper method. Perform dao operation to get sign in user from the db.
     */
    val signedUser: User
        get() = userDao.signInUser

    /**
     * Fetch all logs saved in the database through select command from logdao
     */
    val logs: List<LogRecords>
        get() = logDao.logs

    /**
     * Wrapper method. Perform dao operation to get all tolerant ingredients from the db.
     */
    val allTolerantIngredients: List<Ingredient>
        get() = ingredientDao.allTolerantIngredient

    /**
     * Wrapper method. Perform dao operation to get all pantry ingredients from the db, and place them into an array list. Returns the list of ingredients by ID
     */
    val allPantryIngredients: List<Ingredient>
        get() {
            val pantries = pantryDao.pantryIngredients
            val integers = ArrayList<Int>()

            for (pantry in pantries) {
                integers.add(pantry.ingredientId)
            }

            return ingredientDao.getIngredientsById(integers)
        }

    /**
     * Constructor of AppDataRepo
     */
    constructor(context: Context) {
        ingredientDao = AppDb.getDatabase(context).ingredientDao()
        intoleranceDao = AppDb.getDatabase(context).intoleranceDao()
        userDao = AppDb.getDatabase(context).userDao()
        recipeDao = AppDb.getDatabase(context).recipeDao()
        recipeIngredientsDao = AppDb.getDatabase(context).recipeIngredientsDao()
        recipeIngredientsTotalDao = AppDb.getDatabase(context).recipeIngredientsTotalDao()
        logDao = AppDb.getDatabase(context).logDao()
        pantryDao = AppDb.getDatabase(context).pantryDao()

    }

    /**
     * Creates instance of AppDataRepo in dbPopulatorUtil
     */
    constructor(dbPopulatorUtil: DBPopulatorUtil)

    /**
     * Wrapper method. Perform dao operation to get ingredients by a given category from the db.
     */
    fun getIngredientsByCategory(category: String): List<Ingredient> {
        return ingredientDao.getIngredientsByCategory(category)
    }

    /**
     * Wrapper method. Perform dao operation to get ingredient by a given name from the db.
     */
    fun getIngredientsByName(name: String): List<Ingredient> {
        return ingredientDao.getIngredientsByName(name)
    }

    /**
     * Wrapper method. Perform dao operation to get intolerance by a given name from the db.
     */
    fun getIntoleranceByName(intolerance: String): List<Intolerance> {
        return intoleranceDao.getIntoleranceByName(intolerance)
    }

    /**
     * This methods perform dao operation to all the ingredients the db.
     *
     * @return true if there exists ingredients, false if otherwise
     */
    fun haveIngredient(): Boolean {
        val ingredients = ingredientDao.allIngredients
        return ingredients.size > 0
    }

    /**
     * This methods perform dao operation to all the intolerances the db.
     *
     * @return true if there exists intolerances, false if otherwise
     */
    fun haveIntolerance(): Boolean {
        val intolerances = intoleranceDao.allIntolerances
        return intolerances.size > 0
    }

    /**
     * This methods perform dao operation to all the recipes the db.
     *
     * @return true if there exists recipes, false if otherwise
     */
    fun haveRecipe(): Boolean {
        val recipes = recipeDao.allDefaultRecipes
        return recipes.size > 0
    }

    /**
     * This methods perform dao operation to all the recipesingredients the db.
     *
     * @return true if there exists recipesingredients, false if otherwise
     */
    fun haveRecipeIngredients(): Boolean {
        val recipeIngredients = recipeIngredientsDao.allRecipesAndIngredients
        return recipeIngredients.size > 0
    }

    /**
     * This methods perform dao operation to all the recipesingredientstotal the db.
     *
     * @return true if there exists recipesingredientstotal, false if otherwise
     */
    fun haveRecipeIngredientsTotal(): Boolean {
        val recipeIngredientsTotals = recipeIngredientsTotalDao.allRecipesAndIngredientsTotal
        return recipeIngredientsTotals.size > 0
    }

    /**
     * Wrapper method. Perform dao operation to insert ingredients into the db.
     */
    fun insertIngredients(ingredients: ArrayList<Ingredient>) {
        ingredientDao.addIngredients(ingredients)
    }

    /**
     * Wrapper method. Perform dao operation to insert intolerance into the db.
     */
    fun insertIntolerance(intolerance: Intolerance) {
        intoleranceDao.addIntolerance(intolerance)
    }

    /**
     * Wrapper method. Perform dao operation to insert RecipeIngredientsTotals into the db.
     */
    fun insertRecipeIngredientsTotal(recipeIngredientsTotals: ArrayList<RecipeIngredientsTotal>) {
        recipeIngredientsTotalDao.addRecipeIngredientsTotal(recipeIngredientsTotals)
    }

    /**
     * Wrapper method. Perform dao operation to delete all ingredients from the db.
     */
    private fun deleteAllIngredient() {
        ingredientDao.deleteAll()
    }

    /**
     * Wrapper method. Perform dao operation to delete all intolerances from the db.
     */
    private fun deleteAllIntolerance() {
        intoleranceDao.deleteAll()
    }

    /**
     * Wrapper method. Perform dao operation to include an intolerance to the db.
     */
    fun includeIntolerance(intoleranceName: String) {
        intoleranceDao.includeIntolerance(intoleranceName)
    }

    /**
     * Wrapper method. Perform dao operation to exclude an intolerance from the db.
     */
    fun excludeIntolerance(intoleranceName: String) {
        intoleranceDao.excludeIntolerance(intoleranceName)
    }

    /**
     * Wrapper method. Perform dao operation to exclude an ingredient from the db.
     */
    fun excludeIngredient(ingredientName: String) {
        ingredientDao.excludeIngredient(ingredientName)
    }

    /**
     * Wrapper method. Perform dao operation to include an ingredient to the db.
     */
    fun includeIngredient(ingredientName: String) {
        ingredientDao.includeIngredient(ingredientName)
    }

    /**
     * Wrapper method. Perform dao operation to exclude a recipe from the db.
     */
    fun excludeRecipe(ingredientName: String) {
        recipeDao.excludeRecipe(ingredientName)
    }

    /**
     * Wrapper method. Perform dao operation to include a recipe to the db.
     */
    fun includeRecipe(ingredientName: String) {
        recipeDao.includeRecipe(ingredientName)
    }

    /**
     * Wrapper method. Perform dao operation to mark an ingredient as "checked" in the db.
     */
    fun selectIngredient(ingredientName: String) {
        ingredientDao.selectIngredient(ingredientName)
    }

    /**
     * Wrapper method. Perform dao operation to mark an ingredient as "unchecked" in the db.
     */
    fun deselectIngredient(ingredientName: String) {
        ingredientDao.deselectIngredient(ingredientName)
    }

    /**
     * Wrapper method. Perform dao operation to get a recipe by name from the db.
     */
    fun getRecipesByName(recipeName: String): List<Recipe> {
        return recipeDao.getRecipesByName(recipeName)
    }

    /**
     * Wrapper method. Perform dao operation to get all recipes of which users have ingredient from the db (with a limit).
     *
     * @return all recipes by checked ingredients if there is no limit. otherwise return the first n row (n is the limit)
     */
    fun getAllRecipesByCheckedIngredients(limit: Int): List<Recipe> {
        return if (limit == 0)
            recipeDao.allRecipesByCheckedIngredients
        else
            recipeDao.getAllRecipesByCheckedIngredientsWithLimit(limit)
    }

    /**
     * Wrapper method. Perform dao operation to get all recipes of which users have ingredient from the db (with an offset).
     *
     * @return all recipes by checked ingredients if offset is 10. otherwise return the nth row to the last row (n - 1 is the offset)
     * For example, if offset = 2, return everything from the third row (except first and second row).
     */
    fun getAllRecipesByCheckedIngredientsWithOffset(offset: Int): List<Recipe> {
        return recipeDao.getAllRecipesByCheckedIngredientsWithOffset(offset)
    }

    /**
     * Wrapper method. Perform dao operation to get all missing ingredients by recipe name from the db.
     *
     * @return ALL missing ingredients of a given recipe name if limit is 0. Otherwise return only the first n row (n is the limit)
     */
    fun getMissingIngredientsByName(name: String, limit: Int): List<String> {
        return if (limit == 0)
            recipeDao.getMissingIngredientsByName(name)
        else
            recipeDao.getMissingIngredientsByNameWithLimit(name, limit)
    }

    /**
     * Wrapper method. Perform dao operation to add many recipes to the db.
     */
    fun insertRecipes(recipes: ArrayList<Recipe>) {
        recipeDao.addRecipes(recipes)
    }

    /**
     * Wrapper method. Perform dao operation to add one recipe to the db.
     */
    fun insertRecipe(recipe: Recipe) {
        recipeDao.addRecipe(recipe)
    }

    /**
     * Wrapper method. Perform dao operation to add RecipeIngredients to the db.
     */
    fun insertRecipeIngredients(recipeIngredients: ArrayList<RecipeIngredients>) {
        recipeIngredientsDao.addRecipeIngredients(recipeIngredients)
    }

    /**
     * Wrapper method. Perform dao operation to save a recipe the db.
     */
    fun saveRecipe(recipeId: Int) {
        recipeDao.saveRecipe(recipeId)
    }

    /**
     * Wrapper method. Perform dao operation to unsave a recipe the db.
     */
    fun unSaveRecipe(recipeId: Int) {
        recipeDao.unSaveRecipe(recipeId)
    }

    /**
     * Wrapper method. Perform dao operation to remove a recipe the db.
     */
    fun removeRecipe(recipeId: Int) {
        recipeDao.deleteRecipe(recipeId)
    }

    /*-************** User Repo ***********************/


    /**
     * Perform dao operation to get users from Users db.
     */
    fun getUserName(userName: String): User {
        return userDao.getUser(userName)
    }

    /***
     * Perform dao operation to create a new user into users db.
     */
    @SuppressLint("StaticFieldLeak")
    fun createUser(user: User) {
        userDao.insertUser(user)
    }

    /**
     * Perform operation to update the login status for the user. If the login status is not true, it will also perform the deleteAllIngredient, deleteAllIntolerance
     * and then pantry.deleteAll operations
     */
    fun updateLoginStatus(userId: Int, isLogin: Boolean) {
        userDao.updateLoginStatus(userId, isLogin)
        if (!isLogin) {
            deleteAllIngredient()
            deleteAllIntolerance()
            pantryDao.deleteAll()
        }
    }

    /**
     * Wrapper method. Perform dao operation to add an intolerance that is currently selected in the intolerance db to a users saved JSON. It will use GSON to convert the values to add into a JSON file for the user.
     * This means that on execution of this method, the intolerance value in the JSON file will be added. This is stored this way, as on logout of the user, the intolerance database is reset and on login, they will be reloaded from JSON for that user into the DB,
     * so if they are not added into the JSON file they will not be reloaded into the database.
     */
    fun addTolerance(intoleranceName: String) {
        val user = userDao.signInUser

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        val tolerance = gson.fromJson<List<String>>(user.intolerances, type)
        tolerance.add(intoleranceName)
        userDao.updateIntoleranceValue(gson.toJson(tolerance))
    }

    /**
     * Wrapper method. Perform dao operation to remove an intolerance that is currently selected in the intolerance db to a users saved JSON. It will use GSON to convert the values to remove into a JSON file for the user.
     * This means that on execution of this method, the intolerance value in the JSON file will be removed. This is stored this way, as on logout of the user, the intolerance database is reset and on login, they will be reloaded for that user into the DB,
     * so if they are not cleared from the JSON they will be reloaded into the database anyways.
     */
    fun removeTolerance(intoleranceName: String) {
        val user = userDao.signInUser
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        val tolerance = gson.fromJson<List<String>>(user.intolerances, type)
        tolerance.remove(intoleranceName)
        userDao.updateIntoleranceValue(gson.toJson(tolerance))
    }

    /**
     * Handles insertion of logs. When method runs, insertion into log database will occur through insert command in logdao
     */
    @SuppressLint("StaticFieldLeak")
    fun insertLogs(error: String) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                logDao.insertLog(LogRecords(error))
                return null
            }
        }
                .execute()
    }

    /**
     * Handles the deletion of logs from log database through logdao
     */
    @SuppressLint("StaticFieldLeak")
    fun clearLog() {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                logDao.clearLog()
                return null
            }
        }
                .execute()
    }

    /**
     * Handles the clearing of all selected ingredients
     */
    @SuppressLint("StaticFieldLeak")
    fun clearIngredients() {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                ingredientDao.clearIngredients()
                return null
            }
        }
                .execute()
    }

    /**
     * Wrapper method. Perform dao operation to add ingredients to pantry in the db.
     */
    fun addIngredientToPantry(ingredients: Ingredient) {
        pantryDao.addIngredients(Pantry(ingredients.ingredientID))
        savePantryToUserDB()
    }

    /**
     * Wrapper method. Perform dao operation to clear all ingredients that are currently in the pantry from a users saved JSON. It will use GSON to convert the values to remove into a JSON file for the user.
     * This means that on execution of this method, the values in the JSON file will be blanked out. This is stored this way, as on logout of the user, the pantry database is reset and on login, they will be reloaded from JSON for that user into the DB,
     * so if they are not removed from the JSON file they will be reloaded into the pantry database.
     */
    fun clearUserIngredientsPantry() {
        val gson = Gson()
        val pantries = ArrayList<Pantry>()
        userDao.updatePantryValue(gson.toJson(pantries))
    }

    /**
     * Wrapper method. Perform dao operation to remove all ingredients from the pantry in the db.
     */
    fun deleteIngredientsPantry() {
        pantryDao.deleteAll()
    }

    /**
     * Wrapper method. Perform dao operation to remove an ingredient from the pantry in the db.
     */
    fun removeIngredientFromPantry(ingredientId: Int) {
        pantryDao.remove(ingredientId)
    }

    /**
     * Wrapper method. Perform dao operation to get ingredients that are currently in the pantry from a users saved JSON. It will use GSON to convert the values into a JSON file for the user.
     * This is stored this way, as on logout of the user, the pantr database is reset and on login, they will be reloaded from JSON for that user into the DB,
     * so if they are not added into the JSON file they will not be reloaded into the database.
     */
    fun savePantryToUserDB() {
        val pantries = pantryDao.pantryIngredients
        val gson = Gson()
        gson.toJson(pantries)
        userDao.updatePantryValue(gson.toJson(pantries))
    }

    /**
     * Wrapper method. Perform dao operation to add an ingredient to the pantry in the db.
     */
    fun addIngredientToPantry(pantry: Pantry) {
        pantryDao.addIngredients(pantry)
    }

}