package com.example.fooddream.interfaces

import android.widget.Button
import android.widget.EditText

interface ICustomerController {
    fun handleRegistration(
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    )

    fun handleResetPasswordEmailVerification(
        emailField: EditText
    )

    fun handleResetPassword(
        passwordField: EditText
    )

    fun startSession()
    fun clearSession()
}

