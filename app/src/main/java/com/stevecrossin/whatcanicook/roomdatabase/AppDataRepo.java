package com.stevecrossin.whatcanicook.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.IngredientDao;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.IntoleranceDao;
import com.stevecrossin.whatcanicook.entities.LogDao;
import com.stevecrossin.whatcanicook.entities.LogRecords;
import com.stevecrossin.whatcanicook.entities.Pantry;
import com.stevecrossin.whatcanicook.entities.PantryDao;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.entities.RecipeDao;
import com.stevecrossin.whatcanicook.entities.RecipeIngredients;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsDao;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsTotal;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsTotalDao;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.entities.UserDao;
import com.stevecrossin.whatcanicook.other.DBPopulatorUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Data repository for application. The below contains all the data entry objects (dao) for the room databases of the app.
 */
public class AppDataRepo {
    private IngredientDao ingredientDao;
    private IntoleranceDao intoleranceDao;
    private UserDao userDao;
    private RecipeDao recipeDao;
    private RecipeIngredientsDao recipeIngredientsDao;
    private RecipeIngredientsTotalDao recipeIngredientsTotalDao;
    private LogDao logDao;
    private PantryDao pantryDao;

    /**
     * Constructor of AppDataRepo
     */
    public AppDataRepo(Context context) {
        ingredientDao = AppDb.getDatabase(context).ingredientDao();
        intoleranceDao = AppDb.getDatabase(context).intoleranceDao();
        userDao = AppDb.getDatabase(context).userDao();
        recipeDao = AppDb.getDatabase(context).recipeDao();
        recipeIngredientsDao = AppDb.getDatabase(context).recipeIngredientsDao();
        recipeIngredientsTotalDao = AppDb.getDatabase(context).recipeIngredientsTotalDao();
        logDao = AppDb.getDatabase(context).logDao();
        pantryDao = AppDb.getDatabase(context).pantryDao();

    }

    /**
     * Creates instance of AppDataRepo in dbPopulatorUtil
     */
    public AppDataRepo(DBPopulatorUtil dbPopulatorUtil) {
    }

    /**
     * Wrapper method. Perform dao operation to get all the ingredients from the db.
     */
    public List<Ingredient> getAllIngredients() {
        return ingredientDao.getAllIngredients();
    }

    /**
     * Wrapper method. Perform dao operation to get all checked ingredients from the db.
     */
    public List<Ingredient> getAllCheckedIngredients() {
        return ingredientDao.getAllCheckedIngredients();
    }

    /**
     * Wrapper method. Perform dao operation to get ingredients by a given category from the db.
     */
    public List<Ingredient> getIngredientsByCategory(String category) {
        return ingredientDao.getIngredientsByCategory(category);
    }

    /**
     * Wrapper method. Perform dao operation to get ingredient by a given name from the db.
     */
    public List<Ingredient> getIngredientsByName(String name) {
        return ingredientDao.getIngredientsByName(name);
    }

    /**
     * Wrapper method. Perform dao operation to get intolerance by a given name from the db.
     */
    public List<Intolerance> getIntoleranceByName(String intolerance) {
        return intoleranceDao.getIntoleranceByName(intolerance);
    }

    /**
     * Wrapper method. Perform dao operation to get all categories from the db.
     */
    public List<Ingredient> getAllCategories() {
        return ingredientDao.getAllCategories();
    }

    /**
     * Wrapper method. Perform dao operation to get all intolerances from the db.
     */
    public List<Intolerance> getAllIntolerances() {
        return intoleranceDao.getAllIntolerances();
    }

    /**
     * This methods perform dao operation to all the ingredients the db.
     *
     * @return true if there exists ingredients, false if otherwise
     */
    public boolean haveIngredient() {
        List<Ingredient> ingredients = ingredientDao.getAllIngredients();
        return ingredients.size() > 0;
    }

    /**
     * This methods perform dao operation to all the intolerances the db.
     *
     * @return true if there exists intolerances, false if otherwise
     */
    public boolean haveIntolerance() {
        List<Intolerance> intolerances = intoleranceDao.getAllIntolerances();
        return intolerances.size() > 0;
    }

    /**
     * This methods perform dao operation to all the recipes the db.
     *
     * @return true if there exists recipes, false if otherwise
     */
    public boolean haveRecipe() {
        List<Recipe> recipes = recipeDao.getAllDefaultRecipes();
        return recipes.size() > 0;
    }

    /**
     * This methods perform dao operation to all the recipesingredients the db.
     *
     * @return true if there exists recipesingredients, false if otherwise
     */
    public boolean haveRecipeIngredients() {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsDao.getAllRecipesAndIngredients();
        return recipeIngredients.size() > 0;
    }

