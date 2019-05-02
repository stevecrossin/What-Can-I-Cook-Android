package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe WHERE recipe_custom = 0;")
    List<Recipe> getAllDefaultRecipes();

    @Query("SELECT * FROM recipe WHERE recipe_saved = 1;")
    List<Recipe> getAllSavedRecipes();


    @Query("SELECT Recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            " WHERE recipeingredients.recipe_ingredients IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0) \n" +
            " GROUP BY recipeingredients.recipe_name ORDER BY count(recipeingredients.recipe_name) DESC;")
    List<Recipe> getAllRecipesByCheckedIngredients();

    @Query("SELECT Recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            " WHERE recipeingredients.recipe_ingredients IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0) \n" +
            " GROUP BY recipeingredients.recipe_name ORDER BY count(recipeingredients.recipe_name) DESC LIMIT :limit;")
    List<Recipe> getAllRecipesByCheckedIngredientsWithLimit(int limit);

    @Query("SELECT recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            "JOIN recipeingredientstotal ON recipeingredients.recipe_name = recipeingredientstotal.recipe_name\n" +
            "WHERE recipeingredients.recipe_ingredients IN\n" +
            "(SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0)\n" +
            "GROUP BY recipeingredients.recipe_name \n" +
            "HAVING RecipeIngredientsTotal.total_ingredients = count(recipeingredients.recipe_name)\n" +
            "ORDER BY count(recipeingredients.recipe_name) DESC;")
    List<Recipe> getAllRecipesByCheckedIngredientsWithExactMatch();

    @Query(" SELECT count(recipe_name) FROM recipeingredients\n" +
            " WHERE recipeingredients.recipe_ingredients IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0) \n" +
            " AND recipe_name = :name\n" +
            " GROUP BY recipeingredients.recipe_name ORDER BY count(recipe_name) DESC;")
    List<Integer> getNumberOfMissingIngredientsByName(String name);

    @Query(" SELECT recipe_ingredients FROM RecipeIngredients\n" +
            " WHERE recipe_name = :name\n" +
            " AND recipeingredients.recipe_ingredients NOT IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0) ;")
    List<String> getMissingIngredientsByName(String name);

    @Query(" SELECT recipe_ingredients FROM RecipeIngredients\n" +
            " WHERE recipe_name = :name\n" +
            " AND recipeingredients.recipe_ingredients NOT IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1 AND ingredient_excluded = 0) LIMIT :limit;")
    List<String> getMissingIngredientsByNameWithLimit(String name, int limit);

    @Query("SELECT * FROM recipe WHERE recipe_name = :recipeName;")
    List<Recipe> getRecipesByName(String recipeName);

    @Query("SELECT * FROM recipe WHERE recipe_name IN (:recipeNames);")
    List<Recipe> getRecipesByNames(List<String> recipeNames);

    @Query("UPDATE recipe SET recipe_saved = 1 WHERE recipe_id = :recipeId;")
    void saveRecipe(int recipeId);

    @Insert
    void addRecipes(ArrayList<Recipe> recipes);

    @Insert
    void addRecipe(Recipe recipe);

    @Query("DELETE FROM recipe;")
    void deleteAll();

}
