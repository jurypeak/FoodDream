package com.example.fooddream.models

import android.util.Log
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.messages.Errors

class Customer(
    private var name: String,
    email: String,
    accountId: Int,
    accessLevel: Int,
    password: String
) : Account(
    email,
    accountId,
    accessLevel,
    password
) {
    var controller = AccountController(this, null)
    //Getters
    fun getName(): String = name

    //Setters
    fun setName(newName: String) {
        name = newName
    }

    fun ResetPassword(newPassword: String, emailCode: Int): Boolean {
        //TODO add a helper function to verify email codes.
        try {
            controller.setHashedPassword(newPassword)
            return true
        } catch (error: Errors.HashingException) {
            return false
            Log.d("Hashing Error", "$error")
        }
    }
}
