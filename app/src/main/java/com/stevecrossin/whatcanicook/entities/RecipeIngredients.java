package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Recipe Ingredients database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
public class RecipeIngredients {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    @ColumnInfo(name = "recipe_image")
    private String recipeImage;

    @ColumnInfo(name = "recipe_ingredients")
    private String recipeIngredients;

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public RecipeIngredients(String recipeName, String recipeImage, String recipeIngredients) {
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeIngredients = recipeIngredients;
    }
}
