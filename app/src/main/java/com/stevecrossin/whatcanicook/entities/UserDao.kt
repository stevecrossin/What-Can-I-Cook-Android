package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface UserDao {

    /**
     * Selects all users from the database that are currently logged in
     */
    @get:Query("SELECT * from user WHERE login_status= 1")
    val signInUser: User

    /**
     * Inserts user record into the database. Conflict strategy is set to fail the insert if the userRecord already exists.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertUser(user: User)

    /**
     * Selects users from database where username entered matches one in db
     */
    @Query("SELECT * from user WHERE user_name=:userName")
    fun getUser(userName: String): User

    /**
     * Sets the loginStatus for the user based on the based on the user ID given
     */
    @Query("UPDATE user SET login_status = :isLogin  WHERE userID = :userId;")
    fun updateLoginStatus(userId: Int, isLogin: Boolean)

    /**
     * Set saved_intolerances values for the given user record, based on the fields in intolerances DB when the operation is called for the logged in user
     */
    @Query("UPDATE user SET saved_intolerances = :value  WHERE login_status = 1")
    fun updateIntoleranceValue(value: String)

    /**
     * Set saved_pantry values for the given user record, based on the fields in pantry DB when the operation is called for the logged in user
     */
    @Query("UPDATE user SET saved_pantry = :value  WHERE login_status = 1")
    fun updatePantryValue(value: String)
}