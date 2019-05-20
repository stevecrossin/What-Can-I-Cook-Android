package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface LogDao {

    /**
     * Selects all records from log database
     */
    @get:Query("SELECT * FROM logs")
    val logs: List<LogRecords>

    /**
     * Inserts log record into the database. Conflict strategy is set to fail the insert if the record already exists.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertLog(logDatabase: LogRecords)

    /**
     * Deletes all records in the log.
     */
    @Query("DELETE FROM logs")
    fun clearLog()
}