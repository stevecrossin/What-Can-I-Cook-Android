package com.stevecrossin.whatcanicook.other

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.stevecrossin.whatcanicook.R
import com.stevecrossin.whatcanicook.roomdatabase.AppDataRepo
import com.stevecrossin.whatcanicook.screens.Login
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Class which covers the splash screen of the application and its initialisation
 */
class SplashScreenInit : AppCompatActivity() {

    /**
     * On creation of the activity, the toolbar will be removed, the splash screen will be displayed as full screen and the current view will be set to the contents of
     * activity_splash screen XML layout. It will then create an instance of the splash screen and start it.
     *
     *
     * Code has been rewritten - using RXjava to perform tasks, the application will setup the application databases - by loading all files from CSV into their relevant databases
     * by creating an instance of DBPopulatorUtil which has all this data migrated into one place, and then executing the relevant functions in that
     * file in separate cases and then breaking, first loading intolerances, then ingredients, and then recipes.
     *
     *
     * No further processing is needed for these files, hence why the onSubscribe, onNext have no information as they will never be called. OnError has an error to output to the end user,
     * but given the task reads from and loads from static values this will never occur, and only has a function to insert an error for formatting purposes.
     *
     *
     * Once the application has performed all these operations, the Splash Screen Activity will end, and the Login Activity will start.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_SplashTheme)
        super.onCreate(savedInstanceState)
        setupDB()
    }

    private fun setupDB() {
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .doOnNext { integer ->
                    val util = DBPopulatorUtil()
                    when (integer) {
                        1 -> util.loadIntolerancesToDb(this@SplashScreenInit)
                        2 -> util.loadIngredientsTODb(this@SplashScreenInit)

                        3 -> util.loadRecipesFromCsvToDB(this@SplashScreenInit)

                        else -> {
                        }
                    }
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(integer: Int?) {}

                    override fun onError(e: Throwable) {
                        val repo = AppDataRepo(this@SplashScreenInit)
                        repo.insertLogs("Error occured with DB operation")
                        Toast.makeText(this@SplashScreenInit, "Error occurred with task to load files to DB", Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {
                        val intent = Intent(this@SplashScreenInit, Login::class.java)
                        startActivity(intent)
                        this@SplashScreenInit.finish()
                    }
                })

    }

}
