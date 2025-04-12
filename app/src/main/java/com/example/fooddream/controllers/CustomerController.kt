package com.example.fooddream.controllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.interfaces.ICustomerController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.utils.ValidateManager

class CustomerController(view: AppCompatActivity): ICustomerController {

    private var accountController = AccountController(view)
    private var sessionController = SessionController(view)
    private var validateManager = ValidateManager()
    private var notification = Notification()

    override fun handleRegistration(
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    ) {
        try {
            if (!validateManager.isValidEmail(emailField.text.toString())) {
                notification.sendNotification("Invalid email format.", emailField.context as AppCompatActivity)
                return
            }
            if (!validateManager.isValidName(nameField.text.toString())) {
                notification.sendNotification("Invalid name format.", nameField.context as AppCompatActivity)
                return
            }
            if (!validateManager.isValidPassword(passwordField.text.toString())) {
                notification.sendNotification("Invalid password format.", passwordField.context as AppCompatActivity)
                return
            }
            else {
                accountController.startRegistration(
                    emailField,
                    nameField,
                    passwordField,
                )
            }
        } catch (e: Exception) {
            Log.e("CustomerController", "Error during registration validation: ${e.message}")
        }
    }

    override fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
    ) {
        try {
            if (!validateManager.isValidEmail(emailField.text.toString())) {
                notification.sendNotification("Invalid email format.", emailField.context as AppCompatActivity)
                return
            }
            if (!validateManager.isValidPassword(passwordField.text.toString())) {
                notification.sendNotification("Invalid password format.", passwordField.context as AppCompatActivity)
                return
            }
            else {
                accountController.startLogin(
                    loginButton,
                    emailField,
                    passwordField,
                )
            }
        } catch (e: Exception) {
            Log.e("CustomerController", "Error during login: ${e.message}")
        }
    }

    override fun handleResetPasswordEmailVerification(
        emailField: EditText,
    ) {
        try {
            if (!validateManager.isValidEmail(emailField.text.toString())) {
                notification.sendNotification("Invalid email format.", emailField.context as AppCompatActivity)
                return
            }
            else {
                accountController.startResetPasswordEmailVerification(
                    emailField
                )
            }
        } catch (e: Exception) {
            Log.e("CustomerController", "Error during password reset email verification: ${e.message}")
        }
    }


    override fun handleResetPassword(
        passwordField: EditText
    ) {
        try {
            if (!validateManager.isValidPassword(passwordField.text.toString())) {
                notification.sendNotification("Invalid password format.", passwordField.context as AppCompatActivity)
                return
            }
            else {
                accountController.startResetPassword(
                    passwordField
                )
            }
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