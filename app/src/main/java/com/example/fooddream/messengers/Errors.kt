package com.example.fooddream.messengers

// For custom error messages.
// A lot of this could not be implemented within the time frame of the project.
class Errors {
    class HashingException: Exception("Error occurred hashing the password.")
    class ComparingException: Exception("Error occurred comparing both passwords.")
    class DeletionException: Exception("Error occurred when deleting your account.")
    class SetException: Exception("Error occured setting a new attribute.")
    class BasketAdditionException: Exception("Error occurred when adding product to basket.")
    class ViewBasketException: Exception("Error occurred viewing basket.")
    class CreationException: Exception("Error occurred creating account.")
    class IngredientAdditionException: Exception("Error occurred adding ingredients to product.")
    class LoginException: Exception("An error occurred when logging in.")
    class VerificationException: Exception("An error occured trying to verify your email.")
}