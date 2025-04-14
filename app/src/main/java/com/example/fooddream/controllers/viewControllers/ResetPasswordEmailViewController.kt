package com.example.fooddream.controllers.viewControllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.views.CustomerSupportView

/**
 * ResetPasswordEmailViewController is responsible for handling the reset password email view logic.
 * It sets up click listeners for various UI components and manages navigation.
 *
 * @property navigationController The controller for managing navigation between views.
 * @property notification The notification manager for displaying messages to the user.
 * @property customerController The controller for managing customer account actions.
 */
class ResetPasswordEmailViewController(
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val customerController: CustomerController,
) {

    /**
     * Sets up click listeners for various UI components in the reset password email view.
     *
     * @param view The activity where the reset password email view is displayed.
     * @param exitTextView The TextView for exiting the reset password email view.
     * @param emailField The EditText for entering the email address.
     * @param userGuideButton The ImageView for accessing the user guide.
     * @param submitButton The Button for submitting the reset password request.
     * @param customerSupportButton The ImageView for accessing customer support.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        exitTextView: TextView,
        emailField: EditText,
        userGuideButton: ImageView,
        submitButton: Button,
        customerSupportButton: ImageView
    ) {
        try {
            exitTextView.setOnClickListener {
                view.supportFragmentManager.popBackStack()
            }
            userGuideButton.setOnClickListener {
                navigationController.navigateToUserGuide(
                    BuildConfig.URL_USERGUIDE
                )
            }
            customerSupportButton.setOnClickListener {
                navigationController.navigateToFragment(
                    CustomerSupportView(),
                    R.id.fragment_container
                )
            }
            submitButton.setOnClickListener {
                customerController.handleResetPasswordEmailVerification(emailField)
            }
        } catch (e: Exception) {
            Log.e("ResetPasswordEmailView", "Error setting up listeners: ${e.message}")
            notification.sendNotification("Error while loading reset password page.", view)
        }
    }
}