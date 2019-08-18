package com.stevecrossin.whatcanicook.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PrefManager is a simple utility class to help manage Android SharedPreference
 * SharedPreference use an XML file in the android device to manage key,value pairs
 * Written by : Fawzan
 */
public class PrefManager {

    // Shared preferences file name
    private static final String PREF_NAME = "app_preference";

    // Key to access a shared preference entry
    private static final String LAST_UPDATE_VERSION = "LAST_UPDATE_VERSION";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;

        // initialize the shared preference with context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //setter for the LAST_UPDATE_VERSION
    public void setLastUpdateVersion(int versionCode) {
        editor.putInt(LAST_UPDATE_VERSION, versionCode);
        editor.commit();
    }

    //getter for the LAST_UPDATE_VERSION
    public int getLastUpdateVersion() {

        return pref.getInt(LAST_UPDATE_VERSION, 0);

    }


}