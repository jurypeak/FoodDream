package com.example.fooddream.controllers

import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.interfaces.ICustomerController

class CustomerController(view: AppCompatActivity): ICustomerController {

    private var accountController = AccountController(view)
    private var sessionController = SessionController(view)

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


    override fun handleResetPassword(
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

    override fun startSession() {
        sessionController.startUserSession()
    }

    override fun clearSession() {
        sessionController.clearUserSession()
    }
}