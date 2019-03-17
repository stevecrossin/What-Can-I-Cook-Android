package com.stevecrossin.whatcanicook.roomdatabase;
/**
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.stevecrossin.whatcanicook.screens.IngredientPicker;
import com.stevecrossin.whatcanicook.screens.Intolerances;
import com.stevecrossin.whatcanicook.screens.Logs;
import com.stevecrossin.whatcanicook.screens.Pantry;
import com.stevecrossin.whatcanicook.screens.Recipes;

 * Room Database implementation. As I learned in SIT207 how to do this and it is my intended method of storing data in database tables, I will be re-using the tutorials I followed in that unit, as well as reflecting
 * on previous written code and modifying as appropriate
 *
 * Code is currently non functional and is barebones only.
 *


@Database(entities = {IngredientPicker.class, Intolerances.class, Logs.class, Pantry.class, Recipes.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    private static AppDb INSTANCE;



     * Run RoomDatabase as a singluar instance. This is to ensure the ability to access Room as a single instance throughout lifespan.

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
 **/
