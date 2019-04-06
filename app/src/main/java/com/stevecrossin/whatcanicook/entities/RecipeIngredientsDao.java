package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeIngredientsDao {
    @Query("SELECT * FROM recipeingredients;")
    List<RecipeIngredients> getAllRecipesAndIngredients();

    //Some initial code for recipe search
    @Query("SELECT recipe_name FROM recipeingredients WHERE recipe_ingredients IN (:ingredients) GROUP BY recipe_name ORDER BY count(recipe_name) DESC;")
    List<String> getAllRecipesByIngredient(List<String> ingredients);

    @Insert
    void addRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients);
}
