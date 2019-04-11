package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Intolerance {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "intolerance_id")
    private int intoleranceID;

    @ColumnInfo(name = "intolerance_name")
    private String intoleranceName;

    @ColumnInfo(name = "ingredient_name")
    private String ingredientName;

    @ColumnInfo(name = "intolerance_selected")
    private boolean intoleranceSelected;

    public Intolerance(String intoleranceName, String ingredientName) {
        this.intoleranceName = intoleranceName;
        this.ingredientName = ingredientName;
        this.intoleranceSelected = false;
    }

    public int getIntoleranceID() {
        return intoleranceID;
    }

    public String getIntoleranceName() {
        return intoleranceName;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIntoleranceID(int intoleranceID) {
        this.intoleranceID = intoleranceID;
    }

    public void setIntoleranceName(String intoleranceName) {
        this.intoleranceName = intoleranceName;
    }

    public void setIngredientName(String ingrdientName) {
        this.ingredientName = ingrdientName;
    }

    public void setIntoleranceSelected(boolean intoleranceSelected) {
        this.intoleranceSelected = intoleranceSelected;
    }

    public boolean isIntoleranceSelected() {
        return intoleranceSelected;
    }
}