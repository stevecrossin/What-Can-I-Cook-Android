package com.stevecrossin.whatcanicook.screens

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.entities.Pantry
import com.stevecrossin.whatcanicook.entities.User
import com.stevecrossin.whatcanicook.other.CurrentLoginState
import com.stevecrossin.whatcanicook.other.CurrentLoginState.*
import com.stevecrossin.whatcanicook.other.PasswordHash
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo

/**
 * A login screen that offers login via email/password. Performs various checks to ensure input is valid, and loads saved user data into the relevant databases once login is successful.
 */
class Login : AppCompatActivity() {

    /**
     * Initialisation and declaration of objects and values. Variables and initialises. Keeps track of the Login Task state to ensure we can cancel if requested.
     */
    private var authenticationTask: LoginTask? = null
    private var usernameView: AutoCompleteTextView? = null
    private var passwordView: EditText? = null
    private var progressView: View? = null
    private var loginUIView: View? = null
    private var loginButton: Button? = null
    private var currentLoginState = INVALID_PASSWORD
    internal var repo = AppDataRepo(this@Login)

    /**
     * On start of the activity, checks if the user has previously logged in, and has not logged out. This will check if there are any users in the DB with isLoggedIn = true. If so, it will skip the login activity and will navigate directly to MainActivity (as defined lower in code)
     */
    override fun onStart() {
        super.onStart()
        isUserSignedIn()
    }

    private fun checkPasswordValid(password: String): Boolean {
        return password.length > 6
    }

    /**
     * This method executes upon creation of the activity, which sets the current views as activity_login.xml, and sets the elements to display based on the user ID.
     * Initialises MobileAds for the application.
     * Sets an Action listener for the password view.
     * Also adds a TextWatcher to the username field to ensure text input is validated as it's entered
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        MobileAds.initialize(this, "ca-app-pub-6486258628588307~4051321968")
        loginUIView = findViewById(R.id.login_form)
        progressView = findViewById(R.id.login_progress)
        usernameView = findViewById(R.id.userNameEntry)
        passwordView = findViewById(R.id.passwordEntry)
        loginButton = findViewById(R.id.loginButton)
        passwordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                tryLogin()
                return@OnEditorActionListener true
            }
            false
        })

        usernameView!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}


            /**
             * This code block checks the text entered into the username field, in a background thread. It will first verify that the email address is valid, and then
             * query the user database with getUserName to determine if the username exists or not.
             */
            @SuppressLint("StaticFieldLeak")
            override fun onTextChanged(text: CharSequence, i: Int, i1: Int, i2: Int) {
                object : AsyncTask<String, Void, CurrentLoginState>() {
                    @SuppressLint("WrongThread")
                    override fun doInBackground(vararg params: String): CurrentLoginState {
                        if (isValidEmail(params[0])) {
                            val user = AppDataRepo(this@Login).getUserName(params[0])
                            val currentLoginState: CurrentLoginState
                            currentLoginState = if (user != null)
                                EXISTING_USER
                            else
                                NEW_USER
                            return currentLoginState
                        }
                        return INVALID_PASSWORD
                    }

                    /**
                     * Once this background check is completed, the Login button text will be updated, based on whether the user exists in db or not.
                     * If the user does exist, the text will change to "Sign In", otherwise it will default to "Sign Up"
                     */
                    override fun onPostExecute(loginState: CurrentLoginState) {

                        currentLoginState = loginState
                        if (loginState == NEW_USER) {
                            loginButton!!.text = getString(R.string.signup)
                        } else if (loginState == EXISTING_USER) {
                            loginButton!!.text = getString(R.string.string_signin)
                        } else {
                            loginButton!!.text = getString(R.string.signup)
                        }
                    }
                }.execute(usernameView!!.text.toString())
            }

