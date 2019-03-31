package com.stevecrossin.whatcanicook.roomdatabase;
import android.content.Context;

import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;

import java.util.ArrayList;
import java.util.List;

/**

Initial source code for app data repo - barebones, non functional and still in progress
**/

public class AppDataRepo {
    private IngredientDao ingredientDao;
    private IntoleranceDao intoleranceDao;

    /**
     * private intoleranceDao intoleranceDao;
     * private logsDao logsDao;
     * private pantryDao pantryDao;
     * private recipeDao recipeDao;
     **/

    public AppDataRepo(Context context) {
        ingredientDao = AppDb.getDatabase(context).ingredientDao();
        intoleranceDao = AppDb.getDatabase(context).intoleranceDao();
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

    public List<String> getIngredientsByIntolerance(String intolerance) {
        return intoleranceDao.getIngredientsByIntolerance(intolerance);
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

    public void insertIntolerances(ArrayList<Intolerance> intolerances){
        intoleranceDao.addIntolerances(intolerances);
    }
}
