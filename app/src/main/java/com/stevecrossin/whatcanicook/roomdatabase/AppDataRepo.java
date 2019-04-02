package com.stevecrossin.whatcanicook.roomdatabase;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.IngredientDao;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.entities.RecipeDao;
import com.stevecrossin.whatcanicook.entities.RecipeIngredients;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsDao;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.entities.UserDao;


import java.util.ArrayList;
import java.util.List;

/**

Initial source code for app data repo - barebones, non functional and still in progress
**/

public class AppDataRepo {
    private IngredientDao ingredientDao;
    private IntoleranceDao intoleranceDao;
    private UserDao userDao;
    private RecipeDao recipeDao;
    private RecipeIngredientsDao recipeIngredientsDao;

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

        //intoleranceDao = AppDb.getDatabase(context).intoleranceDao();
        //logsDao = AppDb.getDatabase(context).logsDao();
        //pantryDao = AppDb.getDatabase(context).pantryDao();
        //recipeDao = AppDb.getDatabase(context).recipeDao();

    }

    public List<Ingredient> getAllIngredients() {
        return ingredientDao.getAllIngredients();
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

    public List<String> getAllCategories() {
        return ingredientDao.getAllCategories();
    }

    public List<Intolerance> getAllIntolerances() {
        return intoleranceDao.getAllIntolerances();
    }

    public boolean haveIngredient(){
        List<Ingredient> ingredients = ingredientDao.getAllIngredients();
        if (ingredients.size() > 0)
            return true;
        return false;
    }

    public boolean haveIntolerance(){
        List<Intolerance> intolerances = intoleranceDao.getAllIntolerances();
        if (intolerances.size() > 0)
            return true;
        return false;
    }

    public void insertIngredients(ArrayList<Ingredient> ingredients){
        ingredientDao.addIngredients(ingredients);
    }


    public void insertIntolerance(Intolerance intolerance) {
        intoleranceDao.addIntolerance(intolerance);
    }

    public void deleteAllIngredient() {
        ingredientDao.deleteAll();
    }

    public void deleteAllIntolerance() {
        intoleranceDao.deleteAll();
    }

    public void excludeIngredient(String ingredientName){
        ingredientDao.excludeIngredient(ingredientName);
    }

    public void includeIngredient(String ingredientName){
        ingredientDao.includeIngredient(ingredientName);
    }

    public List<Recipe> getAllRecipes(){
        return recipeDao.getAllRecipes();
    }

    public List<Recipe> getRecipesByName(String recipeName){
        return recipeDao.getRecipesByName(recipeName);
    }

    public void insertRecipe(ArrayList<Recipe> recipes){
        recipeDao.addRecipes(recipes);
    }

    public void insertRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients){
        recipeIngredientsDao.addRecipeIngredients(recipeIngredients);
    }

    public List<RecipeIngredients> getAllRecipesAndIngredients(){
        return recipeIngredientsDao.getAllRecipesAndIngredients();
    }


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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDao.insertUser(user);
                return null;
            }
        }
                .execute();

    }
}
