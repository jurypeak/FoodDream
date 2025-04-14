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
 * ResetPasswordViewController is responsible for handling the reset password view logic.
 * It sets up click listeners for various UI components and manages navigation.
 *
 * @property customerController The controller for managing customer account actions.
 * @property navigationController The controller for managing navigation between views.
 * @property notification The notification manager for displaying messages to the user.
 */
class ResetPasswordViewController(
    private val customerController: CustomerController,
    private val navigationController: NavigationController,
    private val notification: Notification
) {

    /**
     * Sets up click listeners for various UI components in the reset password view.
     *
     * @param view The activity where the reset password view is displayed.
     * @param exitTextView The TextView for exiting the reset password view.
     * @param passwordField The EditText for entering the new password.
     * @param userGuideButton The ImageView for accessing the user guide.
     * @param submitButton The Button for submitting the new password.
     * @param customerSupportButton The ImageView for accessing customer support.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        exitTextView: TextView,
        passwordField: EditText,
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
                customerController.handleNewResetPassword(passwordField)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading reset password page.", view)
            Log.e("ResetPasswordView", "Error setting up listeners: ${e.message}")
        }
    }
}