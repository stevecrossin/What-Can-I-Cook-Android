package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IntoleranceDao {

    /**
     * Get all intolerances in the database based on the name passed
     */
    @Query("SELECT * FROM intolerance WHERE intolerance_name = :intoleranceName;")
    List<Intolerance> getIntoleranceByName(String intoleranceName);

    /**
     * Add an intolerance to the database
     */
    @Insert
    void addIntolerance(Intolerance intolerance);

    /**
     * Get all intolerances from the database
     */
    @Query("SELECT * FROM intolerance;")
    List<Intolerance> getAllIntolerances();

    /**
     * Get all unique intolerances from the database and group them by name
     */
    @Query("SELECT * FROM intolerance GROUP BY intolerance_name")
    List<Intolerance> getUniqueIntolerance();

    /**
     * Delete all intolerances from the database
     */
    @Query("DELETE FROM intolerance;")
    void deleteAll();

    /**
     * Set an intolerance in the database to active (selected) based on the intolerance name passed via the operation
     */
    @Query("UPDATE intolerance SET intolerance_selected = 1 WHERE intolerance_name = :intoleranceName;")
    void excludeIntolerance(String intoleranceName);

    /**
     * Set an intolerance in the database to inactive (unselected) based on the intolerance name passed via the operation
     */
    @Query("UPDATE intolerance SET intolerance_selected = 0 WHERE intolerance_name = :intoleranceName;")
    void includeIntolerance(String intoleranceName);
}