            /**
             * This code has no functions, but is needed for the above implemented code to work correctly
             */
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    /**
     * Method called to check if user is signed in, and the operations to perform.
     * First, an async task starts which sets the progressView to visible. It will then create a new instance of AppDataRepo
     * and call the getSignedUser method which will query the DB to see if any users are currently flagged as logged in.
     *
     *
     * After this background task is executed, it will hide the progress view and determine if any users were noted as logged in. If one was (only one can be logged in at a time) it will call the goToNextScreen method which will load the next activity.
     *
     *
     * Otherwise, onClick of the login button, it will call the tryLoginn method which will attempt login.
     */
    @SuppressLint("StaticFieldLeak")
    private fun isUserSignedIn() {
        object : AsyncTask<Void, Void, User>() {

            override fun onPreExecute() {
                super.onPreExecute()
                progressView!!.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg voids: Void): User {
                val repo = AppDataRepo(this@Login)
                return repo.signedUser
            }

            override fun onPostExecute(user: User?) {
                super.onPostExecute(user)
                progressView!!.visibility = View.GONE
                if (user != null) {
                    goToNextScreen()
                } else {
                    loginButton!!.setOnClickListener { tryLogin() }
                }
            }
        }.execute()
    }

    /**
     * After login is successful, this will finish the activity, and start the MainActivity class, which will then perform its respective operations.
     */
    private fun goToNextScreen() {
        finish()
        startActivity(Intent(this@Login, MainActivity::class.java))
    }

