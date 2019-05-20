package com.stevecrossin.whatcanicook.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * LogRecords database - definition of data model as it will be saved and handled in the database schema
 */
@Entity(tableName = "logs")
class LogRecords(@field:ColumnInfo(name = "error_log")
                 var logs: String?) {

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     */

    @PrimaryKey(autoGenerate = true)
    internal var log_id: Int = 0
}
