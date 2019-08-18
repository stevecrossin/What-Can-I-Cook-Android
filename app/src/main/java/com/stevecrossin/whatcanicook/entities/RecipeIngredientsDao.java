package com.stevecrossin.whatcanicook.entities;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeIngredientsDao {

    /**
     * Get all recipe ingredients from the recipeIngredients database.
     */
    @Query("SELECT * FROM recipeingredients;")
    List<RecipeIngredients> getAllRecipesAndIngredients();

    /**
     * Adds a list of recipe ingredients from an array into the recipeIngredients database
     */
    @Insert
    void addRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients);
}