    /**
     * This methods perform dao operation to all the recipesingredientstotal the db.
     *
     * @return true if there exists recipesingredientstotal, false if otherwise
     */
    public boolean haveRecipeIngredientsTotal() {
        List<RecipeIngredientsTotal> recipeIngredientsTotals = recipeIngredientsTotalDao.getAllRecipesAndIngredientsTotal();
        return recipeIngredientsTotals.size() > 0;
    }

    /**
     * Wrapper method. Perform dao operation to insert ingredients into the db.
     */
    public void insertIngredients(ArrayList<Ingredient> ingredients) {
        ingredientDao.addIngredients(ingredients);
    }

    /**
     * Wrapper method. Perform dao operation to insert intolerance into the db.
     */
    public void insertIntolerance(Intolerance intolerance) {
        intoleranceDao.addIntolerance(intolerance);
    }

    /**
     * Wrapper method. Perform dao operation to get the unique intolerance from the db.
     */
    public List<Intolerance> getUniqueTolerance() {
        return intoleranceDao.getUniqueIntolerance();
    }

    /**
     * Wrapper method. Perform dao operation to insert RecipeIngredientsTotals into the db.
     */
    public void insertRecipeIngredientsTotal(ArrayList<RecipeIngredientsTotal> recipeIngredientsTotals) {
        recipeIngredientsTotalDao.addRecipeIngredientsTotal(recipeIngredientsTotals);
    }

    /**
     * Wrapper method. Perform dao operation to delete all ingredients from the db.
     */
    private void deleteAllIngredient() {
        ingredientDao.deleteAll();
    }

    /**
     * Wrapper method. Perform dao operation to delete all intolerances from the db.
     */
    private void deleteAllIntolerance() {
        intoleranceDao.deleteAll();
    }

    /**
     * Wrapper method. Perform dao operation to include an intolerance to the db.
     */
    public void includeIntolerance(String intoleranceName) {
        intoleranceDao.includeIntolerance(intoleranceName);
    }

    /**
     * Wrapper method. Perform dao operation to exclude an intolerance from the db.
     */
    public void excludeIntolerance(String intoleranceName) {
        intoleranceDao.excludeIntolerance(intoleranceName);
    }

    /**
     * Wrapper method. Perform dao operation to exclude an ingredient from the db.
     */
    public void excludeIngredient(String ingredientName) {
        ingredientDao.excludeIngredient(ingredientName);
    }

    /**
     * Wrapper method. Perform dao operation to include an ingredient to the db.
     */
    public void includeIngredient(String ingredientName) {
        ingredientDao.includeIngredient(ingredientName);
    }

    /**
     * Wrapper method. Perform dao operation to exclude a recipe from the db.
     */
    public void excludeRecipe(String ingredientName) {
        recipeDao.excludeRecipe(ingredientName);
    }

    /**
     * Wrapper method. Perform dao operation to include a recipe to the db.
     */
    public void includeRecipe(String ingredientName) {
        recipeDao.includeRecipe(ingredientName);
    }

    /**
     * Wrapper method. Perform dao operation to mark an ingredient as "checked" in the db.
     */
    public void selectIngredient(String ingredientName) {
        ingredientDao.selectIngredient(ingredientName);
    }

    /**
     * Wrapper method. Perform dao operation to mark an ingredient as "unchecked" in the db.
     */
    public void deselectIngredient(String ingredientName) {
        ingredientDao.deselectIngredient(ingredientName);
    }

    /**
     * Wrapper method. Perform dao operation to get all recipes from the db.
     */
    public List<Recipe> getAllRecipes() {
        return recipeDao.getAllDefaultRecipes();
    }

    /**
     * Wrapper method. Perform dao operation to get a recipe by name from the db.
     */
    public List<Recipe> getRecipesByName(String recipeName) {
        return recipeDao.getRecipesByName(recipeName);
    }

    /**
     * Wrapper method. Perform dao operation to get all RecipeIngredient from the db.
     */
    public List<RecipeIngredients> getAllRecipesAndIngredients() {
        return recipeIngredientsDao.getAllRecipesAndIngredients();
    }

    /**
     * Wrapper method. Perform dao operation to get all RecipeIngredientsTotal from the db.
     */
    public List<RecipeIngredientsTotal> getAllRecipesAndIngredientsTotal() {
        return recipeIngredientsTotalDao.getAllRecipesAndIngredientsTotal();
    }

    /**
     * Wrapper method. Perform dao operation to get all recipes of which users have ingredient from the db (with a limit).
     *
     * @return all recipes by checked ingredients if there is no limit. otherwise return the first n row (n is the limit)
     */
    public List<Recipe> getAllRecipesByCheckedIngredients(int limit) {
        if (limit == 0)
            return recipeDao.getAllRecipesByCheckedIngredients();
        else
            return recipeDao.getAllRecipesByCheckedIngredientsWithLimit(limit);
    }

