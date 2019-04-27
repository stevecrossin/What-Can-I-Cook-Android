package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Pantry {

    @PrimaryKey
    @ColumnInfo(name = "pantry_id")
    private int pantryIngID;

    @ColumnInfo(name = "pantry_ingredient")
    private String pantryIngredient;


    public Pantry(int pantryIngID, String pantryIngredient) {
        this.pantryIngID = pantryIngID;
        this.pantryIngredient = pantryIngredient;

    }

    public int getPantryIngID() {
        return pantryIngID;
    }

    public void setPantryIngID(int pantryIngID) {
        this.pantryIngID = pantryIngID;
    }

    public String getPantryIngredient() {
        return pantryIngredient;
    }

    public void setPantryIngredient(String pantryIngredient) {
        this.pantryIngredient = pantryIngredient;
    }
}