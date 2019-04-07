package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LogDao {

    /**
     * Define all the operations that will be carried out in the database
     *
     * @Query (select) - selects all records from log database
     * @Insert - inserts log record into the database. Conflict strategy is set to fail the insert if the record already exists.
     * @Query (delete) - deletes all records in the log.
     */

    @Query("SELECT * FROM logs")
    List<LogRecords> getLogs();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertLog(LogRecords logDatabase);

    @Query("DELETE FROM logs")
    void clearLog();
}