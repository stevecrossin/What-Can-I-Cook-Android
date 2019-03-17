package com.stevecrossin.whatcanicook.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stevecrossin.whatcanicook.R;

public class SavedRecipes extends AppCompatActivity {
    /*Set view as saved recipes activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedrecipes);
    }
}
