package com.stevecrossin.whatcanicook.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.stevecrossin.whatcanicook.entities.Ingredient;
import com.stevecrossin.whatcanicook.entities.Intolerance;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IntoleranceDao {
    @Query("SELECT ingredient_name FROM intolerance WHERE intolerance_name = :intoleranceName;")
    List<String> getIngredientsByIntolerance(String intoleranceName);

    @Insert
    void addIntolerances(ArrayList<Intolerance> intolerances);
    @Query("SELECT * FROM intolerance;")
    List<Intolerance> getAllIntolerances();

}
