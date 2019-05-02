package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PantryDao {

    @Query("SELECT * FROM pantry;")
    List<Pantry> getPantryIngredients();

    @Query("DELETE FROM pantry;")
    void deleteAll();

    @Insert
    void addIngredients(Pantry pantries);

    @Query("DELETE FROM pantry WHERE ingredient_id = :ingredientId;")
    void remove(int ingredientId);
}