package com.example.fooddream.interfaces

import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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
    fun deleteAccount(
        view: AppCompatActivity,
        requestQueue: RequestQueue,
        url: String
    )
    fun editAccountDetails(
        view: AppCompatActivity,
        fName: String,
        lName: String,
        email: String,
        password: String,
    )
    fun logout()
}