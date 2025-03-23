package com.example.fooddream.controllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.RequestQueue
import com.example.fooddream.interfaces.IAccountController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Manager
import org.mindrot.jbcrypt.BCrypt

class AdminController(
    private var manager: Manager
): IAccountController {
    // Function for encrypting password with BCrypt algorithm
    private fun hashPassword(password:String) {
        try {
            val salt = BCrypt.gensalt(12)
            manager.setPassword(BCrypt.hashpw(password, salt))
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
    }
    // Function to check inputted password against stored hashed password.
    private fun verifyPassword(
        password:String,
        hashedPassword:String
    ): Boolean {
        return try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (error: Errors.ComparingException) {
            Log.d("Password Comparing Error", "$error")
            false
        }
    }
    // Function to hash passwords without showing the algorithm
    override fun setHashedPassword(password: String): Boolean {
        return try {
            hashPassword(password).toString()
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }
    // Function to allow users to login into their account.
    override fun login(
        email:String,
        password:String,
        requestQueue: RequestQueue,
        url: String
    ) {
        //TODO Login needs database to check against username.
    }

    override fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
        url: String
    ) {
        TODO("Not yet implemented")
    }

    // Function that closes users sessions and logs users out.
    override fun logout(
        sessionId: Int
    ): Boolean {
        //TODO Logout needs sessions to be implemented.
        return false
    }
}