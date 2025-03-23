package com.example.fooddream.interfaces

import android.widget.Button
import android.widget.EditText
import com.android.volley.RequestQueue

interface IAccountController {
    fun setHashedPassword(password: String): Boolean
    fun login(email: String,
              password: String,
              requestQueue: RequestQueue,
              url: String
    )
    fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
        url: String
    )
    fun logout(sessionId: Int): Boolean
}