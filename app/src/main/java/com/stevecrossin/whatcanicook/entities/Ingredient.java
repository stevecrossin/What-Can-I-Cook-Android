package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Ingredients database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
public class Ingredient {

    @PrimaryKey
    @ColumnInfo(name = "ingredient_id")
    private int ingredientID;

    @ColumnInfo(name = "ingredient_category")
    private String ingredientCategory;

    @ColumnInfo(name = "category_icon")
    private String categoryIconName;

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

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

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

    public String getCategoryIconName() {
        return categoryIconName;
    }

    public void setCategoryIconName(String categoryIconName) {
        this.categoryIconName = categoryIconName;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public Ingredient(int ingredientID, String ingredientCategory, String categoryIconName, String ingredientSubCat, String ingredientName, String ingredientAlternative) {
        this.ingredientID = ingredientID;
        this.ingredientCategory = ingredientCategory;
        this.categoryIconName = categoryIconName;
        this.ingredientSubCat = ingredientSubCat;
        this.ingredientName = ingredientName;
        this.ingredientAlternative = ingredientAlternative;
    }


}