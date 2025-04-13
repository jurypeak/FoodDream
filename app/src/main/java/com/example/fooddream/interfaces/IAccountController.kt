package com.example.fooddream.interfaces

import android.widget.EditText
import com.android.volley.RequestQueue

interface IAccountController {
    fun sendTwoFactorAuth(
        email: String,
        requestQueue: RequestQueue,
        url: String,
        typeView: String
    )
    fun startLogin(
        emailField: EditText,
        passwordField: EditText,
    )
    fun startRegistration(
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    )
    fun startResetPasswordProcess(
        emailField: EditText,
    )
    fun validateNewResetPassword(
        passwordField: EditText
    )
    fun deleteAccount(): Boolean
    fun viewAccountDetails(): String
    fun editEmail(newEmail: String)
    fun editName(newFName: String, newLName: String)
    fun editPassword(newPassword: String)
    fun logout(sessionId: Int): Boolean
}