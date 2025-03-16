package com.example.fooddream.messengers

// For custom error messages.
class Errors {
    class HashingException: Exception("Error occurred hashing the password.")
    class ComparingException: Exception("Error occurred comparing both passwords.")
    class DeletionException: Exception("Error occurred when deleting your account.")
    class SetException: Exception("Error occured setting a new attribute.")
    class BasketAdditionException: Exception("Error occurred when adding product to basket.")
    class ViewBasketException: Exception("Error occurred viewing basket.")
    class CreationException: Exception("Error occurred creating account.")
    class IngredientAdditionException: Exception("Error occurred adding ingredients to product.")
}