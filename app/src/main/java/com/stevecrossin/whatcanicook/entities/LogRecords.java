package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "logs")
public class LogRecords {

    //Database table structure
    @PrimaryKey(autoGenerate = true)
    private int log_id;

    @ColumnInfo(name = "error_log")
    private String logs;

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

    LogRecords(String logs) {
        this.logs = logs;
    }
}
