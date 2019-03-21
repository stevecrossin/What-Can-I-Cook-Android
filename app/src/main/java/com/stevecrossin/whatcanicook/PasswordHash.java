package com.stevecrossin.whatcanicook;

import java.security.MessageDigest;

/***
 * This class uses MessageDigest to generate a hash for an entered string password. This takes the string data, converts them to SHA-256 hash and then converts bytes to a hexadecimal
 * I previously used part code in an Android Project to implement a login script and this code is largely the same, with some modifications
 */

public class PasswordHash {

    public static String encryptPassword(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return convertToHex(md.digest());
    }

    private static String convertToHex(byte [] bytes)
    {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes)
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
