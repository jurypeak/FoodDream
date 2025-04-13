package com.example.fooddream.controllers

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.messengers.Notification
import com.example.fooddream.views.CustomerCatalogView
import com.example.fooddream.views.CustomerSupportView
import com.example.fooddream.views.RegisterView
import com.example.fooddream.views.ResetPasswordEmailView

/**
 * LoginViewController is responsible for handling the login view logic.
 * It sets up click listeners for various UI components and manages navigation.
 *
 * @property sessionController The controller for managing user sessions.
 * @property navigationController The controller for managing navigation between views.
 * @property notification The notification manager for displaying messages to the user.
 * @property accountController The controller for managing customer account actions.
 */
class LoginViewController(
    private val sessionController: SessionController,
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val accountController: AccountController
) {

    /**
     * Initializes the login screen by checking if the user is already logged in.
     * If the user is logged in, it redirects to the CustomerCatalogView.
     *
     * @throws Exception if an error occurs while checking the user session.
     *
     * @param view The activity where the login screen is displayed.
     */
    fun initializeLoginScreen(view: Activity) {
        try {
            if (sessionController.hasUserSession()) {
                Log.d("LoginController", "User already logged in, redirecting.")
                view.startActivity(Intent(view, CustomerCatalogView::class.java))
                view.finish()
            }
        } catch (e: Exception) {
            notification.sendNotification("Error checking user session.", view)
            Log.e("LoginController", "Session check failed: ${e.message}")
        }
    }

    /**
     * Sets up click listeners for various UI components in the login view.
     *
     * @param view The activity where the login view is displayed.
     * @param registerButton The button to register a new customer account.
     * @param emailField The field for entering the email address.
     * @param passwordField The field for entering the password.
     * @param forgotPasswordButton The button to navigate to reset password page.
     * @param signUpText The text view to navigate to the registration page.
     * @param userGuideButton The button to navigate to the user guide.
     * @param customerSupportButton The button to navigate to customer support.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: Activity,
        registerButton: Button,
        emailField: EditText,
        passwordField: EditText,
        forgotPasswordButton: TextView,
        signUpText: TextView,
        userGuideButton: ImageView,
        customerSupportButton: ImageView
    ) {
        try {
            signUpText.setOnClickListener {
                navigationController.navigateToFragment(RegisterView(), R.id.fragment_container)
            }

            registerButton.setOnClickListener {
                accountController.startLogin(emailField, passwordField)
            }

            userGuideButton.setOnClickListener {
                navigationController.navigateToUserGuide(BuildConfig.URL_USERGUIDE)
            }

            customerSupportButton.setOnClickListener {
                navigationController.navigateToFragment(CustomerSupportView(), R.id.fragment_container)
            }

            forgotPasswordButton.setOnClickListener {
                navigationController.navigateToFragment(ResetPasswordEmailView(), R.id.fragment_container)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading login page.", view)
            Log.e("LoginController", "Listener setup failed: ${e.message}")
        }
    }
}
