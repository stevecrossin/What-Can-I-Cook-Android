package com.stevecrossin.whatcanicook.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo

/**
 * The main class for the activity and the main entry point after login. This is mainly a landing page for the app where all other activities are navigated from, and has very little
 * actual functionality apart from navigation and logging out of the application.
 */
class MainActivity : AppCompatActivity() {

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_main XML and load the UI elements in that XML file into that view.
     * Load Google Ads for the activity and send an adRequest to load an ad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    /**
     * This is an OnClick method that is called when the "Breakfast" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (breakfast), and then start that activity.
     */
    fun breakfastIngredients(view: View) {
        findViewById<View>(R.id.breakfastbutton)
        val breakfast = "What's for Breakfast"

        val intent = Intent(this, CategoryPicker::class.java)
        intent.putExtra("DISH_TYPE", breakfast)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Lunch" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (lunch), and then start that activity.
     */
    fun lunchIngredients(view: View) {
        findViewById<View>(R.id.lunch)
        val lunch = "What's for Lunch?"

        val intent = Intent(this, CategoryPicker::class.java)
        intent.putExtra("DISH_TYPE", lunch)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Dinner" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (dinner), and then start that activity.
     */
    fun dinnerIngredients(view: View) {
        findViewById<View>(R.id.dinner)
        val dessert = "What's for Dinner?"

        val intent = Intent(this, CategoryPicker::class.java)
        intent.putExtra("DISH_TYPE", dessert)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Dessert" button is clicked in the activity. It will load the CategoryPicker.class,
     * and pass via intent the dish type (dessert), and then start that activity.
     */
    fun dessertIngredients(view: View) {
        findViewById<View>(R.id.dessert)
        val dessert = "What's for Dessert?"

        val intent = Intent(this, CategoryPicker::class.java)
        intent.putExtra("DISH_TYPE", dessert)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Saved Recipes" button is clicked in the activity. It will load the SavedRecipes.class, and then start that activity.
     */
    fun savedRecipes(view: View) {
        val intent = Intent(this, SavedRecipes::class.java)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "My Pantry" button is clicked in the activity. It will load the Pantry.class, and then start that activity.
     */
    fun userPantry(view: View) {
        val intent = Intent(this, Pantry::class.java)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Dietary Preferences" button is clicked in the activity. It will load the Intolerances.class, and then start that activity.
     */
    fun dietaryPreferences(view: View) {
        val intent = Intent(this, Intolerances::class.java)
        startActivity(intent)
    }

    /**
     * This is an OnClick method that is called when the "Logout" button is clicked in the activity.
     * It will create a new instance of the AppDataRepo and get the signed in user, and their user ID will be updated as per the DB operation, to set isLogin for that user in
     * the DB to "false".
     *
     *
     * Once this is completed, the method will load the Login.class, and then start that activity.
     */
    @SuppressLint("StaticFieldLeak")
    fun logout(view: View) {
        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg voids: Void): Void? {
                val repo = AppDataRepo(this@MainActivity)
                val user = repo.signedUser
                repo.updateLoginStatus(user.userID, false)
                return null
            }

            override fun onPostExecute(aVoid: Void) {
                super.onPostExecute(aVoid)
                finish()
                startActivity(Intent(this@MainActivity, Login::class.java))
            }
        }.execute()
    }

    /**
     * This is an OnClick method that is called when the "About" icon is clicked in the activity. It will handle changing the view from the current view, to the About class, and then start that activity.
     */
    fun aboutApp(view: View) {
        val intent = Intent(this, About::class.java)
        startActivity(intent)
    }
}