package com.stevecrossin.whatcanicook.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.Pantry;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.other.CurrentLoginState;
import com.stevecrossin.whatcanicook.other.PasswordHash;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.lang.reflect.Type;
import java.util.List;

import static com.stevecrossin.whatcanicook.other.CurrentLoginState.EXISTING_USER;
import static com.stevecrossin.whatcanicook.other.CurrentLoginState.HASH_ERROR;
import static com.stevecrossin.whatcanicook.other.CurrentLoginState.INVALID_PASSWORD;
import static com.stevecrossin.whatcanicook.other.CurrentLoginState.NEW_USER;

public class Login extends AppCompatActivity {

    /**
     * Initialisation and declaration of objects and values. Variables and initialises. Keeps track of the Login Task state to ensure we can cancel if requested.
     */
    private LoginTask authenticationTask = null;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private View progressView;
    private View loginUIView;
    private Button loginButton;
    private CurrentLoginState currentLoginState = INVALID_PASSWORD;
    AppDataRepo repo = new AppDataRepo(Login.this);

    /**
     * On start of the activity, checks if the user has previously logged in, and has not logged out. This will check if there are any users in the DB with isLoggedIn = true. If so, it will skip the login activity and will navigate directly to MainActivity (as defined lower in code)
     */
    @Override
    protected void onStart() {
        super.onStart();
        isUserSignedIn();
    }

