package com.example.fooddream.models

class Ingredient (
    private var ingredientName: String,
    private var ingredientId: Int,
    private var weight: Double,
    private var allergens: String,
    private var productId: Int
) {
    //Getters
    fun getIngredientName(): String = ingredientName
    fun getIngredientId(): Int = ingredientId
    fun getWeight(): Double = weight
    fun getAllergens(): String = allergens
    fun getProductId(): Int = productId

    // Setters
    fun setIngredientName(newIngredientName: String) {
        ingredientName = newIngredientName
    }
    fun setIngredientId(newIngredientId: Int) {
        ingredientId = newIngredientId
    }
    fun setWeight(newWeight: Double) {
        weight = newWeight
    }
    fun setAllergens(newAllergens: String) {
        allergens = newAllergens
    }
    fun setProductId(newProductId: Int) {
        productId = newProductId
    }
}