    /**
     * This handles the attempt to login, after the login/sign in button is clicked. It will only call the async task if it's not running.
     *
     *
     * By default, no UI errors are displayed and once the UI error has been displayed, if any, when the user interface is clicked, any errors will be hidden/removed
     *
     *
     * Once the user has entered all their input, and the login button has been clicked, it will convert the text in the username and password fields to a string.
     * It will then determine perform password and username validation and depending on the rules, e.g. if it was empty or the format doesn't match the rules, errors will be thrown and
     * the request to login will be cancelled and an error message will be displayed.
     */
    private fun tryLogin() {
        if (authenticationTask != null) {
            return
        }

        usernameView!!.error = null
        passwordView!!.error = null

        val username = usernameView!!.text.toString()
        val password = passwordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!checkPasswordValid(password)) {
            passwordView!!.error = "Password is invalid"
            focusView = passwordView
            cancel = true
        }
        if (TextUtils.isEmpty(username)) {
            usernameView!!.error = "Field cannot be empty"
            focusView = usernameView
            cancel = true
        } else if (!isValidEmail(username)) {
            usernameView!!.error = "Email address entered is invalid"
            focusView = usernameView
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            showProgressUI(true)
            authenticationTask = LoginTask(username, password)
            authenticationTask!!.execute(null as Void?)
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgressUI(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        loginUIView!!.visibility = if (show) View.GONE else View.VISIBLE
        loginUIView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginUIView!!.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        progressView!!.visibility = if (show) View.VISIBLE else View.GONE
        progressView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                progressView!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    inner class LoginTask internal constructor(private val userName: String, private val passKey: String) : AsyncTask<Void, Void, CurrentLoginState>() {

        /**
         * Handles login or signup of the user in the background. If email is existing, sign in will be attempted, which will compare the string in the password entered,
         * and convert it to a hash using PasswordHash class, and then compare with the value that is stored in the database. If it is a match, login will be allowed and login state will change to
         * existing user.
         *
         *
         * If the user is a new user, it will pass the string of the password to PasswordHash and convert the value to a hash, and then
         * create that user in the database. They will then become an "existing_user"
         *
         *
         * However, if there is no match, the default status is INVALID_PASSWORD which will deny login. If for some reason the application fails to get a password hash (this will never happen, but in case), an exception is logged
         * and a toast will be displayed, as noted in onPostExecute.
         */
        override fun doInBackground(vararg params: Void): CurrentLoginState {
            if (currentLoginState == EXISTING_USER) {
                try {
                    val hashPass = PasswordHash.encryptPassword(passKey)
                    val user = AppDataRepo(this@Login).getUserName(userName)
                    if (user.passKey == hashPass) {
                        onLoginSuccess(user.userID)
                        return EXISTING_USER
                    }
                } catch (e: Exception) {
                    AppDataRepo(this@Login).insertLogs("Error getting password hash")
                    return HASH_ERROR
                }

            } else if (currentLoginState == NEW_USER)
                try {
                    val hashPass = PasswordHash.encryptPassword(passKey)
                    val repo = AppDataRepo(this@Login)
                    repo.createUser(User(userName, hashPass))
                    onLoginSuccess(repo.getUserName(userName).userID)
                    return EXISTING_USER
                } catch (e: Exception) {
                    AppDataRepo(this@Login).insertLogs("Error getting password hash")
                    return HASH_ERROR
                }

            return INVALID_PASSWORD
        }

        /**
         * Actions will take place in the UI based on the result of this background thread CurrentLoginState.
         * If the authentication against existing user was successful, the activity will end and MainActivity will load, otherwise an error will be thrown that the password is incorrect.
         */
        override fun onPostExecute(success: CurrentLoginState) {
            authenticationTask = null
            showProgressUI(false)

            if (success == EXISTING_USER) {
                goToNextScreen()
            } else if (success == HASH_ERROR) {
                Toast.makeText(this@Login, "Error getting password hash", Toast.LENGTH_SHORT).show()
            } else {
                passwordView!!.error = "Password Incorrect"
                passwordView!!.requestFocus()
                loginButton!!.text = getString(R.string.sign_in_text)
            }
        }

        /**
         * Code to execute if the background tasks are terminated. Will hide the progressUI.
         */
        override fun onCancelled() {
            authenticationTask = null
            showProgressUI(false)
        }
    }

    /**
     * On successful login, this method will take the user ID, and create a new instance of AppDataRepo.
     * It will then update the login status for that user to true (isLoggedIn), and then execute the updatePantrytoDb and updateToleranceToDb methods
     */
    private fun onLoginSuccess(userId: Int?) {
        val repo = AppDataRepo(this@Login)
        repo.updateLoginStatus(userId!!, true)
        updateToleranceToDb()
        updatePantryToDb()
    }

    /**
     * Method to load users saved pantry from JSON into the DB after the user has been flagged as signed in via onLoginSuccess. User pantry ingredients are saved in a JSON file with the savePantryToUserDB method defined in AppDataRepo.
     * This performs the reverse function.
     *
     *
     * It will create a new instance of AppDataRepo, get the currentSignedIn user and get the contents of the JSON for that user, and use GSON to convert the values to Java. It will then update these values into the pantry via the addIngredientToPantry method
     */
    private fun updatePantryToDb() {
        val repository = AppDataRepo(this@Login)
        val gson = Gson()
        val type = object : TypeToken<List<com.stevecrossin.whatcanicook.entities.Pantry>>() {

        }.type
        val pantryList = gson.fromJson<List<Pantry>>(repository.signedUser.savedIngredients, type)

        for (pantry in pantryList)
            repository.addIngredientToPantry(pantry)
    }

    /**
     * Method to load users intolerances from JSON into the DB after the user has been flagged as signed in via onLoginSuccess. User intolerances are saved in a JSON file with the savePantryToUserDB method defined in AppDataRepo.
     * This performs the reverse function.
     *
     *
     * It will create a new instance of AppDataRepo, get the currentSignedIn user and get the contents of the JSON for that user,
     * and use GSON to convert the values to Java. It will then update these values into the intolerances database by calling the excludeIngredient and excludeRecipe methods
     */
    private fun updateToleranceToDb() {
        val repository = AppDataRepo(this@Login)
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        val toleranceList = gson.fromJson<List<String>>(repository.signedUser.intolerances, type)

        for (intoleranceName in toleranceList) {
            repository.excludeIntolerance(intoleranceName)
            val list = repository.getIntoleranceByName(intoleranceName)
            for (intolerance in list) {
                repository.excludeIngredient(intolerance.ingredientName)
                repository.excludeRecipe(intolerance.ingredientName)
            }
        }
    }

    companion object {

        /**
         * Rule to check if username and password valid, being an email must comply with the xxx@yy.zz structure with only certain values allowed, and password must be at least 6 characters
         */
        fun isValidEmail(target: CharSequence): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }


}