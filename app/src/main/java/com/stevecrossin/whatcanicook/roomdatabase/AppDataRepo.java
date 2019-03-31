package com.stevecrossin.whatcanicook.roomdatabase;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.IngredientDao;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.entities.UserDao;

import java.util.ArrayList;
import java.util.List;

/**

Initial source code for app data repo - barebones, non functional and still in progress
**/

public class AppDataRepo {
    private IngredientDao ingredientDao;
    private UserDao userDao;

    /**
     * private intoleranceDao intoleranceDao;
     * private logsDao logsDao;
     * private pantryDao pantryDao;
     * private recipeDao recipeDao;
     **/

    public AppDataRepo(Context context) {
        ingredientDao = AppDb.getDatabase(context).ingredientDao();
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

    public List<String> getAllCategories() {
        return ingredientDao.getAllCategories();
    }

    public boolean haveIngredient(){
        List<Ingredient> ingredients = ingredientDao.getAllIngredients();
        if (ingredients.size() > 0)
            return true;
        return false;
    }

    public void insertIngredients(ArrayList<Ingredient> ingredients){
        ingredientDao.addIngredients(ingredients);
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
