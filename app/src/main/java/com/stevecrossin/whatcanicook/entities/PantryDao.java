package com.stevecrossin.whatcanicook.entities;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PantryDao {

    /**
     * Select all ingredients in the pantry DB.
     */
    @Query("SELECT * FROM pantry;")
    List<Pantry> getPantryIngredients();

    /**
     * Deletes all records in the pantry DB.
     */
    @Query("DELETE FROM pantry;")
    void deleteAll();

    /**
     * Insert ingredients into the pantry DB
     */
    @Insert
    void addIngredients(Pantry pantries);

    /**
     * Deletes records in the pantry based on the ingredientID.
     */
    @Query("DELETE FROM pantry WHERE ingredient_id = :ingredientId;")
    void remove(int ingredientId);
}