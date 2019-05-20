package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface IntoleranceDao {

    /**
     * Get all intolerances from the database
     */
    @get:Query("SELECT * FROM intolerance;")
    val allIntolerances: List<Intolerance>

    /**
     * Get all unique intolerances from the database and group them by name
     */
    @get:Query("SELECT * FROM intolerance GROUP BY intolerance_name")
    val uniqueIntolerance: List<Intolerance>

    /**
     * Get all intolerances in the database based on the name passed
     */
    @Query("SELECT * FROM intolerance WHERE intolerance_name = :intoleranceName;")
    fun getIntoleranceByName(intoleranceName: String): List<Intolerance>

    /**
     * Add an intolerance to the database
     */
    @Insert
    fun addIntolerance(intolerance: Intolerance)

    /**
     * Delete all intolerances from the database
     */
    @Query("DELETE FROM intolerance;")
    fun deleteAll()

    /**
     * Set an intolerance in the database to active (selected) based on the intolerance name passed via the operation
     */
    @Query("UPDATE intolerance SET intolerance_selected = 1 WHERE intolerance_name = :intoleranceName;")
    fun excludeIntolerance(intoleranceName: String)

    /**
     * Set an intolerance in the database to inactive (unselected) based on the intolerance name passed via the operation
     */
    @Query("UPDATE intolerance SET intolerance_selected = 0 WHERE intolerance_name = :intoleranceName;")
    fun includeIntolerance(intoleranceName: String)
}