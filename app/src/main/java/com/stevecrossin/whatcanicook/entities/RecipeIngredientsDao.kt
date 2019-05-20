package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface RecipeIngredientsDao {

    /**
     * Get all recipe ingredients from the recipeIngredients database.
     */
    @get:Query("SELECT * FROM recipeingredients;")
    val allRecipesAndIngredients: List<RecipeIngredients>

    /**
     * Adds a list of recipe ingredients from an array into the recipeIngredients database
     */
    @Insert
    fun addRecipeIngredients(recipeIngredients: ArrayList<RecipeIngredients>)
}
