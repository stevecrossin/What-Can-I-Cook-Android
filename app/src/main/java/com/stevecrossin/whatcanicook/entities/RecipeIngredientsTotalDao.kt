package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface RecipeIngredientsTotalDao {

    /**
     * Get all recipe ingredients from the recipeIngredientsTotal database.
     */
    @get:Query("SELECT * FROM recipeingredientstotal;")
    val allRecipesAndIngredientsTotal: List<RecipeIngredientsTotal>

    /**
     * Adds a list of recipe ingredients from an array into the recipeIngredientsTotal database
     */
    @Insert
    fun addRecipeIngredientsTotal(recipeIngredientsTotals: ArrayList<RecipeIngredientsTotal>)
}
