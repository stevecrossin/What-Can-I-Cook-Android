package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

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

    @ColumnInfo(name = "recipe_saved")
    private boolean isSaved;

    @ColumnInfo(name = "recipe_custom")
    private boolean isCustomed;


    public Recipe(String recipeName, String recipeImage, String recipeIngredients, String recipeSteps) {
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public String getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeId(int recipeId) {
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

    public boolean isCustomed() {
        return isCustomed;
    }

    public void setCustomed(boolean customed) {
        isCustomed = customed;
    }
}