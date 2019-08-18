package com.stevecrossin.whatcanicook.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Recipe Ingredients Total database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
public class RecipeIngredientsTotal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    @ColumnInfo(name = "total_ingredients")
    private int totalIngredient;


    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

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

    public int getId() {
        return id;
    }

    public RecipeIngredientsTotal(String recipeName, int totalIngredient) {
        this.recipeName = recipeName;
        this.totalIngredient = totalIngredient;
    }
}
