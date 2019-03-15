package com.example.whatcanicook;

/**
 * This class contains all users that can login to the application. It also handles part of the login features
 */
public class Users {

    String username;
    String passkey;
    String savedintolerances;//These are any specified intolerances the user has already selected. This field can be null.

    //Handles login activity, as well as new user creation
    private void Login() {
        /* This method will handle the login activity. In short, when the user is presented with the login prompt with a username and password request.
        As a user adds a username in the loginactivity the app in the background will be checking, with a Listener, to see if the username exists,
        and the Sign In/Up button will alternate based on whether user exists or not.

        If an existing user, the method will take the entered password, convert to a hash, and compare with hash stored in database. If it matches, login needs to be allowed,
        otherwise an invalid password notification will need to be given. Username will be comprised of email address, and username and password rules will also apply (e.g.
        must be valid format x@y.xx, password minimum length ~6)

        If a new user, the method will take the entered password, convert to a hash, and append the database with the new user details. By default, the savedintolerances field will be null.
        */
    }

    //The below will control updating user fields. After creation of the user, the only field that can be updated is the savedintolerances.

    private void updateUser() {
        /*This method performs updates on user fields; in particular their saved intolerances. It will receive data passed in a parcel from
        the Intolerance activity, method saveIntolerances. When this data is received, this method will
        1. update (add/delete) the savedintolerances record for the current logged in user with the specified intolerances.
        They will be stored as a string inside brackets, in quotes, separated by commas e.g. "(no pork, no alcohol)".
        2. Pass back to the intolerances activity that these are now active
        */
    }
}




