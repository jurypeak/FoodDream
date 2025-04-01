package com.example.fooddream.controllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.RequestQueue
import com.example.fooddream.interfaces.IAdminController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Manager
import org.mindrot.jbcrypt.BCrypt

class AdminControllerManager(): IAdminController {

    override fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText
    ) {
        TODO("Not yet implemented")
    }

    override fun startSession() {
        TODO("Not yet implemented")
    }

    override fun clearSession() {
        TODO("Not yet implemented")
    }

}