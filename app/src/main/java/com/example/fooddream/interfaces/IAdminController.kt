package com.example.fooddream.interfaces

import android.widget.Button
import android.widget.EditText

interface IAdminController {
    fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
    )

    fun startSession()
    fun clearSession()
}