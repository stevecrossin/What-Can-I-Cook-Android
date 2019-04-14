package com.stevecrossin.whatcanicook.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.IngredientDao;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.IntoleranceDao;
import com.stevecrossin.whatcanicook.entities.LogDao;
import com.stevecrossin.whatcanicook.entities.LogRecords;
import com.stevecrossin.whatcanicook.entities.Recipe;
import com.stevecrossin.whatcanicook.entities.RecipeDao;
import com.stevecrossin.whatcanicook.entities.RecipeIngredients;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsDao;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsTotal;
import com.stevecrossin.whatcanicook.entities.RecipeIngredientsTotalDao;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.entities.UserDao;


@Database(entities = {Ingredient.class, Intolerance.class, User.class, Recipe.class, RecipeIngredients.class, RecipeIngredientsTotal.class, LogRecords.class}, version = 6, exportSchema = false)
 public abstract class AppDb extends RoomDatabase {

    public abstract IngredientDao ingredientDao();

    public abstract IntoleranceDao intoleranceDao();

    public abstract UserDao userDao();

    public abstract RecipeDao recipeDao();

    public abstract RecipeIngredientsDao recipeIngredientsDao();

    public abstract RecipeIngredientsTotalDao recipeIngredientsTotalDao();

    public abstract LogDao logDao();

    private static AppDb INSTANCE;

    /**
     * Run room DB as singluar instance
     */

    static AppDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDb.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}