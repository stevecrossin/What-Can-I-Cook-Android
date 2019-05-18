package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Intolerances database - definition of data model as it will be saved and handled in the database schema
 */
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

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

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

    public int getIntoleranceID() {
        return intoleranceID;
    }

    public Intolerance(String intoleranceName, String ingredientName) {
        this.intoleranceName = intoleranceName;
        this.ingredientName = ingredientName;
        this.intoleranceSelected = false;
    }
}