package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Recipe database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
public class Recipe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    @ColumnInfo(name = "recipe_image")
    private String recipeImage;

    @ColumnInfo(name = "recipe_ingredients")
    private String recipeIngredients;

    @ColumnInfo(name = "recipe_steps")
    private String recipeSteps;

    @ColumnInfo(name = "recipe_excluded")
    private int recipeExcluded = 0;

    @ColumnInfo(name = "recipe_saved")
    private boolean isSaved;

    @ColumnInfo(name = "recipe_custom")
    private boolean isCustomed;

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public String getRecipeSteps() {
        return recipeSteps;
    }

    void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipeSteps(String recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    boolean isCustomed() {
        return isCustomed;
    }

    public void setCustomed(boolean customed) {
        isCustomed = customed;
    }

    int getRecipeExcluded() {
        return recipeExcluded;
    }

    void setRecipeExcluded(int recipeExcluded) {
        this.recipeExcluded = recipeExcluded;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public Recipe(String recipeName, String recipeImage, String recipeIngredients, String recipeSteps) {
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
    }
}