package com.example.fooddream.controllers.viewControllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.views.CustomerSupportView
import com.example.fooddream.views.ResetPasswordEmailView

/**
 * RegisterViewController is responsible for handling the registration view logic.
 * It sets up click listeners for various UI components and manages navigation.
 *
 * @property navigationController The controller for managing navigation between views.
 * @property notification The notification manager for displaying messages to the user.
 * @property accountController The controller for managing customer account actions.
 */
class RegisterViewController(
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val accountController: AccountController
) {

    /**
     * Setting up click listeners for various UI components in the registration view.
     *
     * @param view The activity where the registration view is displayed.
     * @param registerButton The button to register a new customer account.
     * @param emailField The field for entering the email address.
     * @param nameField The field for entering the name.
     * @param passwordField The field for entering the password.
     * @param forgotPasswordButton The button to navigate to reset password page.
     * @param loginText The text view to navigate to the login page.
     * @param userGuideButton The button to navigate to the user guide.
     * @param customerSupportButton The button to navigate to customer support.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        registerButton: Button,
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
        forgotPasswordButton: TextView,
        loginText: TextView,
        userGuideButton: ImageView,
        customerSupportButton: ImageView
    ) {
        try {
            loginText.setOnClickListener {
                view.supportFragmentManager.popBackStack()
            }

            registerButton.setOnClickListener {
                accountController.startRegistration(emailField, nameField, passwordField)
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
            notification.sendNotification("Error while loading register page.", view)
            Log.e("RegisterViewController", "Listener setup failed: ${e.message}")
        }
    }
}
