package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Pantry {

    @PrimaryKey
    @ColumnInfo(name = "ingredient_id")
    private int ingredientId;

    public Pantry(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

}