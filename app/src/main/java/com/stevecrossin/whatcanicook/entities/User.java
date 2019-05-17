package com.stevecrossin.whatcanicook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * User database - definition of data model as it will be saved and handled in the database schema. Also a requirement that username is unique
 */
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

    @ColumnInfo(name = "saved_pantry")
    private String savedIngredients;

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
     */

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

    public String getSavedIngredients() {
        return savedIngredients;
    }

    public void setSavedIngredients(String savedIngredients) {
        this.savedIngredients = savedIngredients;
    }

    public User(String userName, String passKey) {
        this.userName = userName;
        this.passKey = passKey;
        this.isLoggedIn = false;
        this.intolerances = "[]";
        this.savedIngredients = "[]";
    }
}