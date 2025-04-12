package com.example.fooddream.utils

class ValidateManager {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidName(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun isValidMessage(message: String): Boolean {
        return message.isNotEmpty()
    }
}