package com.example.fooddream.controllers

import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.interfaces.ICustomerController

/**
 * CustomerController is responsible for handling customer-related actions in the application.
 * It manages user registration, password reset, and session management.
 *
 * @param view The AppCompatActivity context for displaying notifications and managing UI interactions.
 */
class CustomerController(view: AppCompatActivity): ICustomerController {

    private var accountController = AccountController(view)

    /**
     * NavigationController instance to manage navigation actions.
     * This instance is used to navigate between different views in the application.
     *
     * @property view The AppCompatActivity context used for navigation.
     *
     * @exception Exception if there is a error with the method that occurs.
     */
    override fun handleRegistration(
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    ) {
        try {
            accountController.startRegistration(
                emailField,
                nameField,
                passwordField
            )
        } catch (e: Exception) {
            Log.e("CustomerController", "Error during registration validation: ${e.message}")
        }
    }

    /**
     * Handles the password reset process by validating the email address.
     * This method is called when the user requests a password reset.
     *
     * @param emailField The EditText field containing the user's email address.
     *
     * @exception Exception if there is a error with the method that occurs.
     */
    override fun handleResetPasswordEmailVerification(
        emailField: EditText,
    ) {
        try {
            accountController.startResetPasswordProcess(
                emailField
            )
        } catch (e: Exception) {
            Log.e("CustomerController", "Error during password reset email verification: ${e.message}")
        }
    }

    /**
     * Handles the new password validation process.
     * This method is called when the user enters a new password for resetting.
     *
     * @param passwordField The EditText field containing the new password.
     *
     * @exception Exception if there is a error with the method that occurs.
     */
    override fun handleNewResetPassword(
        passwordField: EditText
    ) {
        try {
            accountController.validateNewResetPassword(
                passwordField
            )
        } catch (e: Exception) {
            Log.e("CustomerController", "Error during password reset: ${e.message}")
        }
    }
}