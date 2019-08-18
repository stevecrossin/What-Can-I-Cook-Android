package com.stevecrossin.whatcanicook.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * LogRecords database - definition of data model as it will be saved and handled in the database schema
 */
@Entity(tableName = "logs")
public class LogRecords {

    @PrimaryKey(autoGenerate = true)
    private int log_id;

    @ColumnInfo(name = "error_log")
    private String logs;

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     */

    int getLog_id() {
        return log_id;
    }

    public String getLogs() {
        return logs;
    }

    void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public LogRecords(String logs) {
        this.logs = logs;
    }
}
