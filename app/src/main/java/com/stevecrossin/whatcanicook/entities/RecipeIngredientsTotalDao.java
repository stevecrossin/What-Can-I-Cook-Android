package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeIngredientsTotalDao {

    @Query("SELECT * FROM recipeingredientstotal;")
    List<RecipeIngredientsTotal> getAllRecipesAndIngredientsTotal();

    @Insert
    void addRecipeIngredientsTotal(ArrayList<RecipeIngredientsTotal> recipeIngredientsTotals);
}
