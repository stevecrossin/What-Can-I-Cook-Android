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
import com.stevecrossin.whatcanicook.CurrentLoginState;
import com.stevecrossin.whatcanicook.PasswordHash;
import com.stevecrossin.whatcanicook.R;
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo;

/**
 * A login screen that offers login via email/password. This code is based off the default Login Activity provided in Android Studio, and was modified based on that.
 * I learned how to implement this by creating a standalone app through a tutorial on YouTube I found here: https://www.youtube.com/watch?v=uV037mLG_Ps&t=676s
 */
public class Login extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

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
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             * Listener which is called when an action is performed
             **/
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        /** Add TextWatcher to ensure input is validated as it is entered.**/
        mEmailView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * /** check if valid user input exist as a user*
             * Perform this in a background thread, and perform verification that the input is a valid email address.
             * Then query user records to determine if user record actually exists.
             */
            @SuppressLint("StaticFieldLeak")
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {

            }


            /**
             * Nothing is performed in below, but is needed for beforeTextChanged method to function
             **/
            public void afterTextChanged(Editable editable) {
            }
        });



        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        /** Store values at the time of the login attempt.**/
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

    }
}
