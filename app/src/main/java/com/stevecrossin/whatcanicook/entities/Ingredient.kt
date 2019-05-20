package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Ingredients database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
class Ingredient(@field:PrimaryKey
                 @field:ColumnInfo(name = "ingredient_id")
                 var ingredientID: Int, @field:ColumnInfo(name = "ingredient_category")
                 /**
                  * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
                  * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
                  */

                 var ingredientCategory: String?, @field:ColumnInfo(name = "category_icon")
                 var categoryIconName: String?, @field:ColumnInfo(name = "ingredient_subcat")
                 var ingredientSubCat: String?, @field:ColumnInfo(name = "ingredient_name")
                 var ingredientName: String?, @field:ColumnInfo(name = "ingredient_alternative")
                 var ingredientAlternative: String?) {

    @ColumnInfo(name = "ingredient_excluded")
    var isIngredientExcluded = false

    @ColumnInfo(name = "ingredient_selected")
    var isIngredientSelected = false


}