    /**
     * Wrapper method. Perform dao operation to get all recipes of which users have ingredient from the db (with an offset).
     *
     * @return all recipes by checked ingredients if offset is 10. otherwise return the nth row to the last row (n - 1 is the offset)
     * For example, if offset = 2, return everything from the third row (except first and second row).
     */
    public List<Recipe> getAllRecipesByCheckedIngredientsWithOffset(int offset) {
        return recipeDao.getAllRecipesByCheckedIngredientsWithOffset(offset);
    }

    /**
     * Wrapper method. Perform dao operation to get all recipes of which users have ingredient from the db with exact match.
     *
     * @return all recipes by checked ingredients ONLY if users have ALL the ingredients from that recipe.
     */
    public List<Recipe> getAllRecipesByCheckedIngredientsWithExactMatch() {
        return recipeDao.getAllRecipesByCheckedIngredientsWithExactMatch();
    }

    /**
     * Wrapper method. Perform dao operation to get all saved recipes from the db.
     */
    public List<Recipe> getAllSavedRecipes() {
        return recipeDao.getAllSavedRecipes();
    }

    /**
     * Wrapper method. Perform dao operation to get all missing ingredients by recipe name from the db.
     *
     * @return ALL missing ingredients of a given recipe name if limit is 0. Otherwise return only the first n row (n is the limit)
     */
    public List<String> getMissingIngredientsByName(String name, int limit) {
        if (limit == 0)
            return recipeDao.getMissingIngredientsByName(name);
        else
            return recipeDao.getMissingIngredientsByNameWithLimit(name, limit);
    }

    /**
     * Wrapper method. Perform dao operation to add many recipes to the db.
     */
    public void insertRecipes(ArrayList<Recipe> recipes) {
        recipeDao.addRecipes(recipes);
    }

    /**
     * Wrapper method. Perform dao operation to add one recipe to the db.
     */
    public void insertRecipe(Recipe recipe) {
        recipeDao.addRecipe(recipe);
    }

