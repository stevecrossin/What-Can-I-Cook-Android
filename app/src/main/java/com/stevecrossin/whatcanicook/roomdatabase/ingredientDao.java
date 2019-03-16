package com.stevecrossin.whatcanicook.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Update;

@Dao
public interface ingredientDao {
    @Update()
    void excludeIngredient(ingredientDao ingredientDao);
}
