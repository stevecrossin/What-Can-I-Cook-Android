package com.stevecrossin.whatcanicook.utils;

public class Update {

    /*
    * We will have this value in both DB & Code.
    * Compare this with DB to decide whether to reset the db or not.
    * When I publish an app update that changes CSV files and needs DB update, increment this by one.
    *
    *  */
    public static int LAST_UPDATE_VERSION = 1;

}
