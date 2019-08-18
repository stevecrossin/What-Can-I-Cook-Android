package com.stevecrossin.whatcanicook.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Pantry database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
public class Pantry {

    @PrimaryKey
    @ColumnInfo(name = "ingredient_id")
    private int ingredientId;

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public Pantry(int ingredientId) {
        this.ingredientId = ingredientId;
    }


}