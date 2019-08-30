package com.stevecrossin.whatcanicook.roomdatabase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

/**
 * Declaration of entities that that exist in the Room Database, and the version number of the database. This version number needs to be updated every time the underlying code for database entities is modified, or the application
 * may crash
 */
@Database(entities = {Ingredient.class, Intolerance.class, User.class, Recipe.class, RecipeIngredients.class, RecipeIngredientsTotal.class, LogRecords.class, Pantry.class}, version = 10, exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    public abstract IngredientDao ingredientDao();

    public abstract IntoleranceDao intoleranceDao();

    public abstract UserDao userDao();

    public abstract RecipeDao recipeDao();

    public abstract RecipeIngredientsDao recipeIngredientsDao();

    public abstract RecipeIngredientsTotalDao recipeIngredientsTotalDao();

    public abstract LogDao logDao();

    public abstract PantryDao pantryDao();

    private static AppDb INSTANCE;

    /**
     * Run room DB as singluar instance. This ensures the ability to access Room as a single instance throughout the lifespan of the application.
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