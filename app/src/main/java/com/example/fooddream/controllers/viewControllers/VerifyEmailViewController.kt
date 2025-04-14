package com.example.fooddream.controllers.viewControllers

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification

/**
 * VerifyEmailViewController is responsible for handling the verify email view logic.
 * It sets up click listeners for various UI components and manages navigation.
 *
 * @property accountController The controller for managing customer account actions.
 * @property navigationController The controller for managing navigation between views.
 * @property notification The notification manager for displaying messages to the user.
 */
class VerifyEmailViewController(
    private val accountController: AccountController,
    private val navigationController: NavigationController,
    private val notification: Notification,
) {

    /**
     * Initializes the verify email screen by sending a two-factor authentication request.
     *
     * @param view The activity where the verify email screen is displayed.
     * @param email The email address to be verified.
     * @param typeView The type of view to be displayed.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    fun initializeVerifyEmailScreen(
        view: AppCompatActivity,
        email: String,
        typeView: String
    ) {
        try {
            accountController.sendTwoFactorAuth(
                email,
                Volley.newRequestQueue(view),
                BuildConfig.URL_VERIFY_EMAIL,
                typeView
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading verify email page.", view)
            Log.e("VerifyEmailView", "Error initializing view components", e)
        }
    }

    /**
     * Sets up click listeners for various UI components in the verify email view.
     *
     * @param view The activity where the verify email view is displayed.
     * @param exitButton The TextView for exiting the verify email view.
     * @param userGuideButton The ImageView for accessing the user guide.
     * @param customerSupportButton The ImageView for accessing customer support.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        exitButton: TextView,
        userGuideButton: ImageView,
        customerSupportButton: ImageView
    ) {
        try {
            exitButton.setOnClickListener {
                view.supportFragmentManager.popBackStack()
            }
            userGuideButton.setOnClickListener {
            }
            customerSupportButton.setOnClickListener {
                navigationController.navigateToUserGuide(
                    BuildConfig.URL_VERIFY_EMAIL
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading verify email page.", view)
            Log.e("VerifyEmailView", "Error setting up listeners", e)
        }
    }
}