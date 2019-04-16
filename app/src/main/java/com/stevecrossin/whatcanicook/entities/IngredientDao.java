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

    @Query("SELECT * FROM ingredient WHERE ingredient_name = :name;")
    List<Ingredient> getIngredientsByName(String name);

    @Query("SELECT * FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0;")
    List<Ingredient> getAllCheckedIngredients();

    @Query("UPDATE ingredient SET ingredient_selected = 0 WHERE ingredient_selected = 1;")
    void clearIngredients();

    @Query("UPDATE ingredient SET ingredient_excluded = 1 WHERE ingredient_name = :ingredientName;")
    void excludeIngredient(String ingredientName);

    @Query("UPDATE ingredient SET ingredient_excluded = 0 WHERE ingredient_name = :ingredientName;")
    void includeIngredient(String ingredientName);

    @Query("UPDATE ingredient SET ingredient_selected = 1 WHERE ingredient_name = :ingredientName;")
    void selectIngredient(String ingredientName);

    @Query("UPDATE ingredient SET ingredient_selected = 0 WHERE ingredient_name = :ingredientName;")
    void deselectIngredient(String ingredientName);

    @Query("SELECT *  FROM ingredient GROUP By ingredient_category;")
    List<Ingredient> getAllCategories();

    @Insert
    void addIngredients(ArrayList<Ingredient> ingredients);

    @Insert
    void addIngredient(Ingredient ingredient);

    @Query("DELETE FROM ingredient;")
    void deleteAll();

}