    /**
     * This method executes upon creation of the activity, which sets the current views as activity_login.xml, and sets the elements to display based on the user ID.
     * Initialises MobileAds for the application.
     * Sets an Action listener for the password view.
     * Also adds a TextWatcher to the username field to ensure text input is validated as it's entered
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobileAds.initialize(this, "ca-app-pub-6486258628588307~4051321968");
        loginUIView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        usernameView = findViewById(R.id.userNameEntry);
        passwordView = findViewById(R.id.passwordEntry);
        loginButton = findViewById(R.id.loginButton);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    tryLogin();
                    return true;
                }
                return false;
            }
        });

        usernameView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            /**
             * This code block checks the text entered into the username field, in a background thread. It will first verify that the email address is valid, and then
             * query the user database with getUserName to determine if the username exists or not.
             */
            @SuppressLint("StaticFieldLeak")
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                new AsyncTask<String, Void, CurrentLoginState>() {
                    @SuppressLint("WrongThread")
                    protected CurrentLoginState doInBackground(String... params) {
                        if (isValidEmail(params[0])) {
                            User user = new AppDataRepo(Login.this).getUserName(params[0]);
                            CurrentLoginState currentLoginState;
                            currentLoginState = (user != null) ?
                                    EXISTING_USER :
                                    NEW_USER;
                            return currentLoginState;
                        }
                        return INVALID_PASSWORD;
                    }

                    /**
                     * Once this background check is completed, the Login button text will be updated, based on whether the user exists in db or not.
                     * If the user does exist, the text will change to "Sign In", otherwise it will default to "Sign Up"
                     */
                    protected void onPostExecute(CurrentLoginState loginState) {

                        currentLoginState = loginState;
                        if (loginState == NEW_USER) {
                            loginButton.setText(getString(R.string.signup));
                        } else if (loginState == EXISTING_USER) {
                            loginButton.setText(getString(R.string.string_signin));
                        } else {
                            loginButton.setText(getString(R.string.signup));
                        }
                    }
                }.execute(usernameView.getText().toString());
            }

            /**
             * This code has no functions, but is needed for the above implemented code to work correctly
             **/
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * Method called to check if user is signed in, and the operations to perform.
     * First, an async task starts which sets the progressView to visible. It will then create a new instance of AppDataRepo
     * and call the getSignedUser method which will query the DB to see if any users are currently flagged as logged in.
     * <p>
     * After this background task is executed, it will hide the progress view and determine if any users were noted as logged in. If one was (only one can be logged in at a time) it will call the goToNextScreen method which will load the next activity.
     * <p>
     * Otherwise, onClick of the login button, it will call the tryLoginn method which will attempt login.
     */
    @SuppressLint("StaticFieldLeak")
    private void isUserSignedIn() {
        new AsyncTask<Void, Void, User>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressView.setVisibility(View.VISIBLE);
            }

            @Override
            protected User doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(Login.this);
                return repo.getSignedUser();
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                progressView.setVisibility(View.GONE);
                if (user != null) {
                    goToNextScreen();
                } else {
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tryLogin();
                        }
                    });
                }
            }
        }.execute();
    }

    /**
     * After login is successful, this will finish the activity, and start the MainActivity class, which will then perform its respective operations.
     */
    private void goToNextScreen() {
        finish();
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    /**
     * This handles the attempt to login, after the login/sign in button is clicked. It will only call the async task if it's not running.
     * <p>
     * Once the user has entered all their input, and the login button has been clicked, it will convert the text in the username and password fields to a string.
     * It will then determine perform password and username validation and depending on the rules, e.g. if it was empty or the format doesn't match the rules, errors will be thrown and
     * the request to login will be cancelled and an error message will be displayed.
     * <p>
     * Finally, once the UI error has been displayed, if any, when the user interface is clicked, any errors will be hidden/removed
     ***/
    private void tryLogin() {
        if (authenticationTask != null) {
            return;
        }

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!checkPasswordValid(password)) {
            passwordView.setError("Password is invalid");
            focusView = passwordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            usernameView.setError("Field cannot be empty");
            focusView = usernameView;
            cancel = true;
        } else if (!isValidEmail(username)) {
            usernameView.setError("Email address entered is invalid");
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgressUI(true);
            authenticationTask = new LoginTask(username, password);
            authenticationTask.execute((Void) null);
        }

        usernameView.setError(null);
        passwordView.setError(null);
    }

    /**
     * Rule to check if username and password valid, being an email must comply with the xxx@yy.zz structure with only certain values allowed, and password must be at least 6 characters
     **/
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean checkPasswordValid(String password) {
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgressUI(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginUIView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginUIView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginUIView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    public class LoginTask extends AsyncTask<Void, Void, CurrentLoginState> {

        private final String userName;
        private final String passKey;

        LoginTask(String email, String password) {
            userName = email;
            passKey = password;
        }

        /**
         * Handles login or signup of the user in the background. If email is existing, sign in will be attempted, which will compare the string in the password entered,
         * and convert it to a hash using PasswordHash class, and then compare with the value that is stored in the database. If it is a match, login will be allowed and login state will change to
         * existing user.
         * <p>
         * If the user is a new user, it will pass the string of the password to PasswordHash and convert the value to a hash, and then
         * create that user in the database. They will then become an "existing_user"
         * <p>
         * However, if there is no match, the default status is INVALID_PASSWORD which will deny login. If for some reason the application fails to get a password hash (this will never happen, but in case), an exception is logged
         * and a toast will be displayed, as noted in onPostExecute.
         */
        @Override
        protected CurrentLoginState doInBackground(Void... params) {
            if (currentLoginState == EXISTING_USER) {
                try {
                    String hashPass = PasswordHash.encryptPassword(passKey);
                    User user = new AppDataRepo(Login.this).getUserName(userName);
                    if (user.getPassKey().equals(hashPass)) {
                        onLoginSuccess(user.getUserID());
                        return EXISTING_USER;
                    }
                } catch (Exception e) {
                    new AppDataRepo(Login.this).insertLogs("Error getting password hash");
                    return HASH_ERROR;
                }
            } else if (currentLoginState == NEW_USER)
                try {
                    String hashPass = PasswordHash.encryptPassword(passKey);
                    AppDataRepo repo = new AppDataRepo(Login.this);
                    repo.createUser(new User(userName, hashPass));
                    onLoginSuccess(repo.getUserName(userName).getUserID());
                    return EXISTING_USER;
                } catch (Exception e) {
                    new AppDataRepo(Login.this).insertLogs("Error getting password hash");
                    return HASH_ERROR;
                }
            return INVALID_PASSWORD;
        }

        /**
         * Actions will take place in the UI based on the result of this background thread CurrentLoginState.
         * If the authentication against existing user was successful, the activity will end and MainActivity will load, otherwise an error will be thrown that the password is incorrect.
         */
        @Override
        protected void onPostExecute(final CurrentLoginState success) {
            authenticationTask = null;
            showProgressUI(false);

            if (success == EXISTING_USER) {
                goToNextScreen();
            } else if (success == HASH_ERROR) {
                Toast.makeText(Login.this, "Error getting password hash", Toast.LENGTH_SHORT).show();
            } else {
                passwordView.setError("Password Incorrect");
                passwordView.requestFocus();
                loginButton.setText(getString(R.string.sign_in_text));
            }
        }

        /**
         * Code to execute if the background tasks are terminated. Will hide the progressUI.
         */
        @Override
        protected void onCancelled() {
            authenticationTask = null;
            showProgressUI(false);
        }
    }

    /**
     * On successful login, this method will take the user ID, and create a new instance of AppDataRepo.
     * It will then update the login status for that user to true (isLoggedIn), and then execute the updatePantrytoDb and updateToleranceToDb methods
     */
    private void onLoginSuccess(Integer userId) {
        AppDataRepo repo = new AppDataRepo(Login.this);
        repo.updateLoginStatus(userId, true);
        updateToleranceToDb();
        updatePantryToDb();
    }

    /**
     * Method to load users saved pantry from JSON into the DB after the user has been flagged as signed in via onLoginSuccess. User pantry ingredients are saved in a JSON file with the savePantryToUserDB method defined in AppDataRepo.
     * This performs the reverse function.
     * <p>
     * It will create a new instance of AppDataRepo, get the currentSignedIn user and get the contents of the JSON for that user, and use GSON to convert the values to Java. It will then update these values into the pantry via the addIngredientToPantry method
     */
    private void updatePantryToDb() {
        AppDataRepo repository = new AppDataRepo(Login.this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<com.stevecrossin.whatcanicook.entities.Pantry>>() {
        }.getType();
        List<Pantry> pantryList = gson.fromJson(repository.getSignedUser().getSavedIngredients(), type);

        for (com.stevecrossin.whatcanicook.entities.Pantry pantry : pantryList)
            repository.addIngredientToPantry(pantry);
    }

    /**
     * Method to load users intolerances from JSON into the DB after the user has been flagged as signed in via onLoginSuccess. User intolerances are saved in a JSON file with the savePantryToUserDB method defined in AppDataRepo.
     * This performs the reverse function.
     * <p>
     * It will create a new instance of AppDataRepo, get the currentSignedIn user and get the contents of the JSON for that user,
     * and use GSON to convert the values to Java. It will then update these values into the intolerances database by calling the excludeIngredient and excludeRecipe methods
     */
    private void updateToleranceToDb() {
        AppDataRepo repository = new AppDataRepo(Login.this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> toleranceList = gson.fromJson(repository.getSignedUser().getIntolerances(), type);

        for (String intoleranceName : toleranceList) {
            repository.excludeIntolerance(intoleranceName);
            List<Intolerance> list = repository.getIntoleranceByName(intoleranceName);
            for (Intolerance intolerance : list) {
                repository.excludeIngredient(intolerance.getIngredientName());
                repository.excludeRecipe(intolerance.getIngredientName());
            }
        }
    }


}