package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user", indices = {@Index(value = {"user_name"}, unique = true)})

public class User {

    @PrimaryKey(autoGenerate = true)
    private int userID;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "pass_key")
    private String passKey;

    @ColumnInfo(name = "saved_intolerances")
    private String intolerances;

    @ColumnInfo(name = "login_status")
    private boolean isLoggedIn;


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

    public User(String userName, String passKey) {
        this.userName = userName;
        this.passKey = passKey;
        this.isLoggedIn = false;
        this.intolerances = "[]";
    }

    public String getIntolerances() {
        return intolerances;
    }

    public void setIntolerances(String intolerances) {
        this.intolerances = intolerances;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}