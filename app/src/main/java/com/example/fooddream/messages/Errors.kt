package com.example.fooddream.messages

// For custom error messages.
class Errors {
    class HashingException: Exception("Error occurred hashing the password.")
    class ComparingException: Exception("Error occurred comparing both passwords.")
}