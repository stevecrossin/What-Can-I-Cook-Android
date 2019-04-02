package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Ingredient {
    @PrimaryKey
    @ColumnInfo(name = "ingredient_id")
    private int ingredientID;
    @ColumnInfo(name = "ingredient_category")
    private String ingredientCategory;
    @ColumnInfo(name = "ingredient_subcat")
    private String ingredientSubCat;
    @ColumnInfo(name = "ingredient_name")
    private String ingredientName;
    @ColumnInfo(name = "ingredient_alternative")
    private String ingredientAlternative;
    @ColumnInfo(name = "ingredient_excluded")
    private boolean ingredientExcluded = false;
    @ColumnInfo(name = "ingredient_selected")
    private boolean ingredientSelected = false;


    public Ingredient(int ingredientID, String ingredientCategory, String ingredientSubCat, String ingredientName, String ingredientAlternative) {
        this.ingredientID = ingredientID;
        this.ingredientCategory = ingredientCategory;
        this.ingredientSubCat = ingredientSubCat;
        this.ingredientName = ingredientName;
        this.ingredientAlternative = ingredientAlternative;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public String getIngredientCategory() {
        return ingredientCategory;
    }

    public String getIngredientSubCat() {
        return ingredientSubCat;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getIngredientAlternative() {
        return ingredientAlternative;
    }


    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public void setIngredientCategory(String ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }

    public void setIngredientSubCat(String ingredientSubCat) {
        this.ingredientSubCat = ingredientSubCat;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setIngredientAlternative(String ingredientAlternative) {
        this.ingredientAlternative = ingredientAlternative;
    }

    public boolean isIngredientExcluded() {
        return ingredientExcluded;
    }

    public void setIngredientExcluded(boolean ingredientExcluded) {
        this.ingredientExcluded = ingredientExcluded;
    }

    public boolean isIngredientSelected() {
        return ingredientSelected;
    }

    public void setIngredientSelected(boolean ingredientSelected) {
        this.ingredientSelected = ingredientSelected;
    }
}
