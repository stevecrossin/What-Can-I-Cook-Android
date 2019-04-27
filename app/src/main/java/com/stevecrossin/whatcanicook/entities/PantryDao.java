package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PantryDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertIngredient(User user);

    @Query("SELECT * FROM pantry;")
    List<Pantry> getPantryIngredients();

    @Query("DELETE FROM ingredient;")
    void clearPantry();

}