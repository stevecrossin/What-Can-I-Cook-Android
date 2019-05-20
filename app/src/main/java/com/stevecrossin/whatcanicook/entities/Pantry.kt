package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Pantry database - definition of data model as it will be saved and handled in the database schema
 */
@Entity
class Pantry(@field:PrimaryKey
             @field:ColumnInfo(name = "ingredient_id")
             /**
              * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
              * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
              */

             var ingredientId: Int)