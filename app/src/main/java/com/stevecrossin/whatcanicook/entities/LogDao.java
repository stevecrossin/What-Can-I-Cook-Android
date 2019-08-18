package com.stevecrossin.whatcanicook.entities;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogDao {

    /**
     * Selects all records from log database
     */
    @Query("SELECT * FROM logs")
    List<LogRecords> getLogs();

    /**
     * Inserts log record into the database. Conflict strategy is set to fail the insert if the record already exists.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertLog(LogRecords logDatabase);

    /**
     * Deletes all records in the log.
     */
    @Query("DELETE FROM logs")
    void clearLog();
}