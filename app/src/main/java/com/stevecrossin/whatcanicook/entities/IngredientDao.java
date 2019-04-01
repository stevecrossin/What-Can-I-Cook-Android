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

    @Query("SELECT * FROM ingredient WHERE ingredient_category = :ingredientCategory AND ingredient_excluded = 0;")
    List<Ingredient> getIngredientsByCategory(String ingredientCategory);

    @Query("UPDATE ingredient SET ingredient_excluded = 1 WHERE ingredient_name = :ingredientName;")
    void excludeIngredient(String ingredientName);

    @Query("UPDATE ingredient SET ingredient_excluded = 0 WHERE ingredient_name = :ingredientName;")
    void includeIngredient(String ingredientName);

    @Query("SELECT DISTINCT ingredient_category FROM ingredient;")
    List<String> getAllCategories();

    @Insert
    void addIngredients(ArrayList<Ingredient> ingredients);

    @Insert
    void addIngredient(Ingredient ingredient);

    @Query("DELETE FROM ingredient;")
    void deleteAll();
}
