package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe;")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipe WHERE recipe_name = :recipeName;")
    List<Recipe> getRecipesByName(String recipeName);

    @Insert
    void addRecipes(ArrayList<Recipe> recipes);

    @Query("DELETE FROM recipe;")
    void deleteAll();

}
