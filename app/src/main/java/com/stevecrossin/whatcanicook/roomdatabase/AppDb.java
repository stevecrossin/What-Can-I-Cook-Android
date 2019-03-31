package com.stevecrossin.whatcanicook.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.IngredientDao;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.entities.UserDao;
import com.stevecrossin.whatcanicook.screens.Intolerances;
import com.stevecrossin.whatcanicook.screens.Logs;
import com.stevecrossin.whatcanicook.screens.Pantry;
import com.stevecrossin.whatcanicook.screens.Recipes;


@Database(entities = {Ingredient.class, Intolerance.class, User.class}, version = 1, exportSchema = false)
 public abstract class AppDb extends RoomDatabase {

    public abstract IngredientDao ingredientDao();

    public abstract IntoleranceDao intoleranceDao();

    public abstract UserDao userDao();

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
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}