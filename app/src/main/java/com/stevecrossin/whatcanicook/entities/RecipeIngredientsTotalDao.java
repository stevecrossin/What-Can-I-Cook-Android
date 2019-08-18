package com.stevecrossin.whatcanicook.entities;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeIngredientsTotalDao {

    /**
     * Get all recipe ingredients from the recipeIngredientsTotal database.
     */
    @Query("SELECT * FROM recipeingredientstotal;")
    List<RecipeIngredientsTotal> getAllRecipesAndIngredientsTotal();

    /**
     * Adds a list of recipe ingredients from an array into the recipeIngredientsTotal database
     */
    @Insert
    void addRecipeIngredientsTotal(ArrayList<RecipeIngredientsTotal> recipeIngredientsTotals);
}
