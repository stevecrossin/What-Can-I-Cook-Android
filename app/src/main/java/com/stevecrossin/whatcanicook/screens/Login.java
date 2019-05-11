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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevecrossin.whatcanicook.CurrentLoginState;
import com.stevecrossin.whatcanicook.PasswordHash;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.entities.Intolerance;
import com.stevecrossin.whatcanicook.entities.Pantry;
import com.stevecrossin.whatcanicook.entities.User;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

import java.lang.reflect.Type;
import java.util.List;

import static com.stevecrossin.whatcanicook.CurrentLoginState.EXISTING_USER;
import static com.stevecrossin.whatcanicook.CurrentLoginState.INVALID_PASSWORD;
import static com.stevecrossin.whatcanicook.CurrentLoginState.NEW_USER;

/**
 * A login screen that offers login via email/password. This code is based off the default Login Activity provided in Android Studio, and was modified based on that.
 * I learned how to implement this by creating a standalone app through a tutorial on YouTube I found here: https://www.youtube.com/watch?v=uV037mLG_Ps&t=676s
 */
public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getName();
    /**
     * Initialisation and declaration of objects and values. Variables and initialises. Keeps track of the Login Task state to ensure we can cancel if requested.
     */

    private LoginTask authenticationTask = null;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private View progressView;
    private View loginUIView;
    private Button loginButton;
    private CurrentLoginState currentLoginState = INVALID_PASSWORD; //Default state = Invalid Pass - deny access

    /**
     * This method executes upon creation of the activity, which sets the current views as activity_login.xml, and sets the elements to display based on the user ID.
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
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            //This is a listener which is called when any changes are made to the UI
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    tryLogin();
                    return true;
                }
                return false;
            }
        });

        //This adds a TextWatcher to the username field to ensure text input is validated as it's entered
        usernameView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /** This code block checks the text entered into the username field, in a background thread. It will first verify that the email address is valid, and then
             * query the user database with getuser to determine if the username exists or not.
             */

            @SuppressLint("StaticFieldLeak")
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                new AsyncTask<String, Void, CurrentLoginState>() {
                    @SuppressLint("WrongThread")
                    protected CurrentLoginState doInBackground(String... params) {
                        if (checkUsernameValid(params[0])) {
                            User user = new AppDataRepo(Login.this).getUserName(params[0]);
                            CurrentLoginState currentLoginState;
                            currentLoginState = (user != null) ?
                                    EXISTING_USER :
                                    NEW_USER;
                            return currentLoginState;
                        }
                        return INVALID_PASSWORD;
                    }

                    /** Once this background check is completed, the Login button text will be updated, based on whether the user exists in db or not.
                     * If the user does exist, the text will change to "Sign In", otherwise it will default to "Sign Up"
                     */
                    protected void onPostExecute(CurrentLoginState loginState) {


                        currentLoginState = loginState;
                        if (loginState == NEW_USER) {
                            loginButton.setText(getString(R.string.signup));
                        } else {
                            loginButton.setText(getString(R.string.string_signin));
                        }
                    }
                }.execute(text.toString());
            }

            //This code has no functions, but is needed for the above implemented code to work correctly
            public void afterTextChanged(Editable editable) {
            }
        });

        /**
         * This adds an onClick listener to the login button. Once the button is clicked - it will call the tryLogin method
         */
        loginButton = findViewById(R.id.loginButton);
    }


    @Override
    protected void onStart() {
        super.onStart();
        isUserSignedIn();
    }

    /**
     * This handles the attempt to login, after the button is clicked. It will only call the async task if it's not running, and store values entered in the UI as strings.
     * Username and password validation is performed, and then if they pass, login is attempted
     ***/
    private void tryLogin() {
        if (authenticationTask != null) {
            return;
        }

        // Reset errors after UI is clicked
        usernameView.setError(null);
        passwordView.setError(null);

        //Take text values in username and password and convert to string
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Perform password validation and throw errors if they are not, with login request cancelled
        if (!checkPasswordValid(password)) {
            passwordView.setError("Password is invalid");
            focusView = passwordView;
            cancel = true;
        }

        //Perform username validation to determine if it is either empty or invalid, and throw errors with login request cancelled
        if (TextUtils.isEmpty(username)) {
            usernameView.setError("Field cannot be empty");
            focusView = usernameView;
            cancel = true;
        } else if (!checkUsernameValid(username)) {
            usernameView.setError("Email address entered is invalid");
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            //There was an error, login is not attempted, don't attempt login and focus on the first field (username) error message
            focusView.requestFocus();
        } else {
            //Shows progress UI and attempts login
            showProgressUI(true);
            authenticationTask = new LoginTask(username, password);
            authenticationTask.execute((Void) null);
        }
    }

    /**
     * These are the rules to check if username and password valid, being an email must comply with the xxx@yy.zz structure with only certain values allowed, and password must be at least 6 characters
     **/
    public static boolean checkUsernameValid(String email) {
        String VALID_EMAIL_ADDRESS_REGEX = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        return email.matches(VALID_EMAIL_ADDRESS_REGEX);
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
        //private final String dietaryNeeds;

        LoginTask(String email, String password) {
            userName = email;
            passKey = password;
            //dietaryNeeds = dietary;
        }

        /**
         * Handles login or signup of the user in the background. If email is existing, sign in will be attempted, which will compare the string in the password entered,
         * and convert it to a hash using PasswordHash class, and then compare with the value that is stored in the database. If it is a match, login will be allowed and login state will change to
         * existing user.
         * <p>
         * If the user is a new user, it will pass the string of the password to PasswordHash and convert the value to a hash, and then
         * create that user in the database. They will then become an "existing_user"
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
                    e.printStackTrace();
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
                    e.printStackTrace();
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
            } else {
                passwordView.setError("Password Incorrect");
                passwordView.requestFocus();
                loginButton.setText("Sign In");
            }
        }

        /**
         * Code to execute if the background tasks are terminated.
         */
        @Override
        protected void onCancelled() {
            authenticationTask = null;
            showProgressUI(false);
        }
    }

    private void goToNextScreen() {
        finish();
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    private void onLoginSuccess(Integer userId) {
        AppDataRepo repo = new AppDataRepo(Login.this);
        repo.updateLoginStatus(userId, true);
        updateToleranceToDb();
        updatePantryToDb();
    }

    private void updatePantryToDb() {
        AppDataRepo repository = new AppDataRepo(Login.this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<com.stevecrossin.whatcanicook.entities.Pantry>>() {
        }.getType();
        List<Pantry> pantryList = gson.fromJson(repository.getSignedUser().getIntegredents(), type);

        for (com.stevecrossin.whatcanicook.entities.Pantry pantry : pantryList)
            repository.addIngredientToPantry(pantry);
    }


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
                Log.d(TAG, "Exclude ingredient: " + intolerance.getIngredientName());
            }
        }

    }

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
}