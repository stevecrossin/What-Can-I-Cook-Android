package com.stevecrossin.whatcanicook.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "app_preference";
    private static final String LAST_UPDATE_VERSION = "LAST_UPDATE_VERSION";


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLastUpdateVersion(int versionCode) {
        editor.putInt(LAST_UPDATE_VERSION, versionCode);
        editor.commit();
    }

    public int getLastUpdateVersion() {

        return pref.getInt(LAST_UPDATE_VERSION, 0);

    }


}