package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.stevecrossin.whatcanicook.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IngredientDao {
    /*
    @Update()
    void excludeIngredient(IngredientDao ingredientDao);
    */

    @Query("SELECT * FROM ingredient;")
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM ingredient WHERE ingredient_category = :ingredientCategory;")
    List<Ingredient> getIngredientsByCategory(String ingredientCategory);

    @Query("SELECT DISTINCT ingredient_category FROM ingredient;")
    List<String> getAllCategories();

    @Insert
    void addIngredients(ArrayList<Ingredient> ingredients);

    @Insert
    void addIngredient(Ingredient ingredient);
}
