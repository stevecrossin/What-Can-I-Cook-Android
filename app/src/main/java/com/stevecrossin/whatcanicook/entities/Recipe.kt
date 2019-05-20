package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable

/**
 * Recipe database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
class Recipe(@field:ColumnInfo(name = "recipe_name")
             /**
              * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
              * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
              */

             var recipeName: String?, @field:ColumnInfo(name = "recipe_image")
             var recipeImage: String?, @field:ColumnInfo(name = "recipe_ingredients")
             var recipeIngredients: String?, @field:ColumnInfo(name = "recipe_steps")
             var recipeSteps: String?) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    var recipeId: Int = 0
        internal set

    @ColumnInfo(name = "recipe_excluded")
    internal var recipeExcluded = 0

    @ColumnInfo(name = "recipe_saved")
    var isSaved: Boolean = false

    @ColumnInfo(name = "recipe_custom")
    internal var isCustomed: Boolean = false
}