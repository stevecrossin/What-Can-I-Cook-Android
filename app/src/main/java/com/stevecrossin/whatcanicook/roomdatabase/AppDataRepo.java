package com.stevecrossin.whatcanicook.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.IngredientDao;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.IntoleranceDao;
import com.stevecrossin.whatcanicook.entities.LogDao;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Initial source code for app data repo - barebones, non functional and still in progress
 **/

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
     * private intoleranceDao intoleranceDao;
     * private logsDao logsDao;
     * private pantryDao pantryDao;
     * private recipeDao recipeDao;
     **/

    public AppDataRepo(Context context) {
        ingredientDao = AppDb.getDatabase(context).ingredientDao();
        intoleranceDao = AppDb.getDatabase(context).intoleranceDao();
        userDao = AppDb.getDatabase(context).userDao();
        recipeDao = AppDb.getDatabase(context).recipeDao();
        recipeIngredientsDao = AppDb.getDatabase(context).recipeIngredientsDao();
        recipeIngredientsTotalDao = AppDb.getDatabase(context).recipeIngredientsTotalDao();
        logDao = AppDb.getDatabase(context).logDao();
        pantryDao = AppDb.getDatabase(context).pantryDao();

        //intoleranceDao = AppDb.getDatabase(context).intoleranceDao();

        //recipeDao = AppDb.getDatabase(context).recipeDao();

    }

    public List<Ingredient> getAllIngredients() {
        return ingredientDao.getAllIngredients();
    }

    public List<Ingredient> getAllCheckedIngredients() {
        return ingredientDao.getAllCheckedIngredients();
    }

    public List<Ingredient> getIngredientsByCategory(String category) {
        return ingredientDao.getIngredientsByCategory(category);
    }

    public List<Ingredient> getIngredientsByName(String name) {
        return ingredientDao.getIngredientsByName(name);
    }

    public List<Intolerance> getIntoleranceByName(String intolerance) {
        return intoleranceDao.getIntoleranceByName(intolerance);
    }

    public List<Ingredient> getAllCategories() {
        return ingredientDao.getAllCategories();
    }

    public List<Intolerance> getAllIntolerances() {
        return intoleranceDao.getAllIntolerances();
    }

    public boolean haveIngredient() {
        List<Ingredient> ingredients = ingredientDao.getAllIngredients();
        if (ingredients.size() > 0)
            return true;
        return false;
    }

    public boolean haveIntolerance() {
        List<Intolerance> intolerances = intoleranceDao.getAllIntolerances();
        if (intolerances.size() > 0)
            return true;
        return false;
    }

    public boolean haveRecipe() {
        List<Recipe> recipes = recipeDao.getAllDefaultRecipes();
        if (recipes.size() > 0)
            return true;
        return false;
    }

    public boolean haveRecipeIngredients() {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsDao.getAllRecipesAndIngredients();
        if (recipeIngredients.size() > 0)
            return true;
        return false;
    }

    public boolean haveRecipeIngredientsTotal() {
        List<RecipeIngredientsTotal> recipeIngredientsTotals = recipeIngredientsTotalDao.getAllRecipesAndIngredientsTotal();
        if (recipeIngredientsTotals.size() > 0)
            return true;
        return false;
    }

    public void insertIngredients(ArrayList<Ingredient> ingredients) {
        ingredientDao.addIngredients(ingredients);
    }


    public void insertIntolerance(Intolerance intolerance) {
        intoleranceDao.addIntolerance(intolerance);
    }

    public void insertRecipeIngredientsTotal(ArrayList<RecipeIngredientsTotal> recipeIngredientsTotals) {
        recipeIngredientsTotalDao.addRecipeIngredientsTotal(recipeIngredientsTotals);
    }

    public void deleteAllIngredient() {
        ingredientDao.deleteAll();
    }

    public void deleteAllIntolerance() {
        intoleranceDao.deleteAll();
    }

    public void includeIntolerance(String intoleranceName) {
        intoleranceDao.includeIntolerance(intoleranceName);
    }

    public void excludeIntolerance(String intoleranceName) {
        intoleranceDao.excludeIntolerance(intoleranceName);
    }

    public void excludeIngredient(String ingredientName) {
        ingredientDao.excludeIngredient(ingredientName);
    }

    public void includeIngredient(String ingredientName) {
        ingredientDao.includeIngredient(ingredientName);
    }

    public void excludeRecipe(String ingredientName) {
        recipeDao.excludeRecipe(ingredientName);
    }

    public void includeRecipe(String ingredientName) {
        recipeDao.includeRecipe(ingredientName);
    }

    public void selectIngredient(String ingredientName) {
        ingredientDao.selectIngredient(ingredientName);
    }

    public void deselectIngredient(String ingredientName) {
        ingredientDao.deselectIngredient(ingredientName);
    }

    public List<Recipe> getAllRecipes() {
        return recipeDao.getAllDefaultRecipes();
    }

    public List<Recipe> getRecipesByName(String recipeName) {
        return recipeDao.getRecipesByName(recipeName);
    }

    public List<Recipe> getRecipesByNames(ArrayList<String> recipeNames) {
        return recipeDao.getRecipesByNames(recipeNames);
    }

    public List<RecipeIngredients> getAllRecipesAndIngredients() {
        return recipeIngredientsDao.getAllRecipesAndIngredients();
    }

    public List<String> getAllRecipesByIngredient(ArrayList<String> ingredients) {
        return recipeIngredientsDao.getAllRecipesByIngredient(ingredients);
    }

    public List<RecipeIngredientsTotal> getAllRecipesAndIngredientsTotal() {
        return recipeIngredientsTotalDao.getAllRecipesAndIngredientsTotal();
    }

    public List<Recipe> getAllRecipesByCheckedIngredients(int limit) {
        if (limit == 0)
            return recipeDao.getAllRecipesByCheckedIngredients();
        else
            return recipeDao.getAllRecipesByCheckedIngredientsWithLimit(limit);
    }


    public List<Recipe> getAllRecipesByCheckedIngredientsWithExactMatch() {
        return recipeDao.getAllRecipesByCheckedIngredientsWithExactMatch();
    }

    public List<Recipe> getAllSavedRecipes(){
        return recipeDao.getAllSavedRecipes();
    }

    public List<Integer> getNumberOfMissingIngredientsByName(String name) {
        return recipeDao.getNumberOfMissingIngredientsByName(name);
    }

    public List<String> getMissingIngredientsByName(String name, int limit) {
        if (limit == 0)
            return recipeDao.getMissingIngredientsByName(name);
        else
            return recipeDao.getMissingIngredientsByNameWithLimit(name, limit);
    }

    public void insertRecipes(ArrayList<Recipe> recipes) {
        recipeDao.addRecipes(recipes);
    }

    public void insertRecipe(Recipe recipe) {
        recipeDao.addRecipe(recipe);
    }

    public void insertRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients) {
        recipeIngredientsDao.addRecipeIngredients(recipeIngredients);
    }

    public void saveRecipe(int recipeId){
        recipeDao.saveRecipe(recipeId);
    }

    /**-************** User Repo ***********************/


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

    public void updateLoginStatus(int userId, boolean isLogin) {
        userDao.updateLoginStatus(userId, isLogin);
        if (!isLogin) {
            deleteAllIngredient();
            deleteAllIntolerance();
            pantryDao.deleteAll();
        }
    }

    public User getSignedUser() {
        return userDao.getSignInUser();
    }

    public void addTolerance(String intoleranceName) {
        User user = userDao.getSignInUser();

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> tolerance = gson.fromJson(user.getIntolerances(), type);
        tolerance.add(intoleranceName);
        userDao.updateIntoleranceValue(gson.toJson(tolerance));
    }

    public void removeTolerance(String intoleranceName) {
        User user = userDao.getSignInUser();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> tolerance = gson.fromJson(user.getIntolerances(), type);
        tolerance.remove(intoleranceName);
        userDao.updateIntoleranceValue(gson.toJson(tolerance));
    }


    /**************** User Repo ***********************/

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

    public List<Ingredient> getAllTolerantIngredients() {
        return ingredientDao.getAllTolerantIngredient();
    }

    //<editor-fold desc=" Pantry">

    public void addIngredientToPantry(Ingredient ingredients) {
        pantryDao.addIngredients(new Pantry(ingredients.getIngredientID()));
        savePantryToUserDB();
    }

    public void clearUserIngredientsPantry() {
        Gson gson = new Gson();
        List<Pantry> pantries = new ArrayList<>();
        userDao.updatePantryValue(gson.toJson(pantries));
    }

    public void deleteIngredientsPantry(){
        pantryDao.deleteAll();
    }

    public List<Ingredient> getAllPantryIngredients() {
        List<Pantry> pantries = pantryDao.getPantryIngredients();
        List<Integer> integers = new ArrayList<>();

        for (Pantry pantry : pantries) {
            integers.add(pantry.getIngredientId());
        }

        return ingredientDao.getIngredientsById(integers);
    }

    public void removeIngredientFromPanty(int ingredientId) {
        pantryDao.remove(ingredientId);
    }

    public void savePantryToUserDB() {
        List<Pantry> pantries = pantryDao.getPantryIngredients();
        Gson gson = new Gson();
        gson.toJson(pantries);
        userDao.updatePantryValue(gson.toJson(pantries));
    }

    public void addIngredientToPantry(Pantry pantry) {
        pantryDao.addIngredients(pantry);
    }


    //</editor-fold>

}