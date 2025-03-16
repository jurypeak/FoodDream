package com.example.fooddream.models

// Open class for inheritance.
open class Account(
    private var email: String,
    private var accountId: Int,
    private var accessLevel: Int,
    private var password: String
) {
    // Getters
    fun getEmail(): String = email
    fun getAccountId(): Int = accountId
    fun getAccessLevel(): Int = accessLevel
    fun getPassword(): String = password

    // Setters
    fun setEmail(newEmail: String) {
        email = newEmail
    }
    fun setAccountId(newAccountId: Int) {
        accountId = newAccountId
    }
    fun setAccessLevel(newAccessLevel: Int) {
        accessLevel = newAccessLevel
    }
    fun setPassword(newPassword: String) {
        password = newPassword
    }
}
