package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Intolerances database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
class Intolerance(@field:ColumnInfo(name = "intolerance_name")
                  /**
                   * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
                   * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
                   */

                  var intoleranceName: String?, @field:ColumnInfo(name = "ingredient_name")
                  var ingredientName: String?) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "intolerance_id")
    var intoleranceID: Int = 0

    @ColumnInfo(name = "intolerance_selected")
    var isIntoleranceSelected: Boolean = false

    init {
        this.isIntoleranceSelected = false
    }
}