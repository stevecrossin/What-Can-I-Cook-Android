package com.stevecrossin.whatcanicook.screens;

import com.stevecrossin.whatcanicook.CurrentLoginState;
import com.stevecrossin.whatcanicook.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A login screen that offers login via email/password. This code is based off the default Login Activity provided in Android Studio, and was modified based on that.
 * I learned how to implement this by creating a standalone app through a tutorial on YouTube I found here: https://www.youtube.com/watch?v=uV037mLG_Ps&t=676s
 */
public class Login extends AppCompatActivity {

    /**
     * Declaration of variables
     */

    private LoginTask authenticationTask = null;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private View progressLoginView;
    private Button loginButton;
    private CurrentLoginState = INVALID_PASSWORD; //Defaults to deny access

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailView = findViewById(R.id.userNameEntry);
        passwordView = findViewById(R.id.passwordEntry);
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

        /**
         * Text Watcher for the username field. This ensures that input is validated as user enters text.
         */
        usernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }





}