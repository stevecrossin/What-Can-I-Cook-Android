package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")

public class User {

    @PrimaryKey(autoGenerate = true)
    private int userID;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "pass_key")
    private String passKey;

    @ColumnInfo(name = "saved_intolerances")
    private String savedIntolerances;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public String getSavedIntolerances() {
        return savedIntolerances;
    }

    public void setSavedIntolerances(String savedIntolerances) {
        this.savedIntolerances = savedIntolerances;
    }

    public User(String userName, String passKey, String savedIntolerances) {
        this.userName = userName;
        this.passKey = passKey;
        this.savedIntolerances = savedIntolerances;
    }


}
