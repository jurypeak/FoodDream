package com.example.fooddream.controllers

import android.app.Activity
import android.util.Log
import com.example.fooddream.models.Account
import com.example.fooddream.messages.Errors
import org.mindrot.jbcrypt.BCrypt

// Create model and view.
class AccountController (
    private var model: Account,
    private var view: Activity?
    ){

    // Function for encrypting password with BCrypt algorithm
    private fun hashPassword(password:String) {
        try {
            val salt = BCrypt.gensalt(12)
            model.setPassword(BCrypt.hashpw(password, salt))
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
    fun setHashedPassword(password: String): Boolean {
        return try {
            hashPassword(password).toString()
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }
    // Function to allow users to login into their account.
    fun login(
        email:String,
        password:String
    ): Boolean {
        //TODO Login needs database to check against username.
        return true
    }

    // Function that closes users sessions and logs users out.
    fun logout(
        sessionId: Int
    ): Boolean {
        //TODO Logout needs sessions to be implemented.
        return false
    }
}