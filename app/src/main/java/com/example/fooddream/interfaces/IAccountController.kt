package com.example.fooddream.interfaces

import android.widget.Button
import android.widget.EditText
import com.android.volley.RequestQueue

interface IAccountController {
    fun sendTwoFactorAuth(
        email: String,
        requestQueue: RequestQueue,
        url: String
    )
    fun startLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
    )
    fun startRegistration(
        registerButton: Button,
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    )
    fun resetPassword(newPassword: String, emailCode: Int): Boolean
    fun deleteAccount(): Boolean
    fun viewAccountDetails(): String
    fun editEmail(newEmail: String)
    fun editName(newFName: String, newLName: String)
    fun editPassword(newPassword: String)
    fun logout(sessionId: Int): Boolean
}