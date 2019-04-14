package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RecipeIngredientsTotal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    @ColumnInfo(name = "total_ingredients")
    private int totalIngredient;

    public RecipeIngredientsTotal(String recipeName, int totalIngredient) {
        this.recipeName = recipeName;
        this.totalIngredient = totalIngredient;
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getTotalIngredient() {
        return totalIngredient;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setTotalIngredient(int totalIngredient) {
        this.totalIngredient = totalIngredient;
    }
}
