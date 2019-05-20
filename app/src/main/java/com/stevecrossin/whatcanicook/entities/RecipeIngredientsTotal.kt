package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Recipe Ingredients Total database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
class RecipeIngredientsTotal(@field:ColumnInfo(name = "recipe_name")
                             /**
                              * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
                              * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
                              */

                             var recipeName: String?, @field:ColumnInfo(name = "total_ingredients")
                             var totalIngredient: Int) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
