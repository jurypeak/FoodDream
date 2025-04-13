package com.example.fooddream.utils

/**
 * ValidateManager is a utility class that provides methods to validate user input.
 * It includes methods for validating email, password, name, and message.
 */
class ValidateManager() {

    /**
     * Validates the provided email address.
     *
     * @param email The email address to validate.
     * @return True if the email is valid, false otherwise.
     */
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Validates the provided password.
     *
     * @param password The password to validate.
     * @return True if the password is valid, false otherwise.
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Validates the provided name.
     *
     * @param name The name to validate.
     * @return True if the name is valid, false otherwise.
     */
    fun isValidName(name: String): Boolean {
        return name.isNotEmpty()
    }

    /**
     * Validates the provided message.
     *
     * @param message The message to validate.
     * @return True if the message is valid, false otherwise.
     */
    fun isValidMessage(message: String): Boolean {
        return message.isNotEmpty()
    }
}