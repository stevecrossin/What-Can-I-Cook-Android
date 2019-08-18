package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {
    /**
     * Get all recipes in the database that are NOT custom recipes, and return this result
     */
    @Query("SELECT * FROM recipe WHERE recipe_custom = 0;")
    List<Recipe> getAllDefaultRecipes();

    /**
     * Get all recipes in the database that have been saved, and return this result
     */
    @Query("SELECT * FROM recipe WHERE recipe_saved = 1;")
    List<Recipe> getAllSavedRecipes();

    /**
     * Get all the recipes that includes the ingredients checked by the user
     * This is a nested, complicated and joined SQL queries using 3 tables: recipe, recipeIngredients and ingredient
     * <p>
     * It first checks in the recipeingredients table to get all of the rows where the recipe_ingredients is contained in the list of checked ingredients (those that the user noted they have)
     * <p>
     * For all the rows returned from recipeingredients, it groups the rows by the recipe_name and order the result in a descending way.
     * (So we will have a list of recipe name ordered by how many times it appears in the result - this denotes how many similar ingredients in a recipe might have in relates to what the user has)
     * <p>
     * After that, it will join the recipe table to get the Recipe object instead of the RecipeIngredients
     * There is a filter recipe_excluded to make sure that those returned recipe will not conflict with the intolerance list.
     * It will then return the list of recipes
     */
    @Query("SELECT Recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            " WHERE recipeingredients.recipe_ingredients IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1) \n" +
            " AND recipe.recipe_excluded = 0 \n" +
            " GROUP BY recipeingredients.recipe_name ORDER BY count(recipeingredients.recipe_name) DESC;")
    List<Recipe> getAllRecipesByCheckedIngredients();

    /**
     * Get all the recipes that includes the ingredients checked by the user
     * This is a nested, complicated and joined SQL queries using 3 tables: recipe, recipeIngredients and ingredient
     * <p>
     * It first checks in the recipeingredients table to get all of the rows where the recipe_ingredients is contained in the list of checked ingredients (those that the user noted they have)
     * <p>
     * For all the rows returned from recipeingredients, it groups the rows by the recipe_name and order the result in a descending way.
     * (So we will have a list of recipe name ordered by how many times it appears in the result - this denotes how many similar ingredients in a recipe might have in relates to what the user has)
     * <p>
     * An offset is given to specify the row from which we return the result. The rest is skipped
     * For example, if offset = 0, return the result from the first row. If offset = 5, return the result from the sixth row, skip the first 5 rows.
     * After that, it will join the recipe table to get the Recipe object instead of the RecipeIngredients
     * There is a filter recipe_excluded to make sure that those returned recipe will not conflict with the intolerance list.
     *
     * @param offset: the row position where the query return
     *                It will then return the list of recipes
     */
    @Query("SELECT Recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            " WHERE recipeingredients.recipe_ingredients IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1) \n" +
            " AND recipe.recipe_excluded = 0 \n" +
            " GROUP BY recipeingredients.recipe_name ORDER BY count(recipeingredients.recipe_name) DESC LIMIT 100 OFFSET :offset;")
    List<Recipe> getAllRecipesByCheckedIngredientsWithOffset(int offset);

    /**
     * Get all the recipes that includes the ingredients checked by the user
     * This is a nested, complicated and joined SQL queries using 3 tables: recipe, recipeIngredients and ingredient
     * <p>
     * It first checks in the recipeingredients table to get all of the rows where the recipe_ingredients is contained in the list of checked ingredients (those that the user noted they have)
     * <p>
     * For all the rows returned from recipeingredients, it groups the rows by the recipe_name and order the result in a descending way.
     * (So we will have a list of recipe name ordered by how many times it appears in the result - this denotes how many similar ingredients in a recipe might have in relates to what the user has)
     * <p>
     * A limit is given to set how many recipe will be returned. If limit is 10, results will only show the first 10 rows.
     * <p>
     * After that, it will join the recipe table to get the Recipe object instead of the RecipeIngredients
     * There is a filter recipe_excluded to make sure that those returned recipe will not conflict with the intolerance list.
     * <p>
     * It will then return the list of recipes
     */
    @Query("SELECT Recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            " WHERE recipeingredients.recipe_ingredients IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1) \n" +
            " AND recipe.recipe_excluded = 0 \n" +
            " GROUP BY recipeingredients.recipe_name ORDER BY count(recipeingredients.recipe_name) DESC LIMIT :limit;")
    List<Recipe> getAllRecipesByCheckedIngredientsWithLimit(int limit);

    /**
     * Get all the recipes that includes the ingredients checked by the user
     * This is a nested, complicated and joined SQL queries using 4 tables: recipe, recipeIngredients, recipeIngredientsTotal and ingredient
     * <p>
     * It first checks in the recipeingredients table to get all of the rows where the recipe_ingredients is contained in the list of checked ingredients (those that the user noted they have)
     * For all the rows returned from recipeingredients, it groups the rows by the recipe_name and order the result in a descending way.
     * <p>
     * (So we will have a list of recipe name ordered by how many times it appears in the result - this denotes how many similar ingredients in a recipe might have in relates to what the user has)
     * <p>
     * After that, it will join the recipe table based on recipe name to get the Recipe object instead of the RecipeIngredients
     * There is a filter recipe_excluded to make sure that those returned recipe will not conflict with the intolerance list.
     * <p>
     * It will then join again on the recipeIngredientsTotal based on recipeName with a condition that the count of recipe_name must be the same as total_ingredients
     * Note that the condition is not the WHERE clause but using the HAVING clause (specific use for aggregate function).
     * <p>
     * It will then return the list of recipes
     */
    @Query("SELECT recipe.* FROM recipeingredients\n" +
            "JOIN recipe ON recipeingredients.recipe_name = Recipe.recipe_name\n" +
            "JOIN recipeingredientstotal ON recipeingredients.recipe_name = recipeingredientstotal.recipe_name\n" +
            "WHERE recipeingredients.recipe_ingredients IN\n" +
            "(SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1)\n" +
            " AND recipe.recipe_excluded = 0 \n" +
            "GROUP BY recipeingredients.recipe_name \n" +
            "HAVING RecipeIngredientsTotal.total_ingredients = count(recipeingredients.recipe_name)\n" +
            "ORDER BY count(recipeingredients.recipe_name) DESC;")
    List<Recipe> getAllRecipesByCheckedIngredientsWithExactMatch();

    /**
     * Get all of the missing ingredients given a recipe name
     * This method is a nested query using 2 tables: RecipeIngredients and ingredient table
     * It will first find all of the ingredients that the user has checked in the ingredients table.
     * In the RecipeIngredients table, it will then find all of the recipe_ingredients that is NOT included from the above list.
     * There is a filter applied on recipe_name, so the rows returned belong only to that specific recipe.
     * Lastly, select ONLY recipe_ingredients as string and return back. (not using the '*')
     * <p>
     * Returns the list of ingredient names as a string
     */
    @Query(" SELECT recipe_ingredients FROM RecipeIngredients\n" +
            " WHERE recipe_name = :name\n" +
            " AND recipeingredients.recipe_ingredients NOT IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1) ;")
    List<String> getMissingIngredientsByName(String name);

    /**
     * Get all of the missing ingredients given a recipe name and a limit
     * This method is a nested query using 2 tables: RecipeIngredients and ingredient table
     * It will first find all of the ingredients that the user has checked in the ingredient table.
     * In the RecipeIngredients table, it again find all of the recipe_ingredients that is NOT include from the above list.
     * There is a filter applied on recipe_name, so the rows returned belong only to that specific recipe.
     * There is also applied a filter on the number of rows returned with the limit param.
     * Lastly, select ONLY recipe_ingredients as string and return back. (not using the '*')
     * <p>
     * Returns the list of ingredient names as a string
     */
    @Query(" SELECT recipe_ingredients FROM RecipeIngredients\n" +
            " WHERE recipe_name = :name\n" +
            " AND recipeingredients.recipe_ingredients NOT IN \n" +
            " (SELECT ingredient_name FROM ingredient WHERE ingredient_selected = 1) LIMIT :limit;")
    List<String> getMissingIngredientsByNameWithLimit(String name, int limit);

    /**
     * Get all the recipes with a given name. Returns the list of recipes that match the query
     */
    @Query("SELECT * FROM recipe WHERE recipe_name = :recipeName;")
    List<Recipe> getRecipesByName(String recipeName);

    /**
     * Save a given recipe by setting the recipe_saved to 1
     *
     * @param recipeId: input to know what recipe to save
     */
    @Query("UPDATE recipe SET recipe_saved = 1 WHERE recipe_id = :recipeId;")
    void saveRecipe(int recipeId);

    /**
     * Un-Save a given recipe by setting the recipe_saved to 0
     *
     * @param recipeId: input to know what recipe to unsave
     */
    @Query("UPDATE recipe SET recipe_saved = 0 WHERE recipe_id = :recipeId;")
    void unSaveRecipe(int recipeId);

    /**
     * This query is a core implementation amongst the intolerance feature.
     * Given an ingredient name, it will mark every recipe that contain the ingredient as "excluded"
     * If more than one intolerance is turned on, a recipe can be "excluded" more than once.
     * The query makes use of the recipeingredients table to find all the recipes that includes a given ingredient.
     * In the recipe table, It will then increment the value of recipe_excluded by 1 for all the recipes contained in the above list.
     * Note that recipe_excluded is in integer, not a boolean. This allows recipes to be excluded many times if many intolerance mode is turned on simultaneously.
     */
    @Query("UPDATE Recipe SET recipe_excluded = recipe_excluded + 1 WHERE\n" +
            "Recipe.recipe_name IN (SELECT recipe_name FROM recipeingredients\n" +
            "WHERE recipeingredients.recipe_ingredients = :ingredientName)")
    void excludeRecipe(String ingredientName);

    /**
     * This query is a core implementation amongst the intolerance feature.
     * Given an ingredient name, it will mark every recipe that contains the ingredient as "included"
     * If more than one intolerance is turned OFF, a recipe can be "included" more than once.
     * The query makes use of the recipeIngredients table to find all the recipes that includes a given ingredient.
     * In the recipe table, it will then decrement the value of recipe_excluded by 1 for all the recipes contained in the above list.
     * Note that recipe_excluded is in integer, not a boolean. This allows recipes to be included many times if many intolerance mode is turned OFF simultaneously.
     */
    @Query("UPDATE Recipe SET recipe_excluded = recipe_excluded - 1 WHERE\n" +
            "Recipe.recipe_name IN (SELECT recipe_name FROM recipeingredients\n" +
            "WHERE recipeingredients.recipe_ingredients = :ingredientName)")
    void includeRecipe(String ingredientName);

    /**
     * Add a list of recipes to the recipes database
     */
    @Insert
    void addRecipes(ArrayList<Recipe> recipes);

    /**
     * Adds a single recipe to the recipes database
     */
    @Insert
    void addRecipe(Recipe recipe);

    /**
     * Delete a recipe from the database, based on the given recipe ID
     */
    @Query("DELETE FROM recipe WHERE recipe_id = :recipeId")
    void deleteRecipe(int recipeId);


    /**
     * Delete all intolerances from the database
     */
    @Query("DELETE FROM recipe;")
    void deleteAll();


}