    /**
     * Wrapper method. Perform dao operation to add RecipeIngredients to the db.
     */
    public void insertRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients) {
        recipeIngredientsDao.addRecipeIngredients(recipeIngredients);
    }

    /**
     * Wrapper method. Perform dao operation to save a recipe the db.
     */
    public void saveRecipe(int recipeId) {
        recipeDao.saveRecipe(recipeId);
    }

    /**
     * Wrapper method. Perform dao operation to unsave a recipe the db.
     */
    public void unSaveRecipe(int recipeId) {
        recipeDao.unSaveRecipe(recipeId);
    }

    /**
     * Wrapper method. Perform dao operation to remove a recipe the db.
     */
    public void removeRecipe(int recipeId) {
        recipeDao.deleteRecipe(recipeId);
    }

    /*-************** User Repo ***********************/


    /**
     * Perform dao operation to get users from Users db.
     */
    public User getUserName(String userName) {
        return userDao.getUser(userName);
    }

    /***
     * Perform dao operation to create a new user into users db.
     */
    @SuppressLint("StaticFieldLeak")
    public void createUser(final User user) {
        userDao.insertUser(user);
    }

    /**
     * Perform operation to update the login status for the user. If the login status is not true, it will also perform the deleteAllIngredient, deleteAllIntolerance
     * and then pantry.deleteAll operations
     */
    public void updateLoginStatus(int userId, boolean isLogin) {
        userDao.updateLoginStatus(userId, isLogin);
        if (!isLogin) {
            deleteAllIngredient();
            deleteAllIntolerance();
            pantryDao.deleteAll();
        }
    }

    /**
     * Wrapper method. Perform dao operation to get sign in user from the db.
     */
    public User getSignedUser() {
        return userDao.getSignInUser();
    }

    /**
     * Wrapper method. Perform dao operation to add an intolerance that is currently selected in the intolerance db to a users saved JSON. It will use GSON to convert the values to add into a JSON file for the user.
     * This means that on execution of this method, the intolerance value in the JSON file will be added. This is stored this way, as on logout of the user, the intolerance database is reset and on login, they will be reloaded from JSON for that user into the DB,
     * so if they are not added into the JSON file they will not be reloaded into the database.
     */
    public void addTolerance(String intoleranceName) {
        User user = userDao.getSignInUser();

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> tolerance = gson.fromJson(user.getIntolerances(), type);
        tolerance.add(intoleranceName);
        userDao.updateIntoleranceValue(gson.toJson(tolerance));
    }

    /**
     * Wrapper method. Perform dao operation to remove an intolerance that is currently selected in the intolerance db to a users saved JSON. It will use GSON to convert the values to remove into a JSON file for the user.
     * This means that on execution of this method, the intolerance value in the JSON file will be removed. This is stored this way, as on logout of the user, the intolerance database is reset and on login, they will be reloaded for that user into the DB,
     * so if they are not cleared from the JSON they will be reloaded into the database anyways.
     */
    public void removeTolerance(String intoleranceName) {
        User user = userDao.getSignInUser();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> tolerance = gson.fromJson(user.getIntolerances(), type);
        tolerance.remove(intoleranceName);
        userDao.updateIntoleranceValue(gson.toJson(tolerance));
    }

    /**
     * Fetch all logs saved in the database through select command from logdao
     */
    public List<LogRecords> getLogs() {
        return logDao.getLogs();
    }

    /**
     * Handles insertion of logs. When method runs, insertion into log database will occur through insert command in logdao
     ***/
    @SuppressLint("StaticFieldLeak")
    public void insertLogs(final String error) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                logDao.insertLog(new LogRecords(error));
                return null;
            }
        }
                .execute();
    }

    /**
     * Handles the deletion of logs from log database through logdao
     **/
    @SuppressLint("StaticFieldLeak")
    public void clearLog() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                logDao.clearLog();
                return null;
            }
        }
                .execute();
    }

    /**
     * Handles the clearing of all selected ingredients
     **/
    @SuppressLint("StaticFieldLeak")
    public void clearIngredients() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ingredientDao.clearIngredients();
                return null;
            }
        }
                .execute();
    }

    /**
     * Wrapper method. Perform dao operation to get all tolerant ingredients from the db.
     */
    public List<Ingredient> getAllTolerantIngredients() {
        return ingredientDao.getAllTolerantIngredient();
    }

    /**
     * Wrapper method. Perform dao operation to add ingredients to pantry in the db.
     */
    public void addIngredientToPantry(Ingredient ingredients) {
        pantryDao.addIngredients(new Pantry(ingredients.getIngredientID()));
        savePantryToUserDB();
    }

    /**
     * Wrapper method. Perform dao operation to clear all ingredients that are currently in the pantry from a users saved JSON. It will use GSON to convert the values to remove into a JSON file for the user.
     * This means that on execution of this method, the values in the JSON file will be blanked out. This is stored this way, as on logout of the user, the pantry database is reset and on login, they will be reloaded from JSON for that user into the DB,
     * so if they are not removed from the JSON file they will be reloaded into the pantry database.
     */
    public void clearUserIngredientsPantry() {
        Gson gson = new Gson();
        List<Pantry> pantries = new ArrayList<>();
        userDao.updatePantryValue(gson.toJson(pantries));
    }

    /**
     * Wrapper method. Perform dao operation to remove all ingredients from the pantry in the db.
     */
    public void deleteIngredientsPantry() {
        pantryDao.deleteAll();
    }

    /**
     * Wrapper method. Perform dao operation to get all pantry ingredients from the db, and place them into an array list. Returns the list of ingredients by ID
     */
    public List<Ingredient> getAllPantryIngredients() {
        List<Pantry> pantries = pantryDao.getPantryIngredients();
        List<Integer> integers = new ArrayList<>();

        for (Pantry pantry : pantries) {
            integers.add(pantry.getIngredientId());
        }

        return ingredientDao.getIngredientsById(integers);
    }

    /**
     * Wrapper method. Perform dao operation to remove an ingredient from the pantry in the db.
     */
    public void removeIngredientFromPantry(int ingredientId) {
        pantryDao.remove(ingredientId);
    }

    /**
     * Wrapper method. Perform dao operation to get ingredients that are currently in the pantry from a users saved JSON. It will use GSON to convert the values into a JSON file for the user.
     * This is stored this way, as on logout of the user, the pantr database is reset and on login, they will be reloaded from JSON for that user into the DB,
     * so if they are not added into the JSON file they will not be reloaded into the database.
     */
    public void savePantryToUserDB() {
        List<Pantry> pantries = pantryDao.getPantryIngredients();
        Gson gson = new Gson();
        gson.toJson(pantries);
        userDao.updatePantryValue(gson.toJson(pantries));
    }

    /**
     * Wrapper method. Perform dao operation to add an ingredient to the pantry in the db.
     */
    public void addIngredientToPantry(Pantry pantry) {
        pantryDao.addIngredients(pantry);
    }


    public void refreshTable() {
        ingredientDao.deleteAll();
        intoleranceDao.deleteAll();
        recipeDao.deleteAll();
    }

    public void logSize() {
        Log.d("APP_DEBUG", "ingredientDao TABLE SIZE : " + ingredientDao.getAllIngredients().size());
        Log.d("APP_DEBUG", "recipeDao TABLE SIZE : " + recipeDao.getAllDefaultRecipes().size());
        Log.d("APP_DEBUG", "intoleranceDao TABLE SIZE : " + intoleranceDao.getAllIntolerances().size());
    }
}