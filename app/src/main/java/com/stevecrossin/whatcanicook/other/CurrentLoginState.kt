package com.stevecrossin.whatcanicook.other

/**
 * Handles the current login state for the application. Default state is INVALID_PASSWORD, which denies access to the application.
 * This login state is utilised by the Login activity.
 * New_User - when the Login activity checks the username and password is entered and determines it does not exist in User database, it will be noted as a new user.
 * Otherwise, it will be noted as an existing user and the class will call
 */
enum class CurrentLoginState {
    NEW_USER,
    EXISTING_USER,
    INVALID_PASSWORD,
    HASH_ERROR
}
