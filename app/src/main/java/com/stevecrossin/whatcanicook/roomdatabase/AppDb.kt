package com.stevecrossin.whatcanicook.roomdatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.stevecrossin.whatcanicook.entities.*

/**
 * Declaration of entities that that exist in the Room Database, and the version number of the database. This version number needs to be updated every time the underlying code for database entities is modified, or the application
 * may crash
 */
@Database(entities = [Ingredient::class, Intolerance::class, User::class, Recipe::class, RecipeIngredients::class, RecipeIngredientsTotal::class, LogRecords::class, Pantry::class], version = 9, exportSchema = false)
abstract class AppDb : RoomDatabase() {

    abstract fun ingredientDao(): IngredientDao

    abstract fun intoleranceDao(): IntoleranceDao

    abstract fun userDao(): UserDao

    abstract fun recipeDao(): RecipeDao

    abstract fun recipeIngredientsDao(): RecipeIngredientsDao

    abstract fun recipeIngredientsTotalDao(): RecipeIngredientsTotalDao

    abstract fun logDao(): LogDao

    abstract fun pantryDao(): PantryDao

    companion object {

        private var INSTANCE: AppDb? = null

        /**
         * Run room DB as singluar instance. This ensures the ability to access Room as a single instance throughout the lifespan of the application.
         */
        internal fun getDatabase(context: Context): AppDb {
            if (INSTANCE == null) {
                synchronized(AppDb::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<AppDb>(context.applicationContext,
                                AppDb::class.java, "app_database")
                                .fallbackToDestructiveMigration()
                                .build()

                    }
                }
            }
            return INSTANCE
        }
    }
}