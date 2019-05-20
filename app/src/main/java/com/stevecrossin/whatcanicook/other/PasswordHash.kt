package com.stevecrossin.whatcanicook.other

import java.security.MessageDigest

/***
 * This class uses MessageDigest to generate a hash for an entered string password. This takes the string data, converts them to SHA-256 hash and then converts bytes to a hexadecimal
 * I previously used part code in an Android Project to implement a login script and this code is largely the same, with some modifications
 */

object PasswordHash {

    @Throws(Exception::class)
    fun encryptPassword(data: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(data.toByteArray())
        return convertToHex(md.digest())
    }

    private fun convertToHex(bytes: ByteArray): String {
        val result = StringBuilder()
        for (b in bytes)
            result.append(Integer.toString((b and 0xff) + 0x100, 16).substring(1))
        return result.toString()
    }
}
