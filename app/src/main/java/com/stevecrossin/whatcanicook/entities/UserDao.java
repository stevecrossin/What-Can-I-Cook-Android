package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface UserDao {
    /**
     * Defines all the operations that are carried out in the database. References UserRecord.java
     *
     * @Insert - inserts user record into the database. Conflict strategy is set to fail the insert if the userRecord already exists.
     * @Query - selects users from db where username entered matches one in db
     */

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertUser(User user);

    @Query("SELECT * from user WHERE user_name=:userName")
    User getUser(String userName);

    }
