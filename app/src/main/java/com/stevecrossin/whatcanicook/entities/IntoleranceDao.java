package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IntoleranceDao {
    @Query("SELECT * FROM intolerance WHERE intolerance_name = :intoleranceName;")
    List<Intolerance> getIntoleranceByName(String intoleranceName);

    @Insert
    void addIntolerance(Intolerance intolerance);

    @Query("SELECT * FROM intolerance;")
    List<Intolerance> getAllIntolerances();

    @Query("SELECT * FROM intolerance GROUP BY intolerance_name")
    List<Intolerance> getUniqueIntolerance();

    @Query("DELETE FROM intolerance;")
    void deleteAll();

    @Query("UPDATE intolerance SET intolerance_selected = 1 WHERE intolerance_name = :intoleranceName;")
    void excludeIntolerance(String intoleranceName);

    @Query("UPDATE intolerance SET intolerance_selected = 0 WHERE intolerance_name = :intoleranceName;")
    void includeIntolerance(String intoleranceName);
}