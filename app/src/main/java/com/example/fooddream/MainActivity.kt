package com.example.fooddream

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.views.SplashScreen

/**
 * MainActivity class that serves as the entry point of the application.
 * It initializes the splash screen and starts the SplashScreen activity.
 */
class MainActivity : AppCompatActivity() {

    /**
     * onCreate method that is called when the activity is created.
     * This method initializes the splash screen and starts the SplashScreen activity.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     *
     * @throws Exception if an error occurs while starting the SplashScreen activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen_page)
        try {
            startActivity(Intent(this, SplashScreen::class.java))
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}