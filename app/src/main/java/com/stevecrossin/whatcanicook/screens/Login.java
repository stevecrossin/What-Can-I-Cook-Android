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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stevencrossin.contactstest.CredAndPass;
import com.example.stevencrossin.contactstest.R;
import com.example.stevencrossin.contactstest.LoginState;
import com.example.stevencrossin.contactstest.roomdb.AppDataRepository;
import com.example.stevencrossin.contactstest.roomdb.UserRecord;
import com.stevecrossin.whatcanicook.CurrentLoginState;
import com.stevecrossin.whatcanicook.PasswordHash;
import com.stevecrossin.whatcanicook.screens.MainActivity;

/**
 * A login screen that offers login via email/password. This code is based off the default Login Activity provided in Android Studio, and was modified based on that.
 * I learned how to implement this by creating a standalone app through a tutorial on YouTube I found here: https://www.youtube.com/watch?v=uV037mLG_Ps&t=676s
 */
public class Login extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private CurrentLoginState currentLoginState = CurrentLoginState.INVALID_PASSWORD; //Default state = Invalid Pass - deny access

    /**
     * Method which executes when activity is initialised. Sets the current view as activity.login.xml, and sets the UI elements based on ID.
     * Sets up the login form and the elements.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**Listener which is called when an action is performed**/
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        /** Add TextWatcher to ensure input is validated as it is entered.**/
        mEmailView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /** check if valid user input exist as a user*
             * Perform this in a background thread, and perform verification that the input is a valid email address.
             * Then query user records to determine if user record actually exists.*/
            @SuppressLint("StaticFieldLeak")
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                new AsyncTask<String, Void, CurrentLoginState>() {
                    protected CurrentLoginState doInBackground(String... params) {
                        if (isEmailValid(params[0])) {
                            UserRecord userRecord = new AppDataRepo(Login.this).getUser(params[0]);
                            return (userRecord != null) ?
                                    CurrentLoginState.EXISTING_USER :
                                    CurrentLoginState.NEW_USER;
                        }
                        return LoginState.INVALID_PASSWORD;
                    }

                    protected void onPostExecute(LoginState loginState) {
                        /** update the signIn button based on background result.
                         * If the user exists, the button will change to "Sign in" otherwise will remain as "sign up"
                         */

                        currentLoginState = loginState;
                        if (loginState == LoginState.NEW_USER) {
                            mEmailSignInButton.setText(R.string.sign_up);
                        } else {
                            mEmailSignInButton.setText(R.string.action_sign_in_short);
                        }
                    }
                }.execute(text.toString());
            }

            /**Nothing is performed in below, but is needed for beforeTextChanged method to function**/
            public void afterTextChanged(Editable editable) {
            }
        });

        /** add a listener to signUp button**/
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Perform attempt to login or signUp**/
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * This method handles login attempts - when the login button is clicked.
     ***/
    private void attemptLogin() {
        /**Only call the asyncTask when it is not running**/
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        /** Store values at the time of the login attempt.**/
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /** Check for a valid password, if the user entered one.**/
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        /** Check for a valid email address.**/
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            /**There was an error; don't attempt login and focus on the first field (username) with an error message.**/
            focusView.requestFocus();
        } else {
            /** Show a progress spinner, and kick off a background task to
             * perform the user login attempt.**/
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Rules to check if email and password are valid
     **/
    private boolean isEmailValid(String email) {
        return Login.isValidEmail(email);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, CurrentLoginState> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        /**
         * Handles login or signup of the user in the background as DB operations are best done in the background.
         * If email is detected as an existing user, sign-in attempt will be performed, which will take the entered password and convert to hash
         * then find existing user, and compare with stored hash in DB. If they match, login will be performed, and login state will change to existing user.
         * If it is a new user, it will pass the string of the password to CredAndPass, which converts to hash and then gets that hash. It then will create the user in the db
         * and user will become an "existing user"
         */
        @Override
        protected CurrentLoginState doInBackground(Void... params) {
            if (currentLoginState == CurrentLoginState.EXISTING_USER) {
                try {
                    String hashPass = CredAndPass.sha256(mPassword);
                    UserRecord userRecord = new AppDataRepository(LoginActivity.this).getUser(mEmail);
                    if (userRecord.getPass_key().equals(hashPass))
                        return LoginState.EXISTING_USER;
                } catch (Exception e) {
                    new AppDataRepository(LoginActivity.this).insertLogs("error with getting password hash ");
                }
            } else if (currentLoginState == LoginState.NEW_USER) {
                try {
                    String hashPass = PasswordHash.sha256(mPassword);
                    new AppDataRepository(LoginActivity.this).createUser(new UserRecord(mEmail, hashPass));
                    return LoginState.EXISTING_USER;
                } catch (Exception e) {
                    new AppDataRepository(LoginActivity.this).insertLogs("error with getting password hash ");
                }

            }
            return CurrentLoginState.INVALID_PASSWORD;
        }

        /**
         * Execute actions in the UI based on the result of the background thread. If authentication against an existing user record is successful, this
         * activity will end, and the ContactDetails Activity will start. If the password is incorrect, an error is thrown and incorrect password error is shown
         */
        @Override
        protected void onPostExecute(final CurrentLoginState success) {
            mAuthTask = null;
            showProgress(false);

            if (success == CurrentLoginState.EXISTING_USER) {
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                mEmailSignInButton.setText(R.string.title_activity_login);
            }
        }

        /**
         * Code executed if the task running in background is terminated for any reason.
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}

