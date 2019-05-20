package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * User database - definition of data model as it will be saved and handled in the database schema. Also a requirement that username is unique
 */
@Entity(tableName = "user", indices = [Index(value = ["user_name"], unique = true)])
class User(@field:ColumnInfo(name = "user_name")
           var userName: String?, @field:ColumnInfo(name = "pass_key")
           var passKey: String?) {

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

    @PrimaryKey(autoGenerate = true)
    var userID: Int = 0

    @ColumnInfo(name = "saved_intolerances")
    var intolerances: String? = null

    @ColumnInfo(name = "login_status")
    var isLoggedIn: Boolean = false

    @ColumnInfo(name = "saved_pantry")
    var savedIngredients: String? = null

    init {
        this.isLoggedIn = false
        this.intolerances = "[]"
        this.savedIngredients = "[]"
    }
}