package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IngredientDao {
    /**
     * Get all ingredient from the database
     */
    @Query("SELECT * FROM ingredient;")
    List<Ingredient> getAllIngredients();

    /**
     * Get all ingredients from the database within a given category, where the ingredient is not excluded
     */
    @Query("SELECT * FROM ingredient WHERE ingredient_category = :ingredientCategory AND ingredient_excluded = 0;")
    List<Ingredient> getIngredientsByCategory(String ingredientCategory);

    /**
     * Get the ingredient by its name.
     */
    @Query("SELECT * FROM ingredient WHERE ingredient_name = :name;")
    List<Ingredient> getIngredientsByName(String name);

    /**
     * Get the ingredients that is selected by the user that is not excluded from selection
     */
    @Query("SELECT * FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0;")
    List<Ingredient> getAllCheckedIngredients();

    /**
     * Clear all the selected ingredients. This resets the value of ingredientSelected from 1 (true) to 0 (false)
     */
    @Query("UPDATE ingredient SET ingredient_selected = 0 WHERE ingredient_selected = 1;")
    void clearIngredients();

    /**
     * Exclude a given ingredient defined by name, by setting ingredient_excluded to 1.
     */
    @Query("UPDATE ingredient SET ingredient_excluded = 1 WHERE ingredient_name = :ingredientName;")
    void excludeIngredient(String ingredientName);

    /**
     * Exclude a given ingredient defined by name, by setting ingredient_excluded to 0.
     */
    @Query("UPDATE ingredient SET ingredient_excluded = 0 WHERE ingredient_name = :ingredientName;")
    void includeIngredient(String ingredientName);

    /**
     * Set the ingredient_selected to 1 for a given ingredient which is defined by name
     */
    @Query("UPDATE ingredient SET ingredient_selected = 1 WHERE ingredient_name = :ingredientName;")
    void selectIngredient(String ingredientName);

    /**
     * Set the ingredient_excluded to 0 for a given ingredient which is defined by name
     */
    @Query("UPDATE ingredient SET ingredient_selected = 0 WHERE ingredient_name = :ingredientName;")
    void deselectIngredient(String ingredientName);

    /**
     * Get all of the ingredients in the database and group them by the ingredient category
     */
    @Query("SELECT *  FROM ingredient GROUP By ingredient_category;")
    List<Ingredient> getAllCategories();

    /**
     * Add all the given ingredients in the ArrayList to the database
     */
    @Insert
    void addIngredients(ArrayList<Ingredient> ingredients);

    /**
     * Delete all ingredients in the database
     */
    @Query("DELETE FROM ingredient;")
    void deleteAll();

    /**
     * Get all of the ingredient where they are not excluded (an intolerance)
     */
    @Query("SELECT *  FROM ingredient WHERE ingredient_excluded = 0")
    List<Ingredient> getAllTolerantIngredient();

    /**
     * Get all ingredients from the database based on its ingredient ID
     */
    @Query("SELECT * FROM ingredient where ingredient_id IN(:ingredientIds)")
    List<Ingredient> getIngredientsById(List<Integer> ingredientIds);


}