package com.example.fooddream.controllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.messengers.CustomerSupport
import com.example.fooddream.messengers.Notification

/**
 * CustomerSupportViewController is responsible for managing the customer support view.
 * It handles the click events for various UI components and interacts with the CustomerSupport class
 * to submit tickets and navigate to the user guide.
 *
 * @param navigationController The controller responsible for navigation actions.
 * @param notification The notification manager for displaying messages to the user.
 * @param customerSupport The customer support manager for handling ticket submissions.
 */
class CustomerSupportViewController(
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val customerSupport: CustomerSupport
) {

    /**
     * Set up click listeners for the customer support view components.
     *
     * @param view The activity where the components are located.
     * @param emailField The EditText for entering the email.
     * @param exitTextView The TextView for exiting the customer support view.
     * @param submitButton The Button for submitting the support request.
     * @param supportButton The ImageView for accessing user guide.
     * @param messageField The EditText for entering the message.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */

    fun setupClickListeners(
        view: AppCompatActivity,
        emailField: EditText,
        exitTextView: TextView,
        submitButton: Button,
        supportButton: ImageView,
        messageField: EditText,

    ) {
        try {
            exitTextView.setOnClickListener {
                view.supportFragmentManager.popBackStack()
            }
            supportButton.setOnClickListener {
                navigationController.navigateToUserGuide(
                    BuildConfig.URL_USERGUIDE
                )
            }
            submitButton.setOnClickListener {
                customerSupport.submitTicket(
                    emailField.text.toString(),
                    messageField.text.toString(),
                    view
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occured loading customer support.", view)
            Log.d("Customer Support Error", "Error setting up listeners: ${e.message}")
        }
    }
}
