package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PantryDao {

    /**
     * Select all ingredients in the pantry DB.
     */
    @get:Query("SELECT * FROM pantry;")
    val pantryIngredients: List<Pantry>

    /**
     * Deletes all records in the pantry DB.
     */
    @Query("DELETE FROM pantry;")
    fun deleteAll()

    /**
     * Insert ingredients into the pantry DB
     */
    @Insert
    fun addIngredients(pantries: Pantry)

    /**
     * Deletes records in the pantry based on the ingredientID.
     */
    @Query("DELETE FROM pantry WHERE ingredient_id = :ingredientId;")
    fun remove(ingredientId: Int)
}